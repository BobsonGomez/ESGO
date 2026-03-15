package com.esgo.backend.dto; // Adjust to your package



public class PublishProfileRequest {
    private String companyName;
    private String sector;
    private String address;
    private String phone;
    private String email;
    private String description;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScoreE() {
        return scoreE;
    }

    public void setScoreE(String scoreE) {
        this.scoreE = scoreE;
    }

    public String getScoreS() {
        return scoreS;
    }

    public void setScoreS(String scoreS) {
        this.scoreS = scoreS;
    }

    public String getScoreG() {
        return scoreG;
    }

    public void setScoreG(String scoreG) {
        this.scoreG = scoreG;
    }

    public String getScoreAvg() {
        return scoreAvg;
    }

    public void setScoreAvg(String scoreAvg) {
        this.scoreAvg = scoreAvg;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    public String getReportFileBase64() {
        return reportFileBase64;
    }

    public void setReportFileBase64(String reportFileBase64) {
        this.reportFileBase64 = reportFileBase64;
    }

    private String scoreE;
    private String scoreS;
    private String scoreG;
    private String scoreAvg;

    // File data sent as strings
    private String reportFileName;
    private String reportFileBase64;
}