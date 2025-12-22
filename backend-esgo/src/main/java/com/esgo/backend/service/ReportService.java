package com.esgo.backend.service;

import com.esgo.backend.dto.BrsrReportRequest;
import com.esgo.backend.model.Report;
import com.esgo.backend.model.User;
import com.esgo.backend.repository.ReportRepository;
import com.esgo.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String COLOR_THEME_GREEN = "548235";
    private static final String COLOR_BORDER = "CCCCCC";

    // --- DB OPERATIONS ---
    public void saveReportToDb(BrsrReportRequest request, String username) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) return;

            Report report = new Report();
            if (request.getId() != null) {
                Report existing = reportRepository.findById(request.getId()).orElse(null);
                if (existing != null && existing.getUser().getId().equals(user.getId())) {
                    report = existing;
                }
            }
            if (report.getId() == null) {
                report.setUser(user);
                report.setCreatedDate(LocalDate.now());
            }

            String cName = checkNull(request.getCompanyName());
            if(cName.equals("-")) cName = "Draft";
            report.setReportName(cName + " Report");
            report.setReportType("General Disclosures");

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            report.setReportDataJson(jsonString);

            reportRepository.save(report);

            if (!user.hasGeneratedReport()) {
                user.setHasGeneratedReport(true);
                userRepository.save(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Report> getUserReports(String username) {
        User user = userRepository.findByUsername(username);
        return reportRepository.findByUserOrderByIdDesc(user);
    }

    public BrsrReportRequest getReportDataById(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow();
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(report.getReportDataJson(), BrsrReportRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse report data");
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

            // --- SECTION A ---
            addSectionHeader(doc, "SECTION A: GENERAL DISCLOSURES");
            addBoldText(doc, "I. Details of the listed entity");

            XWPFTable table = doc.createTable();
            table.setWidth("100%");
            // Ensure header row exists
            XWPFTableRow headerRow = (table.getRow(0) != null) ? table.getRow(0) : table.createRow();
            ensureCells(headerRow, 2);

            styleCell(headerRow.getCell(0), "S. No. Particulars", true);
            styleCell(headerRow.getCell(1), "Details", true);

            // Data
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

            doc.createParagraph().createRun().addBreak();

            // --- POINT 13 ---
            String reportingBoundary = data.getReportingBoundary();
            if (reportingBoundary != null && !reportingBoundary.trim().isEmpty()) {
                XWPFParagraph pageBreakPara = doc.createParagraph();
                pageBreakPara.setPageBreak(true);

                XWPFParagraph p13 = doc.createParagraph();
                XWPFRun r13Num = p13.createRun();
                r13Num.setText("13.  ");
                r13Num.setBold(true);
                r13Num.setColor(COLOR_THEME_GREEN);
                r13Num.setFontFamily("Calibri");

                XWPFRun r13Title = p13.createRun();
                r13Title.setText("Reporting boundary:");
                r13Title.setBold(true);
                r13Title.setFontFamily("Calibri");

                XWPFParagraph p13Desc = doc.createParagraph();
                XWPFRun r13Desc = p13Desc.createRun();
                r13Desc.setText("Are the disclosures under this report made on a standalone basis (i.e., only for the entity) or on a consolidated basis (i.e., for the entity and all the entities which form a part of its consolidated financial statements, taken together).");
                r13Desc.setItalic(true);
                r13Desc.setFontFamily("Calibri");

                XWPFParagraph p13Ans = doc.createParagraph();
                p13Ans.setSpacingBefore(200);
                XWPFRun r13Ans = p13Ans.createRun();
                r13Ans.setText(reportingBoundary);
                r13Ans.setFontFamily("Calibri");
                r13Ans.setBold(true);
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION II: PRODUCTS/SERVICES =================
            addSectionHeader(doc, "II. Products/Services");

            // 14. Activities
            addBoldText(doc, "14. Details of business activities (accounting for 90% of the turnover):");
            XWPFTable table14 = doc.createTable();
            table14.setWidth("100%");
            String[] headers14 = {"S. No.", "Description of Main Activity", "Description of Business Activity", "% of Turnover"};
            addDynamicHeaderRow(table14, headers14);

            List<BrsrReportRequest.BusinessActivity> activities = data.getBusinessActivities();
            if (activities != null && !activities.isEmpty()) {
                int count = 1;
                for (BrsrReportRequest.BusinessActivity act : activities) {
                    addDynamicRow(table14, new String[]{
                            String.valueOf(count++),
                            checkNull(act.getDescriptionMain()),
                            checkNull(act.getDescriptionBusiness()),
                            checkNull(act.getTurnoverPercentage()) + "%"
                    });
                }
            } else {
                addDynamicRow(table14, new String[]{"-", "-", "-", "-"});
            }

            doc.createParagraph().createRun().addBreak();

            // 15. Products
            addBoldText(doc, "15. Products/Services sold by the entity:");
            XWPFTable table15 = doc.createTable();
            table15.setWidth("100%");
            String[] headers15;
            if (data.isIncludeConsolidated()) {
                headers15 = new String[]{"S. No.", "Product/Service", "NIC Code", "Turnover (Stand.)", "%", "Turnover (Cons.)", "%"};
            } else {
                headers15 = new String[]{"S. No.", "Product/Service", "NIC Code", "% of Total Turnover"};
            }
            addDynamicHeaderRow(table15, headers15);

            List<BrsrReportRequest.ProductService> products = data.getProductsServices();
            if (products != null && !products.isEmpty()) {
                int count = 1;
                for (BrsrReportRequest.ProductService prod : products) {
                    if (data.isIncludeConsolidated()) {
                        addDynamicRow(table15, new String[]{
                                String.valueOf(count++),
                                checkNull(prod.getProductName()),
                                checkNull(prod.getNicCode()),
                                checkNull(prod.getTurnoverStandalone()),
                                checkNull(prod.getPercentageStandalone()) + "%",
                                checkNull(prod.getTurnoverConsolidated()),
                                checkNull(prod.getPercentageConsolidated()) + "%"
                        });
                    } else {
                        addDynamicRow(table15, new String[]{
                                String.valueOf(count++),
                                checkNull(prod.getProductName()),
                                checkNull(prod.getNicCode()),
                                checkNull(prod.getPercentageStandalone()) + "%"
                        });
                    }
                }
            } else {
                int cols = data.isIncludeConsolidated() ? 7 : 4;
                String[] empty = new String[cols];
                for (int i = 0; i < cols; i++) empty[i] = "-";
                addDynamicRow(table15, empty);
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION III: OPERATIONS =================
            addSectionHeader(doc, "III. Operations");

            // 16. Locations
            addBoldText(doc, "16. Number of locations:");
            XWPFTable table16 = doc.createTable();
            table16.setWidth("100%");
            String[] headers16 = {"Location", "Number of Plants", "Number of Offices", "Total"};
            addDynamicHeaderRow(table16, headers16);

            addDynamicRow(table16, new String[]{ "National", checkNull(data.getPlantsNational()), checkNull(data.getOfficesNational()), checkNull(data.getTotalNational()) });
            addDynamicRow(table16, new String[]{ "International", checkNull(data.getPlantsInternational()), checkNull(data.getOfficesInternational()), checkNull(data.getTotalInternational()) });

            doc.createParagraph().createRun().addBreak();

            // 17. Markets
            addBoldText(doc, "17. Markets served by the entity:");
            addBoldText(doc, "a. Number of locations");
            XWPFTable table17 = doc.createTable();
            table17.setWidth("100%");

            XWPFTableRow header17 = (table17.getRow(0) != null) ? table17.getRow(0) : table17.createRow();
            ensureCells(header17, 2);
            styleCell(header17.getCell(0), "Locations", true);
            styleCell(header17.getCell(1), "Number", true);

            addRow(table17, "National (No. of States)", data.getLocationsNationalNumber());
            addRow(table17, "International (No. of Countries)", data.getLocationsInternationalNumber());

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "b. Contribution of exports:");
            XWPFParagraph p17b = doc.createParagraph();
            XWPFRun r17b = p17b.createRun();
            r17b.setText(checkNull(data.getContributionExports()));
            r17b.setFontFamily("Calibri");
            p17b.setSpacingAfter(200);

            addBoldText(doc, "c. A brief on types of customers:");
            XWPFParagraph p17c = doc.createParagraph();
            XWPFRun r17c = p17c.createRun();
            r17c.setText(checkNull(data.getTypesOfCustomers()));
            r17c.setFontFamily("Calibri");
            p17c.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION IV: EMPLOYEES =================
            XWPFParagraph pSec4 = doc.createParagraph();
            pSec4.setSpacingBefore(400);
            pSec4.setSpacingAfter(200);
            XWPFRun rSec4 = pSec4.createRun();
            rSec4.setText("IV. Employees");
            rSec4.setBold(true);
            rSec4.setColor(COLOR_THEME_GREEN);
            rSec4.setFontSize(14);
            rSec4.setFontFamily("Calibri");

            addBoldText(doc, "18. Details as at the end of Financial Year:");
            addBoldText(doc, "a. Employees and workers (including differently abled):");

            XWPFTable table18a = doc.createTable();
            table18a.setWidth("100%");

            // Updated Headers for 6 Columns (Total, Male, Female)
            String[] headers18 = {"Particulars", "Total (A)", "Male (No)", "Male (%)", "Female (No)", "Female (%)"};
            addDynamicHeaderRow(table18a, headers18);

            // 18.a Data
            addSectionTitleRow(table18a, "EMPLOYEES", 6);

            String[] empPerm = { "Permanent (D)", checkNull(data.getEmpPermTotal()), checkNull(data.getEmpPermMaleNo()), checkNull(data.getEmpPermMalePerc()), checkNull(data.getEmpPermFemaleNo()), checkNull(data.getEmpPermFemalePerc()) };
            String[] empTemp = { "Other than Permanent (E)", checkNull(data.getEmpTempTotal()), checkNull(data.getEmpTempMaleNo()), checkNull(data.getEmpTempMalePerc()), checkNull(data.getEmpTempFemaleNo()), checkNull(data.getEmpTempFemalePerc()) };
            addDynamicRow(table18a, empPerm);
            addDynamicRow(table18a, empTemp);

            String[] totalEmp = calculateTotalRow("Total Employees (D+E)", empPerm, empTemp);
            addDynamicRow(table18a, totalEmp);

            addSectionTitleRow(table18a, "WORKERS", 6);
            String[] workPerm = { "Permanent (F)", checkNull(data.getWorkPermTotal()), checkNull(data.getWorkPermMaleNo()), checkNull(data.getWorkPermMalePerc()), checkNull(data.getWorkPermFemaleNo()), checkNull(data.getWorkPermFemalePerc()) };
            String[] workTemp = { "Other than Permanent (G)", checkNull(data.getWorkTempTotal()), checkNull(data.getWorkTempMaleNo()), checkNull(data.getWorkTempMalePerc()), checkNull(data.getWorkTempFemaleNo()), checkNull(data.getWorkTempFemalePerc()) };
            addDynamicRow(table18a, workPerm);
            addDynamicRow(table18a, workTemp);

            String[] totalWork = calculateTotalRow("Total Workers (F+G)", workPerm, workTemp);
            addDynamicRow(table18a, totalWork);

            addNote(doc, data.getEmployeeNotesA());
            doc.createParagraph().createRun().addBreak();

            // 18.b Differently Abled
            addBoldText(doc, "b. Differently abled Employees and workers:");
            XWPFTable table18b = doc.createTable();
            table18b.setWidth("100%");
            addDynamicHeaderRow(table18b, headers18);

            addSectionTitleRow(table18b, "DIFFERENTLY ABLED EMPLOYEES", 6);
            addDynamicRow(table18b, new String[]{"Permanent (D)", checkNull(data.getDaEmpPermTotal()), checkNull(data.getDaEmpPermMaleNo()), checkNull(data.getDaEmpPermMalePerc()), checkNull(data.getDaEmpPermFemaleNo()), checkNull(data.getDaEmpPermFemalePerc())});
            addDynamicRow(table18b, new String[]{"Other than Permanent (E)", checkNull(data.getDaEmpTempTotal()), checkNull(data.getDaEmpTempMaleNo()), checkNull(data.getDaEmpTempMalePerc()), checkNull(data.getDaEmpTempFemaleNo()), checkNull(data.getDaEmpTempFemalePerc())});

            addSectionTitleRow(table18b, "DIFFERENTLY ABLED WORKERS", 6);
            addDynamicRow(table18b, new String[]{"Permanent (F)", checkNull(data.getDaWorkPermTotal()), checkNull(data.getDaWorkPermMaleNo()), checkNull(data.getDaWorkPermMalePerc()), checkNull(data.getDaWorkPermFemaleNo()), checkNull(data.getDaWorkPermFemalePerc())});
            addDynamicRow(table18b, new String[]{"Other than Permanent (G)", checkNull(data.getDaWorkTempTotal()), checkNull(data.getDaWorkTempMaleNo()), checkNull(data.getDaWorkTempMalePerc()), checkNull(data.getDaWorkTempFemaleNo()), checkNull(data.getDaWorkTempFemalePerc())});

            addNote(doc, data.getEmployeeNotesB());

            doc.createParagraph().createRun().addBreak();

            // --- 19. Women Representation ---
            addBoldText(doc, "19. Participation/Inclusion/Representation of women:");

            XWPFTable table19 = doc.createTable();
            table19.setWidth("100%");

            // Custom Header Structure for this specific table
            // Row 0: Headers
            XWPFTableRow header19 = table19.getRow(0);
            if(header19 == null) header19 = table19.createRow();
            ensureCells(header19, 4);

            styleCell(header19.getCell(0), "Category", true);
            styleCell(header19.getCell(1), "Total (A)", true);
            styleCell(header19.getCell(2), "No. of Females (B)", true);
            styleCell(header19.getCell(3), "% (B / A)", true);

            // Data Rows
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

            // Notes for Q19
            addNote(doc, data.getWomenRepresentationNotes());

            doc.createParagraph().createRun().addBreak();

            // --- 20. Turnover Rate ---
            addBoldText(doc, "20. Turnover rate for permanent employees and workers (Disclose trends for the past 3 years):");
            XWPFTable table20 = doc.createTable();
            table20.setWidth("100%");

            // H1 - User Defined Years
            // --- HEADER ROW 1 (Years) ---
            // Structure: [Blank] | [Current FY (span 3)] | [Previous FY (span 3)] | [Prior FY (span 3)]
            XWPFTableRow hRow1 = table20.getRow(0);
            ensureCells(hRow1, 10);

            styleCell(hRow1.getCell(0), "Category", true);

            // Use user input, default to generic if empty
            String fyCurrLabel = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "FY (Current)";
            String fyPrevLabel = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "FY (Previous)";
            String fyPriorLabel = (data.getFyPrior() != null && !data.getFyPrior().isEmpty()) ? data.getFyPrior() : "FY (Prior)";

            styleCell(hRow1.getCell(1), fyCurrLabel, true);
            mergeCellsHorizontal(hRow1, 1, 3);

            styleCell(hRow1.getCell(4), fyPrevLabel, true);
            mergeCellsHorizontal(hRow1, 4, 6);

            styleCell(hRow1.getCell(7), fyPriorLabel, true);
            mergeCellsHorizontal(hRow1, 7, 9);

            // --- HEADER ROW 2 (Male/Female/Total) ---
            XWPFTableRow hRow2 = table20.createRow();
            ensureCells(hRow2, 10);
            styleCell(hRow2.getCell(0), "", true); // Empty under "Category"

            // Repeat M/F/T three times
            for(int i=0; i<3; i++) {
                int base = 1 + (i*3);
                styleCell(hRow2.getCell(base), "Male", true);
                styleCell(hRow2.getCell(base+1), "Female", true);
                styleCell(hRow2.getCell(base+2), "Total", true);
            }

            // --- DATA ROW 1: Permanent Employees ---
            String[] rowEmp = new String[10];
            rowEmp[0] = "Permanent Employees";
            // Current
            rowEmp[1] = checkNull(data.getTurnoverEmpCurrMale());
            rowEmp[2] = checkNull(data.getTurnoverEmpCurrFemale());
            rowEmp[3] = checkNull(data.getTurnoverEmpCurrTotal());
            // Previous
            rowEmp[4] = checkNull(data.getTurnoverEmpPrevMale());
            rowEmp[5] = checkNull(data.getTurnoverEmpPrevFemale());
            rowEmp[6] = checkNull(data.getTurnoverEmpPrevTotal());
            // Prior
            rowEmp[7] = checkNull(data.getTurnoverEmpPriorMale());
            rowEmp[8] = checkNull(data.getTurnoverEmpPriorFemale());
            rowEmp[9] = checkNull(data.getTurnoverEmpPriorTotal());

            addDynamicRow(table20, rowEmp);

            // --- DATA ROW 2: Permanent Workers ---
            String[] rowWork = new String[10];
            rowWork[0] = "Permanent Workers";
            // Current
            rowWork[1] = checkNull(data.getTurnoverWorkCurrMale());
            rowWork[2] = checkNull(data.getTurnoverWorkCurrFemale());
            rowWork[3] = checkNull(data.getTurnoverWorkCurrTotal());
            // Previous
            rowWork[4] = checkNull(data.getTurnoverWorkPrevMale());
            rowWork[5] = checkNull(data.getTurnoverWorkPrevFemale());
            rowWork[6] = checkNull(data.getTurnoverWorkPrevTotal());
            // Prior
            rowWork[7] = checkNull(data.getTurnoverWorkPriorMale());
            rowWork[8] = checkNull(data.getTurnoverWorkPriorFemale());
            rowWork[9] = checkNull(data.getTurnoverWorkPriorTotal());

            addDynamicRow(table20, rowWork);

            // Notes
            addNote(doc, data.getTurnoverNotes());

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

    private String checkNull(String val) {
        return (val == null || val.trim().isEmpty()) ? "-" : val;
    }

    // SAFE CALCULATION
    private String[] calculateTotalRow(String label, String[] row1, String[] row2) {
        if (row1 == null || row2 == null || row1.length < 6 || row2.length < 6) {
            return new String[]{label, "-", "-", "-", "-", "-"};
        }

        // Index mapping: 1=Total, 2=MaleNo, 4=FemNo
        double totalA = parseSafe(row1[1]) + parseSafe(row2[1]);
        double maleNo = parseSafe(row1[2]) + parseSafe(row2[2]);
        double femNo = parseSafe(row1[4]) + parseSafe(row2[4]);

        String malePerc = totalA > 0 ? String.format("%.1f", (maleNo / totalA) * 100) : "0.0";
        String femPerc = totalA > 0 ? String.format("%.1f", (femNo / totalA) * 100) : "0.0";

        return new String[]{
                label,
                String.valueOf((int)totalA),
                String.valueOf((int)maleNo),
                malePerc,
                String.valueOf((int)femNo),
                femPerc
        };
    }

    private double parseSafe(String val) {
        if (val == null) return 0.0;
        String cleanVal = val.trim().replaceAll("[^0-9.]", "");
        if (cleanVal.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(cleanVal);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void addNote(XWPFDocument doc, String noteContent) {
        if(noteContent != null && !noteContent.trim().isEmpty()) {
            XWPFParagraph p = doc.createParagraph();
            p.setSpacingBefore(100);
            XWPFRun rLabel = p.createRun();
            rLabel.setText("Note: ");
            rLabel.setBold(true);
            rLabel.setFontSize(10);
            rLabel.setFontFamily("Calibri");
            XWPFRun rContent = p.createRun();
            rContent.setText(noteContent);
            rContent.setItalic(true);
            rContent.setFontSize(10);
            rContent.setFontFamily("Calibri");
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

    private void addDynamicHeaderRow(XWPFTable table, String[] headers) {
        XWPFTableRow row = (table.getRow(0) != null) ? table.getRow(0) : table.createRow();
        ensureCells(row, headers.length);
        for (int i = 0; i < headers.length; i++) styleCell(row.getCell(i), headers[i], true);
    }

    private void addDynamicRow(XWPFTable table, String[] values) {
        XWPFTableRow row = table.createRow();
        ensureCells(row, values.length);
        for (int i = 0; i < values.length; i++) styleCell(row.getCell(i), values[i], false);
    }

    private void addSectionTitleRow(XWPFTable table, String title, int cols) {
        XWPFTableRow row = table.createRow();
        ensureCells(row, cols);

        for(int i=0; i<cols; i++) {
            row.getCell(i).setColor("F2F2F2");
        }
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

    // --- FIX: The Missing Helper ---
    private void ensureCells(XWPFTableRow row, int count) {
        while (row.getTableCells().size() < count) {
            row.addNewTableCell();
        }
    }

    private void styleCell(XWPFTableCell cell, String text, boolean isHeader) {
        if (cell == null) return; // Crash prevention

        if(cell.getParagraphs().size() > 0) cell.removeParagraph(0);
        XWPFParagraph p = cell.addParagraph();
        XWPFRun r = p.createRun();
        r.setText(text);
        r.setFontFamily("Calibri");
        r.setFontSize(10);

        if(isHeader) {
            cell.setColor("E7E7E7");
            r.setBold(true);
            r.setColor(COLOR_THEME_GREEN);
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

    // --- HELPER FOR MERGING CELLS ---
    private void mergeCellsHorizontal(XWPFTableRow row, int startCol, int endCol) {
        for (int i = startCol; i <= endCol; i++) {
            XWPFTableCell cell = row.getCell(i);
            if (i == startCol) {
                // The first cell starts the restart
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // Subsequent cells continue the merge
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }
}