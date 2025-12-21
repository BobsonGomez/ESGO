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

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    // --- COLORS ---
    private static final String COLOR_THEME_GREEN = "548235";
    private static final String COLOR_BORDER = "CCCCCC";

    // --- DB OPERATIONS ---
    public void saveReportToDb(BrsrReportRequest request, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) return;

        Report report;

        if (request.getId() != null) {
            Report existingReport = reportRepository.findById(request.getId()).orElse(null);
            if (existingReport != null && existingReport.getUser().getId().equals(user.getId())) {
                report = existingReport;
            } else {
                report = new Report();
                report.setUser(user);
                report.setCreatedDate(LocalDate.now());
            }
        } else {
            report = new Report();
            report.setUser(user);
            report.setCreatedDate(LocalDate.now());
        }

        String cName = (request.getCompanyName() != null) ? request.getCompanyName() : "Draft";
        report.setReportName(cName + " Report");
        report.setReportType("General Disclosures");

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            report.setReportDataJson(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        reportRepository.save(report);

        if (!user.hasGeneratedReport()) {
            user.setHasGeneratedReport(true);
            userRepository.save(user);
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

            // --- SECTION HEADER ---
            XWPFParagraph pSection = doc.createParagraph();
            pSection.setSpacingAfter(400);
            XWPFRun rSection = pSection.createRun();
            rSection.setText("SECTION A: GENERAL DISCLOSURES");
            rSection.setBold(true);
            rSection.setFontSize(14);
            rSection.setColor(COLOR_THEME_GREEN);
            rSection.setFontFamily("Calibri");

            // --- SUB HEADER ---
            XWPFParagraph pSub = doc.createParagraph();
            pSub.setSpacingAfter(300);
            XWPFRun rSub = pSub.createRun();
            rSub.setText("I. Details of the listed entity");
            rSub.setBold(true);
            rSub.setFontFamily("Calibri");

            // --- TABLE SETUP ---
            XWPFTable table = doc.createTable();
            table.setWidth("100%");

            // Header Row
            XWPFTableRow headerRow = table.getRow(0);
            if(headerRow.getTableCells().size() < 2) {
                headerRow.addNewTableCell();
            }
            styleCell(headerRow.getCell(0), "S. No. Particulars", true);
            styleCell(headerRow.getCell(1), "Details", true);

            // -- Data Rows --
            addRow(table, "1. Corporate Identity Number (CIN)", data.getCin());
            addRow(table, "2. Name of the Listed Entity", data.getCompanyName());
            addRow(table, "3. Year of incorporation", data.getYearOfInc());
            addRow(table, "4. Registered office address", data.getRegisteredAddress());
            addRow(table, "5. Corporate address", data.getCorporateAddress());
            addRow(table, "6. E-mail", data.getEmail());
            addRow(table, "7. Telephone", data.getTelephone());
            addRow(table, "8. Website", data.getWebsite());
            addRow(table, "9. Financial Year", data.getReportingYear());

            // Stock Exchange Formatting
            String stockData = checkNull(data.getStockExchanges());
            stockData = stockData.replaceAll("\\s+([a-z]\\.)", "\n$1");
            if(stockData.matches("^[a-z]\\..*")) {
                // Formatting is already good
            }
            addRow(table, "10. Stock Exchange(s)", stockData);

            addRow(table, "11. Paid-up Capital", data.getPaidUpCapital());

            // Contact Person
            String contactInfo = String.format("%s\n%s\nAddress: %s\nTel: %s | Email: %s",
                    checkNull(data.getContactPersonName()),
                    checkNull(data.getContactPersonDesignation()),
                    checkNull(data.getContactPersonAddress()),
                    checkNull(data.getContactPersonTelephone()),
                    checkNull(data.getContactPersonEmail()));

            addRow(table, "12. Contact Person details", contactInfo);

            // --- POINT 13: REPORTING BOUNDARY ---
            String reportingBoundary = data.getReportingBoundary();

            if (reportingBoundary != null && !reportingBoundary.trim().isEmpty()) {

                // Force Page Break
                XWPFParagraph pageBreakPara = doc.createParagraph();
                pageBreakPara.setPageBreak(true);

                // Add Heading
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

                // Add Description
                XWPFParagraph p13Desc = doc.createParagraph();
                XWPFRun r13Desc = p13Desc.createRun();
                r13Desc.setText("Are the disclosures under this report made on a standalone basis (i.e., only for the entity) or on a consolidated basis (i.e., for the entity and all the entities which form a part of its consolidated financial statements, taken together).");
                r13Desc.setItalic(true);
                r13Desc.setFontFamily("Calibri");

                // Add User Content
                XWPFParagraph p13Ans = doc.createParagraph();
                p13Ans.setSpacingBefore(200);
                XWPFRun r13Ans = p13Ans.createRun();
                r13Ans.setText(reportingBoundary);
                r13Ans.setFontFamily("Calibri");
                r13Ans.setBold(true);
            }

            // ================= SECTION II: PRODUCTS/SERVICES =================
            // No page break required here as Point 13 might already be on a new page.

            XWPFParagraph pSec2 = doc.createParagraph();
            pSec2.setSpacingBefore(300);
            pSec2.setSpacingAfter(200);
            XWPFRun rSec2 = pSec2.createRun();
            rSec2.setText("II. Products/Services");
            rSec2.setBold(true);
            rSec2.setColor(COLOR_THEME_GREEN);
            rSec2.setFontSize(14);
            rSec2.setFontFamily("Calibri");

            // --- 14. BUSINESS ACTIVITIES ---
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

            // --- 15. PRODUCTS/SERVICES ---
            addBoldText(doc, "15. Products/Services sold by the entity (accounting for 90% of the entity's Turnover):");

            XWPFTable table15 = doc.createTable();
            table15.setWidth("100%");

            String[] headers15;
            if (data.isIncludeConsolidated()) {
                headers15 = new String[]{"S. No.", "Product/Service", "NIC Code", "Turnover (Standalone)", "%", "Turnover (Consolidated)", "%"};
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
                for(int i=0; i<cols; i++) empty[i]="-";
                addDynamicRow(table15, empty);
            }

            doc.createParagraph().createRun().addBreak();

            // ================= SECTION III: OPERATIONS =================
            XWPFParagraph pSec3 = doc.createParagraph();
            pSec3.setSpacingBefore(300);
            pSec3.setSpacingAfter(200);
            XWPFRun rSec3 = pSec3.createRun();
            rSec3.setText("III. Operations");
            rSec3.setBold(true);
            rSec3.setColor(COLOR_THEME_GREEN);
            rSec3.setFontSize(14);
            rSec3.setFontFamily("Calibri");

            // --- 16. LOCATIONS ---
            addBoldText(doc, "16. Number of locations where plants and/or operations/offices of the entity are situated:");

            XWPFTable table16 = doc.createTable();
            table16.setWidth("100%");

            // Headers
            String[] headers16 = {"Location", "Number of Plants", "Number of Offices", "Total"};
            addDynamicHeaderRow(table16, headers16);

            // Row 1: National
            addDynamicRow(table16, new String[]{
                    "National",
                    checkNull(data.getPlantsNational()),
                    checkNull(data.getOfficesNational()),
                    checkNull(data.getTotalNational())
            });

            // Row 2: International
            addDynamicRow(table16, new String[]{
                    "International",
                    checkNull(data.getPlantsInternational()),
                    checkNull(data.getOfficesInternational()),
                    checkNull(data.getTotalInternational())
            });

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
        return (val == null || val.isEmpty()) ? "-" : val;
    }

    private void addRow(XWPFTable table, String label, String value) {
        XWPFTableRow row = table.createRow();
        styleCell(row.getCell(0), label, false);

        if(value != null && value.contains("\n")) {
            if(row.getCell(1).getParagraphs().size() > 0) {
                row.getCell(1).removeParagraph(0);
            }
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

    private void styleCell(XWPFTableCell cell, String text, boolean isHeader) {
        if(cell.getParagraphs().size() > 0) {
            cell.removeParagraph(0);
        }
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

    private void addBoldText(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        r.setText(text);
        r.setBold(true);
        r.setFontSize(11);
        r.setFontFamily("Calibri");
    }

    // --- NEW DYNAMIC HELPERS FOR TABLES ---
    private void addDynamicHeaderRow(XWPFTable table, String[] headers) {
        XWPFTableRow row = (table.getRow(0) != null) ? table.getRow(0) : table.createRow();

        while (row.getTableCells().size() < headers.length) {
            row.addNewTableCell();
        }

        for (int i = 0; i < headers.length; i++) {
            styleCell(row.getCell(i), headers[i], true);
        }
    }

    private void addDynamicRow(XWPFTable table, String[] values) {
        XWPFTableRow row = table.createRow();

        while (row.getTableCells().size() < values.length) {
            row.addNewTableCell();
        }

        for (int i = 0; i < values.length; i++) {
            styleCell(row.getCell(i), values[i], false);
        }
    }
}