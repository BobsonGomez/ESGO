package com.esgo.backend.service;

import com.esgo.backend.dto.BrsrReportRequest;
import com.esgo.backend.model.Report;
import com.esgo.backend.model.User;
import com.esgo.backend.repository.ReportRepository;
import com.esgo.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature; // Import this
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static javax.swing.text.html.CSS.Attribute.FONT_FAMILY;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String COLOR_BLUE_HEADER = "0070C0";

    private static final String COLOR_THEME_GREEN = "548235";
    private static final String COLOR_BORDER = "CCCCCC";

    // --- DB OPERATIONS ---
    public Long saveReportToDb(BrsrReportRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user == null) return null;

        Report report = null;

        if (request.getId() != null) {
            report = reportRepository.findById(request.getId()).orElse(null);
            if (report != null && !report.getUser().getId().equals(user.getId())) {
                report = null;
            }
        }

        if (report == null) {
            report = new Report();
            report.setUser(user);
            report.setCreatedDate(LocalDate.now());
            report.setReportName("Draft Report");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            // FIX 1: Ignore fields in DB that don't match Java class (prevents crash on old reports)
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

            BrsrReportRequest dataToSave;

            if (report.getReportDataJson() != null && !report.getReportDataJson().isEmpty()) {
                BrsrReportRequest existingData = mapper.readValue(report.getReportDataJson(), BrsrReportRequest.class);
                mapper.readerForUpdating(existingData).readValue(mapper.writeValueAsString(request));
                dataToSave = existingData;
            } else {
                dataToSave = request;
            }

            if (dataToSave.getCompanyName() != null && !dataToSave.getCompanyName().isEmpty()) {
                report.setReportName(dataToSave.getCompanyName() + " Report");
            }

            String jsonString = mapper.writeValueAsString(dataToSave);
            report.setReportDataJson(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Report savedReport = reportRepository.save(report);

        if (!user.hasGeneratedReport()) {
            user.setHasGeneratedReport(true);
            userRepository.save(user);
        }

        return savedReport.getId();
    }

    public List<Report> getUserReports(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reportRepository.findByUserOrderByIdDesc(user);
    }

    public BrsrReportRequest getReportDataById(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow();
        try {
            ObjectMapper mapper = new ObjectMapper();
            // FIX 2: Ignore unknown properties here too
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.readValue(report.getReportDataJson(), BrsrReportRequest.class);
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace to see what happened
            throw new RuntimeException("Failed to parse report data: " + e.getMessage());
        }
    }

    public void deleteReport(Long id, String username) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
        if (!report.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }
        reportRepository.delete(report);
    }

    // ==================================================================================
    //                         WORD DOCUMENT GENERATION LOGIC
    // ==================================================================================

    public ByteArrayInputStream generateBrsrReport(BrsrReportRequest data) throws IOException {
        try (XWPFDocument doc = new XWPFDocument()) {
            // --- TITLE ---
            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            title.setSpacingAfter(400);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("BUSINESS RESPONSIBILITY & SUSTAINABILITY REPORT");
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.setColor(COLOR_THEME_GREEN);
            titleRun.setFontFamily("Calibri");

            // --- SECTION A: GENERAL DISCLOSURES ---
            addSectionHeader(doc, "SECTION A: GENERAL DISCLOSURES");
            addBoldText(doc, "I. Details of the listed entity");

            XWPFTable table = doc.createTable();
            table.setWidth("100%");

            // Set specific column widths (30% for Labels, 70% for Data) to look better
            CTTblGrid grid = table.getCTTbl().addNewTblGrid();
            grid.addNewGridCol().setW(BigInteger.valueOf(3000));
            grid.addNewGridCol().setW(BigInteger.valueOf(7000));

            XWPFTableRow headerRow = (table.getRow(0) != null) ? table.getRow(0) : table.createRow();
            ensureCells(headerRow, 2);
            styleCell(headerRow.getCell(0), "S. No. Particulars", true);
            styleCell(headerRow.getCell(1), "Details", true);

            // Data Rows 1-12
            addRow(table, "1. Corporate Identity Number (CIN)", data.getCin());
            addRow(table, "2. Name of the Listed Entity", data.getCompanyName());
            addRow(table, "3. Year of incorporation", data.getYearOfInc());
            addRow(table, "4. Registered office address", data.getRegisteredAddress());
            addRow(table, "5. Corporate address", data.getCorporateAddress());
            addRow(table, "6. E-mail", data.getEmail());
            addRow(table, "7. Telephone", data.getTelephone());
            addRow(table, "8. Website", data.getWebsite());
            addRow(table, "9. Financial Year", data.getReportingYear());

            String stockData = checkNull(data.getStockExchanges());
            stockData = stockData.replaceAll("\\s+([a-z]\\.)", "\n$1");
            addRow(table, "10. Stock Exchange(s)", stockData);

            addRow(table, "11. Paid-up Capital", data.getPaidUpCapital());

            String contactInfo = String.format("%s\n%s\nAddress: %s\nTel: %s | Email: %s",
                    checkNull(data.getContactPersonName()), checkNull(data.getContactPersonDesignation()),
                    checkNull(data.getContactPersonAddress()), checkNull(data.getContactPersonTelephone()),
                    checkNull(data.getContactPersonEmail()));
            addRow(table, "12. Contact Person details", contactInfo);

            // --- ADDED 13, 14, 15 to the Table ---

            // 13. Reporting Boundary (Question text added to label)
            String q13Label = "13. Reporting boundary - Are the disclosures under this report made on a standalone basis (i.e. only for the entity) or on a consolidated basis?";
            addRow(table, q13Label, checkNull(data.getReportingBoundary()));

            // 14. Assurance Provider
            addRow(table, "14. Name of assurance provider", checkNull(data.getAssuranceProviderName()));

            // 15. Assurance Type
            addRow(table, "15. Type of assurance obtained", checkNull(data.getAssuranceType()));

            doc.createParagraph().createRun().addBreak();

            // --- II. Products/services ---
            doc.createParagraph().createRun().addBreak(); // Ensure spacing from previous section
            addBoldText(doc, "II. Products/services");

            // Item 16 Text (Regular + Italic part)
            XWPFParagraph p16 = doc.createParagraph();
            XWPFRun r16a = p16.createRun();
            r16a.setText("16. Details of business activities ");
            r16a.setBold(true); // <-- Added bold
            r16a.setFontFamily(FONT_FAMILY.toString()); r16a.setFontSize(10); // FIX: Added .toString()
            XWPFRun r16b = p16.createRun();
            r16b.setText("(accounting for 90% of the turnover):");
            r16b.setItalic(true);
            r16b.setFontFamily(FONT_FAMILY.toString()); r16b.setFontSize(10); // FIX: Added .toString()

            // FIX: Renamed to tableProduct16 to avoid conflict with Section III, and use doc.createTable()
            XWPFTable tableProduct16 = doc.createTable();

            // Set Column Widths: S.No(10%), Main(35%), Business(35%), %(20%)
            CTTblGrid grid16 = tableProduct16.getCTTbl().addNewTblGrid();
            grid16.addNewGridCol().setW(BigInteger.valueOf(1000));
            grid16.addNewGridCol().setW(BigInteger.valueOf(3500));
            grid16.addNewGridCol().setW(BigInteger.valueOf(3500));
            grid16.addNewGridCol().setW(BigInteger.valueOf(2000));

            // FIX: Use your existing helper addDynamicHeaderRow
            addDynamicHeaderRow(tableProduct16, new String[]{"S. No.", "Description of Main Activity", "Description of Business Activity", "% of Turnover of the entity"});

            if (data.getBusinessActivities() != null && !data.getBusinessActivities().isEmpty()) {
                int i = 1;
                for (BrsrReportRequest.BusinessActivity ba : data.getBusinessActivities()) {
                    // FIX: Use your existing helper addDynamicRow
                    addDynamicRow(tableProduct16, new String[]{String.valueOf(i++), checkNull(ba.getDescriptionMain()), checkNull(ba.getDescriptionBusiness()), checkNull(ba.getTurnoverPercentage()) + "%"});
                }
            } else { addDynamicRow(tableProduct16, new String[]{"-", "-", "-", "-"}); }

            doc.createParagraph().createRun().addBreak();

            // Item 17 Text
            XWPFParagraph p17 = doc.createParagraph();
            XWPFRun r17a = p17.createRun();
            r17a.setText("17. Products/Services sold by the entity ");
            r17a.setBold(true); // <-- Added bold
            r17a.setFontFamily(FONT_FAMILY.toString()); r17a.setFontSize(10); // FIX: Added .toString()

            // FIX: Rename variable to avoid conflict later
            XWPFRun rProduct17b = p17.createRun();
            rProduct17b.setText("(accounting for 90% of the entity’s Turnover):");
            rProduct17b.setItalic(true);
            rProduct17b.setFontFamily(FONT_FAMILY.toString()); rProduct17b.setFontSize(10); // FIX: Added .toString()

            // FIX: Renamed to tableProduct17 to avoid conflict with Section III, and use doc.createTable()
            XWPFTable tableProduct17 = doc.createTable();

            // Set Column Widths: S.No(10%), Product(40%), NIC(20%), %(30%)
            CTTblGrid grid17 = tableProduct17.getCTTbl().addNewTblGrid();
            grid17.addNewGridCol().setW(BigInteger.valueOf(1000));
            grid17.addNewGridCol().setW(BigInteger.valueOf(4000));
            grid17.addNewGridCol().setW(BigInteger.valueOf(2000));
            grid17.addNewGridCol().setW(BigInteger.valueOf(3000));

            // Logic for Consolidated vs Standalone headers
            String[] headers17;
            if (data.isIncludeConsolidated()) {
                headers17 = new String[]{"S. No.", "Product/Service", "NIC Code", "Turnover (Stand.)", "%", "Turnover (Cons.)", "%"};
                grid17 = tableProduct17.getCTTbl().getTblGrid();
            } else {
                headers17 = new String[]{"S. No.", "Product/Service", "NIC Code", "% of total Turnover contributed"};
            }

            // FIX: Use your existing helper addDynamicHeaderRow
            addDynamicHeaderRow(tableProduct17, headers17);

            if (data.getProductsServices() != null && !data.getProductsServices().isEmpty()) {
                int i = 1;
                for (BrsrReportRequest.ProductService ps : data.getProductsServices()) {
                    if (data.isIncludeConsolidated()) {
                        // FIX: Use your existing helper addDynamicRow
                        addDynamicRow(tableProduct17, new String[]{
                                String.valueOf(i++),
                                checkNull(ps.getProductName()),
                                checkNull(ps.getNicCode()),
                                checkNull(ps.getTurnoverStandalone()),
                                checkNull(ps.getPercentageStandalone()) + "%",
                                checkNull(ps.getTurnoverConsolidated()),
                                checkNull(ps.getPercentageConsolidated()) + "%"
                        });
                    } else {
                        // FIX: Use your existing helper addDynamicRow
                        addDynamicRow(tableProduct17, new String[]{
                                String.valueOf(i++),
                                checkNull(ps.getProductName()),
                                checkNull(ps.getNicCode()),
                                checkNull(ps.getPercentageStandalone()) + "%"
                        });
                    }
                }
            } else {
                int colCount = data.isIncludeConsolidated() ? 7 : 4;
                String[] emptyRow = new String[colCount];
                for(int k=0; k<colCount; k++) emptyRow[k] = "-";
                // FIX: Use your existing helper addDynamicRow
                addDynamicRow(tableProduct17, emptyRow);
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION III: OPERATIONS =================
            addSectionHeader(doc, "III. Operations");

            // 18. Number of locations
            addBoldText(doc, "18. Number of locations:");
            XWPFTable table18 = doc.createTable();
            table18.setWidth("100%");

            String[] headers18 = {"Location", "Number of Plants", "Number of Offices", "Total"};
            addDynamicHeaderRow(table18, headers18); // Uses your existing header styler

            addDynamicRow(table18, new String[]{ "National", checkNull(data.getPlantsNational()), checkNull(data.getOfficesNational()), checkNull(data.getTotalNational()) });
            addDynamicRow(table18, new String[]{ "International", checkNull(data.getPlantsInternational()), checkNull(data.getOfficesInternational()), checkNull(data.getTotalInternational()) });

            doc.createParagraph().createRun().addBreak();

            // 19. Markets served by the entity
            addBoldText(doc, "19. Markets served by the entity:");

            // a. Locations Table
            addBoldText(doc, "a. Number of locations");
            XWPFTable table19Markets = doc.createTable();
            table19Markets.setWidth("100%");

            // Apply the exact same dynamic stylers to Table 19
            String[] headers19Markets = {"Locations", "Number"};
            addDynamicHeaderRow(table19Markets, headers19Markets);

            addDynamicRow(table19Markets, new String[]{"National (No. of States)", checkNull(data.getLocationsNationalNumber())});
            addDynamicRow(table19Markets, new String[]{"International (No. of Countries)", checkNull(data.getLocationsInternationalNumber())});

            doc.createParagraph().createRun().addBreak();

            // b. Contribution of exports (AI Output formatted cleanly)
            addBoldText(doc, "b. What is the contribution of exports as a percentage of total turnover?");
            XWPFParagraph p19b = doc.createParagraph();
            p19b.setSpacingAfter(200);
            // Uses your existing setTextWithBreaks to handle Gemini's paragraph formatting flawlessly
            setTextWithBreaks(p19b.createRun(), checkNull(data.getContributionExports()));

            // c. Types of customers (AI Output formatted cleanly)
            addBoldText(doc, "c. A brief on types of customers:");
            XWPFParagraph p19c = doc.createParagraph();
            p19c.setSpacingAfter(200);
            setTextWithBreaks(p19c.createRun(), checkNull(data.getTypesOfCustomers()));

            doc.createParagraph().createRun().addBreak();

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION IV: EMPLOYEES =================
            addSectionHeader(doc, "IV. Employees");

            addBoldText(doc, "20. Details as at the end of Financial Year:");
            addBoldText(doc, "a. Employees and workers (including differently abled):");

            XWPFTable table18a = doc.createTable();
            table18a.setWidth("100%");
            setTableBorders(table18a); // FIX: Ensures gridlines are visible

// FIX: Set strict column widths (Particulars wide, numbers narrow)
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid grid18a = table18a.getCTTbl().addNewTblGrid();
            grid18a.addNewGridCol().setW(java.math.BigInteger.valueOf(3000)); // Particulars
            grid18a.addNewGridCol().setW(java.math.BigInteger.valueOf(1400)); // Total (A)
            grid18a.addNewGridCol().setW(java.math.BigInteger.valueOf(1400)); // Male (No)
            grid18a.addNewGridCol().setW(java.math.BigInteger.valueOf(1400)); // Male (%)
            grid18a.addNewGridCol().setW(java.math.BigInteger.valueOf(1400)); // Female (No)
            grid18a.addNewGridCol().setW(java.math.BigInteger.valueOf(1400)); // Female (%)

            String[] headers18a = {"Particulars", "Total (A)", "Male (No)", "Male (%)", "Female (No)", "Female (%)"};
            addDynamicHeaderRow(table18a, headers18a);

            addSectionTitleRow(table18a, "EMPLOYEES", 6);
            String[] empPerm = { "Permanent (D)", checkNull(data.getEmpPermTotal()), checkNull(data.getEmpPermMaleNo()), checkNull(data.getEmpPermMalePerc())+"%", checkNull(data.getEmpPermFemaleNo()), checkNull(data.getEmpPermFemalePerc())+"%" };
            String[] empTemp = { "Other than Permanent (E)", checkNull(data.getEmpTempTotal()), checkNull(data.getEmpTempMaleNo()), checkNull(data.getEmpTempMalePerc())+"%", checkNull(data.getEmpTempFemaleNo()), checkNull(data.getEmpTempFemalePerc())+"%" };
            addDynamicRow(table18a, empPerm);
            addDynamicRow(table18a, empTemp);
            addDynamicRow(table18a, calculateTotalRow("Total Employees (D+E)", empPerm, empTemp));

            addSectionTitleRow(table18a, "WORKERS", 6);
            String[] workPerm = { "Permanent (F)", checkNull(data.getWorkPermTotal()), checkNull(data.getWorkPermMaleNo()), checkNull(data.getWorkPermMalePerc())+"%", checkNull(data.getWorkPermFemaleNo()), checkNull(data.getWorkPermFemalePerc())+"%" };
            String[] workTemp = { "Other than Permanent (G)", checkNull(data.getWorkTempTotal()), checkNull(data.getWorkTempMaleNo()), checkNull(data.getWorkTempMalePerc())+"%", checkNull(data.getWorkTempFemaleNo()), checkNull(data.getWorkTempFemalePerc())+"%" };
            addDynamicRow(table18a, workPerm);
            addDynamicRow(table18a, workTemp);
            addDynamicRow(table18a, calculateTotalRow("Total Workers (F+G)", workPerm, workTemp));

// Adds the AI generated note cleanly below the table
            addNote(doc, data.getEmployeeNotesA());

            doc.createParagraph().createRun().addBreak();

            addBoldText(doc, "b. Differently abled Employees and workers:");
            XWPFTable table18b = doc.createTable();
            table18b.setWidth("100%");
            setTableBorders(table18b); // FIX: Ensures gridlines are visible

// Apply the exact same column widths to Table B
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid grid18b = table18b.getCTTbl().addNewTblGrid();
            grid18b.addNewGridCol().setW(java.math.BigInteger.valueOf(3000));
            grid18b.addNewGridCol().setW(java.math.BigInteger.valueOf(1400));
            grid18b.addNewGridCol().setW(java.math.BigInteger.valueOf(1400));
            grid18b.addNewGridCol().setW(java.math.BigInteger.valueOf(1400));
            grid18b.addNewGridCol().setW(java.math.BigInteger.valueOf(1400));
            grid18b.addNewGridCol().setW(java.math.BigInteger.valueOf(1400));

            addDynamicHeaderRow(table18b, headers18a);

            addSectionTitleRow(table18b, "DIFFERENTLY ABLED EMPLOYEES", 6);
            addDynamicRow(table18b, new String[]{"Permanent (D)", checkNull(data.getDaEmpPermTotal()), checkNull(data.getDaEmpPermMaleNo()), checkNull(data.getDaEmpPermMalePerc())+"%", checkNull(data.getDaEmpPermFemaleNo()), checkNull(data.getDaEmpPermFemalePerc())+"%"});
            addDynamicRow(table18b, new String[]{"Other than Permanent (E)", checkNull(data.getDaEmpTempTotal()), checkNull(data.getDaEmpTempMaleNo()), checkNull(data.getDaEmpTempMalePerc())+"%", checkNull(data.getDaEmpTempFemaleNo()), checkNull(data.getDaEmpTempFemalePerc())+"%"});

            addSectionTitleRow(table18b, "DIFFERENTLY ABLED WORKERS", 6);
            addDynamicRow(table18b, new String[]{"Permanent (F)", checkNull(data.getDaWorkPermTotal()), checkNull(data.getDaWorkPermMaleNo()), checkNull(data.getDaWorkPermMalePerc())+"%", checkNull(data.getDaWorkPermFemaleNo()), checkNull(data.getDaWorkPermFemalePerc())+"%"});
            addDynamicRow(table18b, new String[]{"Other than Permanent (G)", checkNull(data.getDaWorkTempTotal()), checkNull(data.getDaWorkTempMaleNo()), checkNull(data.getDaWorkTempMalePerc())+"%", checkNull(data.getDaWorkTempFemaleNo()), checkNull(data.getDaWorkTempFemalePerc())+"%"});

// Adds the AI generated note cleanly below the table
            addNote(doc, data.getEmployeeNotesB());

            doc.createParagraph().createRun().addBreak();

            // --- 21. Women Representation ---
            addBoldText(doc, "21. Participation/Inclusion/Representation of women:");
            XWPFTable table19 = doc.createTable();
            table19.setWidth("100%");
            setTableBorders(table19); // FIX: Ensures gridlines are visible

            // FIX: Set specific column widths to prevent ugly stretching
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid grid19 = table19.getCTTbl().addNewTblGrid();
            grid19.addNewGridCol().setW(java.math.BigInteger.valueOf(4000)); // Category (Wide)
            grid19.addNewGridCol().setW(java.math.BigInteger.valueOf(2000)); // Total (A)
            grid19.addNewGridCol().setW(java.math.BigInteger.valueOf(2000)); // Female (B)
            grid19.addNewGridCol().setW(java.math.BigInteger.valueOf(2000)); // % (B/A)

            XWPFTableRow header19 = (table19.getRow(0) != null) ? table19.getRow(0) : table19.createRow();
            ensureCells(header19, 4);
            styleCell(header19.getCell(0), "Category", true);
            styleCell(header19.getCell(1), "Total (A)", true);
            styleCell(header19.getCell(2), "No. of Females (B)", true);
            styleCell(header19.getCell(3), "% (B / A)", true);

            List<BrsrReportRequest.WomenRepresentation> womenList = data.getWomenRepresentationList();
            if (womenList != null && !womenList.isEmpty()) {
                for (BrsrReportRequest.WomenRepresentation row : womenList) {
                    addDynamicRow(table19, new String[]{
                            checkNull(row.getCategory()),
                            checkNull(row.getTotalA()),
                            checkNull(row.getFemaleNoB()),
                            checkNull(row.getFemalePerc()) + "%"
                    });
                }
            } else {
                addDynamicRow(table19, new String[]{"-", "-", "-", "-"});
            }

            // Appends the AI note perfectly below the table
            addNote(doc, data.getWomenRepresentationNotes());

            doc.createParagraph().createRun().addBreak();

            // --- 20. TURNOVER RATE ---
            addBoldText(doc, "22. Turnover rate for permanent employees and workers (Disclose trends for the past 3 years):");
            XWPFTable table20 = doc.createTable();
            table20.setWidth("100%");
            setTableBorders(table20); // FIX: Ensures gridlines are visible

            // FIX: Set specific column widths for 10 columns so they fit the page perfectly
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid grid20 = table20.getCTTbl().addNewTblGrid();
            grid20.addNewGridCol().setW(java.math.BigInteger.valueOf(1900)); // Category column (Wider)
            for(int i = 0; i < 9; i++) {
                grid20.addNewGridCol().setW(java.math.BigInteger.valueOf(800)); // 9 Number columns
            }

            XWPFTableRow hRow1 = table20.getRow(0);
            ensureCells(hRow1, 10);
            styleCell(hRow1.getCell(0), "Category", true);

            String fyCurrLabel = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "FY (Current)";
            String fyPrevLabel = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "FY (Previous)";
            String fyPriorLabel = (data.getFyPrior() != null && !data.getFyPrior().isEmpty()) ? data.getFyPrior() : "FY (Prior)";

            styleCell(hRow1.getCell(1), fyCurrLabel, true);
            mergeCellsHorizontal(hRow1, 1, 3);
            styleCell(hRow1.getCell(4), fyPrevLabel, true);
            mergeCellsHorizontal(hRow1, 4, 6);
            styleCell(hRow1.getCell(7), fyPriorLabel, true);
            mergeCellsHorizontal(hRow1, 7, 9);

            XWPFTableRow hRow2 = table20.createRow();
            ensureCells(hRow2, 10);
            styleCell(hRow2.getCell(0), "", true); // Blank under category
            for(int i=0; i<3; i++) {
                int base = 1 + (i*3);
                styleCell(hRow2.getCell(base), "Male", true);
                styleCell(hRow2.getCell(base+1), "Female", true);
                styleCell(hRow2.getCell(base+2), "Total", true);
            }

            String[] rowEmp20 = new String[10];
            rowEmp20[0] = "Permanent Employees";
            rowEmp20[1] = checkNull(data.getTurnoverEmpCurrMale()); rowEmp20[2] = checkNull(data.getTurnoverEmpCurrFemale()); rowEmp20[3] = checkNull(data.getTurnoverEmpCurrTotal());
            rowEmp20[4] = checkNull(data.getTurnoverEmpPrevMale()); rowEmp20[5] = checkNull(data.getTurnoverEmpPrevFemale()); rowEmp20[6] = checkNull(data.getTurnoverEmpPrevTotal());
            rowEmp20[7] = checkNull(data.getTurnoverEmpPriorMale()); rowEmp20[8] = checkNull(data.getTurnoverEmpPriorFemale()); rowEmp20[9] = checkNull(data.getTurnoverEmpPriorTotal());
            addDynamicRow(table20, rowEmp20);

            String[] rowWork20 = new String[10];
            rowWork20[0] = "Permanent Workers";
            rowWork20[1] = checkNull(data.getTurnoverWorkCurrMale()); rowWork20[2] = checkNull(data.getTurnoverWorkCurrFemale()); rowWork20[3] = checkNull(data.getTurnoverWorkCurrTotal());
            rowWork20[4] = checkNull(data.getTurnoverWorkPrevMale()); rowWork20[5] = checkNull(data.getTurnoverWorkPrevFemale()); rowWork20[6] = checkNull(data.getTurnoverWorkPrevTotal());
            rowWork20[7] = checkNull(data.getTurnoverWorkPriorMale()); rowWork20[8] = checkNull(data.getTurnoverWorkPriorFemale()); rowWork20[9] = checkNull(data.getTurnoverWorkPriorTotal());
            addDynamicRow(table20, rowWork20);

            // Appends the AI note perfectly below the table
            addNote(doc, data.getTurnoverNotes());

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION V =================
            addSectionHeader(doc, "V. Holding, Subsidiary and Associate Companies");
            addBoldText(doc, "23. (a) Details of holding/subsidiary/associate companies (including joint ventures):");

            List<BrsrReportRequest.HoldingCompany> holdingList = data.getHoldingCompanies();
            String note = data.getHoldingCompanyNote();

// Mode A: Table Data Exists
            if (holdingList != null && !holdingList.isEmpty()) {
                XWPFTable table21 = doc.createTable();
                table21.setWidth("100%");
                setTableBorders(table21); // FIX: Ensures gridlines are visible

                // FIX: Define column widths so the table fits the page perfectly
                org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid grid21 = table21.getCTTbl().addNewTblGrid();
                grid21.addNewGridCol().setW(java.math.BigInteger.valueOf(800));  // S.No
                grid21.addNewGridCol().setW(java.math.BigInteger.valueOf(3000)); // Name of Company
                grid21.addNewGridCol().setW(java.math.BigInteger.valueOf(2500)); // Type
                grid21.addNewGridCol().setW(java.math.BigInteger.valueOf(1200)); // % Shares
                grid21.addNewGridCol().setW(java.math.BigInteger.valueOf(2000)); // Participates in BR

                // Exact BRSR Standard Headers
                String[] headers21 = {
                        "S. No.",
                        "Name of the holding / subsidiary / associate companies / joint ventures (A)",
                        "Indicate whether holding/ Subsidiary/ Associate/ Joint Venture",
                        "% of shares held by listed entity",
                        "Does the entity indicated at column A, participate in the Business Responsibility initiatives of the listed entity? (Yes/No)"
                };
                addDynamicHeaderRow(table21, headers21);

                int count = 1;
                for (BrsrReportRequest.HoldingCompany company : holdingList) {
                    addDynamicRow(table21, new String[]{
                            String.valueOf(count++),
                            checkNull(company.getName()),
                            checkNull(company.getType()),
                            checkNull(company.getSharesHeld()) + "%", // Appends the % sign automatically
                            checkNull(company.getParticipateBusinessResponsibility())
                    });
                }
            }
// Mode B: Note / Text Mode
            else if (note != null && !note.trim().isEmpty()) {
                XWPFParagraph pNote = doc.createParagraph();
                // Use your existing helper to ensure line-breaks in the textarea render correctly in Word
                setTextWithBreaks(pNote.createRun(), note);
            }
// Fallback: Nothing Provided
            else {
                XWPFParagraph pNone = doc.createParagraph();
                pNone.createRun().setText("Not Applicable / No Data Provided");
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION VI =================
            addSectionHeader(doc, "VI. CSR Details");

            XWPFTable table22 = doc.createTable();
            table22.setWidth("100%");
            XWPFTableRow hRow22 = table22.getRow(0);
            if(hRow22.getTableCells().size()<2) hRow22.addNewTableCell();
            styleCell(hRow22.getCell(0), "Section", true);
            styleCell(hRow22.getCell(1), "Details", true);
            hRow22.addNewTableCell();
            styleCell(hRow22.getCell(2), "Company Particulars", true);

            XWPFTableRow r1 = table22.createRow();
            styleCell(r1.getCell(0), "24. i)", false);
            styleCell(r1.getCell(1), "Whether CSR is applicable as per section 135 of Companies Act, 2013?", false);
            styleCell(r1.getCell(2), checkNull(data.getCsrApplicable()), false);

            XWPFTableRow r2 = table22.createRow();
            styleCell(r2.getCell(0), "24. ii)", false);
            styleCell(r2.getCell(1), "Turnover (in Rs.)", false);
            styleCell(r2.getCell(2), checkNull(data.getCsrTurnover()), false);

            XWPFTableRow r3 = table22.createRow();
            styleCell(r3.getCell(0), "24. iii)", false);
            styleCell(r3.getCell(1), "Net worth (in Rs.)", false);
            styleCell(r3.getCell(2), checkNull(data.getCsrNetWorth()), false);

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION VII: TRANSPARENCY =================
            addSectionHeader(doc, "VII. Transparency and Disclosures Compliances");
            addBoldText(doc, "25. Complaints/Grievances on any of the principles (Principles 1 to 9):");

            List<BrsrReportRequest.Complaint> complaints = data.getComplaintsList();

            if (complaints != null && !complaints.isEmpty()) {
                for (BrsrReportRequest.Complaint c : complaints) {

                    XWPFParagraph pStake = doc.createParagraph();
                    pStake.setSpacingBefore(200);
                    XWPFRun rStakeLabel = pStake.createRun();
                    rStakeLabel.setText("Stakeholder Group: ");
                    rStakeLabel.setBold(true);
                    rStakeLabel.setColor(COLOR_THEME_GREEN);
                    rStakeLabel.setFontFamily("Calibri");

                    XWPFRun rStakeVal = pStake.createRun();
                    rStakeVal.setText(checkNull(c.getStakeholder()));
                    rStakeVal.setFontFamily("Calibri");

                    XWPFParagraph pMech = doc.createParagraph();
                    XWPFRun rMechLabel = pMech.createRun();
                    rMechLabel.setText("Grievance Redressal Mechanism: ");
                    rMechLabel.setBold(true);
                    rMechLabel.setColor(COLOR_THEME_GREEN);
                    rMechLabel.setFontFamily("Calibri");

                    XWPFRun rMechVal = pMech.createRun();
                    rMechVal.setFontFamily("Calibri");
                    setTextWithBreaks(rMechVal, checkNull(c.getMechanism())); // FIX
                    pMech.setSpacingAfter(100);

                    // Mini-Table
                    XWPFTable miniTable = doc.createTable();
                    miniTable.setWidth("100%");

                    XWPFTableRow miniH1 = miniTable.getRow(0);
                    ensureCells(miniH1, 6);

                    String curF = (data.getComplaintsFyCurrentHeader() != null) ? data.getComplaintsFyCurrentHeader() : "Current FY";
                    String prevF = (data.getComplaintsFyPreviousHeader() != null) ? data.getComplaintsFyPreviousHeader() : "Previous FY";

                    styleCell(miniH1.getCell(0), curF, true);
                    mergeCellsHorizontal(miniH1, 0, 2);
                    styleCell(miniH1.getCell(3), prevF, true);
                    mergeCellsHorizontal(miniH1, 3, 5);

                    XWPFTableRow miniH2 = miniTable.createRow();
                    ensureCells(miniH2, 6);
                    styleCell(miniH2.getCell(0), "Filed", true);
                    styleCell(miniH2.getCell(1), "Pending", true);
                    styleCell(miniH2.getCell(2), "Remarks", true);
                    styleCell(miniH2.getCell(3), "Filed", true);
                    styleCell(miniH2.getCell(4), "Pending", true);
                    styleCell(miniH2.getCell(5), "Remarks", true);

                    XWPFTableRow miniData = miniTable.createRow();
                    ensureCells(miniData, 6);
                    styleCell(miniData.getCell(0), checkNull(c.getCurrFiled()), false);
                    styleCell(miniData.getCell(1), checkNull(c.getCurrPending()), false);
                    styleCell(miniData.getCell(2), checkNull(c.getCurrRemarks()), false);
                    styleCell(miniData.getCell(3), checkNull(c.getPrevFiled()), false);
                    styleCell(miniData.getCell(4), checkNull(c.getPrevPending()), false);
                    styleCell(miniData.getCell(5), checkNull(c.getPrevRemarks()), false);

                    XWPFParagraph pSep = doc.createParagraph();
                    pSep.setBorderBottom(Borders.SINGLE);
                }
            } else {
                XWPFParagraph pNone = doc.createParagraph();
                pNone.createRun().setText("No complaints reported.");
            }

            doc.createParagraph().createRun().addBreak();

            // --- 26. Material Issues ---
            addBoldText(doc, "26. Overview of the entity's material responsible business conduct issues");

            XWPFParagraph p26Desc = doc.createParagraph();
            XWPFRun r26Desc = p26Desc.createRun();
            r26Desc.setText("Please indicate material responsible business conduct and sustainability issues pertaining to environmental and social matters that present a risk or an opportunity to your business, rationale for identifying the same, approach to adapt or mitigate the risk along-with its financial implications, as per the following format:");
            r26Desc.setFontFamily("Calibri");
            r26Desc.setFontSize(10);
            p26Desc.setSpacingAfter(200);

            // Print the introductory note if it exists
            addNote(doc, data.getMaterialIssuesNote());

            XWPFTable table26 = doc.createTable();
            table26.setWidth("100%");
            setTableBorders(table26); // FIX: Ensure gridlines render

            // Set specific column widths
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid grid26 = table26.getCTTbl().addNewTblGrid();
            grid26.addNewGridCol().setW(java.math.BigInteger.valueOf(500));  // S.No
            grid26.addNewGridCol().setW(java.math.BigInteger.valueOf(1800)); // Issue
            grid26.addNewGridCol().setW(java.math.BigInteger.valueOf(1000)); // R/O
            grid26.addNewGridCol().setW(java.math.BigInteger.valueOf(2800)); // Rationale
            grid26.addNewGridCol().setW(java.math.BigInteger.valueOf(2800)); // Approach
            grid26.addNewGridCol().setW(java.math.BigInteger.valueOf(1100)); // Financial Implication

            XWPFTableRow hRow26 = table26.getRow(0);
            ensureCells(hRow26, 6);
            styleCell(hRow26.getCell(0), "S. No.", true);
            styleCell(hRow26.getCell(1), "Material issue identified", true);
            styleCell(hRow26.getCell(2), "Indicate whether risk or opportunity (R/O)", true);
            styleCell(hRow26.getCell(3), "Rationale for identifying the risk / opportunity", true);
            styleCell(hRow26.getCell(4), "In case of risk, approach to adapt or mitigate", true);
            styleCell(hRow26.getCell(5), "Financial implications (Positive/Negative)", true);

            List<BrsrReportRequest.MaterialIssue> issues = data.getMaterialIssues();
            if (issues != null && !issues.isEmpty()) {
                int count = 1;
                for (BrsrReportRequest.MaterialIssue issue : issues) {
                    XWPFTableRow row = table26.createRow();
                    ensureCells(row, 6);
                    styleCell(row.getCell(0), String.valueOf(count++), false);
                    styleCell(row.getCell(1), checkNull(issue.getDescription()), false);
                    styleCell(row.getCell(2), checkNull(issue.getRiskOrOpportunity()), false);
                    fillCellWithNewlines(row.getCell(3), checkNull(issue.getRationale())); // Handles AI linebreaks
                    fillCellWithNewlines(row.getCell(4), checkNull(issue.getApproach()));  // Handles AI linebreaks
                    styleCell(row.getCell(5), checkNull(issue.getFinancialImplications()), false);
                }
            } else {
                addDynamicRow(table26, new String[]{"-", "-", "-", "-", "-", "-"});
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION B: MANAGEMENT =================
            doc.createParagraph().setPageBreak(true); // Pushes Section B to a new page
            addSectionHeader(doc, "SECTION B: MANAGEMENT AND PROCESS DISCLOSURES");

            XWPFParagraph pSecBDesc = doc.createParagraph();
            pSecBDesc.createRun().setText("This section is aimed at helping businesses demonstrate the structures, policies and processes put in place towards adopting the NGRBC Principles and Core Elements.");
            pSecBDesc.setSpacingAfter(200);

            // --- UNIFIED MATRIX (Q1 to Q6) ---
            XWPFTable tableUnified = doc.createTable();
            tableUnified.setWidth("100%");
            setTableBorders(tableUnified);

            // Define 10 columns: 1 wide for Questions, 9 narrow for P1-P9
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid gridUnified = tableUnified.getCTTbl().addNewTblGrid();
            gridUnified.addNewGridCol().setW(java.math.BigInteger.valueOf(3800)); // Disclosure Questions
            for(int k=1; k<=9; k++) {
                gridUnified.addNewGridCol().setW(java.math.BigInteger.valueOf(500)); // P1 to P9
            }

            // HEADER ROW
            XWPFTableRow hRowUnified = tableUnified.getRow(0);
            ensureCells(hRowUnified, 10);
            styleCell(hRowUnified.getCell(0), "Disclosure Questions", true);
            for(int k=1; k<=9; k++) {
                styleCell(hRowUnified.getCell(k), "P " + k, true);
            }

            // SUB-HEADER ROW
            XWPFTableRow subHeadRow = tableUnified.createRow();
            ensureCells(subHeadRow, 10);
            styleCell(subHeadRow.getCell(0), "Policy and management processes", true);
            mergeCellsHorizontal(subHeadRow, 0, 9); // Spans all columns

            // 1a. Policies Cover
            XWPFTableRow r1a = tableUnified.createRow();
            ensureCells(r1a, 10);
            styleCell(r1a.getCell(0), "1. a. Whether your entity's policy/policies cover each principle and its core elements of the NGRBCs. (Yes/No)", false);
            for(int k=1; k<=9; k++) styleCell(r1a.getCell(k), getYN(data.getQ1a(), k), false);

            // 1b. Board Approved
            XWPFTableRow r1b = tableUnified.createRow();
            ensureCells(r1b, 10);
            styleCell(r1b.getCell(0), "   b. Has the policy been approved by the Board? (Yes/No)", false);
            for(int k=1; k<=9; k++) styleCell(r1b.getCell(k), getYN(data.getQ1b(), k), false);

            // 1c. Web Link (Merged Cells 1-9)
            XWPFTableRow r1c = tableUnified.createRow();
            ensureCells(r1c, 10);
            styleCell(r1c.getCell(0), "   c. Web Link of the Policies, if available", false);
            styleCell(r1c.getCell(1), checkNull(data.getQ1WebLink()), false);
            mergeCellsHorizontal(r1c, 1, 9);

            // 2. Translated to procedures
            XWPFTableRow rowQ2 = tableUnified.createRow(); // Renamed from r2
            ensureCells(rowQ2, 10);
            styleCell(rowQ2.getCell(0), "2. Whether the entity has translated the policy into procedures. (Yes / No)", false);
            for(int k=1; k<=9; k++) styleCell(rowQ2.getCell(k), getYN(data.getQ2(), k), false);

            // 3. Value Chain
            XWPFTableRow rowQ3 = tableUnified.createRow(); // Renamed from r3
            ensureCells(rowQ3, 10);
            styleCell(rowQ3.getCell(0), "3. Do the enlisted policies extend to your value chain partners? (Yes/No)", false);
            for(int k=1; k<=9; k++) styleCell(rowQ3.getCell(k), getYN(data.getQ3(), k), false);

            // 4. ISO Standards Header
            XWPFTableRow r4Head = tableUnified.createRow();
            ensureCells(r4Head, 10);
            styleCell(r4Head.getCell(0), "4. Name of the national and international codes/certifications/labels/ standards adopted by your entity and mapped to each principle.", false);
            mergeCellsHorizontal(r4Head, 0, 9);

            // 4. ISO Standards Data Rows
            if(data.getQ4Standards() != null && !data.getQ4Standards().isEmpty()) {
                for(BrsrReportRequest.StandardMapping sm : data.getQ4Standards()) {
                    XWPFTableRow r4Data = tableUnified.createRow();
                    ensureCells(r4Data, 10);
                    styleCell(r4Data.getCell(0), "   " + checkNull(sm.getName()), false);
                    styleCell(r4Data.getCell(1), sm.isP1()?"Y":"N", false);
                    styleCell(r4Data.getCell(2), sm.isP2()?"Y":"N", false);
                    styleCell(r4Data.getCell(3), sm.isP3()?"Y":"N", false);
                    styleCell(r4Data.getCell(4), sm.isP4()?"Y":"N", false);
                    styleCell(r4Data.getCell(5), sm.isP5()?"Y":"N", false);
                    styleCell(r4Data.getCell(6), sm.isP6()?"Y":"N", false);
                    styleCell(r4Data.getCell(7), sm.isP7()?"Y":"N", false);
                    styleCell(r4Data.getCell(8), sm.isP8()?"Y":"N", false);
                    styleCell(r4Data.getCell(9), sm.isP9()?"Y":"N", false);
                }
            }

            // 5. Commitments (Merged Cells 1-9)
            XWPFTableRow r5 = tableUnified.createRow();
            ensureCells(r5, 10);
            styleCell(r5.getCell(0), "5. Specific commitments, goals and targets set by the entity with defined timelines, if any.", false);
            fillCellWithNewlines(r5.getCell(1), checkNull(data.getQ5Commitments()));
            mergeCellsHorizontal(r5, 1, 9);

            // 6. Performance (Merged Cells 1-9)
            XWPFTableRow r6 = tableUnified.createRow();
            ensureCells(r6, 10);
            styleCell(r6.getCell(0), "6. Performance of the entity against the specific commitments, goals and targets along-with reasons in case the same are not met.", false);
            fillCellWithNewlines(r6.getCell(1), checkNull(data.getQ6Performance()));
            mergeCellsHorizontal(r6, 1, 9);

            doc.createParagraph().createRun().addBreak();

            // --- Governance (Q7, Q8, Q9) - Two Column Table ---
            addBoldText(doc, "Governance, leadership and oversight");

            XWPFTable tableQ789 = doc.createTable();
            tableQ789.setWidth("100%");
            setTableBorders(tableQ789);

            // Set column widths: 40% for the question, 60% for the answer
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid gridQ789 = tableQ789.getCTTbl().addNewTblGrid();
            gridQ789.addNewGridCol().setW(java.math.BigInteger.valueOf(3500));
            gridQ789.addNewGridCol().setW(java.math.BigInteger.valueOf(5000));

            // Row 7
            XWPFTableRow r7 = tableQ789.getRow(0); // Row 0 is created by default when creating table
            ensureCells(r7, 2);
            styleCell(r7.getCell(0), "7. Statement by director responsible for the business responsibility report, highlighting ESG related challenges, targets and achievements (listed entity has flexibility regarding the placement of this disclosure)", false);
            fillCellWithNewlines(r7.getCell(1), checkNull(data.getGovernanceStatement()));

            // Row 8
            XWPFTableRow r8 = tableQ789.createRow();
            ensureCells(r8, 2);
            styleCell(r8.getCell(0), "8. Details of the highest authority responsible for implementation and oversight of the Business Responsibility policy (ies).", false);
            styleCell(r8.getCell(1), checkNull(data.getOversightAuthority()), false);

            // Row 9
            XWPFTableRow r9 = tableQ789.createRow();
            ensureCells(r9, 2);
            styleCell(r9.getCell(0), "9. Does the entity have a specified Committee of the Board/ Director responsible for decision making on sustainability related issues? (Yes / No). If yes, provide details.", false);
            fillCellWithNewlines(r9.getCell(1), checkNull(data.getQ9Committee()));

            doc.createParagraph().createRun().addBreak();

            // --- Q10 & Q11: Matrix Table ---
            addBoldText(doc, "10. Details of Review of NGRBCs by the Company:");

            XWPFTable tableQ1011 = doc.createTable();
            tableQ1011.setWidth("100%");
            setTableBorders(tableQ1011);

            // Same 10-column layout as the Q1-Q6 matrix
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid gridQ1011 = tableQ1011.getCTTbl().addNewTblGrid();
            gridQ1011.addNewGridCol().setW(java.math.BigInteger.valueOf(3800)); // Questions
            for(int k=1; k<=9; k++) {
                gridQ1011.addNewGridCol().setW(java.math.BigInteger.valueOf(500)); // P1 to P9
            }

            // Header Row
            XWPFTableRow hRowQ1011 = tableQ1011.getRow(0);
            ensureCells(hRowQ1011, 10);
            styleCell(hRowQ1011.getCell(0), "Questions", true);
            for(int k=1; k<=9; k++) {
                styleCell(hRowQ1011.getCell(k), "P " + k, true);
            }

            // 10a. Performance Review
            XWPFTableRow r10a = tableQ1011.createRow();
            ensureCells(r10a, 10);
            styleCell(r10a.getCell(0), "Performance against above policies and follow up action", false);
            fillCellWithNewlines(r10a.getCell(1), checkNull(data.getQ10PerformanceReview()));
            mergeCellsHorizontal(r10a, 1, 9); // Spans text across P1-P9

            // 10b. Compliance Review
            XWPFTableRow r10b = tableQ1011.createRow();
            ensureCells(r10b, 10);
            styleCell(r10b.getCell(0), "Compliance with statutory requirements of relevance to the principles, and, rectification of any non-compliances", false);
            fillCellWithNewlines(r10b.getCell(1), checkNull(data.getQ10ComplianceReview()));
            mergeCellsHorizontal(r10b, 1, 9); // Spans text across P1-P9

            // 11. Independent Assessment (Visual Separator Row)
            XWPFTableRow r11Label = tableQ1011.createRow();
            ensureCells(r11Label, 10);
            styleCell(r11Label.getCell(0), "11.", false);
            for(int k=1; k<=9; k++) styleCell(r11Label.getCell(k), String.valueOf(k), true);

            // 11. Independent Assessment Data
            XWPFTableRow r11 = tableQ1011.createRow();
            ensureCells(r11, 10);
            styleCell(r11.getCell(0), "Has the entity carried out independent Assessment/ evaluation of the working of its policies by an external agency? (Yes/No). If yes, provide name of the Agency.", false);
            fillCellWithNewlines(r11.getCell(1), checkNull(data.getQ11Assessment()));
            mergeCellsHorizontal(r11, 1, 9); // Spans text across P1-P9

            doc.createParagraph().createRun().addBreak();

            // --- Q12: Reasons for "No" ---
            addBoldText(doc, "12. If answer to question (1) above is “No” i.e. not all Principles are covered by a policy, reasons to be stated:");

            XWPFTable tableQ12 = doc.createTable();
            tableQ12.setWidth("100%");
            setTableBorders(tableQ12); // Ensures gridlines are visible

            // Align column widths exactly with the previous matrices (38% for questions, rest for P1-P9)
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid gridQ12 = tableQ12.getCTTbl().addNewTblGrid();
            gridQ12.addNewGridCol().setW(java.math.BigInteger.valueOf(3800)); // Questions
            for(int k=1; k<=9; k++) {
                gridQ12.addNewGridCol().setW(java.math.BigInteger.valueOf(500)); // P1 to P9
            }

            // Header Row
            XWPFTableRow hRowQ12 = tableQ12.getRow(0);
            ensureCells(hRowQ12, 10);
            styleCell(hRowQ12.getCell(0), "Questions", true);
            for(int k=1; k<=9; k++) {
                styleCell(hRowQ12.getCell(k), "P " + k, true);
            }

            // Populate Data Rows
            if(data.getQ12Reasons() != null) {
                for(BrsrReportRequest.ReasonMapping rm : data.getQ12Reasons()) {
                    XWPFTableRow r = tableQ12.createRow();
                    ensureCells(r, 10);

                    styleCell(r.getCell(0), checkNull(rm.getQuestionText()), false);

                    // Prints "Y" if checked, and "N" if unchecked
                    styleCell(r.getCell(1), rm.isP1() ? "Y" : "N", false);
                    styleCell(r.getCell(2), rm.isP2() ? "Y" : "N", false);
                    styleCell(r.getCell(3), rm.isP3() ? "Y" : "N", false);
                    styleCell(r.getCell(4), rm.isP4() ? "Y" : "N", false);
                    styleCell(r.getCell(5), rm.isP5() ? "Y" : "N", false);
                    styleCell(r.getCell(6), rm.isP6() ? "Y" : "N", false);
                    styleCell(r.getCell(7), rm.isP7() ? "Y" : "N", false);
                    styleCell(r.getCell(8), rm.isP8() ? "Y" : "N", false);
                    styleCell(r.getCell(9), rm.isP9() ? "Y" : "N", false);
                }
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION C: PRINCIPLE 1 & OTHERS =================
            // (Include the rest of the sections we implemented previously here)
            // For now, I will include the Leadership Indicators as requested in the new prompt.
            // ================= SECTION C: PRINCIPLE 1 =================
            // Force Page Break
            XWPFParagraph pSecC = doc.createParagraph();
            pSecC.setPageBreak(true);

            XWPFRun rSecC = pSecC.createRun();
            rSecC.setText("SECTION C: PRINCIPLE WISE PERFORMANCE DISCLOSURE");
            rSecC.setBold(true);
            rSecC.setFontSize(14);
            rSecC.setColor(COLOR_THEME_GREEN);
            rSecC.setFontFamily("Calibri");

            XWPFParagraph pDesc = doc.createParagraph();
            XWPFRun rDesc = pDesc.createRun();
            rDesc.setText("This section is aimed at helping entities demonstrate their performance in integrating the Principles and Core Elements with key processes and decisions. The information sought is categorized as \"Essential\" and \"Leadership\".");
            rDesc.setFontFamily("Calibri");
            rDesc.setFontSize(10);
            pDesc.setSpacingAfter(200);

            // --- Principle 1 Header ---
            XWPFParagraph pPrinciple1 = doc.createParagraph();
            XWPFRun rP1 = pPrinciple1.createRun();
            rP1.setText("PRINCIPLE 1: Businesses should conduct and govern themselves with integrity, and in a manner that is Ethical, Transparent and Accountable.");
            rP1.setBold(true);
            rP1.setFontSize(11);
            rP1.setFontFamily("Calibri");
            pPrinciple1.setSpacingAfter(200);

            addBoldText(doc, "Essential Indicators");
            addBoldText(doc, "1. Percentage coverage by training and awareness programmes on any of the principles during the financial year:");

            // --- TRAINING TABLE ---
            XWPFTable tableTrain = doc.createTable();
            tableTrain.setWidth("100%");

            // Column Widths (Give Topic column 50% width)
            CTTblGrid gridTrain = tableTrain.getCTTbl().addNewTblGrid();
            gridTrain.addNewGridCol().setW(BigInteger.valueOf(1500)); // Segment
            gridTrain.addNewGridCol().setW(BigInteger.valueOf(1500)); // Total Programs
            gridTrain.addNewGridCol().setW(BigInteger.valueOf(5000)); // Topics (Wide)
            gridTrain.addNewGridCol().setW(BigInteger.valueOf(1000)); // % Coverage

            // Header
            XWPFTableRow hRowTrain = tableTrain.getRow(0);
            ensureCells(hRowTrain, 4);
            styleCell(hRowTrain.getCell(0), "Segment", true);
            styleCell(hRowTrain.getCell(1), "Total Number of training and awareness programmes held", true);
            styleCell(hRowTrain.getCell(2), "Topics/principles covered under training and its impact", true);
            styleCell(hRowTrain.getCell(3), "%age of persons in respective category", true);

            // Data Rows
            List<BrsrReportRequest.TrainingProgram> trainings = data.getTrainingPrograms();
            if (trainings != null && !trainings.isEmpty()) {
                for (BrsrReportRequest.TrainingProgram tp : trainings) {
                    XWPFTableRow row = tableTrain.createRow();
                    ensureCells(row, 4);

                    styleCell(row.getCell(0), checkNull(tp.getSegment()), false);
                    styleCell(row.getCell(1), checkNull(tp.getTotalPrograms()), false);

                    // Use helper for multi-line text
                    fillCellWithNewlines(row.getCell(2), checkNull(tp.getTopicsCovered()));

                    styleCell(row.getCell(3), checkNull(tp.getPercentageCovered()) + "%", false);
                }
            } else {
                addDynamicRow(tableTrain, new String[]{"-", "-", "-", "-"});
            }

            // --- Footer Note ---
            doc.createParagraph().createRun().addBreak();
            String trainingNote = data.getTrainingNote();
            if (trainingNote != null && !trainingNote.trim().isEmpty()) {
                XWPFParagraph pNote = doc.createParagraph();
                setTextWithBreaks(pNote.createRun(), trainingNote); // Respect newlines
            }

            doc.createParagraph().createRun().addBreak();

            // --- 2. FINES AND PENALTIES ---
            addBoldText(doc, "2. Details of fines / penalties /punishment/ award/ compounding fees/ settlement amount paid in proceedings (by the entity or by directors / KMPs) with regulators/ law enforcement agencies/ judicial institutions, in the financial year:");

            // Note (Before Table)
            addNote(doc, data.getLegalActionNote());
            doc.createParagraph().createRun().addBreak();

            // --- FIX: Initialize table2 here ---
            XWPFTable table2 = doc.createTable();
            table2.setWidth("100%");

            // Define 6 Columns (Critical for layout)
            CTTblGrid grid2 = table2.getCTTbl().addNewTblGrid();
            grid2.addNewGridCol().setW(BigInteger.valueOf(1500)); // Type
            grid2.addNewGridCol().setW(BigInteger.valueOf(1000)); // Principle
            grid2.addNewGridCol().setW(BigInteger.valueOf(2000)); // Agency
            grid2.addNewGridCol().setW(BigInteger.valueOf(1000)); // Amount
            grid2.addNewGridCol().setW(BigInteger.valueOf(3000)); // Brief
            grid2.addNewGridCol().setW(BigInteger.valueOf(800));  // Appeal

            // --- MONETARY SECTION ---
            // Row 0: "Monetary" Header spanning all columns
            XWPFTableRow hMonetary = table2.getRow(0);
            ensureCells(hMonetary, 6);
            styleCell(hMonetary.getCell(0), "Monetary", true);
            mergeCellsHorizontal(hMonetary, 0, 5);

            // Row 1: Column Headers
            XWPFTableRow colHeaders = table2.createRow();
            ensureCells(colHeaders, 6);
            styleCell(colHeaders.getCell(0), "", true);
            styleCell(colHeaders.getCell(1), "NGRBC Principle", true);
            styleCell(colHeaders.getCell(2), "Name of the regulatory/ enforcement agencies", true);
            styleCell(colHeaders.getCell(3), "Amount (In INR)", true);
            styleCell(colHeaders.getCell(4), "Brief of the Case", true);
            styleCell(colHeaders.getCell(5), "Has an appeal been preferred?", true);

            // Filter and Add Monetary Rows
            boolean hasMonetary = false;
            List<BrsrReportRequest.LegalAction> actions = data.getLegalActions();
            if (actions != null) {
                for (BrsrReportRequest.LegalAction la : actions) {
                    String t = checkNull(la.getType());
                    if (t.contains("Penalty") || t.contains("Settlement") || t.contains("Compounding")) {
                        hasMonetary = true;
                        XWPFTableRow row = table2.createRow();
                        ensureCells(row, 6);
                        styleCell(row.getCell(0), t, true); // Bold Type
                        styleCell(row.getCell(1), checkNull(la.getPrinciple()), false);
                        styleCell(row.getCell(2), checkNull(la.getAgency()), false);
                        styleCell(row.getCell(3), checkNull(la.getAmount()), false);
                        fillCellWithNewlines(row.getCell(4), checkNull(la.getBrief()));
                        styleCell(row.getCell(5), checkNull(la.getAppeal()), false);
                    }
                }
            }
            if (!hasMonetary) {
                // Add empty row if none
                addDynamicRow(table2, new String[]{"Penalty/Fine", "-", "-", "-", "-", "-"});
            }

            // --- NON-MONETARY SECTION ---

            // 1. Check if data exists in the list
            boolean hasNonMonetaryData = false;
            if (actions != null) {
                for (BrsrReportRequest.LegalAction la : actions) {
                    String t = checkNull(la.getType());
                    if (t.contains("Imprisonment") || t.contains("Punishment")) {
                        hasNonMonetaryData = true;
                        break;
                    }
                }
            }

            // 2. Add the Section Header Row
            addSectionTitleRow(table2, "Non-Monetary", 6);

            // 3. Re-add Column Headers (Required for clarity in this new section)
            XWPFTableRow colHeadersNM = table2.createRow();
            ensureCells(colHeadersNM, 6);
            styleCell(colHeadersNM.getCell(0), "", true);
            styleCell(colHeadersNM.getCell(1), "NGRBC Principle", true);
            styleCell(colHeadersNM.getCell(2), "Regulatory Agency", true);
            styleCell(colHeadersNM.getCell(3), "N/A", true);
            styleCell(colHeadersNM.getCell(4), "Brief of the Case", true);
            styleCell(colHeadersNM.getCell(5), "Appeal?", true);

            // 4. LOGIC: Check User Flag vs Data vs Empty
            boolean isNA = data.isNonMonetaryNA(); // Did user check the box?

            if (isNA) {
                // CASE A: User explicitly said "Not Applicable"
                addStyledLegalRow(table2, "Imprisonment", "NA", "NA", "-", "NA", "NA");
                addStyledLegalRow(table2, "Punishment", "NA", "NA", "-", "NA", "NA");
            }
            else if (hasNonMonetaryData) {
                // CASE B: User provided data
                for (BrsrReportRequest.LegalAction la : actions) {
                    String t = checkNull(la.getType());
                    if (t.contains("Imprisonment") || t.contains("Punishment")) {
                        XWPFTableRow row = table2.createRow();
                        ensureCells(row, 6);

                        styleCell(row.getCell(0), t, true);
                        styleCell(row.getCell(1), checkNull(la.getPrinciple()), false);
                        styleCell(row.getCell(2), checkNull(la.getAgency()), false);
                        styleCell(row.getCell(3), "-", false);
                        fillCellWithNewlines(row.getCell(4), checkNull(la.getBrief()));
                        styleCell(row.getCell(5), checkNull(la.getAppeal()), false);
                    }
                }
            }
            else {
                // CASE C: User didn't say NA, but provided no data (Implies Nil)
                addStyledLegalRow(table2, "Imprisonment", "Nil", "Nil", "-", "Nil", "Nil");
                addStyledLegalRow(table2, "Punishment", "Nil", "Nil", "-", "Nil", "Nil");
            }

            doc.createParagraph().createRun().addBreak();

            // --- 3. APPEALS ---
            addBoldText(doc, "3. Of the instances disclosed in Question 2 above, details of the Appeal/Revision preferred in cases where monetary or non-monetary action has been appealed.");

            XWPFTable table3 = doc.createTable();
            table3.setWidth("100%");

            // Header
            XWPFTableRow hRow3 = table3.getRow(0);
            ensureCells(hRow3, 2);
            styleCell(hRow3.getCell(0), "Case Details", true);
            styleCell(hRow3.getCell(1), "Name of the regulatory/ enforcement agencies/ judicial institutions", true);

            // Data Rows
            List<BrsrReportRequest.AppealDetail> appeals = data.getAppealDetails();

            if (appeals != null && !appeals.isEmpty()) {
                for (BrsrReportRequest.AppealDetail app : appeals) {
                    addDynamicRow(table3, new String[]{
                            checkNull(app.getCaseDetails()),
                            checkNull(app.getAgencyName())
                    });
                }
            } else {
                // Default NA if list is empty
                addDynamicRow(table3, new String[]{"NA", "NA"});
            }

            doc.createParagraph().createRun().addBreak();

            // --- C: Q4 Anti-Corruption ---
            addBoldText(doc, "4. Does the entity have an anti-corruption or anti-bribery policy? If yes, provide details in brief and if available, provide a web-link to the policy.");
            XWPFParagraph p4 = doc.createParagraph();
            XWPFRun r4Body = p4.createRun();
            r4Body.setFontFamily("Calibri");
            r4Body.setFontSize(10);

            String policyStatus = checkNull(data.getAntiCorruptionPolicy());
            String policyDetails = checkNull(data.getAntiCorruptionDetails());
            String combinedText = policyStatus + (policyDetails.equals("-") ? "" : ", " + policyDetails);
            setTextWithBreaks(r4Body, combinedText);

            String policyLink = data.getAntiCorruptionLink();
            if (policyLink != null && !policyLink.trim().isEmpty()) {
                p4.createRun().addBreak();
                XWPFRun r4Link = p4.createRun();
                r4Link.setFontFamily("Calibri");
                r4Link.setFontSize(10);
                r4Link.setColor("0563C1");
                r4Link.setText("Weblink: " + policyLink);
                r4Link.setUnderline(UnderlinePatterns.SINGLE);
            }

            doc.createParagraph().createRun().addBreak();

            // --- 5. DISCIPLINARY ACTION ---
            addBoldText(doc, "5. Number of Directors/KMPs/employees/workers against whom disciplinary action was taken by any law enforcement agency for the charges of bribery/ corruption:");
            XWPFTable table5 = doc.createTable();
            table5.setWidth("100%");
            XWPFTableRow hRow5 = table5.getRow(0);
            ensureCells(hRow5, 3);
            styleCell(hRow5.getCell(0), "", true);
            String fyCur = (data.getDisciplinaryFyCurrentHeader() != null) ? data.getDisciplinaryFyCurrentHeader() : "Current FY";
            styleCell(hRow5.getCell(1), fyCur, true);
            String fyPrev = (data.getDisciplinaryFyPreviousHeader() != null) ? data.getDisciplinaryFyPreviousHeader() : "Previous FY";
            styleCell(hRow5.getCell(2), fyPrev, true);

            addDynamicRow(table5, new String[]{"Directors", checkNull(data.getDiscDirectorsCurr()), checkNull(data.getDiscDirectorsPrev())});
            addDynamicRow(table5, new String[]{"KMPs", checkNull(data.getDiscKmpsCurr()), checkNull(data.getDiscKmpsPrev())});
            addDynamicRow(table5, new String[]{"Employees", checkNull(data.getDiscEmployeesCurr()), checkNull(data.getDiscEmployeesPrev())});
            addDynamicRow(table5, new String[]{"Workers", checkNull(data.getDiscWorkersCurr()), checkNull(data.getDiscWorkersPrev())});

            doc.createParagraph().createRun().addBreak();

            // --- 6. CONFLICT OF INTEREST ---
            addBoldText(doc, "6. Details of complaints with regard to conflict of interest:");
            XWPFTable table6 = doc.createTable();
            table6.setWidth("100%");
            XWPFTableRow hRow6_1 = table6.getRow(0);
            ensureCells(hRow6_1, 5);
            styleCell(hRow6_1.getCell(0), "", true);
            styleCell(hRow6_1.getCell(1), fyCur, true);
            mergeCellsHorizontal(hRow6_1, 1, 2);
            styleCell(hRow6_1.getCell(3), fyPrev, true);
            mergeCellsHorizontal(hRow6_1, 3, 4);

            XWPFTableRow hRow6_2 = table6.createRow();
            ensureCells(hRow6_2, 5);
            styleCell(hRow6_2.getCell(0), "", true);
            styleCell(hRow6_2.getCell(1), "Number", true);
            styleCell(hRow6_2.getCell(2), "Remarks", true);
            styleCell(hRow6_2.getCell(3), "Number", true);
            styleCell(hRow6_2.getCell(4), "Remarks", true);

            addDynamicRow(table6, new String[]{
                    "Numbers of complaints received in relation to issues of Conflict of Interest of the Directors",
                    checkNull(data.getCoiDirectorsCurrNum()), checkNull(data.getCoiDirectorsCurrRem()),
                    checkNull(data.getCoiDirectorsPrevNum()), checkNull(data.getCoiDirectorsPrevRem())
            });
            addDynamicRow(table6, new String[]{
                    "Numbers of complaints received in relation to issues of Conflict of Interest of the KMPs",
                    checkNull(data.getCoiKmpsCurrNum()), checkNull(data.getCoiKmpsCurrRem()),
                    checkNull(data.getCoiKmpsPrevNum()), checkNull(data.getCoiKmpsPrevRem())
            });

            doc.createParagraph().createRun().addBreak();

            // --- 7. CORRECTIVE ACTIONS ---
            addBoldText(doc, "7. Provide details of any corrective action taken or underway on issues related to fines / penalties / action taken by regulators/ law enforcement agencies/ judicial institutions, on cases of corruption and conflicts of interest.");
            XWPFParagraph p7 = doc.createParagraph();
            setTextWithBreaks(p7.createRun(), checkNull(data.getCorrectiveActionDetails()));

            doc.createParagraph().createRun().addBreak();

            // ================= QUESTION 8 =================
            addBoldText(doc, "8. Number of days of accounts payables ((Accounts payable * 365) / Cost of goods/services procured) in the following format:");
            XWPFTable tableQ8 = doc.createTable(); // <-- Changed
            tableQ8.setWidth("100%"); // <-- Changed

            XWPFTableRow hRow8 = tableQ8.getRow(0); // <-- Changed
            ensureCells(hRow8, 3);
            styleCell(hRow8.getCell(0), "", true);
            styleCell(hRow8.getCell(1), "FY Current", true);
            styleCell(hRow8.getCell(2), "FY Previous", true);

            addDynamicRow(tableQ8, new String[]{"Number of days of accounts payables", checkNull(data.getAccountsPayableCurr()), checkNull(data.getAccountsPayablePrev())}); // <-- Changed

            doc.createParagraph().createRun().addBreak();

            // ================= QUESTION 9 =================
            addBoldText(doc, "9. Open-ness of business");
            addBoldText(doc, "Provide details of concentration of purchases and sales with trading houses, dealers, and related parties along-with loans and advances & investments, with related parties, in the following format:");

            XWPFTable tableQ9 = doc.createTable(); // <-- Changed
            tableQ9.setWidth("100%"); // <-- Changed;

            // Widths: 20%, 40%, 20%, 20%
            CTTblGrid grid9 = tableQ9.getCTTbl().addNewTblGrid(); // <-- Changed
            grid9.addNewGridCol().setW(BigInteger.valueOf(2000));
            grid9.addNewGridCol().setW(BigInteger.valueOf(4000));
            grid9.addNewGridCol().setW(BigInteger.valueOf(2000));
            grid9.addNewGridCol().setW(BigInteger.valueOf(2000));

            XWPFTableRow hRow9 = tableQ9.getRow(0); // <-- Changed
            ensureCells(hRow9, 4);
            styleCell(hRow9.getCell(0), "Parameter", true);
            styleCell(hRow9.getCell(1), "Metrics", true);
            styleCell(hRow9.getCell(2), "FY Current", true);
            styleCell(hRow9.getCell(3), "FY Previous", true);

            // Purchases Block (Row 1-3)
            addDynamicRow(tableQ9, new String[]{"Concentration of Purchases", "a. Purchases from trading houses...", checkNull(data.getPurTradingPercCurr()), checkNull(data.getPurTradingPercPrev())}); // <-- Changed
            addDynamicRow(tableQ9, new String[]{"", "b. Number of trading houses...", checkNull(data.getPurTradingNumCurr()), checkNull(data.getPurTradingNumPrev())}); // <-- Changed
            addDynamicRow(tableQ9, new String[]{"", "c. Purchases from top 10...", checkNull(data.getPurTop10PercCurr()), checkNull(data.getPurTop10PercPrev())}); // <-- Changed
            mergeCellsVertical(tableQ9, 0, 1, 3); // <-- Changed

            // Sales Block (Row 4-6)
            addDynamicRow(tableQ9, new String[]{"Concentration of Sales", "a. Sales to dealers...", checkNull(data.getSalesDealerPercCurr()), checkNull(data.getSalesDealerPercPrev())}); // <-- Changed
            addDynamicRow(tableQ9, new String[]{"", "b. Number of dealers...", checkNull(data.getSalesDealerNumCurr()), checkNull(data.getSalesDealerNumPrev())}); // <-- Changed
            addDynamicRow(tableQ9, new String[]{"", "c. Sales to top 10...", checkNull(data.getSalesTop10PercCurr()), checkNull(data.getSalesTop10PercPrev())}); // <-- Changed
            mergeCellsVertical(tableQ9, 0, 4, 6); // <-- Changed

            // RPT Block (Row 7-10)
            addDynamicRow(tableQ9, new String[]{"Share of RPTs in", "a. Purchases...", checkNull(data.getRptPurCurr()), checkNull(data.getRptPurPrev())}); // <-- Changed
            addDynamicRow(tableQ9, new String[]{"", "b. Sales...", checkNull(data.getRptSalesCurr()), checkNull(data.getRptSalesPrev())}); // <-- Changed
            addDynamicRow(tableQ9, new String[]{"", "c. Loans & advances...", checkNull(data.getRptLoansCurr()), checkNull(data.getRptLoansPrev())}); // <-- Changed
            addDynamicRow(tableQ9, new String[]{"", "d. Investments...", checkNull(data.getRptInvestCurr()), checkNull(data.getRptInvestPrev())}); // <-- Changed
            mergeCellsVertical(tableQ9, 0, 7, 10); // <-- Changed

            // Print AI Note if exists
            addNote(doc, data.getOpennessNote());

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP INDICATORS ---
            addSectionHeader(doc, "Leadership Indicators");
            addBoldText(doc, "1. Awareness programmes conducted for value chain partners on any of the principles during the financial year:");

            // 1. Optional Note
            String leadNote = data.getLeadershipIndicatorNote();
            if (leadNote != null && !leadNote.trim().isEmpty()) {
                XWPFParagraph pNote = doc.createParagraph();
                pNote.setSpacingAfter(200);
                setTextWithBreaks(pNote.createRun(), leadNote);
            }

            // 2. Mandatory Table
            XWPFTable tableLead = doc.createTable();
            tableLead.setWidth("100%");
            CTTblGrid gridLead = tableLead.getCTTbl().addNewTblGrid();
            gridLead.addNewGridCol().setW(BigInteger.valueOf(1500));
            gridLead.addNewGridCol().setW(BigInteger.valueOf(6000));
            gridLead.addNewGridCol().setW(BigInteger.valueOf(1500));

            XWPFTableRow hRowLead = tableLead.getRow(0);
            ensureCells(hRowLead, 3);
            styleCell(hRowLead.getCell(0), "Total number of awareness programmes held", true);
            styleCell(hRowLead.getCell(1), "Topics / principles covered under the training", true);
            styleCell(hRowLead.getCell(2), "%age of value chain partners covered", true);

            List<BrsrReportRequest.LeadershipAwareness> programs = data.getLeadershipAwarenessPrograms();
            if (programs != null && !programs.isEmpty()) {
                for (BrsrReportRequest.LeadershipAwareness prog : programs) {
                    XWPFTableRow row = tableLead.createRow();
                    ensureCells(row, 3);
                    styleCell(row.getCell(0), checkNull(prog.getTotalCount()), false);
                    styleCell(row.getCell(1), checkNull(prog.getTopic()), false);
                    styleCell(row.getCell(2), checkNull(prog.getPercentage()) + "%", false);
                }
            } else {
                addDynamicRow(tableLead, new String[]{"-", "-", "-"});
            }

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 2: CONFLICT OF INTEREST ---
            addBoldText(doc, "2. Does the entity have processes in place to avoid/ manage conflict of interests involving members of the Board? (Yes/No) If Yes, provide details of the same.");
            XWPFParagraph pLead2 = doc.createParagraph();
            XWPFRun rLead2 = pLead2.createRun();

            String conflictStatus = checkNull(data.getConflictOfInterestProcess());
            String conflictDetails = checkNull(data.getConflictOfInterestDetails());
            String conflictText = conflictStatus;
            if (!conflictDetails.equals("-")) {
                conflictText += ", " + conflictDetails;
            }
            setTextWithBreaks(rLead2, conflictText);

            doc.createParagraph().createRun().addBreak();

            // --- PRINCIPLE 2 HEADER ---
            XWPFParagraph pP2 = doc.createParagraph();
            pP2.setPageBreak(true); // Start Principle 2 on a new page
            XWPFRun rP2 = pP2.createRun();
            rP2.setText("PRINCIPLE 2: Businesses should provide goods and services in a manner that is sustainable and safe");
            rP2.setBold(true);
            rP2.setFontSize(12);
            rP2.setColor(COLOR_THEME_GREEN); // Use your defined theme color
            rP2.setFontFamily("Calibri");
            pP2.setSpacingAfter(200);

            addBoldText(doc, "Essential Indicators");

            // --- P2 Q1: R&D & Capex Table ---
            addBoldText(doc, "1. Percentage of R&D and capital expenditure (capex) investments in specific technologies to improve the environmental and social impacts of product and processes to total R&D and capex investments made by the entity, respectively.");

            XWPFTable tableP2 = doc.createTable();
            tableP2.setWidth("100%");

            CTTblGrid gridP2 = tableP2.getCTTbl().addNewTblGrid();
            gridP2.addNewGridCol().setW(BigInteger.valueOf(1000)); // Category
            gridP2.addNewGridCol().setW(BigInteger.valueOf(1500)); // Curr FY
            gridP2.addNewGridCol().setW(BigInteger.valueOf(1500)); // Prev FY
            gridP2.addNewGridCol().setW(BigInteger.valueOf(5000)); // Details (Wide for AI text)

            XWPFTableRow hRowP2 = tableP2.getRow(0);
            ensureCells(hRowP2, 4);
            styleCell(hRowP2.getCell(0), "", true);

            // Dynamic FY Headers
            String fyCurP2 = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "Current Financial Year";
            String fyPrevP2 = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "Previous Financial Year";

            styleCell(hRowP2.getCell(1), fyCurP2, true);
            styleCell(hRowP2.getCell(2), fyPrevP2, true);
            styleCell(hRowP2.getCell(3), "Details of improvements in environmental and social impacts", true);

            // Row 1: R&D
            XWPFTableRow rRd = tableP2.createRow();
            ensureCells(rRd, 4);
            styleCell(rRd.getCell(0), "R&D", false);
            styleCell(rRd.getCell(1), checkNull(data.getRdCurrentYear()), false);
            styleCell(rRd.getCell(2), checkNull(data.getRdPreviousYear()), false);
            fillCellWithNewlines(rRd.getCell(3), checkNull(data.getRdDetails())); // Handles AI generated multiline text

            // Row 2: Capex
            XWPFTableRow rCap = tableP2.createRow();
            ensureCells(rCap, 4);
            styleCell(rCap.getCell(0), "Capex", false);
            styleCell(rCap.getCell(1), checkNull(data.getCapexCurrentYear()), false);
            styleCell(rCap.getCell(2), checkNull(data.getCapexPreviousYear()), false);
            fillCellWithNewlines(rCap.getCell(3), checkNull(data.getCapexDetails())); // Handles AI generated multiline text

            // Optional Note for Q1
            addNote(doc, data.getPrinciple2Note());
            doc.createParagraph().createRun().addBreak();

            // --- P2 Q2: Sustainable Sourcing ---
            addBoldText(doc, "2. a. Does the entity have procedures in place for sustainable sourcing? (Yes/No)");
            addBoldText(doc, "   b. If yes, what percentage of inputs were sourced sustainably?");
            XWPFParagraph pP2Q2 = doc.createParagraph();
            String q2Text = checkNull(data.getSustainableSourcingProcedures()) + ". Percentage sourced sustainably: " + checkNull(data.getSustainableSourcingPercentage());
            if (data.getSustainableSourcingDetails() != null && !data.getSustainableSourcingDetails().isEmpty()) {
                q2Text += "\nDetails: " + data.getSustainableSourcingDetails();
            }
            if (data.getSustainableSourcingRemarks() != null && !data.getSustainableSourcingRemarks().isEmpty()) {
                q2Text += "\nRemarks: " + data.getSustainableSourcingRemarks();
            }
            setTextWithBreaks(pP2Q2.createRun(), q2Text); // Handles AI paragraph
            doc.createParagraph().createRun().addBreak();

            // --- P2 Q3: Reclaim Processes ---
            addBoldText(doc, "3. Describe the processes in place to safely reclaim your products for reusing, recycling and disposing at the end of life, for (a) Plastics (including packaging) (b) E-waste (c) Hazardous waste and (d) other waste.");
            XWPFParagraph pP2Q3 = doc.createParagraph();
            setTextWithBreaks(pP2Q3.createRun(), checkNull(data.getReclaimProcessDetails())); // Handles AI paragraph
            doc.createParagraph().createRun().addBreak();

            // --- P2 Q4: EPR ---
            addBoldText(doc, "4. Whether Extended Producer Responsibility (EPR) is applicable to the entity's activities (Yes / No). If yes, whether the waste collection plan is in line with the Extended Producer Responsibility (EPR) plan submitted to Pollution Control Boards? If not, provide steps taken to address the same.");
            XWPFParagraph pP2Q4 = doc.createParagraph();
            setTextWithBreaks(pP2Q4.createRun(), checkNull(data.getEprDetails())); // Handles AI paragraph
            doc.createParagraph().createRun().addBreak();

            // ================= P2 LEADERSHIP INDICATORS =================
            addSectionHeader(doc, "Leadership Indicators");

            // --- P2 Lead Q1: LCA Table ---
            addBoldText(doc, "1. Has the entity conducted Life Cycle Perspective / Assessments (LCA) for any of its products (for manufacturing industry) or for its services (for service industry)? If yes, provide details in the following format?");
            if (data.isP2Lead1NA()) {
                doc.createParagraph().createRun().setText("Not Applicable");
                doc.createParagraph().createRun().addBreak();
            } else {
                addNote(doc, data.getLcaNote());
                XWPFTable tLca = doc.createTable();
                tLca.setWidth("100%");
                addDynamicHeaderRow(tLca, new String[]{"NIC Code", "Name of Product / Service", "% of total Turnover contributed", "Boundary for which the Life Cycle Perspective / Assessment was conducted", "Whether conducted by independent external agency (Yes/No)", "Results communicated in public domain (Yes/No) If yes, provide the web-link."});

                List<BrsrReportRequest.LcaEntry> lcaList = data.getLcaEntries();
                if (lcaList != null && !lcaList.isEmpty()) {
                    for (BrsrReportRequest.LcaEntry lca : lcaList) {
                        addDynamicRow(tLca, new String[]{checkNull(lca.getNicCode()), checkNull(lca.getProductName()), checkNull(lca.getTurnoverPercentage()), checkNull(lca.getBoundary()), checkNull(lca.getIndependentAgency()), checkNull(lca.getPublicDomainResult())});
                    }
                } else { addDynamicRow(tLca, new String[]{"-", "-", "-", "-", "-", "-"}); }
                doc.createParagraph().createRun().addBreak();
            }

            // --- P2 Lead Q2: Risks from LCA ---
            addBoldText(doc, "2. If there are any significant social or environmental concerns and/or risks arising from production or disposal of your products / services, as identified in the Life Cycle Perspective / Assessments (LCA) or through any other means, briefly describe the same along-with action taken to mitigate the same.");
            if (data.isP2Lead2NA()) {
                doc.createParagraph().createRun().setText("Not Applicable");
                doc.createParagraph().createRun().addBreak();
            } else {
                List<BrsrReportRequest.LcaRisk> riskList = data.getLcaRisks();
                if (riskList != null && !riskList.isEmpty()) {
                    XWPFTable tRisk = doc.createTable();
                    tRisk.setWidth("100%");
                    addDynamicHeaderRow(tRisk, new String[]{"Name of Product / Service", "Description of the risk / concern", "Action Taken"});
                    for (BrsrReportRequest.LcaRisk r : riskList) {
                        XWPFTableRow row = tRisk.createRow();
                        ensureCells(row, 3);
                        styleCell(row.getCell(0), checkNull(r.getProductName()), false);
                        fillCellWithNewlines(row.getCell(1), checkNull(r.getRiskDescription()));
                        fillCellWithNewlines(row.getCell(2), checkNull(r.getActionTaken()));
                    }
                } else { addNote(doc, data.getLcaRiskNote()); }
                doc.createParagraph().createRun().addBreak();
            }

            // --- P2 Lead Q3: Recycled Inputs Table ---
            addBoldText(doc, "3. Percentage of recycled or reused input material to total material (by value) used in production (for manufacturing industry) or providing services (for service industry).");
            if (data.isP2Lead3NA()) {
                doc.createParagraph().createRun().setText("Not Applicable");
                doc.createParagraph().createRun().addBreak();
            } else {
                XWPFTable tRecMat = doc.createTable();
                tRecMat.setWidth("100%");
                XWPFTableRow hrRec1 = tRecMat.getRow(0);
                ensureCells(hrRec1, 3);
                styleCell(hrRec1.getCell(0), "Indicate input material", true);
                styleCell(hrRec1.getCell(1), "Recycled or re-used input material to total material", true);
                mergeCellsHorizontal(hrRec1, 1, 2);
                XWPFTableRow hrRec2 = tRecMat.createRow();
                ensureCells(hrRec2, 3);
                styleCell(hrRec2.getCell(0), "", true);
                styleCell(hrRec2.getCell(1), fyCurP2, true);
                styleCell(hrRec2.getCell(2), fyPrevP2, true);

                List<BrsrReportRequest.RecycledInput> recInpList = data.getRecycledInputs();
                if (recInpList != null && !recInpList.isEmpty()) {
                    for (BrsrReportRequest.RecycledInput r : recInpList) {
                        addDynamicRow(tRecMat, new String[]{checkNull(r.getMaterialName()), checkNull(r.getCurrentFyPercentage()), checkNull(r.getPreviousFyPercentage())});
                    }
                } else { addDynamicRow(tRecMat, new String[]{"-", "-", "-"}); }
                addNote(doc, data.getRecycledInputNote());
                doc.createParagraph().createRun().addBreak();
            }

            // --- P2 Lead Q4: Reclaimed EoL Products ---
            addBoldText(doc, "4. Of the products and packaging reclaimed at end of life of products, amount (in metric tonnes) reused, recycled, and safely disposed, as per the following format:");
            if (data.isP2Lead4NA()) {
                doc.createParagraph().createRun().setText("Not Applicable");
                doc.createParagraph().createRun().addBreak();
            } else {
                XWPFTable tRecVol = doc.createTable();
                tRecVol.setWidth("100%");
                XWPFTableRow hrVol1 = tRecVol.getRow(0);
                ensureCells(hrVol1, 7);
                styleCell(hrVol1.getCell(0), "Category", true);
                styleCell(hrVol1.getCell(1), fyCurP2, true); mergeCellsHorizontal(hrVol1, 1, 3);
                styleCell(hrVol1.getCell(4), fyPrevP2, true); mergeCellsHorizontal(hrVol1, 4, 6);

                XWPFTableRow hrVol2 = tRecVol.createRow();
                ensureCells(hrVol2, 7);
                styleCell(hrVol2.getCell(0), "", true);
                styleCell(hrVol2.getCell(1), "Re-Used", true); styleCell(hrVol2.getCell(2), "Recycled", true); styleCell(hrVol2.getCell(3), "Safely Disposed", true);
                styleCell(hrVol2.getCell(4), "Re-Used", true); styleCell(hrVol2.getCell(5), "Recycled", true); styleCell(hrVol2.getCell(6), "Safely Disposed", true);

                addDynamicRow(tRecVol, new String[]{"Plastics (including packaging)", checkNull(data.getPlasticReusedCurr()), checkNull(data.getPlasticRecycledCurr()), checkNull(data.getPlasticDisposedCurr()), checkNull(data.getPlasticReusedPrev()), checkNull(data.getPlasticRecycledPrev()), checkNull(data.getPlasticDisposedPrev())});
                addDynamicRow(tRecVol, new String[]{"E-waste", checkNull(data.getEwasteReusedCurr()), checkNull(data.getEwasteRecycledCurr()), checkNull(data.getEwasteDisposedCurr()), checkNull(data.getEwasteReusedPrev()), checkNull(data.getEwasteRecycledPrev()), checkNull(data.getEwasteDisposedPrev())});
                addDynamicRow(tRecVol, new String[]{"Hazardous waste", checkNull(data.getHazardReusedCurr()), checkNull(data.getHazardRecycledCurr()), checkNull(data.getHazardDisposedCurr()), checkNull(data.getHazardReusedPrev()), checkNull(data.getHazardRecycledPrev()), checkNull(data.getHazardDisposedPrev())});
                addDynamicRow(tRecVol, new String[]{"Other waste", checkNull(data.getOtherReusedCurr()), checkNull(data.getOtherRecycledCurr()), checkNull(data.getOtherDisposedCurr()), checkNull(data.getOtherReusedPrev()), checkNull(data.getOtherRecycledPrev()), checkNull(data.getOtherDisposedPrev())});

                addNote(doc, data.getReclaimedWasteNote());
                doc.createParagraph().createRun().addBreak();
            }

            // --- P2 Lead Q5: Reclaimed % ---
            addBoldText(doc, "5. Reclaimed products and their packaging materials (as percentage of products sold) for each product category.");
            if (data.isP2Lead5NA()) {
                doc.createParagraph().createRun().setText("Not Applicable");
                doc.createParagraph().createRun().addBreak();
            } else {
                XWPFTable tRecPerc = doc.createTable();
                tRecPerc.setWidth("100%");
                addDynamicHeaderRow(tRecPerc, new String[]{"Indicate product category", "Reclaimed products and their packaging materials as % of total products sold in respective category"});

                List<BrsrReportRequest.ReclaimedPercentage> recPercList = data.getReclaimedPercentages();
                if (recPercList != null && !recPercList.isEmpty()) {
                    for (BrsrReportRequest.ReclaimedPercentage r : recPercList) {
                        addDynamicRow(tRecPerc, new String[]{checkNull(r.getCategory()), checkNull(r.getPercentage())});
                    }
                } else { addDynamicRow(tRecPerc, new String[]{"-", "-"}); }
                addNote(doc, data.getReclaimedPercentageNote());
                doc.createParagraph().createRun().addBreak();
            }

            // --- PRINCIPLE 3 HEADER ---
            // ==================================================================================
            // SECTION C: PRINCIPLE 3 (Employee & Worker Well-being)
            // ==================================================================================
            // --- PRINCIPLE 3 HEADER ---
            // ==================================================================================
            // SECTION C: PRINCIPLE 3 (Employee & Worker Well-being)
            // ==================================================================================
            XWPFParagraph pP3 = doc.createParagraph();
            pP3.setPageBreak(true);
            XWPFRun rP3 = pP3.createRun();
            rP3.setText("PRINCIPLE 3: Businesses should respect and promote the well-being of all employees, including those in their value chains");
            rP3.setBold(true);
            rP3.setFontSize(12);
            rP3.setColor("108a55"); // Your green theme color
            rP3.setFontFamily("Calibri");
            pP3.setSpacingAfter(200);

            addBoldText(doc, "Essential Indicators");

            // --- P3 Q1.a: Wellbeing (Employees) ---
            addBoldText(doc, "1. a. Details of measures for the well-being of employees:");
            generateWellBeingTable(doc, data.getEmployeeWellBeing(), "Permanent employees", "Other than Permanent employees");

            if (data.getEmpWellBeingNote() != null && !data.getEmpWellBeingNote().trim().isEmpty()) {
                addNote(doc, data.getEmpWellBeingNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q1.b: Wellbeing (Workers) ---
            addBoldText(doc, "1. b. Details of measures for the well-being of workers:");
            generateWellBeingTable(doc, data.getWorkerWellBeing(), "Permanent workers", "Other than Permanent workers");

            if (data.getWorkWellBeingNote() != null && !data.getWorkWellBeingNote().trim().isEmpty()) {
                addNote(doc, data.getWorkWellBeingNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q1.c: Spending on Wellbeing ---
            addBoldText(doc, "c. Spending on measures towards well-being of employees and workers (including permanent and other than permanent) in the following format:");

            XWPFTable tCost = doc.createTable();
            tCost.setWidth("100%");

            // Note: Uses the global FY variables if available
            String fyCurP3 = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "Current Financial Year";
            String fyPrevP3 = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "Previous Financial Year";

            addDynamicHeaderRow(tCost, new String[]{
                    "",
                    "FY " + fyCurP3,
                    "FY " + fyPrevP3
            });

            addDynamicRow(tCost, new String[]{
                    "Cost incurred on well-being measures as a % of total revenue of the company",
                    checkNull(data.getWbCostCurr()),
                    checkNull(data.getWbCostPrev())
            });

            if (data.getWbCostNote() != null && !data.getWbCostNote().trim().isEmpty()) {
                addNote(doc, data.getWbCostNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q2: Retirement Benefits ---
            addBoldText(doc, "2. Details of retirement benefits, for Current FY and Previous Financial Year.");
            XWPFTable tRet = doc.createTable();
            tRet.setWidth("100%");

            // Note: fyCurP3 and fyPrevP3 are already declared in Q1.c, so we just use them directly!

            // Header Row 1
            XWPFTableRow hrRet1 = tRet.getRow(0);
            ensureCells(hrRet1, 7);
            styleCell(hrRet1.getCell(0), "Benefits", true);
            styleCell(hrRet1.getCell(1), "FY " + fyCurP3 + "\nCurrent Financial Year", true);
            mergeCellsHorizontal(hrRet1, 1, 3);
            styleCell(hrRet1.getCell(4), "FY " + fyPrevP3 + "\nPrevious Financial Year", true);
            mergeCellsHorizontal(hrRet1, 4, 6);

            // Header Row 2
            XWPFTableRow hrRet2 = tRet.createRow();
            ensureCells(hrRet2, 7);
            styleCell(hrRet2.getCell(0), "", true);
            styleCell(hrRet2.getCell(1), "No. of employees covered as a % of total employees", true);
            styleCell(hrRet2.getCell(2), "No. of workers covered as a % of total workers", true);
            styleCell(hrRet2.getCell(3), "Deducted and deposited with the authority (Y/N/N.A.)", true);
            styleCell(hrRet2.getCell(4), "No. of employees covered as a % of total employees", true);
            styleCell(hrRet2.getCell(5), "No. of workers covered as a % of total workers", true);
            styleCell(hrRet2.getCell(6), "Deducted and deposited with the authority (Y/N/N.A.)", true);

            // Table Data
            List<BrsrReportRequest.RetirementBenefit> retList = data.getRetirementBenefits();
            if (retList != null && !retList.isEmpty()) {
                for (BrsrReportRequest.RetirementBenefit rb : retList) {
                    addDynamicRow(tRet, new String[]{
                            checkNull(rb.getBenefits()),
                            checkNull(rb.getCurrEmpCovered()),
                            checkNull(rb.getCurrWorkCovered()),
                            checkNull(rb.getCurrDeducted()),
                            checkNull(rb.getPrevEmpCovered()),
                            checkNull(rb.getPrevWorkCovered()),
                            checkNull(rb.getPrevDeducted())
                    });
                }
            } else {
                // Inject default rows if data is missing, matching the SEBI format
                addDynamicRow(tRet, new String[]{"PF", "-", "-", "-", "-", "-", "-"});
                addDynamicRow(tRet, new String[]{"Gratuity", "-", "-", "-", "-", "-", "-"});
                addDynamicRow(tRet, new String[]{"ESI", "-", "-", "-", "-", "-", "-"});
                addDynamicRow(tRet, new String[]{"Others - please specify", "-", "-", "-", "-", "-", "-"});
            }

            if (data.getRetirementBenefitNote() != null && !data.getRetirementBenefitNote().trim().isEmpty()) {
                addNote(doc, data.getRetirementBenefitNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q3: Accessibility ---
            addBoldText(doc, "3. Accessibility of workplaces");
            doc.createParagraph().createRun().setText("Are the premises / offices of the entity accessible to differently abled employees and workers, as per the requirements of the Rights of Persons with Disabilities Act, 2016? If not, whether any steps are being taken by the entity in this regard.");

            // Fixed: Using your existing setTextWithBreaks method
            XWPFParagraph pAccessAns = doc.createParagraph();
            setTextWithBreaks(pAccessAns.createRun(), checkNull(data.getAccessibilityDetails()));
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q4: Equal Opportunity Policy ---
            addBoldText(doc, "4. Does the entity have an equal opportunity policy as per the Rights of Persons with Disabilities Act, 2016? If so, provide a web-link to the policy.");
            String q4Text = checkNull(data.getEqualOppPolicy());
            if (data.getEqualOppLink() != null && !data.getEqualOppLink().trim().isEmpty()) {
                q4Text += " | Web-link: " + data.getEqualOppLink();
            }

            // Fixed: Using your existing setTextWithBreaks method
            XWPFParagraph pEqual = doc.createParagraph();
            setTextWithBreaks(pEqual.createRun(), q4Text);

            if (data.getEqualOppDetails() != null && !data.getEqualOppDetails().trim().isEmpty()) {
                addNote(doc, data.getEqualOppDetails());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q5: Parental Leave ---
            addBoldText(doc, "5. Return to work and Retention rates of permanent employees and workers that took parental leave.");
            XWPFTable table5P3 = doc.createTable();
            table5P3.setWidth("100%");

            XWPFTableRow hRow5_1 = table5P3.getRow(0);
            ensureCells(hRow5_1, 5);
            styleCell(hRow5_1.getCell(0), "", true); // Blank header above Gender
            styleCell(hRow5_1.getCell(1), "Permanent employees", true);
            mergeCellsHorizontal(hRow5_1, 1, 2);
            styleCell(hRow5_1.getCell(3), "Permanent workers", true);
            mergeCellsHorizontal(hRow5_1, 3, 4);

            XWPFTableRow hRow5_2 = table5P3.createRow();
            ensureCells(hRow5_2, 5);
            styleCell(hRow5_2.getCell(0), "Gender", true);
            styleCell(hRow5_2.getCell(1), "Return to work rate", true);
            styleCell(hRow5_2.getCell(2), "Retention rate", true);
            styleCell(hRow5_2.getCell(3), "Return to work rate", true);
            styleCell(hRow5_2.getCell(4), "Retention rate", true);

            addDynamicRow(table5P3, new String[]{"Male", checkNull(data.getPlEmpMaleReturn()), checkNull(data.getPlEmpMaleRetain()), checkNull(data.getPlWorkMaleReturn()), checkNull(data.getPlWorkMaleRetain())});
            addDynamicRow(table5P3, new String[]{"Female", checkNull(data.getPlEmpFemaleReturn()), checkNull(data.getPlEmpFemaleRetain()), checkNull(data.getPlWorkFemaleReturn()), checkNull(data.getPlWorkFemaleRetain())});

            // Render Total row with Bold styling
            XWPFTableRow tRowTotal = table5P3.createRow();
            ensureCells(tRowTotal, 5);
            styleCell(tRowTotal.getCell(0), "Total", true);
            styleCell(tRowTotal.getCell(1), checkNull(data.getPlEmpTotalReturn()), true);
            styleCell(tRowTotal.getCell(2), checkNull(data.getPlEmpTotalRetain()), true);
            styleCell(tRowTotal.getCell(3), checkNull(data.getPlWorkTotalReturn()), true);
            styleCell(tRowTotal.getCell(4), checkNull(data.getPlWorkTotalRetain()), true);

            if (data.getParentalLeaveNote() != null && !data.getParentalLeaveNote().trim().isEmpty()) {
                addNote(doc, data.getParentalLeaveNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q6: Grievance Mechanism ---
            addBoldText(doc, "6. Is there a mechanism available to receive and redress grievances for the following categories of employees and worker? If yes, give details of the mechanism in brief.");
            XWPFTable table6P3 = doc.createTable();
            table6P3.setWidth("100%");
            setColumnWidths(table6P3, 4500, 5500);

            XWPFTableRow hRow6 = table6P3.getRow(0);
            ensureCells(hRow6, 2);
            styleCell(hRow6.getCell(0), "", true);
            styleCell(hRow6.getCell(1), "Yes/No\n(If Yes, then give details of the mechanism in brief)", true);

            // Using addDynamicRow but stripping out 'Yes,' prefixes if you want them clean, though keeping them is fine.
            addDynamicRow(table6P3, new String[]{"Permanent Workers", checkNull(data.getGrievancePermWorkers())});
            addDynamicRow(table6P3, new String[]{"Other than Permanent Workers", checkNull(data.getGrievanceTempWorkers())});
            addDynamicRow(table6P3, new String[]{"Permanent Employees", checkNull(data.getGrievancePermEmployees())});
            addDynamicRow(table6P3, new String[]{"Other than Permanent Employees", checkNull(data.getGrievanceTempEmployees())});

            doc.createParagraph().createRun().addBreak();

            // --- P3 Q7: Union Membership ---
            addBoldText(doc, "7. Membership of employees and worker in association(s) or Unions recognised by the listed entity:");
            XWPFTable table7 = doc.createTable();
            table7.setWidth("100%");

            // Note: fyCurP3 and fyPrevP3 are available from Q2
            XWPFTableRow hRow7_1 = table7.getRow(0);
            ensureCells(hRow7_1, 7);
            styleCell(hRow7_1.getCell(0), "Category", true);
            styleCell(hRow7_1.getCell(1), "FY " + fyCurP3 + "\n(Current Financial Year)", true);
            mergeCellsHorizontal(hRow7_1, 1, 3);
            styleCell(hRow7_1.getCell(4), "FY " + fyPrevP3 + "\n(Previous Financial Year)", true);
            mergeCellsHorizontal(hRow7_1, 4, 6);

            XWPFTableRow hRow7_2 = table7.createRow();
            ensureCells(hRow7_2, 7);
            styleCell(hRow7_2.getCell(0), "", true);
            styleCell(hRow7_2.getCell(1), "Total employees / workers in respective category\n(A)", true);
            styleCell(hRow7_2.getCell(2), "No. of employees / workers in respective category, who are part of association(s) or Union\n(B)", true);
            styleCell(hRow7_2.getCell(3), "% (B / A)", true);
            styleCell(hRow7_2.getCell(4), "Total employees / workers in respective category\n(C)", true);
            styleCell(hRow7_2.getCell(5), "No. of employees / workers in respective category, who are part of association(s) or Union\n(D)", true);
            styleCell(hRow7_2.getCell(6), "% (D / C)", true);

            // Row order altered to match SEBI: Total first, then Male/Female
            addDynamicRow(table7, new String[]{"Total Permanent Employees", checkNull(data.getUnionCurrEmpTotalA()), checkNull(data.getUnionCurrEmpUnionB()), checkNull(data.getUnionCurrEmpPerc()), checkNull(data.getUnionPrevEmpTotalC()), checkNull(data.getUnionPrevEmpUnionD()), checkNull(data.getUnionPrevEmpPerc())});
            addDynamicRow(table7, new String[]{"- Male", checkNull(data.getUnionCurrEmpMaleTotal()), checkNull(data.getUnionCurrEmpMaleUnion()), checkNull(data.getUnionCurrEmpMalePerc()), checkNull(data.getUnionPrevEmpMaleTotal()), checkNull(data.getUnionPrevEmpMaleUnion()), checkNull(data.getUnionPrevEmpMalePerc())});
            addDynamicRow(table7, new String[]{"- Female", checkNull(data.getUnionCurrEmpFemaleTotal()), checkNull(data.getUnionCurrEmpFemaleUnion()), checkNull(data.getUnionCurrEmpFemalePerc()), checkNull(data.getUnionPrevEmpFemaleTotal()), checkNull(data.getUnionPrevEmpFemaleUnion()), checkNull(data.getUnionPrevEmpFemalePerc())});

            addDynamicRow(table7, new String[]{"Total Permanent Workers", checkNull(data.getUnionCurrWorkTotalA()), checkNull(data.getUnionCurrWorkUnionB()), checkNull(data.getUnionCurrWorkPerc()), checkNull(data.getUnionPrevWorkTotalC()), checkNull(data.getUnionPrevWorkUnionD()), checkNull(data.getUnionPrevWorkPerc())});
            addDynamicRow(table7, new String[]{"- Male", checkNull(data.getUnionCurrWorkMaleTotal()), checkNull(data.getUnionCurrWorkMaleUnion()), checkNull(data.getUnionCurrWorkMalePerc()), checkNull(data.getUnionPrevWorkMaleTotal()), checkNull(data.getUnionPrevWorkMaleUnion()), checkNull(data.getUnionPrevWorkMalePerc())});
            addDynamicRow(table7, new String[]{"- Female", checkNull(data.getUnionCurrWorkFemaleTotal()), checkNull(data.getUnionCurrWorkFemaleUnion()), checkNull(data.getUnionCurrWorkFemalePerc()), checkNull(data.getUnionPrevWorkFemaleTotal()), checkNull(data.getUnionPrevWorkFemaleUnion()), checkNull(data.getUnionPrevWorkFemalePerc())});

            if (data.getUnionMembershipNote() != null && !data.getUnionMembershipNote().trim().isEmpty()) {
                addNote(doc, data.getUnionMembershipNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q8: TRAINING DETAILS ---
            addBoldText(doc, "8. Details of training given to employees and workers:");
            XWPFTable table8 = doc.createTable();
            table8.setWidth("100%");

            // Header 1 (FY span)
            XWPFTableRow hRow8_1 = table8.getRow(0);
            ensureCells(hRow8_1, 11);
            styleCell(hRow8_1.getCell(0), "Category", true);
            styleCell(hRow8_1.getCell(1), "FY " + fyCurP3 + "\nCurrent Financial Year", true);
            mergeCellsHorizontal(hRow8_1, 1, 5);
            styleCell(hRow8_1.getCell(6), "FY " + fyPrevP3 + "\nPrevious Financial Year", true);
            mergeCellsHorizontal(hRow8_1, 6, 10);

            // Header 2 (Total / Health / Skill)
            XWPFTableRow hRow8_2 = table8.createRow();
            ensureCells(hRow8_2, 11);
            styleCell(hRow8_2.getCell(0), "", true); // Blank under Category
            styleCell(hRow8_2.getCell(1), "Total (A)", true);
            styleCell(hRow8_2.getCell(2), "On Health and safety measures", true);
            mergeCellsHorizontal(hRow8_2, 2, 3);
            styleCell(hRow8_2.getCell(4), "On Skill upgradation", true);
            mergeCellsHorizontal(hRow8_2, 4, 5);
            styleCell(hRow8_2.getCell(6), "Total (D)", true);
            styleCell(hRow8_2.getCell(7), "On Health and safety measures", true);
            mergeCellsHorizontal(hRow8_2, 7, 8);
            styleCell(hRow8_2.getCell(9), "On Skill upgradation", true);
            mergeCellsHorizontal(hRow8_2, 9, 10);

            // Header 3 (No. and Percents)
            XWPFTableRow hRow8_3 = table8.createRow();
            ensureCells(hRow8_3, 11);
            styleCell(hRow8_3.getCell(0), "", true);
            styleCell(hRow8_3.getCell(1), "", true); // Blank under Total A
            styleCell(hRow8_3.getCell(2), "No. (B)", true); styleCell(hRow8_3.getCell(3), "% (B / A)", true);
            styleCell(hRow8_3.getCell(4), "No. (C)", true); styleCell(hRow8_3.getCell(5), "% (C / A)", true);
            styleCell(hRow8_3.getCell(6), "", true); // Blank under Total D
            styleCell(hRow8_3.getCell(7), "No. (E)", true); styleCell(hRow8_3.getCell(8), "% (E / D)", true);
            styleCell(hRow8_3.getCell(9), "No. (F)", true); styleCell(hRow8_3.getCell(10), "% (F / D)", true);

            // Table Data
            addSectionTitleRow(table8, "Employees", 11);
            addDynamicRow(table8, new String[]{ "Male", checkNull(data.getTrainEmpMaleTotal()), checkNull(data.getTrainEmpMaleHealthNo()), checkNull(data.getTrainEmpMaleHealthPerc()), checkNull(data.getTrainEmpMaleSkillNo()), checkNull(data.getTrainEmpMaleSkillPerc()), checkNull(data.getTrainEmpMaleTotalPrev()), checkNull(data.getTrainEmpMaleHealthNoPrev()), checkNull(data.getTrainEmpMaleHealthPercPrev()), checkNull(data.getTrainEmpMaleSkillNoPrev()), checkNull(data.getTrainEmpMaleSkillPercPrev()) });
            addDynamicRow(table8, new String[]{ "Female", checkNull(data.getTrainEmpFemaleTotal()), checkNull(data.getTrainEmpFemaleHealthNo()), checkNull(data.getTrainEmpFemaleHealthPerc()), checkNull(data.getTrainEmpFemaleSkillNo()), checkNull(data.getTrainEmpFemaleSkillPerc()), checkNull(data.getTrainEmpFemaleTotalPrev()), checkNull(data.getTrainEmpFemaleHealthNoPrev()), checkNull(data.getTrainEmpFemaleHealthPercPrev()), checkNull(data.getTrainEmpFemaleSkillNoPrev()), checkNull(data.getTrainEmpFemaleSkillPercPrev()) });
            addDynamicRow(table8, new String[]{ "Total", checkNull(data.getTrainEmpGenTotal()), checkNull(data.getTrainEmpGenHealthNo()), checkNull(data.getTrainEmpGenHealthPerc()), checkNull(data.getTrainEmpGenSkillNo()), checkNull(data.getTrainEmpGenSkillPerc()), checkNull(data.getTrainEmpGenTotalPrev()), checkNull(data.getTrainEmpGenHealthNoPrev()), checkNull(data.getTrainEmpGenHealthPercPrev()), checkNull(data.getTrainEmpGenSkillNoPrev()), checkNull(data.getTrainEmpGenSkillPercPrev()) });

            addSectionTitleRow(table8, "Workers", 11);
            addDynamicRow(table8, new String[]{ "Male", checkNull(data.getTrainWorkMaleTotal()), checkNull(data.getTrainWorkMaleHealthNo()), checkNull(data.getTrainWorkMaleHealthPerc()), checkNull(data.getTrainWorkMaleSkillNo()), checkNull(data.getTrainWorkMaleSkillPerc()), checkNull(data.getTrainWorkMaleTotalPrev()), checkNull(data.getTrainWorkMaleHealthNoPrev()), checkNull(data.getTrainWorkMaleHealthPercPrev()), checkNull(data.getTrainWorkMaleSkillNoPrev()), checkNull(data.getTrainWorkMaleSkillPercPrev()) });
            addDynamicRow(table8, new String[]{ "Female", checkNull(data.getTrainWorkFemaleTotal()), checkNull(data.getTrainWorkFemaleHealthNo()), checkNull(data.getTrainWorkFemaleHealthPerc()), checkNull(data.getTrainWorkFemaleSkillNo()), checkNull(data.getTrainWorkFemaleSkillPerc()), checkNull(data.getTrainWorkFemaleTotalPrev()), checkNull(data.getTrainWorkFemaleHealthNoPrev()), checkNull(data.getTrainWorkFemaleHealthPercPrev()), checkNull(data.getTrainWorkFemaleSkillNoPrev()), checkNull(data.getTrainWorkFemaleSkillPercPrev()) });
            addDynamicRow(table8, new String[]{ "Total", checkNull(data.getTrainWorkGenTotal()), checkNull(data.getTrainWorkGenHealthNo()), checkNull(data.getTrainWorkGenHealthPerc()), checkNull(data.getTrainWorkGenSkillNo()), checkNull(data.getTrainWorkGenSkillPerc()), checkNull(data.getTrainWorkGenTotalPrev()), checkNull(data.getTrainWorkGenHealthNoPrev()), checkNull(data.getTrainWorkGenHealthPercPrev()), checkNull(data.getTrainWorkGenSkillNoPrev()), checkNull(data.getTrainWorkGenSkillPercPrev()) });

            if (data.getTrainingDetailsNote() != null && !data.getTrainingDetailsNote().trim().isEmpty()) {
                addNote(doc, data.getTrainingDetailsNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q9: PERFORMANCE REVIEWS ---
            addBoldText(doc, "9. Details of performance and career development reviews of employees and worker:");
            XWPFTable table9 = doc.createTable();
            table9.setWidth("100%");

            XWPFTableRow hRow9_1 = table9.getRow(0);
            ensureCells(hRow9_1, 7);
            styleCell(hRow9_1.getCell(0), "Category", true);
            styleCell(hRow9_1.getCell(1), "FY " + fyCurP3 + "\nCurrent Financial Year", true);
            mergeCellsHorizontal(hRow9_1, 1, 3);
            styleCell(hRow9_1.getCell(4), "FY " + fyPrevP3 + "\nPrevious Financial Year", true);
            mergeCellsHorizontal(hRow9_1, 4, 6);

            XWPFTableRow hRow9_2 = table9.createRow();
            ensureCells(hRow9_2, 7);
            styleCell(hRow9_2.getCell(0), "", true);
            styleCell(hRow9_2.getCell(1), "Total (A)", true);
            styleCell(hRow9_2.getCell(2), "No. (B)", true);
            styleCell(hRow9_2.getCell(3), "% (B / A)", true);
            styleCell(hRow9_2.getCell(4), "Total (C)", true);
            styleCell(hRow9_2.getCell(5), "No. (D)", true);
            styleCell(hRow9_2.getCell(6), "% (D / C)", true);

            addSectionTitleRow(table9, "Employees", 7);
            addDynamicRow(table9, new String[]{ "Male", checkNull(data.getRevEmpMaleTotal()), checkNull(data.getRevEmpMaleCovered()), checkNull(data.getRevEmpMalePerc()), checkNull(data.getRevEmpMaleTotalPrev()), checkNull(data.getRevEmpMaleCoveredPrev()), checkNull(data.getRevEmpMalePercPrev())});
            addDynamicRow(table9, new String[]{ "Female", checkNull(data.getRevEmpFemTotal()), checkNull(data.getRevEmpFemCovered()), checkNull(data.getRevEmpFemPerc()), checkNull(data.getRevEmpFemTotalPrev()), checkNull(data.getRevEmpFemCoveredPrev()), checkNull(data.getRevEmpFemPercPrev())});
            addDynamicRow(table9, new String[]{ "Total", checkNull(data.getRevEmpGenTotal()), checkNull(data.getRevEmpGenCovered()), checkNull(data.getRevEmpGenPerc()), checkNull(data.getRevEmpGenTotalPrev()), checkNull(data.getRevEmpGenCoveredPrev()), checkNull(data.getRevEmpGenPercPrev())});

            addSectionTitleRow(table9, "Workers", 7);
            addDynamicRow(table9, new String[]{ "Male", checkNull(data.getRevWorkMaleTotal()), checkNull(data.getRevWorkMaleCovered()), checkNull(data.getRevWorkMalePerc()), checkNull(data.getRevWorkMaleTotalPrev()), checkNull(data.getRevWorkMaleCoveredPrev()), checkNull(data.getRevWorkMalePercPrev())});
            addDynamicRow(table9, new String[]{ "Female", checkNull(data.getRevWorkFemTotal()), checkNull(data.getRevWorkFemCovered()), checkNull(data.getRevWorkFemPerc()), checkNull(data.getRevWorkFemTotalPrev()), checkNull(data.getRevWorkFemCoveredPrev()), checkNull(data.getRevWorkFemPercPrev())});
            addDynamicRow(table9, new String[]{ "Total", checkNull(data.getRevWorkGenTotal()), checkNull(data.getRevWorkGenCovered()), checkNull(data.getRevWorkGenPerc()), checkNull(data.getRevWorkGenTotalPrev()), checkNull(data.getRevWorkGenCoveredPrev()), checkNull(data.getRevWorkGenPercPrev())});

            if (data.getReviewDetailsNote() != null && !data.getReviewDetailsNote().trim().isEmpty()) {
                addNote(doc, data.getReviewDetailsNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q10: HEALTH & SAFETY ---
            addBoldText(doc, "10. Health and safety management system:");
            doc.createParagraph().createRun().addBreak();

            doc.createParagraph().createRun().setText("a. Whether an occupational health and safety management system has been implemented by the entity? (Yes/ No). If yes, the coverage such system?");
            XWPFParagraph p10a = doc.createParagraph();
            setTextWithBreaks(p10a.createRun(), checkNull(data.getHealthSafetySystem()));
            p10a.setSpacingAfter(200);

            doc.createParagraph().createRun().setText("b. What are the processes used to identify work-related hazards and assess risks on a routine and non-routine basis by the entity?");
            XWPFParagraph p10b = doc.createParagraph();
            setTextWithBreaks(p10b.createRun(), checkNull(data.getHazardIdentification()));
            p10b.setSpacingAfter(200);

            doc.createParagraph().createRun().setText("c. Whether you have processes for workers to report the work related hazards and to remove themselves from such risks. (Y/N)");
            XWPFParagraph p10c = doc.createParagraph();
            setTextWithBreaks(p10c.createRun(), checkNull(data.getHazardReporting()));
            p10c.setSpacingAfter(200);

            doc.createParagraph().createRun().setText("d. Do the employees/ worker of the entity have access to non-occupational medical and healthcare services? (Yes/ No)");
            XWPFParagraph p10d = doc.createParagraph();
            setTextWithBreaks(p10d.createRun(), checkNull(data.getMedicalAccess()));
            p10d.setSpacingAfter(200);
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q11: SAFETY INCIDENTS ---
            addBoldText(doc, "11. Details of safety related incidents, in the following format:");
            XWPFTable table11 = doc.createTable();
            table11.setWidth("100%");
            XWPFTableRow hRow11 = table11.getRow(0);
            ensureCells(hRow11, 4);
            styleCell(hRow11.getCell(0), "Safety Incident/Number", true);
            styleCell(hRow11.getCell(1), "Category*", true);
            styleCell(hRow11.getCell(2), "FY " + fyCurP3 + "\nCurrent Financial Year", true);
            styleCell(hRow11.getCell(3), "FY " + fyPrevP3 + "\nPrevious Financial Year", true);

            addSafetyGroupRow(table11, "Lost Time Injury Frequency Rate (LTIFR) (per one million-person hours worked)", checkNull(data.getSafetyLtifrEmpCurr()), checkNull(data.getSafetyLtifrEmpPrev()), checkNull(data.getSafetyLtifrWorkCurr()), checkNull(data.getSafetyLtifrWorkPrev()));
            addSafetyGroupRow(table11, "Total recordable work-related injuries", checkNull(data.getSafetyTotalInjEmpCurr()), checkNull(data.getSafetyTotalInjEmpPrev()), checkNull(data.getSafetyTotalInjWorkCurr()), checkNull(data.getSafetyTotalInjWorkPrev()));
            addSafetyGroupRow(table11, "No. of fatalities", checkNull(data.getSafetyFatalEmpCurr()), checkNull(data.getSafetyFatalEmpPrev()), checkNull(data.getSafetyFatalWorkCurr()), checkNull(data.getSafetyFatalWorkPrev()));
            addSafetyGroupRow(table11, "High consequence work-related injury or ill-health (excluding fatalities)", checkNull(data.getSafetyHighConEmpCurr()), checkNull(data.getSafetyHighConEmpPrev()), checkNull(data.getSafetyHighConWorkCurr()), checkNull(data.getSafetyHighConWorkPrev()));

            XWPFParagraph pTableNote = doc.createParagraph();
            XWPFRun rTableNote = pTableNote.createRun();
            rTableNote.setFontSize(9);
            rTableNote.setText("*Including in the contract workforce");

            if (data.getSafetyIncidentsNote() != null && !data.getSafetyIncidentsNote().trim().isEmpty()) {
                addNote(doc, data.getSafetyIncidentsNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q12: SAFETY MEASURES ---
            addBoldText(doc, "12. Describe the measures taken by the entity to ensure a safe and healthy work place.");
            List<BrsrReportRequest.SafetyMeasure> measures = data.getSafetyMeasures();
            if (measures != null && !measures.isEmpty()) {
                int roman = 1;
                for (BrsrReportRequest.SafetyMeasure measure : measures) {
                    XWPFParagraph pMeasure = doc.createParagraph();
                    pMeasure.setSpacingAfter(100);
                    XWPFRun rHead = pMeasure.createRun();
                    // Using standard numbers instead of roman since SEBI doesn't specify
                    rHead.setText(roman++ + ") " + checkNull(measure.getHeading()));
                    rHead.setBold(true);
                    rHead.addBreak();

                    XWPFRun rDescP3 = pMeasure.createRun();
                    rDescP3.setFontFamily("Calibri");
                    setTextWithBreaks(rDescP3, checkNull(measure.getDescription()));
                }
            } else {
                doc.createParagraph().createRun().setText("Detailed safety measures have been implemented across all facilities.");
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q13: WORKER COMPLAINTS ---
            addBoldText(doc, "13. Number of Complaints on the following made by employees and workers:");
            XWPFTable table13 = doc.createTable();
            table13.setWidth("100%");

            XWPFTableRow hRow13_1 = table13.getRow(0);
            ensureCells(hRow13_1, 7);
            styleCell(hRow13_1.getCell(0), "", true);
            styleCell(hRow13_1.getCell(1), "FY " + fyCurP3 + "\n(Current Financial Year)", true);
            mergeCellsHorizontal(hRow13_1, 1, 3);
            styleCell(hRow13_1.getCell(4), "FY " + fyPrevP3 + "\n(Previous Financial Year)", true);
            mergeCellsHorizontal(hRow13_1, 4, 6);

            XWPFTableRow hRow13_2 = table13.createRow();
            ensureCells(hRow13_2, 7);
            styleCell(hRow13_2.getCell(0), "", true);
            styleCell(hRow13_2.getCell(1), "Filed during the year", true);
            styleCell(hRow13_2.getCell(2), "Pending resolution at the end of year", true);
            styleCell(hRow13_2.getCell(3), "Remarks", true);
            styleCell(hRow13_2.getCell(4), "Filed during the year", true);
            styleCell(hRow13_2.getCell(5), "Pending resolution at the end of year", true);
            styleCell(hRow13_2.getCell(6), "Remarks", true);

            addDynamicRow(table13, new String[]{ "Working Conditions", checkNull(data.getWcFiledCurr()), checkNull(data.getWcPendingCurr()), checkNull(data.getWcRemarksCurr()), checkNull(data.getWcFiledPrev()), checkNull(data.getWcPendingPrev()), checkNull(data.getWcRemarksPrev())});
            addDynamicRow(table13, new String[]{ "Health & Safety", checkNull(data.getHsFiledCurr()), checkNull(data.getHsPendingCurr()), checkNull(data.getHsRemarksCurr()), checkNull(data.getHsFiledPrev()), checkNull(data.getHsPendingPrev()), checkNull(data.getHsRemarksPrev())});

            if (data.getWorkerComplaintsNote() != null && !data.getWorkerComplaintsNote().trim().isEmpty()) {
                addNote(doc, data.getWorkerComplaintsNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q14: ASSESSMENTS ---
            addBoldText(doc, "14. Assessments for the year:");
            XWPFTable table14P3 = doc.createTable();
            table14P3.setWidth("100%");
            setColumnWidths(table14P3, 4000, 6000);

            XWPFTableRow hRow14 = table14P3.getRow(0);
            ensureCells(hRow14, 2);
            styleCell(hRow14.getCell(0), "", true);
            styleCell(hRow14.getCell(1), "% of your plants and offices that were assessed\n(by entity or statutory authorities or third parties)", true);

            // Removing the hardcoded "%" sign from Java since the user might type it in the UI
            addRow(table14P3, "Health and safety practices", checkNull(data.getAssessmentHealthPerc()));
            addRow(table14P3, "Working Conditions", checkNull(data.getAssessmentWorkingPerc()));

            if (data.getAssessmentNote() != null && !data.getAssessmentNote().trim().isEmpty()) {
                addNote(doc, data.getAssessmentNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Q15: CORRECTIVE ACTIONS (SAFETY) ---
            addBoldText(doc, "15. Provide details of any corrective action taken or underway to address safety-related incidents (if any) and on significant risks / concerns arising from assessments of health & safety practices and working conditions.");
            XWPFParagraph p15 = doc.createParagraph();
            setTextWithBreaks(p15.createRun(), checkNull(data.getSafetyCorrectiveActions()));
            doc.createParagraph().createRun().addBreak();

            // ================= P3 LEADERSHIP INDICATORS =================
            // Creating a centered, distinct header for Leadership Indicators
            XWPFParagraph pLeadHead = doc.createParagraph();
            pLeadHead.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun rLeadHead = pLeadHead.createRun();
            rLeadHead.setText("Leadership Indicators");
            rLeadHead.setBold(true);
            rLeadHead.setFontSize(12);
            // Use your green theme color (or a specific hex like "70AD47" to match the image's green)
            rLeadHead.setColor("70AD47");
            doc.createParagraph().createRun().addBreak();

            // --- Lead 1: Life Insurance ---
            addBoldText(doc, "1. Does the entity extend any life insurance or any compensatory package in the event of death of (A) Employees (Y/N) (B) Workers (Y/N).");
            XWPFParagraph pLife = doc.createParagraph();
            XWPFRun rLife = pLife.createRun();
            rLife.setText("A) Employees – " + checkNull(data.getLifeInsuranceEmployees()));
            rLife.addBreak();
            rLife.setText("B) Workers – " + checkNull(data.getLifeInsuranceWorkers()));

            if (data.getLifeInsuranceDetails() != null && !data.getLifeInsuranceDetails().trim().isEmpty()) {
                rLife.addBreak();
                rLife.addBreak();
                setTextWithBreaks(rLife, checkNull(data.getLifeInsuranceDetails()));
            }
            doc.createParagraph().createRun().addBreak();

            // --- Lead 2: Statutory Dues ---
            addBoldText(doc, "2. Provide the measures undertaken by the entity to ensure that statutory dues have been deducted and deposited by the value chain partners.");
            XWPFParagraph pDues = doc.createParagraph();
            setTextWithBreaks(pDues.createRun(), checkNull(data.getStatutoryDuesMeasures()));
            doc.createParagraph().createRun().addBreak();

            // --- Lead 3: Rehabilitation ---
            addBoldText(doc, "3. Provide the number of employees / workers having suffered high consequence work-related injury / ill-health / fatalities (as reported in Q11 of Essential Indicators above), who have been rehabilitated and placed in suitable employment or whose family members have been placed in suitable employment:");
            XWPFTable tableLead3 = doc.createTable();
            tableLead3.setWidth("100%");

            XWPFTableRow hRowL3_1 = tableLead3.getRow(0);
            ensureCells(hRowL3_1, 5);
            styleCell(hRowL3_1.getCell(0), "", true);
            styleCell(hRowL3_1.getCell(1), "Total no. of affected employees/ workers", true);
            mergeCellsHorizontal(hRowL3_1, 1, 2);
            styleCell(hRowL3_1.getCell(3), "No. of employees/workers that are rehabilitated and placed in suitable employment or whose family members have been placed in suitable employment", true);
            mergeCellsHorizontal(hRowL3_1, 3, 4);

            XWPFTableRow hRowL3_2 = tableLead3.createRow();
            ensureCells(hRowL3_2, 5);
            styleCell(hRowL3_2.getCell(0), "", true);
            styleCell(hRowL3_2.getCell(1), "FY " + fyCurP3 + "\n(Current Financial Year)", true);
            styleCell(hRowL3_2.getCell(2), "FY " + fyPrevP3 + "\n(Previous Financial Year)", true);
            styleCell(hRowL3_2.getCell(3), "FY " + fyCurP3 + "\n(Current Financial Year)", true);
            styleCell(hRowL3_2.getCell(4), "FY " + fyPrevP3 + "\n(Previous Financial Year)", true);

            addDynamicRow(tableLead3, new String[]{ "Employees", checkNull(data.getRehabEmpAffCurr()), checkNull(data.getRehabEmpAffPrev()), checkNull(data.getRehabEmpPlacedCurr()), checkNull(data.getRehabEmpPlacedPrev()) });
            addDynamicRow(tableLead3, new String[]{ "Workers", checkNull(data.getRehabWorkAffCurr()), checkNull(data.getRehabWorkAffPrev()), checkNull(data.getRehabWorkPlacedCurr()), checkNull(data.getRehabWorkPlacedPrev()) });

            if (data.getRehabilitationNote() != null && !data.getRehabilitationNote().trim().isEmpty()) {
                addNote(doc, data.getRehabilitationNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Lead 4: TRANSITION ASSISTANCE ---
            addBoldText(doc, "4. Does the entity provide transition assistance programs to facilitate continued employability and the management of career endings resulting from retirement or termination of employment? (Yes/ No)");
            XWPFParagraph pTrans = doc.createParagraph();
            XWPFRun rTrans = pTrans.createRun();

            // Appends Yes/No answer, then details
            rTrans.setText(checkNull(data.getTransitionAssistanceYN()));
            if (data.getTransitionAssistanceDetails() != null && !data.getTransitionAssistanceDetails().trim().isEmpty()) {
                rTrans.addBreak();
                setTextWithBreaks(rTrans, data.getTransitionAssistanceDetails());
            }
            pTrans.setSpacingAfter(200);
            doc.createParagraph().createRun().addBreak();

            // --- P3 Lead 5: VALUE CHAIN ASSESSMENT ---
            addBoldText(doc, "5. Details on assessment of value chain partners:");

            XWPFTable tableLead5 = doc.createTable();
            tableLead5.setWidth("100%");
            setColumnWidths(tableLead5, 4000, 6000);

            XWPFTableRow hRowL5 = tableLead5.getRow(0);
            ensureCells(hRowL5, 2);
            styleCell(hRowL5.getCell(0), "", true);
            styleCell(hRowL5.getCell(1), "% of value chain partners (by value of business done with such partners) that were assessed", true);

            addRow(tableLead5, "Health and safety practices", checkNull(data.getVcHealthSafetyPerc()));
            addRow(tableLead5, "Working Conditions", checkNull(data.getVcWorkingCondPerc()));

            String vcNote = data.getValueChainAssessmentNote();
            if (vcNote != null && !vcNote.trim().isEmpty()) {
                XWPFParagraph pVcNote = doc.createParagraph();
                pVcNote.setSpacingBefore(100);
                setTextWithBreaks(pVcNote.createRun(), vcNote);
            }
            doc.createParagraph().createRun().addBreak();

            // --- P3 Lead 6: VALUE CHAIN CORRECTIVE ACTIONS ---
            addBoldText(doc, "6. Provide details of any corrective actions taken or underway to address significant risks / concerns arising from assessments of health and safety practices and working conditions of value chain partners.");

            XWPFParagraph pVcIntro = doc.createParagraph();
            XWPFRun rVcIntro = pVcIntro.createRun();
            setTextWithBreaks(rVcIntro, checkNull(data.getVcCorrectiveActionIntro()));
            pVcIntro.setSpacingAfter(100);

            List<BrsrReportRequest.ValueChainAction> vcActions = data.getVcCorrectiveActions();
            if (vcActions != null && !vcActions.isEmpty()) {
                for (BrsrReportRequest.ValueChainAction action : vcActions) {
                    XWPFParagraph pBullet = doc.createParagraph();
                    pBullet.setSpacingAfter(100);
                    pBullet.setIndentationLeft(720); // Indent 0.5 inch
                    pBullet.setIndentationHanging(360);

                    XWPFRun rBullet = pBullet.createRun();
                    rBullet.setText("• ");

                    XWPFRun rText = pBullet.createRun();
                    setTextWithBreaks(rText, checkNull(action.getActionText()));
                }
            } else {
                XWPFParagraph pNone = doc.createParagraph();
                pNone.createRun().setText("No specific actions listed.");
            }
            doc.createParagraph().createRun().addBreak();

            // --- PRINCIPLE 4 HEADER ---
            XWPFParagraph pP4 = doc.createParagraph();
            pP4.setSpacingBefore(300);
            XWPFRun rP4 = pP4.createRun();
            rP4.setText("PRINCIPLE 4: Businesses should respect the interests of and be responsive to all its stakeholders");
            rP4.setBold(true);
            rP4.setFontSize(12);
            rP4.setColor("548235"); // The specific SEBI green color

            // Essential Indicators Title (matching the clean, left-aligned style)
            XWPFParagraph p4Ess = doc.createParagraph();
            p4Ess.setSpacingBefore(100);
            XWPFRun rP4Ess = p4Ess.createRun();
            rP4Ess.setText("Essential Indicators");
            rP4Ess.setBold(true);
            rP4Ess.setFontSize(11);
            doc.createParagraph().createRun().addBreak();

            // --- 1. STAKEHOLDER IDENTIFICATION ---
            addBoldText(doc, "1. Describe the processes for identifying key stakeholder groups of the entity.");

            XWPFParagraph pP4Intro = doc.createParagraph();
            setTextWithBreaks(pP4Intro.createRun(), checkNull(data.getPrinciple4Q1Intro()));
            pP4Intro.setSpacingAfter(100);

            List<String> p4Points = data.getPrinciple4Q1Points();
            if (p4Points != null && !p4Points.isEmpty()) {
                int num = 1;
                for (String point : p4Points) {
                    XWPFParagraph pPoint = doc.createParagraph();
                    pPoint.setIndentationLeft(720);   // Indent 0.5 inch
                    pPoint.setIndentationHanging(360); // Hanging indent
                    pPoint.setSpacingAfter(100);

                    XWPFRun rPoint = pPoint.createRun();
                    rPoint.setText(num++ + ".  " + point);
                }
            }

            if (data.getPrinciple4Q1Conclusion() != null && !data.getPrinciple4Q1Conclusion().trim().isEmpty()) {
                XWPFParagraph pP4Concl = doc.createParagraph();
                pP4Concl.setSpacingBefore(100);
                setTextWithBreaks(pP4Concl.createRun(), checkNull(data.getPrinciple4Q1Conclusion()));
            }
            doc.createParagraph().createRun().addBreak();

            // --- 2. STAKEHOLDER ENGAGEMENT TABLE ---
            addBoldText(doc, "2. List stakeholder groups identified as key for your entity and the frequency of engagement with each stakeholder group.");

            XWPFTable tableStake = doc.createTable();
            tableStake.setWidth("100%");

            XWPFTableRow hRowStake = tableStake.getRow(0);
            ensureCells(hRowStake, 5);
            styleCell(hRowStake.getCell(0), "Stakeholder Group", true);
            styleCell(hRowStake.getCell(1), "Whether identified as Vulnerable & Marginalized Group (Yes/No)", true);
            styleCell(hRowStake.getCell(2), "Channels of communication\n(Email, SMS, Newspaper, Pamphlets, Advertisement, Community Meetings, Notice Board, Website, Other)", true);
            styleCell(hRowStake.getCell(3), "Frequency of engagement\n(Annually/ Half yearly/ Quarterly / others – please specify)", true);
            styleCell(hRowStake.getCell(4), "Purpose and scope of engagement including key topics and concerns raised during such engagement", true);

            List<BrsrReportRequest.StakeholderEngagement> engagements = data.getStakeholderEngagements();
            if (engagements != null && !engagements.isEmpty()) {
                for (BrsrReportRequest.StakeholderEngagement se : engagements) {
                    XWPFTableRow row = tableStake.createRow();
                    ensureCells(row, 5);

                    styleCell(row.getCell(0), checkNull(se.getGroupName()), false);
                    styleCell(row.getCell(1), checkNull(se.getIsVulnerable()), false);
                    fillCellWithNewlines(row.getCell(2), checkNull(se.getChannels()));
                    fillCellWithNewlines(row.getCell(3), checkNull(se.getFrequency()));
                    fillCellWithNewlines(row.getCell(4), checkNull(se.getPurpose()));
                }
            } else {
                addDynamicRow(tableStake, new String[]{"-", "-", "-", "-", "-"});
            }

            if (data.getStakeholderNote() != null && !data.getStakeholderNote().trim().isEmpty()) {
                addNote(doc, data.getStakeholderNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP INDICATORS HEADER ---
            XWPFParagraph pLeadHeade = doc.createParagraph();
            pLeadHead.setAlignment(ParagraphAlignment.LEFT); // Left-aligned to match the image
            XWPFRun rLeadHeade = pLeadHeade.createRun();
            rLeadHeade.setText("Leadership Indicators");
            rLeadHeade.setBold(true);
            rLeadHeade.setFontSize(12);
            rLeadHeade.setColor("548235"); // The specific green color from the screenshot

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 1: CONSULTATION PROCESS ---
            addBoldText(doc, "1. Provide the processes for consultation between stakeholders and the Board on economic, environmental, and social topics or if consultation is delegated, how is feedback from such consultations provided to the Board.");

            XWPFParagraph pConsult = doc.createParagraph();
            XWPFRun rConsult = pConsult.createRun();
            setTextWithBreaks(rConsult, checkNull(data.getConsultationProcessDetails()));
            pConsult.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 2: STAKEHOLDER CONSULTATION USAGE ---
            addBoldText(doc, "2. Whether stakeholder consultation is used to support the identification and management of environmental, and social topics (Yes / No). If so, provide details of instances as to how the inputs received from stakeholders on these topics were incorporated into policies and activities of the entity.");

            XWPFParagraph pConsult2 = doc.createParagraph();
            XWPFRun rConsult2 = pConsult2.createRun();

            String status2 = checkNull(data.getStakeholderConsultationUsed());
            String details2 = checkNull(data.getStakeholderConsultationDetails());

            // Format: "Yes \n\n [Details]" for cleaner readability in Word
            String combinedText2 = status2;
            if (!details2.equals("-") && !details2.trim().isEmpty()) {
                combinedText2 += "\n\n" + details2;
            }

            setTextWithBreaks(rConsult2, combinedText2);
            pConsult2.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 3: VULNERABLE GROUPS ---
            addBoldText(doc, "3. Provide details of instances of engagement with, and actions taken to, address the concerns of vulnerable/ marginalized stakeholder groups.");

            // 1. Intro Paragraph
            if (data.getVulnerableGroupIntro() != null && !data.getVulnerableGroupIntro().trim().isEmpty()) {
                XWPFParagraph pIntro = doc.createParagraph();
                setTextWithBreaks(pIntro.createRun(), checkNull(data.getVulnerableGroupIntro()));
                pIntro.setSpacingAfter(100);
            }

            // 2. Numbered List of Actions (Fixed variable name check)
            List<String> actionsP4 = data.getVulnerableGroupActions();
            if (actionsP4 != null && !actionsP4.isEmpty()) {
                int count = 1;
                for (String action : actionsP4) {
                    XWPFParagraph pAction = doc.createParagraph();
                    pAction.setIndentationLeft(720);   // Indent 0.5 inch
                    pAction.setIndentationHanging(360); // Hanging indent for number

                    XWPFRun rAction = pAction.createRun();
                    rAction.setText(count++ + ".  " + action);
                }
            }

            // 3. Concluding Paragraph
            if (data.getVulnerableGroupConclusion() != null && !data.getVulnerableGroupConclusion().trim().isEmpty()) {
                XWPFParagraph pConcl = doc.createParagraph();
                pConcl.setSpacingBefore(100);
                setTextWithBreaks(pConcl.createRun(), checkNull(data.getVulnerableGroupConclusion()));
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION C: PRINCIPLE 5 =================
            XWPFParagraph pP5 = doc.createParagraph();
            pP5.setSpacingBefore(400);
            XWPFRun rP5 = pP5.createRun();
            rP5.setText("PRINCIPLE 5: Businesses should respect and promote human rights");
            rP5.setBold(true);
            rP5.setFontSize(12);
            rP5.setColor("548235"); // The specific green color from the screenshot

            // Essential Indicators Title (matching the clean, left-aligned style)
            XWPFParagraph p5Ess = doc.createParagraph();
            p5Ess.setSpacingBefore(100);
            XWPFRun rP5Ess = p5Ess.createRun();
            rP5Ess.setText("Essential Indicators");
            rP5Ess.setBold(true);
            rP5Ess.setFontSize(11);

            doc.createParagraph().createRun().addBreak();

            addBoldText(doc, "1. Employees and workers who have been provided training on human rights issues and policy(ies) of the entity, in the following format:");

            XWPFTable tableP5 = doc.createTable();
            tableP5.setWidth("100%");

            // --- ROW 0: FY HEADERS ---
            XWPFTableRow hRowP5_1 = tableP5.getRow(0);
            ensureCells(hRowP5_1, 7);

            styleCell(hRowP5_1.getCell(0), "Category", true);

            // Re-use FY variables
            String fyCurP5 = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "Current";
            String fyPrevP5 = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "Previous";

            styleCell(hRowP5_1.getCell(1), "FY " + fyCurP5 + "\nCurrent Financial Year", true);
            mergeCellsHorizontal(hRowP5_1, 1, 3);

            styleCell(hRowP5_1.getCell(4), "FY " + fyPrevP5 + "\nPrevious Financial Year", true);
            mergeCellsHorizontal(hRowP5_1, 4, 6);

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRowP5_2 = tableP5.createRow();
            ensureCells(hRowP5_2, 7);
            styleCell(hRowP5_2.getCell(0), "", true);

            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hRowP5_2.getCell(base), i == 0 ? "Total (A)" : "Total (C)", true);
                styleCell(hRowP5_2.getCell(base+1), "No. of employees / workers covered (" + (i == 0 ? "B" : "D") + ")", true);
                styleCell(hRowP5_2.getCell(base+2), i == 0 ? "% (B / A)" : "% (D / C)", true);
            }

            // --- EMPLOYEES ---
            addSectionTitleRow(tableP5, "Employees", 7);

            addDynamicRow(tableP5, new String[]{ "Permanent",
                    checkNull(data.getHrEmpPermTotalCurr()), checkNull(data.getHrEmpPermCovCurr()), checkNull(data.getHrEmpPermPercCurr()),
                    checkNull(data.getHrEmpPermTotalPrev()), checkNull(data.getHrEmpPermCovPrev()), checkNull(data.getHrEmpPermPercPrev())
            });
            addDynamicRow(tableP5, new String[]{ "Other than permanent",
                    checkNull(data.getHrEmpTempTotalCurr()), checkNull(data.getHrEmpTempCovCurr()), checkNull(data.getHrEmpTempPercCurr()),
                    checkNull(data.getHrEmpTempTotalPrev()), checkNull(data.getHrEmpTempCovPrev()), checkNull(data.getHrEmpTempPercPrev())
            });
            // Total Row
            XWPFTableRow rTotalEmp = tableP5.createRow();
            ensureCells(rTotalEmp, 7);
            styleCell(rTotalEmp.getCell(0), "Total Employees", true);
            styleCell(rTotalEmp.getCell(1), checkNull(data.getHrEmpGrandTotalCurr()), true);
            styleCell(rTotalEmp.getCell(2), checkNull(data.getHrEmpGrandCovCurr()), true);
            styleCell(rTotalEmp.getCell(3), checkNull(data.getHrEmpGrandPercCurr()), true);
            styleCell(rTotalEmp.getCell(4), checkNull(data.getHrEmpGrandTotalPrev()), true);
            styleCell(rTotalEmp.getCell(5), checkNull(data.getHrEmpGrandCovPrev()), true);
            styleCell(rTotalEmp.getCell(6), checkNull(data.getHrEmpGrandPercPrev()), true);

            // --- WORKERS ---
            addSectionTitleRow(tableP5, "Workers", 7);

            addDynamicRow(tableP5, new String[]{ "Permanent",
                    checkNull(data.getHrWorkPermTotalCurr()), checkNull(data.getHrWorkPermCovCurr()), checkNull(data.getHrWorkPermPercCurr()),
                    checkNull(data.getHrWorkPermTotalPrev()), checkNull(data.getHrWorkPermCovPrev()), checkNull(data.getHrWorkPermPercPrev())
            });
            addDynamicRow(tableP5, new String[]{ "Other than permanent",
                    checkNull(data.getHrWorkTempTotalCurr()), checkNull(data.getHrWorkTempCovCurr()), checkNull(data.getHrWorkTempPercCurr()),
                    checkNull(data.getHrWorkTempTotalPrev()), checkNull(data.getHrWorkTempCovPrev()), checkNull(data.getHrWorkTempPercPrev())
            });
            // Total Row
            XWPFTableRow rTotalWork = tableP5.createRow();
            ensureCells(rTotalWork, 7);
            styleCell(rTotalWork.getCell(0), "Total Workers", true);
            styleCell(rTotalWork.getCell(1), checkNull(data.getHrWorkGrandTotalCurr()), true);
            styleCell(rTotalWork.getCell(2), checkNull(data.getHrWorkGrandCovCurr()), true);
            styleCell(rTotalWork.getCell(3), checkNull(data.getHrWorkGrandPercCurr()), true);
            styleCell(rTotalWork.getCell(4), checkNull(data.getHrWorkGrandTotalPrev()), true);
            styleCell(rTotalWork.getCell(5), checkNull(data.getHrWorkGrandCovPrev()), true);
            styleCell(rTotalWork.getCell(6), checkNull(data.getHrWorkGrandPercPrev()), true);

            if (data.getHrTrainingNote() != null && !data.getHrTrainingNote().trim().isEmpty()) {
                addNote(doc, data.getHrTrainingNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- 2. MINIMUM WAGES ---
            addBoldText(doc, "2. Details of minimum wages paid to employees and workers, in the following format:");

            XWPFTable tableMw = doc.createTable();
            tableMw.setWidth("100%");

            // --- ROW 0: FY HEADERS ---
            XWPFTableRow hRowMw1 = tableMw.getRow(0);
            ensureCells(hRowMw1, 11);
            styleCell(hRowMw1.getCell(0), "Category", true);
            styleCell(hRowMw1.getCell(1), "FY " + fyCurP5 + "\nCurrent Financial Year", true);
            mergeCellsHorizontal(hRowMw1, 1, 5);
            styleCell(hRowMw1.getCell(6), "FY " + fyPrevP5 + "\nPrevious Financial Year", true);
            mergeCellsHorizontal(hRowMw1, 6, 10);

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRowMw2 = tableMw.createRow();
            ensureCells(hRowMw2, 11);
            styleCell(hRowMw2.getCell(0), "", true);

            for(int i=0; i<2; i++) {
                int base = 1 + (i*5);
                styleCell(hRowMw2.getCell(base), i == 0 ? "Total (A)" : "Total (D)", true);
                styleCell(hRowMw2.getCell(base+1), "Equal to Minimum Wage", true);
                mergeCellsHorizontal(hRowMw2, base+1, base+2);
                styleCell(hRowMw2.getCell(base+3), "More than Minimum Wage", true);
                mergeCellsHorizontal(hRowMw2, base+3, base+4);
            }

            // --- ROW 2: SUB HEADERS ---
            XWPFTableRow hRowMw3 = tableMw.createRow();
            ensureCells(hRowMw3, 11);
            styleCell(hRowMw3.getCell(0), "", true);

            for(int i=0; i<2; i++) {
                int base = 1 + (i*5);
                styleCell(hRowMw3.getCell(base), "", true);
                styleCell(hRowMw3.getCell(base+1), i == 0 ? "No. (B)" : "No. (E)", true);
                styleCell(hRowMw3.getCell(base+2), i == 0 ? "% (B / A)" : "% (E / D)", true);
                styleCell(hRowMw3.getCell(base+3), i == 0 ? "No. (C)" : "No. (F)", true);
                styleCell(hRowMw3.getCell(base+4), i == 0 ? "% (C / A)" : "% (F / D)", true);
            }

            // --- DATA ROWS ---
            // EMPLOYEES
            addSectionTitleRow(tableMw, "Employees", 11);
            addBoldRow(tableMw, "Permanent", 11);
            addDynamicRow(tableMw, new String[]{ "Male", checkNull(data.getMwEmpPermMaleTotalCurr()), checkNull(data.getMwEmpPermMaleEqNoCurr()), checkNull(data.getMwEmpPermMaleEqPercCurr()), checkNull(data.getMwEmpPermMaleMoreNoCurr()), checkNull(data.getMwEmpPermMaleMorePercCurr()), checkNull(data.getMwEmpPermMaleTotalPrev()), checkNull(data.getMwEmpPermMaleEqNoPrev()), checkNull(data.getMwEmpPermMaleEqPercPrev()), checkNull(data.getMwEmpPermMaleMoreNoPrev()), checkNull(data.getMwEmpPermMaleMorePercPrev()) });
            addDynamicRow(tableMw, new String[]{ "Female", checkNull(data.getMwEmpPermFemTotalCurr()), checkNull(data.getMwEmpPermFemEqNoCurr()), checkNull(data.getMwEmpPermFemEqPercCurr()), checkNull(data.getMwEmpPermFemMoreNoCurr()), checkNull(data.getMwEmpPermFemMorePercCurr()), checkNull(data.getMwEmpPermFemTotalPrev()), checkNull(data.getMwEmpPermFemEqNoPrev()), checkNull(data.getMwEmpPermFemEqPercPrev()), checkNull(data.getMwEmpPermFemMoreNoPrev()), checkNull(data.getMwEmpPermFemMorePercPrev()) });

            addBoldRow(tableMw, "Other than Permanent", 11);
            addDynamicRow(tableMw, new String[]{ "Male", checkNull(data.getMwEmpTempMaleTotalCurr()), checkNull(data.getMwEmpTempMaleEqNoCurr()), checkNull(data.getMwEmpTempMaleEqPercCurr()), checkNull(data.getMwEmpTempMaleMoreNoCurr()), checkNull(data.getMwEmpTempMaleMorePercCurr()), checkNull(data.getMwEmpTempMaleTotalPrev()), checkNull(data.getMwEmpTempMaleEqNoPrev()), checkNull(data.getMwEmpTempMaleEqPercPrev()), checkNull(data.getMwEmpTempMaleMoreNoPrev()), checkNull(data.getMwEmpTempMaleMorePercPrev()) });
            addDynamicRow(tableMw, new String[]{ "Female", checkNull(data.getMwEmpTempFemTotalCurr()), checkNull(data.getMwEmpTempFemEqNoCurr()), checkNull(data.getMwEmpTempFemEqPercCurr()), checkNull(data.getMwEmpTempFemMoreNoCurr()), checkNull(data.getMwEmpTempFemMorePercCurr()), checkNull(data.getMwEmpTempFemTotalPrev()), checkNull(data.getMwEmpTempFemEqNoPrev()), checkNull(data.getMwEmpTempFemEqPercPrev()), checkNull(data.getMwEmpTempFemMoreNoPrev()), checkNull(data.getMwEmpTempFemMorePercPrev()) });

            // WORKERS
            addSectionTitleRow(tableMw, "Workers", 11);
            addBoldRow(tableMw, "Permanent", 11);
            addDynamicRow(tableMw, new String[]{ "Male", checkNull(data.getMwWorkPermMaleTotalCurr()), checkNull(data.getMwWorkPermMaleEqNoCurr()), checkNull(data.getMwWorkPermMaleEqPercCurr()), checkNull(data.getMwWorkPermMaleMoreNoCurr()), checkNull(data.getMwWorkPermMaleMorePercCurr()), checkNull(data.getMwWorkPermMaleTotalPrev()), checkNull(data.getMwWorkPermMaleEqNoPrev()), checkNull(data.getMwWorkPermMaleEqPercPrev()), checkNull(data.getMwWorkPermMaleMoreNoPrev()), checkNull(data.getMwWorkPermMaleMorePercPrev()) });
            addDynamicRow(tableMw, new String[]{ "Female", checkNull(data.getMwWorkPermFemTotalCurr()), checkNull(data.getMwWorkPermFemEqNoCurr()), checkNull(data.getMwWorkPermFemEqPercCurr()), checkNull(data.getMwWorkPermFemMoreNoCurr()), checkNull(data.getMwWorkPermFemMorePercCurr()), checkNull(data.getMwWorkPermFemTotalPrev()), checkNull(data.getMwWorkPermFemEqNoPrev()), checkNull(data.getMwWorkPermFemEqPercPrev()), checkNull(data.getMwWorkPermFemMoreNoPrev()), checkNull(data.getMwWorkPermFemMorePercPrev()) });

            addBoldRow(tableMw, "Other than Permanent", 11);
            addDynamicRow(tableMw, new String[]{ "Male", checkNull(data.getMwWorkTempMaleTotalCurr()), checkNull(data.getMwWorkTempMaleEqNoCurr()), checkNull(data.getMwWorkTempMaleEqPercCurr()), checkNull(data.getMwWorkTempMaleMoreNoCurr()), checkNull(data.getMwWorkTempMaleMorePercCurr()), checkNull(data.getMwWorkTempMaleTotalPrev()), checkNull(data.getMwWorkTempMaleEqNoPrev()), checkNull(data.getMwWorkTempMaleEqPercPrev()), checkNull(data.getMwWorkTempMaleMoreNoPrev()), checkNull(data.getMwWorkTempMaleMorePercPrev()) });
            addDynamicRow(tableMw, new String[]{ "Female", checkNull(data.getMwWorkTempFemTotalCurr()), checkNull(data.getMwWorkTempFemEqNoCurr()), checkNull(data.getMwWorkTempFemEqPercCurr()), checkNull(data.getMwWorkTempFemMoreNoCurr()), checkNull(data.getMwWorkTempFemMorePercCurr()), checkNull(data.getMwWorkTempFemTotalPrev()), checkNull(data.getMwWorkTempFemEqNoPrev()), checkNull(data.getMwWorkTempFemEqPercPrev()), checkNull(data.getMwWorkTempFemMoreNoPrev()), checkNull(data.getMwWorkTempFemMorePercPrev()) });

            if (data.getMinWageNote() != null && !data.getMinWageNote().trim().isEmpty()) {
                addNote(doc, data.getMinWageNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- 3. REMUNERATION ---
            addBoldText(doc, "3. Details of remuneration/salary/wages");
            doc.createParagraph().createRun().setText("a. Median remuneration / wages:");

            XWPFTable tableRem = doc.createTable();
            tableRem.setWidth("100%");

            // --- ROW 0: GENDER HEADERS (Merged) ---
            XWPFTableRow hRowRem1 = tableRem.getRow(0);
            ensureCells(hRowRem1, 5);

            styleCell(hRowRem1.getCell(0), "", true); // Empty top-left

            styleCell(hRowRem1.getCell(1), "Male", true);
            mergeCellsHorizontal(hRowRem1, 1, 2);

            styleCell(hRowRem1.getCell(3), "Female", true);
            mergeCellsHorizontal(hRowRem1, 3, 4);

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRowRem2 = tableRem.createRow();
            ensureCells(hRowRem2, 5);

            styleCell(hRowRem2.getCell(0), "", true);

            styleCell(hRowRem2.getCell(1), "Number", true);
            styleCell(hRowRem2.getCell(2), "Median remuneration/ salary/ wages of respective category", true);

            styleCell(hRowRem2.getCell(3), "Number", true);
            styleCell(hRowRem2.getCell(4), "Median remuneration/ salary/ wages of respective category", true);

            // --- DATA ROWS ---
            addDynamicRow(tableRem, new String[]{ "Board of Directors (BoD)", checkNull(data.getRemBodMaleNum()), checkNull(data.getRemBodMaleMedian()), checkNull(data.getRemBodFemNum()), checkNull(data.getRemBodFemMedian()) });
            addDynamicRow(tableRem, new String[]{ "Key Managerial Personnel", checkNull(data.getRemKmpMaleNum()), checkNull(data.getRemKmpMaleMedian()), checkNull(data.getRemKmpFemNum()), checkNull(data.getRemKmpFemMedian()) });
            addDynamicRow(tableRem, new String[]{ "Employees other than BoD and KMP", checkNull(data.getRemEmpMaleNum()), checkNull(data.getRemEmpMaleMedian()), checkNull(data.getRemEmpFemNum()), checkNull(data.getRemEmpFemMedian()) });
            addDynamicRow(tableRem, new String[]{ "Workers", checkNull(data.getRemWorkMaleNum()), checkNull(data.getRemWorkMaleMedian()), checkNull(data.getRemWorkFemNum()), checkNull(data.getRemWorkFemMedian()) });

            if (data.getRemunerationNote() != null && !data.getRemunerationNote().trim().isEmpty()) {
                addNote(doc, data.getRemunerationNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- 3.b GROSS WAGES ---
            doc.createParagraph().createRun().setText("b. Gross wages paid to females as % of total wages paid by the entity, in the following format:");

            XWPFTable tableGw = doc.createTable();
            tableGw.setWidth("100%");
            setColumnWidths(tableGw, 5000, 2500);

            // Header Row
            XWPFTableRow hRowGw = tableGw.getRow(0);
            ensureCells(hRowGw, 3);
            styleCell(hRowGw.getCell(0), "", true);

            String fyCurGw = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "Current FY";
            String fyPrevGw = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowGw.getCell(1), "FY " + fyCurGw + "\nCurrent Financial Year", true);
            styleCell(hRowGw.getCell(2), "FY " + fyPrevGw + "\nPrevious Financial Year", true);

            // Data Row
            // Removing the hardcoded % sign from Java so the user can control input
            addDynamicRow(tableGw, new String[]{
                    "Gross wages paid to females as % of total wages",
                    checkNull(data.getGrossWagesFemalePercCurr()),
                    checkNull(data.getGrossWagesFemalePercPrev())
            });

            if (data.getGrossWagesNote() != null && !data.getGrossWagesNote().trim().isEmpty()) {
                addNote(doc, data.getGrossWagesNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- 4. HUMAN RIGHTS FOCAL POINT ---
            addBoldText(doc, "4. Do you have a focal point (Individual/ Committee) responsible for addressing human rights impacts or issues caused or contributed to by the business? (Yes/No)");

            XWPFParagraph pHrFocal = doc.createParagraph();
            XWPFRun rHrFocal = pHrFocal.createRun();

            String hrStatus = checkNull(data.getHumanRightsFocalPoint());
            String hrDetails = checkNull(data.getHumanRightsFocalDetails());

            // Format: "Yes \n\n [Details]"
            String combinedHrText = hrStatus;
            if (!hrDetails.equals("-") && !hrDetails.trim().isEmpty()) {
                combinedHrText += "\n\n" + hrDetails;
            }

            setTextWithBreaks(rHrFocal, combinedHrText);
            pHrFocal.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 5. GRIEVANCE MECHANISMS ---
            addBoldText(doc, "5. Describe the internal mechanisms in place to redress grievances related to human rights issues.");

            XWPFParagraph pGriev = doc.createParagraph();
            XWPFRun rGriev = pGriev.createRun();
            setTextWithBreaks(rGriev, checkNull(data.getHumanRightsGrievanceMechanism()));
            pGriev.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 6. COMPLAINTS (EMPLOYEES/WORKERS) ---
            addBoldText(doc, "6. Number of Complaints on the following made by employees and workers:");

            XWPFTable tableComp = doc.createTable();
            tableComp.setWidth("100%");

            // --- ROW 0: FY HEADERS ---
            XWPFTableRow hRowComp1 = tableComp.getRow(0);
            ensureCells(hRowComp1, 7);

            styleCell(hRowComp1.getCell(0), "", true);

            String fyCurComp = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "Current FY";
            String fyPrevComp = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowComp1.getCell(1), "FY " + fyCurComp + "\nCurrent Financial Year", true);
            mergeCellsHorizontal(hRowComp1, 1, 3);

            styleCell(hRowComp1.getCell(4), "FY " + fyPrevComp + "\nPrevious Financial Year", true);
            mergeCellsHorizontal(hRowComp1, 4, 6);

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRowComp2 = tableComp.createRow();
            ensureCells(hRowComp2, 7);
            styleCell(hRowComp2.getCell(0), "", true);

            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hRowComp2.getCell(base), "Filed during the year", true);
                styleCell(hRowComp2.getCell(base+1), "Pending resolution at the end of year", true);
                styleCell(hRowComp2.getCell(base+2), "Remarks", true);
            }

            // --- DATA ROWS ---
            addDynamicRow(tableComp, new String[]{ "Sexual Harassment", checkNull(data.getCompShFiledCurr()), checkNull(data.getCompShPendingCurr()), checkNull(data.getCompShRemarksCurr()), checkNull(data.getCompShFiledPrev()), checkNull(data.getCompShPendingPrev()), checkNull(data.getCompShRemarksPrev()) });
            addDynamicRow(tableComp, new String[]{ "Discrimination at workplace", checkNull(data.getCompDiscrimFiledCurr()), checkNull(data.getCompDiscrimPendingCurr()), checkNull(data.getCompDiscrimRemarksCurr()), checkNull(data.getCompDiscrimFiledPrev()), checkNull(data.getCompDiscrimPendingPrev()), checkNull(data.getCompDiscrimRemarksPrev()) });
            addDynamicRow(tableComp, new String[]{ "Child Labour", checkNull(data.getCompChildFiledCurr()), checkNull(data.getCompChildPendingCurr()), checkNull(data.getCompChildRemarksCurr()), checkNull(data.getCompChildFiledPrev()), checkNull(data.getCompChildPendingPrev()), checkNull(data.getCompChildRemarksPrev()) });
            addDynamicRow(tableComp, new String[]{ "Forced Labour/Involuntary Labour", checkNull(data.getCompForcedFiledCurr()), checkNull(data.getCompForcedPendingCurr()), checkNull(data.getCompForcedRemarksCurr()), checkNull(data.getCompForcedFiledPrev()), checkNull(data.getCompForcedPendingPrev()), checkNull(data.getCompForcedRemarksPrev()) });
            addDynamicRow(tableComp, new String[]{ "Wages", checkNull(data.getCompWagesFiledCurr()), checkNull(data.getCompWagesPendingCurr()), checkNull(data.getCompWagesRemarksCurr()), checkNull(data.getCompWagesFiledPrev()), checkNull(data.getCompWagesPendingPrev()), checkNull(data.getCompWagesRemarksPrev()) });
            addDynamicRow(tableComp, new String[]{ "Other human rights related issues", checkNull(data.getCompOtherHrFiledCurr()), checkNull(data.getCompOtherHrPendingCurr()), checkNull(data.getCompOtherHrRemarksCurr()), checkNull(data.getCompOtherHrFiledPrev()), checkNull(data.getCompOtherHrPendingPrev()), checkNull(data.getCompOtherHrRemarksPrev()) });

            if (data.getHrComplaintsNote() != null && !data.getHrComplaintsNote().trim().isEmpty()) {
                addNote(doc, data.getHrComplaintsNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- 7. POSH COMPLAINTS ---
            addBoldText(doc, "7. Complaints filed under the Sexual Harassment of Women at Workplace (Prevention, Prohibition and Redressal) Act, 2013, in the following format:");

            XWPFTable tablePosh = doc.createTable();
            tablePosh.setWidth("100%");
            setColumnWidths(tablePosh, 5000, 2500);

            // Header
            XWPFTableRow hRowPosh = tablePosh.getRow(0);
            ensureCells(hRowPosh, 3);
            styleCell(hRowPosh.getCell(0), "", true);

            String fyCurPosh = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "Current FY";
            String fyPrevPosh = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowPosh.getCell(1), "FY " + fyCurPosh + "\nCurrent Financial Year", true);
            styleCell(hRowPosh.getCell(2), "FY " + fyPrevPosh + "\nPrevious Financial Year", true);

            // Data Rows (Removing hardcoded % so user input handles it)
            addDynamicRow(tablePosh, new String[]{
                    "Total Complaints reported under Sexual Harassment on of Women at Workplace (Prevention, Prohibition and Redressal) Act, 2013 (POSH)",
                    checkNull(data.getPoshTotalCurr()),
                    checkNull(data.getPoshTotalPrev())
            });

            addDynamicRow(tablePosh, new String[]{
                    "Complaints on POSH as a % of female employees / workers",
                    checkNull(data.getPoshPercCurr()),
                    checkNull(data.getPoshPercPrev())
            });

            addDynamicRow(tablePosh, new String[]{
                    "Complaints on POSH upheld",
                    checkNull(data.getPoshUpheldCurr()),
                    checkNull(data.getPoshUpheldPrev())
            });

            if (data.getPoshNote() != null && !data.getPoshNote().trim().isEmpty()) {
                addNote(doc, data.getPoshNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- 8. PROTECTION MECHANISMS ---
            addBoldText(doc, "8. Mechanisms to prevent adverse consequences to the complainant in discrimination and harassment cases.");

            if (data.getProtectionMechanismsIntro() != null && !data.getProtectionMechanismsIntro().trim().isEmpty()) {
                XWPFParagraph pProt = doc.createParagraph();
                setTextWithBreaks(pProt.createRun(), checkNull(data.getProtectionMechanismsIntro()));
                pProt.setSpacingAfter(100);
            }

            // Numbered List (using lower roman numerals)
            List<String> mechs = data.getProtectionMechanismsList();
            if (mechs != null && !mechs.isEmpty()) {
                int count = 1;
                for (String mech : mechs) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);   // Indent 0.5 inch
                    pItem.setIndentationHanging(360);

                    XWPFRun rItem = pItem.createRun();
                    // Assumes you have a helper like intToRomanLower(), otherwise just use standard numbers
                    String label = intToRomanLower(count++) + ".  ";
                    rItem.setText(label + mech);
                }
            }
            doc.createParagraph().createRun().addBreak();

            // --- 9. CONTRACT REQUIREMENTS ---
            addBoldText(doc, "9. Do human rights requirements form part of your business agreements and contracts? (Yes/No)");

            XWPFParagraph pCont = doc.createParagraph();
            XWPFRun rCont = pCont.createRun();

            String contStatus = checkNull(data.getHumanRightsContracts());
            String contDetails = checkNull(data.getHumanRightsContractsDetails());

            // Format: "Yes \n\n [Details]"
            String combinedContText = contStatus;
            if (!contDetails.equals("-") && !contDetails.trim().isEmpty()) {
                combinedContText += "\n\n" + contDetails;
            }
            setTextWithBreaks(rCont, combinedContText);
            pCont.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 10. ASSESSMENTS (HUMAN RIGHTS) ---
            addBoldText(doc, "10. Assessments for the year:");

            XWPFTable table10 = doc.createTable();
            table10.setWidth("100%");
            setColumnWidths(table10, 4000, 6000);

            // Header
            XWPFTableRow hRow10 = table10.getRow(0);
            ensureCells(hRow10, 2);
            styleCell(hRow10.getCell(0), "", true);
            styleCell(hRow10.getCell(1), "% of your plants and offices that were assessed\n(by entity or statutory authorities or third parties)", true);

            // Data Rows (Removing hardcoded % so user input handles it)
            addRow(table10, "Child labour", checkNull(data.getAssessChildLabourPerc()));
            addRow(table10, "Forced/involuntary labour", checkNull(data.getAssessForcedLabourPerc()));
            addRow(table10, "Sexual harassment", checkNull(data.getAssessSexualHarassmentPerc()));
            addRow(table10, "Discrimination at workplace", checkNull(data.getAssessDiscriminationPerc()));
            addRow(table10, "Wages", checkNull(data.getAssessWagesPerc()));
            addRow(table10, "Others – please specify", checkNull(data.getAssessOthersPerc()));

            if (data.getAssessmentsP5Note() != null && !data.getAssessmentsP5Note().trim().isEmpty()) {
                addNote(doc, data.getAssessmentsP5Note());
            }
            doc.createParagraph().createRun().addBreak();

            // --- 11. CORRECTIVE ACTIONS (HUMAN RIGHTS) ---
            addBoldText(doc, "11. Provide details of any corrective actions taken or underway to address significant risks / concerns arising from the assessments at Question 10 above.");

            // 1. Intro Paragraph
            if (data.getAssessCorrectiveIntro() != null && !data.getAssessCorrectiveIntro().trim().isEmpty()) {
                XWPFParagraph pCorrIntro = doc.createParagraph();
                setTextWithBreaks(pCorrIntro.createRun(), checkNull(data.getAssessCorrectiveIntro()));
                pCorrIntro.setSpacingAfter(100);
            }

            // 2. Numbered List
            List<String> corrActions = data.getAssessCorrectiveActions();
            if (corrActions != null && !corrActions.isEmpty()) {
                int count = 1;
                for (String action : corrActions) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);   // Indent 0.5 inch
                    pItem.setIndentationHanging(360);

                    XWPFRun rItem = pItem.createRun();
                    String label = count++ + ".  ";
                    rItem.setText(label + action);
                }
            } else {
                XWPFParagraph pNone = doc.createParagraph();
                pNone.createRun().setText("No specific corrective actions listed.");
            }
            doc.createParagraph().createRun().addBreak();

            // --- PRINCIPLE 5 LEADERSHIP INDICATORS ---
            XWPFParagraph p5LeadHead = doc.createParagraph();
            p5LeadHead.setAlignment(ParagraphAlignment.LEFT); // Left-aligned
            XWPFRun rP5LeadHead = p5LeadHead.createRun();
            rP5LeadHead.setText("Leadership Indicators");
            rP5LeadHead.setBold(true);
            rP5LeadHead.setFontSize(12);
            rP5LeadHead.setColor("548235"); // The specific green color

            doc.createParagraph().createRun().addBreak();

            // Q1
            addBoldText(doc, "1. Details of a business process being modified / introduced as a result of addressing human rights grievances/complaints.");

            if (data.getP5LeadProcessModIntro() != null && !data.getP5LeadProcessModIntro().trim().isEmpty()) {
                XWPFParagraph p5L1 = doc.createParagraph();
                setTextWithBreaks(p5L1.createRun(), checkNull(data.getP5LeadProcessModIntro()));
                p5L1.setSpacingAfter(100);
            }

            List<String> modList = data.getP5LeadProcessModList();
            if (modList != null && !modList.isEmpty()) {
                int count = 1;
                for (String mod : modList) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);
                    pItem.setIndentationHanging(360);
                    XWPFRun rItem = pItem.createRun();
                    rItem.setText(count++ + ".  " + mod);
                }
            }
            doc.createParagraph().createRun().addBreak();

            // Q2
            addBoldText(doc, "2. Details of the scope and coverage of any Human rights due-diligence conducted.");

            XWPFParagraph p5L2 = doc.createParagraph();
            setTextWithBreaks(p5L2.createRun(), checkNull(data.getP5LeadDueDiligenceScope()));
            p5L2.setSpacingAfter(200);

            List<String> issuesList = data.getP5LeadDueDiligenceIssues();
            if (issuesList != null && !issuesList.isEmpty()) {
                addBoldText(doc, "Human Rights issues identified:");
                int count = 1;
                for (String issue : issuesList) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);
                    pItem.setIndentationHanging(360);
                    XWPFRun rItem = pItem.createRun();
                    rItem.setText(count++ + ".  " + issue);
                }
                doc.createParagraph().createRun().addBreak();
            }

            List<String> holdersList = data.getP5LeadDueDiligenceHolders();
            if (holdersList != null && !holdersList.isEmpty()) {
                addBoldText(doc, "Rights holders identified:");
                int count = 1;
                for (String holder : holdersList) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);
                    pItem.setIndentationHanging(360);
                    XWPFRun rItem = pItem.createRun();
                    rItem.setText(count++ + ".  " + holder);
                }
            }
            doc.createParagraph().createRun().addBreak();

            // Q3
            addBoldText(doc, "3. Is the premise/office of the entity accessible to differently abled visitors, as per the requirements of the Rights of Persons with Disabilities Act, 2016?");

            XWPFParagraph p5L3 = doc.createParagraph();
            setTextWithBreaks(p5L3.createRun(), checkNull(data.getP5LeadPremisesAccess()));
            p5L3.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // Q4: VALUE CHAIN ASSESSMENT
            addBoldText(doc, "4. Details on assessment of value chain partners:");

            XWPFTable tableP5L4 = doc.createTable();
            tableP5L4.setWidth("100%");
            setColumnWidths(tableP5L4, 4000, 6000);

            XWPFTableRow hRowP5L4 = tableP5L4.getRow(0);
            ensureCells(hRowP5L4, 2);
            styleCell(hRowP5L4.getCell(0), "", true);
            styleCell(hRowP5L4.getCell(1), "% of value chain partners (by value of business done with such partners) that were assessed", true);

            addRow(tableP5L4, "Sexual Harassment", checkNull(data.getP5LeadVcAssessShPerc()));
            addRow(tableP5L4, "Discrimination at workplace", checkNull(data.getP5LeadVcAssessDiscrimPerc()));
            addRow(tableP5L4, "Child Labour", checkNull(data.getP5LeadVcAssessChildPerc()));
            addRow(tableP5L4, "Forced Labour/Involuntary Labour", checkNull(data.getP5LeadVcAssessForcedPerc()));
            addRow(tableP5L4, "Wages", checkNull(data.getP5LeadVcAssessWagesPerc()));
            addRow(tableP5L4, "Others – please specify", checkNull(data.getP5LeadVcAssessOthersPerc()));

            doc.createParagraph().createRun().addBreak();

            // Q5: CORRECTIVE ACTIONS (VALUE CHAIN)
            addBoldText(doc, "5. Provide details of any corrective actions taken or underway to address significant risks / concerns arising from the assessments at Question 4 above.");

            XWPFParagraph p5L5 = doc.createParagraph();
            setTextWithBreaks(p5L5.createRun(), checkNull(data.getP5LeadValueChainCorrectiveActions()));
            p5L5.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // =================================================================
            // SECTION C: PRINCIPLE 6 (ENVIRONMENT)
            // =================================================================
            // ================= SECTION C: PRINCIPLE 6 =================
            XWPFParagraph pP6 = doc.createParagraph();
            pP6.setSpacingBefore(400);
            XWPFRun rP6 = pP6.createRun();
            rP6.setText("PRINCIPLE 6: Businesses should respect and make efforts to protect and restore the environment");
            rP6.setBold(true);
            rP6.setFontSize(12);
            rP6.setColor("548235");

            XWPFParagraph p6Ess = doc.createParagraph();
            p6Ess.setSpacingBefore(100);
            XWPFRun rP6Ess = p6Ess.createRun();
            rP6Ess.setText("Essential Indicators");
            rP6Ess.setBold(true);
            rP6Ess.setFontSize(11);
            doc.createParagraph().createRun().addBreak();

            // --- Q1: ENERGY CONSUMPTION ---
            addBoldText(doc, "1. Details of total energy consumption (in Joules or multiples) and energy intensity, in the following format:");

            XWPFTable tableP6Q1 = doc.createTable();
            tableP6Q1.setWidth("100%");
            setColumnWidths(tableP6Q1, 5000, 2500);

            // Header
            XWPFTableRow hRowP6Q1 = tableP6Q1.getRow(0);
            ensureCells(hRowP6Q1, 3);
            styleCell(hRowP6Q1.getCell(0), "Parameter", true);
            String fyCurP6 = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "Current FY";
            String fyPrevP6 = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "Previous FY";
            styleCell(hRowP6Q1.getCell(1), "FY " + fyCurP6 + "\n(Current Financial Year)", true);
            styleCell(hRowP6Q1.getCell(2), "FY " + fyPrevP6 + "\n(Previous Financial Year)", true);

            // Renewable block
            XWPFTableRow rP6Q1_1 = tableP6Q1.createRow(); ensureCells(rP6Q1_1, 3);
            styleCell(rP6Q1_1.getCell(0), "From renewable sources", true);
            mergeCellsHorizontal(rP6Q1_1, 0, 2);

            addDynamicRow(tableP6Q1, new String[]{"Total electricity consumption (A)", checkNull(data.getP6Q1RenElecCurr()), checkNull(data.getP6Q1RenElecPrev())});
            addDynamicRow(tableP6Q1, new String[]{"Total fuel consumption (B)", checkNull(data.getP6Q1RenFuelCurr()), checkNull(data.getP6Q1RenFuelPrev())});
            addDynamicRow(tableP6Q1, new String[]{"Energy consumption through other sources (C)", checkNull(data.getP6Q1RenOtherCurr()), checkNull(data.getP6Q1RenOtherPrev())});

            XWPFTableRow rowRenTot = tableP6Q1.createRow(); ensureCells(rowRenTot, 3);
            styleCell(rowRenTot.getCell(0), "Total energy consumed from renewable sources (A+B+C)", true);
            rowRenTot.getCell(1).setText(checkNull(data.getP6Q1RenTotalCurr()));
            rowRenTot.getCell(2).setText(checkNull(data.getP6Q1RenTotalPrev()));

            // Non-Renewable block
            XWPFTableRow rP6Q1_nr = tableP6Q1.createRow(); ensureCells(rP6Q1_nr, 3);
            styleCell(rP6Q1_nr.getCell(0), "From non-renewable sources", true);
            mergeCellsHorizontal(rP6Q1_nr, 0, 2);

            addDynamicRow(tableP6Q1, new String[]{"Total electricity consumption (D)", checkNull(data.getP6Q1NonRenElecCurr()), checkNull(data.getP6Q1NonRenElecPrev())});
            addDynamicRow(tableP6Q1, new String[]{"Total fuel consumption (E)", checkNull(data.getP6Q1NonRenFuelCurr()), checkNull(data.getP6Q1NonRenFuelPrev())});
            addDynamicRow(tableP6Q1, new String[]{"Energy consumption through other sources (F)", checkNull(data.getP6Q1NonRenOtherCurr()), checkNull(data.getP6Q1NonRenOtherPrev())});

            XWPFTableRow rowNonRenTot = tableP6Q1.createRow(); ensureCells(rowNonRenTot, 3);
            styleCell(rowNonRenTot.getCell(0), "Total energy consumed from non-renewable sources (D+E+F)", true);
            rowNonRenTot.getCell(1).setText(checkNull(data.getP6Q1NonRenTotalCurr()));
            rowNonRenTot.getCell(2).setText(checkNull(data.getP6Q1NonRenTotalPrev()));

            // Grand Totals & Intensity
            XWPFTableRow rowGrandTot = tableP6Q1.createRow(); ensureCells(rowGrandTot, 3);
            styleCell(rowGrandTot.getCell(0), "Total energy consumed (A+B+C+D+E+F)", true);
            rowGrandTot.getCell(1).setText(checkNull(data.getP6Q1GrandTotalCurr()));
            rowGrandTot.getCell(2).setText(checkNull(data.getP6Q1GrandTotalPrev()));

            addDynamicRow(tableP6Q1, new String[]{"Energy intensity per rupee of turnover\n(Total energy consumed / Revenue from operations)", checkNull(data.getP6Q1IntTurnoverCurr()), checkNull(data.getP6Q1IntTurnoverPrev())});
            addDynamicRow(tableP6Q1, new String[]{"Energy intensity per rupee of turnover adjusted for Purchasing Power Parity (PPP)\n(Total energy consumed / Revenue from operations adjusted for PPP)", checkNull(data.getP6Q1IntPppCurr()), checkNull(data.getP6Q1IntPppPrev())});
            addDynamicRow(tableP6Q1, new String[]{"Energy intensity in terms of physical output", checkNull(data.getP6Q1IntPhysicalCurr()), checkNull(data.getP6Q1IntPhysicalPrev())});
            addDynamicRow(tableP6Q1, new String[]{"Energy intensity (optional) – the relevant metric may be selected by the entity", checkNull(data.getP6Q1IntOptCurr()), checkNull(data.getP6Q1IntOptPrev())});

            if (data.getP6Q1AssuranceNote() != null && !data.getP6Q1AssuranceNote().trim().isEmpty()) {
                addNote(doc, "Note: " + data.getP6Q1AssuranceNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- Q2: PAT SCHEME ---
            addBoldText(doc, "2. Does the entity have any sites / facilities identified as designated consumers (DCs) under the Performance, Achieve and Trade (PAT) Scheme of the Government of India? (Y/N) If yes, disclose whether targets set under the PAT scheme have been achieved. In case targets have not been achieved, provide the remedial action taken, if any.");
            XWPFParagraph p6Q2 = doc.createParagraph();
            setTextWithBreaks(p6Q2.createRun(), checkNull(data.getP6Q2PatDetails()));
            doc.createParagraph().createRun().addBreak();

            // --- Q3: WATER DISCLOSURES ---
            addBoldText(doc, "3. Provide details of the following disclosures related to water, in the following format:");

            XWPFTable tableP6Q3 = doc.createTable();
            tableP6Q3.setWidth("100%");
            setColumnWidths(tableP6Q3, 5000, 2500);

            // Header
            XWPFTableRow hRowP6Q3 = tableP6Q3.getRow(0);
            ensureCells(hRowP6Q3, 3);
            styleCell(hRowP6Q3.getCell(0), "Parameter", true);
            styleCell(hRowP6Q3.getCell(1), "FY " + fyCurP6 + "\n(Current Financial Year)", true);
            styleCell(hRowP6Q3.getCell(2), "FY " + fyPrevP6 + "\n(Previous Financial Year)", true);

            XWPFTableRow rP6Q3_cat = tableP6Q3.createRow(); ensureCells(rP6Q3_cat, 3);
            styleCell(rP6Q3_cat.getCell(0), "Water withdrawal by source (in kilolitres)", true);
            mergeCellsHorizontal(rP6Q3_cat, 0, 2);

            addDynamicRow(tableP6Q3, new String[]{"(i) Surface water", checkNull(data.getP6Q3SurfaceCurr()), checkNull(data.getP6Q3SurfacePrev())});
            addDynamicRow(tableP6Q3, new String[]{"(ii) Groundwater", checkNull(data.getP6Q3GroundCurr()), checkNull(data.getP6Q3GroundPrev())});
            addDynamicRow(tableP6Q3, new String[]{"(iii) Third party water", checkNull(data.getP6Q3ThirdPartyCurr()), checkNull(data.getP6Q3ThirdPartyPrev())});
            addDynamicRow(tableP6Q3, new String[]{"(iv) Seawater / desalinated water", checkNull(data.getP6Q3SeaCurr()), checkNull(data.getP6Q3SeaPrev())});
            addDynamicRow(tableP6Q3, new String[]{"(v) Others", checkNull(data.getP6Q3OthersCurr()), checkNull(data.getP6Q3OthersPrev())});

            XWPFTableRow rowWaterWith = tableP6Q3.createRow(); ensureCells(rowWaterWith, 3);
            XWPFRun rWaterWith = rowWaterWith.getCell(0).addParagraph().createRun();
            rWaterWith.setText("Total volume of water withdrawal (in kilolitres) (i + ii + iii + iv + v)");
            rWaterWith.setBold(true); rWaterWith.setItalic(true);
            rowWaterWith.getCell(1).setText(checkNull(data.getP6Q3TotalWithCurr()));
            rowWaterWith.getCell(2).setText(checkNull(data.getP6Q3TotalWithPrev()));

            XWPFTableRow rowWaterCons = tableP6Q3.createRow(); ensureCells(rowWaterCons, 3);
            styleCell(rowWaterCons.getCell(0), "Total volume of water consumption (in kilolitres)", true);
            rowWaterCons.getCell(1).setText(checkNull(data.getP6Q3TotalConsCurr()));
            rowWaterCons.getCell(2).setText(checkNull(data.getP6Q3TotalConsPrev()));

            addDynamicRow(tableP6Q3, new String[]{"Water intensity per rupee of turnover\n(Total water consumption / Revenue from operations)", checkNull(data.getP6Q3IntTurnoverCurr()), checkNull(data.getP6Q3IntTurnoverPrev())});
            addDynamicRow(tableP6Q3, new String[]{"Water intensity per rupee of turnover adjusted for Purchasing Power Parity (PPP)\n(Total water consumption / Revenue from operations adjusted for PPP)", checkNull(data.getP6Q3IntPppCurr()), checkNull(data.getP6Q3IntPppPrev())});
            addDynamicRow(tableP6Q3, new String[]{"Water intensity in terms of physical output", checkNull(data.getP6Q3IntPhysicalCurr()), checkNull(data.getP6Q3IntPhysicalPrev())});
            addDynamicRow(tableP6Q3, new String[]{"Water intensity (optional) – the relevant metric may be selected by the entity", checkNull(data.getP6Q3IntOptCurr()), checkNull(data.getP6Q3IntOptPrev())});

            if (data.getP6Q3AssuranceNote() != null && !data.getP6Q3AssuranceNote().trim().isEmpty()) {
                addNote(doc, "Note: " + data.getP6Q3AssuranceNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- Q4: WATER DISCHARGE ---
            addBoldText(doc, "4. Provide the following details related to water discharged:");

            XWPFTable tableP6Q4 = doc.createTable();
            tableP6Q4.setWidth("100%");
            setColumnWidths(tableP6Q4, 5000, 2500);

            XWPFTableRow hRowP6Q4 = tableP6Q4.getRow(0);
            ensureCells(hRowP6Q4, 3);
            styleCell(hRowP6Q4.getCell(0), "Parameter", true);
            styleCell(hRowP6Q4.getCell(1), "FY " + fyCur + "\n(Current Financial Year)", true);
            styleCell(hRowP6Q4.getCell(2), "FY " + fyPrev + "\n(Previous Financial Year)", true);

            XWPFTableRow rP6Q4_cat = tableP6Q4.createRow(); ensureCells(rP6Q4_cat, 3);
            styleCell(rP6Q4_cat.getCell(0), "Water discharge by destination and level of treatment (in kilolitres)", true);
            mergeCellsHorizontal(rP6Q4_cat, 0, 2);

            // Helper to render treatment rows cleanly
            java.util.function.Consumer<String[]> addDischargeRow = (params) -> {
                XWPFTableRow rHeader = tableP6Q4.createRow(); ensureCells(rHeader, 3);
                styleCell(rHeader.getCell(0), params[0], false);
                mergeCellsHorizontal(rHeader, 0, 2);

                addDynamicRow(tableP6Q4, new String[]{"   - No treatment", checkNull(params[1]), checkNull(params[2])});

                String levelStr = checkNull(params[5]);
                String labelWith = "   - With treatment" + (!levelStr.isEmpty() ? " - " + levelStr : " - please specify level of treatment");
                addDynamicRow(tableP6Q4, new String[]{labelWith, checkNull(params[3]), checkNull(params[4])});
            };

            addDischargeRow.accept(new String[]{"(i) To Surface water", data.getP6Q4SurfNoCurr(), data.getP6Q4SurfNoPrev(), data.getP6Q4SurfWithCurr(), data.getP6Q4SurfWithPrev(), data.getP6Q4SurfLevel()});
            addDischargeRow.accept(new String[]{"(ii) To Groundwater", data.getP6Q4GroundNoCurr(), data.getP6Q4GroundNoPrev(), data.getP6Q4GroundWithCurr(), data.getP6Q4GroundWithPrev(), data.getP6Q4GroundLevel()});
            addDischargeRow.accept(new String[]{"(iii) To Seawater", data.getP6Q4SeaNoCurr(), data.getP6Q4SeaNoPrev(), data.getP6Q4SeaWithCurr(), data.getP6Q4SeaWithPrev(), data.getP6Q4SeaLevel()});
            addDischargeRow.accept(new String[]{"(iv) Sent to third-parties", data.getP6Q4ThirdNoCurr(), data.getP6Q4ThirdNoPrev(), data.getP6Q4ThirdWithCurr(), data.getP6Q4ThirdWithPrev(), data.getP6Q4ThirdLevel()});
            addDischargeRow.accept(new String[]{"(v) Others", data.getP6Q4OtherNoCurr(), data.getP6Q4OtherNoPrev(), data.getP6Q4OtherWithCurr(), data.getP6Q4OtherWithPrev(), data.getP6Q4OtherLevel()});

            XWPFTableRow rowDisTotal = tableP6Q4.createRow(); ensureCells(rowDisTotal, 3);
            styleCell(rowDisTotal.getCell(0), "Total water discharged (in kilolitres)", true);
            rowDisTotal.getCell(1).setText(checkNull(data.getP6Q4TotalCurr()));
            rowDisTotal.getCell(2).setText(checkNull(data.getP6Q4TotalPrev()));

            if (data.getP6Q4AssuranceNote() != null && !data.getP6Q4AssuranceNote().trim().isEmpty()) {
                addNote(doc, "Note: " + data.getP6Q4AssuranceNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- Q5: ZERO LIQUID DISCHARGE ---
            addBoldText(doc, "5. Has the entity implemented a mechanism for Zero Liquid Discharge? If yes, provide details of its coverage and implementation.");
            XWPFParagraph p6Q5 = doc.createParagraph();
            setTextWithBreaks(p6Q5.createRun(), checkNull(data.getP6Q5ZldDetails()));
            doc.createParagraph().createRun().addBreak();

            // --- Q6: AIR EMISSIONS ---
            addBoldText(doc, "6. Please provide details of air emissions (other than GHG emissions) by the entity, in the following format:");

            XWPFTable tableP6Q6 = doc.createTable();
            tableP6Q6.setWidth("100%");
            setColumnWidths(tableP6Q6, 3000, 2000, 2500, 2500); // 4 columns!

            XWPFTableRow hRowP6Q6 = tableP6Q6.getRow(0);
            ensureCells(hRowP6Q6, 4);
            styleCell(hRowP6Q6.getCell(0), "Parameter", true);
            styleCell(hRowP6Q6.getCell(1), "Please specify unit", true);
            styleCell(hRowP6Q6.getCell(2), "FY " + fyCur + "\n(Current Financial Year)", true);
            styleCell(hRowP6Q6.getCell(3), "FY " + fyPrev + "\n(Previous Financial Year)", true);

            // Using helper to handle 4 columns
            java.util.function.Consumer<String[]> addAirRow = (vals) -> {
                XWPFTableRow r = tableP6Q6.createRow(); ensureCells(r, 4);
                r.getCell(0).setText(vals[0]); r.getCell(1).setText(vals[1]); r.getCell(2).setText(vals[2]); r.getCell(3).setText(vals[3]);
            };

            addAirRow.accept(new String[]{"NOx", checkNull(data.getP6Q6NoxUnit()), checkNull(data.getP6Q6NoxCurr()), checkNull(data.getP6Q6NoxPrev())});
            addAirRow.accept(new String[]{"SOx", checkNull(data.getP6Q6SoxUnit()), checkNull(data.getP6Q6SoxCurr()), checkNull(data.getP6Q6SoxPrev())});
            addAirRow.accept(new String[]{"Particulate matter (PM)", checkNull(data.getP6Q6PmUnit()), checkNull(data.getP6Q6PmCurr()), checkNull(data.getP6Q6PmPrev())});
            addAirRow.accept(new String[]{"Persistent organic pollutants (POP)", checkNull(data.getP6Q6PopUnit()), checkNull(data.getP6Q6PopCurr()), checkNull(data.getP6Q6PopPrev())});
            addAirRow.accept(new String[]{"Volatile organic compounds (VOC)", checkNull(data.getP6Q6VocUnit()), checkNull(data.getP6Q6VocCurr()), checkNull(data.getP6Q6VocPrev())});
            addAirRow.accept(new String[]{"Hazardous air pollutants (HAP)", checkNull(data.getP6Q6HapUnit()), checkNull(data.getP6Q6HapCurr()), checkNull(data.getP6Q6HapPrev())});

            String otherLabel = "Others" + (!checkNull(data.getP6Q6OtherName()).isEmpty() ? " - " + data.getP6Q6OtherName() : " – please specify");
            addAirRow.accept(new String[]{otherLabel, checkNull(data.getP6Q6OtherUnit()), checkNull(data.getP6Q6OtherCurr()), checkNull(data.getP6Q6OtherPrev())});

            if (data.getP6Q6AssuranceNote() != null && !data.getP6Q6AssuranceNote().trim().isEmpty()) {
                addNote(doc, "Note: " + data.getP6Q6AssuranceNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- Q7: GHG EMISSIONS ---
            addBoldText(doc, "7. Provide details of greenhouse gas emissions (Scope 1 and Scope 2 emissions) & its intensity, in the following format:");

            XWPFTable tableP6Q7 = doc.createTable();
            tableP6Q7.setWidth("100%");
            setColumnWidths(tableP6Q7, 3000, 2000, 2500, 2500); // Re-using our 4-column helper!

            XWPFTableRow hRowP6Q7 = tableP6Q7.getRow(0);
            ensureCells(hRowP6Q7, 4);
            styleCell(hRowP6Q7.getCell(0), "Parameter", true);
            styleCell(hRowP6Q7.getCell(1), "Unit", true);
            styleCell(hRowP6Q7.getCell(2), "FY " + fyCurP6 + "\n(Current Financial Year)", true);
            styleCell(hRowP6Q7.getCell(3), "FY " + fyPrevP6 + "\n(Previous Financial Year)", true);

            // Using helper to handle 4 columns (like we did in Q6)
            java.util.function.Consumer<String[]> addGhgRow = (vals) -> {
                XWPFTableRow r = tableP6Q7.createRow(); ensureCells(r, 4);
                styleCell(r.getCell(0), vals[0], true); // First column is bolded per screenshot
                r.getCell(1).setText(vals[1]); r.getCell(2).setText(vals[2]); r.getCell(3).setText(vals[3]);
            };

            addGhgRow.accept(new String[]{"Total Scope 1 emissions\n(Break-up of the GHG into CO2, CH4, N2O, HFCs, PFCs, SF6, NF3, if available)", checkNull(data.getP6Q7Scope1Unit()), checkNull(data.getP6Q7Scope1Curr()), checkNull(data.getP6Q7Scope1Prev())});
            addGhgRow.accept(new String[]{"Total Scope 2 emissions\n(Break-up of the GHG into CO2, CH4, N2O, HFCs, PFCs, SF6, NF3, if available)", checkNull(data.getP6Q7Scope2Unit()), checkNull(data.getP6Q7Scope2Curr()), checkNull(data.getP6Q7Scope2Prev())});
            addGhgRow.accept(new String[]{"Total Scope 1 and Scope 2 emission intensity per rupee of turnover\n(Total Scope 1 and Scope 2 GHG emissions / Revenue from operations)", "", checkNull(data.getP6Q7IntTurnoverCurr()), checkNull(data.getP6Q7IntTurnoverPrev())});
            addGhgRow.accept(new String[]{"Total Scope 1 and Scope 2 emission intensity per rupee of turnover adjusted for Purchasing Power Parity (PPP)\n(Total Scope 1 and Scope 2 GHG emissions / Revenue from operations adjusted for PPP)", "", checkNull(data.getP6Q7IntPppCurr()), checkNull(data.getP6Q7IntPppPrev())});
            addGhgRow.accept(new String[]{"Total Scope 1 and Scope 2 emission intensity in terms of physical output", "", checkNull(data.getP6Q7IntPhysCurr()), checkNull(data.getP6Q7IntPhysPrev())});
            addGhgRow.accept(new String[]{"Total Scope 1 and Scope 2 emission intensity (optional)\n– the relevant metric may be selected by the entity", "", checkNull(data.getP6Q7IntOptCurr()), checkNull(data.getP6Q7IntOptPrev())});

            if (data.getP6Q7AssuranceNote() != null && !data.getP6Q7AssuranceNote().trim().isEmpty()) {
                addNote(doc, "Note: " + data.getP6Q7AssuranceNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- Q8: GHG PROJECTS ---
            addBoldText(doc, "8. Does the entity have any project related to reducing Green House Gas emission? If Yes, then provide details.");
            XWPFParagraph p6Q8 = doc.createParagraph();
            setTextWithBreaks(p6Q8.createRun(), checkNull(data.getP6Q8GhgProjectDetails()));
            doc.createParagraph().createRun().addBreak();

            // --- Q9: WASTE MANAGEMENT ---
            addBoldText(doc, "9. Provide details related to waste management by the entity, in the following format:");

            XWPFTable tableP6Q9 = doc.createTable();
            tableP6Q9.setWidth("100%");
            setColumnWidths(tableP6Q9, 5000, 2500); // Back to the standard 3-column split

            XWPFTableRow hRowP6Q9 = tableP6Q9.getRow(0);
            ensureCells(hRowP6Q9, 3);
            styleCell(hRowP6Q9.getCell(0), "Parameter", true);
            styleCell(hRowP6Q9.getCell(1), "FY " + fyCurP6 + "\n(Current Financial Year)", true);
            styleCell(hRowP6Q9.getCell(2), "FY " + fyPrevP6 + "\n(Previous Financial Year)", true);

            // Generated Waste
            XWPFTableRow rP6Q9_gen = tableP6Q9.createRow(); ensureCells(rP6Q9_gen, 3);
            styleCell(rP6Q9_gen.getCell(0), "Total Waste generated (in metric tonnes)", true);
            rP6Q9_gen.getCell(0).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
            mergeCellsHorizontal(rP6Q9_gen, 0, 2);

            addDynamicRow(tableP6Q9, new String[]{"Plastic waste (A)", checkNull(data.getP6Q9GenPlastCurr()), checkNull(data.getP6Q9GenPlastPrev())});
            addDynamicRow(tableP6Q9, new String[]{"E-waste (B)", checkNull(data.getP6Q9GenEwasteCurr()), checkNull(data.getP6Q9GenEwastePrev())});
            addDynamicRow(tableP6Q9, new String[]{"Bio-medical waste (C)", checkNull(data.getP6Q9GenBioCurr()), checkNull(data.getP6Q9GenBioPrev())});
            addDynamicRow(tableP6Q9, new String[]{"Construction and demolition waste (D)", checkNull(data.getP6Q9GenConstCurr()), checkNull(data.getP6Q9GenConstPrev())});
            addDynamicRow(tableP6Q9, new String[]{"Battery waste (E)", checkNull(data.getP6Q9GenBattCurr()), checkNull(data.getP6Q9GenBattPrev())});
            addDynamicRow(tableP6Q9, new String[]{"Radioactive waste (F)", checkNull(data.getP6Q9GenRadioCurr()), checkNull(data.getP6Q9GenRadioPrev())});
            addDynamicRow(tableP6Q9, new String[]{"Other Hazardous waste. Please specify, if any. (G)", checkNull(data.getP6Q9GenHazCurr()), checkNull(data.getP6Q9GenHazPrev())});
            addDynamicRow(tableP6Q9, new String[]{"Other Non-hazardous waste generated (H). Please specify, if any.", checkNull(data.getP6Q9GenNonHazCurr()), checkNull(data.getP6Q9GenNonHazPrev())});

            XWPFTableRow rowGenTot = tableP6Q9.createRow(); ensureCells(rowGenTot, 3);
            XWPFRun rGenTot = rowGenTot.getCell(0).addParagraph().createRun();
            rGenTot.setText("Total (A+B + C + D + E + F + G + H)"); rGenTot.setBold(true); rGenTot.setItalic(true);
            rowGenTot.getCell(1).setText(checkNull(data.getP6Q9GenTotalCurr()));
            rowGenTot.getCell(2).setText(checkNull(data.getP6Q9GenTotalPrev()));

            // Intensities
            addDynamicRow(tableP6Q9, new String[]{"Waste intensity per rupee of turnover", checkNull(data.getP6Q9IntTurnCurr()), checkNull(data.getP6Q9IntTurnPrev())});
            addDynamicRow(tableP6Q9, new String[]{"Waste intensity per rupee of turnover adjusted for PPP", checkNull(data.getP6Q9IntPppCurr()), checkNull(data.getP6Q9IntPppPrev())});
            addDynamicRow(tableP6Q9, new String[]{"Waste intensity in terms of physical output", checkNull(data.getP6Q9IntPhysCurr()), checkNull(data.getP6Q9IntPhysPrev())});
            addDynamicRow(tableP6Q9, new String[]{"Waste intensity (optional)", checkNull(data.getP6Q9IntOptCurr()), checkNull(data.getP6Q9IntOptPrev())});

            // Recovered Waste
            XWPFTableRow rP6Q9_rec = tableP6Q9.createRow(); ensureCells(rP6Q9_rec, 3);
            styleCell(rP6Q9_rec.getCell(0), "For each category of waste generated, total waste recovered through recycling, re-using or other recovery operations (in metric tonnes)", true);
            rP6Q9_rec.getCell(0).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
            mergeCellsHorizontal(rP6Q9_rec, 0, 2);

            XWPFTableRow rP6Q9_recCat = tableP6Q9.createRow(); ensureCells(rP6Q9_recCat, 3);
            styleCell(rP6Q9_recCat.getCell(0), "Category of waste", true);
            mergeCellsHorizontal(rP6Q9_recCat, 0, 2);

            addDynamicRow(tableP6Q9, new String[]{"(i) Recycled", checkNull(data.getP6Q9RecRecyCurr()), checkNull(data.getP6Q9RecRecyPrev())});
            addDynamicRow(tableP6Q9, new String[]{"(ii) Re-used", checkNull(data.getP6Q9RecReuseCurr()), checkNull(data.getP6Q9RecReusePrev())});
            addDynamicRow(tableP6Q9, new String[]{"(iii) Other recovery operations", checkNull(data.getP6Q9RecOthCurr()), checkNull(data.getP6Q9RecOthPrev())});

            XWPFTableRow rowRecTot = tableP6Q9.createRow(); ensureCells(rowRecTot, 3);
            XWPFRun rRecTot = rowRecTot.getCell(0).addParagraph().createRun();
            rRecTot.setText("Total"); rRecTot.setBold(true); rRecTot.setItalic(true);
            rowRecTot.getCell(1).setText(checkNull(data.getP6Q9RecTotalCurr()));
            rowRecTot.getCell(2).setText(checkNull(data.getP6Q9RecTotalPrev()));

            // Disposed Waste
            XWPFTableRow rP6Q9_disp = tableP6Q9.createRow(); ensureCells(rP6Q9_disp, 3);
            styleCell(rP6Q9_disp.getCell(0), "For each category of waste generated, total waste disposed by nature of disposal method (in metric tonnes)", true);
            rP6Q9_disp.getCell(0).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
            mergeCellsHorizontal(rP6Q9_disp, 0, 2);

            XWPFTableRow rP6Q9_dispCat = tableP6Q9.createRow(); ensureCells(rP6Q9_dispCat, 3);
            styleCell(rP6Q9_dispCat.getCell(0), "Category of waste", true);
            mergeCellsHorizontal(rP6Q9_dispCat, 0, 2);

            addDynamicRow(tableP6Q9, new String[]{"(i) Incineration", checkNull(data.getP6Q9DispIncCurr()), checkNull(data.getP6Q9DispIncPrev())});
            addDynamicRow(tableP6Q9, new String[]{"(ii) Landfilling", checkNull(data.getP6Q9DispLandCurr()), checkNull(data.getP6Q9DispLandPrev())});
            addDynamicRow(tableP6Q9, new String[]{"(iii) Other disposal operations", checkNull(data.getP6Q9DispOthCurr()), checkNull(data.getP6Q9DispOthPrev())});

            XWPFTableRow rowDispTot = tableP6Q9.createRow(); ensureCells(rowDispTot, 3);
            XWPFRun rDispTot = rowDispTot.getCell(0).addParagraph().createRun();
            rDispTot.setText("Total"); rDispTot.setBold(true); rDispTot.setItalic(true);
            rowDispTot.getCell(1).setText(checkNull(data.getP6Q9DispTotalCurr()));
            rowDispTot.getCell(2).setText(checkNull(data.getP6Q9DispTotalPrev()));

            if (data.getP6Q9AssuranceNote() != null && !data.getP6Q9AssuranceNote().trim().isEmpty()) {
                addNote(doc, "Note: " + data.getP6Q9AssuranceNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- Q10: WASTE PRACTICES ---
            addBoldText(doc, "10. Briefly describe the waste management practices adopted in your establishments. Describe the strategy adopted by your company to reduce usage of hazardous and toxic chemicals in your products and processes and the practices adopted to manage such wastes.");
            XWPFParagraph p6Q10 = doc.createParagraph();
            setTextWithBreaks(p6Q10.createRun(), checkNull(data.getP6Q10WastePractices()));
            doc.createParagraph().createRun().addBreak();

            // --- Q11: ECOLOGICALLY SENSITIVE AREAS ---
            addBoldText(doc, "11. If the entity has operations/offices in/around ecologically sensitive areas... please specify details in the following format:");
            XWPFTable tableP6Q11 = doc.createTable();
            tableP6Q11.setWidth("100%");

            XWPFTableRow hRowP6Q11 = tableP6Q11.getRow(0);
            ensureCells(hRowP6Q11, 4);
            setColumnWidths(tableP6Q11, 1000, 2500, 2500, 4000); // 4 columns
            styleCell(hRowP6Q11.getCell(0), "S. No.", true);
            styleCell(hRowP6Q11.getCell(1), "Location of operations/offices", true);
            styleCell(hRowP6Q11.getCell(2), "Type of operations", true);
            styleCell(hRowP6Q11.getCell(3), "Whether the conditions of environmental approval / clearance are being complied with? (Y/N) If no, the reasons thereof and corrective action taken, if any.", true);

            if (data.getP6Q11EcoList() != null && !data.getP6Q11EcoList().isEmpty()) {
                for (BrsrReportRequest.P6Q11Eco item : data.getP6Q11EcoList()) {
                    addDynamicRow(tableP6Q11, new String[]{checkNull(item.getSNo()), checkNull(item.getLocation()), checkNull(item.getType()), checkNull(item.getCompliance())});
                }
            } else {
                addDynamicRow(tableP6Q11, new String[]{"-", "-", "-", "-"});
            }
            doc.createParagraph().createRun().addBreak();

            // --- Q12: EIA ---
            addBoldText(doc, "12. Details of environmental impact assessments of projects undertaken by the entity based on applicable laws, in the current financial year:");
            XWPFTable tableP6Q12 = doc.createTable();
            tableP6Q12.setWidth("100%");

            XWPFTableRow hRowP6Q12 = tableP6Q12.getRow(0);
            ensureCells(hRowP6Q12, 6);
            setColumnWidths(tableP6Q12, 2000, 1500, 1500, 1500, 1500, 2000); // 6 columns
            styleCell(hRowP6Q12.getCell(0), "Name and brief details of project", true);
            styleCell(hRowP6Q12.getCell(1), "EIA Notification No.", true);
            styleCell(hRowP6Q12.getCell(2), "Date", true);
            styleCell(hRowP6Q12.getCell(3), "Whether conducted by independent external agency (Yes / No)", true);
            styleCell(hRowP6Q12.getCell(4), "Results communicated in public domain (Yes / No)", true);
            styleCell(hRowP6Q12.getCell(5), "Relevant Web link", true);

            if (data.getP6Q12EiaList() != null && !data.getP6Q12EiaList().isEmpty()) {
                for (BrsrReportRequest.P6Q12Eia item : data.getP6Q12EiaList()) {
                    addDynamicRow(tableP6Q12, new String[]{checkNull(item.getName()), checkNull(item.getNotifNo()), checkNull(item.getDate()), checkNull(item.getIndependent()), checkNull(item.getPublicDomain()), checkNull(item.getLink())});
                }
            } else {
                addDynamicRow(tableP6Q12, new String[]{"-", "-", "-", "-", "-", "-"});
            }
            doc.createParagraph().createRun().addBreak();

            // --- Q13: NON-COMPLIANCE ---
            addBoldText(doc, "13. Is the entity compliant with the applicable environmental law/ regulations/ guidelines in India... (Y/N). If not, provide details of all such non-compliances, in the following format:");
            XWPFTable tableP6Q13 = doc.createTable();
            tableP6Q13.setWidth("100%");

            XWPFTableRow hRowP6Q13 = tableP6Q13.getRow(0);
            ensureCells(hRowP6Q13, 5);
            setColumnWidths(tableP6Q13, 1000, 2500, 2500, 2000, 2000); // 5 columns
            styleCell(hRowP6Q13.getCell(0), "S. No.", true);
            styleCell(hRowP6Q13.getCell(1), "Specify the law / regulation / guidelines which was not complied with", true);
            styleCell(hRowP6Q13.getCell(2), "Provide details of the non-compliance", true);
            styleCell(hRowP6Q13.getCell(3), "Any fines / penalties / action taken by regulatory agencies such as pollution control boards or by courts", true);
            styleCell(hRowP6Q13.getCell(4), "Corrective action taken, if any", true);

            if (data.getP6Q13NonCompList() != null && !data.getP6Q13NonCompList().isEmpty()) {
                for (BrsrReportRequest.P6Q13NonComp item : data.getP6Q13NonCompList()) {
                    addDynamicRow(tableP6Q13, new String[]{checkNull(item.getSNo()), checkNull(item.getLaw()), checkNull(item.getDetails()), checkNull(item.getFines()), checkNull(item.getAction())});
                }
            } else {
                addDynamicRow(tableP6Q13, new String[]{"-", "-", "-", "-", "-"});
            }
            doc.createParagraph().createRun().addBreak();

            // =================================================================
            // SECTION C: PRINCIPLE 6 LEADERSHIP
            // =================================================================

            // ================= P6 LEADERSHIP INDICATORS =================
            XWPFParagraph p6LeadHead = doc.createParagraph();
            p6LeadHead.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun rP6LeadHead = p6LeadHead.createRun();
            rP6LeadHead.setText("Leadership Indicators");
            rP6LeadHead.setBold(true);
            rP6LeadHead.setFontSize(12);
            rP6LeadHead.setColor("548235");
            doc.createParagraph().createRun().addBreak();

            // --- Lead Q1: Water Stress ---
            addBoldText(doc, "1. Water withdrawal, consumption and discharge in areas of water stress (in kilolitres):");
            addBoldText(doc, "For each facility / plant located in areas of water stress, provide the following information:");
            setTextWithBreaks(doc.createParagraph().createRun(), checkNull(data.getP6LeadQ1Facilities()));

            XWPFTable tableLeadQ1 = doc.createTable();
            tableLeadQ1.setWidth("100%");
            setColumnWidths(tableLeadQ1, 5000, 2500);

            XWPFTableRow hRowLeadQ1 = tableLeadQ1.getRow(0); ensureCells(hRowLeadQ1, 3);
            styleCell(hRowLeadQ1.getCell(0), "Parameter", true);
            styleCell(hRowLeadQ1.getCell(1), "FY " + fyCurP6 + "\n(Current Financial Year)", true);
            styleCell(hRowLeadQ1.getCell(2), "FY " + fyPrevP6 + "\n(Previous Financial Year)", true);

            // Withdrawal
            XWPFTableRow rL1W = tableLeadQ1.createRow(); ensureCells(rL1W, 3);
            styleCell(rL1W.getCell(0), "Water withdrawal by source (in kilolitres)", true); mergeCellsHorizontal(rL1W, 0, 2);
            addDynamicRow(tableLeadQ1, new String[]{"(i) Surface water", checkNull(data.getP6LeadQ1WithSurfCurr()), checkNull(data.getP6LeadQ1WithSurfPrev())});
            addDynamicRow(tableLeadQ1, new String[]{"(ii) Groundwater", checkNull(data.getP6LeadQ1WithGroundCurr()), checkNull(data.getP6LeadQ1WithGroundPrev())});
            addDynamicRow(tableLeadQ1, new String[]{"(iii) Third party water", checkNull(data.getP6LeadQ1WithThirdCurr()), checkNull(data.getP6LeadQ1WithThirdPrev())});
            addDynamicRow(tableLeadQ1, new String[]{"(iv) Seawater / desalinated water", checkNull(data.getP6LeadQ1WithSeaCurr()), checkNull(data.getP6LeadQ1WithSeaPrev())});
            addDynamicRow(tableLeadQ1, new String[]{"(v) Others", checkNull(data.getP6LeadQ1WithOtherCurr()), checkNull(data.getP6LeadQ1WithOtherPrev())});

            XWPFTableRow rL1WTot = tableLeadQ1.createRow(); ensureCells(rL1WTot, 3);
            rL1WTot.getCell(0).setText("Total volume of water withdrawal");
            rL1WTot.getCell(1).setText(checkNull(data.getP6LeadQ1WithTotalCurr()));
            rL1WTot.getCell(2).setText(checkNull(data.getP6LeadQ1WithTotalPrev()));

            // Consumption & Intensity
            XWPFTableRow rL1CTot = tableLeadQ1.createRow(); ensureCells(rL1CTot, 3);
            styleCell(rL1CTot.getCell(0), "Total volume of water consumption (in kilolitres)", true);
            rL1CTot.getCell(1).setText(checkNull(data.getP6LeadQ1ConsTotalCurr()));
            rL1CTot.getCell(2).setText(checkNull(data.getP6LeadQ1ConsTotalPrev()));
            addDynamicRow(tableLeadQ1, new String[]{"Water intensity per rupee of turnover", checkNull(data.getP6LeadQ1IntTurnCurr()), checkNull(data.getP6LeadQ1IntTurnPrev())});
            addDynamicRow(tableLeadQ1, new String[]{"Water intensity (optional)", checkNull(data.getP6LeadQ1IntOptCurr()), checkNull(data.getP6LeadQ1IntOptPrev())});

            // Discharge
            XWPFTableRow rL1D = tableLeadQ1.createRow(); ensureCells(rL1D, 3);
            styleCell(rL1D.getCell(0), "Water discharge by destination and level of treatment (in kilolitres)", true); mergeCellsHorizontal(rL1D, 0, 2);

            java.util.function.Consumer<String[]> addLeadDisRow = (params) -> {
                XWPFTableRow rHeader = tableLeadQ1.createRow(); ensureCells(rHeader, 3);
                styleCell(rHeader.getCell(0), params[0], false); mergeCellsHorizontal(rHeader, 0, 2);
                addDynamicRow(tableLeadQ1, new String[]{"   - No treatment", checkNull(params[1]), checkNull(params[2])});
                String levelStr = checkNull(params[5]);
                String labelWith = "   - With treatment" + (!levelStr.isEmpty() ? " - " + levelStr : "");
                addDynamicRow(tableLeadQ1, new String[]{labelWith, checkNull(params[3]), checkNull(params[4])});
            };

            addLeadDisRow.accept(new String[]{"(i) Into Surface water", data.getP6LeadQ1DisSurfNoCurr(), data.getP6LeadQ1DisSurfNoPrev(), data.getP6LeadQ1DisSurfWithCurr(), data.getP6LeadQ1DisSurfWithPrev(), data.getP6LeadQ1DisSurfLevel()});
            addLeadDisRow.accept(new String[]{"(ii) Into Groundwater", data.getP6LeadQ1DisGroundNoCurr(), data.getP6LeadQ1DisGroundNoPrev(), data.getP6LeadQ1DisGroundWithCurr(), data.getP6LeadQ1DisGroundWithPrev(), data.getP6LeadQ1DisGroundLevel()});
            addLeadDisRow.accept(new String[]{"(iii) Into Seawater", data.getP6LeadQ1DisSeaNoCurr(), data.getP6LeadQ1DisSeaNoPrev(), data.getP6LeadQ1DisSeaWithCurr(), data.getP6LeadQ1DisSeaWithPrev(), data.getP6LeadQ1DisSeaLevel()});
            addLeadDisRow.accept(new String[]{"(iv) Sent to third-parties", data.getP6LeadQ1DisThirdNoCurr(), data.getP6LeadQ1DisThirdNoPrev(), data.getP6LeadQ1DisThirdWithCurr(), data.getP6LeadQ1DisThirdWithPrev(), data.getP6LeadQ1DisThirdLevel()});
            addLeadDisRow.accept(new String[]{"(v) Others", data.getP6LeadQ1DisOtherNoCurr(), data.getP6LeadQ1DisOtherNoPrev(), data.getP6LeadQ1DisOtherWithCurr(), data.getP6LeadQ1DisOtherWithPrev(), data.getP6LeadQ1DisOtherLevel()});

            XWPFTableRow rL1DTot = tableLeadQ1.createRow(); ensureCells(rL1DTot, 3);
            styleCell(rL1DTot.getCell(0), "Total water discharged (in kilolitres)", true);
            rL1DTot.getCell(1).setText(checkNull(data.getP6LeadQ1DisTotalCurr()));
            rL1DTot.getCell(2).setText(checkNull(data.getP6LeadQ1DisTotalPrev()));

            if (data.getP6LeadQ1AssuranceNote() != null && !data.getP6LeadQ1AssuranceNote().trim().isEmpty()) {
                addNote(doc, "Note: " + data.getP6LeadQ1AssuranceNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- Lead Q2: Scope 3 ---
            addBoldText(doc, "2. Please provide details of total Scope 3 emissions & its intensity, in the following format:");
            XWPFTable tableLeadQ2 = doc.createTable();
            tableLeadQ2.setWidth("100%");
            setColumnWidths(tableLeadQ2, 3000, 2000, 2500, 2500);

            XWPFTableRow hRowLeadQ2 = tableLeadQ2.getRow(0); ensureCells(hRowLeadQ2, 4);
            styleCell(hRowLeadQ2.getCell(0), "Parameter", true); styleCell(hRowLeadQ2.getCell(1), "Unit", true);
            styleCell(hRowLeadQ2.getCell(2), "FY " + fyCurP6, true); styleCell(hRowLeadQ2.getCell(3), "FY " + fyPrevP6, true);

            XWPFTableRow rL2_1 = tableLeadQ2.createRow(); ensureCells(rL2_1, 4);
            styleCell(rL2_1.getCell(0), "Total Scope 3 emissions", true); rL2_1.getCell(1).setText("Metric tonnes of CO2 equivalent");
            rL2_1.getCell(2).setText(checkNull(data.getP6LeadQ2Scope3Curr())); rL2_1.getCell(3).setText(checkNull(data.getP6LeadQ2Scope3Prev()));

            XWPFTableRow rL2_2 = tableLeadQ2.createRow(); ensureCells(rL2_2, 4);
            styleCell(rL2_2.getCell(0), "Total Scope 3 emissions per rupee of turnover", true);
            rL2_2.getCell(2).setText(checkNull(data.getP6LeadQ2IntTurnCurr())); rL2_2.getCell(3).setText(checkNull(data.getP6LeadQ2IntTurnPrev()));

            XWPFTableRow rL2_3 = tableLeadQ2.createRow(); ensureCells(rL2_3, 4);
            styleCell(rL2_3.getCell(0), "Total Scope 3 emission intensity (optional)", true);
            rL2_3.getCell(2).setText(checkNull(data.getP6LeadQ2IntOptCurr())); rL2_3.getCell(3).setText(checkNull(data.getP6LeadQ2IntOptPrev()));

            if (data.getP6LeadQ2AssuranceNote() != null && !data.getP6LeadQ2AssuranceNote().trim().isEmpty()) {
                addNote(doc, "Note: " + data.getP6LeadQ2AssuranceNote());
            }
            doc.createParagraph().createRun().addBreak();

            // --- Lead Q3 to Q7 ---
            addBoldText(doc, "3. With respect to the ecologically sensitive areas... provide details of impact on biodiversity.");
            setTextWithBreaks(doc.createParagraph().createRun(), checkNull(data.getP6LeadQ3EcoImpact()));
            doc.createParagraph().createRun().addBreak();

            addBoldText(doc, "4. Specific initiatives or innovative technology for resource efficiency:");
            XWPFTable tableLeadQ4 = doc.createTable(); tableLeadQ4.setWidth("100%");
            XWPFTableRow hRowLeadQ4 = tableLeadQ4.getRow(0); ensureCells(hRowLeadQ4, 4);
            setColumnWidths(tableLeadQ4, 1000, 3000, 3500, 2500);
            styleCell(hRowLeadQ4.getCell(0), "Sr. No", true); styleCell(hRowLeadQ4.getCell(1), "Initiative undertaken", true);
            styleCell(hRowLeadQ4.getCell(2), "Details of the initiative", true); styleCell(hRowLeadQ4.getCell(3), "Outcome", true);

            if (data.getP6LeadQ4InitiativesList() != null && !data.getP6LeadQ4InitiativesList().isEmpty()) {
                for (BrsrReportRequest.P6LeadQ4Initiative item : data.getP6LeadQ4InitiativesList()) {
                    addDynamicRow(tableLeadQ4, new String[]{checkNull(item.getSNo()), checkNull(item.getInitiative()), checkNull(item.getDetails()), checkNull(item.getOutcome())});
                }
            } else { addDynamicRow(tableLeadQ4, new String[]{"-", "-", "-", "-"}); }
            doc.createParagraph().createRun().addBreak();

            addBoldText(doc, "5. Does the entity have a business continuity and disaster management plan?");
            setTextWithBreaks(doc.createParagraph().createRun(), checkNull(data.getP6LeadQ5DisasterPlan()));
            doc.createParagraph().createRun().addBreak();

            addBoldText(doc, "6. Disclose any significant adverse impact to the environment, arising from the value chain:");
            setTextWithBreaks(doc.createParagraph().createRun(), checkNull(data.getP6LeadQ6ValueChainImpact()));
            doc.createParagraph().createRun().addBreak();

            addBoldText(doc, "7. Percentage of value chain partners assessed for environmental impacts.");
            doc.createParagraph().createRun().setText(checkNull(data.getP6LeadQ7ValueChainAssessed()));
            doc.createParagraph().createRun().addBreak();

            // =================================================================
            // SECTION C: PRINCIPLE 7 (PUBLIC POLICY)
            // =================================================================
            // ================= SECTION C: PRINCIPLE 7 =================
            XWPFParagraph pP7 = doc.createParagraph();
            pP7.setSpacingBefore(400);
            XWPFRun rP7 = pP7.createRun();
            rP7.setText("PRINCIPLE 7: Businesses, when engaging in influencing public and regulatory policy, should do so in a manner that is responsible and transparent");
            rP7.setBold(true);
            rP7.setFontSize(12);
            rP7.setColor("548235");

            // --- Essential Indicators ---
            XWPFParagraph p7Ess = doc.createParagraph();
            XWPFRun rP7Ess = p7Ess.createRun();
            rP7Ess.setText("Essential Indicators");
            rP7Ess.setBold(true);
            doc.createParagraph().createRun().addBreak();

            // Q1a
            addBoldText(doc, "1. a. Number of affiliations with trade and industry chambers/ associations.");
            doc.createParagraph().createRun().setText(checkNull(data.getP7Q1aAffiliations()));
            doc.createParagraph().createRun().addBreak();

            // Q1b
            addBoldText(doc, "b. List the top 10 trade and industry chambers/ associations (determined based on the total members of such body) the entity is a member of/ affiliated to.");
            XWPFTable tableP7Q1b = doc.createTable();
            tableP7Q1b.setWidth("100%");
            setColumnWidths(tableP7Q1b, 1000, 6000, 3000); // 3 cols

            XWPFTableRow hRowP7Q1b = tableP7Q1b.getRow(0); ensureCells(hRowP7Q1b, 3);
            styleCell(hRowP7Q1b.getCell(0), "S. No.", true);
            styleCell(hRowP7Q1b.getCell(1), "Name of the trade and industry chambers/ associations", true);
            styleCell(hRowP7Q1b.getCell(2), "Reach of trade and industry chambers/ associations (State/National)", true);

            if (data.getP7Q1bList() != null && !data.getP7Q1bList().isEmpty()) {
                for (BrsrReportRequest.P7Q1bAssociation item : data.getP7Q1bList()) {
                    addDynamicRow(tableP7Q1b, new String[]{checkNull(item.getSNo()), checkNull(item.getName()), checkNull(item.getReach())});
                }
            } else {
                addDynamicRow(tableP7Q1b, new String[]{"-", "-", "-"});
            }
            doc.createParagraph().createRun().addBreak();

            // Q2
            addBoldText(doc, "2. Provide details of corrective action taken or underway on any issues related to anti-competitive conduct by the entity, based on adverse orders from regulatory authorities.");
            XWPFTable tableP7Q2 = doc.createTable();
            tableP7Q2.setWidth("100%");
            setColumnWidths(tableP7Q2, 3000, 4000, 3000);

            XWPFTableRow hRowP7Q2 = tableP7Q2.getRow(0); ensureCells(hRowP7Q2, 3);
            styleCell(hRowP7Q2.getCell(0), "Name of authority", true);
            styleCell(hRowP7Q2.getCell(1), "Brief of the case", true);
            styleCell(hRowP7Q2.getCell(2), "Corrective action taken", true);

            if (data.getP7Q2List() != null && !data.getP7Q2List().isEmpty()) {
                for (BrsrReportRequest.P7Q2CorrectiveAction item : data.getP7Q2List()) {
                    addDynamicRow(tableP7Q2, new String[]{checkNull(item.getAuthority()), checkNull(item.getBrief()), checkNull(item.getCorrectiveAction())});
                }
            } else {
                addDynamicRow(tableP7Q2, new String[]{"-", "-", "-"});
            }
            doc.createParagraph().createRun().addBreak();

            // --- Leadership Indicators ---
            XWPFParagraph p7Lead = doc.createParagraph();
            XWPFRun rP7Lead = p7Lead.createRun();
            rP7Lead.setText("Leadership Indicators");
            rP7Lead.setBold(true);
            doc.createParagraph().createRun().addBreak();

            // Lead Q1
            addBoldText(doc, "1. Details of public policy positions advocated by the entity:");
            XWPFTable tableP7Lead = doc.createTable();
            tableP7Lead.setWidth("100%");
            setColumnWidths(tableP7Lead, 800, 2000, 2000, 1500, 1700, 2000); // 6 cols

            XWPFTableRow hRowP7Lead = tableP7Lead.getRow(0); ensureCells(hRowP7Lead, 6);
            styleCell(hRowP7Lead.getCell(0), "S. No.", true);
            styleCell(hRowP7Lead.getCell(1), "Public policy advocated", true);
            styleCell(hRowP7Lead.getCell(2), "Method resorted for such advocacy", true);
            styleCell(hRowP7Lead.getCell(3), "Whether information available in public domain? (Yes/No)", true);
            styleCell(hRowP7Lead.getCell(4), "Frequency of Review by Board", true);
            styleCell(hRowP7Lead.getCell(5), "Web Link, if available", true);

            if (data.getP7LeadQ1List() != null && !data.getP7LeadQ1List().isEmpty()) {
                for (BrsrReportRequest.P7LeadQ1Policy item : data.getP7LeadQ1List()) {
                    addDynamicRow(tableP7Lead, new String[]{checkNull(item.getSNo()), checkNull(item.getPolicy()), checkNull(item.getMethod()), checkNull(item.getPublicDomain()), checkNull(item.getFrequency()), checkNull(item.getLink())});
                }
            } else {
                addDynamicRow(tableP7Lead, new String[]{"-", "-", "-", "-", "-", "-"});
            }
            doc.createParagraph().createRun().addBreak();

            // =================================================================
            // SECTION C: PRINCIPLE 8 (INCLUSIVE GROWTH)
            // =================================================================
            // ================= SECTION C: PRINCIPLE 8 =================
            XWPFParagraph pP8 = doc.createParagraph();
            pP8.setSpacingBefore(400);
            XWPFRun rP8 = pP8.createRun();
            rP8.setText("PRINCIPLE 8: Businesses should promote inclusive growth and equitable development");
            rP8.setBold(true);
            rP8.setFontSize(12);
            rP8.setColor("548235");

            XWPFParagraph p8Ess = doc.createParagraph();
            XWPFRun rP8Ess = p8Ess.createRun();
            rP8Ess.setText("Essential Indicators");
            rP8Ess.setBold(true);
            doc.createParagraph().createRun().addBreak();

            // Q1: SIA
            addBoldText(doc, "1. Details of Social Impact Assessments (SIA) of projects undertaken by the entity based on applicable laws, in the current financial year.");
            XWPFTable tableP8Q1 = doc.createTable(); tableP8Q1.setWidth("100%");
            setColumnWidths(tableP8Q1, 2000, 1500, 1500, 1500, 1500, 2000); // 6 cols

            XWPFTableRow hRowP8Q1 = tableP8Q1.getRow(0); ensureCells(hRowP8Q1, 6);
            styleCell(hRowP8Q1.getCell(0), "Name and brief details of project", true); styleCell(hRowP8Q1.getCell(1), "SIA Notification No.", true);
            styleCell(hRowP8Q1.getCell(2), "Date of notification", true); styleCell(hRowP8Q1.getCell(3), "Whether conducted by independent external agency (Yes / No)", true);
            styleCell(hRowP8Q1.getCell(4), "Results communicated in public domain (Yes / No)", true); styleCell(hRowP8Q1.getCell(5), "Relevant Web link", true);

            if (data.getP8Q1SiaList() != null && !data.getP8Q1SiaList().isEmpty()) {
                for (BrsrReportRequest.P8Q1Sia item : data.getP8Q1SiaList()) {
                    addDynamicRow(tableP8Q1, new String[]{checkNull(item.getName()), checkNull(item.getNotifNo()), checkNull(item.getDate()), checkNull(item.getIndependent()), checkNull(item.getPublicDomain()), checkNull(item.getLink())});
                }
            } else { addDynamicRow(tableP8Q1, new String[]{"-", "-", "-", "-", "-", "-"}); }
            doc.createParagraph().createRun().addBreak();

            // Q2: R&R
            addBoldText(doc, "2. Provide information on project(s) for which ongoing Rehabilitation and Resettlement (R&R) is being undertaken by your entity, in the following format:");
            XWPFTable tableP8Q2 = doc.createTable(); tableP8Q2.setWidth("100%");
            setColumnWidths(tableP8Q2, 800, 2500, 1500, 1500, 1200, 1200, 1500); // 7 cols

            XWPFTableRow hRowP8Q2 = tableP8Q2.getRow(0); ensureCells(hRowP8Q2, 7);
            styleCell(hRowP8Q2.getCell(0), "S. No.", true); styleCell(hRowP8Q2.getCell(1), "Name of Project for which R&R is ongoing", true);
            styleCell(hRowP8Q2.getCell(2), "State", true); styleCell(hRowP8Q2.getCell(3), "District", true);
            styleCell(hRowP8Q2.getCell(4), "No. of Project Affected Families (PAFs)", true); styleCell(hRowP8Q2.getCell(5), "% of PAFs covered by R&R", true);
            styleCell(hRowP8Q2.getCell(6), "Amounts paid to PAFs in the FY (In INR)", true);

            if (data.getP8Q2RrList() != null && !data.getP8Q2RrList().isEmpty()) {
                for (BrsrReportRequest.P8Q2Rr item : data.getP8Q2RrList()) {
                    addDynamicRow(tableP8Q2, new String[]{checkNull(item.getSNo()), checkNull(item.getName()), checkNull(item.getState()), checkNull(item.getDistrict()), checkNull(item.getPafs()), checkNull(item.getPerc()), checkNull(item.getAmount())});
                }
            } else { addDynamicRow(tableP8Q2, new String[]{"-", "-", "-", "-", "-", "-", "-"}); }
            doc.createParagraph().createRun().addBreak();

            // Q3: Grievance
            addBoldText(doc, "3. Describe the mechanisms to receive and redress grievances of the community.");
            setTextWithBreaks(doc.createParagraph().createRun(), checkNull(data.getP8Q3GrievanceMech()));
            doc.createParagraph().createRun().addBreak();

            // Q4: Material Sourcing
            addBoldText(doc, "4. Percentage of input material (inputs to total inputs by value) sourced from suppliers:");
            XWPFTable tableP8Q4 = doc.createTable(); tableP8Q4.setWidth("100%");
            setColumnWidths(tableP8Q4, 5000, 2500, 2500);

            XWPFTableRow hRowP8Q4 = tableP8Q4.getRow(0); ensureCells(hRowP8Q4, 3);
            styleCell(hRowP8Q4.getCell(0), "Parameter", true); styleCell(hRowP8Q4.getCell(1), "FY " + fyCurP6, true); styleCell(hRowP8Q4.getCell(2), "FY " + fyPrevP6, true);
            addDynamicRow(tableP8Q4, new String[]{"Directly sourced from MSMEs/ small producers", checkNull(data.getP8Q4MsmeCurr()), checkNull(data.getP8Q4MsmePrev())});
            addDynamicRow(tableP8Q4, new String[]{"Directly from within India", checkNull(data.getP8Q4IndiaCurr()), checkNull(data.getP8Q4IndiaPrev())});
            doc.createParagraph().createRun().addBreak();

            // Q5: Job Creation
            addBoldText(doc, "5. Job creation in smaller towns – Disclose wages paid to persons employed... as % of total wage cost");
            XWPFTable tableP8Q5 = doc.createTable(); tableP8Q5.setWidth("100%");
            setColumnWidths(tableP8Q5, 5000, 2500, 2500);

            XWPFTableRow hRowP8Q5 = tableP8Q5.getRow(0); ensureCells(hRowP8Q5, 3);
            styleCell(hRowP8Q5.getCell(0), "Location", true); styleCell(hRowP8Q5.getCell(1), "FY " + fyCurP6, true); styleCell(hRowP8Q5.getCell(2), "FY " + fyPrevP6, true);
            addDynamicRow(tableP8Q5, new String[]{"Rural", checkNull(data.getP8Q5RuralCurr()), checkNull(data.getP8Q5RuralPrev())});
            addDynamicRow(tableP8Q5, new String[]{"Semi-urban", checkNull(data.getP8Q5SemiCurr()), checkNull(data.getP8Q5SemiPrev())});
            addDynamicRow(tableP8Q5, new String[]{"Urban", checkNull(data.getP8Q5UrbanCurr()), checkNull(data.getP8Q5UrbanPrev())});
            addDynamicRow(tableP8Q5, new String[]{"Metropolitan", checkNull(data.getP8Q5MetroCurr()), checkNull(data.getP8Q5MetroPrev())});
            doc.createParagraph().createRun().addBreak();

            // =================================================================
            // SECTION C: PRINCIPLE 9 (CONSUMER)
            // =================================================================
            XWPFParagraph pP9 = doc.createParagraph();
            pP9.setPageBreak(true);
            pP9.setSpacingBefore(300);
            XWPFRun rP9 = pP9.createRun();
            rP9.setText("PRINCIPLE 9: Businesses should engage with and provide value to their consumers in a responsible manner.");
            rP9.setBold(true);
            rP9.setFontSize(12);
            rP9.setColor(COLOR_THEME_GREEN);
            rP9.setFontFamily("Calibri");

            addBoldText(doc, "Essential Indicators");
            addBoldText(doc, "1. Describe the mechanisms in place to receive and respond to consumer complaints.");
            XWPFParagraph p9_1 = doc.createParagraph();
            setTextWithBreaks(p9_1.createRun(), checkNull(data.getP9ConsumerComplaintMech()));

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "2. Number of consumer complaints (Received / Pending):");
            XWPFTable tableComp9 = doc.createTable();
            tableComp9.setWidth("100%");
            XWPFTableRow h9 = tableComp9.getRow(0);
            ensureCells(h9, 3);
            styleCell(h9.getCell(0), "Category", true);
            styleCell(h9.getCell(1), fyCur, true);
            styleCell(h9.getCell(2), fyPrev, true);

            addDynamicRow(tableComp9, new String[]{"Data Privacy", checkNull(data.getP9DataPrivacyCurr()), checkNull(data.getP9DataPrivacyPrev())});
            addDynamicRow(tableComp9, new String[]{"Advertising", checkNull(data.getP9AdvertisingCurr()), checkNull(data.getP9AdvertisingPrev())});
            addDynamicRow(tableComp9, new String[]{"Cyber-security", checkNull(data.getP9CyberCurr()), checkNull(data.getP9CyberPrev())});
            addDynamicRow(tableComp9, new String[]{"Delivery of Services", checkNull(data.getP9DeliveryCurr()), checkNull(data.getP9DeliveryPrev())});
            addDynamicRow(tableComp9, new String[]{"Restrictive Practices", checkNull(data.getP9RestrictiveCurr()), checkNull(data.getP9RestrictivePrev())});
            addDynamicRow(tableComp9, new String[]{"Unfair Practices", checkNull(data.getP9UnfairCurr()), checkNull(data.getP9UnfairPrev())});
            addDynamicRow(tableComp9, new String[]{"Other", checkNull(data.getP9OtherCurr()), checkNull(data.getP9OtherPrev())});

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "3. Details of instances of product recalls on account of safety issues:");
            XWPFTable tableRec = doc.createTable();
            tableRec.setWidth("100%");
            addRow(tableRec, "Voluntary recalls", data.getP9RecallVoluntary(), "-"); // Using 3rd col as filler if needed, or adjust addRow
            addRow(tableRec, "Forced recalls", data.getP9RecallForced(), "-");

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "4. Does the entity have a framework/policy on cyber security and data privacy?");
            XWPFParagraph p9_4 = doc.createParagraph();
            setTextWithBreaks(p9_4.createRun(), checkNull(data.getP9CyberSecurityPolicy()));

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "5. Provide details of any corrective actions taken or underway on issues relating to advertising, delivery, cyber security etc.");
            XWPFParagraph p9_5 = doc.createParagraph();
            setTextWithBreaks(p9_5.createRun(), checkNull(data.getP9CorrectiveActions()));

            addSectionHeader(doc, "Leadership Indicators");
            addBoldText(doc, "1. Channels/platforms where product information can be accessed.");
            XWPFParagraph p9L1 = doc.createParagraph();
            setTextWithBreaks(p9L1.createRun(), checkNull(data.getP9InfoChannels()));

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "2. Steps taken to inform consumers about safe usage.");
            XWPFParagraph p9L2 = doc.createParagraph();
            setTextWithBreaks(p9L2.createRun(), checkNull(data.getP9SafeUsageSteps()));

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "3. Mechanisms to inform consumers of disruption of services.");
            XWPFParagraph p9L3 = doc.createParagraph();
            setTextWithBreaks(p9L3.createRun(), checkNull(data.getP9DisruptionInfo()));

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "4. Display of product information over and above mandated laws / Customer Satisfaction.");
            XWPFParagraph p9L4 = doc.createParagraph();
            setTextWithBreaks(p9L4.createRun(), checkNull(data.getP9ProductInfoDisplay()));
            p9L4.createRun().addBreak();
            p9L4.createRun().setText("Customer Satisfaction Survey Result: " + checkNull(data.getP9CustomerSatSurvey()));

            // --- Footer ---
            XWPFParagraph footer = doc.createParagraph();
            footer.setAlignment(ParagraphAlignment.CENTER);
            footer.setSpacingBefore(400);
            XWPFRun footerRun = footer.createRun();
            footerRun.setText("Generated via ESGO Portal");
            footerRun.setItalic(true);
            footerRun.setColor("808080");
            footerRun.setFontSize(9);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // --- HELPER METHODS ---

    private void setTextWithBreaks(XWPFRun run, String text) {
        if (text == null) return;

        // Get the parent paragraph to set alignment
        if (run.getParent() instanceof XWPFParagraph) {
            ((XWPFParagraph) run.getParent()).setAlignment(ParagraphAlignment.LEFT);
        }

        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            run.setText(lines[i]);
            if (i < lines.length - 1) {
                run.addBreak();
            }
        }
    }

    // --- HELPER: Table Cell with Newlines (JUSTIFIED) ---
    private void fillCellWithNewlines(XWPFTableCell cell, String text) {
        if (cell.getParagraphs().size() > 0) {
            cell.removeParagraph(0);
        }

        if (text == null) text = "-";
        String[] lines = text.split("\n");

        for (String line : lines) {
            XWPFParagraph p = cell.addParagraph();
            p.setSpacingAfter(0);

            // --- FIX: JUSTIFY TEXT ---
            p.setAlignment(ParagraphAlignment.LEFT);

            XWPFRun r = p.createRun();
            r.setText(line);
            r.setFontFamily("Calibri");
            r.setFontSize(10);
        }
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP);
    }

    private String checkNull(String val) {
        return (val == null || val.trim().isEmpty()) ? "-" : val;
    }

    private String[] calculateTotalRow(String label, String[] row1, String[] row2) {
        if (row1 == null || row2 == null || row1.length < 6 || row2.length < 6) return new String[]{label, "-", "-", "-", "-", "-"};

        double totalA = parseSafe(row1[1]) + parseSafe(row2[1]);
        double maleNo = parseSafe(row1[2]) + parseSafe(row2[2]);
        double femNo = parseSafe(row1[4]) + parseSafe(row2[4]);

        String malePerc = totalA > 0 ? String.format("%.1f", (maleNo / totalA) * 100) : "0.0";
        String femPerc = totalA > 0 ? String.format("%.1f", (femNo / totalA) * 100) : "0.0";

        return new String[]{ label, String.valueOf((int)totalA), String.valueOf((int)maleNo), malePerc, String.valueOf((int)femNo), femPerc };
    }

    private double parseSafe(String val) {
        if (val == null) return 0.0;
        String cleanVal = val.trim().replaceAll("[^0-9.]", "");
        if (cleanVal.isEmpty()) return 0.0;
        try { return Double.parseDouble(cleanVal); } catch (NumberFormatException e) { return 0.0; }
    }

    // --- HELPER: Add Note with Line Break Support ---
    private void addNote(XWPFDocument doc, String noteContent) {
        if(noteContent != null && !noteContent.trim().isEmpty()) {
            XWPFParagraph p = doc.createParagraph();
            p.setSpacingBefore(100);

            // "Note:" Label
            XWPFRun rLabel = p.createRun();
            rLabel.setText("Note: ");
            rLabel.setBold(true);
            rLabel.setFontSize(10);
            rLabel.setFontFamily("Calibri");

            // Content (with line break support)
            XWPFRun rContent = p.createRun();
            rContent.setFontSize(10);
            rContent.setFontFamily("Calibri");
            rContent.setItalic(true);

            // FIX: Use the helper to respect \n characters
            setTextWithBreaks(rContent, noteContent);
        }
    }

    private void addRow(XWPFTable table, String label, String value) {
        XWPFTableRow row = table.createRow();
        ensureCells(row, 2);
        styleCell(row.getCell(0), label, false);

        if(value != null && value.contains("\n")) {
            if(row.getCell(1).getParagraphs().size() > 0) row.getCell(1).removeParagraph(0);
            String[] lines = value.split("\n");
            for(String line : lines) {
                XWPFParagraph p = row.getCell(1).addParagraph();
                p.setSpacingAfter(0);
                XWPFRun r = p.createRun();
                r.setText(line);
                r.setFontFamily("Calibri");
                r.setFontSize(10);
            }
        } else {
            row.getCell(1).setText(checkNull(value));
        }
    }

    /*private void addDynamicHeaderRow(XWPFTable table, String[] headers) {
        XWPFTableRow row = (table.getRow(0) != null) ? table.getRow(0) : table.createRow();
        ensureCells(row, headers.length);
        for (int i = 0; i < headers.length; i++) styleCell(row.getCell(i), headers[i], true);
    }*/

    private void addDynamicRow(XWPFTable table, String[] values) {
        XWPFTableRow row = table.createRow();
        ensureCells(row, values.length);

        for (int i = 0; i < values.length; i++) {
            // FIX: Apply multi-line check for dynamic rows too (like Q24 Rationale)
            String val = values[i];
            XWPFTableCell cell = row.getCell(i);

            if(val != null && val.contains("\n")) {
                if(cell.getParagraphs().size() > 0) cell.removeParagraph(0);
                String[] lines = val.split("\n");
                for(String line : lines) {
                    XWPFParagraph p = cell.addParagraph();
                    p.setSpacingAfter(0);
                    XWPFRun r = p.createRun();
                    r.setText(line);
                    r.setFontFamily("Calibri");
                    r.setFontSize(10);
                }
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP); // Ensure top align
            } else {
                styleCell(cell, checkNull(val), false);
            }
        }
    }

    private void addSectionTitleRow(XWPFTable table, String title, int cols) {
        XWPFTableRow row = table.createRow();
        ensureCells(row, cols);
        for(int i=0; i<cols; i++) row.getCell(i).setColor("F2F2F2");
        XWPFTableCell cell = row.getCell(0);
        if(cell.getParagraphs().size()>0) cell.removeParagraph(0);
        XWPFParagraph p = cell.addParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun r = p.createRun();
        r.setText(title);
        r.setBold(true);
        r.setFontSize(10);
        r.setFontFamily("Calibri");
        r.setColor("000000");
    }

    private void mergeCellsHorizontal(XWPFTableRow row, int startCol, int endCol) {
        for (int i = startCol; i <= endCol; i++) {
            XWPFTableCell cell = row.getCell(i);
            if (i == startCol) {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    private void ensureCells(XWPFTableRow row, int count) {
        while (row.getTableCells().size() < count) row.addNewTableCell();
    }

    private void styleCell(XWPFTableCell cell, String text, boolean isHeader) {
        if (cell == null) return;
        if(cell.getParagraphs().size() > 0) cell.removeParagraph(0);
        XWPFParagraph p = cell.addParagraph();
        XWPFRun r = p.createRun();
        r.setText(text);
        r.setFontFamily("Calibri");
        r.setFontSize(10);

        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP);

        if(isHeader) {
            cell.setColor("E7E7E7");
            r.setBold(true);
            r.setColor(COLOR_THEME_GREEN);
            p.setAlignment(ParagraphAlignment.CENTER);
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        }
    }

    private void addSectionHeader(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingAfter(200);
        XWPFRun r = p.createRun();
        r.setText(text);
        r.setBold(true);
        r.setFontSize(14);
        r.setColor(COLOR_THEME_GREEN);
        r.setFontFamily("Calibri");
    }

    private void addBoldText(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        r.setText(text);
        r.setBold(true);
        r.setFontSize(11);
        r.setFontFamily("Calibri");
    }

    private void addStyledLegalRow(XWPFTable table, String col0, String col1, String col2, String col3, String col4, String col5) {
        XWPFTableRow row = table.createRow();
        ensureCells(row, 6);

        styleCell(row.getCell(0), col0, true);  // Bold Green
        styleCell(row.getCell(1), col1, false);
        styleCell(row.getCell(2), col2, false);
        styleCell(row.getCell(3), col3, false);
        styleCell(row.getCell(4), col4, false);
        styleCell(row.getCell(5), col5, false);
    }

    // --- HELPER: Generate Complex Well-Being Table ---
    private void generateWellBeingTable(XWPFDocument doc, List<BrsrReportRequest.WellBeingRow> rows, String label1, String label2) {
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Configure Grid (12 Columns: Category, Total, 5 Benefits * 2 cols)
        CTTblGrid grid = table.getCTTbl().addNewTblGrid();
        grid.addNewGridCol().setW(BigInteger.valueOf(1500)); // Category
        grid.addNewGridCol().setW(BigInteger.valueOf(800));  // Total
        for(int i=0; i<10; i++) grid.addNewGridCol().setW(BigInteger.valueOf(700)); // 10 data columns

        // Determine target group for the master header
        String targetGroup = (label1 != null && label1.toLowerCase().contains("worker")) ? "workers" : "employees";

        // --- HEADER ROW 1 (Master Header) ---
        XWPFTableRow h0 = table.getRow(0);
        ensureCells(h0, 12);
        styleCell(h0.getCell(0), "Category", true);
        styleCell(h0.getCell(1), "Total (A)", true);
        styleCell(h0.getCell(2), "% of " + targetGroup + " covered by", true);
        mergeCellsHorizontal(h0, 2, 11);

        // --- HEADER ROW 2 (Benefit Names) ---
        XWPFTableRow h1 = table.createRow();
        ensureCells(h1, 12);
        styleCell(h1.getCell(0), "", true); // Blank under Category
        styleCell(h1.getCell(1), "", true); // Blank under Total (A)

        String[] benefits = {"Health insurance", "Accident insurance", "Maternity benefits", "Paternity Benefits", "Day Care facilities"};
        for(int i=0; i<5; i++) {
            int col = 2 + (i*2);
            styleCell(h1.getCell(col), benefits[i], true);
            mergeCellsHorizontal(h1, col, col+1);
        }

        // --- HEADER ROW 3 (Number / %) ---
        XWPFTableRow h2 = table.createRow();
        ensureCells(h2, 12);
        styleCell(h2.getCell(0), "", true);
        styleCell(h2.getCell(1), "", true);

        for(int i=0; i<5; i++) {
            int col = 2 + (i*2);
            styleCell(h2.getCell(col), "Number (B)", true);
            styleCell(h2.getCell(col+1), "% (B / A)", true);
        }

        // --- DATA ROWS ---
        if (rows != null && !rows.isEmpty()) {
            // Section 1 Header (e.g., Permanent Employees)
            addSectionTitleRow(table, label1, 12);

            int count = 0;
            for (BrsrReportRequest.WellBeingRow r : rows) {
                // Section 2 Header (e.g., Other than Permanent Employees)
                if (count == 3) {
                    addSectionTitleRow(table, label2, 12);
                }

                XWPFTableRow row = table.createRow();
                ensureCells(row, 12);
                styleCell(row.getCell(0), checkNull(r.getCategory()), false);
                styleCell(row.getCell(1), checkNull(r.getTotalA()), false);

                styleCell(row.getCell(2), checkNull(r.getHealthNo()), false);
                styleCell(row.getCell(3), checkNull(r.getHealthPerc()) + "%", false);

                styleCell(row.getCell(4), checkNull(r.getAccidentNo()), false);
                styleCell(row.getCell(5), checkNull(r.getAccidentPerc()) + "%", false);

                styleCell(row.getCell(6), checkNull(r.getMaternityNo()), false);
                styleCell(row.getCell(7), checkNull(r.getMaternityPerc()) + "%", false);

                styleCell(row.getCell(8), checkNull(r.getPaternityNo()), false);
                styleCell(row.getCell(9), checkNull(r.getPaternityPerc()) + "%", false);

                styleCell(row.getCell(10), checkNull(r.getDaycareNo()), false);
                styleCell(row.getCell(11), checkNull(r.getDaycarePerc()) + "%", false);

                count++;
            }
        } else {
            addDynamicRow(table, new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"});
        }
    }

    // --- HELPER: Set Column Widths ---
    private void setColumnWidths(XWPFTable table, int width1, int width2) {
        // Create the grid for the table
        CTTblGrid grid = table.getCTTbl().addNewTblGrid();

        // Column 1 Width
        grid.addNewGridCol().setW(BigInteger.valueOf(width1));

        // Column 2 Width
        grid.addNewGridCol().setW(BigInteger.valueOf(width2));
    }

    // --- HELPER: Add Safety Group Row (Metric + Employee Row + Worker Row) ---
    private void addSafetyGroupRow(XWPFTable table, String metricName, String ec, String ep, String wc, String wp) {
        // Row 1: Employee
        XWPFTableRow r1 = table.createRow();
        ensureCells(r1, 4);
        styleCell(r1.getCell(0), metricName, false); // Metric Name
        styleCell(r1.getCell(1), "Employees", false);
        styleCell(r1.getCell(2), ec, false);
        styleCell(r1.getCell(3), ep, false);

        // Row 2: Worker
        XWPFTableRow r2 = table.createRow();
        ensureCells(r2, 4);
        styleCell(r2.getCell(0), "", false); // Leave blank to simulate merge
        styleCell(r2.getCell(1), "Workers", false);
        styleCell(r2.getCell(2), wc, false);
        styleCell(r2.getCell(3), wp, false);
    }

    // Simple helper for I, II, III...
    private String romanToDecimal(int num) {
        String[] romans = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        if (num > 0 && num <= 10) return romans[num-1];
        return String.valueOf(num);
    }
    //helper for princple 5 essential 2
    private void addBoldRow(XWPFTable table, String text, int cells) {
        XWPFTableRow row = table.createRow();
        ensureCells(row, cells);
        styleCell(row.getCell(0), text, true); // Bold first cell
    }

    private String intToRomanLower(int num) {
        String[] romans = {"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x"};
        if (num > 0 && num <= 10) return romans[num-1];
        return String.valueOf(num);
    }

    // Helper for 3-column row (Label, Val1, Val2)
    private void addRow(XWPFTable table, String label, String val1, String val2) {
        XWPFTableRow row = table.createRow();
        ensureCells(row, 3);
        styleCell(row.getCell(0), label, false);
        styleCell(row.getCell(1), checkNull(val1), false);
        styleCell(row.getCell(2), checkNull(val2), false);
    }

    /**
     * Formats AI-generated paragraphs with a bold label and spacing.
     */
    private void formatAISection(XWPFDocument doc, String question, String answer) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingBefore(200); // Adds professional gap between sections

        // The Question (Bold)
        XWPFRun runQ = p.createRun();
        runQ.setBold(true);
        runQ.setFontFamily("Calibri");
        runQ.setText(question);
        runQ.addBreak(); // Forces answer to start on a new line

        // The AI Answer (Regular)
        XWPFRun runA = p.createRun();
        runA.setFontFamily("Calibri");
        // Fallback if AI generation failed or was empty
        runA.setText(answer != null && !answer.trim().isEmpty() ? answer : "Not disclosed.");
    }

    /**
     * Standardizes spacing between sections.
     */
    private void addSpacing(XWPFDocument doc) {
        XWPFParagraph spacer = doc.createParagraph();
        spacer.createRun().addBreak();
    }

    /**
     * Ensures table headers are styled consistently.
     */
    private void addDynamicHeaderRow(XWPFTable table, String[] headers) {
        XWPFTableRow headerRow = table.getRow(0); // Tables created with 1 row by default
        for (int i = 0; i < headers.length; i++) {
            XWPFTableCell cell = (i == 0) ? headerRow.getCell(0) : headerRow.addNewTableCell();
            XWPFParagraph p = cell.getParagraphs().get(0);
            p.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun r = p.createRun();
            r.setBold(true);
            r.setText(headers[i]);
            cell.setColor("F2F2F2"); // Light grey background
        }
    }

    /**
     * Fixes "Invisible Tables" by forcing grid borders to appear in Word.
     */
    private void setTableBorders(XWPFTable table) {
        org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr tblPr = table.getCTTbl().getTblPr();
        if (tblPr == null) {
            tblPr = table.getCTTbl().addNewTblPr();
        }

        org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();

        // Set all borders (Top, Bottom, Left, Right, InsideH, InsideV) to a Single Line
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.Enum lineStyle = org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.SINGLE;

        if (!borders.isSetBottom()) borders.addNewBottom().setVal(lineStyle);
        if (!borders.isSetTop()) borders.addNewTop().setVal(lineStyle);
        if (!borders.isSetLeft()) borders.addNewLeft().setVal(lineStyle);
        if (!borders.isSetRight()) borders.addNewRight().setVal(lineStyle);
        if (!borders.isSetInsideH()) borders.addNewInsideH().setVal(lineStyle);
        if (!borders.isSetInsideV()) borders.addNewInsideV().setVal(lineStyle);
    }

    // Helper to convert boolean to Y/N
    private String getYN(BrsrReportRequest.PrincipleBooleans pb, int p) {
        if (pb == null) return "N";
        switch (p) {
            case 1: return pb.isP1() ? "Y" : "N";
            case 2: return pb.isP2() ? "Y" : "N";
            case 3: return pb.isP3() ? "Y" : "N";
            case 4: return pb.isP4() ? "Y" : "N";
            case 5: return pb.isP5() ? "Y" : "N";
            case 6: return pb.isP6() ? "Y" : "N";
            case 7: return pb.isP7() ? "Y" : "N";
            case 8: return pb.isP8() ? "Y" : "N";
            case 9: return pb.isP9() ? "Y" : "N";
            default: return "N";
        }
    }

// Helper method for Vertical Cell Merging (Place this anywhere in ReportService if you don't have it yet)
             public void mergeCellsVertical(XWPFTable table, int col, int fromRow, int toRow) {
                for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
                    XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
                    if (rowIndex == fromRow) {
                        cell.getCTTc().addNewTcPr().addNewVMerge().setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge.RESTART);
                    } else {
                        cell.getCTTc().addNewTcPr().addNewVMerge().setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge.CONTINUE);
                    }
                }
            }

    // Overloaded helper for 4-column tables
    private void setColumnWidths(XWPFTable table, int w1, int w2, int w3, int w4) {
        if (table.getRow(0) != null && table.getRow(0).getTableCells().size() >= 4) {
            table.getRow(0).getCell(0).setWidth(String.valueOf(w1));
            table.getRow(0).getCell(1).setWidth(String.valueOf(w2));
            table.getRow(0).getCell(2).setWidth(String.valueOf(w3));
            table.getRow(0).getCell(3).setWidth(String.valueOf(w4));
        }
    }

    private void setColumnWidths(XWPFTable table, int w1, int w2, int w3, int w4, int w5) {
        if (table.getRow(0) != null && table.getRow(0).getTableCells().size() >= 5) {
            table.getRow(0).getCell(0).setWidth(String.valueOf(w1)); table.getRow(0).getCell(1).setWidth(String.valueOf(w2));
            table.getRow(0).getCell(2).setWidth(String.valueOf(w3)); table.getRow(0).getCell(3).setWidth(String.valueOf(w4));
            table.getRow(0).getCell(4).setWidth(String.valueOf(w5));
        }
    }
    private void setColumnWidths(XWPFTable table, int w1, int w2, int w3, int w4, int w5, int w6) {
        if (table.getRow(0) != null && table.getRow(0).getTableCells().size() >= 6) {
            table.getRow(0).getCell(0).setWidth(String.valueOf(w1)); table.getRow(0).getCell(1).setWidth(String.valueOf(w2));
            table.getRow(0).getCell(2).setWidth(String.valueOf(w3)); table.getRow(0).getCell(3).setWidth(String.valueOf(w4));
            table.getRow(0).getCell(4).setWidth(String.valueOf(w5)); table.getRow(0).getCell(5).setWidth(String.valueOf(w6));
        }
    }

    // Overloaded helper for 3-column tables
    private void setColumnWidths(XWPFTable table, int w1, int w2, int w3) {
        if (table.getRow(0) != null && table.getRow(0).getTableCells().size() >= 3) {
            table.getRow(0).getCell(0).setWidth(String.valueOf(w1));
            table.getRow(0).getCell(1).setWidth(String.valueOf(w2));
            table.getRow(0).getCell(2).setWidth(String.valueOf(w3));
        }
    }

    // Overloaded helper for 7-column tables
    private void setColumnWidths(XWPFTable table, int w1, int w2, int w3, int w4, int w5, int w6, int w7) {
        if (table.getRow(0) != null && table.getRow(0).getTableCells().size() >= 7) {
            table.getRow(0).getCell(0).setWidth(String.valueOf(w1)); table.getRow(0).getCell(1).setWidth(String.valueOf(w2));
            table.getRow(0).getCell(2).setWidth(String.valueOf(w3)); table.getRow(0).getCell(3).setWidth(String.valueOf(w4));
            table.getRow(0).getCell(4).setWidth(String.valueOf(w5)); table.getRow(0).getCell(5).setWidth(String.valueOf(w6));
            table.getRow(0).getCell(6).setWidth(String.valueOf(w7));
        }
    }


}


