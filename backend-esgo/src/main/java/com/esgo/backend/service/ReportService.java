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

            // ... (Rest of your generation logic remains exactly the same as before) ...
            // ... I am abbreviating here to save space, but DO NOT DELETE the rest of the file ...

            // --- COPY PASTE THE REST OF THE GENERATE METHOD FROM PREVIOUS STEPS ---

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
            XWPFTableRow headerRow = (table.getRow(0) != null) ? table.getRow(0) : table.createRow();
            ensureCells(headerRow, 2);
            styleCell(headerRow.getCell(0), "S. No. Particulars", true);
            styleCell(headerRow.getCell(1), "Details", true);

            // Data Rows
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
            String[] headers15 = data.isIncludeConsolidated() ?
                    new String[]{"S. No.", "Product/Service", "NIC Code", "Turnover (Stand.)", "%", "Turnover (Cons.)", "%"} :
                    new String[]{"S. No.", "Product/Service", "NIC Code", "% of Total Turnover"};
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
            addSectionHeader(doc, "IV. Employees");

            addBoldText(doc, "18. Details as at the end of Financial Year:");
            addBoldText(doc, "a. Employees and workers (including differently abled):");

            XWPFTable table18a = doc.createTable();
            table18a.setWidth("100%");
            String[] headers18 = {"Particulars", "Total (A)", "Male (No)", "Male (%)", "Female (No)", "Female (%)"};
            addDynamicHeaderRow(table18a, headers18);

            addSectionTitleRow(table18a, "EMPLOYEES", 6);
            String[] empPerm = { "Permanent (D)", checkNull(data.getEmpPermTotal()), checkNull(data.getEmpPermMaleNo()), checkNull(data.getEmpPermMalePerc()), checkNull(data.getEmpPermFemaleNo()), checkNull(data.getEmpPermFemalePerc()) };
            String[] empTemp = { "Other than Permanent (E)", checkNull(data.getEmpTempTotal()), checkNull(data.getEmpTempMaleNo()), checkNull(data.getEmpTempMalePerc()), checkNull(data.getEmpTempFemaleNo()), checkNull(data.getEmpTempFemalePerc()) };
            addDynamicRow(table18a, empPerm);
            addDynamicRow(table18a, empTemp);
            addDynamicRow(table18a, calculateTotalRow("Total Employees (D+E)", empPerm, empTemp));

            addSectionTitleRow(table18a, "WORKERS", 6);
            String[] workPerm = { "Permanent (F)", checkNull(data.getWorkPermTotal()), checkNull(data.getWorkPermMaleNo()), checkNull(data.getWorkPermMalePerc()), checkNull(data.getWorkPermFemaleNo()), checkNull(data.getWorkPermFemalePerc()) };
            String[] workTemp = { "Other than Permanent (G)", checkNull(data.getWorkTempTotal()), checkNull(data.getWorkTempMaleNo()), checkNull(data.getWorkTempMalePerc()), checkNull(data.getWorkTempFemaleNo()), checkNull(data.getWorkTempFemalePerc()) };
            addDynamicRow(table18a, workPerm);
            addDynamicRow(table18a, workTemp);
            addDynamicRow(table18a, calculateTotalRow("Total Workers (F+G)", workPerm, workTemp));
            addNote(doc, data.getEmployeeNotesA());

            doc.createParagraph().createRun().addBreak();

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
            XWPFTableRow header19 = (table19.getRow(0) != null) ? table19.getRow(0) : table19.createRow();
            ensureCells(header19, 4);
            styleCell(header19.getCell(0), "Category", true);
            styleCell(header19.getCell(1), "Total (A)", true);
            styleCell(header19.getCell(2), "No. of Females (B)", true);
            styleCell(header19.getCell(3), "% (B / A)", true);

            List<BrsrReportRequest.WomenRepresentation> womenList = data.getWomenRepresentationList();
            if (womenList != null && !womenList.isEmpty()) {
                for (BrsrReportRequest.WomenRepresentation row : womenList) {
                    addDynamicRow(table19, new String[]{ checkNull(row.getCategory()), checkNull(row.getTotalA()), checkNull(row.getFemaleNoB()), checkNull(row.getFemalePerc()) + "%" });
                }
            } else {
                addDynamicRow(table19, new String[]{"-", "-", "-", "-"});
            }
            addNote(doc, data.getWomenRepresentationNotes());

            doc.createParagraph().createRun().addBreak();

            // --- 20. TURNOVER RATE ---
            addBoldText(doc, "20. Turnover rate for permanent employees and workers (Disclose trends for the past 3 years):");
            XWPFTable table20 = doc.createTable();
            table20.setWidth("100%");

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
            styleCell(hRow2.getCell(0), "", true);
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
            addNote(doc, data.getTurnoverNotes());

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION V =================
            addSectionHeader(doc, "V. Holding, Subsidiary and Associate Companies");
            addBoldText(doc, "21. (a) Names of holding / subsidiary / associate companies / joint ventures:");

            List<BrsrReportRequest.HoldingCompany> holdingList = data.getHoldingCompanies();
            if (holdingList != null && !holdingList.isEmpty()) {
                XWPFTable table21 = doc.createTable();
                table21.setWidth("100%");
                String[] headers21 = {"S. No.", "Name of Company (A)", "Type", "% Shares", "Participates in BR?"};
                addDynamicHeaderRow(table21, headers21);

                int count = 1;
                for (BrsrReportRequest.HoldingCompany company : holdingList) {
                    addDynamicRow(table21, new String[]{
                            String.valueOf(count++),
                            checkNull(company.getName()),
                            checkNull(company.getType()),
                            checkNull(company.getSharesHeld()) + "%",
                            checkNull(company.getParticipateBusinessResponsibility())
                    });
                }
            } else {
                String note = data.getHoldingCompanyNote();
                if (note != null && !note.trim().isEmpty()) {
                    XWPFParagraph pNote = doc.createParagraph();
                    pNote.createRun().setText(note);
                } else {
                    XWPFParagraph pNone = doc.createParagraph();
                    pNone.createRun().setText("Not Applicable / No Data Provided");
                }
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
            styleCell(r1.getCell(0), "22. i)", false);
            styleCell(r1.getCell(1), "Whether CSR is applicable as per section 135 of Companies Act, 2013?", false);
            styleCell(r1.getCell(2), checkNull(data.getCsrApplicable()), false);

            XWPFTableRow r2 = table22.createRow();
            styleCell(r2.getCell(0), "22. ii)", false);
            styleCell(r2.getCell(1), "Turnover (in Rs.)", false);
            styleCell(r2.getCell(2), checkNull(data.getCsrTurnover()), false);

            XWPFTableRow r3 = table22.createRow();
            styleCell(r3.getCell(0), "22. iii)", false);
            styleCell(r3.getCell(1), "Net worth (in Rs.)", false);
            styleCell(r3.getCell(2), checkNull(data.getCsrNetWorth()), false);

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION VII: TRANSPARENCY =================
            addSectionHeader(doc, "VII. Transparency and Disclosures Compliances");
            addBoldText(doc, "23. Complaints/Grievances on any of the principles (Principles 1 to 9):");

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

            // --- 24. Material Issues ---
            addBoldText(doc, "24. Overview of the entity's material responsible business conduct issues");

            XWPFParagraph p24Desc = doc.createParagraph();
            XWPFRun r24Desc = p24Desc.createRun();
            r24Desc.setText("Please indicate material responsible business conduct and sustainability issues pertaining to environmental and social matters that present a risk or an opportunity to your business, rationale for identifying the same, approach to adapt or mitigate the risk along-with its financial implications, as per the following format:");
            r24Desc.setFontFamily("Calibri");
            r24Desc.setFontSize(10);
            p24Desc.setSpacingAfter(200);

            addNote(doc, data.getMaterialIssuesNote());
            doc.createParagraph().createRun().addBreak();

            XWPFTable table24 = doc.createTable();
            table24.setWidth("100%");

            CTTblGrid grid24 = table24.getCTTbl().addNewTblGrid();
            grid24.addNewGridCol().setW(BigInteger.valueOf(400));
            grid24.addNewGridCol().setW(BigInteger.valueOf(1400));
            grid24.addNewGridCol().setW(BigInteger.valueOf(800));
            grid24.addNewGridCol().setW(BigInteger.valueOf(3200));
            grid24.addNewGridCol().setW(BigInteger.valueOf(3200));
            grid24.addNewGridCol().setW(BigInteger.valueOf(1000));

            XWPFTableRow hRow24 = table24.getRow(0);
            ensureCells(hRow24, 6);
            styleCell(hRow24.getCell(0), "S. No.", true);
            styleCell(hRow24.getCell(1), "Material issue identified", true);
            styleCell(hRow24.getCell(2), "Indicate whether risk or opportunity (R/O)", true);
            styleCell(hRow24.getCell(3), "Rationale for identifying the risk / opportunity", true);
            styleCell(hRow24.getCell(4), "In case of risk, approach to adapt or mitigate", true);
            styleCell(hRow24.getCell(5), "Financial implications (Positive/Negative)", true);

            List<BrsrReportRequest.MaterialIssue> issues = data.getMaterialIssues();
            if (issues != null && !issues.isEmpty()) {
                int count = 1;
                for (BrsrReportRequest.MaterialIssue issue : issues) {
                    XWPFTableRow row = table24.createRow();
                    ensureCells(row, 6);
                    styleCell(row.getCell(0), String.valueOf(count++), false);
                    styleCell(row.getCell(1), checkNull(issue.getDescription()), false);
                    styleCell(row.getCell(2), checkNull(issue.getRiskOrOpportunity()), false);
                    fillCellWithNewlines(row.getCell(3), checkNull(issue.getRationale()));
                    fillCellWithNewlines(row.getCell(4), checkNull(issue.getApproach()));
                    styleCell(row.getCell(5), checkNull(issue.getFinancialImplications()), false);
                }
            } else {
                addDynamicRow(table24, new String[]{"-", "-", "-", "-", "-", "-"});
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION B: MANAGEMENT =================
            // --- SECTION B HEADER ---
            XWPFParagraph pSecB = doc.createParagraph();
            pSecB.setPageBreak(true);
            XWPFRun rSecB = pSecB.createRun();
            rSecB.setText("SECTION B: MANAGEMENT AND PROCESS DISCLOSURES");
            rSecB.setBold(true);
            rSecB.setFontSize(14);
            rSecB.setColor("108a55");

// --- Q1: Policy Matrix ---
            addBoldText(doc, "1. Policy and management processes");
            XWPFTable tableQ1 = doc.createTable();
            tableQ1.setWidth("100%");
            XWPFTableRow hQ1 = tableQ1.getRow(0);
            ensureCells(hQ1, 12);
            styleCell(hQ1.getCell(0), "Policy Name", true);
            for(int k=1; k<=9; k++) styleCell(hQ1.getCell(k), "P"+k, true);
            styleCell(hQ1.getCell(10), "Board Appr", true);
            styleCell(hQ1.getCell(11), "Web Link", true);

            if(data.getQ1Policies() != null) {
                for(BrsrReportRequest.PolicyMapping pm : data.getQ1Policies()) {
                    XWPFTableRow r = tableQ1.createRow();
                    ensureCells(r, 12);
                    styleCell(r.getCell(0), checkNull(pm.getName()), false);
                    styleCell(r.getCell(1), pm.isP1()?"Y":"", false);
                    styleCell(r.getCell(2), pm.isP2()?"Y":"", false);
                    styleCell(r.getCell(3), pm.isP3()?"Y":"", false);
                    styleCell(r.getCell(4), pm.isP4()?"Y":"", false);
                    styleCell(r.getCell(5), pm.isP5()?"Y":"", false);
                    styleCell(r.getCell(6), pm.isP6()?"Y":"", false);
                    styleCell(r.getCell(7), pm.isP7()?"Y":"", false);
                    styleCell(r.getCell(8), pm.isP8()?"Y":"", false);
                    styleCell(r.getCell(9), pm.isP9()?"Y":"", false);
                    styleCell(r.getCell(10), checkNull(pm.getBoardApproved()), false);
                    styleCell(r.getCell(11), checkNull(pm.getWebLink()), false);
                }
            }
            doc.createParagraph().createRun().addBreak();

// --- Q2: Procedures ---
            addBoldText(doc, "2. Whether the entity has translated the policy into procedures. (Yes/No)");
            XWPFParagraph pQ2 = doc.createParagraph();
// Handle line breaks from textarea
            String[] q2Lines = checkNull(data.getQ2Procedures()).split("\n");
            for(String line : q2Lines) {
                XWPFRun r = pQ2.createRun();
                r.setText(line);
                r.addBreak();
            }

// --- Q3: Value Chain ---
            addBoldText(doc, "3. Do the enlisted policies extend to your value chain partners? (Yes/No)");
            XWPFParagraph pQ3 = doc.createParagraph();
            pQ3.createRun().setText(checkNull(data.getQ3ValueChain()));
            doc.createParagraph().createRun().addBreak();

// --- Q4: Standards Matrix ---
            addBoldText(doc, "4. Name of national and international standards adopted");
            XWPFTable tableQ4 = doc.createTable();
            tableQ4.setWidth("100%");
            XWPFTableRow hQ4 = tableQ4.getRow(0);
            ensureCells(hQ4, 10);
            styleCell(hQ4.getCell(0), "Standard Name", true);
            for(int k=1; k<=9; k++) styleCell(hQ4.getCell(k), "P"+k, true);

            if(data.getQ4Standards() != null) {
                for(BrsrReportRequest.StandardMapping sm : data.getQ4Standards()) {
                    XWPFTableRow r = tableQ4.createRow();
                    ensureCells(r, 10);
                    styleCell(r.getCell(0), checkNull(sm.getName()), false);
                    styleCell(r.getCell(1), sm.isP1()?"Y":"", false);
                    styleCell(r.getCell(2), sm.isP2()?"Y":"", false);
                    styleCell(r.getCell(3), sm.isP3()?"Y":"", false);
                    styleCell(r.getCell(4), sm.isP4()?"Y":"", false);
                    styleCell(r.getCell(5), sm.isP5()?"Y":"", false);
                    styleCell(r.getCell(6), sm.isP6()?"Y":"", false);
                    styleCell(r.getCell(7), sm.isP7()?"Y":"", false);
                    styleCell(r.getCell(8), sm.isP8()?"Y":"", false);
                    styleCell(r.getCell(9), sm.isP9()?"Y":"", false);
                }
            }
            doc.createParagraph().createRun().addBreak();

// --- Q5: Commitments ---
            addBoldText(doc, "5. Specific commitments, goals and targets set by the entity");
            XWPFParagraph pQ5 = doc.createParagraph();
            String[] q5Lines = checkNull(data.getQ5Commitments()).split("\n");
            for(String line : q5Lines) {
                XWPFRun r = pQ5.createRun();
                r.setText(line);
                r.addBreak();
            }

// --- Q6: Performance ---
            addBoldText(doc, "6. Performance of the entity against the specific commitments");
            XWPFParagraph pQ6 = doc.createParagraph();
            pQ6.createRun().setText(checkNull(data.getQ6Performance()));

            // --- Q9 ---
            addBoldText(doc, "9. Specified Committee of the Board/Director responsible for decision making");
            XWPFParagraph pQ9 = doc.createParagraph();
            setTextWithBreaks(pQ9.createRun(), checkNull(data.getQ9Committee()));

// --- Q10: Review Details (Table) ---
            addBoldText(doc, "10. Details of Review of NGRBCs by the Company");
            XWPFTable tableQ10 = doc.createTable();
            tableQ10.setWidth("100%");
            XWPFTableRow hQ10 = tableQ10.getRow(0);
            ensureCells(hQ10, 2);
            styleCell(hQ10.getCell(0), "Subject for Review", true);
            styleCell(hQ10.getCell(1), "Reviewer & Frequency", true);

            XWPFTableRow r10_1 = tableQ10.createRow();
            ensureCells(r10_1, 2);
            styleCell(r10_1.getCell(0), "Performance against above policies", false);
            styleCell(r10_1.getCell(1), checkNull(data.getQ10PerformanceReview()), false);

            XWPFTableRow r10_2 = tableQ10.createRow();
            ensureCells(r10_2, 2);
            styleCell(r10_2.getCell(0), "Compliance with statutory requirements", false);
            styleCell(r10_2.getCell(1), checkNull(data.getQ10ComplianceReview()), false);
            doc.createParagraph().createRun().addBreak();

// --- Q11: Assessment ---
            addBoldText(doc, "11. Has the entity carried out independent assessment/ evaluation?");
            XWPFParagraph pQ11 = doc.createParagraph();
            setTextWithBreaks(pQ11.createRun(), checkNull(data.getQ11Assessment()));

// --- Q12: Reasons for No ---
            addBoldText(doc, "12. If answer to question (1) above is “No”, reasons to be stated:");
            XWPFTable tableQ12 = doc.createTable();
            tableQ12.setWidth("100%");

            XWPFTableRow hQ12 = tableQ12.getRow(0);
            ensureCells(hQ12, 10);
            styleCell(hQ12.getCell(0), "Reason", true);
            for(int k=1; k<=9; k++) styleCell(hQ12.getCell(k), "P"+k, true);

            if(data.getQ12Reasons() != null) {
                for(BrsrReportRequest.ReasonMapping rm : data.getQ12Reasons()) {
                    XWPFTableRow r = tableQ12.createRow();
                    ensureCells(r, 10);
                    styleCell(r.getCell(0), checkNull(rm.getQuestionText()), false);
                    styleCell(r.getCell(1), rm.isP1()?"Y":"", false);
                    styleCell(r.getCell(2), rm.isP2()?"Y":"", false);
                    styleCell(r.getCell(3), rm.isP3()?"Y":"", false);
                    styleCell(r.getCell(4), rm.isP4()?"Y":"", false);
                    styleCell(r.getCell(5), rm.isP5()?"Y":"", false);
                    styleCell(r.getCell(6), rm.isP6()?"Y":"", false);
                    styleCell(r.getCell(7), rm.isP7()?"Y":"", false);
                    styleCell(r.getCell(8), rm.isP8()?"Y":"", false);
                    styleCell(r.getCell(9), rm.isP9()?"Y":"", false);
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
            styleCell(hRowTrain.getCell(1), "Total number of training programmes held", true);
            styleCell(hRowTrain.getCell(2), "Topics / principles covered under the training and its impact", true);
            styleCell(hRowTrain.getCell(3), "%age of persons covered", true);

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
                    "Conflict of Interest of Directors",
                    checkNull(data.getCoiDirectorsCurrNum()), checkNull(data.getCoiDirectorsCurrRem()),
                    checkNull(data.getCoiDirectorsPrevNum()), checkNull(data.getCoiDirectorsPrevRem())
            });
            addDynamicRow(table6, new String[]{
                    "Conflict of Interest of KMPs",
                    checkNull(data.getCoiKmpsCurrNum()), checkNull(data.getCoiKmpsCurrRem()),
                    checkNull(data.getCoiKmpsPrevNum()), checkNull(data.getCoiKmpsPrevRem())
            });

            doc.createParagraph().createRun().addBreak();

            // --- 7. CORRECTIVE ACTIONS ---
            addBoldText(doc, "7. Provide details of any corrective action taken or underway on issues related to fines / penalties / action taken by regulators/ law enforcement agencies/ judicial institutions, on cases of corruption and conflicts of interest.");
            XWPFParagraph p7 = doc.createParagraph();
            setTextWithBreaks(p7.createRun(), checkNull(data.getCorrectiveActionDetails()));

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
            pP2.setSpacingBefore(300);
            XWPFRun rP2 = pP2.createRun();
            rP2.setText("PRINCIPLE 2: Businesses should provide goods and services in a manner that is sustainable and safe.");
            rP2.setBold(true);
            rP2.setFontSize(12);
            rP2.setColor(COLOR_THEME_GREEN);
            rP2.setFontFamily("Calibri");

            addBoldText(doc, "Essential Indicators");
            addBoldText(doc, "1. Percentage of R&D and capital expenditure (capex) investments in specific technologies to improve the environmental and social impacts of product and processes to total R&D and capex investments made by the entity, respectively.");

            // --- TABLE FOR R&D / CAPEX ---
            XWPFTable tableP2 = doc.createTable();
            tableP2.setWidth("100%");

            CTTblGrid gridP2 = tableP2.getCTTbl().addNewTblGrid();
            gridP2.addNewGridCol().setW(BigInteger.valueOf(1000));
            gridP2.addNewGridCol().setW(BigInteger.valueOf(1500));
            gridP2.addNewGridCol().setW(BigInteger.valueOf(1500));
            gridP2.addNewGridCol().setW(BigInteger.valueOf(5000));

            // Header
            XWPFTableRow hRowP2 = tableP2.getRow(0);
            ensureCells(hRowP2, 4);
            styleCell(hRowP2.getCell(0), "", true);

            // DYNAMIC HEADERS
            String fyCurP2 = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "Current FY";
            String fyPrevP2 = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowP2.getCell(1), fyCurP2, true);
            styleCell(hRowP2.getCell(2), fyPrevP2, true);
            styleCell(hRowP2.getCell(3), "Details of improvements in environmental and social impacts", true);

            // Row 1: R&D
            XWPFTableRow rRd = tableP2.createRow();
            ensureCells(rRd, 4);
            styleCell(rRd.getCell(0), "R&D", false);
            styleCell(rRd.getCell(1), checkNull(data.getRdCurrentYear()), false);
            styleCell(rRd.getCell(2), checkNull(data.getRdPreviousYear()), false);
            fillCellWithNewlines(rRd.getCell(3), checkNull(data.getRdDetails())); // This will now be justified

            // Row 2: Capex
            XWPFTableRow rCap = tableP2.createRow();
            ensureCells(rCap, 4);
            styleCell(rCap.getCell(0), "Capex", false);
            styleCell(rCap.getCell(1), checkNull(data.getCapexCurrentYear()), false);
            styleCell(rCap.getCell(2), checkNull(data.getCapexPreviousYear()), false);
            fillCellWithNewlines(rCap.getCell(3), checkNull(data.getCapexDetails())); // This will now be justified

            // --- NEW: Add the Note here ---
            addNote(doc, data.getPrinciple2Note());
            // ------------------------------

            doc.createParagraph().createRun().addBreak();

            // --- 2. Sustainable Sourcing ---
            addBoldText(doc, "2. a. Does the entity have procedures in place for sustainable sourcing? (Yes/No)");

            // 2.a Answer + Details
            XWPFParagraph pSrc = doc.createParagraph();
            XWPFRun rSrc = pSrc.createRun();
            rSrc.setFontFamily("Calibri");
            rSrc.setFontSize(10);

            String procStatus = checkNull(data.getSustainableSourcingProcedures());
            // Format: "Yes, [Details]"
            String procDetails = procStatus;
            String detailText = data.getSustainableSourcingDetails();

            if (detailText != null && !detailText.trim().isEmpty()) {
                procDetails += ", " + detailText;
            }
            setTextWithBreaks(rSrc, procDetails); // Use helper for newlines
            pSrc.setSpacingAfter(200);

            // 2.b Percentage
            addBoldText(doc, "b. If yes, what percentage of inputs were sourced sustainably?");

            XWPFParagraph pPerc = doc.createParagraph();
            XWPFRun rPerc = pPerc.createRun();
            rPerc.setFontFamily("Calibri");
            rPerc.setFontSize(10);

            String percVal = checkNull(data.getSustainableSourcingPercentage());
            String percRem = checkNull(data.getSustainableSourcingRemarks());

            // Format: "100%. [Remarks]"
            setTextWithBreaks(rPerc, percVal + "% " + percRem);
            pPerc.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 3. RECLAIM PROCESSES ---
            addBoldText(doc, "3. Describe the processes in place to safely reclaim your products for reusing, recycling and disposing at the end of life, for (a) Plastics (including packaging) (b) E-waste (c) Hazardous waste and (d) other waste.");

            XWPFParagraph pReclaim = doc.createParagraph();
            XWPFRun rReclaim = pReclaim.createRun();
            rReclaim.setFontFamily("Calibri");
            rReclaim.setFontSize(10);

            // Use helper to respect newlines from textarea
            setTextWithBreaks(rReclaim, checkNull(data.getReclaimProcessDetails()));
            pReclaim.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 4. EPR DETAILS ---
            addBoldText(doc, "4. Whether Extended Producer Responsibility (EPR) is applicable to the entity's activities (Yes / No). If yes, whether the waste collection plan is in line with the Extended Producer Responsibility (EPR) plan submitted to Pollution Control Boards? If not, provide steps taken to address the same.");

            XWPFParagraph pEpr = doc.createParagraph();
            XWPFRun rEpr = pEpr.createRun();
            rEpr.setFontFamily("Calibri");
            rEpr.setFontSize(10);

            // Use helper to respect newlines for long descriptions
            setTextWithBreaks(rEpr, checkNull(data.getEprDetails()));
            pEpr.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP INDICATORS ---
            addSectionHeader(doc, "Leadership Indicators");

            // 1. LCA Question
            addBoldText(doc, "1. Has the entity conducted Life Cycle Perspective / Assessments (LCA) for any of its products (for manufacturing industry) or for its services (for service industry)? If yes, provide details in the following format?");

            // Note (After Heading, Before Table)
            String lcaNote = data.getLcaNote();
            if (lcaNote != null && !lcaNote.trim().isEmpty()) {
                XWPFParagraph pNote = doc.createParagraph();
                pNote.setSpacingAfter(200);
                setTextWithBreaks(pNote.createRun(), lcaNote);
            }

            // --- LCA TABLE ---
            XWPFTable tableLca = doc.createTable();
            tableLca.setWidth("100%");

            // Define 6 Columns
            CTTblGrid gridLca = tableLca.getCTTbl().addNewTblGrid();
            gridLca.addNewGridCol().setW(BigInteger.valueOf(800));  // NIC
            gridLca.addNewGridCol().setW(BigInteger.valueOf(2000)); // Name
            gridLca.addNewGridCol().setW(BigInteger.valueOf(1000)); // % Turnover
            gridLca.addNewGridCol().setW(BigInteger.valueOf(2000)); // Boundary
            gridLca.addNewGridCol().setW(BigInteger.valueOf(1200)); // Agency
            gridLca.addNewGridCol().setW(BigInteger.valueOf(2500)); // Results/Link

            // Header
            XWPFTableRow hRowLca = tableLca.getRow(0);
            ensureCells(hRowLca, 6);
            styleCell(hRowLca.getCell(0), "NIC Code", true);
            styleCell(hRowLca.getCell(1), "Name of Product /Service", true);
            styleCell(hRowLca.getCell(2), "% of total Turnover contributed", true);
            styleCell(hRowLca.getCell(3), "Boundary for which the Life Cycle Perspective / Assessment was conducted", true);
            styleCell(hRowLca.getCell(4), "Whether conducted by independent external agency (Yes/No)", true);
            styleCell(hRowLca.getCell(5), "Results communicated in public domain (Yes/No) If yes, provide the web-link.", true);

            // Data Rows
            List<BrsrReportRequest.LcaEntry> lcaList = data.getLcaEntries();
            if (lcaList != null && !lcaList.isEmpty()) {
                for (BrsrReportRequest.LcaEntry lca : lcaList) {
                    XWPFTableRow row = tableLca.createRow();
                    ensureCells(row, 6);

                    styleCell(row.getCell(0), checkNull(lca.getNicCode()), false);
                    styleCell(row.getCell(1), checkNull(lca.getProductName()), false);
                    styleCell(row.getCell(2), checkNull(lca.getTurnoverPercentage()) + "%", false);
                    fillCellWithNewlines(row.getCell(3), checkNull(lca.getBoundary())); // Allow multiline for boundary
                    styleCell(row.getCell(4), checkNull(lca.getIndependentAgency()), false);
                    fillCellWithNewlines(row.getCell(5), checkNull(lca.getPublicDomainResult())); // Allow multiline for links
                }
            } else {
                addDynamicRow(tableLca, new String[]{"-", "-", "-", "-", "-", "-"});
            }

            doc.createParagraph().createRun().addBreak();

            // --- 2. LCA RISKS ---
            addBoldText(doc, "2. If there are any significant social or environmental concerns and/or risks arising from production or disposal of your products / services, as identified in the Life Cycle Perspective / Assessments (LCA) or through any other means, briefly describe the same along-with action taken to mitigate the same.");

            List<BrsrReportRequest.LcaRisk> risks = data.getLcaRisks();

            // Logic: Use Table if data exists, otherwise use Note
            if (risks != null && !risks.isEmpty()) {
                XWPFTable tableRisk = doc.createTable();
                tableRisk.setWidth("100%");

                // Headers
                XWPFTableRow hRow = tableRisk.getRow(0);
                ensureCells(hRow, 3);
                styleCell(hRow.getCell(0), "Name of Product / Service", true);
                styleCell(hRow.getCell(1), "Description of the risk / concern", true);
                styleCell(hRow.getCell(2), "Action Taken", true);

                for (BrsrReportRequest.LcaRisk risk : risks) {
                    XWPFTableRow row = tableRisk.createRow();
                    ensureCells(row, 3);
                    styleCell(row.getCell(0), checkNull(risk.getProductName()), false);
                    fillCellWithNewlines(row.getCell(1), checkNull(risk.getRiskDescription()));
                    fillCellWithNewlines(row.getCell(2), checkNull(risk.getActionTaken()));
                }
            } else {
                // If list is empty, print the Note (Sample Style)
                String riskNote = data.getLcaRiskNote();
                XWPFParagraph pNote = doc.createParagraph();
                pNote.setSpacingBefore(100);

                // Use helper to respect newlines
                if (riskNote != null && !riskNote.trim().isEmpty()) {
                    setTextWithBreaks(pNote.createRun(), riskNote);
                } else {
                    pNote.createRun().setText("Nil / Not Applicable");
                }
            }

            doc.createParagraph().createRun().addBreak();

            // --- 3. RECYCLED INPUT MATERIAL ---
            addBoldText(doc, "3. Percentage of recycled or reused input material to total material (by value) used in production (for manufacturing industry) or providing services (for service industry).");

            XWPFTable tableRecycle = doc.createTable();
            tableRecycle.setWidth("100%");

            // --- ROW 0: Headers ---
            XWPFTableRow hRowRec = tableRecycle.getRow(0);
            ensureCells(hRowRec, 2);
            styleCell(hRowRec.getCell(0), "Indicate input material", true);
            styleCell(hRowRec.getCell(1), "Recycled or re-used input material to total material", true);

            // --- ROW 1: FY Sub-headers ---
            // We reuse the FY headers from Section A or C
            String fyCurRec = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current Financial Year";
            String fyPrevRec = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous Financial Year";

            XWPFTableRow subHeaderRec = tableRecycle.createRow();
            ensureCells(subHeaderRec, 3);

            // Cell 0 is empty (under "Indicate input material")
            styleCell(subHeaderRec.getCell(0), "", true);

            // Split Cell 1 into two columns visually (by using a new row logic)
            // But POI tables are strict grids. To match the sample exactly (merged header),
            // We need 3 columns total: [Material] [FY Curr] [FY Prev]
            // And merge Row 0 Cell 1 across columns 1 & 2.

            // RE-DOING TABLE STRUCTURE FOR MERGED HEADER
            // 1. Reset Table Grid to 3 Columns
            CTTblGrid gridRec = tableRecycle.getCTTbl().getTblGrid();
            if(gridRec == null) gridRec = tableRecycle.getCTTbl().addNewTblGrid();
            // Clear existing cols if any (simplified)

            // 2. Configure Row 0 (Merge)
            ensureCells(hRowRec, 3); // Make sure it has 3 cells
            styleCell(hRowRec.getCell(0), "Indicate input material", true);
            styleCell(hRowRec.getCell(1), "Recycled or re-used input material to total material", true);
            mergeCellsHorizontal(hRowRec, 1, 2); // Merge Cell 1 and 2

            // 3. Configure Row 1 (Sub-headers)
            styleCell(subHeaderRec.getCell(0), "", true);
            styleCell(subHeaderRec.getCell(1), fyCurRec, true);
            styleCell(subHeaderRec.getCell(2), fyPrevRec, true);

            // --- DATA ROWS ---
            List<BrsrReportRequest.RecycledInput> inputs = data.getRecycledInputs();
            if (inputs != null && !inputs.isEmpty()) {
                for (BrsrReportRequest.RecycledInput input : inputs) {
                    addDynamicRow(tableRecycle, new String[]{
                            checkNull(input.getMaterialName()),
                            checkNull(input.getCurrentFyPercentage()),
                            checkNull(input.getPreviousFyPercentage())
                    });
                }
            } else {
                addDynamicRow(tableRecycle, new String[]{"-", "-", "-"});
            }

            // --- NOTE (After Table) ---
            addNote(doc, data.getRecycledInputNote());

            doc.createParagraph().createRun().addBreak();

            // --- 4. RECLAIMED PRODUCTS (End of Life) ---
            addBoldText(doc, "4. Of the products and packaging reclaimed at end of life of products, amount (in metric tonnes) reused, recycled, and safely disposed, as per the following format:");

            XWPFTable table4 = doc.createTable();
            table4.setWidth("100%");

            // --- ROW 0: FY HEADERS (Merged) ---
            XWPFTableRow hRow4_1 = table4.getRow(0);
            ensureCells(hRow4_1, 7); // Label + (3 Current) + (3 Previous)

            styleCell(hRow4_1.getCell(0), "", true); // Empty top-left

            // Re-use FY variables defined earlier in method
            String fyCur4 = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrev4 = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRow4_1.getCell(1), fyCur4, true);
            mergeCellsHorizontal(hRow4_1, 1, 3); // Merge 1,2,3

            styleCell(hRow4_1.getCell(4), fyPrev4, true);
            mergeCellsHorizontal(hRow4_1, 4, 6); // Merge 4,5,6

            // --- ROW 1: SUB HEADERS ---
            XWPFTableRow hRow4_2 = table4.createRow();
            ensureCells(hRow4_2, 7);
            styleCell(hRow4_2.getCell(0), "", true);

            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hRow4_2.getCell(base), "Re-Used", true);
                styleCell(hRow4_2.getCell(base+1), "Recycled", true);
                styleCell(hRow4_2.getCell(base+2), "Safely Disposed", true);
            }

            // --- DATA ROWS (Fixed Categories) ---
            // Plastics
            addDynamicRow(table4, new String[]{
                    "Plastics (including packaging)",
                    checkNull(data.getPlasticReusedCurr()), checkNull(data.getPlasticRecycledCurr()), checkNull(data.getPlasticDisposedCurr()),
                    checkNull(data.getPlasticReusedPrev()), checkNull(data.getPlasticRecycledPrev()), checkNull(data.getPlasticDisposedPrev())
            });
            // E-Waste
            addDynamicRow(table4, new String[]{
                    "E-waste",
                    checkNull(data.getEwasteReusedCurr()), checkNull(data.getEwasteRecycledCurr()), checkNull(data.getEwasteDisposedCurr()),
                    checkNull(data.getEwasteReusedPrev()), checkNull(data.getEwasteRecycledPrev()), checkNull(data.getEwasteDisposedPrev())
            });
            // Hazardous
            addDynamicRow(table4, new String[]{
                    "Hazardous waste",
                    checkNull(data.getHazardReusedCurr()), checkNull(data.getHazardRecycledCurr()), checkNull(data.getHazardDisposedCurr()),
                    checkNull(data.getHazardReusedPrev()), checkNull(data.getHazardRecycledPrev()), checkNull(data.getHazardDisposedPrev())
            });
            // Other
            addDynamicRow(table4, new String[]{
                    "Other waste",
                    checkNull(data.getOtherReusedCurr()), checkNull(data.getOtherRecycledCurr()), checkNull(data.getOtherDisposedCurr()),
                    checkNull(data.getOtherReusedPrev()), checkNull(data.getOtherRecycledPrev()), checkNull(data.getOtherDisposedPrev())
            });

            // Note for Q4
            addNote(doc, data.getReclaimedWasteNote());

            doc.createParagraph().createRun().addBreak();

            // --- 5. RECLAIMED % OF SOLD PRODUCTS ---
            addBoldText(doc, "5. Reclaimed products and their packaging materials (as percentage of products sold) for each product category.");

            XWPFTable table5p2 = doc.createTable();
            table5p2.setWidth("100%");

            XWPFTableRow hRow5p2 = table5.getRow(0);
            ensureCells(hRow5p2, 2);
            styleCell(hRow5p2.getCell(0), "Indicate product category", true);
            styleCell(hRow5p2.getCell(1), "Reclaimed products and their packaging materials as % of total products sold in respective category", true);

            List<BrsrReportRequest.ReclaimedPercentage> recList = data.getReclaimedPercentages();
            if (recList != null && !recList.isEmpty()) {
                for (BrsrReportRequest.ReclaimedPercentage item : recList) {
                    addDynamicRow(table5p2, new String[]{
                            checkNull(item.getCategory()),
                            checkNull(item.getPercentage())
                    });
                }
            } else {
                addDynamicRow(table5p2, new String[]{"-", "-"});
            }

            // Note for Q5
            addNote(doc, data.getReclaimedPercentageNote());

            doc.createParagraph().createRun().addBreak();

            // --- PRINCIPLE 3 HEADER ---
            XWPFParagraph pP3 = doc.createParagraph();
            pP3.setSpacingBefore(300);
            XWPFRun rP3 = pP3.createRun();
            rP3.setText("PRINCIPLE 3: Businesses should respect and promote the well-being of all employees, including those in their value chains");
            rP3.setBold(true);
            rP3.setFontSize(12);
            rP3.setColor(COLOR_THEME_GREEN);
            rP3.setFontFamily("Calibri");

            addBoldText(doc, "Essential Indicators");
            addBoldText(doc, "1. a. Details of measures for the well-being of employees:");

            // --- TABLE 1.a (Employees) ---
            generateWellBeingTable(doc, data.getEmployeeWellBeing(), "Permanent employees", "Other than Permanent employees");

            doc.createParagraph().createRun().addBreak();

            addBoldText(doc, "1. b. Details of measures for the well-being of workers:");

            // --- TABLE 1.b (Workers) ---
            generateWellBeingTable(doc, data.getWorkerWellBeing(), "Permanent workers", "Other than Permanent workers");

            // --- NOTE (Left Aligned) ---
            String wbNote = data.getWellBeingNote();
            if (wbNote != null && !wbNote.trim().isEmpty()) {
                XWPFParagraph pNote = doc.createParagraph();
                pNote.setSpacingBefore(100);
                pNote.setAlignment(ParagraphAlignment.LEFT); // Explicitly Left Aligned

                XWPFRun rNote = pNote.createRun();
                rNote.setFontFamily("Calibri");
                rNote.setFontSize(9);
                rNote.setItalic(true);
                setTextWithBreaks(rNote, wbNote);
            }

            doc.createParagraph().createRun().addBreak();

            // --- 2. RETIREMENT BENEFITS ---
            addBoldText(doc, "2. Details of retirement benefits, for Current FY and Previous Financial Year.");

            XWPFTable tableRetire = doc.createTable();
            tableRetire.setWidth("100%");

            // --- ROW 0: FY HEADERS (Merged) ---
            XWPFTableRow hRowRet1 = tableRetire.getRow(0);
            ensureCells(hRowRet1, 7); // Benefit + (3 Current) + (3 Previous)

            styleCell(hRowRet1.getCell(0), "Benefits", true);

            // Re-use FY variables
            String fyCurp3 = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrevp3 = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowRet1.getCell(1), fyCurp3, true);
            mergeCellsHorizontal(hRowRet1, 1, 3); // Merge 1,2,3

            styleCell(hRowRet1.getCell(4), fyPrevp3, true);
            mergeCellsHorizontal(hRowRet1, 4, 6); // Merge 4,5,6

            // --- ROW 1: SUB HEADERS ---
            XWPFTableRow hRowRet2 = tableRetire.createRow();
            ensureCells(hRowRet2, 7);
            styleCell(hRowRet2.getCell(0), "", true); // Empty

            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hRowRet2.getCell(base), "Employees Covered (%)", true);
                styleCell(hRowRet2.getCell(base+1), "Workers Covered (%)", true);
                styleCell(hRowRet2.getCell(base+2), "Deducted & Deposited (Y/N/NA)", true);
            }

            // --- DATA ROWS ---
            List<BrsrReportRequest.RetirementBenefit> retList = data.getRetirementBenefits();
            if (retList != null && !retList.isEmpty()) {
                for (BrsrReportRequest.RetirementBenefit rb : retList) {
                    addDynamicRow(tableRetire, new String[]{
                            checkNull(rb.getBenefits()),
                            checkNull(rb.getCurrEmpCovered()) + "%",
                            checkNull(rb.getCurrWorkCovered()) + "%",
                            checkNull(rb.getCurrDeducted()),
                            checkNull(rb.getPrevEmpCovered()) + "%",
                            checkNull(rb.getPrevWorkCovered()) + "%",
                            checkNull(rb.getPrevDeducted())
                    });
                }
            } else {
                addDynamicRow(tableRetire, new String[]{"PF/Gratuity", "-", "-", "-", "-", "-", "-"});
            }

            // Note (After Table)
            addNote(doc, data.getRetirementBenefitNote());

            doc.createParagraph().createRun().addBreak();

            doc.createParagraph().createRun().addBreak();

            // --- 3. ACCESSIBILITY ---
            // Heading exactly as requested
            addBoldText(doc, "3. Accessibility of workplaces");

            // Sub-heading/Question text
            XWPFParagraph pAccessQ = doc.createParagraph();
            XWPFRun rAccessQ = pAccessQ.createRun();
            rAccessQ.setFontFamily("Calibri");
            rAccessQ.setFontSize(10);
            rAccessQ.setText("Are the premises / offices of the entity accessible to differently abled employees and workers, as per the requirements of the Rights of Persons with Disabilities Act, 2016? If not, whether any steps are being taken by the entity in this regard.");
            pAccessQ.setSpacingAfter(100);

            // User Answer (Note/Text)
            XWPFParagraph pAccessAns = doc.createParagraph();
            XWPFRun rAccessAns = pAccessAns.createRun();
            rAccessAns.setFontFamily("Calibri");
            rAccessAns.setFontSize(10);
            setTextWithBreaks(rAccessAns, checkNull(data.getAccessibilityDetails()));
            pAccessAns.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 4. EQUAL OPPORTUNITY POLICY ---
            addBoldText(doc, "4. Does the entity have an equal opportunity policy as per the Rights of Persons with Disabilities Act, 2016? If so, provide a web-link to the policy.");

            // Answer Paragraph
            XWPFParagraph pEqual = doc.createParagraph();

            // 1. Yes/No + Details
            XWPFRun rEqual = pEqual.createRun();
            rEqual.setFontFamily("Calibri");
            rEqual.setFontSize(10);

            String eqStatus = checkNull(data.getEqualOppPolicy());
            String eqDetails = checkNull(data.getEqualOppDetails());

            // Format: "Yes, [Details]"
            String combinedEqText = eqStatus;
            if (!eqDetails.equals("-")) {
                combinedEqText += ", " + eqDetails;
            }
            setTextWithBreaks(rEqual, combinedEqText);

            // 2. Web Link (New Line)
            String eqLink = data.getEqualOppLink();
            if (eqLink != null && !eqLink.trim().isEmpty()) {
                pEqual.createRun().addBreak(); // Line break
                XWPFRun rLink = pEqual.createRun();
                rLink.setFontFamily("Calibri");
                rLink.setFontSize(10);
                rLink.setColor("0563C1"); // Standard Link Blue
                rLink.setText("Weblink: " + eqLink);
                rLink.setUnderline(UnderlinePatterns.SINGLE);
            }

            pEqual.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 5. PARENTAL LEAVE ---
            addBoldText(doc, "5. Return to work and Retention rates of permanent employees and workers that took parental leave.");

            XWPFTable table5P3 = doc.createTable();
            table5.setWidth("100%");

            // --- ROW 0: CATEGORY HEADERS (Merged) ---
            XWPFTableRow hRow5_1 = table5P3.getRow(0);
            ensureCells(hRow5_1, 5); // Gender + (2 Emp) + (2 Work)

            styleCell(hRow5_1.getCell(0), "Gender", true);

            styleCell(hRow5_1.getCell(1), "Permanent Employees", true);
            mergeCellsHorizontal(hRow5_1, 1, 2); // Merge cols 1-2

            styleCell(hRow5_1.getCell(3), "Permanent Workers", true);
            mergeCellsHorizontal(hRow5_1, 3, 4); // Merge cols 3-4

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRow5_2 = table5P3.createRow();
            ensureCells(hRow5_2, 5);
            styleCell(hRow5_2.getCell(0), "", true); // Empty under "Gender"

            styleCell(hRow5_2.getCell(1), "Return to Work Rate (%)", true);
            styleCell(hRow5_2.getCell(2), "Retention Rate (%)", true);
            styleCell(hRow5_2.getCell(3), "Return to Work Rate (%)", true);
            styleCell(hRow5_2.getCell(4), "Retention Rate (%)", true);

            // --- DATA ROWS ---
            // Male
            addDynamicRow(table5P3, new String[]{
                    "Male",
                    checkNull(data.getPlEmpMaleReturn()), checkNull(data.getPlEmpMaleRetain()),
                    checkNull(data.getPlWorkMaleReturn()), checkNull(data.getPlWorkMaleRetain())
            });

            // Female
            addDynamicRow(table5P3, new String[]{
                    "Female",
                    checkNull(data.getPlEmpFemaleReturn()), checkNull(data.getPlEmpFemaleRetain()),
                    checkNull(data.getPlWorkFemaleReturn()), checkNull(data.getPlWorkFemaleRetain())
            });

            // Total
            addDynamicRow(table5P3, new String[]{
                    "Total",
                    checkNull(data.getPlEmpTotalReturn()), checkNull(data.getPlEmpTotalRetain()),
                    checkNull(data.getPlWorkTotalReturn()), checkNull(data.getPlWorkTotalRetain())
            });

            // Note (After Table)
            addNote(doc, data.getParentalLeaveNote());

            doc.createParagraph().createRun().addBreak();

            // --- 6. GRIEVANCE MECHANISM ---
            addBoldText(doc, "6. Is there a mechanism available to receive and redress grievances for the following categories of employees and worker? If yes, give details of the mechanism in brief.");

            XWPFTable table6P3 = doc.createTable();
            table6.setWidth("100%");

            // Set widths: Category (30%), Details (70%)
            setColumnWidths(table6P3, 3000, 7000);

            // Header Row
            XWPFTableRow hRow6 = table6P3.getRow(0);
            ensureCells(hRow6, 2);
            styleCell(hRow6.getCell(0), "Category", true);
            styleCell(hRow6.getCell(1), "Yes/No (If Yes, then give details of the mechanism in brief)", true);

            // Data Rows
            addRow(table6P3, "Permanent Workers", data.getGrievancePermWorkers());
            addRow(table6P3, "Other than Permanent Workers", data.getGrievanceTempWorkers());
            addRow(table6P3, "Permanent Employees", data.getGrievancePermEmployees());
            addRow(table6P3, "Other than Permanent Employees", data.getGrievanceTempEmployees());

            doc.createParagraph().createRun().addBreak();

            // --- 7. UNION MEMBERSHIP ---
            addBoldText(doc, "7. Membership of employees and worker in association(s) or Unions recognised by the listed entity:");

            XWPFTable table7 = doc.createTable();
            table7.setWidth("100%");

            // --- ROW 0: FY HEADERS (Merged) ---
            XWPFTableRow hRow7_1 = table7.getRow(0);
            ensureCells(hRow7_1, 7); // Category + (3 Current) + (3 Previous)

            styleCell(hRow7_1.getCell(0), "Category", true);

            String fyCurP3 = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrevP3 = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRow7_1.getCell(1), fyCurP3, true);
            mergeCellsHorizontal(hRow7_1, 1, 3); // Merge 1,2,3

            styleCell(hRow7_1.getCell(4), fyPrevP3, true);
            mergeCellsHorizontal(hRow7_1, 4, 6); // Merge 4,5,6

            // --- ROW 1: SUB HEADERS ---
            XWPFTableRow hRow7_2 = table7.createRow();
            ensureCells(hRow7_2, 7);
            styleCell(hRow7_2.getCell(0), "", true); // Empty

            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hRow7_2.getCell(base), "Total (A/C)", true);
                styleCell(hRow7_2.getCell(base+1), "No. in Union (B/D)", true);
                styleCell(hRow7_2.getCell(base+2), "% (B/A)", true);
            }

            // --- DATA ROWS ---

            // 1. Employees (Header)
            addSectionTitleRow(table7, "Total Permanent Employees", 7);

            // Employee Rows (Total, Male, Female)
            // Note: Total row is calculated in JS , but we store it.
            addDynamicRow(table7, new String[]{
                    "- Total",
                    checkNull(data.getUnionCurrEmpTotalA()), checkNull(data.getUnionCurrEmpUnionB()), checkNull(data.getUnionCurrEmpPerc()),
                    checkNull(data.getUnionPrevEmpTotalC()), checkNull(data.getUnionPrevEmpUnionD()), checkNull(data.getUnionPrevEmpPerc())
            });
            addDynamicRow(table7, new String[]{
                    "- Male",
                    checkNull(data.getUnionCurrEmpMaleTotal()), checkNull(data.getUnionCurrEmpMaleUnion()), checkNull(data.getUnionCurrEmpMalePerc()),
                    checkNull(data.getUnionPrevEmpMaleTotal()), checkNull(data.getUnionPrevEmpMaleUnion()), checkNull(data.getUnionPrevEmpMalePerc())
            });
            addDynamicRow(table7, new String[]{
                    "- Female",
                    checkNull(data.getUnionCurrEmpFemaleTotal()), checkNull(data.getUnionCurrEmpFemaleUnion()), checkNull(data.getUnionCurrEmpFemalePerc()),
                    checkNull(data.getUnionPrevEmpFemaleTotal()), checkNull(data.getUnionPrevEmpFemaleUnion()), checkNull(data.getUnionPrevEmpFemalePerc())
            });

            // 2. Workers (Header)
            addSectionTitleRow(table7, "Total Permanent Workers", 7);

            // Worker Rows
            addDynamicRow(table7, new String[]{
                    "- Total",
                    checkNull(data.getUnionCurrWorkTotalA()), checkNull(data.getUnionCurrWorkUnionB()), checkNull(data.getUnionCurrWorkPerc()),
                    checkNull(data.getUnionPrevWorkTotalC()), checkNull(data.getUnionPrevWorkUnionD()), checkNull(data.getUnionPrevWorkPerc())
            });
            addDynamicRow(table7, new String[]{
                    "- Male",
                    checkNull(data.getUnionCurrWorkMaleTotal()), checkNull(data.getUnionCurrWorkMaleUnion()), checkNull(data.getUnionCurrWorkMalePerc()),
                    checkNull(data.getUnionPrevWorkMaleTotal()), checkNull(data.getUnionPrevWorkMaleUnion()), checkNull(data.getUnionPrevWorkMalePerc())
            });
            addDynamicRow(table7, new String[]{
                    "- Female",
                    checkNull(data.getUnionCurrWorkFemaleTotal()), checkNull(data.getUnionCurrWorkFemaleUnion()), checkNull(data.getUnionCurrWorkFemalePerc()),
                    checkNull(data.getUnionPrevWorkFemaleTotal()), checkNull(data.getUnionPrevWorkFemaleUnion()), checkNull(data.getUnionPrevWorkFemalePerc())
            });

            // Note (After Table)
            addNote(doc, data.getUnionMembershipNote());

            doc.createParagraph().createRun().addBreak();

            // --- 8. TRAINING DETAILS ---
            addBoldText(doc, "8. Details of training given to employees and workers:");

            XWPFTable table8 = doc.createTable();
            table8.setWidth("100%");

            // --- ROW 0: FY HEADERS ---
            XWPFTableRow hRow8_1 = table8.getRow(0);
            ensureCells(hRow8_1, 11);

            styleCell(hRow8_1.getCell(0), "Category", true);
            String fyCur8 = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrev8 = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRow8_1.getCell(1), fyCur8, true);
            mergeCellsHorizontal(hRow8_1, 1, 5);
            styleCell(hRow8_1.getCell(6), fyPrev8, true);
            mergeCellsHorizontal(hRow8_1, 6, 10);

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRow8_2 = table8.createRow();
            ensureCells(hRow8_2, 11);
            styleCell(hRow8_2.getCell(0), "", true);
            for(int i=0; i<2; i++) {
                int base = 1 + (i*5);
                styleCell(hRow8_2.getCell(base), "Total", true);
                styleCell(hRow8_2.getCell(base+1), "Health No.", true);
                styleCell(hRow8_2.getCell(base+2), "%", true);
                styleCell(hRow8_2.getCell(base+3), "Skill No.", true);
                styleCell(hRow8_2.getCell(base+4), "%", true);
            }

            // --- EMPLOYEES ---
            addSectionTitleRow(table8, "EMPLOYEES", 11);
            // Male
            addDynamicRow(table8, new String[]{ "Male", checkNull(data.getTrainEmpMaleTotal()), checkNull(data.getTrainEmpMaleHealthNo()), checkNull(data.getTrainEmpMaleHealthPerc()), checkNull(data.getTrainEmpMaleSkillNo()), checkNull(data.getTrainEmpMaleSkillPerc()), checkNull(data.getTrainEmpMaleTotalPrev()), checkNull(data.getTrainEmpMaleHealthNoPrev()), checkNull(data.getTrainEmpMaleHealthPercPrev()), checkNull(data.getTrainEmpMaleSkillNoPrev()), checkNull(data.getTrainEmpMaleSkillPercPrev()) });
            // Female
            addDynamicRow(table8, new String[]{ "Female", checkNull(data.getTrainEmpFemaleTotal()), checkNull(data.getTrainEmpFemaleHealthNo()), checkNull(data.getTrainEmpFemaleHealthPerc()), checkNull(data.getTrainEmpFemaleSkillNo()), checkNull(data.getTrainEmpFemaleSkillPerc()), checkNull(data.getTrainEmpFemaleTotalPrev()), checkNull(data.getTrainEmpFemaleHealthNoPrev()), checkNull(data.getTrainEmpFemaleHealthPercPrev()), checkNull(data.getTrainEmpFemaleSkillNoPrev()), checkNull(data.getTrainEmpFemaleSkillPercPrev()) });
            // Others
            addDynamicRow(table8, new String[]{ "Others", checkNull(data.getTrainEmpOtherTotal()), checkNull(data.getTrainEmpOtherHealthNo()), checkNull(data.getTrainEmpOtherHealthPerc()), checkNull(data.getTrainEmpOtherSkillNo()), checkNull(data.getTrainEmpOtherSkillPerc()), checkNull(data.getTrainEmpOtherTotalPrev()), checkNull(data.getTrainEmpOtherHealthNoPrev()), checkNull(data.getTrainEmpOtherHealthPercPrev()), checkNull(data.getTrainEmpOtherSkillNoPrev()), checkNull(data.getTrainEmpOtherSkillPercPrev()) });
            // Total
            addDynamicRow(table8, new String[]{ "Total", checkNull(data.getTrainEmpGenTotal()), checkNull(data.getTrainEmpGenHealthNo()), checkNull(data.getTrainEmpGenHealthPerc()), checkNull(data.getTrainEmpGenSkillNo()), checkNull(data.getTrainEmpGenSkillPerc()), checkNull(data.getTrainEmpGenTotalPrev()), checkNull(data.getTrainEmpGenHealthNoPrev()), checkNull(data.getTrainEmpGenHealthPercPrev()), checkNull(data.getTrainEmpGenSkillNoPrev()), checkNull(data.getTrainEmpGenSkillPercPrev()) });

            // --- WORKERS ---
            addSectionTitleRow(table8, "WORKERS", 11);
            // Male
            addDynamicRow(table8, new String[]{ "Male", checkNull(data.getTrainWorkMaleTotal()), checkNull(data.getTrainWorkMaleHealthNo()), checkNull(data.getTrainWorkMaleHealthPerc()), checkNull(data.getTrainWorkMaleSkillNo()), checkNull(data.getTrainWorkMaleSkillPerc()), checkNull(data.getTrainWorkMaleTotalPrev()), checkNull(data.getTrainWorkMaleHealthNoPrev()), checkNull(data.getTrainWorkMaleHealthPercPrev()), checkNull(data.getTrainWorkMaleSkillNoPrev()), checkNull(data.getTrainWorkMaleSkillPercPrev()) });
            // Female
            addDynamicRow(table8, new String[]{ "Female", checkNull(data.getTrainWorkFemaleTotal()), checkNull(data.getTrainWorkFemaleHealthNo()), checkNull(data.getTrainWorkFemaleHealthPerc()), checkNull(data.getTrainWorkFemaleSkillNo()), checkNull(data.getTrainWorkFemaleSkillPerc()), checkNull(data.getTrainWorkFemaleTotalPrev()), checkNull(data.getTrainWorkFemaleHealthNoPrev()), checkNull(data.getTrainWorkFemaleHealthPercPrev()), checkNull(data.getTrainWorkFemaleSkillNoPrev()), checkNull(data.getTrainWorkFemaleSkillPercPrev()) });
            // Others
            addDynamicRow(table8, new String[]{ "Others", checkNull(data.getTrainWorkOtherTotal()), checkNull(data.getTrainWorkOtherHealthNo()), checkNull(data.getTrainWorkOtherHealthPerc()), checkNull(data.getTrainWorkOtherSkillNo()), checkNull(data.getTrainWorkOtherSkillPerc()), checkNull(data.getTrainWorkOtherTotalPrev()), checkNull(data.getTrainWorkOtherHealthNoPrev()), checkNull(data.getTrainWorkOtherHealthPercPrev()), checkNull(data.getTrainWorkOtherSkillNoPrev()), checkNull(data.getTrainWorkOtherSkillPercPrev()) });
            // Total
            addDynamicRow(table8, new String[]{ "Total", checkNull(data.getTrainWorkGenTotal()), checkNull(data.getTrainWorkGenHealthNo()), checkNull(data.getTrainWorkGenHealthPerc()), checkNull(data.getTrainWorkGenSkillNo()), checkNull(data.getTrainWorkGenSkillPerc()), checkNull(data.getTrainWorkGenTotalPrev()), checkNull(data.getTrainWorkGenHealthNoPrev()), checkNull(data.getTrainWorkGenHealthPercPrev()), checkNull(data.getTrainWorkGenSkillNoPrev()), checkNull(data.getTrainWorkGenSkillPercPrev()) });

            addNote(doc, data.getTrainingDetailsNote());

            doc.createParagraph().createRun().addBreak();

            // --- 9. PERFORMANCE REVIEWS ---
            addBoldText(doc, "9. Details of performance and career development reviews of employees and worker:");

            XWPFTable table9 = doc.createTable();
            table9.setWidth("100%");

            // --- ROW 0: FY HEADERS ---
            XWPFTableRow hRow9_1 = table9.getRow(0);
            ensureCells(hRow9_1, 7); // Category + (3 Current) + (3 Previous)

            styleCell(hRow9_1.getCell(0), "Category", true);

            String fyCur9 = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrev9 = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRow9_1.getCell(1), fyCur9, true);
            mergeCellsHorizontal(hRow9_1, 1, 3);
            styleCell(hRow9_1.getCell(4), fyPrev9, true);
            mergeCellsHorizontal(hRow9_1, 4, 6);

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRow9_2 = table9.createRow();
            ensureCells(hRow9_2, 7);
            styleCell(hRow9_2.getCell(0), "", true);

            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hRow9_2.getCell(base), "Total (A)", true);
                styleCell(hRow9_2.getCell(base+1), "No. Covered (B)", true);
                styleCell(hRow9_2.getCell(base+2), "% (B/A)", true);
            }

            // --- EMPLOYEES ---
            addSectionTitleRow(table9, "EMPLOYEES", 7);
            // Male
            addDynamicRow(table9, new String[]{ "Male",
                    checkNull(data.getRevEmpMaleTotal()), checkNull(data.getRevEmpMaleCovered()), checkNull(data.getRevEmpMalePerc()),
                    checkNull(data.getRevEmpMaleTotalPrev()), checkNull(data.getRevEmpMaleCoveredPrev()), checkNull(data.getRevEmpMalePercPrev())
            });
            // Female
            addDynamicRow(table9, new String[]{ "Female",
                    checkNull(data.getRevEmpFemTotal()), checkNull(data.getRevEmpFemCovered()), checkNull(data.getRevEmpFemPerc()),
                    checkNull(data.getRevEmpFemTotalPrev()), checkNull(data.getRevEmpFemCoveredPrev()), checkNull(data.getRevEmpFemPercPrev())
            });
            // Others
            addDynamicRow(table9, new String[]{ "Others",
                    checkNull(data.getRevEmpOthTotal()), checkNull(data.getRevEmpOthCovered()), checkNull(data.getRevEmpOthPerc()),
                    checkNull(data.getRevEmpOthTotalPrev()), checkNull(data.getRevEmpOthCoveredPrev()), checkNull(data.getRevEmpOthPercPrev())
            });
            // Total
            addDynamicRow(table9, new String[]{ "Total",
                    checkNull(data.getRevEmpGenTotal()), checkNull(data.getRevEmpGenCovered()), checkNull(data.getRevEmpGenPerc()),
                    checkNull(data.getRevEmpGenTotalPrev()), checkNull(data.getRevEmpGenCoveredPrev()), checkNull(data.getRevEmpGenPercPrev())
            });

            // --- WORKERS ---
            addSectionTitleRow(table9, "WORKERS", 7);
            // Male
            addDynamicRow(table9, new String[]{ "Male",
                    checkNull(data.getRevWorkMaleTotal()), checkNull(data.getRevWorkMaleCovered()), checkNull(data.getRevWorkMalePerc()),
                    checkNull(data.getRevWorkMaleTotalPrev()), checkNull(data.getRevWorkMaleCoveredPrev()), checkNull(data.getRevWorkMalePercPrev())
            });
            // Female
            addDynamicRow(table9, new String[]{ "Female",
                    checkNull(data.getRevWorkFemTotal()), checkNull(data.getRevWorkFemCovered()), checkNull(data.getRevWorkFemPerc()),
                    checkNull(data.getRevWorkFemTotalPrev()), checkNull(data.getRevWorkFemCoveredPrev()), checkNull(data.getRevWorkFemPercPrev())
            });
            // Others
            addDynamicRow(table9, new String[]{ "Others",
                    checkNull(data.getRevWorkOthTotal()), checkNull(data.getRevWorkOthCovered()), checkNull(data.getRevWorkOthPerc()),
                    checkNull(data.getRevWorkOthTotalPrev()), checkNull(data.getRevWorkOthCoveredPrev()), checkNull(data.getRevWorkOthPercPrev())
            });
            // Total
            addDynamicRow(table9, new String[]{ "Total",
                    checkNull(data.getRevWorkGenTotal()), checkNull(data.getRevWorkGenCovered()), checkNull(data.getRevWorkGenPerc()),
                    checkNull(data.getRevWorkGenTotalPrev()), checkNull(data.getRevWorkGenCoveredPrev()), checkNull(data.getRevWorkGenPercPrev())
            });

            addNote(doc, data.getReviewDetailsNote());

            doc.createParagraph().createRun().addBreak();

            // --- 10. HEALTH & SAFETY ---
            addBoldText(doc, "10. Health and safety management system:");

            // 10.a
            addBoldText(doc, "a. Whether an occupational health and safety management system has been implemented by the entity? (Yes/ No). If yes, the coverage such system?");
            XWPFParagraph p10a = doc.createParagraph();
            p10a.setSpacingAfter(200);
            setTextWithBreaks(p10a.createRun(), checkNull(data.getHealthSafetySystem()));

            // 10.b
            addBoldText(doc, "b. What are the processes used to identify work-related hazards and assess risks on a routine and non-routine basis by the entity?");
            XWPFParagraph p10b = doc.createParagraph();
            p10b.setSpacingAfter(200);
            setTextWithBreaks(p10b.createRun(), checkNull(data.getHazardIdentification()));

            // 10.c
            addBoldText(doc, "c. Whether you have processes for workers to report the work related hazards and to remove themselves from such risks. (Y/N)");
            XWPFParagraph p10c = doc.createParagraph();
            p10c.setSpacingAfter(200);
            setTextWithBreaks(p10c.createRun(), checkNull(data.getHazardReporting()));

            // 10.d
            addBoldText(doc, "d. Do the employees/ worker of the entity have access to non-occupational medical and healthcare services? (Yes/ No)");
            XWPFParagraph p10d = doc.createParagraph();
            p10d.setSpacingAfter(200);
            setTextWithBreaks(p10d.createRun(), checkNull(data.getMedicalAccess()));

            doc.createParagraph().createRun().addBreak();

            // --- 11. SAFETY INCIDENTS ---
            addBoldText(doc, "11. Details of safety related incidents, in the following format:");

            XWPFTable table11 = doc.createTable();
            table11.setWidth("100%");

            // --- HEADERS ---
            XWPFTableRow hRow11 = table11.getRow(0);
            ensureCells(hRow11, 4);

            styleCell(hRow11.getCell(0), "Safety Incident/Number", true);
            styleCell(hRow11.getCell(1), "Category", true);

            // Use User Provided FY or defaults
            String fyCur11 = (data.getFyCurrent() != null && !data.getFyCurrent().isEmpty()) ? data.getFyCurrent() : "Current FY";
            String fyPrev11 = (data.getFyPrevious() != null && !data.getFyPrevious().isEmpty()) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRow11.getCell(2), fyCur11, true);
            styleCell(hRow11.getCell(3), fyPrev11, true);

            // --- DATA ROWS ---

            // 1. LTIFR
            addSafetyGroupRow(table11, "Lost Time Injury Frequency Rate (LTIFR)",
                    checkNull(data.getSafetyLtifrEmpCurr()), checkNull(data.getSafetyLtifrEmpPrev()),
                    checkNull(data.getSafetyLtifrWorkCurr()), checkNull(data.getSafetyLtifrWorkPrev()));

            // 2. Total Injuries
            addSafetyGroupRow(table11, "Total recordable work-related injuries",
                    checkNull(data.getSafetyTotalInjEmpCurr()), checkNull(data.getSafetyTotalInjEmpPrev()),
                    checkNull(data.getSafetyTotalInjWorkCurr()), checkNull(data.getSafetyTotalInjWorkPrev()));

            // 3. Fatalities
            addSafetyGroupRow(table11, "No. of fatalities",
                    checkNull(data.getSafetyFatalEmpCurr()), checkNull(data.getSafetyFatalEmpPrev()),
                    checkNull(data.getSafetyFatalWorkCurr()), checkNull(data.getSafetyFatalWorkPrev()));

            // 4. High Consequence
            addSafetyGroupRow(table11, "High consequence work-related injury or ill-health (excluding fatalities)",
                    checkNull(data.getSafetyHighConEmpCurr()), checkNull(data.getSafetyHighConEmpPrev()),
                    checkNull(data.getSafetyHighConWorkCurr()), checkNull(data.getSafetyHighConWorkPrev()));

            // 5. Permanent Disabilities
            addSafetyGroupRow(table11, "Number of Permanent Disabilities",
                    checkNull(data.getSafetyPermDisEmpCurr()), checkNull(data.getSafetyPermDisEmpPrev()),
                    checkNull(data.getSafetyPermDisWorkCurr()), checkNull(data.getSafetyPermDisWorkPrev()));

            // Note
            addNote(doc, data.getSafetyIncidentsNote());

            doc.createParagraph().createRun().addBreak();

            // --- 12. SAFETY MEASURES ---
            addBoldText(doc, "12. Describe the measures taken by the entity to ensure a safe and healthy work place.");

            List<BrsrReportRequest.SafetyMeasure> measures = data.getSafetyMeasures();

            if (measures != null && !measures.isEmpty()) {
                int roman = 1;
                for (BrsrReportRequest.SafetyMeasure measure : measures) {
                    XWPFParagraph pMeasure = doc.createParagraph();
                    pMeasure.setSpacingAfter(100);

                    // Roman Numeral Heading (I), II), etc.)
                    XWPFRun rHead = pMeasure.createRun();
                    rHead.setText(romanToDecimal(roman++) + ") " + checkNull(measure.getHeading()));
                    rHead.setBold(true);
                    rHead.setColor(COLOR_BLUE_HEADER);
                    rHead.setFontFamily("Calibri");
                    rHead.addBreak(); // New line for description

                    // Description
                    XWPFRun rDescP3 = pMeasure.createRun();
                    rDesc.setFontFamily("Calibri");
                    setTextWithBreaks(rDescP3, checkNull(measure.getDescription()));

                    pMeasure.createRun().addBreak(); // Spacing between items
                }
            } else {
                XWPFParagraph pNone = doc.createParagraph();
                pNone.createRun().setText("No specific measures detailed.");
            }

            doc.createParagraph().createRun().addBreak();

            // --- 13. WORKER COMPLAINTS ---
            addBoldText(doc, "13. Number of Complaints on the following made by employees and workers:");

            XWPFTable table13 = doc.createTable();
            table13.setWidth("100%");

            // --- ROW 0: FY HEADERS (Merged) ---
            XWPFTableRow hRow13_1 = table13.getRow(0);
            ensureCells(hRow13_1, 7); // Category + (3 Current) + (3 Previous)

            styleCell(hRow13_1.getCell(0), "Category", true);

            // Reuse FY variables
            String fyCur13 = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrev13 = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRow13_1.getCell(1), fyCur13, true);
            mergeCellsHorizontal(hRow13_1, 1, 3); // Merge 1-3

            styleCell(hRow13_1.getCell(4), fyPrev13, true);
            mergeCellsHorizontal(hRow13_1, 4, 6); // Merge 4-6

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRow13_2 = table13.createRow();
            ensureCells(hRow13_2, 7);
            styleCell(hRow13_2.getCell(0), "", true); // Empty

            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hRow13_2.getCell(base), "Filed", true);
                styleCell(hRow13_2.getCell(base+1), "Pending", true);
                styleCell(hRow13_2.getCell(base+2), "Remarks", true);
            }

            // --- DATA ROWS ---
            // Working Conditions
            addDynamicRow(table13, new String[]{
                    "Working Conditions",
                    checkNull(data.getWcFiledCurr()), checkNull(data.getWcPendingCurr()), checkNull(data.getWcRemarksCurr()),
                    checkNull(data.getWcFiledPrev()), checkNull(data.getWcPendingPrev()), checkNull(data.getWcRemarksPrev())
            });

            // Health & Safety
            addDynamicRow(table13, new String[]{
                    "Health & Safety",
                    checkNull(data.getHsFiledCurr()), checkNull(data.getHsPendingCurr()), checkNull(data.getHsRemarksCurr()),
                    checkNull(data.getHsFiledPrev()), checkNull(data.getHsPendingPrev()), checkNull(data.getHsRemarksPrev())
            });

            // Note
            addNote(doc, data.getWorkerComplaintsNote());

            doc.createParagraph().createRun().addBreak();

            // --- 14. ASSESSMENTS ---
            addBoldText(doc, "14. Assessments for the year:");

            XWPFTable table14P3 = doc.createTable();
            table14.setWidth("100%");

            // Set Widths: Label (40%), Percentage (60%)
            setColumnWidths(table14P3, 4000, 6000);

            // Header
            XWPFTableRow hRow14 = table14P3.getRow(0);
            ensureCells(hRow14, 2);
            styleCell(hRow14.getCell(0), "", true); // Empty top-left
            styleCell(hRow14.getCell(1), "% of your plants and offices that were assessed (by entity or statutory authorities or third parties)", true);

            // Data Rows
            addRow(table14P3, "Health & Safety Practices", checkNull(data.getAssessmentHealthPerc()) + "%");
            addRow(table14P3, "Working Conditions", checkNull(data.getAssessmentWorkingPerc()) + "%");

            // Note
            addNote(doc, data.getAssessmentNote());

            doc.createParagraph().createRun().addBreak();

            // --- 15. CORRECTIVE ACTIONS (SAFETY) ---
            addBoldText(doc, "15. Provide details of any corrective action taken or underway to address safety-related incidents (if any) and on significant risks / concerns arising from assessments of health & safety practices and working conditions.");

            XWPFParagraph p15 = doc.createParagraph();
            XWPFRun r15 = p15.createRun();
            r15.setFontFamily("Calibri");
            r15.setFontSize(10);

            // Use helper to respect newlines
            setTextWithBreaks(r15, checkNull(data.getSafetyCorrectiveActions()));
            p15.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP INDICATORS ---
            addSectionHeader(doc, "Leadership Indicators");

            // 1. Life Insurance
            addBoldText(doc, "1. Does the entity extend any life insurance or any compensatory package in the event of death of (A) Employees (Y/N) (B) Workers (Y/N).");

            XWPFParagraph pLife = doc.createParagraph();
            XWPFRun rLife = pLife.createRun();
            rLife.setFontFamily("Calibri");
            rLife.setFontSize(10);

            // Format: "A) Employees - Yes  B) Workers - Yes"
            String empStatus = checkNull(data.getLifeInsuranceEmployees());
            String workStatus = checkNull(data.getLifeInsuranceWorkers());

            rLife.setText("A) Employees – " + empStatus);
            rLife.addBreak();
            rLife.setText("B) Workers – " + workStatus);
            rLife.addBreak();
            rLife.addBreak(); // Gap before details

            // Details Paragraph
            setTextWithBreaks(rLife, checkNull(data.getLifeInsuranceDetails()));
            pLife.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 2: STATUTORY DUES ---
            addBoldText(doc, "2. Provide the measures undertaken by the entity to ensure that statutory dues have been deducted and deposited by the value chain partners.");

            XWPFParagraph pDues = doc.createParagraph();
            XWPFRun rDues = pDues.createRun();
            rDues.setFontFamily("Calibri");
            rDues.setFontSize(10);

            // Use helper to respect newlines
            setTextWithBreaks(rDues, checkNull(data.getStatutoryDuesMeasures()));
            pDues.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 3: REHABILITATION ---
            addBoldText(doc, "3. Provide the number of employees / workers having suffered high consequence work-related injury / ill-health / fatalities (as reported in Q11 of Essential Indicators above), who have been rehabilitated and placed in suitable employment or whose family members have been placed in suitable employment:");

            XWPFTable tableLead3 = doc.createTable();
            tableLead3.setWidth("100%");

            // --- ROW 0: METRIC HEADERS (Merged) ---
            XWPFTableRow hRowL3_1 = tableLead3.getRow(0);
            ensureCells(hRowL3_1, 5); // Empty + (2 Affected) + (2 Placed)

            styleCell(hRowL3_1.getCell(0), "", true);

            styleCell(hRowL3_1.getCell(1), "Total no. of affected employees/ workers", true);
            mergeCellsHorizontal(hRowL3_1, 1, 2);

            styleCell(hRowL3_1.getCell(3), "No. of employees/workers that are rehabilitated and placed in suitable employment or whose family members have been placed in suitable employment", true);
            mergeCellsHorizontal(hRowL3_1, 3, 4);

            // --- ROW 1: FY HEADERS ---
            XWPFTableRow hRowL3_2 = tableLead3.createRow();
            ensureCells(hRowL3_2, 5);
            styleCell(hRowL3_2.getCell(0), "", true);

            // Reuse User FY
            String fyCurL3 = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrevL3 = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowL3_2.getCell(1), fyCurL3, true);
            styleCell(hRowL3_2.getCell(2), fyPrevL3, true);
            styleCell(hRowL3_2.getCell(3), fyCurL3, true);
            styleCell(hRowL3_2.getCell(4), fyPrevL3, true);

            // --- DATA ROWS ---
            // Employees
            addDynamicRow(tableLead3, new String[]{
                    "Employees",
                    checkNull(data.getRehabEmpAffCurr()), checkNull(data.getRehabEmpAffPrev()),
                    checkNull(data.getRehabEmpPlacedCurr()), checkNull(data.getRehabEmpPlacedPrev())
            });

            // Workers
            addDynamicRow(tableLead3, new String[]{
                    "Workers",
                    checkNull(data.getRehabWorkAffCurr()), checkNull(data.getRehabWorkAffPrev()),
                    checkNull(data.getRehabWorkPlacedCurr()), checkNull(data.getRehabWorkPlacedPrev())
            });

            // Note (After Table)
            addNote(doc, data.getRehabilitationNote());

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 4: TRANSITION ASSISTANCE ---
            addBoldText(doc, "4. Does the entity provide transition assistance programs to facilitate continued employability and the management of career endings resulting from retirement or termination of employment? (Yes/ No)");

            XWPFParagraph pTrans = doc.createParagraph();
            XWPFRun rTrans = pTrans.createRun();
            rTrans.setFontFamily("Calibri");
            rTrans.setFontSize(10);

            // Use helper to respect newlines
            setTextWithBreaks(rTrans, checkNull(data.getTransitionAssistanceDetails()));
            pTrans.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 5: VALUE CHAIN ASSESSMENT ---
            addBoldText(doc, "5. Details on assessment of value chain partners:");

            // Note (Before Table)
            String vcNote = data.getValueChainAssessmentNote();
            if (vcNote != null && !vcNote.trim().isEmpty()) {
                XWPFParagraph pVcNote = doc.createParagraph();
                pVcNote.setSpacingAfter(100);
                setTextWithBreaks(pVcNote.createRun(), vcNote);
            }

            // Table
            XWPFTable tableLead5 = doc.createTable();
            tableLead5.setWidth("100%");

            // Set widths: Label (40%), Percentage (60%)
            setColumnWidths(tableLead5, 4000, 6000);

            // Header
            XWPFTableRow hRowL5 = tableLead5.getRow(0);
            ensureCells(hRowL5, 2);
            styleCell(hRowL5.getCell(0), "", true);
            styleCell(hRowL5.getCell(1), "% of value chain partners (by value of business done with such partners) that were assessed", true);

            // Data Rows
            addRow(tableLead5, "Health and safety practices", checkNull(data.getVcHealthSafetyPerc()) + "%");
            addRow(tableLead5, "Working Conditions", checkNull(data.getVcWorkingCondPerc()) + "%");

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 6: VALUE CHAIN CORRECTIVE ACTIONS ---
            addBoldText(doc, "6. Provide details of any corrective actions taken or underway to address significant risks / concerns arising from assessments of health and safety practices and working conditions of value chain partners.");

            // Intro Text
            XWPFParagraph pVcIntro = doc.createParagraph();
            XWPFRun rVcIntro = pVcIntro.createRun();
            rVcIntro.setFontFamily("Calibri");
            rVcIntro.setFontSize(10);
            setTextWithBreaks(rVcIntro, checkNull(data.getVcCorrectiveActionIntro()));
            pVcIntro.setSpacingAfter(100);

            // Bullet Points
            List<BrsrReportRequest.ValueChainAction> vcActions = data.getVcCorrectiveActions();
            if (vcActions != null && !vcActions.isEmpty()) {
                for (BrsrReportRequest.ValueChainAction action : vcActions) {
                    XWPFParagraph pBullet = doc.createParagraph();
                    pBullet.setSpacingAfter(100);
                    pBullet.setIndentationLeft(720); // Indent (0.5 inch)
                    pBullet.setIndentationHanging(360); // Hanging indent for bullet

                    XWPFRun rBullet = pBullet.createRun();
                    rBullet.setFontFamily("Calibri");
                    rBullet.setFontSize(10);
                    rBullet.setColor("0070C0"); // Blue Bullet
                    rBullet.setText("»  "); // Custom Bullet Symbol

                    XWPFRun rText = pBullet.createRun();
                    rText.setFontFamily("Calibri");
                    rText.setFontSize(10);
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
            rP4.setColor(COLOR_THEME_GREEN);
            rP4.setFontFamily("Calibri");

            addBoldText(doc, "Essential Indicators");
            addBoldText(doc, "1. Describe the processes for identifying key stakeholder groups of the entity.");

            // 1. Intro Paragraph
            XWPFParagraph pP4Intro = doc.createParagraph();
            setTextWithBreaks(pP4Intro.createRun(), checkNull(data.getPrinciple4Q1Intro()));
            pP4Intro.setSpacingAfter(100);

            // 2. Numbered List
            List<String> p4Points = data.getPrinciple4Q1Points();
            if (p4Points != null && !p4Points.isEmpty()) {
                int num = 1;
                for (String point : p4Points) {
                    XWPFParagraph pPoint = doc.createParagraph();
                    pPoint.setIndentationLeft(720);   // Indent whole block
                    pPoint.setIndentationHanging(360); // Hanging indent for number
                    pPoint.setSpacingAfter(0); // Tight spacing

                    XWPFRun rPoint = pPoint.createRun();
                    rPoint.setFontFamily("Calibri");
                    rPoint.setFontSize(10);
                    rPoint.setText(num++ + ".  " + point);
                }
            }

            // 3. Closing Paragraph (New line after list)
            XWPFParagraph pP4Concl = doc.createParagraph();
            pP4Concl.setSpacingBefore(200);
            setTextWithBreaks(pP4Concl.createRun(), checkNull(data.getPrinciple4Q1Conclusion()));

            doc.createParagraph().createRun().addBreak();

            // --- 2. STAKEHOLDER ENGAGEMENT TABLE ---
            addBoldText(doc, "2. List stakeholder groups identified as key for your entity and the frequency of engagement with each stakeholder group.");

            XWPFTable tableStake = doc.createTable();
            tableStake.setWidth("100%");

            // Set Column Widths (optimized for text-heavy columns)
            CTTblGrid gridStake = tableStake.getCTTbl().addNewTblGrid();
            gridStake.addNewGridCol().setW(BigInteger.valueOf(1200)); // Group Name
            gridStake.addNewGridCol().setW(BigInteger.valueOf(1000)); // Vulnerable?
            gridStake.addNewGridCol().setW(BigInteger.valueOf(2800)); // Channels
            gridStake.addNewGridCol().setW(BigInteger.valueOf(1500)); // Frequency
            gridStake.addNewGridCol().setW(BigInteger.valueOf(3500)); // Purpose

            // Header
            XWPFTableRow hRowStake = tableStake.getRow(0);
            ensureCells(hRowStake, 5);
            styleCell(hRowStake.getCell(0), "Stakeholder Group", true);
            styleCell(hRowStake.getCell(1), "Whether identified as Vulnerable & Marginalized Group (Yes/No)", true);
            styleCell(hRowStake.getCell(2), "Channels of communication (Email, SMS, Meetings, etc.)", true);
            styleCell(hRowStake.getCell(3), "Frequency of engagement (Annually/ Quarterly/ others)", true);
            styleCell(hRowStake.getCell(4), "Purpose and scope of engagement including key topics and concerns raised", true);

            // Data Rows
            List<BrsrReportRequest.StakeholderEngagement> engagements = data.getStakeholderEngagements();
            if (engagements != null && !engagements.isEmpty()) {
                for (BrsrReportRequest.StakeholderEngagement se : engagements) {
                    XWPFTableRow row = tableStake.createRow();
                    ensureCells(row, 5);

                    // Simple Columns
                    styleCell(row.getCell(0), checkNull(se.getGroupName()), false);
                    styleCell(row.getCell(1), checkNull(se.getIsVulnerable()), false);

                    // Complex Text Columns (Respect Newlines/Lists)
                    fillCellWithNewlines(row.getCell(2), checkNull(se.getChannels()));
                    fillCellWithNewlines(row.getCell(3), checkNull(se.getFrequency()));
                    fillCellWithNewlines(row.getCell(4), checkNull(se.getPurpose()));
                }
            } else {
                addDynamicRow(tableStake, new String[]{"-", "-", "-", "-", "-"});
            }

            // Note (After Table)
            addNote(doc, data.getStakeholderNote());

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP INDICATORS ---
            addSectionHeader(doc, "Leadership Indicators");

            // 1. Consultation Process
            addBoldText(doc, "1. Provide the processes for consultation between stakeholders and the Board on economic, environmental, and social topics or if consultation is delegated, how is feedback from such consultations provided to the Board.");

            XWPFParagraph pConsult = doc.createParagraph();
            XWPFRun rConsult = pConsult.createRun();
            rConsult.setFontFamily("Calibri");
            rConsult.setFontSize(10);

            // Use helper to respect newlines and ensure Left Alignment
            setTextWithBreaks(rConsult, checkNull(data.getConsultationProcessDetails()));
            pConsult.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 2: STAKEHOLDER CONSULTATION ---
            addBoldText(doc, "2. Whether stakeholder consultation is used to support the identification and management of environmental, and social topics (Yes / No). If so, provide details of instances as to how the inputs received from stakeholders on these topics were incorporated into policies and activities of the entity.");

            XWPFParagraph pConsult2 = doc.createParagraph();
            XWPFRun rConsult2 = pConsult2.createRun();
            rConsult2.setFontFamily("Calibri");
            rConsult2.setFontSize(10);

            String status2 = checkNull(data.getStakeholderConsultationUsed());
            String details2 = checkNull(data.getStakeholderConsultationDetails());

            // Format: "Yes, [Details]"
            String combinedText2 = status2;
            if (!details2.equals("-")) {
                combinedText2 += ", " + details2;
            }

            // Use helper to respect newlines and ensure Left Alignment
            setTextWithBreaks(rConsult2, combinedText2);
            pConsult2.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP 3: VULNERABLE GROUPS ---
            addBoldText(doc, "3. Provide details of instances of engagement with, and actions taken to, address the concerns of vulnerable/ marginalized stakeholder groups.");

            // 1. Intro Paragraph
            XWPFParagraph pIntro = doc.createParagraph();
            setTextWithBreaks(pIntro.createRun(), checkNull(data.getVulnerableGroupIntro()));
            pIntro.setSpacingAfter(100);

            // 2. Numbered List of Actions
            List<String> actionsP4 = data.getVulnerableGroupActions();
            if (actions != null && !actions.isEmpty()) {
                int count = 1;
                for (String action : actionsP4) {
                    XWPFParagraph pAction = doc.createParagraph();
                    pAction.setIndentationLeft(720);   // Indent
                    pAction.setIndentationHanging(360); // Hanging indent for number

                    XWPFRun rAction = pAction.createRun();
                    rAction.setFontFamily("Calibri");
                    rAction.setFontSize(10);
                    rAction.setText(count++ + ".  " + action);
                }
            }

            // 3. Concluding Paragraph
            XWPFParagraph pConcl = doc.createParagraph();
            pConcl.setSpacingBefore(100);
            setTextWithBreaks(pConcl.createRun(), checkNull(data.getVulnerableGroupConclusion()));

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION C: PRINCIPLE 5 =================
            XWPFParagraph pP5 = doc.createParagraph();
            pP5.setSpacingBefore(400);
            XWPFRun rP5 = pP5.createRun();
            rP5.setText("PRINCIPLE 5: Businesses should respect and promote human rights.");
            rP5.setBold(true);
            rP5.setFontSize(12);
            rP5.setColor(COLOR_THEME_GREEN);
            rP5.setFontFamily("Calibri");

            addBoldText(doc, "Essential Indicators");
            addBoldText(doc, "1. Employees and workers who have been provided training on human rights issues and policy(ies) of the entity, in the following format:");

            XWPFTable tableP5 = doc.createTable();
            tableP5.setWidth("100%");

            // --- ROW 0: FY HEADERS ---
            XWPFTableRow hRowP5_1 = tableP5.getRow(0);
            ensureCells(hRowP5_1, 7); // Category + (3 Current) + (3 Previous)

            styleCell(hRowP5_1.getCell(0), "Category", true);

            // Re-use FY variables
            String fyCurP5 = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrevP5 = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowP5_1.getCell(1), fyCurP5, true);
            mergeCellsHorizontal(hRowP5_1, 1, 3);

            styleCell(hRowP5_1.getCell(4), fyPrevP5, true);
            mergeCellsHorizontal(hRowP5_1, 4, 6);

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRowP5_2 = tableP5.createRow();
            ensureCells(hRowP5_2, 7);
            styleCell(hRowP5_2.getCell(0), "", true);

            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hRowP5_2.getCell(base), "Total (A)", true);
                styleCell(hRowP5_2.getCell(base+1), "No. Covered (B)", true);
                styleCell(hRowP5_2.getCell(base+2), "% (B/A)", true);
            }

            // --- EMPLOYEES ---
            addSectionTitleRow(tableP5, "EMPLOYEES", 7);

            addDynamicRow(tableP5, new String[]{ "Permanent",
                    checkNull(data.getHrEmpPermTotalCurr()), checkNull(data.getHrEmpPermCovCurr()), checkNull(data.getHrEmpPermPercCurr()),
                    checkNull(data.getHrEmpPermTotalPrev()), checkNull(data.getHrEmpPermCovPrev()), checkNull(data.getHrEmpPermPercPrev())
            });
            addDynamicRow(tableP5, new String[]{ "Other than permanent",
                    checkNull(data.getHrEmpTempTotalCurr()), checkNull(data.getHrEmpTempCovCurr()), checkNull(data.getHrEmpTempPercCurr()),
                    checkNull(data.getHrEmpTempTotalPrev()), checkNull(data.getHrEmpTempCovPrev()), checkNull(data.getHrEmpTempPercPrev())
            });
            // Total Row (Calculated in JS, saved here)
            XWPFTableRow rTotalEmp = tableP5.createRow();
            ensureCells(rTotalEmp, 7);
            styleCell(rTotalEmp.getCell(0), "Total Employees", true); // Bold
            styleCell(rTotalEmp.getCell(1), checkNull(data.getHrEmpGrandTotalCurr()), true);
            styleCell(rTotalEmp.getCell(2), checkNull(data.getHrEmpGrandCovCurr()), true);
            styleCell(rTotalEmp.getCell(3), checkNull(data.getHrEmpGrandPercCurr()), true);
            styleCell(rTotalEmp.getCell(4), checkNull(data.getHrEmpGrandTotalPrev()), true);
            styleCell(rTotalEmp.getCell(5), checkNull(data.getHrEmpGrandCovPrev()), true);
            styleCell(rTotalEmp.getCell(6), checkNull(data.getHrEmpGrandPercPrev()), true);

            // --- WORKERS ---
            addSectionTitleRow(tableP5, "WORKERS", 7);

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

            // Note
            addNote(doc, data.getHrTrainingNote());

            doc.createParagraph().createRun().addBreak();

            // --- 2. MINIMUM WAGES ---
            addBoldText(doc, "2. Details of minimum wages paid to employees and workers, in the following format:");

            XWPFTable tableMw = doc.createTable();
            tableMw.setWidth("100%");

            // --- ROW 0: FY HEADERS ---
            XWPFTableRow hRowMw1 = tableMw.getRow(0);
            ensureCells(hRowMw1, 11);

            styleCell(hRowMw1.getCell(0), "Category", true);

            String fyCurMw = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrevMw = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowMw1.getCell(1), fyCurMw, true);
            mergeCellsHorizontal(hRowMw1, 1, 5); // Merge 1-5

            styleCell(hRowMw1.getCell(6), fyPrevMw, true);
            mergeCellsHorizontal(hRowMw1, 6, 10); // Merge 6-10

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRowMw2 = tableMw.createRow();
            ensureCells(hRowMw2, 11);
            styleCell(hRowMw2.getCell(0), "", true);

            for(int i=0; i<2; i++) {
                int base = 1 + (i*5);
                styleCell(hRowMw2.getCell(base), "Total (A)", true);

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
                styleCell(hRowMw3.getCell(base+1), "No.", true);
                styleCell(hRowMw3.getCell(base+2), "%", true);
                styleCell(hRowMw3.getCell(base+3), "No.", true);
                styleCell(hRowMw3.getCell(base+4), "%", true);
            }

            // --- DATA ROWS ---

            // EMPLOYEES
            addSectionTitleRow(tableMw, "EMPLOYEES", 11);

            // Permanent
            addBoldRow(tableMw, "Permanent", 11);
            // Male
            addDynamicRow(tableMw, new String[]{ "Male",
                    checkNull(data.getMwEmpPermMaleTotalCurr()), checkNull(data.getMwEmpPermMaleEqNoCurr()), checkNull(data.getMwEmpPermMaleEqPercCurr()), checkNull(data.getMwEmpPermMaleMoreNoCurr()), checkNull(data.getMwEmpPermMaleMorePercCurr()),
                    checkNull(data.getMwEmpPermMaleTotalPrev()), checkNull(data.getMwEmpPermMaleEqNoPrev()), checkNull(data.getMwEmpPermMaleEqPercPrev()), checkNull(data.getMwEmpPermMaleMoreNoPrev()), checkNull(data.getMwEmpPermMaleMorePercPrev()) });
            // Female
            addDynamicRow(tableMw, new String[]{ "Female",
                    checkNull(data.getMwEmpPermFemTotalCurr()), checkNull(data.getMwEmpPermFemEqNoCurr()), checkNull(data.getMwEmpPermFemEqPercCurr()), checkNull(data.getMwEmpPermFemMoreNoCurr()), checkNull(data.getMwEmpPermFemMorePercCurr()),
                    checkNull(data.getMwEmpPermFemTotalPrev()), checkNull(data.getMwEmpPermFemEqNoPrev()), checkNull(data.getMwEmpPermFemEqPercPrev()), checkNull(data.getMwEmpPermFemMoreNoPrev()), checkNull(data.getMwEmpPermFemMorePercPrev()) });
            // Others
            addDynamicRow(tableMw, new String[]{ "Others",
                    checkNull(data.getMwEmpPermOthTotalCurr()), checkNull(data.getMwEmpPermOthEqNoCurr()), checkNull(data.getMwEmpPermOthEqPercCurr()), checkNull(data.getMwEmpPermOthMoreNoCurr()), checkNull(data.getMwEmpPermOthMorePercCurr()),
                    checkNull(data.getMwEmpPermOthTotalPrev()), checkNull(data.getMwEmpPermOthEqNoPrev()), checkNull(data.getMwEmpPermOthEqPercPrev()), checkNull(data.getMwEmpPermOthMoreNoPrev()), checkNull(data.getMwEmpPermOthMorePercPrev()) });

            // Other than Permanent
            addBoldRow(tableMw, "Other than Permanent", 11);
            // Male
            addDynamicRow(tableMw, new String[]{ "Male",
                    checkNull(data.getMwEmpTempMaleTotalCurr()), checkNull(data.getMwEmpTempMaleEqNoCurr()), checkNull(data.getMwEmpTempMaleEqPercCurr()), checkNull(data.getMwEmpTempMaleMoreNoCurr()), checkNull(data.getMwEmpTempMaleMorePercCurr()),
                    checkNull(data.getMwEmpTempMaleTotalPrev()), checkNull(data.getMwEmpTempMaleEqNoPrev()), checkNull(data.getMwEmpTempMaleEqPercPrev()), checkNull(data.getMwEmpTempMaleMoreNoPrev()), checkNull(data.getMwEmpTempMaleMorePercPrev()) });
            // Female
            addDynamicRow(tableMw, new String[]{ "Female",
                    checkNull(data.getMwEmpTempFemTotalCurr()), checkNull(data.getMwEmpTempFemEqNoCurr()), checkNull(data.getMwEmpTempFemEqPercCurr()), checkNull(data.getMwEmpTempFemMoreNoCurr()), checkNull(data.getMwEmpTempFemMorePercCurr()),
                    checkNull(data.getMwEmpTempFemTotalPrev()), checkNull(data.getMwEmpTempFemEqNoPrev()), checkNull(data.getMwEmpTempFemEqPercPrev()), checkNull(data.getMwEmpTempFemMoreNoPrev()), checkNull(data.getMwEmpTempFemMorePercPrev()) });
            // Others
            addDynamicRow(tableMw, new String[]{ "Others",
                    checkNull(data.getMwEmpTempOthTotalCurr()), checkNull(data.getMwEmpTempOthEqNoCurr()), checkNull(data.getMwEmpTempOthEqPercCurr()), checkNull(data.getMwEmpTempOthMoreNoCurr()), checkNull(data.getMwEmpTempOthMorePercCurr()),
                    checkNull(data.getMwEmpTempOthTotalPrev()), checkNull(data.getMwEmpTempOthEqNoPrev()), checkNull(data.getMwEmpTempOthEqPercPrev()), checkNull(data.getMwEmpTempOthMoreNoPrev()), checkNull(data.getMwEmpTempOthMorePercPrev()) });

            // WORKERS
            addSectionTitleRow(tableMw, "WORKERS", 11);

            // Permanent
            addBoldRow(tableMw, "Permanent", 11);
            // Male
            addDynamicRow(tableMw, new String[]{ "Male",
                    checkNull(data.getMwWorkPermMaleTotalCurr()), checkNull(data.getMwWorkPermMaleEqNoCurr()), checkNull(data.getMwWorkPermMaleEqPercCurr()), checkNull(data.getMwWorkPermMaleMoreNoCurr()), checkNull(data.getMwWorkPermMaleMorePercCurr()),
                    checkNull(data.getMwWorkPermMaleTotalPrev()), checkNull(data.getMwWorkPermMaleEqNoPrev()), checkNull(data.getMwWorkPermMaleEqPercPrev()), checkNull(data.getMwWorkPermMaleMoreNoPrev()), checkNull(data.getMwWorkPermMaleMorePercPrev()) });
            // Female
            addDynamicRow(tableMw, new String[]{ "Female",
                    checkNull(data.getMwWorkPermFemTotalCurr()), checkNull(data.getMwWorkPermFemEqNoCurr()), checkNull(data.getMwWorkPermFemEqPercCurr()), checkNull(data.getMwWorkPermFemMoreNoCurr()), checkNull(data.getMwWorkPermFemMorePercCurr()),
                    checkNull(data.getMwWorkPermFemTotalPrev()), checkNull(data.getMwWorkPermFemEqNoPrev()), checkNull(data.getMwWorkPermFemEqPercPrev()), checkNull(data.getMwWorkPermFemMoreNoPrev()), checkNull(data.getMwWorkPermFemMorePercPrev()) });
            // Others
            addDynamicRow(tableMw, new String[]{ "Others",
                    checkNull(data.getMwWorkPermOthTotalCurr()), checkNull(data.getMwWorkPermOthEqNoCurr()), checkNull(data.getMwWorkPermOthEqPercCurr()), checkNull(data.getMwWorkPermOthMoreNoCurr()), checkNull(data.getMwWorkPermOthMorePercCurr()),
                    checkNull(data.getMwWorkPermOthTotalPrev()), checkNull(data.getMwWorkPermOthEqNoPrev()), checkNull(data.getMwWorkPermOthEqPercPrev()), checkNull(data.getMwWorkPermOthMoreNoPrev()), checkNull(data.getMwWorkPermOthMorePercPrev()) });

            // Other than Permanent
            addBoldRow(tableMw, "Other than Permanent", 11);
            // Male
            addDynamicRow(tableMw, new String[]{ "Male",
                    checkNull(data.getMwWorkTempMaleTotalCurr()), checkNull(data.getMwWorkTempMaleEqNoCurr()), checkNull(data.getMwWorkTempMaleEqPercCurr()), checkNull(data.getMwWorkTempMaleMoreNoCurr()), checkNull(data.getMwWorkTempMaleMorePercCurr()),
                    checkNull(data.getMwWorkTempMaleTotalPrev()), checkNull(data.getMwWorkTempMaleEqNoPrev()), checkNull(data.getMwWorkTempMaleEqPercPrev()), checkNull(data.getMwWorkTempMaleMoreNoPrev()), checkNull(data.getMwWorkTempMaleMorePercPrev()) });
            // Female
            addDynamicRow(tableMw, new String[]{ "Female",
                    checkNull(data.getMwWorkTempFemTotalCurr()), checkNull(data.getMwWorkTempFemEqNoCurr()), checkNull(data.getMwWorkTempFemEqPercCurr()), checkNull(data.getMwWorkTempFemMoreNoCurr()), checkNull(data.getMwWorkTempFemMorePercCurr()),
                    checkNull(data.getMwWorkTempFemTotalPrev()), checkNull(data.getMwWorkTempFemEqNoPrev()), checkNull(data.getMwWorkTempFemEqPercPrev()), checkNull(data.getMwWorkTempFemMoreNoPrev()), checkNull(data.getMwWorkTempFemMorePercPrev()) });
            // Others
            addDynamicRow(tableMw, new String[]{ "Others",
                    checkNull(data.getMwWorkTempOthTotalCurr()), checkNull(data.getMwWorkTempOthEqNoCurr()), checkNull(data.getMwWorkTempOthEqPercCurr()), checkNull(data.getMwWorkTempOthMoreNoCurr()), checkNull(data.getMwWorkTempOthMorePercCurr()),
                    checkNull(data.getMwWorkTempOthTotalPrev()), checkNull(data.getMwWorkTempOthEqNoPrev()), checkNull(data.getMwWorkTempOthEqPercPrev()), checkNull(data.getMwWorkTempOthMoreNoPrev()), checkNull(data.getMwWorkTempOthMorePercPrev()) });

            // Note
            addNote(doc, data.getMinWageNote());

            doc.createParagraph().createRun().addBreak();

            // --- 3. REMUNERATION ---
            addBoldText(doc, "3. Details of remuneration/salary/wages, in the following format:");
            addBoldText(doc, "a. Median remuneration / wages");

            XWPFTable tableRem = doc.createTable();
            tableRem.setWidth("100%");

            // --- ROW 0: GENDER HEADERS (Merged) ---
            XWPFTableRow hRowRem1 = tableRem.getRow(0);
            ensureCells(hRowRem1, 5); // Label + (2 Male) + (2 Female)

            styleCell(hRowRem1.getCell(0), "Category", true);

            styleCell(hRowRem1.getCell(1), "Male", true);
            mergeCellsHorizontal(hRowRem1, 1, 2); // Merge Cols 1-2

            styleCell(hRowRem1.getCell(3), "Female", true);
            mergeCellsHorizontal(hRowRem1, 3, 4); // Merge Cols 3-4

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRowRem2 = tableRem.createRow();
            ensureCells(hRowRem2, 5);

            styleCell(hRowRem2.getCell(0), "", true); // Empty

            styleCell(hRowRem2.getCell(1), "Number", true);
            styleCell(hRowRem2.getCell(2), "Median Remuneration/ Salary/ Wages", true);

            styleCell(hRowRem2.getCell(3), "Number", true);
            styleCell(hRowRem2.getCell(4), "Median Remuneration/ Salary/ Wages", true);

            // --- DATA ROWS ---
            // 1. BoD
            addDynamicRow(tableRem, new String[]{
                    "Board of Directors (BoD)",
                    checkNull(data.getRemBodMaleNum()), checkNull(data.getRemBodMaleMedian()),
                    checkNull(data.getRemBodFemNum()), checkNull(data.getRemBodFemMedian())
            });

            // 2. KMP
            addDynamicRow(tableRem, new String[]{
                    "Key Managerial Personnel",
                    checkNull(data.getRemKmpMaleNum()), checkNull(data.getRemKmpMaleMedian()),
                    checkNull(data.getRemKmpFemNum()), checkNull(data.getRemKmpFemMedian())
            });

            // 3. Employees
            addDynamicRow(tableRem, new String[]{
                    "Employees other than BoD and KMP",
                    checkNull(data.getRemEmpMaleNum()), checkNull(data.getRemEmpMaleMedian()),
                    checkNull(data.getRemEmpFemNum()), checkNull(data.getRemEmpFemMedian())
            });

            // 4. Workers
            addDynamicRow(tableRem, new String[]{
                    "Workers",
                    checkNull(data.getRemWorkMaleNum()), checkNull(data.getRemWorkMaleMedian()),
                    checkNull(data.getRemWorkFemNum()), checkNull(data.getRemWorkFemMedian())
            });

            // Note
            addNote(doc, data.getRemunerationNote());

            doc.createParagraph().createRun().addBreak();

            // --- 2.b GROSS WAGES ---
            addBoldText(doc, "b. Gross wages paid to females as % of total wages paid by the entity, in the following format:");

            XWPFTable tableGw = doc.createTable();
            tableGw.setWidth("100%");

            // Set Widths (Label 60%, Data 20% each)
            setColumnWidths(tableGw, 6000, 2000);

            // Header Row
            XWPFTableRow hRowGw = tableGw.getRow(0);
            ensureCells(hRowGw, 3);
            styleCell(hRowGw.getCell(0), "", true);

            // Reuse FY
            String fyCurGw = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrevGw = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowGw.getCell(1), fyCurGw, true);
            styleCell(hRowGw.getCell(2), fyPrevGw, true);

            // Data Row
            addDynamicRow(tableGw, new String[]{
                    "Gross wages paid to females as % of total wages",
                    checkNull(data.getGrossWagesFemalePercCurr()) + "%",
                    checkNull(data.getGrossWagesFemalePercPrev()) + "%"
            });

            // Note
            addNote(doc, data.getGrossWagesNote());

            doc.createParagraph().createRun().addBreak();

            // --- 4. HUMAN RIGHTS FOCAL POINT ---
            addBoldText(doc, "4. Do you have a focal point (Individual/Committee) responsible for addressing human rights impacts or issues caused or contributed to by the business? (Yes/No)");

            XWPFParagraph pHrFocal = doc.createParagraph();
            XWPFRun rHrFocal = pHrFocal.createRun();
            rHrFocal.setFontFamily("Calibri");
            rHrFocal.setFontSize(10);

            String hrStatus = checkNull(data.getHumanRightsFocalPoint());
            String hrDetails = checkNull(data.getHumanRightsFocalDetails());

            // Format: "Yes, [Details]"
            String combinedHrText = hrStatus;
            if (!hrDetails.equals("-")) {
                combinedHrText += ", " + hrDetails;
            }

            setTextWithBreaks(rHrFocal, combinedHrText);
            pHrFocal.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 5. GRIEVANCE MECHANISMS ---
            addBoldText(doc, "5. Describe the internal mechanisms in place to redress grievances related to human rights issues.");

            XWPFParagraph pGriev = doc.createParagraph();
            XWPFRun rGriev = pGriev.createRun();
            rGriev.setFontFamily("Calibri");
            rGriev.setFontSize(10);

            // Use helper to respect newlines
            setTextWithBreaks(rGriev, checkNull(data.getHumanRightsGrievanceMechanism()));
            pGriev.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 6. COMPLAINTS (EMPLOYEES/WORKERS) ---
            addBoldText(doc, "6. Number of Complaints on the following made by employees and workers:");

            XWPFTable tableComp = doc.createTable();
            tableComp.setWidth("100%");

            // --- ROW 0: FY HEADERS ---
            XWPFTableRow hRowComp1 = tableComp.getRow(0);
            ensureCells(hRowComp1, 7); // Category + (3 Current) + (3 Previous)

            styleCell(hRowComp1.getCell(0), "", true); // Top-left empty

            // Reuse FY
            String fyCurComp = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrevComp = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowComp1.getCell(1), fyCurComp, true);
            mergeCellsHorizontal(hRowComp1, 1, 3); // Merge 1-3

            styleCell(hRowComp1.getCell(4), fyPrevComp, true);
            mergeCellsHorizontal(hRowComp1, 4, 6); // Merge 4-6

            // --- ROW 1: METRIC HEADERS ---
            XWPFTableRow hRowComp2 = tableComp.createRow();
            ensureCells(hRowComp2, 7);
            styleCell(hRowComp2.getCell(0), "", true);

            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hRowComp2.getCell(base), "Filed during the year", true);
                styleCell(hRowComp2.getCell(base+1), "Pending resolution at end of year", true);
                styleCell(hRowComp2.getCell(base+2), "Remarks", true);
            }

            // --- DATA ROWS ---

            // 1. Sexual Harassment
            addDynamicRow(tableComp, new String[]{
                    "Sexual Harassment",
                    checkNull(data.getCompShFiledCurr()), checkNull(data.getCompShPendingCurr()), checkNull(data.getCompShRemarksCurr()),
                    checkNull(data.getCompShFiledPrev()), checkNull(data.getCompShPendingPrev()), checkNull(data.getCompShRemarksPrev())
            });

            // 2. Discrimination
            addDynamicRow(tableComp, new String[]{
                    "Discrimination at workplace",
                    checkNull(data.getCompDiscrimFiledCurr()), checkNull(data.getCompDiscrimPendingCurr()), checkNull(data.getCompDiscrimRemarksCurr()),
                    checkNull(data.getCompDiscrimFiledPrev()), checkNull(data.getCompDiscrimPendingPrev()), checkNull(data.getCompDiscrimRemarksPrev())
            });

            // 3. Child Labour
            addDynamicRow(tableComp, new String[]{
                    "Child Labour",
                    checkNull(data.getCompChildFiledCurr()), checkNull(data.getCompChildPendingCurr()), checkNull(data.getCompChildRemarksCurr()),
                    checkNull(data.getCompChildFiledPrev()), checkNull(data.getCompChildPendingPrev()), checkNull(data.getCompChildRemarksPrev())
            });

            // 4. Forced Labour
            addDynamicRow(tableComp, new String[]{
                    "Forced Labour/Involuntary Labour",
                    checkNull(data.getCompForcedFiledCurr()), checkNull(data.getCompForcedPendingCurr()), checkNull(data.getCompForcedRemarksCurr()),
                    checkNull(data.getCompForcedFiledPrev()), checkNull(data.getCompForcedPendingPrev()), checkNull(data.getCompForcedRemarksPrev())
            });

            // 5. Wages
            addDynamicRow(tableComp, new String[]{
                    "Wages",
                    checkNull(data.getCompWagesFiledCurr()), checkNull(data.getCompWagesPendingCurr()), checkNull(data.getCompWagesRemarksCurr()),
                    checkNull(data.getCompWagesFiledPrev()), checkNull(data.getCompWagesPendingPrev()), checkNull(data.getCompWagesRemarksPrev())
            });

            // 6. Other
            addDynamicRow(tableComp, new String[]{
                    "Other human rights related issues",
                    checkNull(data.getCompOtherHrFiledCurr()), checkNull(data.getCompOtherHrPendingCurr()), checkNull(data.getCompOtherHrRemarksCurr()),
                    checkNull(data.getCompOtherHrFiledPrev()), checkNull(data.getCompOtherHrPendingPrev()), checkNull(data.getCompOtherHrRemarksPrev())
            });

            doc.createParagraph().createRun().addBreak();

            // --- 7. POSH COMPLAINTS ---
            addBoldText(doc, "7. Complaints filed under the Sexual Harassment of Women at Workplace (Prevention, Prohibition and Redressal) Act, 2013, in the following format:");

            XWPFTable tablePosh = doc.createTable();
            tablePosh.setWidth("100%");

            // Set Widths (Label 60%, Years 20% each)
            setColumnWidths(tablePosh, 6000, 2000);

            // Header
            XWPFTableRow hRowPosh = tablePosh.getRow(0);
            ensureCells(hRowPosh, 3);
            styleCell(hRowPosh.getCell(0), "", true);

            // Reuse FY
            String fyCurPosh = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrevPosh = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";

            styleCell(hRowPosh.getCell(1), fyCurPosh, true);
            styleCell(hRowPosh.getCell(2), fyPrevPosh, true);

            // Data Rows
            addDynamicRow(tablePosh, new String[]{
                    "Total Complaints reported under Sexual Harassment of Women at Workplace Act, 2013 (POSH)",
                    checkNull(data.getPoshTotalCurr()),
                    checkNull(data.getPoshTotalPrev())
            });

            addDynamicRow(tablePosh, new String[]{
                    "Complaints on POSH as a % of female employees / workers",
                    checkNull(data.getPoshPercCurr()) + "%",
                    checkNull(data.getPoshPercPrev()) + "%"
            });

            addDynamicRow(tablePosh, new String[]{
                    "Complaints on POSH upheld",
                    checkNull(data.getPoshUpheldCurr()),
                    checkNull(data.getPoshUpheldPrev())
            });

            // Note
            addNote(doc, data.getPoshNote());

            doc.createParagraph().createRun().addBreak();

            // --- 8. PROTECTION MECHANISMS ---
            addBoldText(doc, "8. Mechanisms to prevent adverse consequences to the complainant in discrimination and harassment cases.");

            // Intro Paragraph
            XWPFParagraph pProt = doc.createParagraph();
            XWPFRun rProt = pProt.createRun();
            rProt.setFontFamily("Calibri");
            rProt.setFontSize(10);
            setTextWithBreaks(rProt, checkNull(data.getProtectionMechanismsIntro()));
            pProt.setSpacingAfter(100);

            // Numbered List (i., ii., iii.)
            List<String> mechs = data.getProtectionMechanismsList();
            if (mechs != null && !mechs.isEmpty()) {
                int count = 1;
                for (String mech : mechs) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);   // Indent
                    pItem.setIndentationHanging(360); // Hanging indent for number

                    XWPFRun rItem = pItem.createRun();
                    rItem.setFontFamily("Calibri");
                    rItem.setFontSize(10);
                    // Use romanToDecimal helper (assuming you have a simple roman converter, or just use i. ii.)
                    // Simple approach:
                    String label = intToRomanLower(count++) + ".  ";
                    rItem.setText(label + mech);
                }
            } else {
                XWPFParagraph pNone = doc.createParagraph();
                pNone.createRun().setText("No specific mechanisms listed.");
            }

            doc.createParagraph().createRun().addBreak();

            // --- 9. CONTRACT REQUIREMENTS ---
            addBoldText(doc, "9. Do human rights requirements form part of your business agreements and contracts? (Yes/No)");

            XWPFParagraph pCont = doc.createParagraph();
            XWPFRun rCont = pCont.createRun();
            rCont.setFontFamily("Calibri");
            rCont.setFontSize(10);

            String contStatus = checkNull(data.getHumanRightsContracts());
            String contDetails = checkNull(data.getHumanRightsContractsDetails());

            // Format: "Yes, [Details]"
            String combinedContText = contStatus;
            if (!contDetails.equals("-")) {
                combinedContText += ", " + contDetails;
            }
            setTextWithBreaks(rCont, combinedContText);
            pCont.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- 10. ASSESSMENTS (HUMAN RIGHTS) ---
            addBoldText(doc, "10. Assessments for the year:");

            XWPFTable table10 = doc.createTable();
            table10.setWidth("100%");

            // Set Widths: Category (40%), Percentage (60%)
            setColumnWidths(table10, 4000, 6000);

            // Header
            XWPFTableRow hRow10 = table10.getRow(0);
            ensureCells(hRow10, 2);
            styleCell(hRow10.getCell(0), "", true);
            styleCell(hRow10.getCell(1), "% of your plants and offices that were assessed (by entity or statutory authorities or third parties)", true);

            // Data Rows
            addRow(table10, "Child labour", checkNull(data.getAssessChildLabourPerc()) + "%");
            addRow(table10, "Forced/involuntary labour", checkNull(data.getAssessForcedLabourPerc()) + "%");
            addRow(table10, "Sexual harassment", checkNull(data.getAssessSexualHarassmentPerc()) + "%");
            addRow(table10, "Discrimination at workplace", checkNull(data.getAssessDiscriminationPerc()) + "%");
            addRow(table10, "Wages", checkNull(data.getAssessWagesPerc()) + "%");
            addRow(table10, "Others – please specify", checkNull(data.getAssessOthersPerc()) + "%");

            // Note
            addNote(doc, data.getAssessmentsP5Note());

            doc.createParagraph().createRun().addBreak();

            // --- 11. CORRECTIVE ACTIONS (HUMAN RIGHTS) ---
            addBoldText(doc, "11. Provide details of any corrective actions taken or underway to address significant risks / concerns arising from the assessments at Question 10 above.");

            // 1. Intro Paragraph
            XWPFParagraph pCorrIntro = doc.createParagraph();
            XWPFRun rCorrIntro = pCorrIntro.createRun();
            rCorrIntro.setFontFamily("Calibri");
            rCorrIntro.setFontSize(10);
            setTextWithBreaks(rCorrIntro, checkNull(data.getAssessCorrectiveIntro()));
            pCorrIntro.setSpacingAfter(100);

            // 2. Numbered List (i., ii., iii.)
            List<String> corrActions = data.getAssessCorrectiveActions();
            if (corrActions != null && !corrActions.isEmpty()) {
                int count = 1;
                for (String action : corrActions) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);   // Indent body
                    pItem.setIndentationHanging(360); // Hanging indent for numbering
                    pItem.setSpacingAfter(0); // Tight spacing

                    XWPFRun rItem = pItem.createRun();
                    rItem.setFontFamily("Calibri");
                    rItem.setFontSize(10);

                    // Roman Numeral Label
                    String label = intToRomanLower(count++) + ".  ";
                    rItem.setText(label + action);
                }
            } else {
                XWPFParagraph pNone = doc.createParagraph();
                pNone.createRun().setText("No specific corrective actions listed.");
            }

            doc.createParagraph().createRun().addBreak();

            // --- PRINCIPLE 5 LEADERSHIP INDICATORS ---
            addSectionHeader(doc, "Leadership Indicators");

            // Q1
            addBoldText(doc, "1. Details of a business process being modified / introduced as a result of addressing human rights grievances/complaints.");

            // Intro Text
            XWPFParagraph p5L1 = doc.createParagraph();
            setTextWithBreaks(p5L1.createRun(), checkNull(data.getP5LeadProcessModIntro()));
            p5L1.setSpacingAfter(100);

            // Bullet List
            List<String> modList = data.getP5LeadProcessModList();
            if (modList != null && !modList.isEmpty()) {
                int count = 1;
                for (String mod : modList) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);
                    pItem.setIndentationHanging(360);
                    pItem.setSpacingAfter(0);

                    XWPFRun rItem = pItem.createRun();
                    rItem.setFontFamily("Calibri");
                    rItem.setFontSize(10);
                    rItem.setText(intToRomanLower(count++) + ".  " + mod);
                }
            } else {
                XWPFParagraph pNone = doc.createParagraph();
                pNone.createRun().setText("No specific modifications listed.");
            }

            doc.createParagraph().createRun().addBreak();

            // Q2
            addBoldText(doc, "2. Details of the scope and coverage of any Human rights due-diligence conducted.");

            // Scope Text
            XWPFParagraph p5L2 = doc.createParagraph();
            setTextWithBreaks(p5L2.createRun(), checkNull(data.getP5LeadDueDiligenceScope()));
            p5L2.setSpacingAfter(200);

            // Issues Identified
            List<String> issuesList = data.getP5LeadDueDiligenceIssues();
            if (issuesList != null && !issuesList.isEmpty()) {
                addBoldText(doc, "For the due diligence exercise, the following Human Rights issues have been identified:");
                int count = 1;
                for (String issue : issuesList) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);
                    pItem.setIndentationHanging(360);
                    pItem.setSpacingAfter(0);

                    XWPFRun rItem = pItem.createRun();
                    rItem.setFontFamily("Calibri");
                    rItem.setFontSize(10);
                    rItem.setText(intToRomanLower(count++) + ".  " + issue);
                }
                doc.createParagraph().createRun().addBreak();
            }

            // Rights Holders
            List<String> holdersList = data.getP5LeadDueDiligenceHolders();
            if (holdersList != null && !holdersList.isEmpty()) {
                addBoldText(doc, "The entity has also identified the following rights holders:");
                int count = 1;
                for (String holder : holdersList) {
                    XWPFParagraph pItem = doc.createParagraph();
                    pItem.setIndentationLeft(720);
                    pItem.setIndentationHanging(360);
                    pItem.setSpacingAfter(0);

                    XWPFRun rItem = pItem.createRun();
                    rItem.setFontFamily("Calibri");
                    rItem.setFontSize(10);
                    rItem.setText(intToRomanLower(count++) + ".  " + holder);
                }
            }

            doc.createParagraph().createRun().addBreak();

            // Q3: Accessibility
            addBoldText(doc, "3. Is the premise/office of the entity accessible to differently abled visitors, as per the requirements of the Rights of Persons with Disabilities Act, 2016?");

            XWPFParagraph p5L3 = doc.createParagraph();
            setTextWithBreaks(p5L3.createRun(), checkNull(data.getP5LeadPremisesAccess()));
            p5L3.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP Q4: VALUE CHAIN ASSESSMENT ---
            addBoldText(doc, "4. Details on assessment of value chain partners:");

            XWPFTable tableP5L4 = doc.createTable();
            tableP5L4.setWidth("100%");

            // Set Column Widths (Approx 35% for labels, 65% for details)
            setColumnWidths(tableP5L4, 3500, 6500);

            // Header Row
            XWPFTableRow hRowP5L4 = tableP5L4.getRow(0);
            ensureCells(hRowP5L4, 2);
            styleCell(hRowP5L4.getCell(0), "Human Rights issues", true);
            styleCell(hRowP5L4.getCell(1), "% of value chain partners (by value of business done with such partners) that were assessed", true);

            // The specific categories required by the format
            String[] issuesp5 = {
                    "Child Labour",
                    "Forced/Involuntary Labour",
                    "Sexual Harassment",
                    "Discrimination at workplace",
                    "Wages",
                    "Others"
            };

            // Create rows with Vertical Merge on the 2nd column
            for (int i = 0; i < issuesp5.length; i++) {
                XWPFTableRow row = tableP5L4.createRow();
                ensureCells(row, 2);

                // Column 1: The Category Label
                styleCell(row.getCell(0), issuesp5[i], false);

                // Column 2: The Merged Content
                XWPFTableCell rightCell = row.getCell(1);

                if (i == 0) {
                    // First row: START the merge and set the text
                    rightCell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
                    // Use helper to handle newlines correctly within the cell
                    fillCellWithNewlines(rightCell, checkNull(data.getP5LeadValueChainAssessment()));
                } else {
                    // Subsequent rows: CONTINUE the merge (no text needed here)
                    rightCell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
                }
            }

            doc.createParagraph().createRun().addBreak();

            // --- LEADERSHIP Q5: CORRECTIVE ACTIONS (VALUE CHAIN) ---
            addBoldText(doc, "5. Provide details of any corrective actions taken or underway to address significant risks/concerns arising from the assessments at Question 4 above.");

            XWPFParagraph p5L5 = doc.createParagraph();
            setTextWithBreaks(p5L5.createRun(), checkNull(data.getP5LeadValueChainCorrectiveActions()));
            p5L5.setSpacingAfter(200);

            doc.createParagraph().createRun().addBreak();

            // =================================================================
            // SECTION C: PRINCIPLE 6 (ENVIRONMENT)
            // =================================================================
            XWPFParagraph pP6 = doc.createParagraph();
            pP6.setPageBreak(true); // Start P6 on new page
            pP6.setSpacingBefore(300);
            XWPFRun rP6 = pP6.createRun();
            rP6.setText("PRINCIPLE 6: Businesses should respect and make efforts to protect and restore the environment.");
            rP6.setBold(true);
            rP6.setFontSize(12);
            rP6.setColor(COLOR_THEME_GREEN);
            rP6.setFontFamily("Calibri");

            addBoldText(doc, "Essential Indicators");

            // --- 1. ENERGY ---
            addBoldText(doc, "1. Details of total energy consumption (in Joules or multiples) and energy intensity:");

            XWPFTable tableEnergy = doc.createTable();
            tableEnergy.setWidth("100%");
            setColumnWidths(tableEnergy, 4000, 6000); // 40% label, 60% data (split later)

            // Header
            XWPFTableRow hEn = tableEnergy.getRow(0);
            ensureCells(hEn, 3);
            styleCell(hEn.getCell(0), "Parameter", true);
            String fyCurp6 = (data.getFyCurrent() != null) ? data.getFyCurrent() : "Current FY";
            String fyPrevp6 = (data.getFyPrevious() != null) ? data.getFyPrevious() : "Previous FY";
            styleCell(hEn.getCell(1), fyCurp6, true);
            styleCell(hEn.getCell(2), fyPrevp6, true);

            // Data Rows
            addRow(tableEnergy, "Total electricity consumption (A)", data.getP6ElecConsumCurr(), data.getP6ElecConsumPrev());
            addRow(tableEnergy, "Total fuel consumption (B)", data.getP6FuelConsumCurr(), data.getP6FuelConsumPrev());
            addRow(tableEnergy, "Energy consumption through other sources (C)", data.getP6EnergyOtherCurr(), data.getP6EnergyOtherPrev());

            // Total Row (Bold)
            XWPFTableRow rTotalEn = tableEnergy.createRow();
            ensureCells(rTotalEn, 3);
            styleCell(rTotalEn.getCell(0), "Total Energy Consumption (A+B+C)", true);
            styleCell(rTotalEn.getCell(1), checkNull(data.getP6EnergyTotalCurr()), false);
            styleCell(rTotalEn.getCell(2), checkNull(data.getP6EnergyTotalPrev()), false);

            addRow(tableEnergy, "Energy intensity per rupee of turnover", data.getP6EnergyIntensityCurr(), data.getP6EnergyIntensityPrev());

            addNote(doc, data.getP6EnergyNote());
            doc.createParagraph().createRun().addBreak();

            // --- 2. EMISSIONS ---
            addBoldText(doc, "2. Details of following Disclosures related to Scope 1 and Scope 2 emissions:");
            XWPFTable tableEm = doc.createTable();
            tableEm.setWidth("100%");

            // Header
            XWPFTableRow hEm = tableEm.getRow(0);
            ensureCells(hEm, 3);
            styleCell(hEm.getCell(0), "Parameter", true);
            styleCell(hEm.getCell(1), fyCur, true);
            styleCell(hEm.getCell(2), fyPrev, true);

            addRow(tableEm, "Total Scope 1 emissions (Metric tonnes CO2e)", data.getP6Scope1Curr(), data.getP6Scope1Prev());
            addRow(tableEm, "Total Scope 2 emissions (Metric tonnes CO2e)", data.getP6Scope2Curr(), data.getP6Scope2Prev());

            // Total Row
            XWPFTableRow rTotalEm = tableEm.createRow();
            ensureCells(rTotalEm, 3);
            styleCell(rTotalEm.getCell(0), "Total Scope 1 and Scope 2 emissions", true);
            styleCell(rTotalEm.getCell(1), checkNull(data.getP6ScopeTotalCurr()), false);
            styleCell(rTotalEm.getCell(2), checkNull(data.getP6ScopeTotalPrev()), false);

            addRow(tableEm, "Emission intensity per rupee of turnover", data.getP6EmissionIntensityCurr(), data.getP6EmissionIntensityPrev());

            addNote(doc, data.getP6EmissionNote());
            doc.createParagraph().createRun().addBreak();

            // --- 3. WATER ---
            addBoldText(doc, "3. Water Withdrawal and Consumption:");
            XWPFTable tableWat = doc.createTable();
            tableWat.setWidth("100%");

            XWPFTableRow hWat = tableWat.getRow(0);
            ensureCells(hWat, 3);
            styleCell(hWat.getCell(0), "Parameter", true);
            styleCell(hWat.getCell(1), fyCur, true);
            styleCell(hWat.getCell(2), fyPrev, true);

            addRow(tableWat, "Total water withdrawal (in kilolitres)", data.getP6WaterWithdrawalCurr(), data.getP6WaterWithdrawalPrev());
            addRow(tableWat, "Total water consumption (in kilolitres)", data.getP6WaterConsumCurr(), data.getP6WaterConsumPrev());
            addRow(tableWat, "Water intensity per rupee of turnover", data.getP6WaterIntensityCurr(), data.getP6WaterIntensityPrev());

            addNote(doc, data.getP6WaterNote());
            doc.createParagraph().createRun().addBreak();

            // --- 4. WASTE MANAGEMENT ---
            addBoldText(doc, "4. Details of Waste Management (Metric Tonnes):");
            XWPFTable tableWaste = doc.createTable();
            tableWaste.setWidth("100%");

            // Header 1 (Merged)
            XWPFTableRow hW1 = tableWaste.getRow(0);
            ensureCells(hW1, 7);
            styleCell(hW1.getCell(0), "Category", true);
            styleCell(hW1.getCell(1), fyCur, true);
            mergeCellsHorizontal(hW1, 1, 3);
            styleCell(hW1.getCell(4), fyPrev, true);
            mergeCellsHorizontal(hW1, 4, 6);

            // Header 2
            XWPFTableRow hW2 = tableWaste.createRow();
            ensureCells(hW2, 7);
            styleCell(hW2.getCell(0), "", true);
            for(int i=0; i<2; i++) {
                int base = 1 + (i*3);
                styleCell(hW2.getCell(base), "Generated", true);
                styleCell(hW2.getCell(base+1), "Recycled", true);
                styleCell(hW2.getCell(base+2), "Disposed", true);
            }

            // Data Rows
            List<BrsrReportRequest.WasteManagementRow> wList = data.getP6WasteManagementList();
            if (wList != null && !wList.isEmpty()) {
                for (BrsrReportRequest.WasteManagementRow w : wList) {
                    addDynamicRow(tableWaste, new String[]{
                            checkNull(w.getCategory()),
                            checkNull(w.getCurrGenerated()), checkNull(w.getCurrRecycled()), checkNull(w.getCurrDisposed()),
                            checkNull(w.getPrevGenerated()), checkNull(w.getPrevRecycled()), checkNull(w.getPrevDisposed())
                    });
                }
            } else {
                addDynamicRow(tableWaste, new String[]{"-", "-", "-", "-", "-", "-", "-"});
            }

            addNote(doc, data.getP6WasteNote());
            doc.createParagraph().createRun().addBreak();

            // =================================================================
            // SECTION C: PRINCIPLE 7 (PUBLIC POLICY)
            // =================================================================
            XWPFParagraph pP7 = doc.createParagraph();
            pP7.setPageBreak(true);
            pP7.setSpacingBefore(300);
            XWPFRun rP7 = pP7.createRun();
            rP7.setText("PRINCIPLE 7: Businesses, when engaging in influencing public and regulatory policy, should do so in a manner that is responsible and transparent.");
            rP7.setBold(true);
            rP7.setFontSize(12);
            rP7.setColor(COLOR_THEME_GREEN);
            rP7.setFontFamily("Calibri");

            addBoldText(doc, "Essential Indicators");
            addBoldText(doc, "1. a. Number of affiliations with trade and industry chambers/associations.");
            XWPFParagraph p7a = doc.createParagraph();
            p7a.createRun().setText(checkNull(data.getP7AffiliationsCount()));

            addBoldText(doc, "b. List the top 10 trade and industry chambers/associations the entity is a member of/affiliated to.");
            XWPFTable tableP7 = doc.createTable();
            tableP7.setWidth("100%");
            XWPFTableRow hP7 = tableP7.getRow(0);
            ensureCells(hP7, 3);
            styleCell(hP7.getCell(0), "S. No.", true);
            styleCell(hP7.getCell(1), "Name of the Association", true);
            styleCell(hP7.getCell(2), "Reach (State/National)", true);

            List<BrsrReportRequest.TradeAssociation> assocs = data.getP7TradeAssociations();
            if (assocs != null && !assocs.isEmpty()) {
                int count = 1;
                for (BrsrReportRequest.TradeAssociation ta : assocs) {
                    addDynamicRow(tableP7, new String[]{ String.valueOf(count++), checkNull(ta.getName()), checkNull(ta.getReach()) });
                }
            } else {
                addDynamicRow(tableP7, new String[]{"-", "-", "-"});
            }

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "2. Provide details of corrective action taken or underway on any issues related to anti-competitive conduct.");
            XWPFParagraph p7b = doc.createParagraph();
            setTextWithBreaks(p7b.createRun(), checkNull(data.getP7AntiCompetitiveDetails()));

            addSectionHeader(doc, "Leadership Indicators");
            addBoldText(doc, "1. Details of public policy positions advocated by the entity:");

            XWPFTable tablePol7 = doc.createTable();
            tablePol7.setWidth("100%");
            XWPFTableRow hPol7 = tablePol7.getRow(0);
            ensureCells(hPol7, 3);
            styleCell(hPol7.getCell(0), "Public Policy Advocated", true);
            styleCell(hPol7.getCell(1), "Method Resorted", true);
            styleCell(hPol7.getCell(2), "Web Link / Info", true);

            List<BrsrReportRequest.PublicPolicyPosition> pols = data.getP7PublicPolicyPositions();
            if(pols != null && !pols.isEmpty()){
                for(BrsrReportRequest.PublicPolicyPosition p : pols){
                    XWPFTableRow r = tablePol7.createRow();
                    ensureCells(r, 3);
                    fillCellWithNewlines(r.getCell(0), p.getPolicyAdvocated());
                    fillCellWithNewlines(r.getCell(1), p.getMethodResorted());
                    fillCellWithNewlines(r.getCell(2), p.getWebLink());
                }
            } else {
                addDynamicRow(tablePol7, new String[]{"-", "-", "-"});
            }

            doc.createParagraph().createRun().addBreak();

            // =================================================================
            // SECTION C: PRINCIPLE 8 (INCLUSIVE GROWTH)
            // =================================================================
            XWPFParagraph pP8 = doc.createParagraph();
            pP8.setPageBreak(true);
            pP8.setSpacingBefore(300);
            XWPFRun rP8 = pP8.createRun();
            rP8.setText("PRINCIPLE 8: Businesses should promote inclusive growth and equitable development.");
            rP8.setBold(true);
            rP8.setFontSize(12);
            rP8.setColor(COLOR_THEME_GREEN);
            rP8.setFontFamily("Calibri");

            addBoldText(doc, "Essential Indicators");
            addBoldText(doc, "1. Details of Social Impact Assessments (SIA) of projects undertaken by the entity.");
            XWPFParagraph p8_1 = doc.createParagraph();
            setTextWithBreaks(p8_1.createRun(), checkNull(data.getP8SiaDetails()));

            addBoldText(doc, "2. Provide information on project(s) for which ongoing Rehabilitation and Resettlement (R&R) is being undertaken:");
            XWPFTable tableRr = doc.createTable();
            tableRr.setWidth("100%");
            XWPFTableRow hRr = tableRr.getRow(0);
            ensureCells(hRr, 6);
            styleCell(hRr.getCell(0), "S. No.", true);
            styleCell(hRr.getCell(1), "Project Name", true);
            styleCell(hRr.getCell(2), "State", true);
            styleCell(hRr.getCell(3), "District", true);
            styleCell(hRr.getCell(4), "No. of PAFs", true);
            styleCell(hRr.getCell(5), "% Covered", true);

            List<BrsrReportRequest.RandRProject> rnrList = data.getP8RandRProjects();
            if(rnrList != null && !rnrList.isEmpty()){
                int count = 1;
                for(BrsrReportRequest.RandRProject rr : rnrList){
                    addDynamicRow(tableRr, new String[]{
                            String.valueOf(count++), checkNull(rr.getProjectName()), checkNull(rr.getState()),
                            checkNull(rr.getDistrict()), checkNull(rr.getNoOfPafs()), checkNull(rr.getPercCovered())
                    });
                }
            } else {
                addDynamicRow(tableRr, new String[]{"-", "NA", "-", "-", "-", "-"});
            }

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "3. Describe the mechanisms to receive and redress grievances of the community.");
            XWPFParagraph p8_3 = doc.createParagraph();
            setTextWithBreaks(p8_3.createRun(), checkNull(data.getP8GrievanceMechanism()));

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "4. Percentage of input material (inputs to total inputs by value) sourced from suppliers:");
            XWPFTable tableInp = doc.createTable();
            tableInp.setWidth("100%");
            XWPFTableRow hInp = tableInp.getRow(0);
            ensureCells(hInp, 3);
            styleCell(hInp.getCell(0), "Category", true);
            styleCell(hInp.getCell(1), fyCur, true);
            styleCell(hInp.getCell(2), fyPrev, true);

            addRow(tableInp, "Directly sourced from MSMEs/small producers", data.getP8InputMsmeCurr(), data.getP8InputMsmePrev());
            addRow(tableInp, "Directly sourced from within India", data.getP8InputIndiaCurr(), data.getP8InputIndiaPrev());

            addSectionHeader(doc, "Leadership Indicators");
            addBoldText(doc, "1. CSR projects in designated aspirational districts:");
            XWPFTable tableAsp = doc.createTable();
            tableAsp.setWidth("100%");
            XWPFTableRow hAsp = tableAsp.getRow(0);
            ensureCells(hAsp, 3);
            styleCell(hAsp.getCell(0), "State", true);
            styleCell(hAsp.getCell(1), "District", true);
            styleCell(hAsp.getCell(2), "Amount Spent", true);

            List<BrsrReportRequest.CsrAspirationalDistrict> aspList = data.getP8AspirationalDistricts();
            if(aspList != null && !aspList.isEmpty()){
                for(BrsrReportRequest.CsrAspirationalDistrict ad : aspList){
                    addDynamicRow(tableAsp, new String[]{ checkNull(ad.getState()), checkNull(ad.getDistrict()), checkNull(ad.getAmountSpent()) });
                }
            } else {
                addDynamicRow(tableAsp, new String[]{"-", "-", "-"});
            }

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "2. (a) Preferential procurement policy for marginalized groups (Yes/No).");
            XWPFParagraph p8L2 = doc.createParagraph();
            p8L2.createRun().setText(checkNull(data.getP8PreferentialProcurement()));

            addBoldText(doc, "(b) From which marginalized groups do you procure?");
            XWPFParagraph p8L2b = doc.createParagraph();
            setTextWithBreaks(p8L2b.createRun(), checkNull(data.getP8MarginalizedGroups()));

            addBoldText(doc, "(c) % of total procurement?");
            XWPFParagraph p8L2c = doc.createParagraph();
            p8L2c.createRun().setText(checkNull(data.getP8ProcurementPercentage()));

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "3. Benefits derived from intellectual properties based on traditional knowledge:");
            XWPFParagraph p8L3 = doc.createParagraph();
            p8L3.createRun().setText(checkNull(data.getP8IpBenefits()));

            doc.createParagraph().createRun().addBreak();
            addBoldText(doc, "4. Details of beneficiaries of CSR Projects:");
            XWPFTable tableBen = doc.createTable();
            tableBen.setWidth("100%");
            XWPFTableRow hBen = tableBen.getRow(0);
            ensureCells(hBen, 3);
            styleCell(hBen.getCell(0), "CSR Project", true);
            styleCell(hBen.getCell(1), "No. Benefitted", true);
            styleCell(hBen.getCell(2), "% Vulnerable", true);

            List<BrsrReportRequest.CsrBeneficiary> benList = data.getP8CsrBeneficiaries();
            if(benList != null && !benList.isEmpty()){
                for(BrsrReportRequest.CsrBeneficiary b : benList){
                    addDynamicRow(tableBen, new String[]{ checkNull(b.getCsrProject()), checkNull(b.getNoBenefitted()), checkNull(b.getPercVulnerable()) });
                }
            } else {
                addDynamicRow(tableBen, new String[]{"-", "-", "-"});
            }

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

    private void addDynamicHeaderRow(XWPFTable table, String[] headers) {
        XWPFTableRow row = (table.getRow(0) != null) ? table.getRow(0) : table.createRow();
        ensureCells(row, headers.length);
        for (int i = 0; i < headers.length; i++) styleCell(row.getCell(i), headers[i], true);
    }

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

        // --- HEADER ROW 1 (Benefit Names) ---
        XWPFTableRow h1 = table.getRow(0);
        ensureCells(h1, 12);

        styleCell(h1.getCell(0), "Category", true);
        styleCell(h1.getCell(1), "Total (A)", true);

        String[] benefits = {"Health insurance", "Accident insurance", "Maternity benefits", "Paternity Benefits", "Day Care facilities"};
        for(int i=0; i<5; i++) {
            int col = 2 + (i*2);
            styleCell(h1.getCell(col), benefits[i], true);
            mergeCellsHorizontal(h1, col, col+1);
        }

        // --- HEADER ROW 2 (Number / %) ---
        XWPFTableRow h2 = table.createRow();
        ensureCells(h2, 12);
        styleCell(h2.getCell(0), "", true);
        styleCell(h2.getCell(1), "", true);

        for(int i=0; i<5; i++) {
            int col = 2 + (i*2);
            styleCell(h2.getCell(col), "Number (B)", true);
            styleCell(h2.getCell(col+1), "% (B/A)", true);
        }

        // --- DATA ROWS ---
        if (rows != null && !rows.isEmpty()) {
            // Split list logic: usually first 3 are Permanent, next 3 are Temporary
            // We'll add Section Headers manually based on index

            // Section 1 Header
            addSectionTitleRow(table, label1, 12);

            int count = 0;
            for (BrsrReportRequest.WellBeingRow r : rows) {
                // If we hit the middle of the list (assuming 3 rows per section for Male/Female/Total)
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
}