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

    // --- SECTION III: Q17 (NEW) ---
    private String locationsNationalNumber;      // 17.a National Number
    private String locationsInternationalNumber; // 17.a International Number
    private String contributionExports;          // 17.b Text Paragraph
    private String typesOfCustomers;             // 17.c Text Paragraph

    // --- SECTION IV: EMPLOYEES (18.a) ---
    // Structure: [Category]_[Gender]_[Metric]

    // Permanent Employees
    private String empPermTotal, empPermMaleNo, empPermMalePerc, empPermFemaleNo, empPermFemalePerc, empPermOtherNo, empPermOtherPerc;
    // Temporary Employees
    private String empTempTotal, empTempMaleNo, empTempMalePerc, empTempFemaleNo, empTempFemalePerc, empTempOtherNo, empTempOtherPerc;
    // Permanent Workers
    private String workPermTotal, workPermMaleNo, workPermMalePerc, workPermFemaleNo, workPermFemalePerc, workPermOtherNo, workPermOtherPerc;
    // Temporary Workers
    private String workTempTotal, workTempMaleNo, workTempMalePerc, workTempFemaleNo, workTempFemalePerc, workTempOtherNo, workTempOtherPerc;

    // --- SECTION IV: DIFFERENTLY ABLED (18.b) ---
    // Permanent Employees (Diff Abled)
    private String daEmpPermTotal, daEmpPermMaleNo, daEmpPermMalePerc, daEmpPermFemaleNo, daEmpPermFemalePerc, daEmpPermOtherNo, daEmpPermOtherPerc;
    // Temporary Employees (Diff Abled)
    private String daEmpTempTotal, daEmpTempMaleNo, daEmpTempMalePerc, daEmpTempFemaleNo, daEmpTempFemalePerc, daEmpTempOtherNo, daEmpTempOtherPerc;
    // Permanent Workers (Diff Abled)
    private String daWorkPermTotal, daWorkPermMaleNo, daWorkPermMalePerc, daWorkPermFemaleNo, daWorkPermFemalePerc, daWorkPermOtherNo, daWorkPermOtherPerc;
    // Temporary Workers (Diff Abled)
    private String daWorkTempTotal, daWorkTempMaleNo, daWorkTempMalePerc, daWorkTempFemaleNo, daWorkTempFemalePerc, daWorkTempOtherNo, daWorkTempOtherPerc;

    private String employeeNotesA; // For 18.a
    private String employeeNotesB; // For 18.b

    // --- SECTION IV: Q19 (Women Representation) ---
    private List<WomenRepresentation> womenRepresentationList;
    private String womenRepresentationNotes;

    // --- SECTION IV: Q20 (Turnover Rate) ---
    // Structure: turnover_[Category]_[Year]_[Gender]
    private String fyCurrent;
    private String fyPrevious;
    private String fyPrior;

    // 1. Current FY
    private String turnoverEmpCurrMale, turnoverEmpCurrFemale, turnoverEmpCurrTotal;
    private String turnoverWorkCurrMale, turnoverWorkCurrFemale, turnoverWorkCurrTotal;

    // 2. Previous FY
    private String turnoverEmpPrevMale, turnoverEmpPrevFemale, turnoverEmpPrevTotal;
    private String turnoverWorkPrevMale, turnoverWorkPrevFemale, turnoverWorkPrevTotal;

    // 3. Year Prior to Previous
    private String turnoverEmpPriorMale, turnoverEmpPriorFemale, turnoverEmpPriorTotal;
    private String turnoverWorkPriorMale, turnoverWorkPriorFemale, turnoverWorkPriorTotal;

    private String turnoverNotes;

    // --- INNER CLASS ---
    public static class WomenRepresentation {
        private String category; // e.g. Board of Directors
        private String totalA;
        private String femaleNoB;
        private String femalePerc; // Calculated

        // Getters & Setters
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getTotalA() { return totalA; }
        public void setTotalA(String totalA) { this.totalA = totalA; }
        public String getFemaleNoB() { return femaleNoB; }
        public void setFemaleNoB(String femaleNoB) { this.femaleNoB = femaleNoB; }
        public String getFemalePerc() { return femalePerc; }
        public void setFemalePerc(String femalePerc) { this.femalePerc = femalePerc; }
    }

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

    public String getLocationsNationalNumber() { return locationsNationalNumber; }
    public void setLocationsNationalNumber(String locationsNationalNumber) { this.locationsNationalNumber = locationsNationalNumber; }

    public String getLocationsInternationalNumber() { return locationsInternationalNumber; }
    public void setLocationsInternationalNumber(String locationsInternationalNumber) { this.locationsInternationalNumber = locationsInternationalNumber; }

    public String getContributionExports() { return contributionExports; }
    public void setContributionExports(String contributionExports) { this.contributionExports = contributionExports; }

    public String getTypesOfCustomers() { return typesOfCustomers; }
    public void setTypesOfCustomers(String typesOfCustomers) { this.typesOfCustomers = typesOfCustomers; }

    //getters and setters for section IV

    public String getEmpPermTotal() {return empPermTotal;}
    public void setEmpPermTotal(String empPermTotal) {this.empPermTotal = empPermTotal;}

    public String getEmpPermMaleNo() {return empPermMaleNo;}
    public void setEmpPermMaleNo(String empPermMaleNo) {this.empPermMaleNo = empPermMaleNo;}

    public String getEmpPermMalePerc() {return empPermMalePerc;}
    public void setEmpPermMalePerc(String empPermMalePerc) {this.empPermMalePerc = empPermMalePerc;}

    public String getEmpPermFemaleNo() {return empPermFemaleNo;}
    public void setEmpPermFemaleNo(String empPermFemaleNo) {this.empPermFemaleNo = empPermFemaleNo;}

    public String getEmpPermFemalePerc() {return empPermFemalePerc;}
    public void setEmpPermFemalePerc(String empPermFemalePerc) {this.empPermFemalePerc = empPermFemalePerc;}

    public String getEmpPermOtherNo() {return empPermOtherNo;}
    public void setEmpPermOtherNo(String empPermOtherNo) {this.empPermOtherNo = empPermOtherNo;}

    public String getEmpPermOtherPerc() {return empPermOtherPerc;}
    public void setEmpPermOtherPerc(String empPermOtherPerc) {this.empPermOtherPerc = empPermOtherPerc;}

    public String getEmpTempTotal() {return empTempTotal;}
    public void setEmpTempTotal(String empTempTotal) {this.empTempTotal = empTempTotal;}

    public String getEmpTempMaleNo() {return empTempMaleNo;}
    public void setEmpTempMaleNo(String empTempMaleNo) {this.empTempMaleNo = empTempMaleNo;}

    public String getEmpTempMalePerc() {return empTempMalePerc;}
    public void setEmpTempMalePerc(String empTempMalePerc) {this.empTempMalePerc = empTempMalePerc;}

    public String getEmpTempFemaleNo() {return empTempFemaleNo;}
    public void setEmpTempFemaleNo(String empTempFemaleNo) {this.empTempFemaleNo = empTempFemaleNo;}

    public String getEmpTempFemalePerc() {return empTempFemalePerc;}
    public void setEmpTempFemalePerc(String empTempFemalePerc) {this.empTempFemalePerc = empTempFemalePerc;}

    public String getEmpTempOtherNo() {
        return empTempOtherNo;
    }

    public void setEmpTempOtherNo(String empTempOtherNo) {
        this.empTempOtherNo = empTempOtherNo;
    }

    public String getEmpTempOtherPerc() {
        return empTempOtherPerc;
    }

    public void setEmpTempOtherPerc(String empTempOtherPerc) {
        this.empTempOtherPerc = empTempOtherPerc;
    }

    public String getWorkPermTotal() {
        return workPermTotal;
    }

    public void setWorkPermTotal(String workPermTotal) {
        this.workPermTotal = workPermTotal;
    }

    public String getWorkPermMaleNo() {
        return workPermMaleNo;
    }

    public void setWorkPermMaleNo(String workPermMaleNo) {
        this.workPermMaleNo = workPermMaleNo;
    }

    public String getWorkPermMalePerc() {
        return workPermMalePerc;
    }

    public void setWorkPermMalePerc(String workPermMalePerc) {
        this.workPermMalePerc = workPermMalePerc;
    }

    public String getWorkPermFemaleNo() {
        return workPermFemaleNo;
    }

    public void setWorkPermFemaleNo(String workPermFemaleNo) {
        this.workPermFemaleNo = workPermFemaleNo;
    }

    public String getWorkPermFemalePerc() {
        return workPermFemalePerc;
    }

    public void setWorkPermFemalePerc(String workPermFemalePerc) {
        this.workPermFemalePerc = workPermFemalePerc;
    }

    public String getWorkPermOtherNo() {
        return workPermOtherNo;
    }

    public void setWorkPermOtherNo(String workPermOtherNo) {
        this.workPermOtherNo = workPermOtherNo;
    }

    public String getWorkPermOtherPerc() {
        return workPermOtherPerc;
    }

    public void setWorkPermOtherPerc(String workPermOtherPerc) {
        this.workPermOtherPerc = workPermOtherPerc;
    }

    public String getWorkTempTotal() {
        return workTempTotal;
    }

    public void setWorkTempTotal(String workTempTotal) {
        this.workTempTotal = workTempTotal;
    }

    public String getWorkTempMaleNo() {
        return workTempMaleNo;
    }

    public void setWorkTempMaleNo(String workTempMaleNo) {
        this.workTempMaleNo = workTempMaleNo;
    }

    public String getWorkTempMalePerc() {
        return workTempMalePerc;
    }

    public void setWorkTempMalePerc(String workTempMalePerc) {
        this.workTempMalePerc = workTempMalePerc;
    }

    public String getWorkTempFemaleNo() {
        return workTempFemaleNo;
    }

    public void setWorkTempFemaleNo(String workTempFemaleNo) {
        this.workTempFemaleNo = workTempFemaleNo;
    }

    public String getWorkTempFemalePerc() {
        return workTempFemalePerc;
    }

    public void setWorkTempFemalePerc(String workTempFemalePerc) {
        this.workTempFemalePerc = workTempFemalePerc;
    }

    public String getWorkTempOtherNo() {
        return workTempOtherNo;
    }

    public void setWorkTempOtherNo(String workTempOtherNo) {
        this.workTempOtherNo = workTempOtherNo;
    }

    public String getWorkTempOtherPerc() {
        return workTempOtherPerc;
    }

    public void setWorkTempOtherPerc(String workTempOtherPerc) {
        this.workTempOtherPerc = workTempOtherPerc;
    }

    public String getDaEmpPermTotal() {
        return daEmpPermTotal;
    }

    public void setDaEmpPermTotal(String daEmpPermTotal) {
        this.daEmpPermTotal = daEmpPermTotal;
    }

    public String getDaEmpPermMaleNo() {
        return daEmpPermMaleNo;
    }

    public void setDaEmpPermMaleNo(String daEmpPermMaleNo) {
        this.daEmpPermMaleNo = daEmpPermMaleNo;
    }

    public String getDaEmpPermMalePerc() {
        return daEmpPermMalePerc;
    }

    public void setDaEmpPermMalePerc(String daEmpPermMalePerc) {
        this.daEmpPermMalePerc = daEmpPermMalePerc;
    }

    public String getDaEmpPermFemaleNo() {
        return daEmpPermFemaleNo;
    }

    public void setDaEmpPermFemaleNo(String daEmpPermFemaleNo) {
        this.daEmpPermFemaleNo = daEmpPermFemaleNo;
    }

    public String getDaEmpPermFemalePerc() {
        return daEmpPermFemalePerc;
    }

    public void setDaEmpPermFemalePerc(String daEmpPermFemalePerc) {
        this.daEmpPermFemalePerc = daEmpPermFemalePerc;
    }

    public String getDaEmpPermOtherNo() {
        return daEmpPermOtherNo;
    }

    public void setDaEmpPermOtherNo(String daEmpPermOtherNo) {
        this.daEmpPermOtherNo = daEmpPermOtherNo;
    }

    public String getDaEmpPermOtherPerc() {
        return daEmpPermOtherPerc;
    }

    public void setDaEmpPermOtherPerc(String daEmpPermOtherPerc) {
        this.daEmpPermOtherPerc = daEmpPermOtherPerc;
    }

    public String getDaEmpTempTotal() {
        return daEmpTempTotal;
    }

    public void setDaEmpTempTotal(String daEmpTempTotal) {
        this.daEmpTempTotal = daEmpTempTotal;
    }

    public String getDaEmpTempMaleNo() {
        return daEmpTempMaleNo;
    }

    public void setDaEmpTempMaleNo(String daEmpTempMaleNo) {
        this.daEmpTempMaleNo = daEmpTempMaleNo;
    }

    public String getDaEmpTempMalePerc() {
        return daEmpTempMalePerc;
    }

    public void setDaEmpTempMalePerc(String daEmpTempMalePerc) {
        this.daEmpTempMalePerc = daEmpTempMalePerc;
    }

    public String getDaEmpTempFemaleNo() {
        return daEmpTempFemaleNo;
    }

    public void setDaEmpTempFemaleNo(String daEmpTempFemaleNo) {
        this.daEmpTempFemaleNo = daEmpTempFemaleNo;
    }

    public String getDaEmpTempFemalePerc() {
        return daEmpTempFemalePerc;
    }

    public void setDaEmpTempFemalePerc(String daEmpTempFemalePerc) {
        this.daEmpTempFemalePerc = daEmpTempFemalePerc;
    }

    public String getDaEmpTempOtherNo() {
        return daEmpTempOtherNo;
    }

    public void setDaEmpTempOtherNo(String daEmpTempOtherNo) {
        this.daEmpTempOtherNo = daEmpTempOtherNo;
    }

    public String getDaEmpTempOtherPerc() {
        return daEmpTempOtherPerc;
    }

    public void setDaEmpTempOtherPerc(String daEmpTempOtherPerc) {
        this.daEmpTempOtherPerc = daEmpTempOtherPerc;
    }

    public String getDaWorkPermTotal() {
        return daWorkPermTotal;
    }

    public void setDaWorkPermTotal(String daWorkPermTotal) {
        this.daWorkPermTotal = daWorkPermTotal;
    }

    public String getDaWorkPermMaleNo() {
        return daWorkPermMaleNo;
    }

    public void setDaWorkPermMaleNo(String daWorkPermMaleNo) {
        this.daWorkPermMaleNo = daWorkPermMaleNo;
    }

    public String getDaWorkPermMalePerc() {
        return daWorkPermMalePerc;
    }

    public void setDaWorkPermMalePerc(String daWorkPermMalePerc) {
        this.daWorkPermMalePerc = daWorkPermMalePerc;
    }

    public String getDaWorkPermFemaleNo() {
        return daWorkPermFemaleNo;
    }

    public void setDaWorkPermFemaleNo(String daWorkPermFemaleNo) {
        this.daWorkPermFemaleNo = daWorkPermFemaleNo;
    }

    public String getDaWorkPermFemalePerc() {
        return daWorkPermFemalePerc;
    }

    public void setDaWorkPermFemalePerc(String daWorkPermFemalePerc) {
        this.daWorkPermFemalePerc = daWorkPermFemalePerc;
    }

    public String getDaWorkPermOtherNo() {
        return daWorkPermOtherNo;
    }

    public void setDaWorkPermOtherNo(String daWorkPermOtherNo) {
        this.daWorkPermOtherNo = daWorkPermOtherNo;
    }

    public String getDaWorkPermOtherPerc() {
        return daWorkPermOtherPerc;
    }

    public void setDaWorkPermOtherPerc(String daWorkPermOtherPerc) {
        this.daWorkPermOtherPerc = daWorkPermOtherPerc;
    }

    public String getDaWorkTempTotal() {
        return daWorkTempTotal;
    }

    public void setDaWorkTempTotal(String daWorkTempTotal) {
        this.daWorkTempTotal = daWorkTempTotal;
    }

    public String getDaWorkTempMaleNo() {
        return daWorkTempMaleNo;
    }

    public void setDaWorkTempMaleNo(String daWorkTempMaleNo) {
        this.daWorkTempMaleNo = daWorkTempMaleNo;
    }

    public String getDaWorkTempMalePerc() {
        return daWorkTempMalePerc;
    }

    public void setDaWorkTempMalePerc(String daWorkTempMalePerc) {
        this.daWorkTempMalePerc = daWorkTempMalePerc;
    }

    public String getDaWorkTempFemaleNo() {
        return daWorkTempFemaleNo;
    }

    public void setDaWorkTempFemaleNo(String daWorkTempFemaleNo) {
        this.daWorkTempFemaleNo = daWorkTempFemaleNo;
    }

    public String getDaWorkTempFemalePerc() {
        return daWorkTempFemalePerc;
    }

    public void setDaWorkTempFemalePerc(String daWorkTempFemalePerc) {
        this.daWorkTempFemalePerc = daWorkTempFemalePerc;
    }

    public String getDaWorkTempOtherNo() {
        return daWorkTempOtherNo;
    }

    public void setDaWorkTempOtherNo(String daWorkTempOtherNo) {
        this.daWorkTempOtherNo = daWorkTempOtherNo;
    }

    public String getDaWorkTempOtherPerc() {
        return daWorkTempOtherPerc;
    }

    public void setDaWorkTempOtherPerc(String daWorkTempOtherPerc) {
        this.daWorkTempOtherPerc = daWorkTempOtherPerc;
    }

    public String getEmployeeNotesB() {return employeeNotesB;}
    public void setEmployeeNotesB(String employeeNotesB) {this.employeeNotesB = employeeNotesB;}

    public String getEmployeeNotesA() {return employeeNotesA;}
    public void setEmployeeNotesA(String employeeNotesA) {this.employeeNotesA = employeeNotesA;}

    public List<WomenRepresentation> getWomenRepresentationList() { return womenRepresentationList; }
    public void setWomenRepresentationList(List<WomenRepresentation> womenRepresentationList) { this.womenRepresentationList = womenRepresentationList; }

    public String getWomenRepresentationNotes() { return womenRepresentationNotes; }
    public void setWomenRepresentationNotes(String womenRepresentationNotes) { this.womenRepresentationNotes = womenRepresentationNotes; }

    public String getTurnoverEmpCurrMale() { return turnoverEmpCurrMale; }
    public void setTurnoverEmpCurrMale(String turnoverEmpCurrMale) { this.turnoverEmpCurrMale = turnoverEmpCurrMale; }
    public String getTurnoverEmpCurrFemale() { return turnoverEmpCurrFemale; }
    public void setTurnoverEmpCurrFemale(String turnoverEmpCurrFemale) { this.turnoverEmpCurrFemale = turnoverEmpCurrFemale; }
    public String getTurnoverEmpCurrTotal() { return turnoverEmpCurrTotal; }
    public void setTurnoverEmpCurrTotal(String turnoverEmpCurrTotal) { this.turnoverEmpCurrTotal = turnoverEmpCurrTotal; }
    public String getTurnoverWorkCurrMale() { return turnoverWorkCurrMale; }
    public void setTurnoverWorkCurrMale(String turnoverWorkCurrMale) { this.turnoverWorkCurrMale = turnoverWorkCurrMale; }
    public String getTurnoverWorkCurrFemale() { return turnoverWorkCurrFemale; }
    public void setTurnoverWorkCurrFemale(String turnoverWorkCurrFemale) { this.turnoverWorkCurrFemale = turnoverWorkCurrFemale; }
    public String getTurnoverWorkCurrTotal() { return turnoverWorkCurrTotal; }
    public void setTurnoverWorkCurrTotal(String turnoverWorkCurrTotal) { this.turnoverWorkCurrTotal = turnoverWorkCurrTotal; }

    public String getTurnoverEmpPrevMale() { return turnoverEmpPrevMale; }
    public void setTurnoverEmpPrevMale(String turnoverEmpPrevMale) { this.turnoverEmpPrevMale = turnoverEmpPrevMale; }
    public String getTurnoverEmpPrevFemale() { return turnoverEmpPrevFemale; }
    public void setTurnoverEmpPrevFemale(String turnoverEmpPrevFemale) { this.turnoverEmpPrevFemale = turnoverEmpPrevFemale; }
    public String getTurnoverEmpPrevTotal() { return turnoverEmpPrevTotal; }
    public void setTurnoverEmpPrevTotal(String turnoverEmpPrevTotal) { this.turnoverEmpPrevTotal = turnoverEmpPrevTotal; }
    public String getTurnoverWorkPrevMale() { return turnoverWorkPrevMale; }
    public void setTurnoverWorkPrevMale(String turnoverWorkPrevMale) { this.turnoverWorkPrevMale = turnoverWorkPrevMale; }
    public String getTurnoverWorkPrevFemale() { return turnoverWorkPrevFemale; }
    public void setTurnoverWorkPrevFemale(String turnoverWorkPrevFemale) { this.turnoverWorkPrevFemale = turnoverWorkPrevFemale; }
    public String getTurnoverWorkPrevTotal() { return turnoverWorkPrevTotal; }
    public void setTurnoverWorkPrevTotal(String turnoverWorkPrevTotal) { this.turnoverWorkPrevTotal = turnoverWorkPrevTotal; }

    public String getTurnoverEmpPriorMale() { return turnoverEmpPriorMale; }
    public void setTurnoverEmpPriorMale(String turnoverEmpPriorMale) { this.turnoverEmpPriorMale = turnoverEmpPriorMale; }
    public String getTurnoverEmpPriorFemale() { return turnoverEmpPriorFemale; }
    public void setTurnoverEmpPriorFemale(String turnoverEmpPriorFemale) { this.turnoverEmpPriorFemale = turnoverEmpPriorFemale; }
    public String getTurnoverEmpPriorTotal() { return turnoverEmpPriorTotal; }
    public void setTurnoverEmpPriorTotal(String turnoverEmpPriorTotal) { this.turnoverEmpPriorTotal = turnoverEmpPriorTotal; }
    public String getTurnoverWorkPriorMale() { return turnoverWorkPriorMale; }
    public void setTurnoverWorkPriorMale(String turnoverWorkPriorMale) { this.turnoverWorkPriorMale = turnoverWorkPriorMale; }
    public String getTurnoverWorkPriorFemale() { return turnoverWorkPriorFemale; }
    public void setTurnoverWorkPriorFemale(String turnoverWorkPriorFemale) { this.turnoverWorkPriorFemale = turnoverWorkPriorFemale; }
    public String getTurnoverWorkPriorTotal() { return turnoverWorkPriorTotal; }
    public void setTurnoverWorkPriorTotal(String turnoverWorkPriorTotal) { this.turnoverWorkPriorTotal = turnoverWorkPriorTotal; }

    public String getTurnoverNotes() { return turnoverNotes; }
    public void setTurnoverNotes(String turnoverNotes) { this.turnoverNotes = turnoverNotes; }

    public String getFyCurrent() { return fyCurrent; }
    public void setFyCurrent(String fyCurrent) { this.fyCurrent = fyCurrent; }

    public String getFyPrevious() { return fyPrevious; }
    public void setFyPrevious(String fyPrevious) { this.fyPrevious = fyPrevious; }

    public String getFyPrior() { return fyPrior; }
    public void setFyPrior(String fyPrior) { this.fyPrior = fyPrior; }
}