package com.esgo.backend.service;

import com.esgo.backend.dto.BrsrReportRequest;
import com.esgo.backend.model.Report;
import com.esgo.backend.model.User;
import com.esgo.backend.repository.ReportRepository;
import com.esgo.backend.repository.UserRepository;
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

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String COLOR_THEME_GREEN = "548235";
    private static final String COLOR_BORDER = "CCCCCC";

    // --- DB OPERATIONS (Unchanged) ---
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

                // FIX: Use addMultiLineText to respect newlines in the textarea
                XWPFParagraph p13Ans = doc.createParagraph();
                p13Ans.setSpacingBefore(200);
                XWPFRun r13Ans = p13Ans.createRun();
                r13Ans.setFontFamily("Calibri");
                r13Ans.setBold(true);
                setTextWithBreaks(r13Ans, checkNull(reportingBoundary));
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION II: PRODUCTS/SERVICES =================
            addSectionHeader(doc, "II. Products/Services");

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

            addBoldText(doc, "16. Number of locations:");
            XWPFTable table16 = doc.createTable();
            table16.setWidth("100%");
            String[] headers16 = {"Location", "Number of Plants", "Number of Offices", "Total"};
            addDynamicHeaderRow(table16, headers16);
            addDynamicRow(table16, new String[]{ "National", checkNull(data.getPlantsNational()), checkNull(data.getOfficesNational()), checkNull(data.getTotalNational()) });
            addDynamicRow(table16, new String[]{ "International", checkNull(data.getPlantsInternational()), checkNull(data.getOfficesInternational()), checkNull(data.getTotalInternational()) });

            doc.createParagraph().createRun().addBreak();

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

            // FIX: Multi-line support for exports
            XWPFParagraph p17b = doc.createParagraph();
            XWPFRun r17b = p17b.createRun();
            r17b.setFontFamily("Calibri");
            setTextWithBreaks(r17b, checkNull(data.getContributionExports()));
            p17b.setSpacingAfter(200);

            addBoldText(doc, "c. A brief on types of customers:");
            // FIX: Multi-line support for customers
            XWPFParagraph p17c = doc.createParagraph();
            XWPFRun r17c = p17c.createRun();
            r17c.setFontFamily("Calibri");
            setTextWithBreaks(r17c, checkNull(data.getTypesOfCustomers()));
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

            // --- REDESIGNED LAYOUT FOR Q23 (BLOCK STYLE) ---
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

                    // Mechanism Text (Use new helper to respect line breaks)
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

                    String curFy = (data.getComplaintsFyCurrentHeader() != null) ? data.getComplaintsFyCurrentHeader() : "Current FY";
                    String prevFy = (data.getComplaintsFyPreviousHeader() != null) ? data.getComplaintsFyPreviousHeader() : "Previous FY";

                    styleCell(miniH1.getCell(0), curFy, true);
                    mergeCellsHorizontal(miniH1, 0, 2);
                    styleCell(miniH1.getCell(3), prevFy, true);
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

            // User Note with "Note:" prefix
            String matNote = data.getMaterialIssuesNote();
            if (matNote != null && !matNote.trim().isEmpty()) {
                XWPFParagraph pNote = doc.createParagraph();
                XWPFRun rNoteLbl = pNote.createRun();
                rNoteLbl.setText("Note: ");
                rNoteLbl.setBold(true);
                rNoteLbl.setItalic(true);
                rNoteLbl.setFontFamily("Calibri");

                XWPFRun rNoteVal = pNote.createRun();
                rNoteVal.setText(matNote);
                rNoteVal.setItalic(true);
                rNoteVal.setFontFamily("Calibri");
            }
            doc.createParagraph().createRun().addBreak();

            // --- TABLE 24 (Optimized Widths) ---
            XWPFTable table24 = doc.createTable();
            table24.setWidth("100%");

            // New Width Calculation (Total ~10000 twips)
            // Giving maximum space (65%) to Rationale & Approach
            CTTblGrid grid24 = table24.getCTTbl().addNewTblGrid();
            grid24.addNewGridCol().setW(BigInteger.valueOf(400));  // S.No (Small)
            grid24.addNewGridCol().setW(BigInteger.valueOf(1400)); // Material Issue
            grid24.addNewGridCol().setW(BigInteger.valueOf(800));  // R/O (Small)
            grid24.addNewGridCol().setW(BigInteger.valueOf(3200)); // Rationale (LARGE)
            grid24.addNewGridCol().setW(BigInteger.valueOf(3200)); // Approach (LARGE)
            grid24.addNewGridCol().setW(BigInteger.valueOf(1000)); // Financial

            // Header Row
            XWPFTableRow hRow24 = table24.getRow(0);
            ensureCells(hRow24, 6);
            styleCell(hRow24.getCell(0), "S. No.", true);
            styleCell(hRow24.getCell(1), "Material issue identified", true);
            styleCell(hRow24.getCell(2), "Indicate whether risk or opportunity (R/O)", true);
            styleCell(hRow24.getCell(3), "Rationale for identifying the risk / opportunity", true);
            styleCell(hRow24.getCell(4), "In case of risk, approach to adapt or mitigate", true);
            styleCell(hRow24.getCell(5), "Financial implications (Positive/Negative)", true);

            // Data Rows with Text Wrapping Support
            List<BrsrReportRequest.MaterialIssue> issues = data.getMaterialIssues();
            if (issues != null && !issues.isEmpty()) {
                int count = 1;
                for (BrsrReportRequest.MaterialIssue issue : issues) {
                    XWPFTableRow row = table24.createRow();
                    ensureCells(row, 6);

                    // Simple columns
                    styleCell(row.getCell(0), String.valueOf(count++), false);
                    styleCell(row.getCell(1), checkNull(issue.getDescription()), false);
                    styleCell(row.getCell(2), checkNull(issue.getRiskOrOpportunity()), false);

                    // Complex columns (Rationale & Approach) - Use helper to respect newlines
                    fillCellWithNewlines(row.getCell(3), checkNull(issue.getRationale()));
                    fillCellWithNewlines(row.getCell(4), checkNull(issue.getApproach()));

                    // Financial column
                    styleCell(row.getCell(5), checkNull(issue.getFinancialImplications()), false);
                }
            } else {
                addDynamicRow(table24, new String[]{"-", "-", "-", "-", "-", "-"});
            }


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

    // --- NEW: Handle Newlines in Blocks ---
    private void setTextWithBreaks(XWPFRun run, String text) {
        if (text == null) return;
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            run.setText(lines[i]);
            if (i < lines.length - 1) {
                run.addBreak();
            }
        }
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

        // Use updated Logic to respect newlines in Tables too
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

    // --- Helper to put multi-line text into a table cell ---
    private void fillCellWithNewlines(XWPFTableCell cell, String text) {
        // Clear existing default paragraph
        if (cell.getParagraphs().size() > 0) {
            cell.removeParagraph(0);
        }

        if (text == null) text = "-";
        String[] lines = text.split("\n");

        for (String line : lines) {
            XWPFParagraph p = cell.addParagraph();
            p.setSpacingAfter(0); // Tight spacing within the cell
            XWPFRun r = p.createRun();
            r.setText(line);
            r.setFontFamily("Calibri");
            r.setFontSize(10);
        }
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP);
    }
}