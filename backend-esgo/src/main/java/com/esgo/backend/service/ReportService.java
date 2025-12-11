package com.esgo.backend.service;

import com.esgo.backend.dto.BrsrReportRequest;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

@Service
public class ReportService {

    private static final String THEME_COLOR = "1e8ca0"; // Your Brand Blue/Green

    public ByteArrayInputStream generateBrsrReport(BrsrReportRequest data) throws IOException {
        try (XWPFDocument doc = new XWPFDocument()) {

            // ================= TITLE PAGE =================
            addTitle(doc, "BUSINESS RESPONSIBILITY &");
            addTitle(doc, "SUSTAINABILITY REPORT");
            addSubtitle(doc, "Financial Year: " + data.getReportingYear());
            addParagraph(doc, ""); // Spacer

            // ================= SECTION A =================
            addSectionHeader(doc, "SECTION A: GENERAL DISCLOSURES");
            addBoldText(doc, "I. Details of the listed entity");

            // Create Key-Value Table for Company Details
            createTable(doc, new String[][]{
                    {"1. Corporate Identity Number (CIN)", data.getCin()},
                    {"2. Name of the Listed Entity", data.getCompanyName()},
                    {"3. Year of incorporation", data.getYearOfInc()},
                    {"4. Registered office address", data.getRegisteredAddress()},
                    {"5. Corporate address", data.getCorporateAddress()},
                    {"6. E-mail", data.getEmail()},
                    {"7. Telephone", data.getTelephone()},
                    {"8. Website", data.getWebsite()},
                    {"9. Financial Year", data.getReportingYear()},
                    {"10. Paid-up Capital", "₹ " + data.getPaidUpCapital()}
            });

            doc.createParagraph().createRun().addBreak();

            // ================= EMPLOYEES TABLE =================
            addBoldText(doc, "IV. Employees");
            addParagraph(doc, "18. Details as at the end of Financial Year:");

            // Complex Table Structure (Headers + Data)
            String[][] empData = {
                    {"Category", "Male", "Female", "Total"}, // Header Row
                    {"Permanent Employees", data.getEmpMalePermanent(), data.getEmpFemalePermanent(), data.getEmpTotalPermanent()},
                    {"Permanent Workers", data.getWorkerMalePermanent(), data.getWorkerFemalePermanent(), data.getWorkerTotalPermanent()}
            };
            createTable(doc, empData);

            doc.createParagraph().createRun().addBreak(BreakType.PAGE); // New Page

            // ================= SECTION C: PRINCIPLE 6 =================
            addSectionHeader(doc, "SECTION C: PRINCIPLE WISE PERFORMANCE");
            addBoldText(doc, "Principle 6: Businesses should respect and make efforts to protect and restore the environment.");

            addBoldText(doc, "1. Energy Consumption & Intensity");
            createTable(doc, new String[][]{
                    {"Parameter", "Current FY (" + data.getReportingYear() + ")"},
                    {"Total Electricity Consumption", data.getElectricityConsumption() + " GJ"},
                    {"Total Fuel Consumption", data.getFuelConsumption() + " GJ"},
                    {"Energy Intensity (per rupee turnover)", data.getEnergyIntensity()}
            });

            addParagraph(doc, "");

            addBoldText(doc, "2. Greenhouse Gas Emissions (Scope 1 & 2)");
            createTable(doc, new String[][]{
                    {"Parameter", "Current FY (" + data.getReportingYear() + ")"},
                    {"Total Scope 1 Emissions", data.getScope1Emissions() + " tCO2e"},
                    {"Total Scope 2 Emissions", data.getScope2Emissions() + " tCO2e"},
                    {"Emission Intensity", data.getEmissionIntensity()}
            });

            addParagraph(doc, "");

            addBoldText(doc, "3. Waste Management");
            createTable(doc, new String[][]{
                    {"Category", "Metric Tonnes"},
                    {"Plastic Waste", data.getPlasticWaste()},
                    {"E-Waste", data.getEWaste()},
                    {"Hazardous Waste", data.getHazardousWaste()},
                    {"Total Waste Generated", data.getTotalWasteGenerated()},
                    {"Total Waste Recycled", data.getTotalWasteRecycled()}
            });

            // Footer
            XWPFParagraph footer = doc.createParagraph();
            footer.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun footerRun = footer.createRun();
            footerRun.addBreak();
            footerRun.addBreak();
            footerRun.setText("Generated via ESGO Industry Portal");
            footerRun.setItalic(true);
            footerRun.setColor("808080");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // --- HELPERS FOR FORMATTING ---

    private void addTitle(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r = p.createRun();
        r.setText(text);
        r.setBold(true);
        r.setFontSize(24);
        r.setColor(THEME_COLOR);
        r.setFontFamily("Calibri");
    }

    private void addSubtitle(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r = p.createRun();
        r.setText(text);
        r.setFontSize(14);
        r.setColor("666666");
    }

    private void addSectionHeader(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setBorderBottom(Borders.SINGLE); // Underline the section
        XWPFRun r = p.createRun();
        r.setText(text);
        r.setBold(true);
        r.setFontSize(16);
        r.setColor(THEME_COLOR);
        r.addBreak();
    }

    private void addBoldText(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        r.setText(text);
        r.setBold(true);
        r.setFontSize(12);
    }

    private void addParagraph(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        r.setText(text);
    }

    private void createTable(XWPFDocument doc, String[][] data) {
        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        // Remove default empty row
        if(table.getRow(0) != null) table.removeRow(0);

        for (int i = 0; i < data.length; i++) {
            XWPFTableRow row = table.createRow();
            String[] rowData = data[i];

            for (int j = 0; j < rowData.length; j++) {
                XWPFTableCell cell = row.getCell(j);
                if (cell == null) cell = row.createCell();

                // Style the cell
                XWPFParagraph p = cell.getParagraphs().get(0);
                p.setSpacingAfter(0);
                p.setSpacingBefore(0);
                XWPFRun r = p.createRun();
                r.setText(rowData[j]);
                r.setFontSize(10);

                // Header Row Styling (First Row)
                if (i == 0) {
                    cell.setColor("E7E7E7"); // Light Gray background
                    r.setBold(true);
                }
            }
        }
    }
}