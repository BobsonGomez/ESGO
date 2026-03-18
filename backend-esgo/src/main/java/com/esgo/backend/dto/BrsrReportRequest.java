package com.esgo.backend.dto;
import lombok.Data;

import java.util.List;
import java.util.Map;

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

    public String getAssuranceProviderName() {
        return assuranceProviderName;
    }

    public void setAssuranceProviderName(String assuranceProviderName) {
        this.assuranceProviderName = assuranceProviderName;
    }

    public String getAssuranceType() {
        return assuranceType;
    }

    public void setAssuranceType(String assuranceType) {
        this.assuranceType = assuranceType;
    }

    private String assuranceProviderName;
    private String assuranceType;

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
    // === SECTION B DATA ===

    // Q1 - Q3: Unified Matrix Rows
    private PrincipleBooleans q1a;
    private PrincipleBooleans q1b;
    private String q1WebLink;
    private PrincipleBooleans q2;
    private PrincipleBooleans q3;

    // Q4: National/International Standards Matrix
    private List<StandardMapping> q4Standards;

    // Q5 & Q6: Commitments & Performance
    private String q5Commitments;
    private String q6Performance;

    // Q7 & Q8: Governance
    private String governanceStatement;
    private String oversightAuthority;

    // Q9 - Q11: Committees & Assessment
    private String q9Committee;
    private String q10PerformanceReview;
    private String q10ComplianceReview;
    private String q11Assessment;

    // Q12: Reasons for "No" (Matrix)
    private List<ReasonMapping> q12Reasons;

    // --- GETTERS & SETTERS ---
    public PrincipleBooleans getQ1a() { return q1a; } public void setQ1a(PrincipleBooleans q1a) { this.q1a = q1a; }
    public PrincipleBooleans getQ1b() { return q1b; } public void setQ1b(PrincipleBooleans q1b) { this.q1b = q1b; }
    public String getQ1WebLink() { return q1WebLink; } public void setQ1WebLink(String q1WebLink) { this.q1WebLink = q1WebLink; }
    public PrincipleBooleans getQ2() { return q2; } public void setQ2(PrincipleBooleans q2) { this.q2 = q2; }
    public PrincipleBooleans getQ3() { return q3; } public void setQ3(PrincipleBooleans q3) { this.q3 = q3; }
    public List<StandardMapping> getQ4Standards() { return q4Standards; } public void setQ4Standards(List<StandardMapping> q4Standards) { this.q4Standards = q4Standards; }
    public String getQ5Commitments() { return q5Commitments; } public void setQ5Commitments(String q5Commitments) { this.q5Commitments = q5Commitments; }
    public String getQ6Performance() { return q6Performance; } public void setQ6Performance(String q6Performance) { this.q6Performance = q6Performance; }
    public String getGovernanceStatement() { return governanceStatement; } public void setGovernanceStatement(String governanceStatement) { this. governanceStatement = governanceStatement; }
    public String getOversightAuthority() { return oversightAuthority; } public void setOversightAuthority(String oversightAuthority) { this.oversightAuthority = oversightAuthority; }
    public String getQ9Committee() { return q9Committee; } public void setQ9Committee(String q9Committee) { this.q9Committee = q9Committee; }
    public String getQ10PerformanceReview() { return q10PerformanceReview; } public void setQ10PerformanceReview(String q10PerformanceReview) { this.q10PerformanceReview = q10PerformanceReview; }
    public String getQ10ComplianceReview() { return q10ComplianceReview; } public void setQ10ComplianceReview(String q10ComplianceReview) { this.q10ComplianceReview = q10ComplianceReview; }
    public String getQ11Assessment() { return q11Assessment; } public void setQ11Assessment(String q11Assessment) { this.q11Assessment = q11Assessment; }
    public List<ReasonMapping> getQ12Reasons() { return q12Reasons; } public void setQ12Reasons(List<ReasonMapping> q12Reasons) { this.q12Reasons = q12Reasons; }

    // --- INNER CLASSES ---

    // Reusable class for rows 1a, 1b, 2, 3
    public static class PrincipleBooleans {
        private boolean p1, p2, p3, p4, p5, p6, p7, p8, p9;
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

    // For Q4
    public static class StandardMapping {
        private String name;
        private boolean p1, p2, p3, p4, p5, p6, p7, p8, p9;
        public String getName() { return name; } public void setName(String name) { this.name = name; }
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

    // For Q12
    public static class ReasonMapping {
        private String questionText;
        private boolean p1, p2, p3, p4, p5, p6, p7, p8, p9;
        public String getQuestionText() { return questionText; } public void setQuestionText(String questionText) { this.questionText = questionText; }
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

    //public String getGovernanceStatement() { return governanceStatement; } public void setGovernanceStatement(String s) { this.governanceStatement = s; }
    //public String getOversightAuthority() { return oversightAuthority; } public void setOversightAuthority(String s) { this.oversightAuthority = s; }

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

    // --- Principle 1: Q8 Accounts Payable ---
    private String accountsPayableCurr;
    private String accountsPayablePrev;

    // --- Principle 1: Q9 Openness of Business ---
    private String purTradingPercCurr;
    private String purTradingPercPrev;
    private String purTradingNumCurr;

    public String getPurTradingNumPrev() {
        return purTradingNumPrev;
    }

    public void setPurTradingNumPrev(String purTradingNumPrev) {
        this.purTradingNumPrev = purTradingNumPrev;
    }

    public String getAccountsPayableCurr() {
        return accountsPayableCurr;
    }

    public void setAccountsPayableCurr(String accountsPayableCurr) {
        this.accountsPayableCurr = accountsPayableCurr;
    }

    public String getAccountsPayablePrev() {
        return accountsPayablePrev;
    }

    public void setAccountsPayablePrev(String accountsPayablePrev) {
        this.accountsPayablePrev = accountsPayablePrev;
    }

    public String getPurTradingPercCurr() {
        return purTradingPercCurr;
    }

    public void setPurTradingPercCurr(String purTradingPercCurr) {
        this.purTradingPercCurr = purTradingPercCurr;
    }

    public String getPurTradingPercPrev() {
        return purTradingPercPrev;
    }

    public void setPurTradingPercPrev(String purTradingPercPrev) {
        this.purTradingPercPrev = purTradingPercPrev;
    }

    public String getPurTradingNumCurr() {
        return purTradingNumCurr;
    }

    public void setPurTradingNumCurr(String purTradingNumCurr) {
        this.purTradingNumCurr = purTradingNumCurr;
    }

    public String getPurTop10PercCurr() {
        return purTop10PercCurr;
    }

    public void setPurTop10PercCurr(String purTop10PercCurr) {
        this.purTop10PercCurr = purTop10PercCurr;
    }

    public String getPurTop10PercPrev() {
        return purTop10PercPrev;
    }

    public void setPurTop10PercPrev(String purTop10PercPrev) {
        this.purTop10PercPrev = purTop10PercPrev;
    }

    public String getSalesDealerPercCurr() {
        return salesDealerPercCurr;
    }

    public void setSalesDealerPercCurr(String salesDealerPercCurr) {
        this.salesDealerPercCurr = salesDealerPercCurr;
    }

    public String getSalesDealerPercPrev() {
        return salesDealerPercPrev;
    }

    public void setSalesDealerPercPrev(String salesDealerPercPrev) {
        this.salesDealerPercPrev = salesDealerPercPrev;
    }

    public String getSalesDealerNumCurr() {
        return salesDealerNumCurr;
    }

    public void setSalesDealerNumCurr(String salesDealerNumCurr) {
        this.salesDealerNumCurr = salesDealerNumCurr;
    }

    public String getSalesDealerNumPrev() {
        return salesDealerNumPrev;
    }

    public void setSalesDealerNumPrev(String salesDealerNumPrev) {
        this.salesDealerNumPrev = salesDealerNumPrev;
    }

    public String getSalesTop10PercCurr() {
        return salesTop10PercCurr;
    }

    public void setSalesTop10PercCurr(String salesTop10PercCurr) {
        this.salesTop10PercCurr = salesTop10PercCurr;
    }

    public String getSalesTop10PercPrev() {
        return salesTop10PercPrev;
    }

    public void setSalesTop10PercPrev(String salesTop10PercPrev) {
        this.salesTop10PercPrev = salesTop10PercPrev;
    }

    public String getRptPurCurr() {
        return rptPurCurr;
    }

    public void setRptPurCurr(String rptPurCurr) {
        this.rptPurCurr = rptPurCurr;
    }

    public String getRptPurPrev() {
        return rptPurPrev;
    }

    public void setRptPurPrev(String rptPurPrev) {
        this.rptPurPrev = rptPurPrev;
    }

    public String getRptSalesCurr() {
        return rptSalesCurr;
    }

    public void setRptSalesCurr(String rptSalesCurr) {
        this.rptSalesCurr = rptSalesCurr;
    }

    public String getRptSalesPrev() {
        return rptSalesPrev;
    }

    public void setRptSalesPrev(String rptSalesPrev) {
        this.rptSalesPrev = rptSalesPrev;
    }

    public String getRptLoansCurr() {
        return rptLoansCurr;
    }

    public void setRptLoansCurr(String rptLoansCurr) {
        this.rptLoansCurr = rptLoansCurr;
    }

    public String getRptLoansPrev() {
        return rptLoansPrev;
    }

    public void setRptLoansPrev(String rptLoansPrev) {
        this.rptLoansPrev = rptLoansPrev;
    }

    public String getRptInvestCurr() {
        return rptInvestCurr;
    }

    public void setRptInvestCurr(String rptInvestCurr) {
        this.rptInvestCurr = rptInvestCurr;
    }

    public String getRptInvestPrev() {
        return rptInvestPrev;
    }

    public void setRptInvestPrev(String rptInvestPrev) {
        this.rptInvestPrev = rptInvestPrev;
    }

    public String getOpennessNote() {
        return opennessNote;
    }

    public void setOpennessNote(String opennessNote) {
        this.opennessNote = opennessNote;
    }

    private String purTradingNumPrev;
    private String purTop10PercCurr;
    private String purTop10PercPrev;

    private String salesDealerPercCurr;
    private String salesDealerPercPrev;
    private String salesDealerNumCurr;
    private String salesDealerNumPrev;
    private String salesTop10PercCurr;
    private String salesTop10PercPrev;

    private String rptPurCurr;
    private String rptPurPrev;
    private String rptSalesCurr;
    private String rptSalesPrev;
    private String rptLoansCurr;
    private String rptLoansPrev;
    private String rptInvestCurr;
    private String rptInvestPrev;

    private String opennessNote;

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

    // --- Principle 2 Leadership NA Flags ---
    private boolean p2Lead1NA;
    private boolean p2Lead2NA;
    private boolean p2Lead3NA;
    private boolean p2Lead4NA;
    private boolean p2Lead5NA;

    public boolean isP2Lead4NA() {
        return p2Lead4NA;
    }

    public void setP2Lead4NA(boolean p2Lead4NA) {
        this.p2Lead4NA = p2Lead4NA;
    }

    public boolean isP2Lead1NA() {
        return p2Lead1NA;
    }

    public void setP2Lead1NA(boolean p2Lead1NA) {
        this.p2Lead1NA = p2Lead1NA;
    }

    public boolean isP2Lead2NA() {
        return p2Lead2NA;
    }

    public void setP2Lead2NA(boolean p2Lead2NA) {
        this.p2Lead2NA = p2Lead2NA;
    }

    public boolean isP2Lead3NA() {
        return p2Lead3NA;
    }

    public void setP2Lead3NA(boolean p2Lead3NA) {
        this.p2Lead3NA = p2Lead3NA;
    }

    public boolean isP2Lead5NA() {
        return p2Lead5NA;
    }

    public void setP2Lead5NA(boolean p2Lead5NA) {
        this.p2Lead5NA = p2Lead5NA;
    }

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

    // Principle 3 - Q1 Additions
    private String empWellBeingNote;
    private String workWellBeingNote;
    private String wbCostCurr;

    public String getWbCostPrev() {
        return wbCostPrev;
    }

    public void setWbCostPrev(String wbCostPrev) {
        this.wbCostPrev = wbCostPrev;
    }

    public String getEmpWellBeingNote() {
        return empWellBeingNote;
    }

    public void setEmpWellBeingNote(String empWellBeingNote) {
        this.empWellBeingNote = empWellBeingNote;
    }

    public String getWorkWellBeingNote() {
        return workWellBeingNote;
    }

    public void setWorkWellBeingNote(String workWellBeingNote) {
        this.workWellBeingNote = workWellBeingNote;
    }

    public String getWbCostCurr() {
        return wbCostCurr;
    }

    public void setWbCostCurr(String wbCostCurr) {
        this.wbCostCurr = wbCostCurr;
    }

    public String getWbCostNote() {
        return wbCostNote;
    }

    public void setWbCostNote(String wbCostNote) {
        this.wbCostNote = wbCostNote;
    }

    private String wbCostPrev;
    private String wbCostNote;

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

    public String getTransitionAssistanceYN() {
        return transitionAssistanceYN;
    }

    public void setTransitionAssistanceYN(String transitionAssistanceYN) {
        this.transitionAssistanceYN = transitionAssistanceYN;
    }

    private String transitionAssistanceYN;

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

    public String getHrComplaintsNote() {
        return hrComplaintsNote;
    }

    public void setHrComplaintsNote(String hrComplaintsNote) {
        this.hrComplaintsNote = hrComplaintsNote;
    }

    private String hrComplaintsNote;

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

    public String getP5LeadVcAssessShPerc() {
        return p5LeadVcAssessShPerc;
    }

    public void setP5LeadVcAssessShPerc(String p5LeadVcAssessShPerc) {
        this.p5LeadVcAssessShPerc = p5LeadVcAssessShPerc;
    }

    public String getP5LeadVcAssessDiscrimPerc() {
        return p5LeadVcAssessDiscrimPerc;
    }

    public void setP5LeadVcAssessDiscrimPerc(String p5LeadVcAssessDiscrimPerc) {
        this.p5LeadVcAssessDiscrimPerc = p5LeadVcAssessDiscrimPerc;
    }

    public String getP5LeadVcAssessChildPerc() {
        return p5LeadVcAssessChildPerc;
    }

    public void setP5LeadVcAssessChildPerc(String p5LeadVcAssessChildPerc) {
        this.p5LeadVcAssessChildPerc = p5LeadVcAssessChildPerc;
    }

    public String getP5LeadVcAssessForcedPerc() {
        return p5LeadVcAssessForcedPerc;
    }

    public void setP5LeadVcAssessForcedPerc(String p5LeadVcAssessForcedPerc) {
        this.p5LeadVcAssessForcedPerc = p5LeadVcAssessForcedPerc;
    }

    public String getP5LeadVcAssessWagesPerc() {
        return p5LeadVcAssessWagesPerc;
    }

    public void setP5LeadVcAssessWagesPerc(String p5LeadVcAssessWagesPerc) {
        this.p5LeadVcAssessWagesPerc = p5LeadVcAssessWagesPerc;
    }

    public String getP5LeadVcAssessOthersPerc() {
        return p5LeadVcAssessOthersPerc;
    }

    public void setP5LeadVcAssessOthersPerc(String p5LeadVcAssessOthersPerc) {
        this.p5LeadVcAssessOthersPerc = p5LeadVcAssessOthersPerc;
    }

    // Principle 5 - Leadership Q4 (Value Chain Assessments)
    private String p5LeadVcAssessShPerc;
    private String p5LeadVcAssessDiscrimPerc;
    private String p5LeadVcAssessChildPerc;
    private String p5LeadVcAssessForcedPerc;
    private String p5LeadVcAssessWagesPerc;
    private String p5LeadVcAssessOthersPerc;

    // --- GETTERS AND SETTERS FOR ALL NEW FIELDS --- princple 6,7,8 and 9
    // (Generate standard getters/setters for all String fields and Lists above)

    // --- Principle 6 : Q1 Energy ---
    private String p6Q1RenElecCurr; private String p6Q1RenElecPrev;
    private String p6Q1RenFuelCurr; private String p6Q1RenFuelPrev;
    private String p6Q1RenOtherCurr; private String p6Q1RenOtherPrev;
    private String p6Q1RenTotalCurr; private String p6Q1RenTotalPrev;
    private String p6Q1NonRenElecCurr; private String p6Q1NonRenElecPrev;
    private String p6Q1NonRenFuelCurr; private String p6Q1NonRenFuelPrev;
    private String p6Q1NonRenOtherCurr; private String p6Q1NonRenOtherPrev;
    private String p6Q1NonRenTotalCurr; private String p6Q1NonRenTotalPrev;
    private String p6Q1GrandTotalCurr; private String p6Q1GrandTotalPrev;
    private String p6Q1IntTurnoverCurr; private String p6Q1IntTurnoverPrev;
    private String p6Q1IntPppCurr; private String p6Q1IntPppPrev;
    private String p6Q1IntPhysicalCurr; private String p6Q1IntPhysicalPrev;
    private String p6Q1IntOptCurr; private String p6Q1IntOptPrev;
    private String p6Q1AssuranceNote;

    // --- Principle 6 : Q2 PAT ---
    private String p6Q2PatDetails;

    // --- Principle 6 : Q3 Water ---
    private String p6Q3SurfaceCurr; private String p6Q3SurfacePrev;
    private String p6Q3GroundCurr; private String p6Q3GroundPrev;
    private String p6Q3ThirdPartyCurr; private String p6Q3ThirdPartyPrev;
    private String p6Q3SeaCurr; private String p6Q3SeaPrev;
    private String p6Q3OthersCurr; private String p6Q3OthersPrev;
    private String p6Q3TotalWithCurr; private String p6Q3TotalWithPrev;
    private String p6Q3TotalConsCurr; private String p6Q3TotalConsPrev;
    private String p6Q3IntTurnoverCurr; private String p6Q3IntTurnoverPrev;

    public String getP6Q3IntPppCurr() {
        return p6Q3IntPppCurr;
    }

    public void setP6Q3IntPppCurr(String p6Q3IntPppCurr) {
        this.p6Q3IntPppCurr = p6Q3IntPppCurr;
    }

    public String getP6Q1RenElecCurr() {
        return p6Q1RenElecCurr;
    }

    public void setP6Q1RenElecCurr(String p6Q1RenElecCurr) {
        this.p6Q1RenElecCurr = p6Q1RenElecCurr;
    }

    public String getP6Q1RenElecPrev() {
        return p6Q1RenElecPrev;
    }

    public void setP6Q1RenElecPrev(String p6Q1RenElecPrev) {
        this.p6Q1RenElecPrev = p6Q1RenElecPrev;
    }

    public String getP6Q1RenFuelCurr() {
        return p6Q1RenFuelCurr;
    }

    public void setP6Q1RenFuelCurr(String p6Q1RenFuelCurr) {
        this.p6Q1RenFuelCurr = p6Q1RenFuelCurr;
    }

    public String getP6Q1RenFuelPrev() {
        return p6Q1RenFuelPrev;
    }

    public void setP6Q1RenFuelPrev(String p6Q1RenFuelPrev) {
        this.p6Q1RenFuelPrev = p6Q1RenFuelPrev;
    }

    public String getP6Q1RenOtherCurr() {
        return p6Q1RenOtherCurr;
    }

    public void setP6Q1RenOtherCurr(String p6Q1RenOtherCurr) {
        this.p6Q1RenOtherCurr = p6Q1RenOtherCurr;
    }

    public String getP6Q1RenOtherPrev() {
        return p6Q1RenOtherPrev;
    }

    public void setP6Q1RenOtherPrev(String p6Q1RenOtherPrev) {
        this.p6Q1RenOtherPrev = p6Q1RenOtherPrev;
    }

    public String getP6Q1RenTotalCurr() {
        return p6Q1RenTotalCurr;
    }

    public void setP6Q1RenTotalCurr(String p6Q1RenTotalCurr) {
        this.p6Q1RenTotalCurr = p6Q1RenTotalCurr;
    }

    public String getP6Q1RenTotalPrev() {
        return p6Q1RenTotalPrev;
    }

    public void setP6Q1RenTotalPrev(String p6Q1RenTotalPrev) {
        this.p6Q1RenTotalPrev = p6Q1RenTotalPrev;
    }

    public String getP6Q1NonRenElecCurr() {
        return p6Q1NonRenElecCurr;
    }

    public void setP6Q1NonRenElecCurr(String p6Q1NonRenElecCurr) {
        this.p6Q1NonRenElecCurr = p6Q1NonRenElecCurr;
    }

    public String getP6Q1NonRenElecPrev() {
        return p6Q1NonRenElecPrev;
    }

    public void setP6Q1NonRenElecPrev(String p6Q1NonRenElecPrev) {
        this.p6Q1NonRenElecPrev = p6Q1NonRenElecPrev;
    }

    public String getP6Q1NonRenFuelCurr() {
        return p6Q1NonRenFuelCurr;
    }

    public void setP6Q1NonRenFuelCurr(String p6Q1NonRenFuelCurr) {
        this.p6Q1NonRenFuelCurr = p6Q1NonRenFuelCurr;
    }

    public String getP6Q1NonRenFuelPrev() {
        return p6Q1NonRenFuelPrev;
    }

    public void setP6Q1NonRenFuelPrev(String p6Q1NonRenFuelPrev) {
        this.p6Q1NonRenFuelPrev = p6Q1NonRenFuelPrev;
    }

    public String getP6Q1NonRenOtherCurr() {
        return p6Q1NonRenOtherCurr;
    }

    public void setP6Q1NonRenOtherCurr(String p6Q1NonRenOtherCurr) {
        this.p6Q1NonRenOtherCurr = p6Q1NonRenOtherCurr;
    }

    public String getP6Q1NonRenOtherPrev() {
        return p6Q1NonRenOtherPrev;
    }

    public void setP6Q1NonRenOtherPrev(String p6Q1NonRenOtherPrev) {
        this.p6Q1NonRenOtherPrev = p6Q1NonRenOtherPrev;
    }

    public String getP6Q1NonRenTotalCurr() {
        return p6Q1NonRenTotalCurr;
    }

    public void setP6Q1NonRenTotalCurr(String p6Q1NonRenTotalCurr) {
        this.p6Q1NonRenTotalCurr = p6Q1NonRenTotalCurr;
    }

    public String getP6Q1NonRenTotalPrev() {
        return p6Q1NonRenTotalPrev;
    }

    public void setP6Q1NonRenTotalPrev(String p6Q1NonRenTotalPrev) {
        this.p6Q1NonRenTotalPrev = p6Q1NonRenTotalPrev;
    }

    public String getP6Q1GrandTotalCurr() {
        return p6Q1GrandTotalCurr;
    }

    public void setP6Q1GrandTotalCurr(String p6Q1GrandTotalCurr) {
        this.p6Q1GrandTotalCurr = p6Q1GrandTotalCurr;
    }

    public String getP6Q1GrandTotalPrev() {
        return p6Q1GrandTotalPrev;
    }

    public void setP6Q1GrandTotalPrev(String p6Q1GrandTotalPrev) {
        this.p6Q1GrandTotalPrev = p6Q1GrandTotalPrev;
    }

    public String getP6Q1IntTurnoverCurr() {
        return p6Q1IntTurnoverCurr;
    }

    public void setP6Q1IntTurnoverCurr(String p6Q1IntTurnoverCurr) {
        this.p6Q1IntTurnoverCurr = p6Q1IntTurnoverCurr;
    }

    public String getP6Q1IntTurnoverPrev() {
        return p6Q1IntTurnoverPrev;
    }

    public void setP6Q1IntTurnoverPrev(String p6Q1IntTurnoverPrev) {
        this.p6Q1IntTurnoverPrev = p6Q1IntTurnoverPrev;
    }

    public String getP6Q1IntPppCurr() {
        return p6Q1IntPppCurr;
    }

    public void setP6Q1IntPppCurr(String p6Q1IntPppCurr) {
        this.p6Q1IntPppCurr = p6Q1IntPppCurr;
    }

    public String getP6Q1IntPppPrev() {
        return p6Q1IntPppPrev;
    }

    public void setP6Q1IntPppPrev(String p6Q1IntPppPrev) {
        this.p6Q1IntPppPrev = p6Q1IntPppPrev;
    }

    public String getP6Q1IntPhysicalCurr() {
        return p6Q1IntPhysicalCurr;
    }

    public void setP6Q1IntPhysicalCurr(String p6Q1IntPhysicalCurr) {
        this.p6Q1IntPhysicalCurr = p6Q1IntPhysicalCurr;
    }

    public String getP6Q1IntPhysicalPrev() {
        return p6Q1IntPhysicalPrev;
    }

    public void setP6Q1IntPhysicalPrev(String p6Q1IntPhysicalPrev) {
        this.p6Q1IntPhysicalPrev = p6Q1IntPhysicalPrev;
    }

    public String getP6Q1IntOptCurr() {
        return p6Q1IntOptCurr;
    }

    public void setP6Q1IntOptCurr(String p6Q1IntOptCurr) {
        this.p6Q1IntOptCurr = p6Q1IntOptCurr;
    }

    public String getP6Q1IntOptPrev() {
        return p6Q1IntOptPrev;
    }

    public void setP6Q1IntOptPrev(String p6Q1IntOptPrev) {
        this.p6Q1IntOptPrev = p6Q1IntOptPrev;
    }

    public String getP6Q1AssuranceNote() {
        return p6Q1AssuranceNote;
    }

    public void setP6Q1AssuranceNote(String p6Q1AssuranceNote) {
        this.p6Q1AssuranceNote = p6Q1AssuranceNote;
    }

    public String getP6Q2PatDetails() {
        return p6Q2PatDetails;
    }

    public void setP6Q2PatDetails(String p6Q2PatDetails) {
        this.p6Q2PatDetails = p6Q2PatDetails;
    }

    public String getP6Q3SurfaceCurr() {
        return p6Q3SurfaceCurr;
    }

    public void setP6Q3SurfaceCurr(String p6Q3SurfaceCurr) {
        this.p6Q3SurfaceCurr = p6Q3SurfaceCurr;
    }

    public String getP6Q3SurfacePrev() {
        return p6Q3SurfacePrev;
    }

    public void setP6Q3SurfacePrev(String p6Q3SurfacePrev) {
        this.p6Q3SurfacePrev = p6Q3SurfacePrev;
    }

    public String getP6Q3GroundCurr() {
        return p6Q3GroundCurr;
    }

    public void setP6Q3GroundCurr(String p6Q3GroundCurr) {
        this.p6Q3GroundCurr = p6Q3GroundCurr;
    }

    public String getP6Q3GroundPrev() {
        return p6Q3GroundPrev;
    }

    public void setP6Q3GroundPrev(String p6Q3GroundPrev) {
        this.p6Q3GroundPrev = p6Q3GroundPrev;
    }

    public String getP6Q3ThirdPartyCurr() {
        return p6Q3ThirdPartyCurr;
    }

    public void setP6Q3ThirdPartyCurr(String p6Q3ThirdPartyCurr) {
        this.p6Q3ThirdPartyCurr = p6Q3ThirdPartyCurr;
    }

    public String getP6Q3ThirdPartyPrev() {
        return p6Q3ThirdPartyPrev;
    }

    public void setP6Q3ThirdPartyPrev(String p6Q3ThirdPartyPrev) {
        this.p6Q3ThirdPartyPrev = p6Q3ThirdPartyPrev;
    }

    public String getP6Q3SeaCurr() {
        return p6Q3SeaCurr;
    }

    public void setP6Q3SeaCurr(String p6Q3SeaCurr) {
        this.p6Q3SeaCurr = p6Q3SeaCurr;
    }

    public String getP6Q3SeaPrev() {
        return p6Q3SeaPrev;
    }

    public void setP6Q3SeaPrev(String p6Q3SeaPrev) {
        this.p6Q3SeaPrev = p6Q3SeaPrev;
    }

    public String getP6Q3OthersCurr() {
        return p6Q3OthersCurr;
    }

    public void setP6Q3OthersCurr(String p6Q3OthersCurr) {
        this.p6Q3OthersCurr = p6Q3OthersCurr;
    }

    public String getP6Q3OthersPrev() {
        return p6Q3OthersPrev;
    }

    public void setP6Q3OthersPrev(String p6Q3OthersPrev) {
        this.p6Q3OthersPrev = p6Q3OthersPrev;
    }

    public String getP6Q3TotalWithCurr() {
        return p6Q3TotalWithCurr;
    }

    public void setP6Q3TotalWithCurr(String p6Q3TotalWithCurr) {
        this.p6Q3TotalWithCurr = p6Q3TotalWithCurr;
    }

    public String getP6Q3TotalWithPrev() {
        return p6Q3TotalWithPrev;
    }

    public void setP6Q3TotalWithPrev(String p6Q3TotalWithPrev) {
        this.p6Q3TotalWithPrev = p6Q3TotalWithPrev;
    }

    public String getP6Q3TotalConsCurr() {
        return p6Q3TotalConsCurr;
    }

    public void setP6Q3TotalConsCurr(String p6Q3TotalConsCurr) {
        this.p6Q3TotalConsCurr = p6Q3TotalConsCurr;
    }

    public String getP6Q3TotalConsPrev() {
        return p6Q3TotalConsPrev;
    }

    public void setP6Q3TotalConsPrev(String p6Q3TotalConsPrev) {
        this.p6Q3TotalConsPrev = p6Q3TotalConsPrev;
    }

    public String getP6Q3IntTurnoverCurr() {
        return p6Q3IntTurnoverCurr;
    }

    public void setP6Q3IntTurnoverCurr(String p6Q3IntTurnoverCurr) {
        this.p6Q3IntTurnoverCurr = p6Q3IntTurnoverCurr;
    }

    public String getP6Q3IntTurnoverPrev() {
        return p6Q3IntTurnoverPrev;
    }

    public void setP6Q3IntTurnoverPrev(String p6Q3IntTurnoverPrev) {
        this.p6Q3IntTurnoverPrev = p6Q3IntTurnoverPrev;
    }

    public String getP6Q3IntPppPrev() {
        return p6Q3IntPppPrev;
    }

    public void setP6Q3IntPppPrev(String p6Q3IntPppPrev) {
        this.p6Q3IntPppPrev = p6Q3IntPppPrev;
    }

    public String getP6Q3IntPhysicalCurr() {
        return p6Q3IntPhysicalCurr;
    }

    public void setP6Q3IntPhysicalCurr(String p6Q3IntPhysicalCurr) {
        this.p6Q3IntPhysicalCurr = p6Q3IntPhysicalCurr;
    }

    public String getP6Q3IntPhysicalPrev() {
        return p6Q3IntPhysicalPrev;
    }

    public void setP6Q3IntPhysicalPrev(String p6Q3IntPhysicalPrev) {
        this.p6Q3IntPhysicalPrev = p6Q3IntPhysicalPrev;
    }

    public String getP6Q3IntOptCurr() {
        return p6Q3IntOptCurr;
    }

    public void setP6Q3IntOptCurr(String p6Q3IntOptCurr) {
        this.p6Q3IntOptCurr = p6Q3IntOptCurr;
    }

    public String getP6Q3IntOptPrev() {
        return p6Q3IntOptPrev;
    }

    public void setP6Q3IntOptPrev(String p6Q3IntOptPrev) {
        this.p6Q3IntOptPrev = p6Q3IntOptPrev;
    }

    public String getP6Q3AssuranceNote() {
        return p6Q3AssuranceNote;
    }

    public void setP6Q3AssuranceNote(String p6Q3AssuranceNote) {
        this.p6Q3AssuranceNote = p6Q3AssuranceNote;
    }

    private String p6Q3IntPppCurr; private String p6Q3IntPppPrev;
    private String p6Q3IntPhysicalCurr; private String p6Q3IntPhysicalPrev;
    private String p6Q3IntOptCurr; private String p6Q3IntOptPrev;
    private String p6Q3AssuranceNote;

    // --- Principle 6 : Q4 Water Discharge ---
    private String p6Q4SurfNoCurr; private String p6Q4SurfNoPrev;
    private String p6Q4SurfWithCurr; private String p6Q4SurfWithPrev; private String p6Q4SurfLevel;
    private String p6Q4GroundNoCurr; private String p6Q4GroundNoPrev;
    private String p6Q4GroundWithCurr; private String p6Q4GroundWithPrev; private String p6Q4GroundLevel;
    private String p6Q4SeaNoCurr; private String p6Q4SeaNoPrev;
    private String p6Q4SeaWithCurr; private String p6Q4SeaWithPrev; private String p6Q4SeaLevel;
    private String p6Q4ThirdNoCurr; private String p6Q4ThirdNoPrev;
    private String p6Q4ThirdWithCurr; private String p6Q4ThirdWithPrev; private String p6Q4ThirdLevel;
    private String p6Q4OtherNoCurr; private String p6Q4OtherNoPrev;
    private String p6Q4OtherWithCurr; private String p6Q4OtherWithPrev; private String p6Q4OtherLevel;
    private String p6Q4TotalCurr; private String p6Q4TotalPrev;
    private String p6Q4AssuranceNote;

    // --- Principle 6 : Q5 ZLD ---
    private String p6Q5ZldDetails;

    // --- Principle 6 : Q6 Air Emissions ---
    private String p6Q6NoxUnit; private String p6Q6NoxCurr; private String p6Q6NoxPrev;
    private String p6Q6SoxUnit; private String p6Q6SoxCurr; private String p6Q6SoxPrev;
    private String p6Q6PmUnit; private String p6Q6PmCurr; private String p6Q6PmPrev;

    public String getP6Q6PopUnit() {
        return p6Q6PopUnit;
    }

    public void setP6Q6PopUnit(String p6Q6PopUnit) {
        this.p6Q6PopUnit = p6Q6PopUnit;
    }

    public String getP6Q4SurfNoCurr() {
        return p6Q4SurfNoCurr;
    }

    public void setP6Q4SurfNoCurr(String p6Q4SurfNoCurr) {
        this.p6Q4SurfNoCurr = p6Q4SurfNoCurr;
    }

    public String getP6Q4SurfNoPrev() {
        return p6Q4SurfNoPrev;
    }

    public void setP6Q4SurfNoPrev(String p6Q4SurfNoPrev) {
        this.p6Q4SurfNoPrev = p6Q4SurfNoPrev;
    }

    public String getP6Q4SurfWithCurr() {
        return p6Q4SurfWithCurr;
    }

    public void setP6Q4SurfWithCurr(String p6Q4SurfWithCurr) {
        this.p6Q4SurfWithCurr = p6Q4SurfWithCurr;
    }

    public String getP6Q4SurfWithPrev() {
        return p6Q4SurfWithPrev;
    }

    public void setP6Q4SurfWithPrev(String p6Q4SurfWithPrev) {
        this.p6Q4SurfWithPrev = p6Q4SurfWithPrev;
    }

    public String getP6Q4SurfLevel() {
        return p6Q4SurfLevel;
    }

    public void setP6Q4SurfLevel(String p6Q4SurfLevel) {
        this.p6Q4SurfLevel = p6Q4SurfLevel;
    }

    public String getP6Q4GroundNoCurr() {
        return p6Q4GroundNoCurr;
    }

    public void setP6Q4GroundNoCurr(String p6Q4GroundNoCurr) {
        this.p6Q4GroundNoCurr = p6Q4GroundNoCurr;
    }

    public String getP6Q4GroundNoPrev() {
        return p6Q4GroundNoPrev;
    }

    public void setP6Q4GroundNoPrev(String p6Q4GroundNoPrev) {
        this.p6Q4GroundNoPrev = p6Q4GroundNoPrev;
    }

    public String getP6Q4GroundWithCurr() {
        return p6Q4GroundWithCurr;
    }

    public void setP6Q4GroundWithCurr(String p6Q4GroundWithCurr) {
        this.p6Q4GroundWithCurr = p6Q4GroundWithCurr;
    }

    public String getP6Q4GroundWithPrev() {
        return p6Q4GroundWithPrev;
    }

    public void setP6Q4GroundWithPrev(String p6Q4GroundWithPrev) {
        this.p6Q4GroundWithPrev = p6Q4GroundWithPrev;
    }

    public String getP6Q4GroundLevel() {
        return p6Q4GroundLevel;
    }

    public void setP6Q4GroundLevel(String p6Q4GroundLevel) {
        this.p6Q4GroundLevel = p6Q4GroundLevel;
    }

    public String getP6Q4SeaNoCurr() {
        return p6Q4SeaNoCurr;
    }

    public void setP6Q4SeaNoCurr(String p6Q4SeaNoCurr) {
        this.p6Q4SeaNoCurr = p6Q4SeaNoCurr;
    }

    public String getP6Q4SeaNoPrev() {
        return p6Q4SeaNoPrev;
    }

    public void setP6Q4SeaNoPrev(String p6Q4SeaNoPrev) {
        this.p6Q4SeaNoPrev = p6Q4SeaNoPrev;
    }

    public String getP6Q4SeaWithCurr() {
        return p6Q4SeaWithCurr;
    }

    public void setP6Q4SeaWithCurr(String p6Q4SeaWithCurr) {
        this.p6Q4SeaWithCurr = p6Q4SeaWithCurr;
    }

    public String getP6Q4SeaWithPrev() {
        return p6Q4SeaWithPrev;
    }

    public void setP6Q4SeaWithPrev(String p6Q4SeaWithPrev) {
        this.p6Q4SeaWithPrev = p6Q4SeaWithPrev;
    }

    public String getP6Q4SeaLevel() {
        return p6Q4SeaLevel;
    }

    public void setP6Q4SeaLevel(String p6Q4SeaLevel) {
        this.p6Q4SeaLevel = p6Q4SeaLevel;
    }

    public String getP6Q4ThirdNoCurr() {
        return p6Q4ThirdNoCurr;
    }

    public void setP6Q4ThirdNoCurr(String p6Q4ThirdNoCurr) {
        this.p6Q4ThirdNoCurr = p6Q4ThirdNoCurr;
    }

    public String getP6Q4ThirdNoPrev() {
        return p6Q4ThirdNoPrev;
    }

    public void setP6Q4ThirdNoPrev(String p6Q4ThirdNoPrev) {
        this.p6Q4ThirdNoPrev = p6Q4ThirdNoPrev;
    }

    public String getP6Q4ThirdWithCurr() {
        return p6Q4ThirdWithCurr;
    }

    public void setP6Q4ThirdWithCurr(String p6Q4ThirdWithCurr) {
        this.p6Q4ThirdWithCurr = p6Q4ThirdWithCurr;
    }

    public String getP6Q4ThirdWithPrev() {
        return p6Q4ThirdWithPrev;
    }

    public void setP6Q4ThirdWithPrev(String p6Q4ThirdWithPrev) {
        this.p6Q4ThirdWithPrev = p6Q4ThirdWithPrev;
    }

    public String getP6Q4ThirdLevel() {
        return p6Q4ThirdLevel;
    }

    public void setP6Q4ThirdLevel(String p6Q4ThirdLevel) {
        this.p6Q4ThirdLevel = p6Q4ThirdLevel;
    }

    public String getP6Q4OtherNoCurr() {
        return p6Q4OtherNoCurr;
    }

    public void setP6Q4OtherNoCurr(String p6Q4OtherNoCurr) {
        this.p6Q4OtherNoCurr = p6Q4OtherNoCurr;
    }

    public String getP6Q4OtherNoPrev() {
        return p6Q4OtherNoPrev;
    }

    public void setP6Q4OtherNoPrev(String p6Q4OtherNoPrev) {
        this.p6Q4OtherNoPrev = p6Q4OtherNoPrev;
    }

    public String getP6Q4OtherWithCurr() {
        return p6Q4OtherWithCurr;
    }

    public void setP6Q4OtherWithCurr(String p6Q4OtherWithCurr) {
        this.p6Q4OtherWithCurr = p6Q4OtherWithCurr;
    }

    public String getP6Q4OtherWithPrev() {
        return p6Q4OtherWithPrev;
    }

    public void setP6Q4OtherWithPrev(String p6Q4OtherWithPrev) {
        this.p6Q4OtherWithPrev = p6Q4OtherWithPrev;
    }

    public String getP6Q4OtherLevel() {
        return p6Q4OtherLevel;
    }

    public void setP6Q4OtherLevel(String p6Q4OtherLevel) {
        this.p6Q4OtherLevel = p6Q4OtherLevel;
    }

    public String getP6Q4TotalCurr() {
        return p6Q4TotalCurr;
    }

    public void setP6Q4TotalCurr(String p6Q4TotalCurr) {
        this.p6Q4TotalCurr = p6Q4TotalCurr;
    }

    public String getP6Q4TotalPrev() {
        return p6Q4TotalPrev;
    }

    public void setP6Q4TotalPrev(String p6Q4TotalPrev) {
        this.p6Q4TotalPrev = p6Q4TotalPrev;
    }

    public String getP6Q4AssuranceNote() {
        return p6Q4AssuranceNote;
    }

    public void setP6Q4AssuranceNote(String p6Q4AssuranceNote) {
        this.p6Q4AssuranceNote = p6Q4AssuranceNote;
    }

    public String getP6Q5ZldDetails() {
        return p6Q5ZldDetails;
    }

    public void setP6Q5ZldDetails(String p6Q5ZldDetails) {
        this.p6Q5ZldDetails = p6Q5ZldDetails;
    }

    public String getP6Q6NoxUnit() {
        return p6Q6NoxUnit;
    }

    public void setP6Q6NoxUnit(String p6Q6NoxUnit) {
        this.p6Q6NoxUnit = p6Q6NoxUnit;
    }

    public String getP6Q6NoxCurr() {
        return p6Q6NoxCurr;
    }

    public void setP6Q6NoxCurr(String p6Q6NoxCurr) {
        this.p6Q6NoxCurr = p6Q6NoxCurr;
    }

    public String getP6Q6NoxPrev() {
        return p6Q6NoxPrev;
    }

    public void setP6Q6NoxPrev(String p6Q6NoxPrev) {
        this.p6Q6NoxPrev = p6Q6NoxPrev;
    }

    public String getP6Q6SoxUnit() {
        return p6Q6SoxUnit;
    }

    public void setP6Q6SoxUnit(String p6Q6SoxUnit) {
        this.p6Q6SoxUnit = p6Q6SoxUnit;
    }

    public String getP6Q6SoxCurr() {
        return p6Q6SoxCurr;
    }

    public void setP6Q6SoxCurr(String p6Q6SoxCurr) {
        this.p6Q6SoxCurr = p6Q6SoxCurr;
    }

    public String getP6Q6SoxPrev() {
        return p6Q6SoxPrev;
    }

    public void setP6Q6SoxPrev(String p6Q6SoxPrev) {
        this.p6Q6SoxPrev = p6Q6SoxPrev;
    }

    public String getP6Q6PmUnit() {
        return p6Q6PmUnit;
    }

    public void setP6Q6PmUnit(String p6Q6PmUnit) {
        this.p6Q6PmUnit = p6Q6PmUnit;
    }

    public String getP6Q6PmCurr() {
        return p6Q6PmCurr;
    }

    public void setP6Q6PmCurr(String p6Q6PmCurr) {
        this.p6Q6PmCurr = p6Q6PmCurr;
    }

    public String getP6Q6PmPrev() {
        return p6Q6PmPrev;
    }

    public void setP6Q6PmPrev(String p6Q6PmPrev) {
        this.p6Q6PmPrev = p6Q6PmPrev;
    }

    public String getP6Q6PopCurr() {
        return p6Q6PopCurr;
    }

    public void setP6Q6PopCurr(String p6Q6PopCurr) {
        this.p6Q6PopCurr = p6Q6PopCurr;
    }

    public String getP6Q6PopPrev() {
        return p6Q6PopPrev;
    }

    public void setP6Q6PopPrev(String p6Q6PopPrev) {
        this.p6Q6PopPrev = p6Q6PopPrev;
    }

    public String getP6Q6VocUnit() {
        return p6Q6VocUnit;
    }

    public void setP6Q6VocUnit(String p6Q6VocUnit) {
        this.p6Q6VocUnit = p6Q6VocUnit;
    }

    public String getP6Q6VocCurr() {
        return p6Q6VocCurr;
    }

    public void setP6Q6VocCurr(String p6Q6VocCurr) {
        this.p6Q6VocCurr = p6Q6VocCurr;
    }

    public String getP6Q6VocPrev() {
        return p6Q6VocPrev;
    }

    public void setP6Q6VocPrev(String p6Q6VocPrev) {
        this.p6Q6VocPrev = p6Q6VocPrev;
    }

    public String getP6Q6HapUnit() {
        return p6Q6HapUnit;
    }

    public void setP6Q6HapUnit(String p6Q6HapUnit) {
        this.p6Q6HapUnit = p6Q6HapUnit;
    }

    public String getP6Q6HapCurr() {
        return p6Q6HapCurr;
    }

    public void setP6Q6HapCurr(String p6Q6HapCurr) {
        this.p6Q6HapCurr = p6Q6HapCurr;
    }

    public String getP6Q6HapPrev() {
        return p6Q6HapPrev;
    }

    public void setP6Q6HapPrev(String p6Q6HapPrev) {
        this.p6Q6HapPrev = p6Q6HapPrev;
    }

    public String getP6Q6OtherName() {
        return p6Q6OtherName;
    }

    public void setP6Q6OtherName(String p6Q6OtherName) {
        this.p6Q6OtherName = p6Q6OtherName;
    }

    public String getP6Q6OtherUnit() {
        return p6Q6OtherUnit;
    }

    public void setP6Q6OtherUnit(String p6Q6OtherUnit) {
        this.p6Q6OtherUnit = p6Q6OtherUnit;
    }

    public String getP6Q6OtherCurr() {
        return p6Q6OtherCurr;
    }

    public void setP6Q6OtherCurr(String p6Q6OtherCurr) {
        this.p6Q6OtherCurr = p6Q6OtherCurr;
    }

    public String getP6Q6OtherPrev() {
        return p6Q6OtherPrev;
    }

    public void setP6Q6OtherPrev(String p6Q6OtherPrev) {
        this.p6Q6OtherPrev = p6Q6OtherPrev;
    }

    public String getP6Q6AssuranceNote() {
        return p6Q6AssuranceNote;
    }

    public void setP6Q6AssuranceNote(String p6Q6AssuranceNote) {
        this.p6Q6AssuranceNote = p6Q6AssuranceNote;
    }

    private String p6Q6PopUnit; private String p6Q6PopCurr; private String p6Q6PopPrev;
    private String p6Q6VocUnit; private String p6Q6VocCurr; private String p6Q6VocPrev;
    private String p6Q6HapUnit; private String p6Q6HapCurr; private String p6Q6HapPrev;
    private String p6Q6OtherName; private String p6Q6OtherUnit; private String p6Q6OtherCurr; private String p6Q6OtherPrev;
    private String p6Q6AssuranceNote;

    // --- Principle 6 : Q7 GHG Emissions ---
    private String p6Q7Scope1Unit; private String p6Q7Scope1Curr; private String p6Q7Scope1Prev;
    private String p6Q7Scope2Unit; private String p6Q7Scope2Curr; private String p6Q7Scope2Prev;
    private String p6Q7IntTurnoverCurr; private String p6Q7IntTurnoverPrev;
    private String p6Q7IntPppCurr;

    public String getP6Q7IntPppPrev() {
        return p6Q7IntPppPrev;
    }

    public void setP6Q7IntPppPrev(String p6Q7IntPppPrev) {
        this.p6Q7IntPppPrev = p6Q7IntPppPrev;
    }

    public String getP6Q7Scope1Unit() {
        return p6Q7Scope1Unit;
    }

    public void setP6Q7Scope1Unit(String p6Q7Scope1Unit) {
        this.p6Q7Scope1Unit = p6Q7Scope1Unit;
    }

    public String getP6Q7Scope1Curr() {
        return p6Q7Scope1Curr;
    }

    public void setP6Q7Scope1Curr(String p6Q7Scope1Curr) {
        this.p6Q7Scope1Curr = p6Q7Scope1Curr;
    }

    public String getP6Q7Scope1Prev() {
        return p6Q7Scope1Prev;
    }

    public void setP6Q7Scope1Prev(String p6Q7Scope1Prev) {
        this.p6Q7Scope1Prev = p6Q7Scope1Prev;
    }

    public String getP6Q7Scope2Unit() {
        return p6Q7Scope2Unit;
    }

    public void setP6Q7Scope2Unit(String p6Q7Scope2Unit) {
        this.p6Q7Scope2Unit = p6Q7Scope2Unit;
    }

    public String getP6Q7Scope2Curr() {
        return p6Q7Scope2Curr;
    }

    public void setP6Q7Scope2Curr(String p6Q7Scope2Curr) {
        this.p6Q7Scope2Curr = p6Q7Scope2Curr;
    }

    public String getP6Q7Scope2Prev() {
        return p6Q7Scope2Prev;
    }

    public void setP6Q7Scope2Prev(String p6Q7Scope2Prev) {
        this.p6Q7Scope2Prev = p6Q7Scope2Prev;
    }

    public String getP6Q7IntTurnoverCurr() {
        return p6Q7IntTurnoverCurr;
    }

    public void setP6Q7IntTurnoverCurr(String p6Q7IntTurnoverCurr) {
        this.p6Q7IntTurnoverCurr = p6Q7IntTurnoverCurr;
    }

    public String getP6Q7IntTurnoverPrev() {
        return p6Q7IntTurnoverPrev;
    }

    public void setP6Q7IntTurnoverPrev(String p6Q7IntTurnoverPrev) {
        this.p6Q7IntTurnoverPrev = p6Q7IntTurnoverPrev;
    }

    public String getP6Q7IntPppCurr() {
        return p6Q7IntPppCurr;
    }

    public void setP6Q7IntPppCurr(String p6Q7IntPppCurr) {
        this.p6Q7IntPppCurr = p6Q7IntPppCurr;
    }

    public String getP6Q7IntPhysCurr() {
        return p6Q7IntPhysCurr;
    }

    public void setP6Q7IntPhysCurr(String p6Q7IntPhysCurr) {
        this.p6Q7IntPhysCurr = p6Q7IntPhysCurr;
    }

    public String getP6Q7IntPhysPrev() {
        return p6Q7IntPhysPrev;
    }

    public void setP6Q7IntPhysPrev(String p6Q7IntPhysPrev) {
        this.p6Q7IntPhysPrev = p6Q7IntPhysPrev;
    }

    public String getP6Q7IntOptCurr() {
        return p6Q7IntOptCurr;
    }

    public void setP6Q7IntOptCurr(String p6Q7IntOptCurr) {
        this.p6Q7IntOptCurr = p6Q7IntOptCurr;
    }

    public String getP6Q7IntOptPrev() {
        return p6Q7IntOptPrev;
    }

    public void setP6Q7IntOptPrev(String p6Q7IntOptPrev) {
        this.p6Q7IntOptPrev = p6Q7IntOptPrev;
    }

    public String getP6Q7AssuranceNote() {
        return p6Q7AssuranceNote;
    }

    public void setP6Q7AssuranceNote(String p6Q7AssuranceNote) {
        this.p6Q7AssuranceNote = p6Q7AssuranceNote;
    }

    public String getP6Q8GhgProjectDetails() {
        return p6Q8GhgProjectDetails;
    }

    public void setP6Q8GhgProjectDetails(String p6Q8GhgProjectDetails) {
        this.p6Q8GhgProjectDetails = p6Q8GhgProjectDetails;
    }

    public String getP6Q9GenPlastCurr() {
        return p6Q9GenPlastCurr;
    }

    public void setP6Q9GenPlastCurr(String p6Q9GenPlastCurr) {
        this.p6Q9GenPlastCurr = p6Q9GenPlastCurr;
    }

    public String getP6Q9GenPlastPrev() {
        return p6Q9GenPlastPrev;
    }

    public void setP6Q9GenPlastPrev(String p6Q9GenPlastPrev) {
        this.p6Q9GenPlastPrev = p6Q9GenPlastPrev;
    }

    public String getP6Q9GenEwasteCurr() {
        return p6Q9GenEwasteCurr;
    }

    public void setP6Q9GenEwasteCurr(String p6Q9GenEwasteCurr) {
        this.p6Q9GenEwasteCurr = p6Q9GenEwasteCurr;
    }

    public String getP6Q9GenEwastePrev() {
        return p6Q9GenEwastePrev;
    }

    public void setP6Q9GenEwastePrev(String p6Q9GenEwastePrev) {
        this.p6Q9GenEwastePrev = p6Q9GenEwastePrev;
    }

    public String getP6Q9GenBioCurr() {
        return p6Q9GenBioCurr;
    }

    public void setP6Q9GenBioCurr(String p6Q9GenBioCurr) {
        this.p6Q9GenBioCurr = p6Q9GenBioCurr;
    }

    public String getP6Q9GenBioPrev() {
        return p6Q9GenBioPrev;
    }

    public void setP6Q9GenBioPrev(String p6Q9GenBioPrev) {
        this.p6Q9GenBioPrev = p6Q9GenBioPrev;
    }

    public String getP6Q9GenConstCurr() {
        return p6Q9GenConstCurr;
    }

    public void setP6Q9GenConstCurr(String p6Q9GenConstCurr) {
        this.p6Q9GenConstCurr = p6Q9GenConstCurr;
    }

    public String getP6Q9GenConstPrev() {
        return p6Q9GenConstPrev;
    }

    public void setP6Q9GenConstPrev(String p6Q9GenConstPrev) {
        this.p6Q9GenConstPrev = p6Q9GenConstPrev;
    }

    public String getP6Q9GenBattCurr() {
        return p6Q9GenBattCurr;
    }

    public void setP6Q9GenBattCurr(String p6Q9GenBattCurr) {
        this.p6Q9GenBattCurr = p6Q9GenBattCurr;
    }

    public String getP6Q9GenBattPrev() {
        return p6Q9GenBattPrev;
    }

    public void setP6Q9GenBattPrev(String p6Q9GenBattPrev) {
        this.p6Q9GenBattPrev = p6Q9GenBattPrev;
    }

    public String getP6Q9GenRadioCurr() {
        return p6Q9GenRadioCurr;
    }

    public void setP6Q9GenRadioCurr(String p6Q9GenRadioCurr) {
        this.p6Q9GenRadioCurr = p6Q9GenRadioCurr;
    }

    public String getP6Q9GenRadioPrev() {
        return p6Q9GenRadioPrev;
    }

    public void setP6Q9GenRadioPrev(String p6Q9GenRadioPrev) {
        this.p6Q9GenRadioPrev = p6Q9GenRadioPrev;
    }

    public String getP6Q9GenHazCurr() {
        return p6Q9GenHazCurr;
    }

    public void setP6Q9GenHazCurr(String p6Q9GenHazCurr) {
        this.p6Q9GenHazCurr = p6Q9GenHazCurr;
    }

    public String getP6Q9GenHazPrev() {
        return p6Q9GenHazPrev;
    }

    public void setP6Q9GenHazPrev(String p6Q9GenHazPrev) {
        this.p6Q9GenHazPrev = p6Q9GenHazPrev;
    }

    public String getP6Q9GenNonHazCurr() {
        return p6Q9GenNonHazCurr;
    }

    public void setP6Q9GenNonHazCurr(String p6Q9GenNonHazCurr) {
        this.p6Q9GenNonHazCurr = p6Q9GenNonHazCurr;
    }

    public String getP6Q9GenNonHazPrev() {
        return p6Q9GenNonHazPrev;
    }

    public void setP6Q9GenNonHazPrev(String p6Q9GenNonHazPrev) {
        this.p6Q9GenNonHazPrev = p6Q9GenNonHazPrev;
    }

    public String getP6Q9GenTotalCurr() {
        return p6Q9GenTotalCurr;
    }

    public void setP6Q9GenTotalCurr(String p6Q9GenTotalCurr) {
        this.p6Q9GenTotalCurr = p6Q9GenTotalCurr;
    }

    public String getP6Q9GenTotalPrev() {
        return p6Q9GenTotalPrev;
    }

    public void setP6Q9GenTotalPrev(String p6Q9GenTotalPrev) {
        this.p6Q9GenTotalPrev = p6Q9GenTotalPrev;
    }

    public String getP6Q9IntTurnCurr() {
        return p6Q9IntTurnCurr;
    }

    public void setP6Q9IntTurnCurr(String p6Q9IntTurnCurr) {
        this.p6Q9IntTurnCurr = p6Q9IntTurnCurr;
    }

    public String getP6Q9IntTurnPrev() {
        return p6Q9IntTurnPrev;
    }

    public void setP6Q9IntTurnPrev(String p6Q9IntTurnPrev) {
        this.p6Q9IntTurnPrev = p6Q9IntTurnPrev;
    }

    public String getP6Q9IntPppCurr() {
        return p6Q9IntPppCurr;
    }

    public void setP6Q9IntPppCurr(String p6Q9IntPppCurr) {
        this.p6Q9IntPppCurr = p6Q9IntPppCurr;
    }

    public String getP6Q9IntPppPrev() {
        return p6Q9IntPppPrev;
    }

    public void setP6Q9IntPppPrev(String p6Q9IntPppPrev) {
        this.p6Q9IntPppPrev = p6Q9IntPppPrev;
    }

    public String getP6Q9IntPhysCurr() {
        return p6Q9IntPhysCurr;
    }

    public void setP6Q9IntPhysCurr(String p6Q9IntPhysCurr) {
        this.p6Q9IntPhysCurr = p6Q9IntPhysCurr;
    }

    public String getP6Q9IntPhysPrev() {
        return p6Q9IntPhysPrev;
    }

    public void setP6Q9IntPhysPrev(String p6Q9IntPhysPrev) {
        this.p6Q9IntPhysPrev = p6Q9IntPhysPrev;
    }

    public String getP6Q9IntOptCurr() {
        return p6Q9IntOptCurr;
    }

    public void setP6Q9IntOptCurr(String p6Q9IntOptCurr) {
        this.p6Q9IntOptCurr = p6Q9IntOptCurr;
    }

    public String getP6Q9IntOptPrev() {
        return p6Q9IntOptPrev;
    }

    public void setP6Q9IntOptPrev(String p6Q9IntOptPrev) {
        this.p6Q9IntOptPrev = p6Q9IntOptPrev;
    }

    public String getP6Q9RecRecyCurr() {
        return p6Q9RecRecyCurr;
    }

    public void setP6Q9RecRecyCurr(String p6Q9RecRecyCurr) {
        this.p6Q9RecRecyCurr = p6Q9RecRecyCurr;
    }

    public String getP6Q9RecRecyPrev() {
        return p6Q9RecRecyPrev;
    }

    public void setP6Q9RecRecyPrev(String p6Q9RecRecyPrev) {
        this.p6Q9RecRecyPrev = p6Q9RecRecyPrev;
    }

    public String getP6Q9RecReuseCurr() {
        return p6Q9RecReuseCurr;
    }

    public void setP6Q9RecReuseCurr(String p6Q9RecReuseCurr) {
        this.p6Q9RecReuseCurr = p6Q9RecReuseCurr;
    }

    public String getP6Q9RecReusePrev() {
        return p6Q9RecReusePrev;
    }

    public void setP6Q9RecReusePrev(String p6Q9RecReusePrev) {
        this.p6Q9RecReusePrev = p6Q9RecReusePrev;
    }

    public String getP6Q9RecOthCurr() {
        return p6Q9RecOthCurr;
    }

    public void setP6Q9RecOthCurr(String p6Q9RecOthCurr) {
        this.p6Q9RecOthCurr = p6Q9RecOthCurr;
    }

    public String getP6Q9RecOthPrev() {
        return p6Q9RecOthPrev;
    }

    public void setP6Q9RecOthPrev(String p6Q9RecOthPrev) {
        this.p6Q9RecOthPrev = p6Q9RecOthPrev;
    }

    public String getP6Q9RecTotalCurr() {
        return p6Q9RecTotalCurr;
    }

    public void setP6Q9RecTotalCurr(String p6Q9RecTotalCurr) {
        this.p6Q9RecTotalCurr = p6Q9RecTotalCurr;
    }

    public String getP6Q9RecTotalPrev() {
        return p6Q9RecTotalPrev;
    }

    public void setP6Q9RecTotalPrev(String p6Q9RecTotalPrev) {
        this.p6Q9RecTotalPrev = p6Q9RecTotalPrev;
    }

    public String getP6Q9DispIncCurr() {
        return p6Q9DispIncCurr;
    }

    public void setP6Q9DispIncCurr(String p6Q9DispIncCurr) {
        this.p6Q9DispIncCurr = p6Q9DispIncCurr;
    }

    public String getP6Q9DispIncPrev() {
        return p6Q9DispIncPrev;
    }

    public void setP6Q9DispIncPrev(String p6Q9DispIncPrev) {
        this.p6Q9DispIncPrev = p6Q9DispIncPrev;
    }

    public String getP6Q9DispLandCurr() {
        return p6Q9DispLandCurr;
    }

    public void setP6Q9DispLandCurr(String p6Q9DispLandCurr) {
        this.p6Q9DispLandCurr = p6Q9DispLandCurr;
    }

    public String getP6Q9DispLandPrev() {
        return p6Q9DispLandPrev;
    }

    public void setP6Q9DispLandPrev(String p6Q9DispLandPrev) {
        this.p6Q9DispLandPrev = p6Q9DispLandPrev;
    }

    public String getP6Q9DispOthCurr() {
        return p6Q9DispOthCurr;
    }

    public void setP6Q9DispOthCurr(String p6Q9DispOthCurr) {
        this.p6Q9DispOthCurr = p6Q9DispOthCurr;
    }

    public String getP6Q9DispOthPrev() {
        return p6Q9DispOthPrev;
    }

    public void setP6Q9DispOthPrev(String p6Q9DispOthPrev) {
        this.p6Q9DispOthPrev = p6Q9DispOthPrev;
    }

    public String getP6Q9DispTotalCurr() {
        return p6Q9DispTotalCurr;
    }

    public void setP6Q9DispTotalCurr(String p6Q9DispTotalCurr) {
        this.p6Q9DispTotalCurr = p6Q9DispTotalCurr;
    }

    public String getP6Q9DispTotalPrev() {
        return p6Q9DispTotalPrev;
    }

    public void setP6Q9DispTotalPrev(String p6Q9DispTotalPrev) {
        this.p6Q9DispTotalPrev = p6Q9DispTotalPrev;
    }

    public String getP6Q9AssuranceNote() {
        return p6Q9AssuranceNote;
    }

    public void setP6Q9AssuranceNote(String p6Q9AssuranceNote) {
        this.p6Q9AssuranceNote = p6Q9AssuranceNote;
    }

    private String p6Q7IntPppPrev;
    private String p6Q7IntPhysCurr; private String p6Q7IntPhysPrev;
    private String p6Q7IntOptCurr; private String p6Q7IntOptPrev;
    private String p6Q7AssuranceNote;

    // --- Principle 6 : Q8 GHG Projects ---
    private String p6Q8GhgProjectDetails;

    // --- Principle 6 : Q9 Waste Management ---
    private String p6Q9GenPlastCurr; private String p6Q9GenPlastPrev;
    private String p6Q9GenEwasteCurr; private String p6Q9GenEwastePrev;
    private String p6Q9GenBioCurr; private String p6Q9GenBioPrev;
    private String p6Q9GenConstCurr; private String p6Q9GenConstPrev;
    private String p6Q9GenBattCurr; private String p6Q9GenBattPrev;
    private String p6Q9GenRadioCurr; private String p6Q9GenRadioPrev;
    private String p6Q9GenHazCurr; private String p6Q9GenHazPrev;
    private String p6Q9GenNonHazCurr; private String p6Q9GenNonHazPrev;
    private String p6Q9GenTotalCurr; private String p6Q9GenTotalPrev;
    private String p6Q9IntTurnCurr; private String p6Q9IntTurnPrev;
    private String p6Q9IntPppCurr; private String p6Q9IntPppPrev;
    private String p6Q9IntPhysCurr; private String p6Q9IntPhysPrev;
    private String p6Q9IntOptCurr; private String p6Q9IntOptPrev;
    private String p6Q9RecRecyCurr; private String p6Q9RecRecyPrev;
    private String p6Q9RecReuseCurr; private String p6Q9RecReusePrev;
    private String p6Q9RecOthCurr; private String p6Q9RecOthPrev;
    private String p6Q9RecTotalCurr; private String p6Q9RecTotalPrev;
    private String p6Q9DispIncCurr; private String p6Q9DispIncPrev;
    private String p6Q9DispLandCurr; private String p6Q9DispLandPrev;
    private String p6Q9DispOthCurr; private String p6Q9DispOthPrev;
    private String p6Q9DispTotalCurr; private String p6Q9DispTotalPrev;
    private String p6Q9AssuranceNote;

    // --- Principle 6: Q10 - Q13 ---
    private String p6Q10WastePractices;
    private List<P6Q11Eco> p6Q11EcoList;
    private List<P6Q12Eia> p6Q12EiaList;
    private List<P6Q13NonComp> p6Q13NonCompList;

    // --- Inner Classes for Dynamic Tables ---
    @Data
    public static class P6Q11Eco {
        private String sNo;
        private String location;
        private String type;
        private String compliance;
    }

    @Data
    public static class P6Q12Eia {
        private String name;
        private String notifNo;
        private String date;
        private String independent;
        private String publicDomain;
        private String link;
    }

    @Data
    public static class P6Q13NonComp {
        private String sNo;
        private String law;
        private String details;
        private String fines;
        private String action;
    }

    // --- Getters and Setters for P6 Q10-Q13 ---
    public String getP6Q10WastePractices() { return p6Q10WastePractices; }
    public void setP6Q10WastePractices(String p6Q10WastePractices) { this.p6Q10WastePractices = p6Q10WastePractices; }

    public List<P6Q11Eco> getP6Q11EcoList() { return p6Q11EcoList; }
    public void setP6Q11EcoList(List<P6Q11Eco> p6Q11EcoList) { this.p6Q11EcoList = p6Q11EcoList; }

    public List<P6Q12Eia> getP6Q12EiaList() { return p6Q12EiaList; }
    public void setP6Q12EiaList(List<P6Q12Eia> p6Q12EiaList) { this.p6Q12EiaList = p6Q12EiaList; }

    public List<P6Q13NonComp> getP6Q13NonCompList() { return p6Q13NonCompList; }
    public void setP6Q13NonCompList(List<P6Q13NonComp> p6Q13NonCompList) { this.p6Q13NonCompList = p6Q13NonCompList; }

    // --- P6 Leadership Q1 ---
    private String p6LeadQ1Facilities;
    private String p6LeadQ1WithSurfCurr; private String p6LeadQ1WithSurfPrev;
    private String p6LeadQ1WithGroundCurr; private String p6LeadQ1WithGroundPrev;
    private String p6LeadQ1WithThirdCurr; private String p6LeadQ1WithThirdPrev;
    private String p6LeadQ1WithSeaCurr; private String p6LeadQ1WithSeaPrev;
    private String p6LeadQ1WithOtherCurr; private String p6LeadQ1WithOtherPrev;
    private String p6LeadQ1WithTotalCurr; private String p6LeadQ1WithTotalPrev;
    private String p6LeadQ1ConsTotalCurr; private String p6LeadQ1ConsTotalPrev;
    private String p6LeadQ1IntTurnCurr; private String p6LeadQ1IntTurnPrev;
    private String p6LeadQ1IntOptCurr; private String p6LeadQ1IntOptPrev;
    private String p6LeadQ1DisSurfNoCurr; private String p6LeadQ1DisSurfNoPrev;
    private String p6LeadQ1DisSurfWithCurr; private String p6LeadQ1DisSurfWithPrev; private String p6LeadQ1DisSurfLevel;
    private String p6LeadQ1DisGroundNoCurr; private String p6LeadQ1DisGroundNoPrev;
    private String p6LeadQ1DisGroundWithCurr; private String p6LeadQ1DisGroundWithPrev; private String p6LeadQ1DisGroundLevel;
    private String p6LeadQ1DisSeaNoCurr; private String p6LeadQ1DisSeaNoPrev;
    private String p6LeadQ1DisSeaWithCurr; private String p6LeadQ1DisSeaWithPrev; private String p6LeadQ1DisSeaLevel;
    private String p6LeadQ1DisThirdNoCurr; private String p6LeadQ1DisThirdNoPrev;
    private String p6LeadQ1DisThirdWithCurr; private String p6LeadQ1DisThirdWithPrev; private String p6LeadQ1DisThirdLevel;
    private String p6LeadQ1DisOtherNoCurr; private String p6LeadQ1DisOtherNoPrev;
    private String p6LeadQ1DisOtherWithCurr; private String p6LeadQ1DisOtherWithPrev; private String p6LeadQ1DisOtherLevel;
    private String p6LeadQ1DisTotalCurr; private String p6LeadQ1DisTotalPrev;
    private String p6LeadQ1AssuranceNote;

    // --- P6 Leadership Q2-Q7 ---
    private String p6LeadQ2Scope3Curr; private String p6LeadQ2Scope3Prev;
    private String p6LeadQ2IntTurnCurr; private String p6LeadQ2IntTurnPrev;

    public String getP6LeadQ2IntTurnPrev() {
        return p6LeadQ2IntTurnPrev;
    }

    public void setP6LeadQ2IntTurnPrev(String p6LeadQ2IntTurnPrev) {
        this.p6LeadQ2IntTurnPrev = p6LeadQ2IntTurnPrev;
    }

    public String getP6LeadQ1Facilities() {
        return p6LeadQ1Facilities;
    }

    public void setP6LeadQ1Facilities(String p6LeadQ1Facilities) {
        this.p6LeadQ1Facilities = p6LeadQ1Facilities;
    }

    public String getP6LeadQ1WithSurfCurr() {
        return p6LeadQ1WithSurfCurr;
    }

    public void setP6LeadQ1WithSurfCurr(String p6LeadQ1WithSurfCurr) {
        this.p6LeadQ1WithSurfCurr = p6LeadQ1WithSurfCurr;
    }

    public String getP6LeadQ1WithSurfPrev() {
        return p6LeadQ1WithSurfPrev;
    }

    public void setP6LeadQ1WithSurfPrev(String p6LeadQ1WithSurfPrev) {
        this.p6LeadQ1WithSurfPrev = p6LeadQ1WithSurfPrev;
    }

    public String getP6LeadQ1WithGroundCurr() {
        return p6LeadQ1WithGroundCurr;
    }

    public void setP6LeadQ1WithGroundCurr(String p6LeadQ1WithGroundCurr) {
        this.p6LeadQ1WithGroundCurr = p6LeadQ1WithGroundCurr;
    }

    public String getP6LeadQ1WithGroundPrev() {
        return p6LeadQ1WithGroundPrev;
    }

    public void setP6LeadQ1WithGroundPrev(String p6LeadQ1WithGroundPrev) {
        this.p6LeadQ1WithGroundPrev = p6LeadQ1WithGroundPrev;
    }

    public String getP6LeadQ1WithThirdCurr() {
        return p6LeadQ1WithThirdCurr;
    }

    public void setP6LeadQ1WithThirdCurr(String p6LeadQ1WithThirdCurr) {
        this.p6LeadQ1WithThirdCurr = p6LeadQ1WithThirdCurr;
    }

    public String getP6LeadQ1WithThirdPrev() {
        return p6LeadQ1WithThirdPrev;
    }

    public void setP6LeadQ1WithThirdPrev(String p6LeadQ1WithThirdPrev) {
        this.p6LeadQ1WithThirdPrev = p6LeadQ1WithThirdPrev;
    }

    public String getP6LeadQ1WithSeaCurr() {
        return p6LeadQ1WithSeaCurr;
    }

    public void setP6LeadQ1WithSeaCurr(String p6LeadQ1WithSeaCurr) {
        this.p6LeadQ1WithSeaCurr = p6LeadQ1WithSeaCurr;
    }

    public String getP6LeadQ1WithSeaPrev() {
        return p6LeadQ1WithSeaPrev;
    }

    public void setP6LeadQ1WithSeaPrev(String p6LeadQ1WithSeaPrev) {
        this.p6LeadQ1WithSeaPrev = p6LeadQ1WithSeaPrev;
    }

    public String getP6LeadQ1WithOtherCurr() {
        return p6LeadQ1WithOtherCurr;
    }

    public void setP6LeadQ1WithOtherCurr(String p6LeadQ1WithOtherCurr) {
        this.p6LeadQ1WithOtherCurr = p6LeadQ1WithOtherCurr;
    }

    public String getP6LeadQ1WithOtherPrev() {
        return p6LeadQ1WithOtherPrev;
    }

    public void setP6LeadQ1WithOtherPrev(String p6LeadQ1WithOtherPrev) {
        this.p6LeadQ1WithOtherPrev = p6LeadQ1WithOtherPrev;
    }

    public String getP6LeadQ1WithTotalCurr() {
        return p6LeadQ1WithTotalCurr;
    }

    public void setP6LeadQ1WithTotalCurr(String p6LeadQ1WithTotalCurr) {
        this.p6LeadQ1WithTotalCurr = p6LeadQ1WithTotalCurr;
    }

    public String getP6LeadQ1WithTotalPrev() {
        return p6LeadQ1WithTotalPrev;
    }

    public void setP6LeadQ1WithTotalPrev(String p6LeadQ1WithTotalPrev) {
        this.p6LeadQ1WithTotalPrev = p6LeadQ1WithTotalPrev;
    }

    public String getP6LeadQ1ConsTotalCurr() {
        return p6LeadQ1ConsTotalCurr;
    }

    public void setP6LeadQ1ConsTotalCurr(String p6LeadQ1ConsTotalCurr) {
        this.p6LeadQ1ConsTotalCurr = p6LeadQ1ConsTotalCurr;
    }

    public String getP6LeadQ1ConsTotalPrev() {
        return p6LeadQ1ConsTotalPrev;
    }

    public void setP6LeadQ1ConsTotalPrev(String p6LeadQ1ConsTotalPrev) {
        this.p6LeadQ1ConsTotalPrev = p6LeadQ1ConsTotalPrev;
    }

    public String getP6LeadQ1IntTurnCurr() {
        return p6LeadQ1IntTurnCurr;
    }

    public void setP6LeadQ1IntTurnCurr(String p6LeadQ1IntTurnCurr) {
        this.p6LeadQ1IntTurnCurr = p6LeadQ1IntTurnCurr;
    }

    public String getP6LeadQ1IntTurnPrev() {
        return p6LeadQ1IntTurnPrev;
    }

    public void setP6LeadQ1IntTurnPrev(String p6LeadQ1IntTurnPrev) {
        this.p6LeadQ1IntTurnPrev = p6LeadQ1IntTurnPrev;
    }

    public String getP6LeadQ1IntOptCurr() {
        return p6LeadQ1IntOptCurr;
    }

    public void setP6LeadQ1IntOptCurr(String p6LeadQ1IntOptCurr) {
        this.p6LeadQ1IntOptCurr = p6LeadQ1IntOptCurr;
    }

    public String getP6LeadQ1IntOptPrev() {
        return p6LeadQ1IntOptPrev;
    }

    public void setP6LeadQ1IntOptPrev(String p6LeadQ1IntOptPrev) {
        this.p6LeadQ1IntOptPrev = p6LeadQ1IntOptPrev;
    }

    public String getP6LeadQ1DisSurfNoCurr() {
        return p6LeadQ1DisSurfNoCurr;
    }

    public void setP6LeadQ1DisSurfNoCurr(String p6LeadQ1DisSurfNoCurr) {
        this.p6LeadQ1DisSurfNoCurr = p6LeadQ1DisSurfNoCurr;
    }

    public String getP6LeadQ1DisSurfNoPrev() {
        return p6LeadQ1DisSurfNoPrev;
    }

    public void setP6LeadQ1DisSurfNoPrev(String p6LeadQ1DisSurfNoPrev) {
        this.p6LeadQ1DisSurfNoPrev = p6LeadQ1DisSurfNoPrev;
    }

    public String getP6LeadQ1DisSurfWithCurr() {
        return p6LeadQ1DisSurfWithCurr;
    }

    public void setP6LeadQ1DisSurfWithCurr(String p6LeadQ1DisSurfWithCurr) {
        this.p6LeadQ1DisSurfWithCurr = p6LeadQ1DisSurfWithCurr;
    }

    public String getP6LeadQ1DisSurfWithPrev() {
        return p6LeadQ1DisSurfWithPrev;
    }

    public void setP6LeadQ1DisSurfWithPrev(String p6LeadQ1DisSurfWithPrev) {
        this.p6LeadQ1DisSurfWithPrev = p6LeadQ1DisSurfWithPrev;
    }

    public String getP6LeadQ1DisSurfLevel() {
        return p6LeadQ1DisSurfLevel;
    }

    public void setP6LeadQ1DisSurfLevel(String p6LeadQ1DisSurfLevel) {
        this.p6LeadQ1DisSurfLevel = p6LeadQ1DisSurfLevel;
    }

    public String getP6LeadQ1DisGroundNoCurr() {
        return p6LeadQ1DisGroundNoCurr;
    }

    public void setP6LeadQ1DisGroundNoCurr(String p6LeadQ1DisGroundNoCurr) {
        this.p6LeadQ1DisGroundNoCurr = p6LeadQ1DisGroundNoCurr;
    }

    public String getP6LeadQ1DisGroundNoPrev() {
        return p6LeadQ1DisGroundNoPrev;
    }

    public void setP6LeadQ1DisGroundNoPrev(String p6LeadQ1DisGroundNoPrev) {
        this.p6LeadQ1DisGroundNoPrev = p6LeadQ1DisGroundNoPrev;
    }

    public String getP6LeadQ1DisGroundWithCurr() {
        return p6LeadQ1DisGroundWithCurr;
    }

    public void setP6LeadQ1DisGroundWithCurr(String p6LeadQ1DisGroundWithCurr) {
        this.p6LeadQ1DisGroundWithCurr = p6LeadQ1DisGroundWithCurr;
    }

    public String getP6LeadQ1DisGroundWithPrev() {
        return p6LeadQ1DisGroundWithPrev;
    }

    public void setP6LeadQ1DisGroundWithPrev(String p6LeadQ1DisGroundWithPrev) {
        this.p6LeadQ1DisGroundWithPrev = p6LeadQ1DisGroundWithPrev;
    }

    public String getP6LeadQ1DisGroundLevel() {
        return p6LeadQ1DisGroundLevel;
    }

    public void setP6LeadQ1DisGroundLevel(String p6LeadQ1DisGroundLevel) {
        this.p6LeadQ1DisGroundLevel = p6LeadQ1DisGroundLevel;
    }

    public String getP6LeadQ1DisSeaNoCurr() {
        return p6LeadQ1DisSeaNoCurr;
    }

    public void setP6LeadQ1DisSeaNoCurr(String p6LeadQ1DisSeaNoCurr) {
        this.p6LeadQ1DisSeaNoCurr = p6LeadQ1DisSeaNoCurr;
    }

    public String getP6LeadQ1DisSeaNoPrev() {
        return p6LeadQ1DisSeaNoPrev;
    }

    public void setP6LeadQ1DisSeaNoPrev(String p6LeadQ1DisSeaNoPrev) {
        this.p6LeadQ1DisSeaNoPrev = p6LeadQ1DisSeaNoPrev;
    }

    public String getP6LeadQ1DisSeaWithCurr() {
        return p6LeadQ1DisSeaWithCurr;
    }

    public void setP6LeadQ1DisSeaWithCurr(String p6LeadQ1DisSeaWithCurr) {
        this.p6LeadQ1DisSeaWithCurr = p6LeadQ1DisSeaWithCurr;
    }

    public String getP6LeadQ1DisSeaWithPrev() {
        return p6LeadQ1DisSeaWithPrev;
    }

    public void setP6LeadQ1DisSeaWithPrev(String p6LeadQ1DisSeaWithPrev) {
        this.p6LeadQ1DisSeaWithPrev = p6LeadQ1DisSeaWithPrev;
    }

    public String getP6LeadQ1DisSeaLevel() {
        return p6LeadQ1DisSeaLevel;
    }

    public void setP6LeadQ1DisSeaLevel(String p6LeadQ1DisSeaLevel) {
        this.p6LeadQ1DisSeaLevel = p6LeadQ1DisSeaLevel;
    }

    public String getP6LeadQ1DisThirdNoCurr() {
        return p6LeadQ1DisThirdNoCurr;
    }

    public void setP6LeadQ1DisThirdNoCurr(String p6LeadQ1DisThirdNoCurr) {
        this.p6LeadQ1DisThirdNoCurr = p6LeadQ1DisThirdNoCurr;
    }

    public String getP6LeadQ1DisThirdNoPrev() {
        return p6LeadQ1DisThirdNoPrev;
    }

    public void setP6LeadQ1DisThirdNoPrev(String p6LeadQ1DisThirdNoPrev) {
        this.p6LeadQ1DisThirdNoPrev = p6LeadQ1DisThirdNoPrev;
    }

    public String getP6LeadQ1DisThirdWithCurr() {
        return p6LeadQ1DisThirdWithCurr;
    }

    public void setP6LeadQ1DisThirdWithCurr(String p6LeadQ1DisThirdWithCurr) {
        this.p6LeadQ1DisThirdWithCurr = p6LeadQ1DisThirdWithCurr;
    }

    public String getP6LeadQ1DisThirdWithPrev() {
        return p6LeadQ1DisThirdWithPrev;
    }

    public void setP6LeadQ1DisThirdWithPrev(String p6LeadQ1DisThirdWithPrev) {
        this.p6LeadQ1DisThirdWithPrev = p6LeadQ1DisThirdWithPrev;
    }

    public String getP6LeadQ1DisThirdLevel() {
        return p6LeadQ1DisThirdLevel;
    }

    public void setP6LeadQ1DisThirdLevel(String p6LeadQ1DisThirdLevel) {
        this.p6LeadQ1DisThirdLevel = p6LeadQ1DisThirdLevel;
    }

    public String getP6LeadQ1DisOtherNoCurr() {
        return p6LeadQ1DisOtherNoCurr;
    }

    public void setP6LeadQ1DisOtherNoCurr(String p6LeadQ1DisOtherNoCurr) {
        this.p6LeadQ1DisOtherNoCurr = p6LeadQ1DisOtherNoCurr;
    }

    public String getP6LeadQ1DisOtherNoPrev() {
        return p6LeadQ1DisOtherNoPrev;
    }

    public void setP6LeadQ1DisOtherNoPrev(String p6LeadQ1DisOtherNoPrev) {
        this.p6LeadQ1DisOtherNoPrev = p6LeadQ1DisOtherNoPrev;
    }

    public String getP6LeadQ1DisOtherWithCurr() {
        return p6LeadQ1DisOtherWithCurr;
    }

    public void setP6LeadQ1DisOtherWithCurr(String p6LeadQ1DisOtherWithCurr) {
        this.p6LeadQ1DisOtherWithCurr = p6LeadQ1DisOtherWithCurr;
    }

    public String getP6LeadQ1DisOtherWithPrev() {
        return p6LeadQ1DisOtherWithPrev;
    }

    public void setP6LeadQ1DisOtherWithPrev(String p6LeadQ1DisOtherWithPrev) {
        this.p6LeadQ1DisOtherWithPrev = p6LeadQ1DisOtherWithPrev;
    }

    public String getP6LeadQ1DisOtherLevel() {
        return p6LeadQ1DisOtherLevel;
    }

    public void setP6LeadQ1DisOtherLevel(String p6LeadQ1DisOtherLevel) {
        this.p6LeadQ1DisOtherLevel = p6LeadQ1DisOtherLevel;
    }

    public String getP6LeadQ1DisTotalCurr() {
        return p6LeadQ1DisTotalCurr;
    }

    public void setP6LeadQ1DisTotalCurr(String p6LeadQ1DisTotalCurr) {
        this.p6LeadQ1DisTotalCurr = p6LeadQ1DisTotalCurr;
    }

    public String getP6LeadQ1DisTotalPrev() {
        return p6LeadQ1DisTotalPrev;
    }

    public void setP6LeadQ1DisTotalPrev(String p6LeadQ1DisTotalPrev) {
        this.p6LeadQ1DisTotalPrev = p6LeadQ1DisTotalPrev;
    }

    public String getP6LeadQ1AssuranceNote() {
        return p6LeadQ1AssuranceNote;
    }

    public void setP6LeadQ1AssuranceNote(String p6LeadQ1AssuranceNote) {
        this.p6LeadQ1AssuranceNote = p6LeadQ1AssuranceNote;
    }

    public String getP6LeadQ2Scope3Curr() {
        return p6LeadQ2Scope3Curr;
    }

    public void setP6LeadQ2Scope3Curr(String p6LeadQ2Scope3Curr) {
        this.p6LeadQ2Scope3Curr = p6LeadQ2Scope3Curr;
    }

    public String getP6LeadQ2Scope3Prev() {
        return p6LeadQ2Scope3Prev;
    }

    public void setP6LeadQ2Scope3Prev(String p6LeadQ2Scope3Prev) {
        this.p6LeadQ2Scope3Prev = p6LeadQ2Scope3Prev;
    }

    public String getP6LeadQ2IntTurnCurr() {
        return p6LeadQ2IntTurnCurr;
    }

    public void setP6LeadQ2IntTurnCurr(String p6LeadQ2IntTurnCurr) {
        this.p6LeadQ2IntTurnCurr = p6LeadQ2IntTurnCurr;
    }

    public String getP6LeadQ2IntOptCurr() {
        return p6LeadQ2IntOptCurr;
    }

    public void setP6LeadQ2IntOptCurr(String p6LeadQ2IntOptCurr) {
        this.p6LeadQ2IntOptCurr = p6LeadQ2IntOptCurr;
    }

    public String getP6LeadQ2IntOptPrev() {
        return p6LeadQ2IntOptPrev;
    }

    public void setP6LeadQ2IntOptPrev(String p6LeadQ2IntOptPrev) {
        this.p6LeadQ2IntOptPrev = p6LeadQ2IntOptPrev;
    }

    public String getP6LeadQ2AssuranceNote() {
        return p6LeadQ2AssuranceNote;
    }

    public void setP6LeadQ2AssuranceNote(String p6LeadQ2AssuranceNote) {
        this.p6LeadQ2AssuranceNote = p6LeadQ2AssuranceNote;
    }

    public String getP6LeadQ3EcoImpact() {
        return p6LeadQ3EcoImpact;
    }

    public void setP6LeadQ3EcoImpact(String p6LeadQ3EcoImpact) {
        this.p6LeadQ3EcoImpact = p6LeadQ3EcoImpact;
    }

    public List<P6LeadQ4Initiative> getP6LeadQ4InitiativesList() {
        return p6LeadQ4InitiativesList;
    }

    public void setP6LeadQ4InitiativesList(List<P6LeadQ4Initiative> p6LeadQ4InitiativesList) {
        this.p6LeadQ4InitiativesList = p6LeadQ4InitiativesList;
    }

    public String getP6LeadQ5DisasterPlan() {
        return p6LeadQ5DisasterPlan;
    }

    public void setP6LeadQ5DisasterPlan(String p6LeadQ5DisasterPlan) {
        this.p6LeadQ5DisasterPlan = p6LeadQ5DisasterPlan;
    }

    public String getP6LeadQ6ValueChainImpact() {
        return p6LeadQ6ValueChainImpact;
    }

    public void setP6LeadQ6ValueChainImpact(String p6LeadQ6ValueChainImpact) {
        this.p6LeadQ6ValueChainImpact = p6LeadQ6ValueChainImpact;
    }

    public String getP6LeadQ7ValueChainAssessed() {
        return p6LeadQ7ValueChainAssessed;
    }

    public void setP6LeadQ7ValueChainAssessed(String p6LeadQ7ValueChainAssessed) {
        this.p6LeadQ7ValueChainAssessed = p6LeadQ7ValueChainAssessed;
    }

    private String p6LeadQ2IntOptCurr; private String p6LeadQ2IntOptPrev;
    private String p6LeadQ2AssuranceNote;
    private String p6LeadQ3EcoImpact;
    private List<P6LeadQ4Initiative> p6LeadQ4InitiativesList;
    private String p6LeadQ5DisasterPlan;
    private String p6LeadQ6ValueChainImpact;
    private String p6LeadQ7ValueChainAssessed;

    @Data
    public static class P6LeadQ4Initiative {
        private String sNo;
        private String initiative;
        private String details;
        private String outcome;
    }

    // ================= PRINCIPLE 7 =================
    private String p7Q1aAffiliations;
    private List<P7Q1bAssociation> p7Q1bList;

    public List<P7Q2CorrectiveAction> getP7Q2List() {
        return p7Q2List;
    }

    public void setP7Q2List(List<P7Q2CorrectiveAction> p7Q2List) {
        this.p7Q2List = p7Q2List;
    }

    public String getP7Q1aAffiliations() {
        return p7Q1aAffiliations;
    }

    public void setP7Q1aAffiliations(String p7Q1aAffiliations) {
        this.p7Q1aAffiliations = p7Q1aAffiliations;
    }

    public List<P7Q1bAssociation> getP7Q1bList() {
        return p7Q1bList;
    }

    public void setP7Q1bList(List<P7Q1bAssociation> p7Q1bList) {
        this.p7Q1bList = p7Q1bList;
    }

    public List<P7LeadQ1Policy> getP7LeadQ1List() {
        return p7LeadQ1List;
    }

    public void setP7LeadQ1List(List<P7LeadQ1Policy> p7LeadQ1List) {
        this.p7LeadQ1List = p7LeadQ1List;
    }

    private List<P7Q2CorrectiveAction> p7Q2List;
    private List<P7LeadQ1Policy> p7LeadQ1List;

    @Data
    public static class P7Q1bAssociation {
        private String sNo;
        private String name;
        private String reach;
    }

    @Data
    public static class P7Q2CorrectiveAction {
        private String authority;
        private String brief;
        private String correctiveAction;
    }

    @Data
    public static class P7LeadQ1Policy {
        private String sNo;
        private String policy;
        private String method;
        private String publicDomain;
        private String frequency;
        private String link;
    }

    // ================= PRINCIPLE 8 =================
    private List<P8Q1Sia> p8Q1SiaList;
    private List<P8Q2Rr> p8Q2RrList;
    private String p8Q3GrievanceMech;
    private String p8Q4MsmeCurr; private String p8Q4MsmePrev;
    private String p8Q4IndiaCurr; private String p8Q4IndiaPrev;
    private String p8Q5RuralCurr; private String p8Q5RuralPrev;
    private String p8Q5SemiCurr; private String p8Q5SemiPrev;
    private String p8Q5UrbanCurr; private String p8Q5UrbanPrev;
    private String p8Q5MetroCurr; private String p8Q5MetroPrev;

    public static class P8Q1Sia {
        private String name; private String notifNo; private String date;
        private String independent; private String publicDomain; private String link;
        public String getName() { return name; } public void setName(String name) { this.name = name; }
        public String getNotifNo() { return notifNo; } public void setNotifNo(String notifNo) { this.notifNo = notifNo; }
        public String getDate() { return date; } public void setDate(String date) { this.date = date; }
        public String getIndependent() { return independent; } public void setIndependent(String independent) { this.independent = independent; }
        public String getPublicDomain() { return publicDomain; } public void setPublicDomain(String publicDomain) { this.publicDomain = publicDomain; }
        public String getLink() { return link; } public void setLink(String link) { this.link = link; }
    }

    public static class P8Q2Rr {
        private String sNo; private String name; private String state; private String district;
        private String pafs; private String perc; private String amount;
        public String getSNo() { return sNo; } public void setSNo(String sNo) { this.sNo = sNo; }
        public String getName() { return name; } public void setName(String name) { this.name = name; }
        public String getState() { return state; } public void setState(String state) { this.state = state; }
        public String getDistrict() { return district; } public void setDistrict(String district) { this.district = district; }
        public String getPafs() { return pafs; } public void setPafs(String pafs) { this.pafs = pafs; }
        public String getPerc() { return perc; } public void setPerc(String perc) { this.perc = perc; }
        public String getAmount() { return amount; } public void setAmount(String amount) { this.amount = amount; }
    }

    // Main Field Getters and Setters
    public List<P8Q1Sia> getP8Q1SiaList() { return p8Q1SiaList; } public void setP8Q1SiaList(List<P8Q1Sia> p8Q1SiaList) { this.p8Q1SiaList = p8Q1SiaList; }
    public List<P8Q2Rr> getP8Q2RrList() { return p8Q2RrList; } public void setP8Q2RrList(List<P8Q2Rr> p8Q2RrList) { this.p8Q2RrList = p8Q2RrList; }
    public String getP8Q3GrievanceMech() { return p8Q3GrievanceMech; } public void setP8Q3GrievanceMech(String p8Q3GrievanceMech) { this.p8Q3GrievanceMech = p8Q3GrievanceMech; }
    public String getP8Q4MsmeCurr() { return p8Q4MsmeCurr; } public void setP8Q4MsmeCurr(String p8Q4MsmeCurr) { this.p8Q4MsmeCurr = p8Q4MsmeCurr; }
    public String getP8Q4MsmePrev() { return p8Q4MsmePrev; } public void setP8Q4MsmePrev(String p8Q4MsmePrev) { this.p8Q4MsmePrev = p8Q4MsmePrev; }
    public String getP8Q4IndiaCurr() { return p8Q4IndiaCurr; } public void setP8Q4IndiaCurr(String p8Q4IndiaCurr) { this.p8Q4IndiaCurr = p8Q4IndiaCurr; }
    public String getP8Q4IndiaPrev() { return p8Q4IndiaPrev; } public void setP8Q4IndiaPrev(String p8Q4IndiaPrev) { this.p8Q4IndiaPrev = p8Q4IndiaPrev; }
    public String getP8Q5RuralCurr() { return p8Q5RuralCurr; } public void setP8Q5RuralCurr(String p8Q5RuralCurr) { this.p8Q5RuralCurr = p8Q5RuralCurr; }
    public String getP8Q5RuralPrev() { return p8Q5RuralPrev; } public void setP8Q5RuralPrev(String p8Q5RuralPrev) { this.p8Q5RuralPrev = p8Q5RuralPrev; }
    public String getP8Q5SemiCurr() { return p8Q5SemiCurr; } public void setP8Q5SemiCurr(String p8Q5SemiCurr) { this.p8Q5SemiCurr = p8Q5SemiCurr; }
    public String getP8Q5SemiPrev() { return p8Q5SemiPrev; } public void setP8Q5SemiPrev(String p8Q5SemiPrev) { this.p8Q5SemiPrev = p8Q5SemiPrev; }
    public String getP8Q5UrbanCurr() { return p8Q5UrbanCurr; } public void setP8Q5UrbanCurr(String p8Q5UrbanCurr) { this.p8Q5UrbanCurr = p8Q5UrbanCurr; }
    public String getP8Q5UrbanPrev() { return p8Q5UrbanPrev; } public void setP8Q5UrbanPrev(String p8Q5UrbanPrev) { this.p8Q5UrbanPrev = p8Q5UrbanPrev; }
    public String getP8Q5MetroCurr() { return p8Q5MetroCurr; } public void setP8Q5MetroCurr(String p8Q5MetroCurr) { this.p8Q5MetroCurr = p8Q5MetroCurr; }
    public String getP8Q5MetroPrev() { return p8Q5MetroPrev; } public void setP8Q5MetroPrev(String p8Q5MetroPrev) { this.p8Q5MetroPrev = p8Q5MetroPrev; }

    //p6 old fields
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

    //p6 old fields

}