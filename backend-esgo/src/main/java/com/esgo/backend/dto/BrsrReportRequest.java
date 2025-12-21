package com.esgo.backend.dto;

import java.util.List;

public class BrsrReportRequest {
    private Long id;

    // --- SECTION A: GENERAL DISCLOSURES ---
    private String cin;
    private String companyName;
    private String yearOfInc;
    private String registeredAddress;
    private String corporateAddress;
    private String email;
    private String telephone;
    private String website;
    private String reportingYear;
    private String stockExchanges;
    private String paidUpCapital;
    private String reportingBoundary;

    // Contact Person
    private String contactPersonName;
    private String contactPersonDesignation;
    private String contactPersonAddress;
    private String contactPersonEmail;
    private String contactPersonTelephone;

    // --- SECTION II: PRODUCTS/SERVICES (NEW) ---
    private List<BusinessActivity> businessActivities;
    private boolean includeConsolidated;
    private List<ProductService> productsServices;

    // --- SECTION III: OPERATIONS (NEW) ---
    private String plantsNational;
    private String officesNational;
    private String totalNational;

    private String plantsInternational;
    private String officesInternational;
    private String totalInternational;

    // --- INNER CLASSES FOR TABLES ---
    public static class BusinessActivity {
        private String descriptionMain;
        private String descriptionBusiness;
        private String turnoverPercentage;

        public String getDescriptionMain() { return descriptionMain; }
        public void setDescriptionMain(String descriptionMain) { this.descriptionMain = descriptionMain; }
        public String getDescriptionBusiness() { return descriptionBusiness; }
        public void setDescriptionBusiness(String descriptionBusiness) { this.descriptionBusiness = descriptionBusiness; }
        public String getTurnoverPercentage() { return turnoverPercentage; }
        public void setTurnoverPercentage(String turnoverPercentage) { this.turnoverPercentage = turnoverPercentage; }
    }

    public static class ProductService {
        private String productName;
        private String nicCode;
        private String turnoverStandalone;
        private String percentageStandalone;
        private String turnoverConsolidated;
        private String percentageConsolidated;

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getNicCode() { return nicCode; }
        public void setNicCode(String nicCode) { this.nicCode = nicCode; }
        public String getTurnoverStandalone() { return turnoverStandalone; }
        public void setTurnoverStandalone(String turnoverStandalone) { this.turnoverStandalone = turnoverStandalone; }
        public String getPercentageStandalone() { return percentageStandalone; }
        public void setPercentageStandalone(String percentageStandalone) { this.percentageStandalone = percentageStandalone; }
        public String getTurnoverConsolidated() { return turnoverConsolidated; }
        public void setTurnoverConsolidated(String turnoverConsolidated) { this.turnoverConsolidated = turnoverConsolidated; }
        public String getPercentageConsolidated() { return percentageConsolidated; }
        public void setPercentageConsolidated(String percentageConsolidated) { this.percentageConsolidated = percentageConsolidated; }
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getYearOfInc() { return yearOfInc; }
    public void setYearOfInc(String yearOfInc) { this.yearOfInc = yearOfInc; }
    public String getRegisteredAddress() { return registeredAddress; }
    public void setRegisteredAddress(String registeredAddress) { this.registeredAddress = registeredAddress; }
    public String getCorporateAddress() { return corporateAddress; }
    public void setCorporateAddress(String corporateAddress) { this.corporateAddress = corporateAddress; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getReportingYear() { return reportingYear; }
    public void setReportingYear(String reportingYear) { this.reportingYear = reportingYear; }
    public String getStockExchanges() { return stockExchanges; }
    public void setStockExchanges(String stockExchanges) { this.stockExchanges = stockExchanges; }
    public String getPaidUpCapital() { return paidUpCapital; }
    public void setPaidUpCapital(String paidUpCapital) { this.paidUpCapital = paidUpCapital; }
    public String getReportingBoundary() { return reportingBoundary; }
    public void setReportingBoundary(String reportingBoundary) { this.reportingBoundary = reportingBoundary; }
    public String getContactPersonName() { return contactPersonName; }
    public void setContactPersonName(String contactPersonName) { this.contactPersonName = contactPersonName; }
    public String getContactPersonDesignation() { return contactPersonDesignation; }
    public void setContactPersonDesignation(String contactPersonDesignation) { this.contactPersonDesignation = contactPersonDesignation; }
    public String getContactPersonAddress() { return contactPersonAddress; }
    public void setContactPersonAddress(String contactPersonAddress) { this.contactPersonAddress = contactPersonAddress; }
    public String getContactPersonEmail() { return contactPersonEmail; }
    public void setContactPersonEmail(String contactPersonEmail) { this.contactPersonEmail = contactPersonEmail; }
    public String getContactPersonTelephone() { return contactPersonTelephone; }
    public void setContactPersonTelephone(String contactPersonTelephone) { this.contactPersonTelephone = contactPersonTelephone; }

    // List Getters
    public List<BusinessActivity> getBusinessActivities() { return businessActivities; }
    public void setBusinessActivities(List<BusinessActivity> businessActivities) { this.businessActivities = businessActivities; }
    public boolean isIncludeConsolidated() { return includeConsolidated; }
    public void setIncludeConsolidated(boolean includeConsolidated) { this.includeConsolidated = includeConsolidated; }
    public List<ProductService> getProductsServices() { return productsServices; }
    public void setProductsServices(List<ProductService> productsServices) { this.productsServices = productsServices; }

    // --- GETTERS AND SETTERS FOR SECTION III ---
    public String getPlantsNational() { return plantsNational; }
    public void setPlantsNational(String plantsNational) { this.plantsNational = plantsNational; }
    public String getOfficesNational() { return officesNational; }
    public void setOfficesNational(String officesNational) { this.officesNational = officesNational; }
    public String getTotalNational() { return totalNational; }
    public void setTotalNational(String totalNational) { this.totalNational = totalNational; }

    public String getPlantsInternational() { return plantsInternational; }
    public void setPlantsInternational(String plantsInternational) { this.plantsInternational = plantsInternational; }
    public String getOfficesInternational() { return officesInternational; }
    public void setOfficesInternational(String officesInternational) { this.officesInternational = officesInternational; }
    public String getTotalInternational() { return totalInternational; }
    public void setTotalInternational(String totalInternational) { this.totalInternational = totalInternational; }
}