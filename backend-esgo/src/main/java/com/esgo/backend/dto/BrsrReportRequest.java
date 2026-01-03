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

    // --- SECTION V: HOLDING/SUBSIDIARY COMPANIES (Q21) ---
    private List<HoldingCompany> holdingCompanies;
    private String holdingCompanyNote; // If table is not used

    // --- SECTION VI: CSR DETAILS (Q22) ---
    private String csrApplicable; // Yes/No
    private String csrTurnover;
    private String csrNetWorth;

    // --- INNER CLASS FOR Q21 TABLE ---
    public static class HoldingCompany {
        private String name;
        private String type; // Holding/Subsidiary/Associate/JV
        private String sharesHeld;
        private String participateBusinessResponsibility; // Yes/No

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getSharesHeld() { return sharesHeld; }
        public void setSharesHeld(String sharesHeld) { this.sharesHeld = sharesHeld; }
        public String getParticipateBusinessResponsibility() { return participateBusinessResponsibility; }
        public void setParticipateBusinessResponsibility(String participateBusinessResponsibility) { this.participateBusinessResponsibility = participateBusinessResponsibility; }
    }

    // --- SECTION VII: TRANSPARENCY (Q23) ---
    private String complaintsFyCurrentHeader;  // User input for "FY 2023-24"
    private String complaintsFyPreviousHeader; // User input for "FY 2022-23"
    private List<Complaint> complaintsList;

    // --- INNER CLASS FOR COMPLAINTS ---
    public static class Complaint {
        private String stakeholder;
        private String mechanism; // Large text
        // Current FY
        private String currFiled;
        private String currPending;
        private String currRemarks;
        // Previous FY
        private String prevFiled;
        private String prevPending;
        private String prevRemarks;

        // Getters & Setters
        public String getStakeholder() { return stakeholder; }
        public void setStakeholder(String stakeholder) { this.stakeholder = stakeholder; }
        public String getMechanism() { return mechanism; }
        public void setMechanism(String mechanism) { this.mechanism = mechanism; }
        public String getCurrFiled() { return currFiled; }
        public void setCurrFiled(String currFiled) { this.currFiled = currFiled; }
        public String getCurrPending() { return currPending; }
        public void setCurrPending(String currPending) { this.currPending = currPending; }
        public String getCurrRemarks() { return currRemarks; }
        public void setCurrRemarks(String currRemarks) { this.currRemarks = currRemarks; }
        public String getPrevFiled() { return prevFiled; }
        public void setPrevFiled(String prevFiled) { this.prevFiled = prevFiled; }
        public String getPrevPending() { return prevPending; }
        public void setPrevPending(String prevPending) { this.prevPending = prevPending; }
        public String getPrevRemarks() { return prevRemarks; }
        public void setPrevRemarks(String prevRemarks) { this.prevRemarks = prevRemarks; }
    }

    // --- SECTION VII: Q24 (Material Issues) ---
    private String materialIssuesNote; // User note before table
    private List<MaterialIssue> materialIssues;

    // --- INNER CLASS ---
    public static class MaterialIssue {
        private String description;
        private String riskOrOpportunity; // Risk / Opportunity
        private String rationale;
        private String approach;
        private String financialImplications; // Positive / Negative

        // Getters & Setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getRiskOrOpportunity() { return riskOrOpportunity; }
        public void setRiskOrOpportunity(String riskOrOpportunity) { this.riskOrOpportunity = riskOrOpportunity; }
        public String getRationale() { return rationale; }
        public void setRationale(String rationale) { this.rationale = rationale; }
        public String getApproach() { return approach; }
        public void setApproach(String approach) { this.approach = approach; }
        public String getFinancialImplications() { return financialImplications; }
        public void setFinancialImplications(String financialImplications) { this.financialImplications = financialImplications; }
    }

    // --- SECTION B: MANAGEMENT & PROCESS ---
    private String policyGovernanceNote; // For Q7 (Director Statement)
    private List<PolicyMapping> policyMappings;
    private String governanceStatement;
    private String oversightAuthority;

    // --- INNER CLASS ---
    public static class PolicyMapping {
        private String policyName;
        private boolean p1;
        private boolean p2;
        private boolean p3;
        private boolean p4;
        private boolean p5;
        private boolean p6;
        private boolean p7;
        private boolean p8;
        private boolean p9;

        // Getters & Setters
        public String getPolicyName() { return policyName; }
        public void setPolicyName(String policyName) { this.policyName = policyName; }
        public boolean isP1() { return p1; } public void setP1(boolean p1) { this.p1 = p1; }
        public boolean isP2() { return p2; } public void setP2(boolean p2) { this.p2 = p2; }
        public boolean isP3() { return p3; } public void setP3(boolean p3) { this.p3 = p3; }
        public boolean isP4() { return p4; } public void setP4(boolean p4) { this.p4 = p4; }
        public boolean isP5() { return p5; } public void setP5(boolean p5) { this.p5 = p5; }
        public boolean isP6() { return p6; } public void setP6(boolean p6) { this.p6 = p6; }
        public boolean isP7() { return p7; } public void setP7(boolean p7) { this.p7 = p7; }
        public boolean isP8() { return p8; } public void setP8(boolean p8) { this.p8 = p8; }
        public boolean isP9() { return p9; } public void setP9(boolean p9) { this.p9 = p9; }
    }

    // --- SECTION C: PRINCIPLE 1 (Training & Awareness) ---
    private List<TrainingProgram> trainingPrograms;
    private String trainingNote; // Footer note

    // --- INNER CLASS ---
    public static class TrainingProgram {
        private String segment; // Board, KMP, etc.
        private String totalPrograms;
        private String topicsCovered; // Large text
        private String percentageCovered;

        // Getters & Setters
        public String getSegment() { return segment; }
        public void setSegment(String segment) { this.segment = segment; }
        public String getTotalPrograms() { return totalPrograms; }
        public void setTotalPrograms(String totalPrograms) { this.totalPrograms = totalPrograms; }
        public String getTopicsCovered() { return topicsCovered; }
        public void setTopicsCovered(String topicsCovered) { this.topicsCovered = topicsCovered; }
        public String getPercentageCovered() { return percentageCovered; }
        public void setPercentageCovered(String percentageCovered) { this.percentageCovered = percentageCovered; }
    }

    // --- SECTION C: Q2 (Fines/Penalties) ---
    private String legalActionNote;
    private List<LegalAction> legalActions;
    private boolean nonMonetaryNA;

    // --- INNER CLASS ---
    public static class LegalAction {
        private String type; // Penalty, Settlement, Imprisonment, etc.
        private String principle;
        private String agency;
        private String amount;
        private String brief;
        private String appeal; // Yes/No

        // Getters & Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getPrinciple() { return principle; }
        public void setPrinciple(String principle) { this.principle = principle; }
        public String getAgency() { return agency; }
        public void setAgency(String agency) { this.agency = agency; }
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
        public String getBrief() { return brief; }
        public void setBrief(String brief) { this.brief = brief; }
        public String getAppeal() { return appeal; }
        public void setAppeal(String appeal) { this.appeal = appeal; }
    }

    // --- SECTION C: Q3 (Appeals) ---
    private List<AppealDetail> appealDetails;

    // --- INNER CLASS ---
    public static class AppealDetail {
        private String caseDetails;
        private String agencyName;

        public String getCaseDetails() { return caseDetails; }
        public void setCaseDetails(String caseDetails) { this.caseDetails = caseDetails; }
        public String getAgencyName() { return agencyName; }
        public void setAgencyName(String agencyName) { this.agencyName = agencyName; }
    }

    // --- SECTION C: Q4 (Anti-Corruption) ---
    private String antiCorruptionPolicy; // Yes/No
    private String antiCorruptionDetails; // The main text
    private String antiCorruptionLink;    // The URL

    // --- SECTION C: Q5 (Disciplinary Action) ---
    private String disciplinaryFyCurrentHeader;  // e.g. "FY 2023-24"
    private String disciplinaryFyPreviousHeader; // e.g. "FY 2022-23"

    // Current Year Data
    private String discDirectorsCurr;
    private String discKmpsCurr;
    private String discEmployeesCurr;
    private String discWorkersCurr;

    // Previous Year Data
    private String discDirectorsPrev;
    private String discKmpsPrev;
    private String discEmployeesPrev;
    private String discWorkersPrev;

    // --- SECTION C: Q6 (Conflict of Interest) ---
    // Directors
    private String coiDirectorsCurrNum;
    private String coiDirectorsCurrRem;
    private String coiDirectorsPrevNum;
    private String coiDirectorsPrevRem;

    // KMPs
    private String coiKmpsCurrNum;
    private String coiKmpsCurrRem;
    private String coiKmpsPrevNum;
    private String coiKmpsPrevRem;

    // --- SECTION C: Q7 (Corrective Action) ---
    private String correctiveActionDetails;

    // --- SECTION C: LEADERSHIP INDICATOR 1 ---
    private String leadershipIndicatorNote; // OPTIONAL TEXT
    private List<LeadershipAwareness> leadershipAwarenessPrograms; // MANDATORY TABLE

    public static class LeadershipAwareness {
        private String topic;      // Column 2
        private String totalCount; // Column 1
        private String percentage; // Column 3

        // Getters & Setters
        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public String getTotalCount() { return totalCount; }
        public void setTotalCount(String totalCount) { this.totalCount = totalCount; }
        public String getPercentage() { return percentage; }
        public void setPercentage(String percentage) { this.percentage = percentage; }
    }

    // --- SECTION C: LEADERSHIP INDICATOR 2 ---
    private String conflictOfInterestProcess; // Yes/No
    private String conflictOfInterestDetails; // Text

    // --- SECTION C: PRINCIPLE 2 (Sustainable Goods & Services) ---
    // R&D
    private String rdCurrentYear;
    private String rdPreviousYear;
    private String rdDetails;

    // Capex
    private String capexCurrentYear;
    private String capexPreviousYear;
    private String capexDetails;

    // --- NEW FIELD ---
    private String principle2Note;

    // --- SECTION C: PRINCIPLE 2, Q2 (Sustainable Sourcing) ---
    private String sustainableSourcingProcedures; // Yes/No
    private String sustainableSourcingDetails;    // Detailed Text
    private String sustainableSourcingPercentage; // Percentage
    private String sustainableSourcingRemarks;    // Remarks Text

    // --- SECTION C: PRINCIPLE 2, Q3 (Reclaim Processes) ---
    private String reclaimProcessDetails;

    // --- SECTION C: PRINCIPLE 2, Q4 (EPR) ---
    private String eprDetails;

    // --- SECTION C: PRINCIPLE 2, LEADERSHIP IND. 1 (LCA) ---
    private String lcaNote;
    private List<LcaEntry> lcaEntries;

    // --- INNER CLASS ---
    public static class LcaEntry {
        private String nicCode;
        private String productName;
        private String turnoverPercentage;
        private String boundary;
        private String independentAgency; // Yes/No
        private String publicDomainResult; // Yes + Link

        // Getters & Setters
        public String getNicCode() { return nicCode; } public void setNicCode(String s) { this.nicCode = s; }
        public String getProductName() { return productName; } public void setProductName(String s) { this.productName = s; }
        public String getTurnoverPercentage() { return turnoverPercentage; } public void setTurnoverPercentage(String s) { this.turnoverPercentage = s; }
        public String getBoundary() { return boundary; } public void setBoundary(String s) { this.boundary = s; }
        public String getIndependentAgency() { return independentAgency; } public void setIndependentAgency(String s) { this.independentAgency = s; }
        public String getPublicDomainResult() { return publicDomainResult; } public void setPublicDomainResult(String s) { this.publicDomainResult = s; }
    }

    // --- SECTION C: PRINCIPLE 2, Q2 (RISKS) ---
    private String lcaRiskNote; // If user chooses Note format
    private List<LcaRisk> lcaRisks; // If user chooses Table format

    // --- INNER CLASS ---
    public static class LcaRisk {
        private String productName;
        private String riskDescription;
        private String actionTaken;

        // Getters & Setters
        public String getProductName() { return productName; } public void setProductName(String s) { this.productName = s; }
        public String getRiskDescription() { return riskDescription; } public void setRiskDescription(String s) { this.riskDescription = s; }
        public String getActionTaken() { return actionTaken; } public void setActionTaken(String s) { this.actionTaken = s; }
    }

    // --- SECTION C: PRINCIPLE 2, Q3 (Recycled Input Material) ---
    private List<RecycledInput> recycledInputs;
    private String recycledInputNote;

    // --- INNER CLASS ---
    public static class RecycledInput {
        private String materialName;
        private String currentFyPercentage;
        private String previousFyPercentage;

        // Getters & Setters
        public String getMaterialName() { return materialName; } public void setMaterialName(String s) { this.materialName = s; }
        public String getCurrentFyPercentage() { return currentFyPercentage; } public void setCurrentFyPercentage(String s) { this.currentFyPercentage = s; }
        public String getPreviousFyPercentage() { return previousFyPercentage; } public void setPreviousFyPercentage(String s) { this.previousFyPercentage = s; }
    }

    // --- SECTION C: Q4 (Reclaimed at End of Life) ---
    // Plastics
    private String plasticReusedCurr, plasticRecycledCurr, plasticDisposedCurr;
    private String plasticReusedPrev, plasticRecycledPrev, plasticDisposedPrev;
    // E-Waste
    private String ewasteReusedCurr, ewasteRecycledCurr, ewasteDisposedCurr;
    private String ewasteReusedPrev, ewasteRecycledPrev, ewasteDisposedPrev;
    // Hazardous
    private String hazardReusedCurr, hazardRecycledCurr, hazardDisposedCurr;
    private String hazardReusedPrev, hazardRecycledPrev, hazardDisposedPrev;
    // Other
    private String otherReusedCurr, otherRecycledCurr, otherDisposedCurr;
    private String otherReusedPrev, otherRecycledPrev, otherDisposedPrev;

    private String reclaimedWasteNote; // Note for Q4

    // --- SECTION C: Q5 (Reclaimed %) ---
    private List<ReclaimedPercentage> reclaimedPercentages;
    private String reclaimedPercentageNote; // Note for Q5

    // --- INNER CLASS FOR Q5 ---
    public static class ReclaimedPercentage {
        private String category;
        private String percentage;

        public String getCategory() { return category; } public void setCategory(String s) { this.category = s; }
        public String getPercentage() { return percentage; } public void setPercentage(String s) { this.percentage = s; }
    }

    // --- SECTION C: PRINCIPLE 3 (WELL-BEING) ---
    private List<WellBeingRow> employeeWellBeing; // Table 1.a
    private List<WellBeingRow> workerWellBeing;   // Table 1.b
    private String wellBeingNote; // Note after tables

    // --- INNER CLASS FOR TABLE ROW ---
    public static class WellBeingRow {
        private String category; // Male, Female, Total
        private String totalA;

        // Benefits (Number and Percentage)
        private String healthNo, healthPerc;
        private String accidentNo, accidentPerc;
        private String maternityNo, maternityPerc;
        private String paternityNo, paternityPerc;
        private String daycareNo, daycarePerc;

        // Getters & Setters
        public String getCategory() { return category; } public void setCategory(String s) { this.category = s; }
        public String getTotalA() { return totalA; } public void setTotalA(String s) { this.totalA = s; }

        public String getHealthNo() { return healthNo; } public void setHealthNo(String s) { this.healthNo = s; }
        public String getHealthPerc() { return healthPerc; } public void setHealthPerc(String s) { this.healthPerc = s; }

        public String getAccidentNo() { return accidentNo; } public void setAccidentNo(String s) { this.accidentNo = s; }
        public String getAccidentPerc() { return accidentPerc; } public void setAccidentPerc(String s) { this.accidentPerc = s; }

        public String getMaternityNo() { return maternityNo; } public void setMaternityNo(String s) { this.maternityNo = s; }
        public String getMaternityPerc() { return maternityPerc; } public void setMaternityPerc(String s) { this.maternityPerc = s; }

        public String getPaternityNo() { return paternityNo; } public void setPaternityNo(String s) { this.paternityNo = s; }
        public String getPaternityPerc() { return paternityPerc; } public void setPaternityPerc(String s) { this.paternityPerc = s; }

        public String getDaycareNo() { return daycareNo; } public void setDaycareNo(String s) { this.daycareNo = s; }
        public String getDaycarePerc() { return daycarePerc; } public void setDaycarePerc(String s) { this.daycarePerc = s; }
    }

    // --- SECTION C: PRINCIPLE 3, Q2 (Retirement Benefits) ---
    private List<RetirementBenefit> retirementBenefits;
    private String retirementBenefitNote;

    // --- INNER CLASS ---
    public static class RetirementBenefit {
        private String benefits; // PF, Gratuity, etc.

        // Current FY
        private String currEmpCovered; // %
        private String currWorkCovered; // %
        private String currDeducted; // Y/N/NA

        // Previous FY
        private String prevEmpCovered; // %
        private String prevWorkCovered; // %
        private String prevDeducted; // Y/N/NA

        // Getters & Setters
        public String getBenefits() { return benefits; } public void setBenefits(String s) { this.benefits = s; }

        public String getCurrEmpCovered() { return currEmpCovered; } public void setCurrEmpCovered(String s) { this.currEmpCovered = s; }
        public String getCurrWorkCovered() { return currWorkCovered; } public void setCurrWorkCovered(String s) { this.currWorkCovered = s; }
        public String getCurrDeducted() { return currDeducted; } public void setCurrDeducted(String s) { this.currDeducted = s; }

        public String getPrevEmpCovered() { return prevEmpCovered; } public void setPrevEmpCovered(String s) { this.prevEmpCovered = s; }
        public String getPrevWorkCovered() { return prevWorkCovered; } public void setPrevWorkCovered(String s) { this.prevWorkCovered = s; }
        public String getPrevDeducted() { return prevDeducted; } public void setPrevDeducted(String s) { this.prevDeducted = s; }
    }

    // --- SECTION C: PRINCIPLE 3, Q3 (Accessibility) ---
    private String accessibilityDetails;

    // --- SECTION C: PRINCIPLE 3, Q4 (Equal Opportunity) ---
    private String equalOppPolicy; // Yes/No
    private String equalOppLink;   // URL
    private String equalOppDetails; // Detailed text

    // --- SECTION C: PRINCIPLE 3, Q5 (Parental Leave) ---
    // Structure: pl_[Category]_[Gender]_[Metric]

    // Permanent Employees
    private String plEmpMaleReturn, plEmpMaleRetain;
    private String plEmpFemaleReturn, plEmpFemaleRetain;
    private String plEmpTotalReturn, plEmpTotalRetain;

    // Permanent Workers
    private String plWorkMaleReturn, plWorkMaleRetain;
    private String plWorkFemaleReturn, plWorkFemaleRetain;
    private String plWorkTotalReturn, plWorkTotalRetain;

    private String parentalLeaveNote;

    // --- SECTION C: PRINCIPLE 3, Q6 (Grievance Mechanism) ---
    private String grievancePermWorkers;
    private String grievanceTempWorkers;
    private String grievancePermEmployees;
    private String grievanceTempEmployees;

    // --- SECTION C: PRINCIPLE 3, Q7 (Union Membership) ---
    // Structure: union_[Year]_[Category]_[Metric]

    // CURRENT YEAR
    private String unionCurrEmpTotalA, unionCurrEmpUnionB, unionCurrEmpPerc;
    private String unionCurrEmpMaleTotal, unionCurrEmpMaleUnion, unionCurrEmpMalePerc;
    private String unionCurrEmpFemaleTotal, unionCurrEmpFemaleUnion, unionCurrEmpFemalePerc;

    private String unionCurrWorkTotalA, unionCurrWorkUnionB, unionCurrWorkPerc;
    private String unionCurrWorkMaleTotal, unionCurrWorkMaleUnion, unionCurrWorkMalePerc;
    private String unionCurrWorkFemaleTotal, unionCurrWorkFemaleUnion, unionCurrWorkFemalePerc;

    // PREVIOUS YEAR
    private String unionPrevEmpTotalC, unionPrevEmpUnionD, unionPrevEmpPerc;
    private String unionPrevEmpMaleTotal, unionPrevEmpMaleUnion, unionPrevEmpMalePerc;
    private String unionPrevEmpFemaleTotal, unionPrevEmpFemaleUnion, unionPrevEmpFemalePerc;

    private String unionPrevWorkTotalC, unionPrevWorkUnionD, unionPrevWorkPerc;
    private String unionPrevWorkMaleTotal, unionPrevWorkMaleUnion, unionPrevWorkMalePerc;
    private String unionPrevWorkFemaleTotal, unionPrevWorkFemaleUnion, unionPrevWorkFemalePerc;

    private String unionMembershipNote;

    // SECTION C: PRINCIPLE 3, Q8 (TRAINING DETAILS)
    // =========================================================

    private String trainingDetailsNote;

    // --- EMPLOYEES (CURRENT FY) ---
    private String trainEmpMaleTotal, trainEmpMaleHealthNo, trainEmpMaleHealthPerc, trainEmpMaleSkillNo, trainEmpMaleSkillPerc;
    private String trainEmpFemaleTotal, trainEmpFemaleHealthNo, trainEmpFemaleHealthPerc, trainEmpFemaleSkillNo, trainEmpFemaleSkillPerc;
    private String trainEmpGenTotal, trainEmpGenHealthNo, trainEmpGenHealthPerc, trainEmpGenSkillNo, trainEmpGenSkillPerc; // Grand Total Row

    // --- EMPLOYEES (PREVIOUS FY) ---
    private String trainEmpMaleTotalPrev, trainEmpMaleHealthNoPrev, trainEmpMaleHealthPercPrev, trainEmpMaleSkillNoPrev, trainEmpMaleSkillPercPrev;
    private String trainEmpFemaleTotalPrev, trainEmpFemaleHealthNoPrev, trainEmpFemaleHealthPercPrev, trainEmpFemaleSkillNoPrev, trainEmpFemaleSkillPercPrev;
    private String trainEmpGenTotalPrev, trainEmpGenHealthNoPrev, trainEmpGenHealthPercPrev, trainEmpGenSkillNoPrev, trainEmpGenSkillPercPrev;

    // --- WORKERS (CURRENT FY) ---
    private String trainWorkMaleTotal, trainWorkMaleHealthNo, trainWorkMaleHealthPerc, trainWorkMaleSkillNo, trainWorkMaleSkillPerc;
    private String trainWorkFemaleTotal, trainWorkFemaleHealthNo, trainWorkFemaleHealthPerc, trainWorkFemaleSkillNo, trainWorkFemaleSkillPerc;
    private String trainWorkGenTotal, trainWorkGenHealthNo, trainWorkGenHealthPerc, trainWorkGenSkillNo, trainWorkGenSkillPerc;

    // --- WORKERS (PREVIOUS FY) ---
    private String trainWorkMaleTotalPrev, trainWorkMaleHealthNoPrev, trainWorkMaleHealthPercPrev, trainWorkMaleSkillNoPrev, trainWorkMaleSkillPercPrev;
    private String trainWorkFemaleTotalPrev, trainWorkFemaleHealthNoPrev, trainWorkFemaleHealthPercPrev, trainWorkFemaleSkillNoPrev, trainWorkFemaleSkillPercPrev;
    private String trainWorkGenTotalPrev, trainWorkGenHealthNoPrev, trainWorkGenHealthPercPrev, trainWorkGenSkillNoPrev, trainWorkGenSkillPercPrev;

    private String trainEmpOtherTotal, trainEmpOtherHealthNo, trainEmpOtherHealthPerc, trainEmpOtherSkillNo, trainEmpOtherSkillPerc;
    private String trainWorkOtherTotal, trainWorkOtherHealthNo, trainWorkOtherHealthPerc, trainWorkOtherSkillNo, trainWorkOtherSkillPerc;

    private String trainEmpOtherTotalPrev, trainEmpOtherHealthNoPrev, trainEmpOtherHealthPercPrev, trainEmpOtherSkillNoPrev, trainEmpOtherSkillPercPrev;
    private String trainWorkOtherTotalPrev, trainWorkOtherHealthNoPrev, trainWorkOtherHealthPercPrev, trainWorkOtherSkillNoPrev, trainWorkOtherSkillPercPrev;

    // SECTION C: PRINCIPLE 3, Q9 (PERFORMANCE REVIEWS)
    // =========================================================
    private String reviewDetailsNote;

    // EMPLOYEES
    // Current
    private String revEmpMaleTotal, revEmpMaleCovered, revEmpMalePerc;
    private String revEmpFemTotal, revEmpFemCovered, revEmpFemPerc;
    private String revEmpOthTotal, revEmpOthCovered, revEmpOthPerc;
    private String revEmpGenTotal, revEmpGenCovered, revEmpGenPerc;

    // Previous
    private String revEmpMaleTotalPrev, revEmpMaleCoveredPrev, revEmpMalePercPrev;
    private String revEmpFemTotalPrev, revEmpFemCoveredPrev, revEmpFemPercPrev;
    private String revEmpOthTotalPrev, revEmpOthCoveredPrev, revEmpOthPercPrev;
    private String revEmpGenTotalPrev, revEmpGenCoveredPrev, revEmpGenPercPrev;

    // WORKERS
    // Current
    private String revWorkMaleTotal, revWorkMaleCovered, revWorkMalePerc;
    private String revWorkFemTotal, revWorkFemCovered, revWorkFemPerc;
    private String revWorkOthTotal, revWorkOthCovered, revWorkOthPerc;
    private String revWorkGenTotal, revWorkGenCovered, revWorkGenPerc;

    // Previous
    private String revWorkMaleTotalPrev, revWorkMaleCoveredPrev, revWorkMalePercPrev;
    private String revWorkFemTotalPrev, revWorkFemCoveredPrev, revWorkFemPercPrev;
    private String revWorkOthTotalPrev, revWorkOthCoveredPrev, revWorkOthPercPrev;
    private String revWorkGenTotalPrev, revWorkGenCoveredPrev, revWorkGenPercPrev;

    // --- SECTION C: PRINCIPLE 3, Q10 (Health & Safety) ---
    private String healthSafetySystem;      // 10.a
    private String hazardIdentification;    // 10.b
    private String hazardReporting;         // 10.c
    private String medicalAccess;           // 10.d

    // --- SECTION C: Q11 (SAFETY INCIDENTS) ---
    // Structure: safety[Metric][Category][Year]

    // 1. LTIFR
    private String safetyLtifrEmpCurr, safetyLtifrEmpPrev;
    private String safetyLtifrWorkCurr, safetyLtifrWorkPrev;

    // 2. Total Recordable Injuries
    private String safetyTotalInjEmpCurr, safetyTotalInjEmpPrev;
    private String safetyTotalInjWorkCurr, safetyTotalInjWorkPrev;

    // 3. Fatalities
    private String safetyFatalEmpCurr, safetyFatalEmpPrev;
    private String safetyFatalWorkCurr, safetyFatalWorkPrev;

    // 4. High Consequence Injuries
    private String safetyHighConEmpCurr, safetyHighConEmpPrev;
    private String safetyHighConWorkCurr, safetyHighConWorkPrev;

    // 5. Permanent Disabilities (From sample image, usually part of this table)
    private String safetyPermDisEmpCurr, safetyPermDisEmpPrev;
    private String safetyPermDisWorkCurr, safetyPermDisWorkPrev;

    private String safetyIncidentsNote;

    // --- SECTION C: Q12 (Safety Measures) ---
    private List<SafetyMeasure> safetyMeasures;

    // --- INNER CLASS ---
    public static class SafetyMeasure {
        private String heading;
        private String description;

        public String getHeading() { return heading; } public void setHeading(String s) { this.heading = s; }
        public String getDescription() { return description; } public void setDescription(String s) { this.description = s; }
    }

    // --- SECTION C: Q13 (WORKER COMPLAINTS) ---
    // Working Conditions
    private String wcFiledCurr, wcPendingCurr, wcRemarksCurr;
    private String wcFiledPrev, wcPendingPrev, wcRemarksPrev;

    // Health & Safety
    private String hsFiledCurr, hsPendingCurr, hsRemarksCurr;
    private String hsFiledPrev, hsPendingPrev, hsRemarksPrev;

    private String workerComplaintsNote;

    // --- SECTION C: Q14 (ASSESSMENTS) ---
    private String assessmentHealthPerc;   // % for Health & Safety
    private String assessmentWorkingPerc;  // % for Working Conditions
    private String assessmentNote;

    // --- SECTION C: PRINCIPLE 3, Q15 (Corrective Actions) ---
    private String safetyCorrectiveActions;

    // --- SECTION C: PRINCIPLE 3, LEADERSHIP 1 (Life Insurance) ---
    private String lifeInsuranceEmployees; // Yes/No
    private String lifeInsuranceWorkers;   // Yes/No
    private String lifeInsuranceDetails;   // Detailed text

    // --- SECTION C: PRINCIPLE 3, LEADERSHIP 2 (Statutory Dues) ---
    private String statutoryDuesMeasures;

    // --- SECTION C: PRINCIPLE 3, LEADERSHIP 3 (Rehabilitation) ---
    // Affected (Total)
    private String rehabEmpAffCurr, rehabEmpAffPrev;
    private String rehabWorkAffCurr, rehabWorkAffPrev;

    // Rehabilitated (Placed)
    private String rehabEmpPlacedCurr, rehabEmpPlacedPrev;
    private String rehabWorkPlacedCurr, rehabWorkPlacedPrev;

    private String rehabilitationNote;

    // --- SECTION C: PRINCIPLE 3, LEADERSHIP 4 (Transition) ---
    private String transitionAssistanceDetails;

    // --- SECTION C: PRINCIPLE 3, LEADERSHIP 5 (Value Chain Assessment) ---
    private String valueChainAssessmentNote; // Note/Description
    private String vcHealthSafetyPerc;       // % Health & Safety
    private String vcWorkingCondPerc;        // % Working Conditions

    // --- SECTION C: PRINCIPLE 3, LEADERSHIP 6 (Value Chain Actions) ---
    private String vcCorrectiveActionIntro; // The opening paragraph
    private List<ValueChainAction> vcCorrectiveActions; // The bullet points

    // --- INNER CLASS ---
    public static class ValueChainAction {
        private String actionText; // The bullet point text

        public String getActionText() { return actionText; }
        public void setActionText(String s) { this.actionText = s; }
    }

    // --- SECTION C: PRINCIPLE 4 (STAKEHOLDERS) q1 ---
    private String principle4Q1Intro;
    private List<String> principle4Q1Points; // List of standards/frameworks
    private String principle4Q1Conclusion;

    // --- SECTION C: PRINCIPLE 4, Q2 (Stakeholder Engagement) ---
    private List<StakeholderEngagement> stakeholderEngagements;
    private String stakeholderNote; // Optional note below table

    // --- INNER CLASS ---
    public static class StakeholderEngagement {
        private String groupName;
        private String isVulnerable; // Yes/No
        private String channels;     // Multiline text
        private String frequency;    // Multiline text
        private String purpose;      // Multiline text

        // Getters & Setters
        public String getGroupName() { return groupName; } public void setGroupName(String s) { this.groupName = s; }
        public String getIsVulnerable() { return isVulnerable; } public void setIsVulnerable(String s) { this.isVulnerable = s; }
        public String getChannels() { return channels; } public void setChannels(String s) { this.channels = s; }
        public String getFrequency() { return frequency; } public void setFrequency(String s) { this.frequency = s; }
        public String getPurpose() { return purpose; } public void setPurpose(String s) { this.purpose = s; }
    }

    // --- SECTION C: PRINCIPLE 4, LEADERSHIP 1 (Consultation Process) ---
    private String consultationProcessDetails;

    // --- SECTION C: PRINCIPLE 4, LEADERSHIP 2 (Stakeholder Consultation) ---
    private String stakeholderConsultationUsed; // Yes/No
    private String stakeholderConsultationDetails; // Detailed text

    // --- SECTION C: PRINCIPLE 4, LEADERSHIP 3 (Vulnerable Groups) ---
    private String vulnerableGroupIntro;
    private List<String> vulnerableGroupActions; // The numbered list
    private String vulnerableGroupConclusion;

    // --- SECTION C: PRINCIPLE 5, Q1 (Human Rights Training) ---
    // Structure: hr[Category][Type][Year][Metric]

    // Employees
    private String hrEmpPermTotalCurr, hrEmpPermCovCurr, hrEmpPermPercCurr;
    private String hrEmpPermTotalPrev, hrEmpPermCovPrev, hrEmpPermPercPrev;

    private String hrEmpTempTotalCurr, hrEmpTempCovCurr, hrEmpTempPercCurr;
    private String hrEmpTempTotalPrev, hrEmpTempCovPrev, hrEmpTempPercPrev;

    private String hrEmpGrandTotalCurr, hrEmpGrandCovCurr, hrEmpGrandPercCurr;
    private String hrEmpGrandTotalPrev, hrEmpGrandCovPrev, hrEmpGrandPercPrev;

    // Workers
    private String hrWorkPermTotalCurr, hrWorkPermCovCurr, hrWorkPermPercCurr;
    private String hrWorkPermTotalPrev, hrWorkPermCovPrev, hrWorkPermPercPrev;

    private String hrWorkTempTotalCurr, hrWorkTempCovCurr, hrWorkTempPercCurr;
    private String hrWorkTempTotalPrev, hrWorkTempCovPrev, hrWorkTempPercPrev;

    private String hrWorkGrandTotalCurr, hrWorkGrandCovCurr, hrWorkGrandPercCurr;
    private String hrWorkGrandTotalPrev, hrWorkGrandCovPrev, hrWorkGrandPercPrev;

    private String hrTrainingNote;

    // GETTERS & SETTERS FOR SECTION C: P5 Q2 (MINIMUM WAGES)
    // =========================================================
    private String minWageNote;
    // --- EMPLOYEES: PERMANENT (CURRENT FY) ---
    private String mwEmpPermMaleTotalCurr, mwEmpPermMaleEqNoCurr, mwEmpPermMaleEqPercCurr, mwEmpPermMaleMoreNoCurr, mwEmpPermMaleMorePercCurr;
    private String mwEmpPermFemTotalCurr, mwEmpPermFemEqNoCurr, mwEmpPermFemEqPercCurr, mwEmpPermFemMoreNoCurr, mwEmpPermFemMorePercCurr;
    private String mwEmpPermOthTotalCurr, mwEmpPermOthEqNoCurr, mwEmpPermOthEqPercCurr, mwEmpPermOthMoreNoCurr, mwEmpPermOthMorePercCurr;
    // --- EMPLOYEES: PERMANENT (PREVIOUS FY) ---
    private String mwEmpPermMaleTotalPrev, mwEmpPermMaleEqNoPrev, mwEmpPermMaleEqPercPrev, mwEmpPermMaleMoreNoPrev, mwEmpPermMaleMorePercPrev;
    private String mwEmpPermFemTotalPrev, mwEmpPermFemEqNoPrev, mwEmpPermFemEqPercPrev, mwEmpPermFemMoreNoPrev, mwEmpPermFemMorePercPrev;
    private String mwEmpPermOthTotalPrev, mwEmpPermOthEqNoPrev, mwEmpPermOthEqPercPrev, mwEmpPermOthMoreNoPrev, mwEmpPermOthMorePercPrev;
    // --- EMPLOYEES: TEMPORARY (CURRENT FY) ---
    private String mwEmpTempMaleTotalCurr, mwEmpTempMaleEqNoCurr, mwEmpTempMaleEqPercCurr, mwEmpTempMaleMoreNoCurr, mwEmpTempMaleMorePercCurr;
    private String mwEmpTempFemTotalCurr, mwEmpTempFemEqNoCurr, mwEmpTempFemEqPercCurr, mwEmpTempFemMoreNoCurr, mwEmpTempFemMorePercCurr;
    private String mwEmpTempOthTotalCurr, mwEmpTempOthEqNoCurr, mwEmpTempOthEqPercCurr, mwEmpTempOthMoreNoCurr, mwEmpTempOthMorePercCurr;
    // --- EMPLOYEES: TEMPORARY (PREVIOUS FY) ---
    private String mwEmpTempMaleTotalPrev, mwEmpTempMaleEqNoPrev, mwEmpTempMaleEqPercPrev, mwEmpTempMaleMoreNoPrev, mwEmpTempMaleMorePercPrev;
    private String mwEmpTempFemTotalPrev, mwEmpTempFemEqNoPrev, mwEmpTempFemEqPercPrev, mwEmpTempFemMoreNoPrev, mwEmpTempFemMorePercPrev;
    private String mwEmpTempOthTotalPrev, mwEmpTempOthEqNoPrev, mwEmpTempOthEqPercPrev, mwEmpTempOthMoreNoPrev, mwEmpTempOthMorePercPrev;
    // --- WORKERS: PERMANENT (CURRENT FY) ---
    private String mwWorkPermMaleTotalCurr, mwWorkPermMaleEqNoCurr, mwWorkPermMaleEqPercCurr, mwWorkPermMaleMoreNoCurr, mwWorkPermMaleMorePercCurr;
    private String mwWorkPermFemTotalCurr, mwWorkPermFemEqNoCurr, mwWorkPermFemEqPercCurr, mwWorkPermFemMoreNoCurr, mwWorkPermFemMorePercCurr;
    private String mwWorkPermOthTotalCurr, mwWorkPermOthEqNoCurr, mwWorkPermOthEqPercCurr, mwWorkPermOthMoreNoCurr, mwWorkPermOthMorePercCurr;
    // --- WORKERS: PERMANENT (PREVIOUS FY) ---
    private String mwWorkPermMaleTotalPrev, mwWorkPermMaleEqNoPrev, mwWorkPermMaleEqPercPrev, mwWorkPermMaleMoreNoPrev, mwWorkPermMaleMorePercPrev;
    private String mwWorkPermFemTotalPrev, mwWorkPermFemEqNoPrev, mwWorkPermFemEqPercPrev, mwWorkPermFemMoreNoPrev, mwWorkPermFemMorePercPrev;
    private String mwWorkPermOthTotalPrev, mwWorkPermOthEqNoPrev, mwWorkPermOthEqPercPrev, mwWorkPermOthMoreNoPrev, mwWorkPermOthMorePercPrev;
    // --- WORKERS: TEMPORARY (CURRENT FY) ---
    private String mwWorkTempMaleTotalCurr, mwWorkTempMaleEqNoCurr, mwWorkTempMaleEqPercCurr, mwWorkTempMaleMoreNoCurr, mwWorkTempMaleMorePercCurr;
    private String mwWorkTempFemTotalCurr, mwWorkTempFemEqNoCurr, mwWorkTempFemEqPercCurr, mwWorkTempFemMoreNoCurr, mwWorkTempFemMorePercCurr;
    private String mwWorkTempOthTotalCurr, mwWorkTempOthEqNoCurr, mwWorkTempOthEqPercCurr, mwWorkTempOthMoreNoCurr, mwWorkTempOthMorePercCurr;
    // --- WORKERS: TEMPORARY (PREVIOUS FY) ---
    private String mwWorkTempMaleTotalPrev;
    private String mwWorkTempMaleEqNoPrev;
    private String mwWorkTempMaleEqPercPrev;
    private String mwWorkTempMaleMoreNoPrev;
    private String mwWorkTempMaleMorePercPrev;
    private String mwWorkTempFemTotalPrev, mwWorkTempFemEqNoPrev, mwWorkTempFemEqPercPrev, mwWorkTempFemMoreNoPrev, mwWorkTempFemMorePercPrev;
    private String mwWorkTempOthTotalPrev, mwWorkTempOthEqNoPrev, mwWorkTempOthEqPercPrev, mwWorkTempOthMoreNoPrev, mwWorkTempOthMorePercPrev;

    // --- SECTION C: P5 Q3 (REMUNERATION) ---
    // BoD
    private String remBodMaleNum, remBodMaleMedian;
    private String remBodFemNum, remBodFemMedian;

    // KMP
    private String remKmpMaleNum, remKmpMaleMedian;
    private String remKmpFemNum, remKmpFemMedian;

    // Employees (other than BoD/KMP)
    private String remEmpMaleNum, remEmpMaleMedian;
    private String remEmpFemNum, remEmpFemMedian;

    // Workers
    private String remWorkMaleNum, remWorkMaleMedian;
    private String remWorkFemNum, remWorkFemMedian;

    private String remunerationNote;

    // --- SECTION C: P5 Q2.b (Gross Wages to Females) ---
    private String grossWagesFemalePercCurr; // Current FY %
    private String grossWagesFemalePercPrev; // Previous FY %
    private String grossWagesNote;           // Note

    // --- SECTION C: PRINCIPLE 5, Q4 (Focal Point) ---
    private String humanRightsFocalPoint; // Yes/No
    private String humanRightsFocalDetails; // Detailed text

    // --- SECTION C: PRINCIPLE 5, Q5 (Grievance Mechanisms) ---
    private String humanRightsGrievanceMechanism;

    // --- SECTION C: PRINCIPLE 5, Q6 (Complaints) ---

    // 1. Sexual Harassment
    private String compShFiledCurr, compShPendingCurr, compShRemarksCurr;
    private String compShFiledPrev, compShPendingPrev, compShRemarksPrev;

    // 2. Discrimination
    private String compDiscrimFiledCurr, compDiscrimPendingCurr, compDiscrimRemarksCurr;
    private String compDiscrimFiledPrev, compDiscrimPendingPrev, compDiscrimRemarksPrev;

    // 3. Child Labour
    private String compChildFiledCurr, compChildPendingCurr, compChildRemarksCurr;
    private String compChildFiledPrev, compChildPendingPrev, compChildRemarksPrev;

    // 4. Forced Labour
    private String compForcedFiledCurr, compForcedPendingCurr, compForcedRemarksCurr;
    private String compForcedFiledPrev, compForcedPendingPrev, compForcedRemarksPrev;

    // 5. Wages
    private String compWagesFiledCurr, compWagesPendingCurr, compWagesRemarksCurr;
    private String compWagesFiledPrev, compWagesPendingPrev, compWagesRemarksPrev;

    // 6. Other HR Issues
    private String compOtherHrFiledCurr, compOtherHrPendingCurr, compOtherHrRemarksCurr;
    private String compOtherHrFiledPrev;
    private String compOtherHrPendingPrev;

    // --- SECTION C: PRINCIPLE 5, Q7 (POSH) ---
    // Row 1: Total Complaints
    private String poshTotalCurr, poshTotalPrev;
    // Row 2: Percentage
    private String poshPercCurr, poshPercPrev;
    // Row 3: Upheld
    private String poshUpheldCurr, poshUpheldPrev;

    private String poshNote;

    // --- SECTION C: PRINCIPLE 5, Q8 (Protection Mechanisms) ---
    private String protectionMechanismsIntro;
    private List<String> protectionMechanismsList;

    // --- SECTION C: PRINCIPLE 5, Q9 (Contracts) ---
    private String humanRightsContracts; // Yes/No
    private String humanRightsContractsDetails;

    // --- SECTION C: PRINCIPLE 5, Q10 (Assessments) ---
    private String assessChildLabourPerc;
    private String assessForcedLabourPerc;
    private String assessSexualHarassmentPerc;
    private String assessDiscriminationPerc;
    private String assessWagesPerc;
    private String assessOthersPerc;

    private String assessmentsP5Note; // Note for Q10

    //q11
    private String assessCorrectiveIntro;
    private List<String> assessCorrectiveActions; // List for i., ii., iii

    // --- SECTION C: PRINCIPLE 5 LEADERSHIP INDICATORS ---

    // Q1: Business Process Modifications
    private String p5LeadProcessModIntro;
    private List<String> p5LeadProcessModList; // The list (i, ii, iii...)

    // Q2: Human Rights Due Diligence
    private String p5LeadDueDiligenceScope; // The main text paragraph
    private List<String> p5LeadDueDiligenceIssues; // List of 14 issues
    private List<String> p5LeadDueDiligenceHolders; // List of 6 rights holders

    // Q3: Premises Accessibility
    private String p5LeadPremisesAccess;

    // Q4: Value Chain Assessment
    private String p5LeadValueChainAssessment;

    // Q5: Value Chain Corrective Actions
    private String p5LeadValueChainCorrectiveActions;

    // =================================================================
    // SECTION C: PRINCIPLE 6 (ENVIRONMENT)
    // =================================================================

    // 1. Energy
    private String p6ElecConsumCurr, p6ElecConsumPrev;
    private String p6FuelConsumCurr, p6FuelConsumPrev;
    private String p6EnergyOtherCurr, p6EnergyOtherPrev;
    private String p6EnergyTotalCurr, p6EnergyTotalPrev;
    private String p6EnergyIntensityCurr, p6EnergyIntensityPrev;
    private String p6EnergyNote;

    // 2. Emissions
    private String p6Scope1Curr, p6Scope1Prev;
    private String p6Scope2Curr, p6Scope2Prev;
    private String p6ScopeTotalCurr, p6ScopeTotalPrev;
    private String p6EmissionIntensityCurr, p6EmissionIntensityPrev;
    private String p6EmissionNote;

    // 3. Water
    private String p6WaterWithdrawalCurr, p6WaterWithdrawalPrev;
    private String p6WaterConsumCurr, p6WaterConsumPrev;
    private String p6WaterIntensityCurr, p6WaterIntensityPrev;
    private String p6WaterNote;

    // 4. Waste
    private List<WasteManagementRow> p6WasteManagementList;
    private String p6WasteNote;

    public static class WasteManagementRow {
        private String category;
        private String currGenerated, currRecycled, currDisposed;
        private String prevGenerated, prevRecycled, prevDisposed;

        // Getters & Setters
        public String getCategory() { return category; } public void setCategory(String s) { this.category = s; }
        public String getCurrGenerated() { return currGenerated; } public void setCurrGenerated(String s) { this.currGenerated = s; }
        public String getCurrRecycled() { return currRecycled; } public void setCurrRecycled(String s) { this.currRecycled = s; }
        public String getCurrDisposed() { return currDisposed; } public void setCurrDisposed(String s) { this.currDisposed = s; }
        public String getPrevGenerated() { return prevGenerated; } public void setPrevGenerated(String s) { this.prevGenerated = s; }
        public String getPrevRecycled() { return prevRecycled; } public void setPrevRecycled(String s) { this.prevRecycled = s; }
        public String getPrevDisposed() { return prevDisposed; } public void setPrevDisposed(String s) { this.prevDisposed = s; }
    }

    // =================================================================
    // SECTION C: PRINCIPLE 7 (PUBLIC POLICY)
    // =================================================================

    private String p7AffiliationsCount;
    private List<TradeAssociation> p7TradeAssociations;
    private String p7AntiCompetitiveDetails;

    // Leadership
    private List<PublicPolicyPosition> p7PublicPolicyPositions;

    public static class TradeAssociation {
        private String name;
        private String reach; // State/National
        public String getName() { return name; } public void setName(String s) { this.name = s; }
        public String getReach() { return reach; } public void setReach(String s) { this.reach = s; }
    }

    public static class PublicPolicyPosition {
        private String policyAdvocated;
        private String methodResorted;
        private String webLink;
        public String getPolicyAdvocated() { return policyAdvocated; } public void setPolicyAdvocated(String s) { this.policyAdvocated = s; }
        public String getMethodResorted() { return methodResorted; } public void setMethodResorted(String s) { this.methodResorted = s; }
        public String getWebLink() { return webLink; } public void setWebLink(String s) { this.webLink = s; }
    }

    // =================================================================
    // SECTION C: PRINCIPLE 8 (INCLUSIVE GROWTH)
    // =================================================================

    private String p8SiaDetails;
    private List<RandRProject> p8RandRProjects;
    private String p8GrievanceMechanism;

    // Input Material Sourcing
    private String p8InputMsmeCurr, p8InputMsmePrev;
    private String p8InputIndiaCurr, p8InputIndiaPrev;

    // Leadership
    private List<CsrAspirationalDistrict> p8AspirationalDistricts;
    private String p8PreferentialProcurement;
    private String p8MarginalizedGroups;
    private String p8ProcurementPercentage;
    private String p8IpBenefits;
    private List<CsrBeneficiary> p8CsrBeneficiaries;

    public static class RandRProject {
        private String projectName;
        private String state;
        private String district;
        private String noOfPafs;
        private String percCovered;
        // Getters & Setters
        public String getProjectName() { return projectName; } public void setProjectName(String s) { this.projectName = s; }
        public String getState() { return state; } public void setState(String s) { this.state = s; }
        public String getDistrict() { return district; } public void setDistrict(String s) { this.district = s; }
        public String getNoOfPafs() { return noOfPafs; } public void setNoOfPafs(String s) { this.noOfPafs = s; }
        public String getPercCovered() { return percCovered; } public void setPercCovered(String s) { this.percCovered = s; }
    }

    public static class CsrAspirationalDistrict {
        private String state;
        private String district;
        private String amountSpent;
        // Getters & Setters
        public String getState() { return state; } public void setState(String s) { this.state = s; }
        public String getDistrict() { return district; } public void setDistrict(String s) { this.district = s; }
        public String getAmountSpent() { return amountSpent; } public void setAmountSpent(String s) { this.amountSpent = s; }
    }

    public static class CsrBeneficiary {
        private String csrProject;
        private String noBenefitted;
        private String percVulnerable;
        // Getters & Setters
        public String getCsrProject() { return csrProject; } public void setCsrProject(String s) { this.csrProject = s; }
        public String getNoBenefitted() { return noBenefitted; } public void setNoBenefitted(String s) { this.noBenefitted = s; }
        public String getPercVulnerable() { return percVulnerable; } public void setPercVulnerable(String s) { this.percVulnerable = s; }
    }

    // =================================================================
    // SECTION C: PRINCIPLE 9 (CONSUMER VALUE)
    // =================================================================

    private String p9ConsumerComplaintMech;

    // Complaints Table
    private String p9DataPrivacyCurr, p9DataPrivacyPrev;
    private String p9AdvertisingCurr, p9AdvertisingPrev;
    private String p9CyberCurr, p9CyberPrev;
    private String p9DeliveryCurr, p9DeliveryPrev;
    private String p9RestrictiveCurr, p9RestrictivePrev;
    private String p9UnfairCurr, p9UnfairPrev;
    private String p9OtherCurr, p9OtherPrev;

    private String p9RecallVoluntary;
    private String p9RecallForced;
    private String p9CyberSecurityPolicy;
    private String p9CorrectiveActions;

    // Leadership
    private String p9InfoChannels;
    private String p9SafeUsageSteps;
    private String p9DisruptionInfo;
    private String p9ProductInfoDisplay;
    private String p9CustomerSatSurvey;

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

    // --- GETTERS AND SETTERS FOR SECTION V & VI ---
    public List<HoldingCompany> getHoldingCompanies() { return holdingCompanies; }
    public void setHoldingCompanies(List<HoldingCompany> holdingCompanies) { this.holdingCompanies = holdingCompanies; }

    public String getHoldingCompanyNote() { return holdingCompanyNote; }
    public void setHoldingCompanyNote(String holdingCompanyNote) { this.holdingCompanyNote = holdingCompanyNote; }

    public String getCsrApplicable() { return csrApplicable; }
    public void setCsrApplicable(String csrApplicable) { this.csrApplicable = csrApplicable; }

    public String getCsrTurnover() { return csrTurnover; }
    public void setCsrTurnover(String csrTurnover) { this.csrTurnover = csrTurnover; }

    public String getCsrNetWorth() { return csrNetWorth; }
    public void setCsrNetWorth(String csrNetWorth) { this.csrNetWorth = csrNetWorth; }

    public String getComplaintsFyCurrentHeader() { return complaintsFyCurrentHeader; }
    public void setComplaintsFyCurrentHeader(String complaintsFyCurrentHeader) { this.complaintsFyCurrentHeader = complaintsFyCurrentHeader; }
    public String getComplaintsFyPreviousHeader() { return complaintsFyPreviousHeader; }
    public void setComplaintsFyPreviousHeader(String complaintsFyPreviousHeader) { this.complaintsFyPreviousHeader = complaintsFyPreviousHeader; }
    public List<Complaint> getComplaintsList() { return complaintsList; }
    public void setComplaintsList(List<Complaint> complaintsList) { this.complaintsList = complaintsList; }

    public String getMaterialIssuesNote() { return materialIssuesNote; }
    public void setMaterialIssuesNote(String materialIssuesNote) { this.materialIssuesNote = materialIssuesNote; }
    public List<MaterialIssue> getMaterialIssues() { return materialIssues; }
    public void setMaterialIssues(List<MaterialIssue> materialIssues) { this.materialIssues = materialIssues; }

    public List<PolicyMapping> getPolicyMappings() { return policyMappings; }
    public void setPolicyMappings(List<PolicyMapping> policyMappings) { this.policyMappings = policyMappings; }
    public String getPolicyGovernanceNote() { return policyGovernanceNote; }
    public void setPolicyGovernanceNote(String policyGovernanceNote) { this.policyGovernanceNote = policyGovernanceNote; }

    public String getGovernanceStatement() { return governanceStatement; } public void setGovernanceStatement(String s) { this.governanceStatement = s; }
    public String getOversightAuthority() { return oversightAuthority; } public void setOversightAuthority(String s) { this.oversightAuthority = s; }

    // --- SECTION C: PRINCIPLE 1 (Training & Awareness) ---
    public List<TrainingProgram> getTrainingPrograms() { return trainingPrograms; }
    public void setTrainingPrograms(List<TrainingProgram> trainingPrograms) { this.trainingPrograms = trainingPrograms; }
    public String getTrainingNote() { return trainingNote; }
    public void setTrainingNote(String trainingNote) { this.trainingNote = trainingNote; }

    //question 2
    public String getLegalActionNote() { return legalActionNote; }
    public void setLegalActionNote(String legalActionNote) { this.legalActionNote = legalActionNote; }
    public List<LegalAction> getLegalActions() { return legalActions; }
    public void setLegalActions(List<LegalAction> legalActions) { this.legalActions = legalActions; }
    public boolean isNonMonetaryNA() { return nonMonetaryNA; }
    public void setNonMonetaryNA(boolean nonMonetaryNA) { this.nonMonetaryNA = nonMonetaryNA; }

    //question 3
    public List<AppealDetail> getAppealDetails() { return appealDetails; }
    public void setAppealDetails(List<AppealDetail> appealDetails) { this.appealDetails = appealDetails; }

    //question 4
    public String getAntiCorruptionPolicy() { return antiCorruptionPolicy; }
    public void setAntiCorruptionPolicy(String antiCorruptionPolicy) { this.antiCorruptionPolicy = antiCorruptionPolicy; }

    public String getAntiCorruptionDetails() { return antiCorruptionDetails; }
    public void setAntiCorruptionDetails(String antiCorruptionDetails) { this.antiCorruptionDetails = antiCorruptionDetails; }

    public String getAntiCorruptionLink() { return antiCorruptionLink; }
    public void setAntiCorruptionLink(String antiCorruptionLink) { this.antiCorruptionLink = antiCorruptionLink; }

    public String getDisciplinaryFyCurrentHeader() { return disciplinaryFyCurrentHeader; }
    public void setDisciplinaryFyCurrentHeader(String disciplinaryFyCurrentHeader) { this.disciplinaryFyCurrentHeader = disciplinaryFyCurrentHeader; }
    public String getDisciplinaryFyPreviousHeader() { return disciplinaryFyPreviousHeader; }
    public void setDisciplinaryFyPreviousHeader(String disciplinaryFyPreviousHeader) { this.disciplinaryFyPreviousHeader = disciplinaryFyPreviousHeader; }

    public String getDiscDirectorsCurr() { return discDirectorsCurr; }
    public void setDiscDirectorsCurr(String discDirectorsCurr) { this.discDirectorsCurr = discDirectorsCurr; }
    public String getDiscKmpsCurr() { return discKmpsCurr; }
    public void setDiscKmpsCurr(String discKmpsCurr) { this.discKmpsCurr = discKmpsCurr; }
    public String getDiscEmployeesCurr() { return discEmployeesCurr; }
    public void setDiscEmployeesCurr(String discEmployeesCurr) { this.discEmployeesCurr = discEmployeesCurr; }
    public String getDiscWorkersCurr() { return discWorkersCurr; }
    public void setDiscWorkersCurr(String discWorkersCurr) { this.discWorkersCurr = discWorkersCurr; }

    public String getDiscDirectorsPrev() { return discDirectorsPrev; }
    public void setDiscDirectorsPrev(String discDirectorsPrev) { this.discDirectorsPrev = discDirectorsPrev; }
    public String getDiscKmpsPrev() { return discKmpsPrev; }
    public void setDiscKmpsPrev(String discKmpsPrev) { this.discKmpsPrev = discKmpsPrev; }
    public String getDiscEmployeesPrev() { return discEmployeesPrev; }
    public void setDiscEmployeesPrev(String discEmployeesPrev) { this.discEmployeesPrev = discEmployeesPrev; }
    public String getDiscWorkersPrev() { return discWorkersPrev; }
    public void setDiscWorkersPrev(String discWorkersPrev) { this.discWorkersPrev = discWorkersPrev; }

    //question 6
    public String getCoiDirectorsCurrNum() { return coiDirectorsCurrNum; }
    public void setCoiDirectorsCurrNum(String coiDirectorsCurrNum) { this.coiDirectorsCurrNum = coiDirectorsCurrNum; }
    public String getCoiDirectorsCurrRem() { return coiDirectorsCurrRem; }
    public void setCoiDirectorsCurrRem(String coiDirectorsCurrRem) { this.coiDirectorsCurrRem = coiDirectorsCurrRem; }
    public String getCoiDirectorsPrevNum() { return coiDirectorsPrevNum; }
    public void setCoiDirectorsPrevNum(String coiDirectorsPrevNum) { this.coiDirectorsPrevNum = coiDirectorsPrevNum; }
    public String getCoiDirectorsPrevRem() { return coiDirectorsPrevRem; }
    public void setCoiDirectorsPrevRem(String coiDirectorsPrevRem) { this.coiDirectorsPrevRem = coiDirectorsPrevRem; }

    public String getCoiKmpsCurrNum() { return coiKmpsCurrNum; }
    public void setCoiKmpsCurrNum(String coiKmpsCurrNum) { this.coiKmpsCurrNum = coiKmpsCurrNum; }
    public String getCoiKmpsCurrRem() { return coiKmpsCurrRem; }
    public void setCoiKmpsCurrRem(String coiKmpsCurrRem) { this.coiKmpsCurrRem = coiKmpsCurrRem; }
    public String getCoiKmpsPrevNum() { return coiKmpsPrevNum; }
    public void setCoiKmpsPrevNum(String coiKmpsPrevNum) { this.coiKmpsPrevNum = coiKmpsPrevNum; }
    public String getCoiKmpsPrevRem() { return coiKmpsPrevRem; }
    public void setCoiKmpsPrevRem(String coiKmpsPrevRem) { this.coiKmpsPrevRem = coiKmpsPrevRem; }

    //question 7
    public String getCorrectiveActionDetails() { return correctiveActionDetails; }
    public void setCorrectiveActionDetails(String correctiveActionDetails) { this.correctiveActionDetails = correctiveActionDetails; }

    //SECTION C LEADERSHIP INDICATORS 1
    public String getLeadershipIndicatorNote() { return leadershipIndicatorNote; }
    public void setLeadershipIndicatorNote(String leadershipIndicatorNote) { this.leadershipIndicatorNote = leadershipIndicatorNote; }
    public List<LeadershipAwareness> getLeadershipAwarenessPrograms() { return leadershipAwarenessPrograms; }
    public void setLeadershipAwarenessPrograms(List<LeadershipAwareness> leadershipAwarenessPrograms) { this.leadershipAwarenessPrograms = leadershipAwarenessPrograms; }

    //SECTION C LEADERSHIP INDICATORS 2
    public String getConflictOfInterestProcess() { return conflictOfInterestProcess; }
    public void setConflictOfInterestProcess(String conflictOfInterestProcess) { this.conflictOfInterestProcess = conflictOfInterestProcess; }
    public String getConflictOfInterestDetails() { return conflictOfInterestDetails; }
    public void setConflictOfInterestDetails(String conflictOfInterestDetails) { this.conflictOfInterestDetails = conflictOfInterestDetails; }

    // --- SECTION C: PRINCIPLE 2 (Sustainable Goods & Services) ---
    public String getRdCurrentYear() { return rdCurrentYear; } public void setRdCurrentYear(String s) { this.rdCurrentYear = s; }
    public String getRdPreviousYear() { return rdPreviousYear; } public void setRdPreviousYear(String s) { this.rdPreviousYear = s; }
    public String getRdDetails() { return rdDetails; } public void setRdDetails(String s) { this.rdDetails = s; }

    public String getCapexCurrentYear() { return capexCurrentYear; } public void setCapexCurrentYear(String s) { this.capexCurrentYear = s; }
    public String getCapexPreviousYear() { return capexPreviousYear; } public void setCapexPreviousYear(String s) { this.capexPreviousYear = s; }
    public String getCapexDetails() { return capexDetails; } public void setCapexDetails(String s) { this.capexDetails = s; }


    public String getPrinciple2Note() { return principle2Note; }
    public void setPrinciple2Note(String principle2Note) { this.principle2Note = principle2Note; }

    //--section c : principle 2
    public String getSustainableSourcingProcedures() { return sustainableSourcingProcedures; }
    public void setSustainableSourcingProcedures(String s) { this.sustainableSourcingProcedures = s; }

    public String getSustainableSourcingDetails() { return sustainableSourcingDetails; }
    public void setSustainableSourcingDetails(String s) { this.sustainableSourcingDetails = s; }

    public String getSustainableSourcingPercentage() { return sustainableSourcingPercentage; }
    public void setSustainableSourcingPercentage(String s) { this.sustainableSourcingPercentage = s; }

    public String getSustainableSourcingRemarks() { return sustainableSourcingRemarks; }
    public void setSustainableSourcingRemarks(String s) { this.sustainableSourcingRemarks = s; }

    //--section c : principle 2 question 3
    public String getReclaimProcessDetails() { return reclaimProcessDetails; }
    public void setReclaimProcessDetails(String s) { this.reclaimProcessDetails = s; }

    //--section c : principle 2 question 4
    public String getEprDetails() { return eprDetails; }
    public void setEprDetails(String eprDetails) { this.eprDetails = eprDetails; }

    //--section c : princple 2 leadership indicators q1
    public String getLcaNote() { return lcaNote; } public void setLcaNote(String s) { this.lcaNote = s; }
    public List<LcaEntry> getLcaEntries() { return lcaEntries; } public void setLcaEntries(List<LcaEntry> list) { this.lcaEntries = list; }

    //--section c : princple 2 leadership indicators q2
    public String getLcaRiskNote() { return lcaRiskNote; } public void setLcaRiskNote(String s) { this.lcaRiskNote = s; }
    public List<LcaRisk> getLcaRisks() { return lcaRisks; } public void setLcaRisks(List<LcaRisk> list) { this.lcaRisks = list; }

    //--section c : princple 2 leadership indicators q3
    public List<RecycledInput> getRecycledInputs() { return recycledInputs; }
    public void setRecycledInputs(List<RecycledInput> list) { this.recycledInputs = list; }
    public String getRecycledInputNote() { return recycledInputNote; }
    public void setRecycledInputNote(String s) { this.recycledInputNote = s; }


    // GETTERS & SETTERS FOR SECTION C (Q4 & Q5)
    // --- Q4: Plastics ---
    public String getPlasticReusedCurr() { return plasticReusedCurr; }
    public void setPlasticReusedCurr(String plasticReusedCurr) { this.plasticReusedCurr = plasticReusedCurr; }

    public String getPlasticRecycledCurr() { return plasticRecycledCurr; }
    public void setPlasticRecycledCurr(String plasticRecycledCurr) { this.plasticRecycledCurr = plasticRecycledCurr; }

    public String getPlasticDisposedCurr() { return plasticDisposedCurr; }
    public void setPlasticDisposedCurr(String plasticDisposedCurr) { this.plasticDisposedCurr = plasticDisposedCurr; }

    public String getPlasticReusedPrev() { return plasticReusedPrev; }
    public void setPlasticReusedPrev(String plasticReusedPrev) { this.plasticReusedPrev = plasticReusedPrev; }

    public String getPlasticRecycledPrev() { return plasticRecycledPrev; }
    public void setPlasticRecycledPrev(String plasticRecycledPrev) { this.plasticRecycledPrev = plasticRecycledPrev; }

    public String getPlasticDisposedPrev() { return plasticDisposedPrev; }
    public void setPlasticDisposedPrev(String plasticDisposedPrev) { this.plasticDisposedPrev = plasticDisposedPrev; }

    // --- Q4: E-Waste ---
    public String getEwasteReusedCurr() { return ewasteReusedCurr; }
    public void setEwasteReusedCurr(String ewasteReusedCurr) { this.ewasteReusedCurr = ewasteReusedCurr; }

    public String getEwasteRecycledCurr() { return ewasteRecycledCurr; }
    public void setEwasteRecycledCurr(String ewasteRecycledCurr) { this.ewasteRecycledCurr = ewasteRecycledCurr; }

    public String getEwasteDisposedCurr() { return ewasteDisposedCurr; }
    public void setEwasteDisposedCurr(String ewasteDisposedCurr) { this.ewasteDisposedCurr = ewasteDisposedCurr; }

    public String getEwasteReusedPrev() { return ewasteReusedPrev; }
    public void setEwasteReusedPrev(String ewasteReusedPrev) { this.ewasteReusedPrev = ewasteReusedPrev; }

    public String getEwasteRecycledPrev() { return ewasteRecycledPrev; }
    public void setEwasteRecycledPrev(String ewasteRecycledPrev) { this.ewasteRecycledPrev = ewasteRecycledPrev; }

    public String getEwasteDisposedPrev() { return ewasteDisposedPrev; }
    public void setEwasteDisposedPrev(String ewasteDisposedPrev) { this.ewasteDisposedPrev = ewasteDisposedPrev; }

    // --- Q4: Hazardous Waste ---
    public String getHazardReusedCurr() { return hazardReusedCurr; }
    public void setHazardReusedCurr(String hazardReusedCurr) { this.hazardReusedCurr = hazardReusedCurr; }

    public String getHazardRecycledCurr() { return hazardRecycledCurr; }
    public void setHazardRecycledCurr(String hazardRecycledCurr) { this.hazardRecycledCurr = hazardRecycledCurr; }

    public String getHazardDisposedCurr() { return hazardDisposedCurr; }
    public void setHazardDisposedCurr(String hazardDisposedCurr) { this.hazardDisposedCurr = hazardDisposedCurr; }

    public String getHazardReusedPrev() { return hazardReusedPrev; }
    public void setHazardReusedPrev(String hazardReusedPrev) { this.hazardReusedPrev = hazardReusedPrev; }

    public String getHazardRecycledPrev() { return hazardRecycledPrev; }
    public void setHazardRecycledPrev(String hazardRecycledPrev) { this.hazardRecycledPrev = hazardRecycledPrev; }

    public String getHazardDisposedPrev() { return hazardDisposedPrev; }
    public void setHazardDisposedPrev(String hazardDisposedPrev) { this.hazardDisposedPrev = hazardDisposedPrev; }

    // --- Q4: Other Waste ---
    public String getOtherReusedCurr() { return otherReusedCurr; }
    public void setOtherReusedCurr(String otherReusedCurr) { this.otherReusedCurr = otherReusedCurr; }

    public String getOtherRecycledCurr() { return otherRecycledCurr; }
    public void setOtherRecycledCurr(String otherRecycledCurr) { this.otherRecycledCurr = otherRecycledCurr; }

    public String getOtherDisposedCurr() { return otherDisposedCurr; }
    public void setOtherDisposedCurr(String otherDisposedCurr) { this.otherDisposedCurr = otherDisposedCurr; }

    public String getOtherReusedPrev() { return otherReusedPrev; }
    public void setOtherReusedPrev(String otherReusedPrev) { this.otherReusedPrev = otherReusedPrev; }

    public String getOtherRecycledPrev() { return otherRecycledPrev; }
    public void setOtherRecycledPrev(String otherRecycledPrev) { this.otherRecycledPrev = otherRecycledPrev; }

    public String getOtherDisposedPrev() { return otherDisposedPrev; }
    public void setOtherDisposedPrev(String otherDisposedPrev) { this.otherDisposedPrev = otherDisposedPrev; }

    // --- Q4 Note ---
    public String getReclaimedWasteNote() { return reclaimedWasteNote; }
    public void setReclaimedWasteNote(String reclaimedWasteNote) { this.reclaimedWasteNote = reclaimedWasteNote; }

    // --- Q5: Reclaimed Percentages (List) ---
    public List<ReclaimedPercentage> getReclaimedPercentages() { return reclaimedPercentages; }
    public void setReclaimedPercentages(List<ReclaimedPercentage> reclaimedPercentages) { this.reclaimedPercentages = reclaimedPercentages; }

    // --- Q5 Note ---
    public String getReclaimedPercentageNote() { return reclaimedPercentageNote; }
    public void setReclaimedPercentageNote(String reclaimedPercentageNote) { this.reclaimedPercentageNote = reclaimedPercentageNote; }

    //--- PRINCIPLE 3 Q1 ---
    public List<WellBeingRow> getEmployeeWellBeing() { return employeeWellBeing; }
    public void setEmployeeWellBeing(List<WellBeingRow> list) { this.employeeWellBeing = list; }

    public List<WellBeingRow> getWorkerWellBeing() { return workerWellBeing; }
    public void setWorkerWellBeing(List<WellBeingRow> list) { this.workerWellBeing = list; }

    public String getWellBeingNote() { return wellBeingNote; }
    public void setWellBeingNote(String s) { this.wellBeingNote = s; }

    //--- PRINCIPLE 3 Q2 ---
    public List<RetirementBenefit> getRetirementBenefits() { return retirementBenefits; } public void setRetirementBenefits(List<RetirementBenefit> l) { this.retirementBenefits = l; }
    public String getRetirementBenefitNote() { return retirementBenefitNote; } public void setRetirementBenefitNote(String s) { this.retirementBenefitNote = s; }

    //--- PRINCIPLE 3 Q3 ---
    public String getAccessibilityDetails() { return accessibilityDetails; }
    public void setAccessibilityDetails(String s) { this.accessibilityDetails = s; }

    //--- PRINCIPLE 3 Q4 ---
    public String getEqualOppPolicy() { return equalOppPolicy; }
    public void setEqualOppPolicy(String s) { this.equalOppPolicy = s; }

    public String getEqualOppLink() { return equalOppLink; }
    public void setEqualOppLink(String s) { this.equalOppLink = s; }

    public String getEqualOppDetails() { return equalOppDetails; }
    public void setEqualOppDetails(String s) { this.equalOppDetails = s; }

    // GETTERS & SETTERS FOR SECTION C: Q5 (PARENTAL)
    // ===============================================

    // --- Permanent Employees (Male) ---
    public String getPlEmpMaleReturn() { return plEmpMaleReturn; }
    public void setPlEmpMaleReturn(String plEmpMaleReturn) { this.plEmpMaleReturn = plEmpMaleReturn; }

    public String getPlEmpMaleRetain() { return plEmpMaleRetain; }
    public void setPlEmpMaleRetain(String plEmpMaleRetain) { this.plEmpMaleRetain = plEmpMaleRetain; }

    // --- Permanent Employees (Female) ---
    public String getPlEmpFemaleReturn() { return plEmpFemaleReturn; }
    public void setPlEmpFemaleReturn(String plEmpFemaleReturn) { this.plEmpFemaleReturn = plEmpFemaleReturn; }

    public String getPlEmpFemaleRetain() { return plEmpFemaleRetain; }
    public void setPlEmpFemaleRetain(String plEmpFemaleRetain) { this.plEmpFemaleRetain = plEmpFemaleRetain; }

    // --- Permanent Employees (Total) ---
    public String getPlEmpTotalReturn() { return plEmpTotalReturn; }
    public void setPlEmpTotalReturn(String plEmpTotalReturn) { this.plEmpTotalReturn = plEmpTotalReturn; }

    public String getPlEmpTotalRetain() { return plEmpTotalRetain; }
    public void setPlEmpTotalRetain(String plEmpTotalRetain) { this.plEmpTotalRetain = plEmpTotalRetain; }

    // --- Permanent Workers (Male) ---
    public String getPlWorkMaleReturn() { return plWorkMaleReturn; }
    public void setPlWorkMaleReturn(String plWorkMaleReturn) { this.plWorkMaleReturn = plWorkMaleReturn; }

    public String getPlWorkMaleRetain() { return plWorkMaleRetain; }
    public void setPlWorkMaleRetain(String plWorkMaleRetain) { this.plWorkMaleRetain = plWorkMaleRetain; }

    // --- Permanent Workers (Female) ---
    public String getPlWorkFemaleReturn() { return plWorkFemaleReturn; }
    public void setPlWorkFemaleReturn(String plWorkFemaleReturn) { this.plWorkFemaleReturn = plWorkFemaleReturn; }

    public String getPlWorkFemaleRetain() { return plWorkFemaleRetain; }
    public void setPlWorkFemaleRetain(String plWorkFemaleRetain) { this.plWorkFemaleRetain = plWorkFemaleRetain; }

    // --- Permanent Workers (Total) ---
    public String getPlWorkTotalReturn() { return plWorkTotalReturn; }
    public void setPlWorkTotalReturn(String plWorkTotalReturn) { this.plWorkTotalReturn = plWorkTotalReturn; }

    public String getPlWorkTotalRetain() { return plWorkTotalRetain; }
    public void setPlWorkTotalRetain(String plWorkTotalRetain) { this.plWorkTotalRetain = plWorkTotalRetain; }

    // --- Note ---
    public String getParentalLeaveNote() { return parentalLeaveNote; }
    public void setParentalLeaveNote(String parentalLeaveNote) { this.parentalLeaveNote = parentalLeaveNote; }

    //princple 3 q6
    public String getGrievancePermWorkers() { return grievancePermWorkers; }
    public void setGrievancePermWorkers(String s) { this.grievancePermWorkers = s; }

    public String getGrievanceTempWorkers() { return grievanceTempWorkers; }
    public void setGrievanceTempWorkers(String s) { this.grievanceTempWorkers = s; }

    public String getGrievancePermEmployees() { return grievancePermEmployees; }
    public void setGrievancePermEmployees(String s) { this.grievancePermEmployees = s; }

    public String getGrievanceTempEmployees() { return grievanceTempEmployees; }
    public void setGrievanceTempEmployees(String s) { this.grievanceTempEmployees = s; }

    // GETTERS & SETTERS FOR SECTION C: P3 Q7 (UNION)
    // ===============================================

    public String getUnionCurrEmpTotalA() { return unionCurrEmpTotalA; }
    public void setUnionCurrEmpTotalA(String unionCurrEmpTotalA) { this.unionCurrEmpTotalA = unionCurrEmpTotalA; }

    public String getUnionCurrEmpUnionB() { return unionCurrEmpUnionB; }
    public void setUnionCurrEmpUnionB(String unionCurrEmpUnionB) { this.unionCurrEmpUnionB = unionCurrEmpUnionB; }

    public String getUnionCurrEmpPerc() { return unionCurrEmpPerc; }
    public void setUnionCurrEmpPerc(String unionCurrEmpPerc) { this.unionCurrEmpPerc = unionCurrEmpPerc; }

    public String getUnionCurrEmpMaleTotal() { return unionCurrEmpMaleTotal; }
    public void setUnionCurrEmpMaleTotal(String unionCurrEmpMaleTotal) { this.unionCurrEmpMaleTotal = unionCurrEmpMaleTotal; }

    public String getUnionCurrEmpMaleUnion() { return unionCurrEmpMaleUnion; }
    public void setUnionCurrEmpMaleUnion(String unionCurrEmpMaleUnion) { this.unionCurrEmpMaleUnion = unionCurrEmpMaleUnion; }

    public String getUnionCurrEmpMalePerc() { return unionCurrEmpMalePerc; }
    public void setUnionCurrEmpMalePerc(String unionCurrEmpMalePerc) { this.unionCurrEmpMalePerc = unionCurrEmpMalePerc; }

    public String getUnionCurrEmpFemaleTotal() { return unionCurrEmpFemaleTotal; }
    public void setUnionCurrEmpFemaleTotal(String unionCurrEmpFemaleTotal) { this.unionCurrEmpFemaleTotal = unionCurrEmpFemaleTotal; }

    public String getUnionCurrEmpFemaleUnion() { return unionCurrEmpFemaleUnion; }
    public void setUnionCurrEmpFemaleUnion(String unionCurrEmpFemaleUnion) { this.unionCurrEmpFemaleUnion = unionCurrEmpFemaleUnion; }

    public String getUnionCurrEmpFemalePerc() { return unionCurrEmpFemalePerc; }
    public void setUnionCurrEmpFemalePerc(String unionCurrEmpFemalePerc) { this.unionCurrEmpFemalePerc = unionCurrEmpFemalePerc; }

    public String getUnionCurrWorkTotalA() { return unionCurrWorkTotalA; }
    public void setUnionCurrWorkTotalA(String unionCurrWorkTotalA) { this.unionCurrWorkTotalA = unionCurrWorkTotalA; }

    public String getUnionCurrWorkUnionB() { return unionCurrWorkUnionB; }
    public void setUnionCurrWorkUnionB(String unionCurrWorkUnionB) { this.unionCurrWorkUnionB = unionCurrWorkUnionB; }

    public String getUnionCurrWorkPerc() { return unionCurrWorkPerc; }
    public void setUnionCurrWorkPerc(String unionCurrWorkPerc) { this.unionCurrWorkPerc = unionCurrWorkPerc; }

    public String getUnionCurrWorkMaleTotal() { return unionCurrWorkMaleTotal; }
    public void setUnionCurrWorkMaleTotal(String unionCurrWorkMaleTotal) { this.unionCurrWorkMaleTotal = unionCurrWorkMaleTotal; }

    public String getUnionCurrWorkMaleUnion() { return unionCurrWorkMaleUnion; }
    public void setUnionCurrWorkMaleUnion(String unionCurrWorkMaleUnion) { this.unionCurrWorkMaleUnion = unionCurrWorkMaleUnion; }

    public String getUnionCurrWorkMalePerc() { return unionCurrWorkMalePerc; }
    public void setUnionCurrWorkMalePerc(String unionCurrWorkMalePerc) { this.unionCurrWorkMalePerc = unionCurrWorkMalePerc; }

    public String getUnionCurrWorkFemaleTotal() { return unionCurrWorkFemaleTotal; }
    public void setUnionCurrWorkFemaleTotal(String unionCurrWorkFemaleTotal) { this.unionCurrWorkFemaleTotal = unionCurrWorkFemaleTotal; }

    public String getUnionCurrWorkFemaleUnion() { return unionCurrWorkFemaleUnion; }
    public void setUnionCurrWorkFemaleUnion(String unionCurrWorkFemaleUnion) { this.unionCurrWorkFemaleUnion = unionCurrWorkFemaleUnion; }

    public String getUnionCurrWorkFemalePerc() { return unionCurrWorkFemalePerc; }
    public void setUnionCurrWorkFemalePerc(String unionCurrWorkFemalePerc) { this.unionCurrWorkFemalePerc = unionCurrWorkFemalePerc; }

    public String getUnionPrevEmpTotalC() { return unionPrevEmpTotalC; }
    public void setUnionPrevEmpTotalC(String unionPrevEmpTotalC) { this.unionPrevEmpTotalC = unionPrevEmpTotalC; }

    public String getUnionPrevEmpUnionD() { return unionPrevEmpUnionD; }
    public void setUnionPrevEmpUnionD(String unionPrevEmpUnionD) { this.unionPrevEmpUnionD = unionPrevEmpUnionD; }

    public String getUnionPrevEmpPerc() { return unionPrevEmpPerc; }
    public void setUnionPrevEmpPerc(String unionPrevEmpPerc) { this.unionPrevEmpPerc = unionPrevEmpPerc; }

    public String getUnionPrevEmpMaleTotal() { return unionPrevEmpMaleTotal; }
    public void setUnionPrevEmpMaleTotal(String unionPrevEmpMaleTotal) { this.unionPrevEmpMaleTotal = unionPrevEmpMaleTotal; }

    public String getUnionPrevEmpMaleUnion() { return unionPrevEmpMaleUnion; }
    public void setUnionPrevEmpMaleUnion(String unionPrevEmpMaleUnion) { this.unionPrevEmpMaleUnion = unionPrevEmpMaleUnion; }

    public String getUnionPrevEmpMalePerc() { return unionPrevEmpMalePerc; }
    public void setUnionPrevEmpMalePerc(String unionPrevEmpMalePerc) { this.unionPrevEmpMalePerc = unionPrevEmpMalePerc; }

    public String getUnionPrevEmpFemaleTotal() { return unionPrevEmpFemaleTotal; }
    public void setUnionPrevEmpFemaleTotal(String unionPrevEmpFemaleTotal) { this.unionPrevEmpFemaleTotal = unionPrevEmpFemaleTotal; }

    public String getUnionPrevEmpFemaleUnion() { return unionPrevEmpFemaleUnion; }
    public void setUnionPrevEmpFemaleUnion(String unionPrevEmpFemaleUnion) { this.unionPrevEmpFemaleUnion = unionPrevEmpFemaleUnion; }

    public String getUnionPrevEmpFemalePerc() { return unionPrevEmpFemalePerc; }
    public void setUnionPrevEmpFemalePerc(String unionPrevEmpFemalePerc) { this.unionPrevEmpFemalePerc = unionPrevEmpFemalePerc; }

    public String getUnionPrevWorkTotalC() { return unionPrevWorkTotalC; }
    public void setUnionPrevWorkTotalC(String unionPrevWorkTotalC) { this.unionPrevWorkTotalC = unionPrevWorkTotalC; }

    public String getUnionPrevWorkUnionD() { return unionPrevWorkUnionD; }
    public void setUnionPrevWorkUnionD(String unionPrevWorkUnionD) { this.unionPrevWorkUnionD = unionPrevWorkUnionD; }

    public String getUnionPrevWorkPerc() { return unionPrevWorkPerc; }
    public void setUnionPrevWorkPerc(String unionPrevWorkPerc) { this.unionPrevWorkPerc = unionPrevWorkPerc; }

    public String getUnionPrevWorkMaleTotal() { return unionPrevWorkMaleTotal; }
    public void setUnionPrevWorkMaleTotal(String unionPrevWorkMaleTotal) { this.unionPrevWorkMaleTotal = unionPrevWorkMaleTotal; }

    public String getUnionPrevWorkMaleUnion() { return unionPrevWorkMaleUnion; }
    public void setUnionPrevWorkMaleUnion(String unionPrevWorkMaleUnion) { this.unionPrevWorkMaleUnion = unionPrevWorkMaleUnion; }

    public String getUnionPrevWorkMalePerc() { return unionPrevWorkMalePerc; }
    public void setUnionPrevWorkMalePerc(String unionPrevWorkMalePerc) { this.unionPrevWorkMalePerc = unionPrevWorkMalePerc; }

    public String getUnionPrevWorkFemaleTotal() { return unionPrevWorkFemaleTotal; }
    public void setUnionPrevWorkFemaleTotal(String unionPrevWorkFemaleTotal) { this.unionPrevWorkFemaleTotal = unionPrevWorkFemaleTotal; }

    public String getUnionPrevWorkFemaleUnion() { return unionPrevWorkFemaleUnion; }
    public void setUnionPrevWorkFemaleUnion(String unionPrevWorkFemaleUnion) { this.unionPrevWorkFemaleUnion = unionPrevWorkFemaleUnion; }

    public String getUnionPrevWorkFemalePerc() { return unionPrevWorkFemalePerc; }
    public void setUnionPrevWorkFemalePerc(String unionPrevWorkFemalePerc) { this.unionPrevWorkFemalePerc = unionPrevWorkFemalePerc; }

    public String getUnionMembershipNote() { return unionMembershipNote; }
    public void setUnionMembershipNote(String unionMembershipNote) { this.unionMembershipNote = unionMembershipNote; }

    //princple 3 q8

    public String getTrainingDetailsNote() { return trainingDetailsNote; }
    public void setTrainingDetailsNote(String trainingDetailsNote) { this.trainingDetailsNote = trainingDetailsNote; }

    // Employees Current
    public String getTrainEmpMaleTotal() { return trainEmpMaleTotal; } public void setTrainEmpMaleTotal(String s) { this.trainEmpMaleTotal = s; }
    public String getTrainEmpMaleHealthNo() { return trainEmpMaleHealthNo; } public void setTrainEmpMaleHealthNo(String s) { this.trainEmpMaleHealthNo = s; }
    public String getTrainEmpMaleHealthPerc() { return trainEmpMaleHealthPerc; } public void setTrainEmpMaleHealthPerc(String s) { this.trainEmpMaleHealthPerc = s; }
    public String getTrainEmpMaleSkillNo() { return trainEmpMaleSkillNo; } public void setTrainEmpMaleSkillNo(String s) { this.trainEmpMaleSkillNo = s; }
    public String getTrainEmpMaleSkillPerc() { return trainEmpMaleSkillPerc; } public void setTrainEmpMaleSkillPerc(String s) { this.trainEmpMaleSkillPerc = s; }

    public String getTrainEmpFemaleTotal() { return trainEmpFemaleTotal; } public void setTrainEmpFemaleTotal(String s) { this.trainEmpFemaleTotal = s; }
    public String getTrainEmpFemaleHealthNo() { return trainEmpFemaleHealthNo; } public void setTrainEmpFemaleHealthNo(String s) { this.trainEmpFemaleHealthNo = s; }
    public String getTrainEmpFemaleHealthPerc() { return trainEmpFemaleHealthPerc; } public void setTrainEmpFemaleHealthPerc(String s) { this.trainEmpFemaleHealthPerc = s; }
    public String getTrainEmpFemaleSkillNo() { return trainEmpFemaleSkillNo; } public void setTrainEmpFemaleSkillNo(String s) { this.trainEmpFemaleSkillNo = s; }
    public String getTrainEmpFemaleSkillPerc() { return trainEmpFemaleSkillPerc; } public void setTrainEmpFemaleSkillPerc(String s) { this.trainEmpFemaleSkillPerc = s; }

    public String getTrainEmpGenTotal() { return trainEmpGenTotal; } public void setTrainEmpGenTotal(String s) { this.trainEmpGenTotal = s; }
    public String getTrainEmpGenHealthNo() { return trainEmpGenHealthNo; } public void setTrainEmpGenHealthNo(String s) { this.trainEmpGenHealthNo = s; }
    public String getTrainEmpGenHealthPerc() { return trainEmpGenHealthPerc; } public void setTrainEmpGenHealthPerc(String s) { this.trainEmpGenHealthPerc = s; }
    public String getTrainEmpGenSkillNo() { return trainEmpGenSkillNo; } public void setTrainEmpGenSkillNo(String s) { this.trainEmpGenSkillNo = s; }
    public String getTrainEmpGenSkillPerc() { return trainEmpGenSkillPerc; } public void setTrainEmpGenSkillPerc(String s) { this.trainEmpGenSkillPerc = s; }

    // Employees Previous
    public String getTrainEmpMaleTotalPrev() { return trainEmpMaleTotalPrev; } public void setTrainEmpMaleTotalPrev(String s) { this.trainEmpMaleTotalPrev = s; }
    public String getTrainEmpMaleHealthNoPrev() { return trainEmpMaleHealthNoPrev; } public void setTrainEmpMaleHealthNoPrev(String s) { this.trainEmpMaleHealthNoPrev = s; }
    public String getTrainEmpMaleHealthPercPrev() { return trainEmpMaleHealthPercPrev; } public void setTrainEmpMaleHealthPercPrev(String s) { this.trainEmpMaleHealthPercPrev = s; }
    public String getTrainEmpMaleSkillNoPrev() { return trainEmpMaleSkillNoPrev; } public void setTrainEmpMaleSkillNoPrev(String s) { this.trainEmpMaleSkillNoPrev = s; }
    public String getTrainEmpMaleSkillPercPrev() { return trainEmpMaleSkillPercPrev; } public void setTrainEmpMaleSkillPercPrev(String s) { this.trainEmpMaleSkillPercPrev = s; }

    public String getTrainEmpFemaleTotalPrev() { return trainEmpFemaleTotalPrev; } public void setTrainEmpFemaleTotalPrev(String s) { this.trainEmpFemaleTotalPrev = s; }
    public String getTrainEmpFemaleHealthNoPrev() { return trainEmpFemaleHealthNoPrev; } public void setTrainEmpFemaleHealthNoPrev(String s) { this.trainEmpFemaleHealthNoPrev = s; }
    public String getTrainEmpFemaleHealthPercPrev() { return trainEmpFemaleHealthPercPrev; } public void setTrainEmpFemaleHealthPercPrev(String s) { this.trainEmpFemaleHealthPercPrev = s; }
    public String getTrainEmpFemaleSkillNoPrev() { return trainEmpFemaleSkillNoPrev; } public void setTrainEmpFemaleSkillNoPrev(String s) { this.trainEmpFemaleSkillNoPrev = s; }
    public String getTrainEmpFemaleSkillPercPrev() { return trainEmpFemaleSkillPercPrev; } public void setTrainEmpFemaleSkillPercPrev(String s) { this.trainEmpFemaleSkillPercPrev = s; }

    public String getTrainEmpGenTotalPrev() { return trainEmpGenTotalPrev; } public void setTrainEmpGenTotalPrev(String s) { this.trainEmpGenTotalPrev = s; }
    public String getTrainEmpGenHealthNoPrev() { return trainEmpGenHealthNoPrev; } public void setTrainEmpGenHealthNoPrev(String s) { this.trainEmpGenHealthNoPrev = s; }
    public String getTrainEmpGenHealthPercPrev() { return trainEmpGenHealthPercPrev; } public void setTrainEmpGenHealthPercPrev(String s) { this.trainEmpGenHealthPercPrev = s; }
    public String getTrainEmpGenSkillNoPrev() { return trainEmpGenSkillNoPrev; } public void setTrainEmpGenSkillNoPrev(String s) { this.trainEmpGenSkillNoPrev = s; }
    public String getTrainEmpGenSkillPercPrev() { return trainEmpGenSkillPercPrev; } public void setTrainEmpGenSkillPercPrev(String s) { this.trainEmpGenSkillPercPrev = s; }

    // Workers Current
    public String getTrainWorkMaleTotal() { return trainWorkMaleTotal; } public void setTrainWorkMaleTotal(String s) { this.trainWorkMaleTotal = s; }
    public String getTrainWorkMaleHealthNo() { return trainWorkMaleHealthNo; } public void setTrainWorkMaleHealthNo(String s) { this.trainWorkMaleHealthNo = s; }
    public String getTrainWorkMaleHealthPerc() { return trainWorkMaleHealthPerc; } public void setTrainWorkMaleHealthPerc(String s) { this.trainWorkMaleHealthPerc = s; }
    public String getTrainWorkMaleSkillNo() { return trainWorkMaleSkillNo; } public void setTrainWorkMaleSkillNo(String s) { this.trainWorkMaleSkillNo = s; }
    public String getTrainWorkMaleSkillPerc() { return trainWorkMaleSkillPerc; } public void setTrainWorkMaleSkillPerc(String s) { this.trainWorkMaleSkillPerc = s; }

    public String getTrainWorkFemaleTotal() { return trainWorkFemaleTotal; } public void setTrainWorkFemaleTotal(String s) { this.trainWorkFemaleTotal = s; }
    public String getTrainWorkFemaleHealthNo() { return trainWorkFemaleHealthNo; } public void setTrainWorkFemaleHealthNo(String s) { this.trainWorkFemaleHealthNo = s; }
    public String getTrainWorkFemaleHealthPerc() { return trainWorkFemaleHealthPerc; } public void setTrainWorkFemaleHealthPerc(String s) { this.trainWorkFemaleHealthPerc = s; }
    public String getTrainWorkFemaleSkillNo() { return trainWorkFemaleSkillNo; } public void setTrainWorkFemaleSkillNo(String s) { this.trainWorkFemaleSkillNo = s; }
    public String getTrainWorkFemaleSkillPerc() { return trainWorkFemaleSkillPerc; } public void setTrainWorkFemaleSkillPerc(String s) { this.trainWorkFemaleSkillPerc = s; }

    public String getTrainWorkGenTotal() { return trainWorkGenTotal; } public void setTrainWorkGenTotal(String s) { this.trainWorkGenTotal = s; }
    public String getTrainWorkGenHealthNo() { return trainWorkGenHealthNo; } public void setTrainWorkGenHealthNo(String s) { this.trainWorkGenHealthNo = s; }
    public String getTrainWorkGenHealthPerc() { return trainWorkGenHealthPerc; } public void setTrainWorkGenHealthPerc(String s) { this.trainWorkGenHealthPerc = s; }
    public String getTrainWorkGenSkillNo() { return trainWorkGenSkillNo; } public void setTrainWorkGenSkillNo(String s) { this.trainWorkGenSkillNo = s; }
    public String getTrainWorkGenSkillPerc() { return trainWorkGenSkillPerc; } public void setTrainWorkGenSkillPerc(String s) { this.trainWorkGenSkillPerc = s; }

    // Workers Previous
    public String getTrainWorkMaleTotalPrev() { return trainWorkMaleTotalPrev; } public void setTrainWorkMaleTotalPrev(String s) { this.trainWorkMaleTotalPrev = s; }
    public String getTrainWorkMaleHealthNoPrev() { return trainWorkMaleHealthNoPrev; } public void setTrainWorkMaleHealthNoPrev(String s) { this.trainWorkMaleHealthNoPrev = s; }
    public String getTrainWorkMaleHealthPercPrev() { return trainWorkMaleHealthPercPrev; } public void setTrainWorkMaleHealthPercPrev(String s) { this.trainWorkMaleHealthPercPrev = s; }
    public String getTrainWorkMaleSkillNoPrev() { return trainWorkMaleSkillNoPrev; } public void setTrainWorkMaleSkillNoPrev(String s) { this.trainWorkMaleSkillNoPrev = s; }
    public String getTrainWorkMaleSkillPercPrev() { return trainWorkMaleSkillPercPrev; } public void setTrainWorkMaleSkillPercPrev(String s) { this.trainWorkMaleSkillPercPrev = s; }

    public String getTrainWorkFemaleTotalPrev() { return trainWorkFemaleTotalPrev; } public void setTrainWorkFemaleTotalPrev(String s) { this.trainWorkFemaleTotalPrev = s; }
    public String getTrainWorkFemaleHealthNoPrev() { return trainWorkFemaleHealthNoPrev; } public void setTrainWorkFemaleHealthNoPrev(String s) { this.trainWorkFemaleHealthNoPrev = s; }
    public String getTrainWorkFemaleHealthPercPrev() { return trainWorkFemaleHealthPercPrev; } public void setTrainWorkFemaleHealthPercPrev(String s) { this.trainWorkFemaleHealthPercPrev = s; }
    public String getTrainWorkFemaleSkillNoPrev() { return trainWorkFemaleSkillNoPrev; } public void setTrainWorkFemaleSkillNoPrev(String s) { this.trainWorkFemaleSkillNoPrev = s; }
    public String getTrainWorkFemaleSkillPercPrev() { return trainWorkFemaleSkillPercPrev; } public void setTrainWorkFemaleSkillPercPrev(String s) { this.trainWorkFemaleSkillPercPrev = s; }

    public String getTrainWorkGenTotalPrev() { return trainWorkGenTotalPrev; } public void setTrainWorkGenTotalPrev(String s) { this.trainWorkGenTotalPrev = s; }
    public String getTrainWorkGenHealthNoPrev() { return trainWorkGenHealthNoPrev; } public void setTrainWorkGenHealthNoPrev(String s) { this.trainWorkGenHealthNoPrev = s; }
    public String getTrainWorkGenHealthPercPrev() { return trainWorkGenHealthPercPrev; } public void setTrainWorkGenHealthPercPrev(String s) { this.trainWorkGenHealthPercPrev = s; }
    public String getTrainWorkGenSkillNoPrev() { return trainWorkGenSkillNoPrev; } public void setTrainWorkGenSkillNoPrev(String s) { this.trainWorkGenSkillNoPrev = s; }
    public String getTrainWorkGenSkillPercPrev() { return trainWorkGenSkillPercPrev; } public void setTrainWorkGenSkillPercPrev(String s) { this.trainWorkGenSkillPercPrev = s; }

    // Employee Current
    public String getTrainEmpOtherTotal() { return trainEmpOtherTotal; } public void setTrainEmpOtherTotal(String s) { this.trainEmpOtherTotal = s; }
    public String getTrainEmpOtherHealthNo() { return trainEmpOtherHealthNo; } public void setTrainEmpOtherHealthNo(String s) { this.trainEmpOtherHealthNo = s; }
    public String getTrainEmpOtherHealthPerc() { return trainEmpOtherHealthPerc; } public void setTrainEmpOtherHealthPerc(String s) { this.trainEmpOtherHealthPerc = s; }
    public String getTrainEmpOtherSkillNo() { return trainEmpOtherSkillNo; } public void setTrainEmpOtherSkillNo(String s) { this.trainEmpOtherSkillNo = s; }
    public String getTrainEmpOtherSkillPerc() { return trainEmpOtherSkillPerc; } public void setTrainEmpOtherSkillPerc(String s) { this.trainEmpOtherSkillPerc = s; }

    // Employee Previous
    public String getTrainEmpOtherTotalPrev() { return trainEmpOtherTotalPrev; } public void setTrainEmpOtherTotalPrev(String s) { this.trainEmpOtherTotalPrev = s; }
    public String getTrainEmpOtherHealthNoPrev() { return trainEmpOtherHealthNoPrev; } public void setTrainEmpOtherHealthNoPrev(String s) { this.trainEmpOtherHealthNoPrev = s; }
    public String getTrainEmpOtherHealthPercPrev() { return trainEmpOtherHealthPercPrev; } public void setTrainEmpOtherHealthPercPrev(String s) { this.trainEmpOtherHealthPercPrev = s; }
    public String getTrainEmpOtherSkillNoPrev() { return trainEmpOtherSkillNoPrev; } public void setTrainEmpOtherSkillNoPrev(String s) { this.trainEmpOtherSkillNoPrev = s; }
    public String getTrainEmpOtherSkillPercPrev() { return trainEmpOtherSkillPercPrev; } public void setTrainEmpOtherSkillPercPrev(String s) { this.trainEmpOtherSkillPercPrev = s; }

    // Worker Current
    public String getTrainWorkOtherTotal() { return trainWorkOtherTotal; } public void setTrainWorkOtherTotal(String s) { this.trainWorkOtherTotal = s; }
    public String getTrainWorkOtherHealthNo() { return trainWorkOtherHealthNo; } public void setTrainWorkOtherHealthNo(String s) { this.trainWorkOtherHealthNo = s; }
    public String getTrainWorkOtherHealthPerc() { return trainWorkOtherHealthPerc; } public void setTrainWorkOtherHealthPerc(String s) { this.trainWorkOtherHealthPerc = s; }
    public String getTrainWorkOtherSkillNo() { return trainWorkOtherSkillNo; } public void setTrainWorkOtherSkillNo(String s) { this.trainWorkOtherSkillNo = s; }
    public String getTrainWorkOtherSkillPerc() { return trainWorkOtherSkillPerc; } public void setTrainWorkOtherSkillPerc(String s) { this.trainWorkOtherSkillPerc = s; }

    // Worker Previous
    public String getTrainWorkOtherTotalPrev() { return trainWorkOtherTotalPrev; } public void setTrainWorkOtherTotalPrev(String s) { this.trainWorkOtherTotalPrev = s; }
    public String getTrainWorkOtherHealthNoPrev() { return trainWorkOtherHealthNoPrev; } public void setTrainWorkOtherHealthNoPrev(String s) { this.trainWorkOtherHealthNoPrev = s; }
    public String getTrainWorkOtherHealthPercPrev() { return trainWorkOtherHealthPercPrev; } public void setTrainWorkOtherHealthPercPrev(String s) { this.trainWorkOtherHealthPercPrev = s; }
    public String getTrainWorkOtherSkillNoPrev() { return trainWorkOtherSkillNoPrev; } public void setTrainWorkOtherSkillNoPrev(String s) { this.trainWorkOtherSkillNoPrev = s; }
    public String getTrainWorkOtherSkillPercPrev() { return trainWorkOtherSkillPercPrev; } public void setTrainWorkOtherSkillPercPrev(String s) { this.trainWorkOtherSkillPercPrev = s; }

    // GETTERS & SETTERS FOR SECTION C: Q9 (PERFORMANCE REVIEWS)
    // =========================================================

    public String getReviewDetailsNote() { return reviewDetailsNote; }
    public void setReviewDetailsNote(String reviewDetailsNote) { this.reviewDetailsNote = reviewDetailsNote; }

    // --- EMPLOYEES (CURRENT) ---
    public String getRevEmpMaleTotal() { return revEmpMaleTotal; } public void setRevEmpMaleTotal(String s) { this.revEmpMaleTotal = s; }
    public String getRevEmpMaleCovered() { return revEmpMaleCovered; } public void setRevEmpMaleCovered(String s) { this.revEmpMaleCovered = s; }
    public String getRevEmpMalePerc() { return revEmpMalePerc; } public void setRevEmpMalePerc(String s) { this.revEmpMalePerc = s; }

    public String getRevEmpFemTotal() { return revEmpFemTotal; } public void setRevEmpFemTotal(String s) { this.revEmpFemTotal = s; }
    public String getRevEmpFemCovered() { return revEmpFemCovered; } public void setRevEmpFemCovered(String s) { this.revEmpFemCovered = s; }
    public String getRevEmpFemPerc() { return revEmpFemPerc; } public void setRevEmpFemPerc(String s) { this.revEmpFemPerc = s; }

    public String getRevEmpOthTotal() { return revEmpOthTotal; } public void setRevEmpOthTotal(String s) { this.revEmpOthTotal = s; }
    public String getRevEmpOthCovered() { return revEmpOthCovered; } public void setRevEmpOthCovered(String s) { this.revEmpOthCovered = s; }
    public String getRevEmpOthPerc() { return revEmpOthPerc; } public void setRevEmpOthPerc(String s) { this.revEmpOthPerc = s; }

    public String getRevEmpGenTotal() { return revEmpGenTotal; } public void setRevEmpGenTotal(String s) { this.revEmpGenTotal = s; }
    public String getRevEmpGenCovered() { return revEmpGenCovered; } public void setRevEmpGenCovered(String s) { this.revEmpGenCovered = s; }
    public String getRevEmpGenPerc() { return revEmpGenPerc; } public void setRevEmpGenPerc(String s) { this.revEmpGenPerc = s; }

    // --- EMPLOYEES (PREVIOUS) ---
    public String getRevEmpMaleTotalPrev() { return revEmpMaleTotalPrev; } public void setRevEmpMaleTotalPrev(String s) { this.revEmpMaleTotalPrev = s; }
    public String getRevEmpMaleCoveredPrev() { return revEmpMaleCoveredPrev; } public void setRevEmpMaleCoveredPrev(String s) { this.revEmpMaleCoveredPrev = s; }
    public String getRevEmpMalePercPrev() { return revEmpMalePercPrev; } public void setRevEmpMalePercPrev(String s) { this.revEmpMalePercPrev = s; }

    public String getRevEmpFemTotalPrev() { return revEmpFemTotalPrev; } public void setRevEmpFemTotalPrev(String s) { this.revEmpFemTotalPrev = s; }
    public String getRevEmpFemCoveredPrev() { return revEmpFemCoveredPrev; } public void setRevEmpFemCoveredPrev(String s) { this.revEmpFemCoveredPrev = s; }
    public String getRevEmpFemPercPrev() { return revEmpFemPercPrev; } public void setRevEmpFemPercPrev(String s) { this.revEmpFemPercPrev = s; }

    public String getRevEmpOthTotalPrev() { return revEmpOthTotalPrev; } public void setRevEmpOthTotalPrev(String s) { this.revEmpOthTotalPrev = s; }
    public String getRevEmpOthCoveredPrev() { return revEmpOthCoveredPrev; } public void setRevEmpOthCoveredPrev(String s) { this.revEmpOthCoveredPrev = s; }
    public String getRevEmpOthPercPrev() { return revEmpOthPercPrev; } public void setRevEmpOthPercPrev(String s) { this.revEmpOthPercPrev = s; }

    public String getRevEmpGenTotalPrev() { return revEmpGenTotalPrev; } public void setRevEmpGenTotalPrev(String s) { this.revEmpGenTotalPrev = s; }
    public String getRevEmpGenCoveredPrev() { return revEmpGenCoveredPrev; } public void setRevEmpGenCoveredPrev(String s) { this.revEmpGenCoveredPrev = s; }
    public String getRevEmpGenPercPrev() { return revEmpGenPercPrev; } public void setRevEmpGenPercPrev(String s) { this.revEmpGenPercPrev = s; }

    // --- WORKERS (CURRENT) ---
    public String getRevWorkMaleTotal() { return revWorkMaleTotal; } public void setRevWorkMaleTotal(String s) { this.revWorkMaleTotal = s; }
    public String getRevWorkMaleCovered() { return revWorkMaleCovered; } public void setRevWorkMaleCovered(String s) { this.revWorkMaleCovered = s; }
    public String getRevWorkMalePerc() { return revWorkMalePerc; } public void setRevWorkMalePerc(String s) { this.revWorkMalePerc = s; }

    public String getRevWorkFemTotal() { return revWorkFemTotal; } public void setRevWorkFemTotal(String s) { this.revWorkFemTotal = s; }
    public String getRevWorkFemCovered() { return revWorkFemCovered; } public void setRevWorkFemCovered(String s) { this.revWorkFemCovered = s; }
    public String getRevWorkFemPerc() { return revWorkFemPerc; } public void setRevWorkFemPerc(String s) { this.revWorkFemPerc = s; }

    public String getRevWorkOthTotal() { return revWorkOthTotal; } public void setRevWorkOthTotal(String s) { this.revWorkOthTotal = s; }
    public String getRevWorkOthCovered() { return revWorkOthCovered; } public void setRevWorkOthCovered(String s) { this.revWorkOthCovered = s; }
    public String getRevWorkOthPerc() { return revWorkOthPerc; } public void setRevWorkOthPerc(String s) { this.revWorkOthPerc = s; }

    public String getRevWorkGenTotal() { return revWorkGenTotal; } public void setRevWorkGenTotal(String s) { this.revWorkGenTotal = s; }
    public String getRevWorkGenCovered() { return revWorkGenCovered; } public void setRevWorkGenCovered(String s) { this.revWorkGenCovered = s; }
    public String getRevWorkGenPerc() { return revWorkGenPerc; } public void setRevWorkGenPerc(String s) { this.revWorkGenPerc = s; }

    // --- WORKERS (PREVIOUS) ---
    public String getRevWorkMaleTotalPrev() { return revWorkMaleTotalPrev; } public void setRevWorkMaleTotalPrev(String s) { this.revWorkMaleTotalPrev = s; }
    public String getRevWorkMaleCoveredPrev() { return revWorkMaleCoveredPrev; } public void setRevWorkMaleCoveredPrev(String s) { this.revWorkMaleCoveredPrev = s; }
    public String getRevWorkMalePercPrev() { return revWorkMalePercPrev; } public void setRevWorkMalePercPrev(String s) { this.revWorkMalePercPrev = s; }

    public String getRevWorkFemTotalPrev() { return revWorkFemTotalPrev; } public void setRevWorkFemTotalPrev(String s) { this.revWorkFemTotalPrev = s; }
    public String getRevWorkFemCoveredPrev() { return revWorkFemCoveredPrev; } public void setRevWorkFemCoveredPrev(String s) { this.revWorkFemCoveredPrev = s; }
    public String getRevWorkFemPercPrev() { return revWorkFemPercPrev; } public void setRevWorkFemPercPrev(String s) { this.revWorkFemPercPrev = s; }

    public String getRevWorkOthTotalPrev() { return revWorkOthTotalPrev; } public void setRevWorkOthTotalPrev(String s) { this.revWorkOthTotalPrev = s; }
    public String getRevWorkOthCoveredPrev() { return revWorkOthCoveredPrev; } public void setRevWorkOthCoveredPrev(String s) { this.revWorkOthCoveredPrev = s; }
    public String getRevWorkOthPercPrev() { return revWorkOthPercPrev; } public void setRevWorkOthPercPrev(String s) { this.revWorkOthPercPrev = s; }

    public String getRevWorkGenTotalPrev() { return revWorkGenTotalPrev; } public void setRevWorkGenTotalPrev(String s) { this.revWorkGenTotalPrev = s; }
    public String getRevWorkGenCoveredPrev() { return revWorkGenCoveredPrev; } public void setRevWorkGenCoveredPrev(String s) { this.revWorkGenCoveredPrev = s; }
    public String getRevWorkGenPercPrev() { return revWorkGenPercPrev; } public void setRevWorkGenPercPrev(String s) { this.revWorkGenPercPrev = s; }

    public String getHealthSafetySystem() { return healthSafetySystem; }
    public void setHealthSafetySystem(String s) { this.healthSafetySystem = s; }

    public String getHazardIdentification() { return hazardIdentification; }
    public void setHazardIdentification(String s) { this.hazardIdentification = s; }

    public String getHazardReporting() { return hazardReporting; }
    public void setHazardReporting(String s) { this.hazardReporting = s; }

    public String getMedicalAccess() { return medicalAccess; }
    public void setMedicalAccess(String s) { this.medicalAccess = s; }


    //principle 3 q11
    public String getSafetyLtifrEmpCurr() { return safetyLtifrEmpCurr; } public void setSafetyLtifrEmpCurr(String s) { this.safetyLtifrEmpCurr = s; }
    public String getSafetyLtifrEmpPrev() { return safetyLtifrEmpPrev; } public void setSafetyLtifrEmpPrev(String s) { this.safetyLtifrEmpPrev = s; }
    public String getSafetyLtifrWorkCurr() { return safetyLtifrWorkCurr; } public void setSafetyLtifrWorkCurr(String s) { this.safetyLtifrWorkCurr = s; }
    public String getSafetyLtifrWorkPrev() { return safetyLtifrWorkPrev; } public void setSafetyLtifrWorkPrev(String s) { this.safetyLtifrWorkPrev = s; }

    public String getSafetyTotalInjEmpCurr() { return safetyTotalInjEmpCurr; } public void setSafetyTotalInjEmpCurr(String s) { this.safetyTotalInjEmpCurr = s; }
    public String getSafetyTotalInjEmpPrev() { return safetyTotalInjEmpPrev; } public void setSafetyTotalInjEmpPrev(String s) { this.safetyTotalInjEmpPrev = s; }
    public String getSafetyTotalInjWorkCurr() { return safetyTotalInjWorkCurr; } public void setSafetyTotalInjWorkCurr(String s) { this.safetyTotalInjWorkCurr = s; }
    public String getSafetyTotalInjWorkPrev() { return safetyTotalInjWorkPrev; } public void setSafetyTotalInjWorkPrev(String s) { this.safetyTotalInjWorkPrev = s; }

    public String getSafetyFatalEmpCurr() { return safetyFatalEmpCurr; } public void setSafetyFatalEmpCurr(String s) { this.safetyFatalEmpCurr = s; }
    public String getSafetyFatalEmpPrev() { return safetyFatalEmpPrev; } public void setSafetyFatalEmpPrev(String s) { this.safetyFatalEmpPrev = s; }
    public String getSafetyFatalWorkCurr() { return safetyFatalWorkCurr; } public void setSafetyFatalWorkCurr(String s) { this.safetyFatalWorkCurr = s; }
    public String getSafetyFatalWorkPrev() { return safetyFatalWorkPrev; } public void setSafetyFatalWorkPrev(String s) { this.safetyFatalWorkPrev = s; }

    public String getSafetyHighConEmpCurr() { return safetyHighConEmpCurr; } public void setSafetyHighConEmpCurr(String s) { this.safetyHighConEmpCurr = s; }
    public String getSafetyHighConEmpPrev() { return safetyHighConEmpPrev; } public void setSafetyHighConEmpPrev(String s) { this.safetyHighConEmpPrev = s; }
    public String getSafetyHighConWorkCurr() { return safetyHighConWorkCurr; } public void setSafetyHighConWorkCurr(String s) { this.safetyHighConWorkCurr = s; }
    public String getSafetyHighConWorkPrev() { return safetyHighConWorkPrev; } public void setSafetyHighConWorkPrev(String s) { this.safetyHighConWorkPrev = s; }

    public String getSafetyPermDisEmpCurr() { return safetyPermDisEmpCurr; } public void setSafetyPermDisEmpCurr(String s) { this.safetyPermDisEmpCurr = s; }
    public String getSafetyPermDisEmpPrev() { return safetyPermDisEmpPrev; } public void setSafetyPermDisEmpPrev(String s) { this.safetyPermDisEmpPrev = s; }
    public String getSafetyPermDisWorkCurr() { return safetyPermDisWorkCurr; } public void setSafetyPermDisWorkCurr(String s) { this.safetyPermDisWorkCurr = s; }
    public String getSafetyPermDisWorkPrev() { return safetyPermDisWorkPrev; } public void setSafetyPermDisWorkPrev(String s) { this.safetyPermDisWorkPrev = s; }

    public String getSafetyIncidentsNote() { return safetyIncidentsNote; } public void setSafetyIncidentsNote(String s) { this.safetyIncidentsNote = s; }

    //princple 3 q12
    public List<SafetyMeasure> getSafetyMeasures() { return safetyMeasures; }
    public void setSafetyMeasures(List<SafetyMeasure> list) { this.safetyMeasures = list; }

    //principle 3 q13
    public String getWcFiledCurr() { return wcFiledCurr; } public void setWcFiledCurr(String s) { this.wcFiledCurr = s; }
    public String getWcPendingCurr() { return wcPendingCurr; } public void setWcPendingCurr(String s) { this.wcPendingCurr = s; }
    public String getWcRemarksCurr() { return wcRemarksCurr; } public void setWcRemarksCurr(String s) { this.wcRemarksCurr = s; }

    public String getWcFiledPrev() { return wcFiledPrev; } public void setWcFiledPrev(String s) { this.wcFiledPrev = s; }
    public String getWcPendingPrev() { return wcPendingPrev; } public void setWcPendingPrev(String s) { this.wcPendingPrev = s; }
    public String getWcRemarksPrev() { return wcRemarksPrev; } public void setWcRemarksPrev(String s) { this.wcRemarksPrev = s; }

    public String getHsFiledCurr() { return hsFiledCurr; } public void setHsFiledCurr(String s) { this.hsFiledCurr = s; }
    public String getHsPendingCurr() { return hsPendingCurr; } public void setHsPendingCurr(String s) { this.hsPendingCurr = s; }
    public String getHsRemarksCurr() { return hsRemarksCurr; } public void setHsRemarksCurr(String s) { this.hsRemarksCurr = s; }

    public String getHsFiledPrev() { return hsFiledPrev; } public void setHsFiledPrev(String s) { this.hsFiledPrev = s; }
    public String getHsPendingPrev() { return hsPendingPrev; } public void setHsPendingPrev(String s) { this.hsPendingPrev = s; }
    public String getHsRemarksPrev() { return hsRemarksPrev; } public void setHsRemarksPrev(String s) { this.hsRemarksPrev = s; }

    public String getWorkerComplaintsNote() { return workerComplaintsNote; } public void setWorkerComplaintsNote(String s) { this.workerComplaintsNote = s; }

    // --- GETTERS & SETTERS --- q14 p3
    public String getAssessmentHealthPerc() { return assessmentHealthPerc; }
    public void setAssessmentHealthPerc(String s) { this.assessmentHealthPerc = s; }

    public String getAssessmentWorkingPerc() { return assessmentWorkingPerc; }
    public void setAssessmentWorkingPerc(String s) { this.assessmentWorkingPerc = s; }

    public String getAssessmentNote() { return assessmentNote; }
    public void setAssessmentNote(String s) { this.assessmentNote = s; }

    // --- GETTERS AND SETTERS --- q15 p3
    public String getSafetyCorrectiveActions() { return safetyCorrectiveActions; }
    public void setSafetyCorrectiveActions(String s) { this.safetyCorrectiveActions = s; }

    //princple 3 leadership 1
    public String getLifeInsuranceEmployees() { return lifeInsuranceEmployees; }
    public void setLifeInsuranceEmployees(String s) { this.lifeInsuranceEmployees = s; }

    public String getLifeInsuranceWorkers() { return lifeInsuranceWorkers; }
    public void setLifeInsuranceWorkers(String s) { this.lifeInsuranceWorkers = s; }

    public String getLifeInsuranceDetails() { return lifeInsuranceDetails; }
    public void setLifeInsuranceDetails(String s) { this.lifeInsuranceDetails = s; }

    //princple 3 leadership 2
    public String getStatutoryDuesMeasures() { return statutoryDuesMeasures; }
    public void setStatutoryDuesMeasures(String s) { this.statutoryDuesMeasures = s; }

    // GETTERS & SETTERS FOR SECTION C: LEADERSHIP 3 (REHABILITATION)
    // =========================================================

    // --- Total Affected (Employees) ---
    public String getRehabEmpAffCurr() {
        return rehabEmpAffCurr;
    }

    public void setRehabEmpAffCurr(String rehabEmpAffCurr) {
        this.rehabEmpAffCurr = rehabEmpAffCurr;
    }

    public String getRehabEmpAffPrev() {
        return rehabEmpAffPrev;
    }

    public void setRehabEmpAffPrev(String rehabEmpAffPrev) {
        this.rehabEmpAffPrev = rehabEmpAffPrev;
    }

    // --- Total Affected (Workers) ---
    public String getRehabWorkAffCurr() {
        return rehabWorkAffCurr;
    }

    public void setRehabWorkAffCurr(String rehabWorkAffCurr) {
        this.rehabWorkAffCurr = rehabWorkAffCurr;
    }

    public String getRehabWorkAffPrev() {
        return rehabWorkAffPrev;
    }

    public void setRehabWorkAffPrev(String rehabWorkAffPrev) {
        this.rehabWorkAffPrev = rehabWorkAffPrev;
    }

    // --- Rehabilitated/Placed (Employees) ---
    public String getRehabEmpPlacedCurr() {
        return rehabEmpPlacedCurr;
    }

    public void setRehabEmpPlacedCurr(String rehabEmpPlacedCurr) {
        this.rehabEmpPlacedCurr = rehabEmpPlacedCurr;
    }

    public String getRehabEmpPlacedPrev() {
        return rehabEmpPlacedPrev;
    }

    public void setRehabEmpPlacedPrev(String rehabEmpPlacedPrev) {
        this.rehabEmpPlacedPrev = rehabEmpPlacedPrev;
    }

    // --- Rehabilitated/Placed (Workers) ---
    public String getRehabWorkPlacedCurr() {
        return rehabWorkPlacedCurr;
    }

    public void setRehabWorkPlacedCurr(String rehabWorkPlacedCurr) {
        this.rehabWorkPlacedCurr = rehabWorkPlacedCurr;
    }

    public String getRehabWorkPlacedPrev() {
        return rehabWorkPlacedPrev;
    }

    public void setRehabWorkPlacedPrev(String rehabWorkPlacedPrev) {
        this.rehabWorkPlacedPrev = rehabWorkPlacedPrev;
    }

    // --- Note ---
    public String getRehabilitationNote() {
        return rehabilitationNote;
    }

    public void setRehabilitationNote(String rehabilitationNote) {
        this.rehabilitationNote = rehabilitationNote;
    }

    //princple 3 leadership 4
    public String getTransitionAssistanceDetails() { return transitionAssistanceDetails; }
    public void setTransitionAssistanceDetails(String s) { this.transitionAssistanceDetails = s; }

    //princple 3 leadership 5
    public String getValueChainAssessmentNote() { return valueChainAssessmentNote; }
    public void setValueChainAssessmentNote(String s) { this.valueChainAssessmentNote = s; }

    public String getVcHealthSafetyPerc() { return vcHealthSafetyPerc; }
    public void setVcHealthSafetyPerc(String s) { this.vcHealthSafetyPerc = s; }

    public String getVcWorkingCondPerc() { return vcWorkingCondPerc; }
    public void setVcWorkingCondPerc(String s) { this.vcWorkingCondPerc = s; }

    //princple 3 leadership 6
    public String getVcCorrectiveActionIntro() { return vcCorrectiveActionIntro; }
    public void setVcCorrectiveActionIntro(String s) { this.vcCorrectiveActionIntro = s; }

    public List<ValueChainAction> getVcCorrectiveActions() { return vcCorrectiveActions; }
    public void setVcCorrectiveActions(List<ValueChainAction> list) { this.vcCorrectiveActions = list; }

    //princple 4 essential 1
    public String getPrinciple4Q1Intro() { return principle4Q1Intro; }
    public void setPrinciple4Q1Intro(String s) { this.principle4Q1Intro = s; }

    public List<String> getPrinciple4Q1Points() { return principle4Q1Points; }
    public void setPrinciple4Q1Points(List<String> l) { this.principle4Q1Points = l; }

    public String getPrinciple4Q1Conclusion() { return principle4Q1Conclusion; }
    public void setPrinciple4Q1Conclusion(String s) { this.principle4Q1Conclusion = s; }

    //princple 4 essential 2
    public List<StakeholderEngagement> getStakeholderEngagements() { return stakeholderEngagements; }
    public void setStakeholderEngagements(List<StakeholderEngagement> l) { this.stakeholderEngagements = l; }
    public String getStakeholderNote() { return stakeholderNote; }
    public void setStakeholderNote(String s) { this.stakeholderNote = s; }

    //princple 4 leadership 1
    public String getConsultationProcessDetails() { return consultationProcessDetails; }
    public void setConsultationProcessDetails(String s) { this.consultationProcessDetails = s; }

    //princple 4 leaderhsip 2
    public String getStakeholderConsultationUsed() { return stakeholderConsultationUsed; }
    public void setStakeholderConsultationUsed(String s) { this.stakeholderConsultationUsed = s; }

    public String getStakeholderConsultationDetails() { return stakeholderConsultationDetails; }
    public void setStakeholderConsultationDetails(String s) { this.stakeholderConsultationDetails = s; }

    //principle 4 leadership 3
    public String getVulnerableGroupIntro() { return vulnerableGroupIntro; }
    public void setVulnerableGroupIntro(String s) { this.vulnerableGroupIntro = s; }

    public List<String> getVulnerableGroupActions() { return vulnerableGroupActions; }
    public void setVulnerableGroupActions(List<String> list) { this.vulnerableGroupActions = list; }

    public String getVulnerableGroupConclusion() { return vulnerableGroupConclusion; }
    public void setVulnerableGroupConclusion(String s) { this.vulnerableGroupConclusion = s; }

    //princple 5 essetnial 1
    public String getHrEmpPermTotalCurr() {
        return hrEmpPermTotalCurr;
    }

    public void setHrEmpPermTotalCurr(String hrEmpPermTotalCurr) {
        this.hrEmpPermTotalCurr = hrEmpPermTotalCurr;
    }

    public String getHrEmpPermCovCurr() {
        return hrEmpPermCovCurr;
    }

    public void setHrEmpPermCovCurr(String hrEmpPermCovCurr) {
        this.hrEmpPermCovCurr = hrEmpPermCovCurr;
    }

    public String getHrEmpPermPercCurr() {
        return hrEmpPermPercCurr;
    }

    public void setHrEmpPermPercCurr(String hrEmpPermPercCurr) {
        this.hrEmpPermPercCurr = hrEmpPermPercCurr;
    }

    public String getHrEmpPermTotalPrev() {
        return hrEmpPermTotalPrev;
    }

    public void setHrEmpPermTotalPrev(String hrEmpPermTotalPrev) {
        this.hrEmpPermTotalPrev = hrEmpPermTotalPrev;
    }

    public String getHrEmpPermCovPrev() {
        return hrEmpPermCovPrev;
    }

    public void setHrEmpPermCovPrev(String hrEmpPermCovPrev) {
        this.hrEmpPermCovPrev = hrEmpPermCovPrev;
    }

    public String getHrEmpPermPercPrev() {
        return hrEmpPermPercPrev;
    }

    public void setHrEmpPermPercPrev(String hrEmpPermPercPrev) {
        this.hrEmpPermPercPrev = hrEmpPermPercPrev;
    }

    public String getHrEmpTempTotalCurr() {
        return hrEmpTempTotalCurr;
    }

    public void setHrEmpTempTotalCurr(String hrEmpTempTotalCurr) {
        this.hrEmpTempTotalCurr = hrEmpTempTotalCurr;
    }

    public String getHrEmpTempCovCurr() {
        return hrEmpTempCovCurr;
    }

    public void setHrEmpTempCovCurr(String hrEmpTempCovCurr) {
        this.hrEmpTempCovCurr = hrEmpTempCovCurr;
    }

    public String getHrEmpTempPercCurr() {
        return hrEmpTempPercCurr;
    }

    public void setHrEmpTempPercCurr(String hrEmpTempPercCurr) {
        this.hrEmpTempPercCurr = hrEmpTempPercCurr;
    }

    public String getHrEmpTempTotalPrev() {
        return hrEmpTempTotalPrev;
    }

    public void setHrEmpTempTotalPrev(String hrEmpTempTotalPrev) {
        this.hrEmpTempTotalPrev = hrEmpTempTotalPrev;
    }

    public String getHrEmpTempCovPrev() {
        return hrEmpTempCovPrev;
    }

    public void setHrEmpTempCovPrev(String hrEmpTempCovPrev) {
        this.hrEmpTempCovPrev = hrEmpTempCovPrev;
    }

    public String getHrEmpTempPercPrev() {
        return hrEmpTempPercPrev;
    }

    public void setHrEmpTempPercPrev(String hrEmpTempPercPrev) {
        this.hrEmpTempPercPrev = hrEmpTempPercPrev;
    }

    public String getHrEmpGrandTotalCurr() {
        return hrEmpGrandTotalCurr;
    }

    public void setHrEmpGrandTotalCurr(String hrEmpGrandTotalCurr) {
        this.hrEmpGrandTotalCurr = hrEmpGrandTotalCurr;
    }

    public String getHrEmpGrandCovCurr() {
        return hrEmpGrandCovCurr;
    }

    public void setHrEmpGrandCovCurr(String hrEmpGrandCovCurr) {
        this.hrEmpGrandCovCurr = hrEmpGrandCovCurr;
    }

    public String getHrEmpGrandPercCurr() {
        return hrEmpGrandPercCurr;
    }

    public void setHrEmpGrandPercCurr(String hrEmpGrandPercCurr) {
        this.hrEmpGrandPercCurr = hrEmpGrandPercCurr;
    }

    public String getHrEmpGrandTotalPrev() {
        return hrEmpGrandTotalPrev;
    }

    public void setHrEmpGrandTotalPrev(String hrEmpGrandTotalPrev) {
        this.hrEmpGrandTotalPrev = hrEmpGrandTotalPrev;
    }

    public String getHrEmpGrandCovPrev() {
        return hrEmpGrandCovPrev;
    }

    public void setHrEmpGrandCovPrev(String hrEmpGrandCovPrev) {
        this.hrEmpGrandCovPrev = hrEmpGrandCovPrev;
    }

    public String getHrEmpGrandPercPrev() {
        return hrEmpGrandPercPrev;
    }

    public void setHrEmpGrandPercPrev(String hrEmpGrandPercPrev) {
        this.hrEmpGrandPercPrev = hrEmpGrandPercPrev;
    }

    public String getHrWorkPermTotalCurr() {
        return hrWorkPermTotalCurr;
    }

    public void setHrWorkPermTotalCurr(String hrWorkPermTotalCurr) {
        this.hrWorkPermTotalCurr = hrWorkPermTotalCurr;
    }

    public String getHrWorkPermCovCurr() {
        return hrWorkPermCovCurr;
    }

    public void setHrWorkPermCovCurr(String hrWorkPermCovCurr) {
        this.hrWorkPermCovCurr = hrWorkPermCovCurr;
    }

    public String getHrWorkPermPercCurr() {
        return hrWorkPermPercCurr;
    }

    public void setHrWorkPermPercCurr(String hrWorkPermPercCurr) {
        this.hrWorkPermPercCurr = hrWorkPermPercCurr;
    }

    public String getHrWorkPermTotalPrev() {
        return hrWorkPermTotalPrev;
    }

    public void setHrWorkPermTotalPrev(String hrWorkPermTotalPrev) {
        this.hrWorkPermTotalPrev = hrWorkPermTotalPrev;
    }

    public String getHrWorkPermCovPrev() {
        return hrWorkPermCovPrev;
    }

    public void setHrWorkPermCovPrev(String hrWorkPermCovPrev) {
        this.hrWorkPermCovPrev = hrWorkPermCovPrev;
    }

    public String getHrWorkPermPercPrev() {
        return hrWorkPermPercPrev;
    }

    public void setHrWorkPermPercPrev(String hrWorkPermPercPrev) {
        this.hrWorkPermPercPrev = hrWorkPermPercPrev;
    }

    public String getHrWorkTempTotalCurr() {
        return hrWorkTempTotalCurr;
    }

    public void setHrWorkTempTotalCurr(String hrWorkTempTotalCurr) {
        this.hrWorkTempTotalCurr = hrWorkTempTotalCurr;
    }

    public String getHrWorkTempCovCurr() {
        return hrWorkTempCovCurr;
    }

    public void setHrWorkTempCovCurr(String hrWorkTempCovCurr) {
        this.hrWorkTempCovCurr = hrWorkTempCovCurr;
    }

    public String getHrWorkTempPercCurr() {
        return hrWorkTempPercCurr;
    }

    public void setHrWorkTempPercCurr(String hrWorkTempPercCurr) {
        this.hrWorkTempPercCurr = hrWorkTempPercCurr;
    }

    public String getHrWorkTempTotalPrev() {
        return hrWorkTempTotalPrev;
    }

    public void setHrWorkTempTotalPrev(String hrWorkTempTotalPrev) {
        this.hrWorkTempTotalPrev = hrWorkTempTotalPrev;
    }

    public String getHrWorkTempCovPrev() {
        return hrWorkTempCovPrev;
    }

    public void setHrWorkTempCovPrev(String hrWorkTempCovPrev) {
        this.hrWorkTempCovPrev = hrWorkTempCovPrev;
    }

    public String getHrWorkTempPercPrev() {
        return hrWorkTempPercPrev;
    }

    public void setHrWorkTempPercPrev(String hrWorkTempPercPrev) {
        this.hrWorkTempPercPrev = hrWorkTempPercPrev;
    }

    public String getHrWorkGrandTotalCurr() {
        return hrWorkGrandTotalCurr;
    }

    public void setHrWorkGrandTotalCurr(String hrWorkGrandTotalCurr) {
        this.hrWorkGrandTotalCurr = hrWorkGrandTotalCurr;
    }

    public String getHrWorkGrandCovCurr() {
        return hrWorkGrandCovCurr;
    }

    public void setHrWorkGrandCovCurr(String hrWorkGrandCovCurr) {
        this.hrWorkGrandCovCurr = hrWorkGrandCovCurr;
    }

    public String getHrWorkGrandPercCurr() {
        return hrWorkGrandPercCurr;
    }

    public void setHrWorkGrandPercCurr(String hrWorkGrandPercCurr) {
        this.hrWorkGrandPercCurr = hrWorkGrandPercCurr;
    }

    public String getHrWorkGrandTotalPrev() {
        return hrWorkGrandTotalPrev;
    }

    public void setHrWorkGrandTotalPrev(String hrWorkGrandTotalPrev) {
        this.hrWorkGrandTotalPrev = hrWorkGrandTotalPrev;
    }

    public String getHrWorkGrandCovPrev() {
        return hrWorkGrandCovPrev;
    }

    public void setHrWorkGrandCovPrev(String hrWorkGrandCovPrev) {
        this.hrWorkGrandCovPrev = hrWorkGrandCovPrev;
    }

    public String getHrWorkGrandPercPrev() {
        return hrWorkGrandPercPrev;
    }

    public void setHrWorkGrandPercPrev(String hrWorkGrandPercPrev) {
        this.hrWorkGrandPercPrev = hrWorkGrandPercPrev;
    }

    public String getHrTrainingNote() {
        return hrTrainingNote;
    }

    public void setHrTrainingNote(String hrTrainingNote) {
        this.hrTrainingNote = hrTrainingNote;
    }

    //princple 5 essential 2
    public String getMwWorkTempMaleMoreNoPrev() {
        return mwWorkTempMaleMoreNoPrev;
    }

    public void setMwWorkTempMaleMoreNoPrev(String mwWorkTempMaleMoreNoPrev) {
        this.mwWorkTempMaleMoreNoPrev = mwWorkTempMaleMoreNoPrev;
    }

    public String getMinWageNote() {
        return minWageNote;
    }

    public void setMinWageNote(String minWageNote) {
        this.minWageNote = minWageNote;
    }

    public String getMwEmpPermMaleTotalCurr() {
        return mwEmpPermMaleTotalCurr;
    }

    public void setMwEmpPermMaleTotalCurr(String mwEmpPermMaleTotalCurr) {
        this.mwEmpPermMaleTotalCurr = mwEmpPermMaleTotalCurr;
    }

    public String getMwEmpPermMaleEqNoCurr() {
        return mwEmpPermMaleEqNoCurr;
    }

    public void setMwEmpPermMaleEqNoCurr(String mwEmpPermMaleEqNoCurr) {
        this.mwEmpPermMaleEqNoCurr = mwEmpPermMaleEqNoCurr;
    }

    public String getMwEmpPermMaleEqPercCurr() {
        return mwEmpPermMaleEqPercCurr;
    }

    public void setMwEmpPermMaleEqPercCurr(String mwEmpPermMaleEqPercCurr) {
        this.mwEmpPermMaleEqPercCurr = mwEmpPermMaleEqPercCurr;
    }

    public String getMwEmpPermMaleMoreNoCurr() {
        return mwEmpPermMaleMoreNoCurr;
    }

    public void setMwEmpPermMaleMoreNoCurr(String mwEmpPermMaleMoreNoCurr) {
        this.mwEmpPermMaleMoreNoCurr = mwEmpPermMaleMoreNoCurr;
    }

    public String getMwEmpPermMaleMorePercCurr() {
        return mwEmpPermMaleMorePercCurr;
    }

    public void setMwEmpPermMaleMorePercCurr(String mwEmpPermMaleMorePercCurr) {
        this.mwEmpPermMaleMorePercCurr = mwEmpPermMaleMorePercCurr;
    }

    public String getMwEmpPermFemTotalCurr() {
        return mwEmpPermFemTotalCurr;
    }

    public void setMwEmpPermFemTotalCurr(String mwEmpPermFemTotalCurr) {
        this.mwEmpPermFemTotalCurr = mwEmpPermFemTotalCurr;
    }

    public String getMwEmpPermFemEqNoCurr() {
        return mwEmpPermFemEqNoCurr;
    }

    public void setMwEmpPermFemEqNoCurr(String mwEmpPermFemEqNoCurr) {
        this.mwEmpPermFemEqNoCurr = mwEmpPermFemEqNoCurr;
    }

    public String getMwEmpPermFemEqPercCurr() {
        return mwEmpPermFemEqPercCurr;
    }

    public void setMwEmpPermFemEqPercCurr(String mwEmpPermFemEqPercCurr) {
        this.mwEmpPermFemEqPercCurr = mwEmpPermFemEqPercCurr;
    }

    public String getMwEmpPermFemMoreNoCurr() {
        return mwEmpPermFemMoreNoCurr;
    }

    public void setMwEmpPermFemMoreNoCurr(String mwEmpPermFemMoreNoCurr) {
        this.mwEmpPermFemMoreNoCurr = mwEmpPermFemMoreNoCurr;
    }

    public String getMwEmpPermFemMorePercCurr() {
        return mwEmpPermFemMorePercCurr;
    }

    public void setMwEmpPermFemMorePercCurr(String mwEmpPermFemMorePercCurr) {
        this.mwEmpPermFemMorePercCurr = mwEmpPermFemMorePercCurr;
    }

    public String getMwEmpPermOthTotalCurr() {
        return mwEmpPermOthTotalCurr;
    }

    public void setMwEmpPermOthTotalCurr(String mwEmpPermOthTotalCurr) {
        this.mwEmpPermOthTotalCurr = mwEmpPermOthTotalCurr;
    }

    public String getMwEmpPermOthEqNoCurr() {
        return mwEmpPermOthEqNoCurr;
    }

    public void setMwEmpPermOthEqNoCurr(String mwEmpPermOthEqNoCurr) {
        this.mwEmpPermOthEqNoCurr = mwEmpPermOthEqNoCurr;
    }

    public String getMwEmpPermOthEqPercCurr() {
        return mwEmpPermOthEqPercCurr;
    }

    public void setMwEmpPermOthEqPercCurr(String mwEmpPermOthEqPercCurr) {
        this.mwEmpPermOthEqPercCurr = mwEmpPermOthEqPercCurr;
    }

    public String getMwEmpPermOthMoreNoCurr() {
        return mwEmpPermOthMoreNoCurr;
    }

    public void setMwEmpPermOthMoreNoCurr(String mwEmpPermOthMoreNoCurr) {
        this.mwEmpPermOthMoreNoCurr = mwEmpPermOthMoreNoCurr;
    }

    public String getMwEmpPermOthMorePercCurr() {
        return mwEmpPermOthMorePercCurr;
    }

    public void setMwEmpPermOthMorePercCurr(String mwEmpPermOthMorePercCurr) {
        this.mwEmpPermOthMorePercCurr = mwEmpPermOthMorePercCurr;
    }

    public String getMwEmpPermMaleTotalPrev() {
        return mwEmpPermMaleTotalPrev;
    }

    public void setMwEmpPermMaleTotalPrev(String mwEmpPermMaleTotalPrev) {
        this.mwEmpPermMaleTotalPrev = mwEmpPermMaleTotalPrev;
    }

    public String getMwEmpPermMaleEqNoPrev() {
        return mwEmpPermMaleEqNoPrev;
    }

    public void setMwEmpPermMaleEqNoPrev(String mwEmpPermMaleEqNoPrev) {
        this.mwEmpPermMaleEqNoPrev = mwEmpPermMaleEqNoPrev;
    }

    public String getMwEmpPermMaleEqPercPrev() {
        return mwEmpPermMaleEqPercPrev;
    }

    public void setMwEmpPermMaleEqPercPrev(String mwEmpPermMaleEqPercPrev) {
        this.mwEmpPermMaleEqPercPrev = mwEmpPermMaleEqPercPrev;
    }

    public String getMwEmpPermMaleMoreNoPrev() {
        return mwEmpPermMaleMoreNoPrev;
    }

    public void setMwEmpPermMaleMoreNoPrev(String mwEmpPermMaleMoreNoPrev) {
        this.mwEmpPermMaleMoreNoPrev = mwEmpPermMaleMoreNoPrev;
    }

    public String getMwEmpPermMaleMorePercPrev() {
        return mwEmpPermMaleMorePercPrev;
    }

    public void setMwEmpPermMaleMorePercPrev(String mwEmpPermMaleMorePercPrev) {
        this.mwEmpPermMaleMorePercPrev = mwEmpPermMaleMorePercPrev;
    }

    public String getMwEmpPermFemTotalPrev() {
        return mwEmpPermFemTotalPrev;
    }

    public void setMwEmpPermFemTotalPrev(String mwEmpPermFemTotalPrev) {
        this.mwEmpPermFemTotalPrev = mwEmpPermFemTotalPrev;
    }

    public String getMwEmpPermFemEqNoPrev() {
        return mwEmpPermFemEqNoPrev;
    }

    public void setMwEmpPermFemEqNoPrev(String mwEmpPermFemEqNoPrev) {
        this.mwEmpPermFemEqNoPrev = mwEmpPermFemEqNoPrev;
    }

    public String getMwEmpPermFemEqPercPrev() {
        return mwEmpPermFemEqPercPrev;
    }

    public void setMwEmpPermFemEqPercPrev(String mwEmpPermFemEqPercPrev) {
        this.mwEmpPermFemEqPercPrev = mwEmpPermFemEqPercPrev;
    }

    public String getMwEmpPermFemMoreNoPrev() {
        return mwEmpPermFemMoreNoPrev;
    }

    public void setMwEmpPermFemMoreNoPrev(String mwEmpPermFemMoreNoPrev) {
        this.mwEmpPermFemMoreNoPrev = mwEmpPermFemMoreNoPrev;
    }

    public String getMwEmpPermFemMorePercPrev() {
        return mwEmpPermFemMorePercPrev;
    }

    public void setMwEmpPermFemMorePercPrev(String mwEmpPermFemMorePercPrev) {
        this.mwEmpPermFemMorePercPrev = mwEmpPermFemMorePercPrev;
    }

    public String getMwEmpPermOthTotalPrev() {
        return mwEmpPermOthTotalPrev;
    }

    public void setMwEmpPermOthTotalPrev(String mwEmpPermOthTotalPrev) {
        this.mwEmpPermOthTotalPrev = mwEmpPermOthTotalPrev;
    }

    public String getMwEmpPermOthEqNoPrev() {
        return mwEmpPermOthEqNoPrev;
    }

    public void setMwEmpPermOthEqNoPrev(String mwEmpPermOthEqNoPrev) {
        this.mwEmpPermOthEqNoPrev = mwEmpPermOthEqNoPrev;
    }

    public String getMwEmpPermOthEqPercPrev() {
        return mwEmpPermOthEqPercPrev;
    }

    public void setMwEmpPermOthEqPercPrev(String mwEmpPermOthEqPercPrev) {
        this.mwEmpPermOthEqPercPrev = mwEmpPermOthEqPercPrev;
    }

    public String getMwEmpPermOthMoreNoPrev() {
        return mwEmpPermOthMoreNoPrev;
    }

    public void setMwEmpPermOthMoreNoPrev(String mwEmpPermOthMoreNoPrev) {
        this.mwEmpPermOthMoreNoPrev = mwEmpPermOthMoreNoPrev;
    }

    public String getMwEmpPermOthMorePercPrev() {
        return mwEmpPermOthMorePercPrev;
    }

    public void setMwEmpPermOthMorePercPrev(String mwEmpPermOthMorePercPrev) {
        this.mwEmpPermOthMorePercPrev = mwEmpPermOthMorePercPrev;
    }

    public String getMwEmpTempMaleTotalCurr() {
        return mwEmpTempMaleTotalCurr;
    }

    public void setMwEmpTempMaleTotalCurr(String mwEmpTempMaleTotalCurr) {
        this.mwEmpTempMaleTotalCurr = mwEmpTempMaleTotalCurr;
    }

    public String getMwEmpTempMaleEqNoCurr() {
        return mwEmpTempMaleEqNoCurr;
    }

    public void setMwEmpTempMaleEqNoCurr(String mwEmpTempMaleEqNoCurr) {
        this.mwEmpTempMaleEqNoCurr = mwEmpTempMaleEqNoCurr;
    }

    public String getMwEmpTempMaleEqPercCurr() {
        return mwEmpTempMaleEqPercCurr;
    }

    public void setMwEmpTempMaleEqPercCurr(String mwEmpTempMaleEqPercCurr) {
        this.mwEmpTempMaleEqPercCurr = mwEmpTempMaleEqPercCurr;
    }

    public String getMwEmpTempMaleMoreNoCurr() {
        return mwEmpTempMaleMoreNoCurr;
    }

    public void setMwEmpTempMaleMoreNoCurr(String mwEmpTempMaleMoreNoCurr) {
        this.mwEmpTempMaleMoreNoCurr = mwEmpTempMaleMoreNoCurr;
    }

    public String getMwEmpTempMaleMorePercCurr() {
        return mwEmpTempMaleMorePercCurr;
    }

    public void setMwEmpTempMaleMorePercCurr(String mwEmpTempMaleMorePercCurr) {
        this.mwEmpTempMaleMorePercCurr = mwEmpTempMaleMorePercCurr;
    }

    public String getMwEmpTempFemTotalCurr() {
        return mwEmpTempFemTotalCurr;
    }

    public void setMwEmpTempFemTotalCurr(String mwEmpTempFemTotalCurr) {
        this.mwEmpTempFemTotalCurr = mwEmpTempFemTotalCurr;
    }

    public String getMwEmpTempFemEqNoCurr() {
        return mwEmpTempFemEqNoCurr;
    }

    public void setMwEmpTempFemEqNoCurr(String mwEmpTempFemEqNoCurr) {
        this.mwEmpTempFemEqNoCurr = mwEmpTempFemEqNoCurr;
    }

    public String getMwEmpTempFemEqPercCurr() {
        return mwEmpTempFemEqPercCurr;
    }

    public void setMwEmpTempFemEqPercCurr(String mwEmpTempFemEqPercCurr) {
        this.mwEmpTempFemEqPercCurr = mwEmpTempFemEqPercCurr;
    }

    public String getMwEmpTempFemMoreNoCurr() {
        return mwEmpTempFemMoreNoCurr;
    }

    public void setMwEmpTempFemMoreNoCurr(String mwEmpTempFemMoreNoCurr) {
        this.mwEmpTempFemMoreNoCurr = mwEmpTempFemMoreNoCurr;
    }

    public String getMwEmpTempFemMorePercCurr() {
        return mwEmpTempFemMorePercCurr;
    }

    public void setMwEmpTempFemMorePercCurr(String mwEmpTempFemMorePercCurr) {
        this.mwEmpTempFemMorePercCurr = mwEmpTempFemMorePercCurr;
    }

    public String getMwEmpTempOthTotalCurr() {
        return mwEmpTempOthTotalCurr;
    }

    public void setMwEmpTempOthTotalCurr(String mwEmpTempOthTotalCurr) {
        this.mwEmpTempOthTotalCurr = mwEmpTempOthTotalCurr;
    }

    public String getMwEmpTempOthEqNoCurr() {
        return mwEmpTempOthEqNoCurr;
    }

    public void setMwEmpTempOthEqNoCurr(String mwEmpTempOthEqNoCurr) {
        this.mwEmpTempOthEqNoCurr = mwEmpTempOthEqNoCurr;
    }

    public String getMwEmpTempOthEqPercCurr() {
        return mwEmpTempOthEqPercCurr;
    }

    public void setMwEmpTempOthEqPercCurr(String mwEmpTempOthEqPercCurr) {
        this.mwEmpTempOthEqPercCurr = mwEmpTempOthEqPercCurr;
    }

    public String getMwEmpTempOthMoreNoCurr() {
        return mwEmpTempOthMoreNoCurr;
    }

    public void setMwEmpTempOthMoreNoCurr(String mwEmpTempOthMoreNoCurr) {
        this.mwEmpTempOthMoreNoCurr = mwEmpTempOthMoreNoCurr;
    }

    public String getMwEmpTempOthMorePercCurr() {
        return mwEmpTempOthMorePercCurr;
    }

    public void setMwEmpTempOthMorePercCurr(String mwEmpTempOthMorePercCurr) {
        this.mwEmpTempOthMorePercCurr = mwEmpTempOthMorePercCurr;
    }

    public String getMwEmpTempMaleTotalPrev() {
        return mwEmpTempMaleTotalPrev;
    }

    public void setMwEmpTempMaleTotalPrev(String mwEmpTempMaleTotalPrev) {
        this.mwEmpTempMaleTotalPrev = mwEmpTempMaleTotalPrev;
    }

    public String getMwEmpTempMaleEqNoPrev() {
        return mwEmpTempMaleEqNoPrev;
    }

    public void setMwEmpTempMaleEqNoPrev(String mwEmpTempMaleEqNoPrev) {
        this.mwEmpTempMaleEqNoPrev = mwEmpTempMaleEqNoPrev;
    }

    public String getMwEmpTempMaleEqPercPrev() {
        return mwEmpTempMaleEqPercPrev;
    }

    public void setMwEmpTempMaleEqPercPrev(String mwEmpTempMaleEqPercPrev) {
        this.mwEmpTempMaleEqPercPrev = mwEmpTempMaleEqPercPrev;
    }

    public String getMwEmpTempMaleMoreNoPrev() {
        return mwEmpTempMaleMoreNoPrev;
    }

    public void setMwEmpTempMaleMoreNoPrev(String mwEmpTempMaleMoreNoPrev) {
        this.mwEmpTempMaleMoreNoPrev = mwEmpTempMaleMoreNoPrev;
    }

    public String getMwEmpTempMaleMorePercPrev() {
        return mwEmpTempMaleMorePercPrev;
    }

    public void setMwEmpTempMaleMorePercPrev(String mwEmpTempMaleMorePercPrev) {
        this.mwEmpTempMaleMorePercPrev = mwEmpTempMaleMorePercPrev;
    }

    public String getMwEmpTempFemTotalPrev() {
        return mwEmpTempFemTotalPrev;
    }

    public void setMwEmpTempFemTotalPrev(String mwEmpTempFemTotalPrev) {
        this.mwEmpTempFemTotalPrev = mwEmpTempFemTotalPrev;
    }

    public String getMwEmpTempFemEqNoPrev() {
        return mwEmpTempFemEqNoPrev;
    }

    public void setMwEmpTempFemEqNoPrev(String mwEmpTempFemEqNoPrev) {
        this.mwEmpTempFemEqNoPrev = mwEmpTempFemEqNoPrev;
    }

    public String getMwEmpTempFemEqPercPrev() {
        return mwEmpTempFemEqPercPrev;
    }

    public void setMwEmpTempFemEqPercPrev(String mwEmpTempFemEqPercPrev) {
        this.mwEmpTempFemEqPercPrev = mwEmpTempFemEqPercPrev;
    }

    public String getMwEmpTempFemMoreNoPrev() {
        return mwEmpTempFemMoreNoPrev;
    }

    public void setMwEmpTempFemMoreNoPrev(String mwEmpTempFemMoreNoPrev) {
        this.mwEmpTempFemMoreNoPrev = mwEmpTempFemMoreNoPrev;
    }

    public String getMwEmpTempFemMorePercPrev() {
        return mwEmpTempFemMorePercPrev;
    }

    public void setMwEmpTempFemMorePercPrev(String mwEmpTempFemMorePercPrev) {
        this.mwEmpTempFemMorePercPrev = mwEmpTempFemMorePercPrev;
    }

    public String getMwEmpTempOthTotalPrev() {
        return mwEmpTempOthTotalPrev;
    }

    public void setMwEmpTempOthTotalPrev(String mwEmpTempOthTotalPrev) {
        this.mwEmpTempOthTotalPrev = mwEmpTempOthTotalPrev;
    }

    public String getMwEmpTempOthEqNoPrev() {
        return mwEmpTempOthEqNoPrev;
    }

    public void setMwEmpTempOthEqNoPrev(String mwEmpTempOthEqNoPrev) {
        this.mwEmpTempOthEqNoPrev = mwEmpTempOthEqNoPrev;
    }

    public String getMwEmpTempOthEqPercPrev() {
        return mwEmpTempOthEqPercPrev;
    }

    public void setMwEmpTempOthEqPercPrev(String mwEmpTempOthEqPercPrev) {
        this.mwEmpTempOthEqPercPrev = mwEmpTempOthEqPercPrev;
    }

    public String getMwEmpTempOthMoreNoPrev() {
        return mwEmpTempOthMoreNoPrev;
    }

    public void setMwEmpTempOthMoreNoPrev(String mwEmpTempOthMoreNoPrev) {
        this.mwEmpTempOthMoreNoPrev = mwEmpTempOthMoreNoPrev;
    }

    public String getMwEmpTempOthMorePercPrev() {
        return mwEmpTempOthMorePercPrev;
    }

    public void setMwEmpTempOthMorePercPrev(String mwEmpTempOthMorePercPrev) {
        this.mwEmpTempOthMorePercPrev = mwEmpTempOthMorePercPrev;
    }

    public String getMwWorkPermMaleTotalCurr() {
        return mwWorkPermMaleTotalCurr;
    }

    public void setMwWorkPermMaleTotalCurr(String mwWorkPermMaleTotalCurr) {
        this.mwWorkPermMaleTotalCurr = mwWorkPermMaleTotalCurr;
    }

    public String getMwWorkPermMaleEqNoCurr() {
        return mwWorkPermMaleEqNoCurr;
    }

    public void setMwWorkPermMaleEqNoCurr(String mwWorkPermMaleEqNoCurr) {
        this.mwWorkPermMaleEqNoCurr = mwWorkPermMaleEqNoCurr;
    }

    public String getMwWorkPermMaleEqPercCurr() {
        return mwWorkPermMaleEqPercCurr;
    }

    public void setMwWorkPermMaleEqPercCurr(String mwWorkPermMaleEqPercCurr) {
        this.mwWorkPermMaleEqPercCurr = mwWorkPermMaleEqPercCurr;
    }

    public String getMwWorkPermMaleMoreNoCurr() {
        return mwWorkPermMaleMoreNoCurr;
    }

    public void setMwWorkPermMaleMoreNoCurr(String mwWorkPermMaleMoreNoCurr) {
        this.mwWorkPermMaleMoreNoCurr = mwWorkPermMaleMoreNoCurr;
    }

    public String getMwWorkPermMaleMorePercCurr() {
        return mwWorkPermMaleMorePercCurr;
    }

    public void setMwWorkPermMaleMorePercCurr(String mwWorkPermMaleMorePercCurr) {
        this.mwWorkPermMaleMorePercCurr = mwWorkPermMaleMorePercCurr;
    }

    public String getMwWorkPermFemTotalCurr() {
        return mwWorkPermFemTotalCurr;
    }

    public void setMwWorkPermFemTotalCurr(String mwWorkPermFemTotalCurr) {
        this.mwWorkPermFemTotalCurr = mwWorkPermFemTotalCurr;
    }

    public String getMwWorkPermFemEqNoCurr() {
        return mwWorkPermFemEqNoCurr;
    }

    public void setMwWorkPermFemEqNoCurr(String mwWorkPermFemEqNoCurr) {
        this.mwWorkPermFemEqNoCurr = mwWorkPermFemEqNoCurr;
    }

    public String getMwWorkPermFemEqPercCurr() {
        return mwWorkPermFemEqPercCurr;
    }

    public void setMwWorkPermFemEqPercCurr(String mwWorkPermFemEqPercCurr) {
        this.mwWorkPermFemEqPercCurr = mwWorkPermFemEqPercCurr;
    }

    public String getMwWorkPermFemMoreNoCurr() {
        return mwWorkPermFemMoreNoCurr;
    }

    public void setMwWorkPermFemMoreNoCurr(String mwWorkPermFemMoreNoCurr) {
        this.mwWorkPermFemMoreNoCurr = mwWorkPermFemMoreNoCurr;
    }

    public String getMwWorkPermFemMorePercCurr() {
        return mwWorkPermFemMorePercCurr;
    }

    public void setMwWorkPermFemMorePercCurr(String mwWorkPermFemMorePercCurr) {
        this.mwWorkPermFemMorePercCurr = mwWorkPermFemMorePercCurr;
    }

    public String getMwWorkPermOthTotalCurr() {
        return mwWorkPermOthTotalCurr;
    }

    public void setMwWorkPermOthTotalCurr(String mwWorkPermOthTotalCurr) {
        this.mwWorkPermOthTotalCurr = mwWorkPermOthTotalCurr;
    }

    public String getMwWorkPermOthEqNoCurr() {
        return mwWorkPermOthEqNoCurr;
    }

    public void setMwWorkPermOthEqNoCurr(String mwWorkPermOthEqNoCurr) {
        this.mwWorkPermOthEqNoCurr = mwWorkPermOthEqNoCurr;
    }

    public String getMwWorkPermOthEqPercCurr() {
        return mwWorkPermOthEqPercCurr;
    }

    public void setMwWorkPermOthEqPercCurr(String mwWorkPermOthEqPercCurr) {
        this.mwWorkPermOthEqPercCurr = mwWorkPermOthEqPercCurr;
    }

    public String getMwWorkPermOthMoreNoCurr() {
        return mwWorkPermOthMoreNoCurr;
    }

    public void setMwWorkPermOthMoreNoCurr(String mwWorkPermOthMoreNoCurr) {
        this.mwWorkPermOthMoreNoCurr = mwWorkPermOthMoreNoCurr;
    }

    public String getMwWorkPermOthMorePercCurr() {
        return mwWorkPermOthMorePercCurr;
    }

    public void setMwWorkPermOthMorePercCurr(String mwWorkPermOthMorePercCurr) {
        this.mwWorkPermOthMorePercCurr = mwWorkPermOthMorePercCurr;
    }

    public String getMwWorkPermMaleTotalPrev() {
        return mwWorkPermMaleTotalPrev;
    }

    public void setMwWorkPermMaleTotalPrev(String mwWorkPermMaleTotalPrev) {
        this.mwWorkPermMaleTotalPrev = mwWorkPermMaleTotalPrev;
    }

    public String getMwWorkPermMaleEqNoPrev() {
        return mwWorkPermMaleEqNoPrev;
    }

    public void setMwWorkPermMaleEqNoPrev(String mwWorkPermMaleEqNoPrev) {
        this.mwWorkPermMaleEqNoPrev = mwWorkPermMaleEqNoPrev;
    }

    public String getMwWorkPermMaleEqPercPrev() {
        return mwWorkPermMaleEqPercPrev;
    }

    public void setMwWorkPermMaleEqPercPrev(String mwWorkPermMaleEqPercPrev) {
        this.mwWorkPermMaleEqPercPrev = mwWorkPermMaleEqPercPrev;
    }

    public String getMwWorkPermMaleMoreNoPrev() {
        return mwWorkPermMaleMoreNoPrev;
    }

    public void setMwWorkPermMaleMoreNoPrev(String mwWorkPermMaleMoreNoPrev) {
        this.mwWorkPermMaleMoreNoPrev = mwWorkPermMaleMoreNoPrev;
    }

    public String getMwWorkPermMaleMorePercPrev() {
        return mwWorkPermMaleMorePercPrev;
    }

    public void setMwWorkPermMaleMorePercPrev(String mwWorkPermMaleMorePercPrev) {
        this.mwWorkPermMaleMorePercPrev = mwWorkPermMaleMorePercPrev;
    }

    public String getMwWorkPermFemTotalPrev() {
        return mwWorkPermFemTotalPrev;
    }

    public void setMwWorkPermFemTotalPrev(String mwWorkPermFemTotalPrev) {
        this.mwWorkPermFemTotalPrev = mwWorkPermFemTotalPrev;
    }

    public String getMwWorkPermFemEqNoPrev() {
        return mwWorkPermFemEqNoPrev;
    }

    public void setMwWorkPermFemEqNoPrev(String mwWorkPermFemEqNoPrev) {
        this.mwWorkPermFemEqNoPrev = mwWorkPermFemEqNoPrev;
    }

    public String getMwWorkPermFemEqPercPrev() {
        return mwWorkPermFemEqPercPrev;
    }

    public void setMwWorkPermFemEqPercPrev(String mwWorkPermFemEqPercPrev) {
        this.mwWorkPermFemEqPercPrev = mwWorkPermFemEqPercPrev;
    }

    public String getMwWorkPermFemMoreNoPrev() {
        return mwWorkPermFemMoreNoPrev;
    }

    public void setMwWorkPermFemMoreNoPrev(String mwWorkPermFemMoreNoPrev) {
        this.mwWorkPermFemMoreNoPrev = mwWorkPermFemMoreNoPrev;
    }

    public String getMwWorkPermFemMorePercPrev() {
        return mwWorkPermFemMorePercPrev;
    }

    public void setMwWorkPermFemMorePercPrev(String mwWorkPermFemMorePercPrev) {
        this.mwWorkPermFemMorePercPrev = mwWorkPermFemMorePercPrev;
    }

    public String getMwWorkPermOthTotalPrev() {
        return mwWorkPermOthTotalPrev;
    }

    public void setMwWorkPermOthTotalPrev(String mwWorkPermOthTotalPrev) {
        this.mwWorkPermOthTotalPrev = mwWorkPermOthTotalPrev;
    }

    public String getMwWorkPermOthEqNoPrev() {
        return mwWorkPermOthEqNoPrev;
    }

    public void setMwWorkPermOthEqNoPrev(String mwWorkPermOthEqNoPrev) {
        this.mwWorkPermOthEqNoPrev = mwWorkPermOthEqNoPrev;
    }

    public String getMwWorkPermOthEqPercPrev() {
        return mwWorkPermOthEqPercPrev;
    }

    public void setMwWorkPermOthEqPercPrev(String mwWorkPermOthEqPercPrev) {
        this.mwWorkPermOthEqPercPrev = mwWorkPermOthEqPercPrev;
    }

    public String getMwWorkPermOthMoreNoPrev() {
        return mwWorkPermOthMoreNoPrev;
    }

    public void setMwWorkPermOthMoreNoPrev(String mwWorkPermOthMoreNoPrev) {
        this.mwWorkPermOthMoreNoPrev = mwWorkPermOthMoreNoPrev;
    }

    public String getMwWorkPermOthMorePercPrev() {
        return mwWorkPermOthMorePercPrev;
    }

    public void setMwWorkPermOthMorePercPrev(String mwWorkPermOthMorePercPrev) {
        this.mwWorkPermOthMorePercPrev = mwWorkPermOthMorePercPrev;
    }

    public String getMwWorkTempMaleTotalCurr() {
        return mwWorkTempMaleTotalCurr;
    }

    public void setMwWorkTempMaleTotalCurr(String mwWorkTempMaleTotalCurr) {
        this.mwWorkTempMaleTotalCurr = mwWorkTempMaleTotalCurr;
    }

    public String getMwWorkTempMaleEqNoCurr() {
        return mwWorkTempMaleEqNoCurr;
    }

    public void setMwWorkTempMaleEqNoCurr(String mwWorkTempMaleEqNoCurr) {
        this.mwWorkTempMaleEqNoCurr = mwWorkTempMaleEqNoCurr;
    }

    public String getMwWorkTempMaleEqPercCurr() {
        return mwWorkTempMaleEqPercCurr;
    }

    public void setMwWorkTempMaleEqPercCurr(String mwWorkTempMaleEqPercCurr) {
        this.mwWorkTempMaleEqPercCurr = mwWorkTempMaleEqPercCurr;
    }

    public String getMwWorkTempMaleMoreNoCurr() {
        return mwWorkTempMaleMoreNoCurr;
    }

    public void setMwWorkTempMaleMoreNoCurr(String mwWorkTempMaleMoreNoCurr) {
        this.mwWorkTempMaleMoreNoCurr = mwWorkTempMaleMoreNoCurr;
    }

    public String getMwWorkTempMaleMorePercCurr() {
        return mwWorkTempMaleMorePercCurr;
    }

    public void setMwWorkTempMaleMorePercCurr(String mwWorkTempMaleMorePercCurr) {
        this.mwWorkTempMaleMorePercCurr = mwWorkTempMaleMorePercCurr;
    }

    public String getMwWorkTempFemTotalCurr() {
        return mwWorkTempFemTotalCurr;
    }

    public void setMwWorkTempFemTotalCurr(String mwWorkTempFemTotalCurr) {
        this.mwWorkTempFemTotalCurr = mwWorkTempFemTotalCurr;
    }

    public String getMwWorkTempFemEqNoCurr() {
        return mwWorkTempFemEqNoCurr;
    }

    public void setMwWorkTempFemEqNoCurr(String mwWorkTempFemEqNoCurr) {
        this.mwWorkTempFemEqNoCurr = mwWorkTempFemEqNoCurr;
    }

    public String getMwWorkTempFemEqPercCurr() {
        return mwWorkTempFemEqPercCurr;
    }

    public void setMwWorkTempFemEqPercCurr(String mwWorkTempFemEqPercCurr) {
        this.mwWorkTempFemEqPercCurr = mwWorkTempFemEqPercCurr;
    }

    public String getMwWorkTempFemMoreNoCurr() {
        return mwWorkTempFemMoreNoCurr;
    }

    public void setMwWorkTempFemMoreNoCurr(String mwWorkTempFemMoreNoCurr) {
        this.mwWorkTempFemMoreNoCurr = mwWorkTempFemMoreNoCurr;
    }

    public String getMwWorkTempFemMorePercCurr() {
        return mwWorkTempFemMorePercCurr;
    }

    public void setMwWorkTempFemMorePercCurr(String mwWorkTempFemMorePercCurr) {
        this.mwWorkTempFemMorePercCurr = mwWorkTempFemMorePercCurr;
    }

    public String getMwWorkTempOthTotalCurr() {
        return mwWorkTempOthTotalCurr;
    }

    public void setMwWorkTempOthTotalCurr(String mwWorkTempOthTotalCurr) {
        this.mwWorkTempOthTotalCurr = mwWorkTempOthTotalCurr;
    }

    public String getMwWorkTempOthEqNoCurr() {
        return mwWorkTempOthEqNoCurr;
    }

    public void setMwWorkTempOthEqNoCurr(String mwWorkTempOthEqNoCurr) {
        this.mwWorkTempOthEqNoCurr = mwWorkTempOthEqNoCurr;
    }

    public String getMwWorkTempOthEqPercCurr() {
        return mwWorkTempOthEqPercCurr;
    }

    public void setMwWorkTempOthEqPercCurr(String mwWorkTempOthEqPercCurr) {
        this.mwWorkTempOthEqPercCurr = mwWorkTempOthEqPercCurr;
    }

    public String getMwWorkTempOthMoreNoCurr() {
        return mwWorkTempOthMoreNoCurr;
    }

    public void setMwWorkTempOthMoreNoCurr(String mwWorkTempOthMoreNoCurr) {
        this.mwWorkTempOthMoreNoCurr = mwWorkTempOthMoreNoCurr;
    }

    public String getMwWorkTempOthMorePercCurr() {
        return mwWorkTempOthMorePercCurr;
    }

    public void setMwWorkTempOthMorePercCurr(String mwWorkTempOthMorePercCurr) {
        this.mwWorkTempOthMorePercCurr = mwWorkTempOthMorePercCurr;
    }

    public String getMwWorkTempMaleTotalPrev() {
        return mwWorkTempMaleTotalPrev;
    }

    public void setMwWorkTempMaleTotalPrev(String mwWorkTempMaleTotalPrev) {
        this.mwWorkTempMaleTotalPrev = mwWorkTempMaleTotalPrev;
    }

    public String getMwWorkTempMaleEqNoPrev() {
        return mwWorkTempMaleEqNoPrev;
    }

    public void setMwWorkTempMaleEqNoPrev(String mwWorkTempMaleEqNoPrev) {
        this.mwWorkTempMaleEqNoPrev = mwWorkTempMaleEqNoPrev;
    }

    public String getMwWorkTempMaleEqPercPrev() {
        return mwWorkTempMaleEqPercPrev;
    }

    public void setMwWorkTempMaleEqPercPrev(String mwWorkTempMaleEqPercPrev) {
        this.mwWorkTempMaleEqPercPrev = mwWorkTempMaleEqPercPrev;
    }

    public String getMwWorkTempMaleMorePercPrev() {
        return mwWorkTempMaleMorePercPrev;
    }

    public void setMwWorkTempMaleMorePercPrev(String mwWorkTempMaleMorePercPrev) {
        this.mwWorkTempMaleMorePercPrev = mwWorkTempMaleMorePercPrev;
    }

    public String getMwWorkTempFemTotalPrev() {
        return mwWorkTempFemTotalPrev;
    }

    public void setMwWorkTempFemTotalPrev(String mwWorkTempFemTotalPrev) {
        this.mwWorkTempFemTotalPrev = mwWorkTempFemTotalPrev;
    }

    public String getMwWorkTempFemEqNoPrev() {
        return mwWorkTempFemEqNoPrev;
    }

    public void setMwWorkTempFemEqNoPrev(String mwWorkTempFemEqNoPrev) {
        this.mwWorkTempFemEqNoPrev = mwWorkTempFemEqNoPrev;
    }

    public String getMwWorkTempFemEqPercPrev() {
        return mwWorkTempFemEqPercPrev;
    }

    public void setMwWorkTempFemEqPercPrev(String mwWorkTempFemEqPercPrev) {
        this.mwWorkTempFemEqPercPrev = mwWorkTempFemEqPercPrev;
    }

    public String getMwWorkTempFemMoreNoPrev() {
        return mwWorkTempFemMoreNoPrev;
    }

    public void setMwWorkTempFemMoreNoPrev(String mwWorkTempFemMoreNoPrev) {
        this.mwWorkTempFemMoreNoPrev = mwWorkTempFemMoreNoPrev;
    }

    public String getMwWorkTempFemMorePercPrev() {
        return mwWorkTempFemMorePercPrev;
    }

    public void setMwWorkTempFemMorePercPrev(String mwWorkTempFemMorePercPrev) {
        this.mwWorkTempFemMorePercPrev = mwWorkTempFemMorePercPrev;
    }

    public String getMwWorkTempOthTotalPrev() {
        return mwWorkTempOthTotalPrev;
    }

    public void setMwWorkTempOthTotalPrev(String mwWorkTempOthTotalPrev) {
        this.mwWorkTempOthTotalPrev = mwWorkTempOthTotalPrev;
    }

    public String getMwWorkTempOthEqNoPrev() {
        return mwWorkTempOthEqNoPrev;
    }

    public void setMwWorkTempOthEqNoPrev(String mwWorkTempOthEqNoPrev) {
        this.mwWorkTempOthEqNoPrev = mwWorkTempOthEqNoPrev;
    }

    public String getMwWorkTempOthEqPercPrev() {
        return mwWorkTempOthEqPercPrev;
    }

    public void setMwWorkTempOthEqPercPrev(String mwWorkTempOthEqPercPrev) {
        this.mwWorkTempOthEqPercPrev = mwWorkTempOthEqPercPrev;
    }

    public String getMwWorkTempOthMoreNoPrev() {
        return mwWorkTempOthMoreNoPrev;
    }

    public void setMwWorkTempOthMoreNoPrev(String mwWorkTempOthMoreNoPrev) {
        this.mwWorkTempOthMoreNoPrev = mwWorkTempOthMoreNoPrev;
    }

    public String getMwWorkTempOthMorePercPrev() {
        return mwWorkTempOthMorePercPrev;
    }

    public void setMwWorkTempOthMorePercPrev(String mwWorkTempOthMorePercPrev) {
        this.mwWorkTempOthMorePercPrev = mwWorkTempOthMorePercPrev;
    }

    //princple 5 essential 3
    public String getRemKmpMaleMedian() {
        return remKmpMaleMedian;
    }

    public void setRemKmpMaleMedian(String remKmpMaleMedian) {
        this.remKmpMaleMedian = remKmpMaleMedian;
    }

    public String getRemBodMaleNum() {
        return remBodMaleNum;
    }

    public void setRemBodMaleNum(String remBodMaleNum) {
        this.remBodMaleNum = remBodMaleNum;
    }

    public String getRemBodMaleMedian() {
        return remBodMaleMedian;
    }

    public void setRemBodMaleMedian(String remBodMaleMedian) {
        this.remBodMaleMedian = remBodMaleMedian;
    }

    public String getRemBodFemNum() {
        return remBodFemNum;
    }

    public void setRemBodFemNum(String remBodFemNum) {
        this.remBodFemNum = remBodFemNum;
    }

    public String getRemBodFemMedian() {
        return remBodFemMedian;
    }

    public void setRemBodFemMedian(String remBodFemMedian) {
        this.remBodFemMedian = remBodFemMedian;
    }

    public String getRemKmpMaleNum() {
        return remKmpMaleNum;
    }

    public void setRemKmpMaleNum(String remKmpMaleNum) {
        this.remKmpMaleNum = remKmpMaleNum;
    }

    public String getRemKmpFemNum() {
        return remKmpFemNum;
    }

    public void setRemKmpFemNum(String remKmpFemNum) {
        this.remKmpFemNum = remKmpFemNum;
    }

    public String getRemKmpFemMedian() {
        return remKmpFemMedian;
    }

    public void setRemKmpFemMedian(String remKmpFemMedian) {
        this.remKmpFemMedian = remKmpFemMedian;
    }

    public String getRemEmpMaleNum() {
        return remEmpMaleNum;
    }

    public void setRemEmpMaleNum(String remEmpMaleNum) {
        this.remEmpMaleNum = remEmpMaleNum;
    }

    public String getRemEmpMaleMedian() {
        return remEmpMaleMedian;
    }

    public void setRemEmpMaleMedian(String remEmpMaleMedian) {
        this.remEmpMaleMedian = remEmpMaleMedian;
    }

    public String getRemEmpFemNum() {
        return remEmpFemNum;
    }

    public void setRemEmpFemNum(String remEmpFemNum) {
        this.remEmpFemNum = remEmpFemNum;
    }

    public String getRemEmpFemMedian() {
        return remEmpFemMedian;
    }

    public void setRemEmpFemMedian(String remEmpFemMedian) {
        this.remEmpFemMedian = remEmpFemMedian;
    }

    public String getRemWorkMaleNum() {
        return remWorkMaleNum;
    }

    public void setRemWorkMaleNum(String remWorkMaleNum) {
        this.remWorkMaleNum = remWorkMaleNum;
    }

    public String getRemWorkMaleMedian() {
        return remWorkMaleMedian;
    }

    public void setRemWorkMaleMedian(String remWorkMaleMedian) {
        this.remWorkMaleMedian = remWorkMaleMedian;
    }

    public String getRemWorkFemNum() {
        return remWorkFemNum;
    }

    public void setRemWorkFemNum(String remWorkFemNum) {
        this.remWorkFemNum = remWorkFemNum;
    }

    public String getRemWorkFemMedian() {
        return remWorkFemMedian;
    }

    public void setRemWorkFemMedian(String remWorkFemMedian) {
        this.remWorkFemMedian = remWorkFemMedian;
    }

    public String getRemunerationNote() {
        return remunerationNote;
    }

    public void setRemunerationNote(String remunerationNote) {
        this.remunerationNote = remunerationNote;
    }

    //princple 5 essential 3b
    public String getGrossWagesFemalePercCurr() { return grossWagesFemalePercCurr; }
    public void setGrossWagesFemalePercCurr(String s) { this.grossWagesFemalePercCurr = s; }

    public String getGrossWagesFemalePercPrev() { return grossWagesFemalePercPrev; }
    public void setGrossWagesFemalePercPrev(String s) { this.grossWagesFemalePercPrev = s; }

    public String getGrossWagesNote() { return grossWagesNote; }
    public void setGrossWagesNote(String s) { this.grossWagesNote = s; }

    //princple 5 essential 4
    public String getHumanRightsFocalPoint() { return humanRightsFocalPoint; }
    public void setHumanRightsFocalPoint(String s) { this.humanRightsFocalPoint = s; }

    public String getHumanRightsFocalDetails() { return humanRightsFocalDetails; }
    public void setHumanRightsFocalDetails(String s) { this.humanRightsFocalDetails = s; }

    //princple 5 essential 5
    public String getHumanRightsGrievanceMechanism() { return humanRightsGrievanceMechanism; }
    public void setHumanRightsGrievanceMechanism(String s) { this.humanRightsGrievanceMechanism = s; }

    //princple 5 essential 6
    public String getCompWagesPendingPrev() {
        return compWagesPendingPrev;
    }

    public void setCompWagesPendingPrev(String compWagesPendingPrev) {
        this.compWagesPendingPrev = compWagesPendingPrev;
    }

    public String getCompShFiledCurr() {
        return compShFiledCurr;
    }

    public void setCompShFiledCurr(String compShFiledCurr) {
        this.compShFiledCurr = compShFiledCurr;
    }

    public String getCompShPendingCurr() {
        return compShPendingCurr;
    }

    public void setCompShPendingCurr(String compShPendingCurr) {
        this.compShPendingCurr = compShPendingCurr;
    }

    public String getCompShRemarksCurr() {
        return compShRemarksCurr;
    }

    public void setCompShRemarksCurr(String compShRemarksCurr) {
        this.compShRemarksCurr = compShRemarksCurr;
    }

    public String getCompShFiledPrev() {
        return compShFiledPrev;
    }

    public void setCompShFiledPrev(String compShFiledPrev) {
        this.compShFiledPrev = compShFiledPrev;
    }

    public String getCompShPendingPrev() {
        return compShPendingPrev;
    }

    public void setCompShPendingPrev(String compShPendingPrev) {
        this.compShPendingPrev = compShPendingPrev;
    }

    public String getCompShRemarksPrev() {
        return compShRemarksPrev;
    }

    public void setCompShRemarksPrev(String compShRemarksPrev) {
        this.compShRemarksPrev = compShRemarksPrev;
    }

    public String getCompDiscrimFiledCurr() {
        return compDiscrimFiledCurr;
    }

    public void setCompDiscrimFiledCurr(String compDiscrimFiledCurr) {
        this.compDiscrimFiledCurr = compDiscrimFiledCurr;
    }

    public String getCompDiscrimPendingCurr() {
        return compDiscrimPendingCurr;
    }

    public void setCompDiscrimPendingCurr(String compDiscrimPendingCurr) {
        this.compDiscrimPendingCurr = compDiscrimPendingCurr;
    }

    public String getCompDiscrimRemarksCurr() {
        return compDiscrimRemarksCurr;
    }

    public void setCompDiscrimRemarksCurr(String compDiscrimRemarksCurr) {
        this.compDiscrimRemarksCurr = compDiscrimRemarksCurr;
    }

    public String getCompDiscrimFiledPrev() {
        return compDiscrimFiledPrev;
    }

    public void setCompDiscrimFiledPrev(String compDiscrimFiledPrev) {
        this.compDiscrimFiledPrev = compDiscrimFiledPrev;
    }

    public String getCompDiscrimPendingPrev() {
        return compDiscrimPendingPrev;
    }

    public void setCompDiscrimPendingPrev(String compDiscrimPendingPrev) {
        this.compDiscrimPendingPrev = compDiscrimPendingPrev;
    }

    public String getCompDiscrimRemarksPrev() {
        return compDiscrimRemarksPrev;
    }

    public void setCompDiscrimRemarksPrev(String compDiscrimRemarksPrev) {
        this.compDiscrimRemarksPrev = compDiscrimRemarksPrev;
    }

    public String getCompChildFiledCurr() {
        return compChildFiledCurr;
    }

    public void setCompChildFiledCurr(String compChildFiledCurr) {
        this.compChildFiledCurr = compChildFiledCurr;
    }

    public String getCompChildPendingCurr() {
        return compChildPendingCurr;
    }

    public void setCompChildPendingCurr(String compChildPendingCurr) {
        this.compChildPendingCurr = compChildPendingCurr;
    }

    public String getCompChildRemarksCurr() {
        return compChildRemarksCurr;
    }

    public void setCompChildRemarksCurr(String compChildRemarksCurr) {
        this.compChildRemarksCurr = compChildRemarksCurr;
    }

    public String getCompChildFiledPrev() {
        return compChildFiledPrev;
    }

    public void setCompChildFiledPrev(String compChildFiledPrev) {
        this.compChildFiledPrev = compChildFiledPrev;
    }

    public String getCompChildPendingPrev() {
        return compChildPendingPrev;
    }

    public void setCompChildPendingPrev(String compChildPendingPrev) {
        this.compChildPendingPrev = compChildPendingPrev;
    }

    public String getCompChildRemarksPrev() {
        return compChildRemarksPrev;
    }

    public void setCompChildRemarksPrev(String compChildRemarksPrev) {
        this.compChildRemarksPrev = compChildRemarksPrev;
    }

    public String getCompForcedFiledCurr() {
        return compForcedFiledCurr;
    }

    public void setCompForcedFiledCurr(String compForcedFiledCurr) {
        this.compForcedFiledCurr = compForcedFiledCurr;
    }

    public String getCompForcedPendingCurr() {
        return compForcedPendingCurr;
    }

    public void setCompForcedPendingCurr(String compForcedPendingCurr) {
        this.compForcedPendingCurr = compForcedPendingCurr;
    }

    public String getCompForcedRemarksCurr() {
        return compForcedRemarksCurr;
    }

    public void setCompForcedRemarksCurr(String compForcedRemarksCurr) {
        this.compForcedRemarksCurr = compForcedRemarksCurr;
    }

    public String getCompForcedFiledPrev() {
        return compForcedFiledPrev;
    }

    public void setCompForcedFiledPrev(String compForcedFiledPrev) {
        this.compForcedFiledPrev = compForcedFiledPrev;
    }

    public String getCompForcedPendingPrev() {
        return compForcedPendingPrev;
    }

    public void setCompForcedPendingPrev(String compForcedPendingPrev) {
        this.compForcedPendingPrev = compForcedPendingPrev;
    }

    public String getCompForcedRemarksPrev() {
        return compForcedRemarksPrev;
    }

    public void setCompForcedRemarksPrev(String compForcedRemarksPrev) {
        this.compForcedRemarksPrev = compForcedRemarksPrev;
    }

    public String getCompWagesFiledCurr() {
        return compWagesFiledCurr;
    }

    public void setCompWagesFiledCurr(String compWagesFiledCurr) {
        this.compWagesFiledCurr = compWagesFiledCurr;
    }

    public String getCompWagesPendingCurr() {
        return compWagesPendingCurr;
    }

    public void setCompWagesPendingCurr(String compWagesPendingCurr) {
        this.compWagesPendingCurr = compWagesPendingCurr;
    }

    public String getCompWagesRemarksCurr() {
        return compWagesRemarksCurr;
    }

    public void setCompWagesRemarksCurr(String compWagesRemarksCurr) {
        this.compWagesRemarksCurr = compWagesRemarksCurr;
    }

    public String getCompWagesFiledPrev() {
        return compWagesFiledPrev;
    }

    public void setCompWagesFiledPrev(String compWagesFiledPrev) {
        this.compWagesFiledPrev = compWagesFiledPrev;
    }

    public String getCompWagesRemarksPrev() {
        return compWagesRemarksPrev;
    }

    public void setCompWagesRemarksPrev(String compWagesRemarksPrev) {
        this.compWagesRemarksPrev = compWagesRemarksPrev;
    }

    public String getCompOtherHrFiledCurr() {
        return compOtherHrFiledCurr;
    }

    public void setCompOtherHrFiledCurr(String compOtherHrFiledCurr) {
        this.compOtherHrFiledCurr = compOtherHrFiledCurr;
    }

    public String getCompOtherHrPendingCurr() {
        return compOtherHrPendingCurr;
    }

    public void setCompOtherHrPendingCurr(String compOtherHrPendingCurr) {
        this.compOtherHrPendingCurr = compOtherHrPendingCurr;
    }

    public String getCompOtherHrRemarksCurr() {
        return compOtherHrRemarksCurr;
    }

    public void setCompOtherHrRemarksCurr(String compOtherHrRemarksCurr) {
        this.compOtherHrRemarksCurr = compOtherHrRemarksCurr;
    }

    public String getCompOtherHrFiledPrev() {
        return compOtherHrFiledPrev;
    }

    public void setCompOtherHrFiledPrev(String compOtherHrFiledPrev) {
        this.compOtherHrFiledPrev = compOtherHrFiledPrev;
    }

    public String getCompOtherHrPendingPrev() {
        return compOtherHrPendingPrev;
    }

    public void setCompOtherHrPendingPrev(String compOtherHrPendingPrev) {
        this.compOtherHrPendingPrev = compOtherHrPendingPrev;
    }

    public String getCompOtherHrRemarksPrev() {
        return compOtherHrRemarksPrev;
    }

    public void setCompOtherHrRemarksPrev(String compOtherHrRemarksPrev) {
        this.compOtherHrRemarksPrev = compOtherHrRemarksPrev;
    }

    private String compOtherHrRemarksPrev;

    //principle 5 essential 7
    public String getPoshTotalCurr() { return poshTotalCurr; } public void setPoshTotalCurr(String s) { this.poshTotalCurr = s; }
    public String getPoshTotalPrev() { return poshTotalPrev; } public void setPoshTotalPrev(String s) { this.poshTotalPrev = s; }
    public String getPoshPercCurr() { return poshPercCurr; } public void setPoshPercCurr(String s) { this.poshPercCurr = s; }
    public String getPoshPercPrev() { return poshPercPrev; } public void setPoshPercPrev(String s) { this.poshPercPrev = s; }
    public String getPoshUpheldCurr() { return poshUpheldCurr; } public void setPoshUpheldCurr(String s) { this.poshUpheldCurr = s; }
    public String getPoshUpheldPrev() { return poshUpheldPrev; } public void setPoshUpheldPrev(String s) { this.poshUpheldPrev = s; }
    public String getPoshNote() { return poshNote; } public void setPoshNote(String s) { this.poshNote = s; }

    //princple 5 essential 8
    public String getProtectionMechanismsIntro() { return protectionMechanismsIntro; }
    public void setProtectionMechanismsIntro(String s) { this.protectionMechanismsIntro = s; }

    public List<String> getProtectionMechanismsList() { return protectionMechanismsList; }
    public void setProtectionMechanismsList(List<String> l) { this.protectionMechanismsList = l; }

    //princple 5 essential 9
    public String getHumanRightsContracts() { return humanRightsContracts; }
    public void setHumanRightsContracts(String s) { this.humanRightsContracts = s; }

    public String getHumanRightsContractsDetails() { return humanRightsContractsDetails; }
    public void setHumanRightsContractsDetails(String s) { this.humanRightsContractsDetails = s; }

    //princple 5 essential 10
    public String getAssessChildLabourPerc() { return assessChildLabourPerc; } public void setAssessChildLabourPerc(String s) { this.assessChildLabourPerc = s; }
    public String getAssessForcedLabourPerc() { return assessForcedLabourPerc; } public void setAssessForcedLabourPerc(String s) { this.assessForcedLabourPerc = s; }
    public String getAssessSexualHarassmentPerc() { return assessSexualHarassmentPerc; } public void setAssessSexualHarassmentPerc(String s) { this.assessSexualHarassmentPerc = s; }
    public String getAssessDiscriminationPerc() { return assessDiscriminationPerc; } public void setAssessDiscriminationPerc(String s) { this.assessDiscriminationPerc = s; }
    public String getAssessWagesPerc() { return assessWagesPerc; } public void setAssessWagesPerc(String s) { this.assessWagesPerc = s; }
    public String getAssessOthersPerc() { return assessOthersPerc; } public void setAssessOthersPerc(String s) { this.assessOthersPerc = s; }

    public String getAssessmentsP5Note() { return assessmentsP5Note; } public void setAssessmentsP5Note(String s) { this.assessmentsP5Note = s; }

    //princple 5 essential 11
    public String getAssessCorrectiveIntro() { return assessCorrectiveIntro; }
    public void setAssessCorrectiveIntro(String s) { this.assessCorrectiveIntro = s; }

    public List<String> getAssessCorrectiveActions() { return assessCorrectiveActions; }
    public void setAssessCorrectiveActions(List<String> l) {
        this.assessCorrectiveActions = l;
    }

    //princple 5 leadership 1 and 2
    public String getP5LeadProcessModIntro() { return p5LeadProcessModIntro; }
    public void setP5LeadProcessModIntro(String s) { this.p5LeadProcessModIntro = s; }

    public List<String> getP5LeadProcessModList() { return p5LeadProcessModList; }
    public void setP5LeadProcessModList(List<String> l) { this.p5LeadProcessModList = l; }

    public String getP5LeadDueDiligenceScope() { return p5LeadDueDiligenceScope; }
    public void setP5LeadDueDiligenceScope(String s) { this.p5LeadDueDiligenceScope = s; }

    public List<String> getP5LeadDueDiligenceIssues() { return p5LeadDueDiligenceIssues; }
    public void setP5LeadDueDiligenceIssues(List<String> l) { this.p5LeadDueDiligenceIssues = l; }

    public List<String> getP5LeadDueDiligenceHolders() { return p5LeadDueDiligenceHolders; }
    public void setP5LeadDueDiligenceHolders(List<String> l) { this.p5LeadDueDiligenceHolders = l; }

    //princple 5 leadership 3
    public String getP5LeadPremisesAccess() { return p5LeadPremisesAccess; }
    public void setP5LeadPremisesAccess(String s) { this.p5LeadPremisesAccess = s; }

    //princple 5 leadership 4
    public String getP5LeadValueChainAssessment() { return p5LeadValueChainAssessment; }
    public void setP5LeadValueChainAssessment(String s) { this.p5LeadValueChainAssessment = s; }

    //princple 5 leadership 5
    public String getP5LeadValueChainCorrectiveActions() { return p5LeadValueChainCorrectiveActions; }
    public void setP5LeadValueChainCorrectiveActions(String s) { this.p5LeadValueChainCorrectiveActions = s; }

    // --- GETTERS AND SETTERS FOR ALL NEW FIELDS --- princple 6,7,8 and 9
    // (Generate standard getters/setters for all String fields and Lists above)

    public String getP6ElecConsumCurr() { return p6ElecConsumCurr; } public void setP6ElecConsumCurr(String s) { this.p6ElecConsumCurr = s; }
    public String getP6ElecConsumPrev() { return p6ElecConsumPrev; } public void setP6ElecConsumPrev(String s) { this.p6ElecConsumPrev = s; }
    public String getP6FuelConsumCurr() { return p6FuelConsumCurr; } public void setP6FuelConsumCurr(String s) { this.p6FuelConsumCurr = s; }
    public String getP6FuelConsumPrev() { return p6FuelConsumPrev; } public void setP6FuelConsumPrev(String s) { this.p6FuelConsumPrev = s; }
    public String getP6EnergyOtherCurr() { return p6EnergyOtherCurr; } public void setP6EnergyOtherCurr(String s) { this.p6EnergyOtherCurr = s; }
    public String getP6EnergyOtherPrev() { return p6EnergyOtherPrev; } public void setP6EnergyOtherPrev(String s) { this.p6EnergyOtherPrev = s; }
    public String getP6EnergyTotalCurr() { return p6EnergyTotalCurr; } public void setP6EnergyTotalCurr(String s) { this.p6EnergyTotalCurr = s; }
    public String getP6EnergyTotalPrev() { return p6EnergyTotalPrev; } public void setP6EnergyTotalPrev(String s) { this.p6EnergyTotalPrev = s; }
    public String getP6EnergyIntensityCurr() { return p6EnergyIntensityCurr; } public void setP6EnergyIntensityCurr(String s) { this.p6EnergyIntensityCurr = s; }
    public String getP6EnergyIntensityPrev() { return p6EnergyIntensityPrev; } public void setP6EnergyIntensityPrev(String s) { this.p6EnergyIntensityPrev = s; }
    public String getP6EnergyNote() { return p6EnergyNote; } public void setP6EnergyNote(String s) { this.p6EnergyNote = s; }

    public String getP6Scope1Curr() { return p6Scope1Curr; } public void setP6Scope1Curr(String s) { this.p6Scope1Curr = s; }
    public String getP6Scope1Prev() { return p6Scope1Prev; } public void setP6Scope1Prev(String s) { this.p6Scope1Prev = s; }
    public String getP6Scope2Curr() { return p6Scope2Curr; } public void setP6Scope2Curr(String s) { this.p6Scope2Curr = s; }
    public String getP6Scope2Prev() { return p6Scope2Prev; } public void setP6Scope2Prev(String s) { this.p6Scope2Prev = s; }
    public String getP6ScopeTotalCurr() { return p6ScopeTotalCurr; } public void setP6ScopeTotalCurr(String s) { this.p6ScopeTotalCurr = s; }
    public String getP6ScopeTotalPrev() { return p6ScopeTotalPrev; } public void setP6ScopeTotalPrev(String s) { this.p6ScopeTotalPrev = s; }
    public String getP6EmissionIntensityCurr() { return p6EmissionIntensityCurr; } public void setP6EmissionIntensityCurr(String s) { this.p6EmissionIntensityCurr = s; }
    public String getP6EmissionIntensityPrev() { return p6EmissionIntensityPrev; } public void setP6EmissionIntensityPrev(String s) { this.p6EmissionIntensityPrev = s; }
    public String getP6EmissionNote() { return p6EmissionNote; } public void setP6EmissionNote(String s) { this.p6EmissionNote = s; }

    public String getP6WaterWithdrawalCurr() { return p6WaterWithdrawalCurr; } public void setP6WaterWithdrawalCurr(String s) { this.p6WaterWithdrawalCurr = s; }
    public String getP6WaterWithdrawalPrev() { return p6WaterWithdrawalPrev; } public void setP6WaterWithdrawalPrev(String s) { this.p6WaterWithdrawalPrev = s; }
    public String getP6WaterConsumCurr() { return p6WaterConsumCurr; } public void setP6WaterConsumCurr(String s) { this.p6WaterConsumCurr = s; }
    public String getP6WaterConsumPrev() { return p6WaterConsumPrev; } public void setP6WaterConsumPrev(String s) { this.p6WaterConsumPrev = s; }
    public String getP6WaterIntensityCurr() { return p6WaterIntensityCurr; } public void setP6WaterIntensityCurr(String s) { this.p6WaterIntensityCurr = s; }
    public String getP6WaterIntensityPrev() { return p6WaterIntensityPrev; } public void setP6WaterIntensityPrev(String s) { this.p6WaterIntensityPrev = s; }
    public String getP6WaterNote() { return p6WaterNote; } public void setP6WaterNote(String s) { this.p6WaterNote = s; }

    public List<WasteManagementRow> getP6WasteManagementList() { return p6WasteManagementList; } public void setP6WasteManagementList(List<WasteManagementRow> l) { this.p6WasteManagementList = l; }
    public String getP6WasteNote() { return p6WasteNote; } public void setP6WasteNote(String s) { this.p6WasteNote = s; }

    public String getP7AffiliationsCount() { return p7AffiliationsCount; } public void setP7AffiliationsCount(String s) { this.p7AffiliationsCount = s; }
    public List<TradeAssociation> getP7TradeAssociations() { return p7TradeAssociations; } public void setP7TradeAssociations(List<TradeAssociation> l) { this.p7TradeAssociations = l; }
    public String getP7AntiCompetitiveDetails() { return p7AntiCompetitiveDetails; } public void setP7AntiCompetitiveDetails(String s) { this.p7AntiCompetitiveDetails = s; }
    public List<PublicPolicyPosition> getP7PublicPolicyPositions() { return p7PublicPolicyPositions; } public void setP7PublicPolicyPositions(List<PublicPolicyPosition> l) { this.p7PublicPolicyPositions = l; }

    public String getP8SiaDetails() { return p8SiaDetails; } public void setP8SiaDetails(String s) { this.p8SiaDetails = s; }
    public List<RandRProject> getP8RandRProjects() { return p8RandRProjects; } public void setP8RandRProjects(List<RandRProject> l) { this.p8RandRProjects = l; }
    public String getP8GrievanceMechanism() { return p8GrievanceMechanism; } public void setP8GrievanceMechanism(String s) { this.p8GrievanceMechanism = s; }
    public String getP8InputMsmeCurr() { return p8InputMsmeCurr; } public void setP8InputMsmeCurr(String s) { this.p8InputMsmeCurr = s; }
    public String getP8InputMsmePrev() { return p8InputMsmePrev; } public void setP8InputMsmePrev(String s) { this.p8InputMsmePrev = s; }
    public String getP8InputIndiaCurr() { return p8InputIndiaCurr; } public void setP8InputIndiaCurr(String s) { this.p8InputIndiaCurr = s; }
    public String getP8InputIndiaPrev() { return p8InputIndiaPrev; } public void setP8InputIndiaPrev(String s) { this.p8InputIndiaPrev = s; }
    public List<CsrAspirationalDistrict> getP8AspirationalDistricts() { return p8AspirationalDistricts; } public void setP8AspirationalDistricts(List<CsrAspirationalDistrict> l) { this.p8AspirationalDistricts = l; }
    public String getP8PreferentialProcurement() { return p8PreferentialProcurement; } public void setP8PreferentialProcurement(String s) { this.p8PreferentialProcurement = s; }
    public String getP8MarginalizedGroups() { return p8MarginalizedGroups; } public void setP8MarginalizedGroups(String s) { this.p8MarginalizedGroups = s; }
    public String getP8ProcurementPercentage() { return p8ProcurementPercentage; } public void setP8ProcurementPercentage(String s) { this.p8ProcurementPercentage = s; }
    public String getP8IpBenefits() { return p8IpBenefits; } public void setP8IpBenefits(String s) { this.p8IpBenefits = s; }
    public List<CsrBeneficiary> getP8CsrBeneficiaries() { return p8CsrBeneficiaries; } public void setP8CsrBeneficiaries(List<CsrBeneficiary> l) { this.p8CsrBeneficiaries = l; }

    public String getP9ConsumerComplaintMech() { return p9ConsumerComplaintMech; } public void setP9ConsumerComplaintMech(String s) { this.p9ConsumerComplaintMech = s; }
    public String getP9DataPrivacyCurr() { return p9DataPrivacyCurr; } public void setP9DataPrivacyCurr(String s) { this.p9DataPrivacyCurr = s; }
    public String getP9DataPrivacyPrev() { return p9DataPrivacyPrev; } public void setP9DataPrivacyPrev(String s) { this.p9DataPrivacyPrev = s; }
    public String getP9AdvertisingCurr() { return p9AdvertisingCurr; } public void setP9AdvertisingCurr(String s) { this.p9AdvertisingCurr = s; }
    public String getP9AdvertisingPrev() { return p9AdvertisingPrev; } public void setP9AdvertisingPrev(String s) { this.p9AdvertisingPrev = s; }
    public String getP9CyberCurr() { return p9CyberCurr; } public void setP9CyberCurr(String s) { this.p9CyberCurr = s; }
    public String getP9CyberPrev() { return p9CyberPrev; } public void setP9CyberPrev(String s) { this.p9CyberPrev = s; }
    public String getP9DeliveryCurr() { return p9DeliveryCurr; } public void setP9DeliveryCurr(String s) { this.p9DeliveryCurr = s; }
    public String getP9DeliveryPrev() { return p9DeliveryPrev; } public void setP9DeliveryPrev(String s) { this.p9DeliveryPrev = s; }
    public String getP9RestrictiveCurr() { return p9RestrictiveCurr; } public void setP9RestrictiveCurr(String s) { this.p9RestrictiveCurr = s; }
    public String getP9RestrictivePrev() { return p9RestrictivePrev; } public void setP9RestrictivePrev(String s) { this.p9RestrictivePrev = s; }
    public String getP9UnfairCurr() { return p9UnfairCurr; } public void setP9UnfairCurr(String s) { this.p9UnfairCurr = s; }
    public String getP9UnfairPrev() { return p9UnfairPrev; } public void setP9UnfairPrev(String s) { this.p9UnfairPrev = s; }
    public String getP9OtherCurr() { return p9OtherCurr; } public void setP9OtherCurr(String s) { this.p9OtherCurr = s; }
    public String getP9OtherPrev() { return p9OtherPrev; } public void setP9OtherPrev(String s) { this.p9OtherPrev = s; }
    public String getP9RecallVoluntary() { return p9RecallVoluntary; } public void setP9RecallVoluntary(String s) { this.p9RecallVoluntary = s; }
    public String getP9RecallForced() { return p9RecallForced; } public void setP9RecallForced(String s) { this.p9RecallForced = s; }
    public String getP9CyberSecurityPolicy() { return p9CyberSecurityPolicy; } public void setP9CyberSecurityPolicy(String s) { this.p9CyberSecurityPolicy = s; }
    public String getP9CorrectiveActions() { return p9CorrectiveActions; } public void setP9CorrectiveActions(String s) { this.p9CorrectiveActions = s; }
    public String getP9InfoChannels() { return p9InfoChannels; } public void setP9InfoChannels(String s) { this.p9InfoChannels = s; }
    public String getP9SafeUsageSteps() { return p9SafeUsageSteps; } public void setP9SafeUsageSteps(String s) { this.p9SafeUsageSteps = s; }
    public String getP9DisruptionInfo() { return p9DisruptionInfo; } public void setP9DisruptionInfo(String s) { this.p9DisruptionInfo = s; }
    public String getP9ProductInfoDisplay() { return p9ProductInfoDisplay; } public void setP9ProductInfoDisplay(String s) { this.p9ProductInfoDisplay = s; }
    public String getP9CustomerSatSurvey() { return p9CustomerSatSurvey; } public void setP9CustomerSatSurvey(String s) { this.p9CustomerSatSurvey = s; }
}