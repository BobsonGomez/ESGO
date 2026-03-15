const currentReportId = localStorage.getItem("currentReportId");

// ============================================================================
// --- INITIALIZATION & DATA FETCHING ---
// ============================================================================
document.addEventListener("DOMContentLoaded", function() {
    // 1. Check for active report
    if (!currentReportId) {
        alert("No report started. Redirecting to Part 1.");
        window.location.href = "create_report.html";
        return;
    }

    const token = localStorage.getItem("token");

    // 2. Fetch existing data from backend
    fetch(`http://localhost:8080/api/report/${currentReportId}`, {
        method: "GET",
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(data => {
        console.log("Loaded Part 3 Data:", data);

        // ==========================================================
        // 3A. BATCH LOAD STANDARD TEXT / SELECT FIELDS
        // ==========================================================
        const fields = [
            // --- Core Environmental Fields ---
            "electricityConsumption", "fuelConsumption", "energyIntensity",
            "scope1Emissions", "scope2Emissions", "emissionIntensity",
            "plasticWaste", "eWaste", "hazardousWaste",
            "totalWasteGenerated", "totalWasteRecycled",

            // --- Principle 1 Notes & Policies ---
            "trainingNote", "legalActionNote",
            "antiCorruptionPolicy", "antiCorruptionLink", "antiCorruptionDetails",
            "correctiveActionDetails", "leadershipIndicatorNote",
            "conflictOfInterestProcess", "conflictOfInterestDetails",

            // --- Principle 1: Disciplinary & COI Tables (Direct Inputs) ---
            "disciplinaryFyCurrentHeader", "disciplinaryFyPreviousHeader",
            "discDirectorsCurr", "discKmpsCurr", "discEmployeesCurr", "discWorkersCurr",
            "discDirectorsPrev", "discKmpsPrev", "discEmployeesPrev", "discWorkersPrev",
            "coiDirectorsCurrNum", "coiDirectorsCurrRem", "coiDirectorsPrevNum", "coiDirectorsPrevRem",
            "coiKmpsCurrNum", "coiKmpsCurrRem", "coiKmpsPrevNum", "coiKmpsPrevRem",

            // --- Principle 1: Q8 Accounts Payable ---
            "accountsPayableCurr", "accountsPayablePrev",

            // --- Principle 1: Q9 Openness of Business ---
            "purTradingPercCurr", "purTradingPercPrev", "purTradingNumCurr", "purTradingNumPrev", "purTop10PercCurr", "purTop10PercPrev",
            "salesDealerPercCurr", "salesDealerPercPrev", "salesDealerNumCurr", "salesDealerNumPrev", "salesTop10PercCurr", "salesTop10PercPrev",
            "rptPurCurr", "rptPurPrev", "rptSalesCurr", "rptSalesPrev", "rptLoansCurr", "rptLoansPrev", "rptInvestCurr", "rptInvestPrev",
            "opennessNote",

            // --- Principle 2 Fields ---
            "rdCurrentYear", "rdPreviousYear", "rdDetails",
            "capexCurrentYear", "capexPreviousYear", "capexDetails",
            "principle2Note", "sustainableSourcingProcedures", "sustainableSourcingDetails",
            "sustainableSourcingPercentage", "sustainableSourcingRemarks",
            "reclaimProcessDetails", "eprDetails",

            // --- Principle 3 Fields (Wellbeing, Retirement, Accessibility) ---
            "empWellBeingNote", "workWellBeingNote", "wbCostCurr", "wbCostPrev", "wbCostNote",
            "retirementBenefitNote", "accessibilityDetails",
            "equalOppPolicy", "equalOppLink", "equalOppDetails",
            "plEmpMaleReturn", "plEmpMaleRetain", "plWorkMaleReturn", "plWorkMaleRetain",
            "plEmpFemaleReturn", "plEmpFemaleRetain", "plWorkFemaleReturn", "plWorkFemaleRetain",
            "plEmpTotalReturn", "plEmpTotalRetain", "plWorkTotalReturn", "plWorkTotalRetain",
            "parentalLeaveNote", "grievancePermWorkers", "grievanceTempWorkers",
            "grievancePermEmployees", "grievanceTempEmployees",

            // --- Principle 3: Union Membership ---
            "unionCurrEmpTotalA", "unionCurrEmpUnionB", "unionCurrEmpPerc",
            "unionCurrEmpMaleTotal", "unionCurrEmpMaleUnion", "unionCurrEmpMalePerc",
            "unionCurrEmpFemaleTotal", "unionCurrEmpFemaleUnion", "unionCurrEmpFemalePerc",
            "unionCurrWorkTotalA", "unionCurrWorkUnionB", "unionCurrWorkPerc",
            "unionCurrWorkMaleTotal", "unionCurrWorkMaleUnion", "unionCurrWorkMalePerc",
            "unionCurrWorkFemaleTotal", "unionCurrWorkFemaleUnion", "unionCurrWorkFemalePerc",
            "unionPrevEmpTotalC", "unionPrevEmpUnionD", "unionPrevEmpPerc",
            "unionPrevEmpMaleTotal", "unionPrevEmpMaleUnion", "unionPrevEmpMalePerc",
            "unionPrevEmpFemaleTotal", "unionPrevEmpFemaleUnion", "unionPrevEmpFemalePerc",
            "unionPrevWorkTotalC", "unionPrevWorkUnionD", "unionPrevWorkPerc",
            "unionPrevWorkMaleTotal", "unionPrevWorkMaleUnion", "unionPrevWorkMalePerc",
            "unionPrevWorkFemaleTotal", "unionPrevWorkFemaleUnion", "unionPrevWorkFemalePerc",
            "unionMembershipNote",

            // --- Principle 3: Training Details (Q8) ---
            "trainEmpMaleTotal", "trainEmpMaleHealthNo", "trainEmpMaleHealthPerc", "trainEmpMaleSkillNo", "trainEmpMaleSkillPerc",
            "trainEmpFemaleTotal", "trainEmpFemaleHealthNo", "trainEmpFemaleHealthPerc", "trainEmpFemaleSkillNo", "trainEmpFemaleSkillPerc",
            "trainEmpGenTotal", "trainEmpGenHealthNo", "trainEmpGenHealthPerc", "trainEmpGenSkillNo", "trainEmpGenSkillPerc",
            "trainEmpMaleTotalPrev", "trainEmpMaleHealthNoPrev", "trainEmpMaleHealthPercPrev", "trainEmpMaleSkillNoPrev", "trainEmpMaleSkillPercPrev",
            "trainEmpFemaleTotalPrev", "trainEmpFemaleHealthNoPrev", "trainEmpFemaleHealthPercPrev", "trainEmpFemaleSkillNoPrev", "trainEmpFemaleSkillPercPrev",
            "trainEmpGenTotalPrev", "trainEmpGenHealthNoPrev", "trainEmpGenHealthPercPrev", "trainEmpGenSkillNoPrev", "trainEmpGenSkillPercPrev",
            "trainWorkMaleTotal", "trainWorkMaleHealthNo", "trainWorkMaleHealthPerc", "trainWorkMaleSkillNo", "trainWorkMaleSkillPerc",
            "trainWorkFemaleTotal", "trainWorkFemaleHealthNo", "trainWorkFemaleHealthPerc", "trainWorkFemaleSkillNo", "trainWorkFemaleSkillPerc",
            "trainWorkGenTotal", "trainWorkGenHealthNo", "trainWorkGenHealthPerc", "trainWorkGenSkillNo", "trainWorkGenSkillPerc",
            "trainWorkMaleTotalPrev", "trainWorkMaleHealthNoPrev", "trainWorkMaleHealthPercPrev", "trainWorkMaleSkillNoPrev", "trainWorkMaleSkillPercPrev",
            "trainWorkFemaleTotalPrev", "trainWorkFemaleHealthNoPrev", "trainWorkFemaleHealthPercPrev", "trainWorkFemaleSkillNoPrev", "trainWorkFemaleSkillPercPrev",
            "trainWorkGenTotalPrev", "trainWorkGenHealthNoPrev", "trainWorkGenHealthPercPrev", "trainWorkGenSkillNoPrev", "trainWorkGenSkillPercPrev",
            "trainingDetailsNote",
            "trainEmpOtherTotal", "trainEmpOtherHealthNo", "trainEmpOtherHealthPerc", "trainEmpOtherSkillNo", "trainEmpOtherSkillPerc",
            "trainEmpOtherTotalPrev", "trainEmpOtherHealthNoPrev", "trainEmpOtherHealthPercPrev", "trainEmpOtherSkillNoPrev", "trainEmpOtherSkillPercPrev",
            "trainWorkOtherTotal", "trainWorkOtherHealthNo", "trainWorkOtherHealthPerc", "trainWorkOtherSkillNo", "trainWorkOtherSkillPerc",
            "trainWorkOtherTotalPrev", "trainWorkOtherHealthNoPrev", "trainWorkOtherHealthPercPrev", "trainWorkOtherSkillNoPrev", "trainWorkOtherSkillPercPrev",

            // --- Principle 3: Performance Reviews (Q9) ---
            "reviewDetailsNote",
            "revEmpMaleTotal", "revEmpMaleCovered", "revEmpMalePerc", "revEmpFemTotal", "revEmpFemCovered", "revEmpFemPerc",
            "revEmpOthTotal", "revEmpOthCovered", "revEmpOthPerc", "revEmpGenTotal", "revEmpGenCovered", "revEmpGenPerc",
            "revEmpMaleTotalPrev", "revEmpMaleCoveredPrev", "revEmpMalePercPrev", "revEmpFemTotalPrev", "revEmpFemCoveredPrev", "revEmpFemPercPrev",
            "revEmpOthTotalPrev", "revEmpOthCoveredPrev", "revEmpOthPercPrev", "revEmpGenTotalPrev", "revEmpGenCoveredPrev", "revEmpGenPercPrev",
            "revWorkMaleTotal", "revWorkMaleCovered", "revWorkMalePerc", "revWorkFemTotal", "revWorkFemCovered", "revWorkFemPerc",
            "revWorkOthTotal", "revWorkOthCovered", "revWorkOthPerc", "revWorkGenTotal", "revWorkGenCovered", "revWorkGenPerc",
            "revWorkMaleTotalPrev", "revWorkMaleCoveredPrev", "revWorkMalePercPrev", "revWorkFemTotalPrev", "revWorkFemCoveredPrev", "revWorkFemPercPrev",
            "revWorkOthTotalPrev", "revWorkOthCoveredPrev", "revWorkOthPercPrev", "revWorkGenTotalPrev", "revWorkGenCoveredPrev", "revWorkGenPercPrev",

            // --- Principle 3: Safety & Health (Q10 - Q15) ---
            "healthSafetySystem", "hazardIdentification", "hazardReporting", "medicalAccess",
            "safetyLtifrEmpCurr", "safetyLtifrEmpPrev", "safetyLtifrWorkCurr", "safetyLtifrWorkPrev",
            "safetyTotalInjEmpCurr", "safetyTotalInjEmpPrev", "safetyTotalInjWorkCurr", "safetyTotalInjWorkPrev",
            "safetyFatalEmpCurr", "safetyFatalEmpPrev", "safetyFatalWorkCurr", "safetyFatalWorkPrev",
            "safetyHighConEmpCurr", "safetyHighConEmpPrev", "safetyHighConWorkCurr", "safetyHighConWorkPrev",
            "safetyPermDisEmpCurr", "safetyPermDisEmpPrev", "safetyPermDisWorkCurr", "safetyPermDisWorkPrev",
            "safetyIncidentsNote",
            "wcFiledCurr", "wcPendingCurr", "wcRemarksCurr", "wcFiledPrev", "wcPendingPrev", "wcRemarksPrev",
            "hsFiledCurr", "hsPendingCurr", "hsRemarksCurr", "hsFiledPrev", "hsPendingPrev", "hsRemarksPrev",
            "workerComplaintsNote",
            "assessmentHealthPerc", "assessmentWorkingPerc", "assessmentNote",
            "safetyCorrectiveActions",
            "lifeInsuranceEmployees", "lifeInsuranceWorkers", "lifeInsuranceDetails",
            "statutoryDuesMeasures",
            "rehabEmpAffCurr", "rehabEmpAffPrev", "rehabEmpPlacedCurr", "rehabEmpPlacedPrev",
            "rehabWorkAffCurr", "rehabWorkAffPrev", "rehabWorkPlacedCurr", "rehabWorkPlacedPrev",
            "rehabilitationNote", "transitionAssistanceDetails",
            "valueChainAssessmentNote", "vcHealthSafetyPerc", "vcWorkingCondPerc",
            "vcCorrectiveActionIntro", // Note: Array is handled separately below
                "transitionAssistanceYN", "transitionAssistanceDetails", "valueChainAssessmentNote",

            // --- Principle 4 Fields ---
            "principle4Q1Intro", "principle4Q1Conclusion", "stakeholderNote",
            "consultationProcessDetails", "stakeholderConsultationUsed", "stakeholderConsultationDetails",
            "vulnerableGroupIntro", "vulnerableGroupConclusion",

            // --- Principle 5 Fields ---
            "hrEmpPermTotalCurr", "hrEmpPermCovCurr", "hrEmpPermPercCurr",
            "hrEmpTempTotalCurr", "hrEmpTempCovCurr", "hrEmpTempPercCurr",
            "hrEmpGrandTotalCurr", "hrEmpGrandCovCurr", "hrEmpGrandPercCurr",
            "hrEmpPermTotalPrev", "hrEmpPermCovPrev", "hrEmpPermPercPrev",
            "hrEmpTempTotalPrev", "hrEmpTempCovPrev", "hrEmpTempPercPrev",
            "hrEmpGrandTotalPrev", "hrEmpGrandCovPrev", "hrEmpGrandPercPrev",
            "hrWorkPermTotalCurr", "hrWorkPermCovCurr", "hrWorkPermPercCurr",
            "hrWorkTempTotalCurr", "hrWorkTempCovCurr", "hrWorkTempPercCurr",
            "hrWorkGrandTotalCurr", "hrWorkGrandCovCurr", "hrWorkGrandPercCurr",
            "hrWorkPermTotalPrev", "hrWorkPermCovPrev", "hrWorkPermPercPrev",
            "hrWorkTempTotalPrev", "hrWorkTempCovPrev", "hrWorkTempPercPrev",
            "hrWorkGrandTotalPrev", "hrWorkGrandCovPrev", "hrWorkGrandPercPrev",
            "hrTrainingNote", "minWageNote","hrComplaintsNote",

            // P5: Min Wages (Massive block, simplified mapping)
            "mwEmpPermMaleTotalCurr", "mwEmpPermMaleEqNoCurr", "mwEmpPermMaleEqPercCurr", "mwEmpPermMaleMoreNoCurr", "mwEmpPermMaleMorePercCurr",
            "mwEmpPermMaleTotalPrev", "mwEmpPermMaleEqNoPrev", "mwEmpPermMaleEqPercPrev", "mwEmpPermMaleMoreNoPrev", "mwEmpPermMaleMorePercPrev",
            "mwEmpPermFemTotalCurr", "mwEmpPermFemEqNoCurr", "mwEmpPermFemEqPercCurr", "mwEmpPermFemMoreNoCurr", "mwEmpPermFemMorePercCurr",
            "mwEmpPermFemTotalPrev", "mwEmpPermFemEqNoPrev", "mwEmpPermFemEqPercPrev", "mwEmpPermFemMoreNoPrev", "mwEmpPermFemMorePercPrev",
            "mwEmpPermOthTotalCurr", "mwEmpPermOthEqNoCurr", "mwEmpPermOthEqPercCurr", "mwEmpPermOthMoreNoCurr", "mwEmpPermOthMorePercCurr",
            "mwEmpPermOthTotalPrev", "mwEmpPermOthEqNoPrev", "mwEmpPermOthEqPercPrev", "mwEmpPermOthMoreNoPrev", "mwEmpPermOthMorePercPrev",
            "mwEmpTempMaleTotalCurr", "mwEmpTempMaleEqNoCurr", "mwEmpTempMaleEqPercCurr", "mwEmpTempMaleMoreNoCurr", "mwEmpTempMaleMorePercCurr",
            "mwEmpTempMaleTotalPrev", "mwEmpTempMaleEqNoPrev", "mwEmpTempMaleEqPercPrev", "mwEmpTempMaleMoreNoPrev", "mwEmpTempMaleMorePercPrev",
            "mwEmpTempFemTotalCurr", "mwEmpTempFemEqNoCurr", "mwEmpTempFemEqPercCurr", "mwEmpTempFemMoreNoCurr", "mwEmpTempFemMorePercCurr",
            "mwEmpTempFemTotalPrev", "mwEmpTempFemEqNoPrev", "mwEmpTempFemEqPercPrev", "mwEmpTempFemMoreNoPrev", "mwEmpTempFemMorePercPrev",
            "mwEmpTempOthTotalCurr", "mwEmpTempOthEqNoCurr", "mwEmpTempOthEqPercCurr", "mwEmpTempOthMoreNoCurr", "mwEmpTempOthMorePercCurr",
            "mwEmpTempOthTotalPrev", "mwEmpTempOthEqNoPrev", "mwEmpTempOthEqPercPrev", "mwEmpTempOthMoreNoPrev", "mwEmpTempOthMorePercPrev",
            "mwWorkPermMaleTotalCurr", "mwWorkPermMaleEqNoCurr", "mwWorkPermMaleEqPercCurr", "mwWorkPermMaleMoreNoCurr", "mwWorkPermMaleMorePercCurr",
            "mwWorkPermMaleTotalPrev", "mwWorkPermMaleEqNoPrev", "mwWorkPermMaleEqPercPrev", "mwWorkPermMaleMoreNoPrev", "mwWorkPermMaleMorePercPrev",
            "mwWorkPermFemTotalCurr", "mwWorkPermFemEqNoCurr", "mwWorkPermFemEqPercCurr", "mwWorkPermFemMoreNoCurr", "mwWorkPermFemMorePercCurr",
            "mwWorkPermFemTotalPrev", "mwWorkPermFemEqNoPrev", "mwWorkPermFemEqPercPrev", "mwWorkPermFemMoreNoPrev", "mwWorkPermFemMorePercPrev",
            "mwWorkPermOthTotalCurr", "mwWorkPermOthEqNoCurr", "mwWorkPermOthEqPercCurr", "mwWorkPermOthMoreNoCurr", "mwWorkPermOthMorePercCurr",
            "mwWorkPermOthTotalPrev", "mwWorkPermOthEqNoPrev", "mwWorkPermOthEqPercPrev", "mwWorkPermOthMoreNoPrev", "mwWorkPermOthMorePercPrev",
            "mwWorkTempMaleTotalCurr", "mwWorkTempMaleEqNoCurr", "mwWorkTempMaleEqPercCurr", "mwWorkTempMaleMoreNoCurr", "mwWorkTempMaleMorePercCurr",
            "mwWorkTempMaleTotalPrev", "mwWorkTempMaleEqNoPrev", "mwWorkTempMaleEqPercPrev", "mwWorkTempMaleMoreNoPrev", "mwWorkTempMaleMorePercPrev",
            "mwWorkTempFemTotalCurr", "mwWorkTempFemEqNoCurr", "mwWorkTempFemEqPercCurr", "mwWorkTempFemMoreNoCurr", "mwWorkTempFemMorePercCurr",
            "mwWorkTempFemTotalPrev", "mwWorkTempFemEqNoPrev", "mwWorkTempFemEqPercPrev", "mwWorkTempFemMoreNoPrev", "mwWorkTempFemMorePercPrev",
            "mwWorkTempOthTotalCurr", "mwWorkTempOthEqNoCurr", "mwWorkTempOthEqPercCurr", "mwWorkTempOthMoreNoCurr", "mwWorkTempOthMorePercCurr",
            "mwWorkTempOthTotalPrev", "mwWorkTempOthEqNoPrev", "mwWorkTempOthEqPercPrev", "mwWorkTempOthMoreNoPrev", "mwWorkTempOthMorePercPrev",

            // P5: Remuneration & Disclosures
            "remBodMaleNum", "remBodMaleMedian", "remBodFemNum", "remBodFemMedian",
            "remKmpMaleNum", "remKmpMaleMedian", "remKmpFemNum", "remKmpFemMedian",
            "remEmpMaleNum", "remEmpMaleMedian", "remEmpFemNum", "remEmpFemMedian",
            "remWorkMaleNum", "remWorkMaleMedian", "remWorkFemNum", "remWorkFemMedian",
            "remunerationNote", "grossWagesFemalePercCurr", "grossWagesFemalePercPrev", "grossWagesNote",
            "humanRightsFocalPoint", "humanRightsFocalDetails", "humanRightsGrievanceMechanism",

            // P5: Complaints
            "compShFiledCurr", "compShPendingCurr", "compShRemarksCurr", "compShFiledPrev", "compShPendingPrev", "compShRemarksPrev",
            "compDiscrimFiledCurr", "compDiscrimPendingCurr", "compDiscrimRemarksCurr", "compDiscrimFiledPrev", "compDiscrimPendingPrev", "compDiscrimRemarksPrev",
            "compChildFiledCurr", "compChildPendingCurr", "compChildRemarksCurr", "compChildFiledPrev", "compChildPendingPrev", "compChildRemarksPrev",
            "compForcedFiledCurr", "compForcedPendingCurr", "compForcedRemarksCurr", "compForcedFiledPrev", "compForcedPendingPrev", "compForcedRemarksPrev",
            "compWagesFiledCurr", "compWagesPendingCurr", "compWagesRemarksCurr", "compWagesFiledPrev", "compWagesPendingPrev", "compWagesRemarksPrev",
            "compOtherHrFiledCurr", "compOtherHrPendingCurr", "compOtherHrRemarksCurr", "compOtherHrFiledPrev", "compOtherHrPendingPrev", "compOtherHrRemarksPrev",

            // P5: POSH & Contracts
            "poshTotalCurr", "poshTotalPrev", "poshPercCurr", "poshPercPrev", "poshUpheldCurr", "poshUpheldPrev", "poshNote",
            "protectionMechanismsIntro", "humanRightsContracts", "humanRightsContractsDetails",
            "assessChildLabourPerc", "assessForcedLabourPerc", "assessSexualHarassmentPerc",
            "assessDiscriminationPerc", "assessWagesPerc", "assessOthersPerc", "assessmentsP5Note",
            "assessCorrectiveIntro",

            // P5: Leadership
            "p5LeadProcessModIntro", "p5LeadDueDiligenceScope", "p5LeadPremisesAccess",
            , "p5LeadValueChainCorrectiveActions","p5LeadVcAssessShPerc","p5LeadVcAssessDiscrimPerc",
                                                      "p5LeadVcAssessChildPerc",
                                                      "p5LeadVcAssessForcedPerc",
                                                      "p5LeadVcAssessWagesPerc",
                                                      "p5LeadVcAssessOthersPerc",
        ];

        // Apply all standard text/number fields to HTML
        fields.forEach(id => {
            if(document.getElementById(id)) {
                document.getElementById(id).value = data[id] || "";
            }
        });

        const p2NaCheckboxes = ["p2Lead1NA", "p2Lead2NA", "p2Lead3NA", "p2Lead4NA", "p2Lead5NA"];
            p2NaCheckboxes.forEach(id => {
                const el = document.getElementById(id);
                if (el) {
                    el.checked = (data[id] === true || data[id] === "true");
                    toggleNaSection(id.replace('NA', 'Container'), el.checked);
                }
            });

        // 3B. Fill FY Headers if saved previously
        if(document.getElementById("p2FyCurrent")) document.getElementById("p2FyCurrent").value = data.fyCurrent || "";
        if(document.getElementById("p2FyPrevious")) document.getElementById("p2FyPrevious").value = data.fyPrevious || "";

        // 3C. Handle Standalone Checkboxes
        if(document.getElementById("nonMonetaryNA")) {
            document.getElementById("nonMonetaryNA").checked = data.nonMonetaryNA || false;
        }


        // ==========================================================
        // 4. BATCH LOAD DYNAMIC TABLES (Arrays/Lists)
        // ==========================================================

        // --- Principle 1: Q1 Training Table ---
        const trainBody = document.querySelector("#trainingTable tbody");
        if(trainBody) {
            trainBody.innerHTML = "";
            if (data.trainingPrograms && data.trainingPrograms.length > 0) {
                data.trainingPrograms.forEach(tp => addTrainingRow(tp));
            } else {
                // Matches exact SEBI segments if empty
                addTrainingRow({ segment: "Board of Directors" });
                addTrainingRow({ segment: "Key Managerial Personnel" });
                addTrainingRow({ segment: "Employees other than BoD and KMPs" });
                addTrainingRow({ segment: "Workers" });
            }
        }

        // --- Principle 1: Q2 Legal Actions (Monetary & Non-Monetary Split) ---
        const monBody = document.querySelector("#monetaryTable tbody");
        const nonMonBody = document.querySelector("#nonMonetaryTable tbody");
        if(monBody) monBody.innerHTML = "";
        if(nonMonBody) nonMonBody.innerHTML = "";

        if (data.legalActions && data.legalActions.length > 0) {
            data.legalActions.forEach(la => {
                if (la.type === "Imprisonment" || la.type === "Punishment") {
                    addNonMonetaryRow(la);
                } else {
                    addMonetaryRow(la);
                }
            });
        } else {
            if(monBody) addMonetaryRow();
            if(nonMonBody) addNonMonetaryRow();
        }

        // --- Principle 1: Q3 Appeals Table ---
        const appealBody = document.querySelector("#appealTable tbody");
        if(appealBody) {
            appealBody.innerHTML = "";
            if (data.appealDetails && data.appealDetails.length > 0) {
                data.appealDetails.forEach(ad => addAppealRow(ad));
            } else {
                addAppealRow();
            }
        }

        // --- Principle 1: Leadership Table ---
        const leadBody = document.querySelector("#leadershipTable tbody");
        if(leadBody) {
            leadBody.innerHTML = "";
            if (data.leadershipAwarenessPrograms && data.leadershipAwarenessPrograms.length > 0) {
                data.leadershipAwarenessPrograms.forEach(p => addLeadershipRow(p));
            } else {
                // Default Suggestions
                addLeadershipRow({ topic: "Environment, Health and Safety" });
            }
        }

        // --- Principle 2: Leadership LCA Tables ---



        const lcaBody = document.querySelector("#lcaTable tbody");
        if(lcaBody) {
            lcaBody.innerHTML = "";
            if (data.lcaEntries && data.lcaEntries.length > 0) {
                data.lcaEntries.forEach(row => addLcaRow(row));
            } else { addLcaRow(); }
        }

        const riskBody = document.querySelector("#riskTable tbody");
        if(riskBody) {
            riskBody.innerHTML = "";
            if (data.lcaRisks && data.lcaRisks.length > 0) {
                document.querySelector('input[name="riskMode"][value="table"]').checked = true;
                data.lcaRisks.forEach(r => addRiskRow(r));
            } else {
                document.querySelector('input[name="riskMode"][value="note"]').checked = true;
                addRiskRow(); // Hidden row just in case
            }
            toggleRiskMode();
        }

        const recBody = document.querySelector("#recycledTable tbody");
        if(recBody) {
            recBody.innerHTML = "";
            if (data.recycledInputs && data.recycledInputs.length > 0) {
                data.recycledInputs.forEach(row => addRecycledRow(row));
            } else { addRecycledRow(); }
        }

        const recPercBody = document.querySelector("#reclaimedPercTable tbody");
        if(recPercBody) {
            recPercBody.innerHTML = "";
            if (data.reclaimedPercentages && data.reclaimedPercentages.length > 0) {
                data.reclaimedPercentages.forEach(row => addReclaimedPercRow(row));
            } else { addReclaimedPercRow(); }
        }

        // --- Principle 3: Well-Being & Retirement Tables ---
        const empWbBody = document.querySelector("#empWellBeingTable tbody");
        if(empWbBody) {
            empWbBody.innerHTML = "";
            if (data.employeeWellBeing && data.employeeWellBeing.length > 0) {
                data.employeeWellBeing.forEach(row => addWellBeingRow("#empWellBeingTable", row));
            } else { addDefaultWellBeingRows("#empWellBeingTable"); }
        }

        const workWbBody = document.querySelector("#workWellBeingTable tbody");
        if(workWbBody) {
            workWbBody.innerHTML = "";
            if (data.workerWellBeing && data.workerWellBeing.length > 0) {
                data.workerWellBeing.forEach(row => addWellBeingRow("#workWellBeingTable", row));
            } else { addDefaultWellBeingRows("#workWellBeingTable"); }
        }

        const retBody = document.querySelector("#retirementTable tbody");
        if(retBody) {
            retBody.innerHTML = "";
            if (data.retirementBenefits && data.retirementBenefits.length > 0) {
                data.retirementBenefits.forEach(row => addRetirementRow(row));
            } else {
                addRetirementRow({ benefits: "PF" });
                addRetirementRow({ benefits: "Gratuity" });
                addRetirementRow({ benefits: "ESI" });
            }
        }

        // --- Principle 3: Safety Measures & Corrective Actions ---
        const smContainer = document.getElementById("safetyMeasuresContainer");
        if(smContainer) {
            smContainer.innerHTML = "";
            if (data.safetyMeasures && data.safetyMeasures.length > 0) {
                data.safetyMeasures.forEach(m => addSafetyMeasureRow(m));
            } else { addSafetyMeasureRow({ heading: "Build safety leadership capability" }); }
        }

        const vcContainer = document.getElementById("vcActionsContainer");
        if(vcContainer) {
            vcContainer.innerHTML = "";
            if (data.vcCorrectiveActions && data.vcCorrectiveActions.length > 0) {
                data.vcCorrectiveActions.forEach(a => addVcActionRow(a));
            } else {
                addVcActionRow({ actionText: "Incorporating safety requirements in contracts..." });
            }
        }

        // --- Principle 4: Stakeholders & Actions Lists ---
        const p4Body = document.getElementById("p4PointsContainer");
        if(p4Body) {
            p4Body.innerHTML = "";
            if (data.principle4Q1Points && data.principle4Q1Points.length > 0) {
                data.principle4Q1Points.forEach(pt => addP4PointRow(pt));
            } else {
                addP4PointRow("Global Reporting Initiative");
            }
        }

        const stakeBody = document.querySelector("#stakeholderTable tbody");
        if(stakeBody) {
            stakeBody.innerHTML = "";
            if (data.stakeholderEngagements && data.stakeholderEngagements.length > 0) {
                data.stakeholderEngagements.forEach(row => addStakeholderRow(row));
            } else {
                addStakeholderRow({ groupName: "Investors" });
                addStakeholderRow({ groupName: "Community Representatives" });
            }
        }

        const vgBody = document.getElementById("vulnerableActionsContainer");
        if(vgBody) {
            vgBody.innerHTML = "";
            if (data.vulnerableGroupActions && data.vulnerableGroupActions.length > 0) {
                data.vulnerableGroupActions.forEach(action => addVulnerableActionRow(action));
            } else {
                addVulnerableActionRow("Ensuring safety in operating sites...");
            }
        }

        // --- Principle 5: Protection, Assessments & Process Lists ---
        const protBody = document.getElementById("protectionMechanismsContainer");
        if(protBody) {
            protBody.innerHTML = "";
            if (data.protectionMechanismsList && data.protectionMechanismsList.length > 0) {
                data.protectionMechanismsList.forEach(m => addProtectionMechanismRow(m));
            } else {
                addProtectionMechanismRow("As part of POSH Policy, identity is protected...");
            }
        }

        const acBody = document.getElementById("assessCorrectiveContainer");
        if(acBody) {
            acBody.innerHTML = "";
            if (data.assessCorrectiveActions && data.assessCorrectiveActions.length > 0) {
                data.assessCorrectiveActions.forEach(a => addAssessCorrectiveRow(a));
            } else {
                addAssessCorrectiveRow("Extending training and capability building...");
            }
        }

        const modBody = document.getElementById("p5LeadModContainer");
        if(modBody) {
            modBody.innerHTML = "";
            if (data.p5LeadProcessModList && data.p5LeadProcessModList.length > 0) {
                data.p5LeadProcessModList.forEach(t => addP5LeadModRow(t));
            } else {
                addP5LeadModRow("Statutory rights of contract employees are addressed...");
            }
        }

        const issueBody = document.getElementById("p5LeadIssuesContainer");
        if(issueBody) {
            issueBody.innerHTML = "";
            if (data.p5LeadDueDiligenceIssues && data.p5LeadDueDiligenceIssues.length > 0) {
                data.p5LeadDueDiligenceIssues.forEach(t => addP5LeadIssueRow(t));
            } else {
                addP5LeadIssueRow("Child Labour");
                addP5LeadIssueRow("Forced/Involuntary Labour");
            }
        }

        const holderBody = document.getElementById("p5LeadHoldersContainer");
        if(holderBody) {
            holderBody.innerHTML = "";
            if (data.p5LeadDueDiligenceHolders && data.p5LeadDueDiligenceHolders.length > 0) {
                data.p5LeadDueDiligenceHolders.forEach(t => addP5LeadHolderRow(t));
            } else {
                addP5LeadHolderRow("Employees");
                addP5LeadHolderRow("Contract Workforce");
            }
        }
    })
    .catch(err => console.error("Error loading Part 3 data", err));
});

// --- ROW HELPERS ---
function addTrainingRow(data = null) {
    const tbody = document.querySelector("#trainingTable tbody");
    const d = data || {};
    const row = `
        <tr>
            <td><input type="text" class="tp-seg" value="${d.segment || ''}" placeholder="e.g. Board of Directors"></td>
            <td><input type="text" class="tp-num" value="${d.totalPrograms || ''}" placeholder="0"></td>
            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;">
                    <button type="button" class="btn-ai-mini" onclick="generateTrainingTopicsAI(this)" style="background:none; border:none; color:#2563eb; font-size:11px; cursor:pointer; font-weight:bold;">✨ AI Expand</button>
                </div>
                <textarea class="tp-top" rows="3" style="width:100%; font-size:12px;" placeholder="Keywords (e.g. 'Safety, POSH, Ethics')...">${d.topicsCovered || ''}</textarea>
            </td>
            <td><input type="text" class="tp-perc" value="${d.percentageCovered || ''}" placeholder="%"></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- ADD MONETARY ROW ---
function addMonetaryRow(data = null) {
    const tbody = document.querySelector("#monetaryTable tbody");
    const d = data || {};
    const types = ["Penalty/Fine", "Settlement", "Compounding fee"];
    let typeOptions = types.map(t => `<option value="${t}" ${d.type === t ? 'selected' : ''}>${t}</option>`).join('');

    const row = `
        <tr>
            <td><select class="la-type">${typeOptions}</select></td>
            <td><input type="text" class="la-prin" value="${d.principle || ''}" placeholder="e.g. P1"></td>
            <td><input type="text" class="la-agency" value="${d.agency || ''}" placeholder="Agency"></td>
            <td><input type="text" class="la-amt" value="${d.amount || ''}" placeholder="Amount"></td>
            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;">
                    <button type="button" class="btn-ai-mini" onclick="generateLegalBriefAI(this)" style="background:none; border:none; color:#2563eb; font-size:11px; cursor:pointer; font-weight:bold;">✨ AI Expand</button>
                </div>
                <textarea class="la-brief" rows="3" style="width:100%; font-size:12px;" placeholder="Keywords (e.g. 'Late filing')...">${d.brief || ''}</textarea>
            </td>
            <td>
                <select class="la-app">
                    <option value="No" ${d.appeal === 'No' ? 'selected' : ''}>No</option>
                    <option value="Yes" ${d.appeal === 'Yes' ? 'selected' : ''}>Yes</option>
                    <option value="N/A" ${d.appeal === 'N/A' ? 'selected' : ''}>N/A</option>
                </select>
            </td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- ADD NON-MONETARY ROW ---
function addNonMonetaryRow(data = null) {
    const tbody = document.querySelector("#nonMonetaryTable tbody");
    const d = data || {};
    const types = ["Imprisonment", "Punishment"];
    let typeOptions = types.map(t => `<option value="${t}" ${d.type === t ? 'selected' : ''}>${t}</option>`).join('');

    const row = `
        <tr>
            <td><select class="la-type">${typeOptions}</select></td>
            <td><input type="text" class="la-prin" value="${d.principle || ''}" placeholder="e.g. P1"></td>
            <td><input type="text" class="la-agency" value="${d.agency || ''}" placeholder="Agency"></td>
            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;">
                    <button type="button" class="btn-ai-mini" onclick="generateLegalBriefAI(this)" style="background:none; border:none; color:#2563eb; font-size:11px; cursor:pointer; font-weight:bold;">✨ AI Expand</button>
                </div>
                <textarea class="la-brief" rows="3" style="width:100%; font-size:12px;" placeholder="Keywords (e.g. 'Warning letter')...">${d.brief || ''}</textarea>
            </td>
            <td>
                <select class="la-app">
                    <option value="No" ${d.appeal === 'No' ? 'selected' : ''}>No</option>
                    <option value="Yes" ${d.appeal === 'Yes' ? 'selected' : ''}>Yes</option>
                    <option value="N/A" ${d.appeal === 'N/A' ? 'selected' : ''}>N/A</option>
                </select>
            </td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

//helper
function getVal(id) {
    const el = document.getElementById(id);
    if (!el) {
        console.warn(`Missing HTML Element: ${id}`); // Warns you in console but continues execution
        return "";
    }
    return el.value;
}

// --- ROW MANAGEMENT (Q3) ---
function addAppealRow(data = null) {
    const tbody = document.querySelector("#appealTable tbody");
    const d = data || {};

    const row = `
        <tr>
            <td><textarea class="ap-case" rows="2" style="width:100%" placeholder="Details...">${d.caseDetails || ''}</textarea></td>
            <td><input type="text" class="ap-agency" value="${d.agencyName || ''}" placeholder="Agency Name"></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- TOGGLE FUNCTION ---
function toggleLeadershipMode() {
    const isTable = document.querySelector('input[name="leadershipMode"][value="table"]').checked;

    // Show/Hide Columns
    document.querySelectorAll(".li-table-col").forEach(el => el.style.display = isTable ? "table-cell" : "none");
    document.querySelectorAll(".li-text-col").forEach(el => el.style.display = isTable ? "none" : "table-cell");
}

// --- ROW MANAGEMENT ---
function addLeadershipRow(data = null) {
    const tbody = document.querySelector("#leadershipTable tbody");
    const d = data || {};

    const row = `
        <tr>
            <td><input type="text" class="li-count" value="${d.totalCount || ''}" placeholder="0"></td>
            <td><input type="text" class="li-topic" value="${d.topic || ''}" placeholder="e.g. Safety"></td>
            <td><input type="text" class="li-perc" value="${d.percentage || ''}" placeholder="%"></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addLcaRow(data = null) {
    const tbody = document.querySelector("#lcaTable tbody");
    const d = data || {};

    const row = `
        <tr>
            <td><input type="text" class="lca-nic" value="${d.nicCode || ''}" placeholder="Code"></td>
            <td><input type="text" class="lca-name" value="${d.productName || ''}" placeholder="Product"></td>
            <td><input type="text" class="lca-turn" value="${d.turnoverPercentage || ''}" placeholder="%"></td>
            <td><input type="text" class="lca-bound" value="${d.boundary || ''}" placeholder="e.g. Cradle-to-gate"></td>
            <td>
                <select class="lca-agency">
                    <option value="Yes" ${d.independentAgency === 'Yes' ? 'selected' : ''}>Yes</option>
                    <option value="No" ${d.independentAgency === 'No' ? 'selected' : ''}>No</option>
                </select>
            </td>
            <td><textarea class="lca-res" rows="2" placeholder="Link...">${d.publicDomainResult || ''}</textarea></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- TOGGLE FUNCTION ---
function toggleRiskMode() {
    const isTable = document.querySelector('input[name="riskMode"][value="table"]').checked;
    document.getElementById("riskTableContainer").style.display = isTable ? "block" : "none";
    document.getElementById("riskNoteContainer").style.display = isTable ? "none" : "block";
}

// --- ROW MANAGEMENT ---
function addRiskRow(data = null) {
    const tbody = document.querySelector("#riskTable tbody");
    const d = data || {};

    const row = `
        <tr>
            <td><input type="text" class="rk-name" value="${d.productName || ''}" placeholder="Product Name"></td>
            <td><textarea class="rk-desc" rows="2" style="width:100%">${d.riskDescription || ''}</textarea></td>
            <td><textarea class="rk-act" rows="2" style="width:100%">${d.actionTaken || ''}</textarea></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- ROW MANAGEMENT ---
function addRecycledRow(data = null) {
    const tbody = document.querySelector("#recycledTable tbody");
    const d = data || {};

    const row = `
        <tr>
            <td><input type="text" class="rec-mat" value="${d.materialName || ''}" placeholder="e.g. Scrap, Slag"></td>
            <td><input type="text" class="rec-cur" value="${d.currentFyPercentage || ''}" placeholder="%"></td>
            <td><input type="text" class="rec-prev" value="${d.previousFyPercentage || ''}" placeholder="%"></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- ROW FUNCTION (Q5) ---
function addReclaimedPercRow(data = null) {
    const tbody = document.querySelector("#reclaimedPercTable tbody");
    const d = data || {};
    const row = `
        <tr>
            <td><input type="text" class="rp-cat" value="${d.category || ''}" placeholder="Category"></td>
            <td><input type="text" class="rp-perc" value="${d.percentage || ''}" placeholder="%"></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- ROW MANAGEMENT ---
function addDefaultWellBeingRows(tableId) {
    // 3 Rows for Permanent
    addWellBeingRow(tableId, { category: "Permanent - Male" });
    addWellBeingRow(tableId, { category: "Permanent - Female" });
    addWellBeingRow(tableId, { category: "Permanent - Total" });
    // 3 Rows for Temporary
    addWellBeingRow(tableId, { category: "Temporary - Male" });
    addWellBeingRow(tableId, { category: "Temporary - Female" });
    addWellBeingRow(tableId, { category: "Temporary - Total" });
}

// --- ROW MANAGEMENT (Updated with auto-calc triggers and styling) ---
function addWellBeingRow(tableId, d = {}) {
    const tbody = document.querySelector(tableId + " tbody");

    // Bold the 'Total' rows for better UI readability
    const isTotalRow = d.category && d.category.includes("Total");
    const rowStyle = isTotalRow ? "font-weight: bold; background-color: #f8fafc;" : "";

    const row = `
        <tr style="${rowStyle}">
            <td><input type="text" class="wb-cat" value="${d.category || ''}" style="width:120px; border:none; background:transparent; font-weight:inherit;" readonly></td>

            <td><input type="number" class="wb-tot" value="${d.totalA || ''}" oninput="calcWellBeingRow(this)" style="width:60px;" placeholder="0"></td>

            <td><input type="number" class="wb-h-no" value="${d.healthNo || ''}" oninput="calcWellBeingRow(this)" style="width:50px;" placeholder="0"></td>
            <td><input type="text" class="wb-h-pc" value="${d.healthPerc || ''}" readonly style="width:45px; background:#f1f5f9; border:none; text-align:center;" placeholder="%"></td>

            <td><input type="number" class="wb-a-no" value="${d.accidentNo || ''}" oninput="calcWellBeingRow(this)" style="width:50px;" placeholder="0"></td>
            <td><input type="text" class="wb-a-pc" value="${d.accidentPerc || ''}" readonly style="width:45px; background:#f1f5f9; border:none; text-align:center;" placeholder="%"></td>

            <td><input type="number" class="wb-m-no" value="${d.maternityNo || ''}" oninput="calcWellBeingRow(this)" style="width:50px;" placeholder="0"></td>
            <td><input type="text" class="wb-m-pc" value="${d.maternityPerc || ''}" readonly style="width:45px; background:#f1f5f9; border:none; text-align:center;" placeholder="%"></td>

            <td><input type="number" class="wb-p-no" value="${d.paternityNo || ''}" oninput="calcWellBeingRow(this)" style="width:50px;" placeholder="0"></td>
            <td><input type="text" class="wb-p-pc" value="${d.paternityPerc || ''}" readonly style="width:45px; background:#f1f5f9; border:none; text-align:center;" placeholder="%"></td>

            <td><input type="number" class="wb-d-no" value="${d.daycareNo || ''}" oninput="calcWellBeingRow(this)" style="width:50px;" placeholder="0"></td>
            <td><input type="text" class="wb-d-pc" value="${d.daycarePerc || ''}" readonly style="width:45px; background:#f1f5f9; border:none; text-align:center;" placeholder="%"></td>
        </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- HELPER: Auto-Calculate Well-being Percentages ---
function calcWellBeingRow(inputElement) {
    const row = inputElement.closest("tr");

    // Get the Total (A) value for this specific row
    const totalA = parseFloat(row.querySelector(".wb-tot").value) || 0;

    // Helper to calculate and format percentage safely
    const calcPerc = (numClass, percClass) => {
        const num = parseFloat(row.querySelector(numClass).value) || 0;
        const percInput = row.querySelector(percClass);
        if (totalA > 0 && num > 0) {
            percInput.value = ((num / totalA) * 100).toFixed(1) + "%";
        } else {
            percInput.value = "0%";
        }
    };

    // Calculate all 5 categories for this row
    calcPerc(".wb-h-no", ".wb-h-pc"); // Health
    calcPerc(".wb-a-no", ".wb-a-pc"); // Accident
    calcPerc(".wb-m-no", ".wb-m-pc"); // Maternity
    calcPerc(".wb-p-no", ".wb-p-pc"); // Paternity
    calcPerc(".wb-d-no", ".wb-d-pc"); // Daycare
}

// --- ROW MANAGEMENT ---
function addRetirementRow(data = null) {
    const tbody = document.querySelector("#retirementTable tbody");
    const d = data || {};

    const row = `
        <tr>
            <td><input type="text" class="rb-name" value="${d.benefits || ''}" placeholder="Name"></td>

            <td><input type="text" class="rb-ce" value="${d.currEmpCovered || ''}" style="width:50px;"></td>
            <td><input type="text" class="rb-cw" value="${d.currWorkCovered || ''}" style="width:50px;"></td>
            <td><input type="text" class="rb-cd" value="${d.currDeducted || ''}" style="width:60px;"></td>

            <td><input type="text" class="rb-pe" value="${d.prevEmpCovered || ''}" style="width:50px;"></td>
            <td><input type="text" class="rb-pw" value="${d.prevWorkCovered || ''}" style="width:50px;"></td>
            <td><input type="text" class="rb-pd" value="${d.prevDeducted || ''}" style="width:60px;"></td>

            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- ROW MANAGEMENT: Q2 RETIREMENT BENEFITS ---
function addRetirementRow(data = null) {
    const tbody = document.querySelector("#retirementTable tbody");
    const d = data || {};

    const selectOptions = ["Y", "N", "N.A."];
    const currSelect = selectOptions.map(o => `<option value="${o}" ${d.currDeducted === o ? 'selected' : ''}>${o}</option>`).join('');
    const prevSelect = selectOptions.map(o => `<option value="${o}" ${d.prevDeducted === o ? 'selected' : ''}>${o}</option>`).join('');

    const row = `
        <tr>
            <td><input type="text" class="rb-name" value="${d.benefits || ''}" placeholder="e.g. PF, Gratuity, ESI"></td>

            <td><input type="text" class="rb-ce" value="${d.currEmpCovered || ''}" placeholder="%"></td>
            <td><input type="text" class="rb-cw" value="${d.currWorkCovered || ''}" placeholder="%"></td>
            <td><select class="rb-cd">${currSelect}</select></td>

            <td><input type="text" class="rb-pe" value="${d.prevEmpCovered || ''}" placeholder="%"></td>
            <td><input type="text" class="rb-pw" value="${d.prevWorkCovered || ''}" placeholder="%"></td>
            <td><select class="rb-pd">${prevSelect}</select></td>

            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- HELPER: Calculate Union Percentages (Row-wise) ---
function calcUnionRow(inputElement) {
    // Get the parent row (<tr>)
    const row = inputElement.closest("tr");

    // Get all input boxes in this specific row
    // Index mapping based on HTML:
    // 0: Curr Total, 1: Curr Union, 2: Curr % (ReadOnly)
    // 3: Prev Total, 4: Prev Union, 5: Prev % (ReadOnly)
    const inputs = row.querySelectorAll("input");

    if (inputs.length < 6) return; // Safety check

    // --- Current FY Calculation ---
    const currTotal = parseFloat(inputs[0].value) || 0;
    const currUnion = parseFloat(inputs[1].value) || 0;

    let currPerc = "0%";
    if (currTotal > 0) {
        currPerc = ((currUnion / currTotal) * 100).toFixed(1) + "%";
    }
    inputs[2].value = currPerc;

    // --- Previous FY Calculation ---
    const prevTotal = parseFloat(inputs[3].value) || 0;
    const prevUnion = parseFloat(inputs[4].value) || 0;

    let prevPerc = "0%";
    if (prevTotal > 0) {
        prevPerc = ((prevUnion / prevTotal) * 100).toFixed(1) + "%";
    }
    inputs[5].value = prevPerc;
}

function calcTrainRow(input) {
    const row = input.closest("tr");
    const inputs = row.querySelectorAll("input");

    // Current (Indices 0-4)
    const cTot = parseFloat(inputs[0].value) || 0;
    const cH = parseFloat(inputs[1].value) || 0;
    const cS = parseFloat(inputs[3].value) || 0;

    inputs[2].value = cTot > 0 ? ((cH/cTot)*100).toFixed(0) + "%" : "0%";
    inputs[4].value = cTot > 0 ? ((cS/cTot)*100).toFixed(0) + "%" : "0%";

    // Previous (Indices 5-9)
    const pTot = parseFloat(inputs[5].value) || 0;
    const pH = parseFloat(inputs[6].value) || 0;
    const pS = parseFloat(inputs[8].value) || 0;

    inputs[7].value = pTot > 0 ? ((pH/pTot)*100).toFixed(0) + "%" : "0%";
    inputs[9].value = pTot > 0 ? ((pS/pTot)*100).toFixed(0) + "%" : "0%";
}

// --- 2. CALCULATION LOGIC ---
// Trigger this using: oninput="calcTrainRow(this)" in HTML
function calcTrainRow(input) {
    const row = input.closest("tr");
    const inputs = row.querySelectorAll("input");

    // Indices:
    // 0: Total | 1: Health No | 2: Health % (RO) | 3: Skill No | 4: Skill % (RO)
    // 5: Total | 6: Health No | 7: Health % (RO) | 8: Skill No | 9: Skill % (RO)

    // Current FY
    const cTotal = parseFloat(inputs[0].value) || 0;
    const cHealth = parseFloat(inputs[1].value) || 0;
    const cSkill = parseFloat(inputs[3].value) || 0;

    inputs[2].value = cTotal > 0 ? ((cHealth / cTotal) * 100).toFixed(1) + "%" : "0%";
    inputs[4].value = cTotal > 0 ? ((cSkill / cTotal) * 100).toFixed(1) + "%" : "0%";

    // Previous FY
    const pTotal = parseFloat(inputs[5].value) || 0;
    const pHealth = parseFloat(inputs[6].value) || 0;
    const pSkill = parseFloat(inputs[8].value) || 0;

    inputs[7].value = pTotal > 0 ? ((pHealth / pTotal) * 100).toFixed(1) + "%" : "0%";
    inputs[9].value = pTotal > 0 ? ((pSkill / pTotal) * 100).toFixed(1) + "%" : "0%";
}

// --- HELPER: Calc Review % ---
function calcReviewRow(input) {
    const row = input.closest("tr");
    const inputs = row.querySelectorAll("input");

    // Indices based on HTML structure:
    // 0: Curr Total | 1: Curr Covered | 2: Curr % (Readonly)
    // 3: Prev Total | 4: Prev Covered | 5: Prev % (Readonly)

    // Current FY
    const cTot = parseFloat(inputs[0].value) || 0;
    const cCov = parseFloat(inputs[1].value) || 0;
    inputs[2].value = cTot > 0 ? ((cCov / cTot) * 100).toFixed(0) + "%" : "0%";

    // Previous FY
    const pTot = parseFloat(inputs[3].value) || 0;
    const pCov = parseFloat(inputs[4].value) || 0;
    inputs[5].value = pTot > 0 ? ((pCov / pTot) * 100).toFixed(0) + "%" : "0%";
}

// --- ROW MANAGEMENT ---
function addSafetyMeasureRow(data = null) {
    const container = document.getElementById("safetyMeasuresContainer");
    const d = data || {};

    const div = document.createElement("div");
    div.className = "safety-measure-item";
    div.style.marginBottom = "15px";
    div.style.padding = "15px";
    div.style.background = "#f9f9f9";
    div.style.border = "1px solid #ddd";
    div.style.borderRadius = "6px";

    div.innerHTML = `
        <div style="display:flex; justify-content:space-between; margin-bottom:10px;">
            <label style="font-weight:bold; color:#108a55;">Strategy Heading</label>
            <span style="color:red; cursor:pointer; font-weight:bold;" onclick="this.parentElement.parentElement.remove()">Remove</span>
        </div>
        <input type="text" class="sm-head" value="${d.heading || ''}" placeholder="e.g. Improve competency..." style="width:100%; margin-bottom:10px;">

        <label style="font-size:12px;">Description / Details</label>
        <textarea class="sm-desc" rows="3" style="width:100%" placeholder="Details...">${d.description || ''}</textarea>
    `;
    container.appendChild(div);
}
//helper princple 3 leadership 6
function addVcActionRow(data = null) {
    const container = document.getElementById("vcActionsContainer");
    const d = data || {};

    const div = document.createElement("div");
    div.className = "vc-action-item";
    div.style.marginBottom = "10px";
    div.style.display = "flex";
    div.style.gap = "10px";
    div.style.alignItems = "start";

    div.innerHTML = `
        <span style="font-size:18px; color:#108a55;">»</span>
        <textarea class="vc-act-text" rows="2" style="width:100%" placeholder="Describe action...">${d.actionText || ''}</textarea>
        <span style="color:red; cursor:pointer; font-weight:bold; margin-top:5px;" onclick="this.parentElement.remove()">X</span>
    `;
    container.appendChild(div);
}
// ==========================================
// Q7: AUTO-CALCULATE UNION MEMBERSHIP
// ==========================================
function calcUnionRow(inputElem) {
    const row = inputElem.closest('tr');
    // Get all 6 inputs in this specific row
    const inputs = row.querySelectorAll('input');
    if (inputs.length < 6) return;

    // --- Current FY ---
    const totalA = parseFloat(inputs[0].value) || 0;
    const unionB = parseFloat(inputs[1].value) || 0;
    const percCurr = inputs[2]; // The readonly % field

    if (totalA > 0) {
        percCurr.value = ((unionB / totalA) * 100).toFixed(1) + "%";
    } else {
        percCurr.value = "";
    }

    // --- Previous FY ---
    const totalC = parseFloat(inputs[3].value) || 0;
    const unionD = parseFloat(inputs[4].value) || 0;
    const percPrev = inputs[5]; // The readonly % field

    if (totalC > 0) {
        percPrev.value = ((unionD / totalC) * 100).toFixed(1) + "%";
    } else {
        percPrev.value = "";
    }
}

// ==========================================
// Q8: AUTO-CALCULATE TRAINING DETAILS
// ==========================================
function calcTrainRow(inputElem) {
    const row = inputElem.closest('tr');
    // Get all 10 inputs in this specific row
    const inputs = row.querySelectorAll('input');
    if (inputs.length < 10) return;

    // --- Current FY ---
    const totalA = parseFloat(inputs[0].value) || 0;
    const healthB = parseFloat(inputs[1].value) || 0;
    const healthPerc = inputs[2]; // Readonly
    const skillC = parseFloat(inputs[3].value) || 0;
    const skillPerc = inputs[4]; // Readonly

    if (totalA > 0) {
        healthPerc.value = ((healthB / totalA) * 100).toFixed(1) + "%";
        skillPerc.value = ((skillC / totalA) * 100).toFixed(1) + "%";
    } else {
        healthPerc.value = "";
        skillPerc.value = "";
    }

    // --- Previous FY ---
    const totalD = parseFloat(inputs[5].value) || 0;
    const healthE = parseFloat(inputs[6].value) || 0;
    const healthPercPrev = inputs[7]; // Readonly
    const skillF = parseFloat(inputs[8].value) || 0;
    const skillPercPrev = inputs[9]; // Readonly

    if (totalD > 0) {
        healthPercPrev.value = ((healthE / totalD) * 100).toFixed(1) + "%";
        skillPercPrev.value = ((skillF / totalD) * 100).toFixed(1) + "%";
    } else {
        healthPercPrev.value = "";
        skillPercPrev.value = "";
    }
}
// --- ROW MANAGEMENT --- p4 essential 1
function addP4PointRow(val = "") {
    const container = document.getElementById("p4PointsContainer");
    const div = document.createElement("div");
    div.className = "p4-point-row";
    div.style.display = "flex";
    div.style.gap = "10px";
    div.style.marginBottom = "5px";

    div.innerHTML = `
        <span style="font-weight:bold; color:#108a55;">•</span>
        <input type="text" class="p4-pt-input" value="${val}" style="width:100%" placeholder="Standard Name">
        <span style="color:red; cursor:pointer; font-weight:bold;" onclick="this.parentElement.remove()">X</span>
    `;
    container.appendChild(div);
}

// --- ROW MANAGEMENT --- p4 essential 2
function addStakeholderRow(data = null) {
    const tbody = document.querySelector("#stakeholderTable tbody");
    const d = data || {};

    const row = `
        <tr>
            <td><input type="text" class="st-name" value="${d.groupName || ''}" placeholder="Group Name"></td>
            <td>
                <select class="st-vul">
                    <option value="No" ${d.isVulnerable === 'No' ? 'selected' : ''}>No</option>
                    <option value="Yes" ${d.isVulnerable === 'Yes' ? 'selected' : ''}>Yes</option>
                </select>
            </td>
            <td><textarea class="st-chan" rows="4" style="width:100%" placeholder="1. Email\n2. Meetings...">${d.channels || ''}</textarea></td>
            <td><textarea class="st-freq" rows="4" style="width:100%" placeholder="Quarterly...">${d.frequency || ''}</textarea></td>
            <td><textarea class="st-purp" rows="4" style="width:100%" placeholder="1. Key topics...">${d.purpose || ''}</textarea></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- ROW MANAGEMENT --- P4 LEADERSHIP 4
function addVulnerableActionRow(text = "") {
    const container = document.getElementById("vulnerableActionsContainer");

    const div = document.createElement("div");
    div.className = "vul-act-row";
    div.style.display = "flex";
    div.style.gap = "10px";
    div.style.marginBottom = "8px";
    div.style.alignItems = "center";

    div.innerHTML = `
        <span style="font-weight:bold; color:#108a55;">•</span>
        <input type="text" class="vul-act-input" value="${text}" style="width:100%" placeholder="Action taken...">
        <span style="color:red; cursor:pointer; font-weight:bold;" onclick="this.parentElement.remove()">X</span>
    `;
    container.appendChild(div);
}

//princple 5 essetnial 1 helpers
// --- HELPER: Calc P5 Row & Grand Totals ---
function calcP5Row() {
    // 1. Calculate Individual Rows
    calcPercForId("hrEmpPerm");
    calcPercForId("hrEmpTemp");
    calcPercForId("hrWorkPerm");
    calcPercForId("hrWorkTemp");

    // 2. Calculate Vertical Sums (Employees)
    sumP5Vertical("hrEmpPerm", "hrEmpTemp", "hrEmpGrand");

    // 3. Calculate Vertical Sums (Workers)
    sumP5Vertical("hrWorkPerm", "hrWorkTemp", "hrWorkGrand");
}

function calcPercForId(prefix) {
    // Curr
    const cTot = parseFloat(document.getElementById(prefix+"TotalCurr").value) || 0;
    const cCov = parseFloat(document.getElementById(prefix+"CovCurr").value) || 0;
    document.getElementById(prefix+"PercCurr").value = cTot > 0 ? ((cCov/cTot)*100).toFixed(0)+"%" : "0%";

    // Prev
    const pTot = parseFloat(document.getElementById(prefix+"TotalPrev").value) || 0;
    const pCov = parseFloat(document.getElementById(prefix+"CovPrev").value) || 0;
    document.getElementById(prefix+"PercPrev").value = pTot > 0 ? ((pCov/pTot)*100).toFixed(0)+"%" : "0%";
}

function sumP5Vertical(r1, r2, res) {
    // Curr
    const cTot = (parseFloat(document.getElementById(r1+"TotalCurr").value)||0) + (parseFloat(document.getElementById(r2+"TotalCurr").value)||0);
    const cCov = (parseFloat(document.getElementById(r1+"CovCurr").value)||0) + (parseFloat(document.getElementById(r2+"CovCurr").value)||0);
    document.getElementById(res+"TotalCurr").value = cTot;
    document.getElementById(res+"CovCurr").value = cCov;
    document.getElementById(res+"PercCurr").value = cTot > 0 ? ((cCov/cTot)*100).toFixed(0)+"%" : "0%";

    // Prev
    const pTot = (parseFloat(document.getElementById(r1+"TotalPrev").value)||0) + (parseFloat(document.getElementById(r2+"TotalPrev").value)||0);
    const pCov = (parseFloat(document.getElementById(r1+"CovPrev").value)||0) + (parseFloat(document.getElementById(r2+"CovPrev").value)||0);
    document.getElementById(res+"TotalPrev").value = pTot;
    document.getElementById(res+"CovPrev").value = pCov;
    document.getElementById(res+"PercPrev").value = pTot > 0 ? ((pCov/pTot)*100).toFixed(0)+"%" : "0%";
}

// --- HELPER: Calc Minimum Wage Row ---princple 5 essential 2
function calcMwRow(input) {
    const row = input.closest("tr");
    const inputs = row.querySelectorAll("input");

    // Indices:
    // 0: Curr Total | 1: Curr Eq No | 2: Curr Eq % | 3: Curr More No | 4: Curr More %
    // 5: Prev Total | 6: Prev Eq No | 7: Prev Eq % | 8: Prev More No | 9: Prev More %

    // Current FY
    const cTot = parseFloat(inputs[0].value) || 0;
    const cEq = parseFloat(inputs[1].value) || 0;
    const cMore = parseFloat(inputs[3].value) || 0;

    inputs[2].value = cTot > 0 ? ((cEq/cTot)*100).toFixed(0) + "%" : "0%";
    inputs[4].value = cTot > 0 ? ((cMore/cTot)*100).toFixed(0) + "%" : "0%";

    // Previous FY
    const pTot = parseFloat(inputs[5].value) || 0;
    const pEq = parseFloat(inputs[6].value) || 0;
    const pMore = parseFloat(inputs[8].value) || 0;

    inputs[7].value = pTot > 0 ? ((pEq/pTot)*100).toFixed(0) + "%" : "0%";
    inputs[9].value = pTot > 0 ? ((pMore/pTot)*100).toFixed(0) + "%" : "0%";
}

// --- ROW MANAGEMENT ---
function addProtectionMechanismRow(text = "") {
    const container = document.getElementById("protectionMechanismsContainer");

    const div = document.createElement("div");
    div.className = "prot-mech-row";
    div.style.display = "flex";
    div.style.gap = "10px";
    div.style.marginBottom = "8px";
    div.style.alignItems = "center";

    div.innerHTML = `
        <span style="font-weight:bold; color:#108a55;">•</span>
        <input type="text" class="prot-mech-input" value="${text}" style="width:100%" placeholder="Mechanism details...">
        <span style="color:red; cursor:pointer; font-weight:bold;" onclick="this.parentElement.remove()">X</span>
    `;
    container.appendChild(div);
}

// --- ROW MANAGEMENT ---
function addAssessCorrectiveRow(text = "") {
    const container = document.getElementById("assessCorrectiveContainer");

    const div = document.createElement("div");
    div.className = "ac-action-row";
    div.style.display = "flex";
    div.style.gap = "10px";
    div.style.marginBottom = "8px";
    div.style.alignItems = "center";

    div.innerHTML = `
        <span style="font-weight:bold; color:#108a55;">•</span>
        <input type="text" class="ac-action-input" value="${text}" style="width:100%" placeholder="Initiative details...">
        <span style="color:red; cursor:pointer; font-weight:bold;" onclick="this.parentElement.remove()">X</span>
    `;
    container.appendChild(div);
}

// --- 1. ROW MANAGEMENT FUNCTIONS ---

// Q1: Modifications List
function addP5LeadModRow(text = "") {
    const container = document.getElementById("p5LeadModContainer");
    const div = document.createElement("div");
    div.className = "p5-mod-row";
    div.style.display = "flex";
    div.style.gap = "10px";
    div.style.marginBottom = "8px";
    div.style.alignItems = "center";

    div.innerHTML = `
        <span style="font-weight:bold; color:#108a55;">•</span>
        <textarea class="p5-mod-input" rows="2" style="width:100%" placeholder="Process detail...">${text}</textarea>
        <span style="color:red; cursor:pointer; font-weight:bold;" onclick="this.parentElement.remove()">X</span>
    `;
    container.appendChild(div);
}

// Q2: Issues List
function addP5LeadIssueRow(text = "") {
    const container = document.getElementById("p5LeadIssuesContainer");
    const div = document.createElement("div");
    div.className = "p5-issue-row";
    div.style.display = "flex";
    div.style.gap = "10px";
    div.style.marginBottom = "5px";
    div.style.alignItems = "center";

    div.innerHTML = `
        <span style="font-weight:bold; color:#108a55;">•</span>
        <input type="text" class="p5-issue-input" value="${text}" style="width:100%" placeholder="Issue (e.g. Child Labour)">
        <span style="color:red; cursor:pointer; font-weight:bold;" onclick="this.parentElement.remove()">X</span>
    `;
    container.appendChild(div);
}

// Q2: Rights Holders List
function addP5LeadHolderRow(text = "") {
    const container = document.getElementById("p5LeadHoldersContainer");
    const div = document.createElement("div");
    div.className = "p5-holder-row";
    div.style.display = "flex";
    div.style.gap = "10px";
    div.style.marginBottom = "5px";
    div.style.alignItems = "center";

    div.innerHTML = `
        <span style="font-weight:bold; color:#108a55;">•</span>
        <input type="text" class="p5-holder-input" value="${text}" style="width:100%" placeholder="Rights Holder (e.g. Contract Workforce)">
        <span style="color:red; cursor:pointer; font-weight:bold;" onclick="this.parentElement.remove()">X</span>
    `;
    container.appendChild(div);
}


/// --- MAIN GENERATE FUNCTION ---
function generateFinalReport() {
    const token = localStorage.getItem("token");

    // Check Mode
    const isRiskTableMode = document.querySelector('input[name="riskMode"][value="table"]').checked;
    let finalRiskList = [];
    let finalRiskNote = "";

    if (isRiskTableMode) {
        finalRiskList = Array.from(document.querySelectorAll("#riskTable tbody tr")).map(row => ({
            productName: row.querySelector(".rk-name").value,
            riskDescription: row.querySelector(".rk-desc").value,
            actionTaken: row.querySelector(".rk-act").value
        }));
    } else {
        finalRiskNote = document.getElementById("lcaRiskNote").value;
    }


    const formData = {
        id: parseInt(currentReportId),

        // --- Principle 6 (Environment) ---
        electricityConsumption: getVal("electricityConsumption"),
        fuelConsumption: getVal("fuelConsumption"),
        energyIntensity: getVal("energyIntensity"),
        scope1Emissions: getVal("scope1Emissions"),
        scope2Emissions: getVal("scope2Emissions"),
        emissionIntensity: getVal("emissionIntensity"),
        plasticWaste: getVal("plasticWaste"),
        eWaste: getVal("eWaste"),
        hazardousWaste: getVal("hazardousWaste"),
        totalWasteGenerated: getVal("totalWasteGenerated"),
        totalWasteRecycled: getVal("totalWasteRecycled"),

        // --- Principle 1 (Training) ---
        trainingPrograms: Array.from(document.querySelectorAll("#trainingTable tbody tr")).map(row => ({
            segment: row.querySelector(".tp-seg") ? row.querySelector(".tp-seg").value : "",
            totalPrograms: row.querySelector(".tp-num") ? row.querySelector(".tp-num").value : "",
            topicsCovered: row.querySelector(".tp-top") ? row.querySelector(".tp-top").value : "",
            percentageCovered: row.querySelector(".tp-perc") ? row.querySelector(".tp-perc").value : ""
        })),
        trainingNote: getVal("trainingNote"),

        // --- Question 2 (Legal) ---
        legalActionNote: getVal("legalActionNote"),

        // Safe check for checkbox
        nonMonetaryNA: document.getElementById("nonMonetaryNA") ? document.getElementById("nonMonetaryNA").checked : false,

        // Combine both tables back into a single array for the backend
                legalActions: [
                    ...Array.from(document.querySelectorAll("#monetaryTable tbody tr")).map(row => ({
                        type: row.querySelector(".la-type").value,
                        principle: row.querySelector(".la-prin").value,
                        agency: row.querySelector(".la-agency").value,
                        amount: row.querySelector(".la-amt").value,
                        brief: row.querySelector(".la-brief").value,
                        appeal: row.querySelector(".la-app").value
                    })),
                    ...Array.from(document.querySelectorAll("#nonMonetaryTable tbody tr")).map(row => ({
                        type: row.querySelector(".la-type").value,
                        principle: row.querySelector(".la-prin").value,
                        agency: row.querySelector(".la-agency").value,
                        amount: "N/A", // Force N/A for amount since it's non-monetary
                        brief: row.querySelector(".la-brief").value,
                        appeal: row.querySelector(".la-app").value
                    }))
                ],

        // --- SECTION C Q3 ---
        appealDetails: Array.from(document.querySelectorAll("#appealTable tbody tr")).map(row => ({
            caseDetails: row.querySelector(".ap-case").value,
            agencyName: row.querySelector(".ap-agency").value
        })),

        // --- Question 4 (Anti-Corruption) ---
        antiCorruptionPolicy: getVal("antiCorruptionPolicy"),
        antiCorruptionLink: getVal("antiCorruptionLink"),
        antiCorruptionDetails: getVal("antiCorruptionDetails"),

        // --- Question 5 ---
        disciplinaryFyCurrentHeader: getVal("disciplinaryFyCurrentHeader"),
        disciplinaryFyPreviousHeader: getVal("disciplinaryFyPreviousHeader"),

        discDirectorsCurr: getVal("discDirectorsCurr"),
        discKmpsCurr: getVal("discKmpsCurr"),
        discEmployeesCurr: getVal("discEmployeesCurr"),
        discWorkersCurr: getVal("discWorkersCurr"),

        discDirectorsPrev: getVal("discDirectorsPrev"),
        discKmpsPrev: getVal("discKmpsPrev"),
        discEmployeesPrev: getVal("discEmployeesPrev"),
        discWorkersPrev: getVal("discWorkersPrev"),

        // --- Question 6 ---
        coiDirectorsCurrNum: getVal("coiDirectorsCurrNum"),
        coiDirectorsCurrRem: getVal("coiDirectorsCurrRem"),
        coiDirectorsPrevNum: getVal("coiDirectorsPrevNum"),
        coiDirectorsPrevRem: getVal("coiDirectorsPrevRem"),

        coiKmpsCurrNum: getVal("coiKmpsCurrNum"),
        coiKmpsCurrRem: getVal("coiKmpsCurrRem"),
        coiKmpsPrevNum: getVal("coiKmpsPrevNum"),
        coiKmpsPrevRem: getVal("coiKmpsPrevRem"),

        // --- Question 7 ---
        correctiveActionDetails: getVal("correctiveActionDetails"),

        // --- Leadership Indicators ---
        leadershipIndicatorNote: getVal("leadershipIndicatorNote"),

        leadershipAwarenessPrograms: Array.from(document.querySelectorAll("#leadershipTable tbody tr")).map(row => ({
            totalCount: row.querySelector(".li-count").value,
            topic: row.querySelector(".li-topic").value,
            percentage: row.querySelector(".li-perc").value
        })),

        conflictOfInterestProcess: getVal("conflictOfInterestProcess"),
        conflictOfInterestDetails: getVal("conflictOfInterestDetails"),

        // Map the inputs to the shared FY fields in DTO
        fyCurrent: document.getElementById("p2FyCurrent").value,
        fyPrevious: document.getElementById("p2FyPrevious").value,

        // ... Principle 2 Data ...
        rdCurrentYear: getVal("rdCurrentYear"),
        rdPreviousYear: getVal("rdPreviousYear"),
        rdDetails: getVal("rdDetails"),
        capexCurrentYear: getVal("capexCurrentYear"),
        capexPreviousYear: getVal("capexPreviousYear"),
        capexDetails: getVal("capexDetails"),
        principle2Note: getVal("principle2Note"),

        // --- Principle 2 Q2 ---
        sustainableSourcingProcedures: getVal("sustainableSourcingProcedures"),
        sustainableSourcingDetails: getVal("sustainableSourcingDetails"),
        sustainableSourcingPercentage: getVal("sustainableSourcingPercentage"),
        sustainableSourcingRemarks: getVal("sustainableSourcingRemarks"),

        // --- Principle 2 Q3 ---
        reclaimProcessDetails: getVal("reclaimProcessDetails"),

        // --- Principle 2 Q4 ---
        eprDetails: getVal("eprDetails"),

        // --- Leadership LCA PRINCIPLE 2---

        // --- Principle 2 Leadership NA Flags ---
                p2Lead1NA: document.getElementById("p2Lead1NA") ? document.getElementById("p2Lead1NA").checked : false,
                p2Lead2NA: document.getElementById("p2Lead2NA") ? document.getElementById("p2Lead2NA").checked : false,
                p2Lead3NA: document.getElementById("p2Lead3NA") ? document.getElementById("p2Lead3NA").checked : false,
                p2Lead4NA: document.getElementById("p2Lead4NA") ? document.getElementById("p2Lead4NA").checked : false,
                p2Lead5NA: document.getElementById("p2Lead5NA") ? document.getElementById("p2Lead5NA").checked : false,

        lcaNote: getVal("lcaNote"),
        lcaEntries: Array.from(document.querySelectorAll("#lcaTable tbody tr")).map(row => ({
            nicCode: row.querySelector(".lca-nic").value,
            productName: row.querySelector(".lca-name").value,
            turnoverPercentage: row.querySelector(".lca-turn").value,
            boundary: row.querySelector(".lca-bound").value,
            independentAgency: row.querySelector(".lca-agency").value,
            publicDomainResult: row.querySelector(".lca-res").value
        })),

        // --- Principle 2 leader Q2 ---
        lcaRiskNote: finalRiskNote,
        lcaRisks: finalRiskList,

        // --- Principle 2 leader Q3 ---
        recycledInputNote: getVal("recycledInputNote"),
        recycledInputs: Array.from(document.querySelectorAll("#recycledTable tbody tr")).map(row => ({
            materialName: row.querySelector(".rec-mat").value,
            currentFyPercentage: row.querySelector(".rec-cur").value,
            previousFyPercentage: row.querySelector(".rec-prev").value
        })),

        // --- Q4 (Fixed fields) ---
        plasticReusedCurr: getVal("plasticReusedCurr"), plasticRecycledCurr: getVal("plasticRecycledCurr"), plasticDisposedCurr: getVal("plasticDisposedCurr"),
        plasticReusedPrev: getVal("plasticReusedPrev"), plasticRecycledPrev: getVal("plasticRecycledPrev"), plasticDisposedPrev: getVal("plasticDisposedPrev"),

        ewasteReusedCurr: getVal("ewasteReusedCurr"), ewasteRecycledCurr: getVal("ewasteRecycledCurr"), ewasteDisposedCurr: getVal("ewasteDisposedCurr"),
        ewasteReusedPrev: getVal("ewasteReusedPrev"), ewasteRecycledPrev: getVal("ewasteRecycledPrev"), ewasteDisposedPrev: getVal("ewasteDisposedPrev"),

        hazardReusedCurr: getVal("hazardReusedCurr"), hazardRecycledCurr: getVal("hazardRecycledCurr"), hazardDisposedCurr: getVal("hazardDisposedCurr"),
        hazardReusedPrev: getVal("hazardReusedPrev"), hazardRecycledPrev: getVal("hazardRecycledPrev"), hazardDisposedPrev: getVal("hazardDisposedPrev"),

        otherReusedCurr: getVal("otherReusedCurr"), otherRecycledCurr: getVal("otherRecycledCurr"), otherDisposedCurr: getVal("otherDisposedCurr"),
        otherReusedPrev: getVal("otherReusedPrev"), otherRecycledPrev: getVal("otherRecycledPrev"), otherDisposedPrev: getVal("otherDisposedPrev"),

        reclaimedWasteNote: getVal("reclaimedWasteNote"),

        // --- Q5 (List) ---
        reclaimedPercentageNote: getVal("reclaimedPercentageNote"),
        reclaimedPercentages: Array.from(document.querySelectorAll("#reclaimedPercTable tbody tr")).map(row => ({
            category: row.querySelector(".rp-cat").value,
            percentage: row.querySelector(".rp-perc").value
        })),

        // --- Principle 3 ---
                employeeWellBeing: scrapeWellBeingTable("#empWellBeingTable"),
                empWellBeingNote: getVal("empWellBeingNote"), // Updated Note

                workerWellBeing: scrapeWellBeingTable("#workWellBeingTable"),
                workWellBeingNote: getVal("workWellBeingNote"), // Updated Note

                wbCostCurr: getVal("wbCostCurr"), // New Q1.c
                wbCostPrev: getVal("wbCostPrev"), // New Q1.c
                wbCostNote: getVal("wbCostNote"), // New Q1.c Note

        // --- Principle 3 Q2 ---
        retirementBenefitNote: getVal("retirementBenefitNote"),
        retirementBenefits: Array.from(document.querySelectorAll("#retirementTable tbody tr")).map(row => ({
            benefits: row.querySelector(".rb-name").value,
            currEmpCovered: row.querySelector(".rb-ce").value,
            currWorkCovered: row.querySelector(".rb-cw").value,
            currDeducted: row.querySelector(".rb-cd").value,
            prevEmpCovered: row.querySelector(".rb-pe").value,
            prevWorkCovered: row.querySelector(".rb-pw").value,
            prevDeducted: row.querySelector(".rb-pd").value
        })),

        // --- Principle 3 Q3 ---
        accessibilityDetails: getVal("accessibilityDetails"),

        // --- Principle 3 Q4 ---
        equalOppPolicy: getVal("equalOppPolicy"),
        equalOppLink: getVal("equalOppLink"),
        equalOppDetails: getVal("equalOppDetails"),

        // --- Principle 3 Q5 ---
        plEmpMaleReturn: getVal("plEmpMaleReturn"), plEmpMaleRetain: getVal("plEmpMaleRetain"),
        plWorkMaleReturn: getVal("plWorkMaleReturn"), plWorkMaleRetain: getVal("plWorkMaleRetain"),

        plEmpFemaleReturn: getVal("plEmpFemaleReturn"), plEmpFemaleRetain: getVal("plEmpFemaleRetain"),
        plWorkFemaleReturn: getVal("plWorkFemaleReturn"), plWorkFemaleRetain: getVal("plWorkFemaleRetain"),

        plEmpTotalReturn: getVal("plEmpTotalReturn"), plEmpTotalRetain: getVal("plEmpTotalRetain"),
        plWorkTotalReturn: getVal("plWorkTotalReturn"), plWorkTotalRetain: getVal("plWorkTotalRetain"),

        parentalLeaveNote: getVal("parentalLeaveNote"),

        // --- Principle 3 Q6 ---
        grievancePermWorkers: getVal("grievancePermWorkers"),
        grievanceTempWorkers: getVal("grievanceTempWorkers"),
        grievancePermEmployees: getVal("grievancePermEmployees"),
        grievanceTempEmployees: getVal("grievanceTempEmployees"),

        // --- SECTION C: P3 Q7 (UNION MEMBERSHIP) ---

        // 1. Current Employees
        unionCurrEmpTotalA: getVal("unionCurrEmpTotalA"),
        unionCurrEmpUnionB: getVal("unionCurrEmpUnionB"),
        unionCurrEmpPerc: getVal("unionCurrEmpPerc"),

        unionCurrEmpMaleTotal: getVal("unionCurrEmpMaleTotal"),
        unionCurrEmpMaleUnion: getVal("unionCurrEmpMaleUnion"),
        unionCurrEmpMalePerc: getVal("unionCurrEmpMalePerc"),

        unionCurrEmpFemaleTotal: getVal("unionCurrEmpFemaleTotal"),
        unionCurrEmpFemaleUnion: getVal("unionCurrEmpFemaleUnion"),
        unionCurrEmpFemalePerc: getVal("unionCurrEmpFemalePerc"),

        // 2. Current Workers
        unionCurrWorkTotalA: getVal("unionCurrWorkTotalA"),
        unionCurrWorkUnionB: getVal("unionCurrWorkUnionB"),
        unionCurrWorkPerc: getVal("unionCurrWorkPerc"),

        unionCurrWorkMaleTotal: getVal("unionCurrWorkMaleTotal"),
        unionCurrWorkMaleUnion: getVal("unionCurrWorkMaleUnion"),
        unionCurrWorkMalePerc: getVal("unionCurrWorkMalePerc"),

        unionCurrWorkFemaleTotal: getVal("unionCurrWorkFemaleTotal"),
        unionCurrWorkFemaleUnion: getVal("unionCurrWorkFemaleUnion"),
        unionCurrWorkFemalePerc: getVal("unionCurrWorkFemalePerc"),

        // 3. Previous Employees
        unionPrevEmpTotalC: getVal("unionPrevEmpTotalC"),
        unionPrevEmpUnionD: getVal("unionPrevEmpUnionD"),
        unionPrevEmpPerc: getVal("unionPrevEmpPerc"),

        unionPrevEmpMaleTotal: getVal("unionPrevEmpMaleTotal"),
        unionPrevEmpMaleUnion: getVal("unionPrevEmpMaleUnion"),
        unionPrevEmpMalePerc: getVal("unionPrevEmpMalePerc"),

        unionPrevEmpFemaleTotal: getVal("unionPrevEmpFemaleTotal"),
        unionPrevEmpFemaleUnion: getVal("unionPrevEmpFemaleUnion"),
        unionPrevEmpFemalePerc: getVal("unionPrevEmpFemalePerc"),

        // 4. Previous Workers
        unionPrevWorkTotalC: getVal("unionPrevWorkTotalC"),
        unionPrevWorkUnionD: getVal("unionPrevWorkUnionD"),
        unionPrevWorkPerc: getVal("unionPrevWorkPerc"),

        unionPrevWorkMaleTotal: getVal("unionPrevWorkMaleTotal"),
        unionPrevWorkMaleUnion: getVal("unionPrevWorkMaleUnion"),
        unionPrevWorkMalePerc: getVal("unionPrevWorkMalePerc"),

        unionPrevWorkFemaleTotal: getVal("unionPrevWorkFemaleTotal"),
        unionPrevWorkFemaleUnion: getVal("unionPrevWorkFemaleUnion"),
        unionPrevWorkFemalePerc: getVal("unionPrevWorkFemalePerc"),

        // Note
        unionMembershipNote: getVal("unionMembershipNote"),

        // MAPPING
        trainEmpMaleTotal: getVal("trainEmpMaleTotal"), trainEmpMaleHealthNo: getVal("trainEmpMaleHealthNo"), trainEmpMaleHealthPerc: getVal("trainEmpMaleHealthPerc"), trainEmpMaleSkillNo: getVal("trainEmpMaleSkillNo"), trainEmpMaleSkillPerc: getVal("trainEmpMaleSkillPerc"),
        trainEmpFemaleTotal: getVal("trainEmpFemaleTotal"), trainEmpFemaleHealthNo: getVal("trainEmpFemaleHealthNo"), trainEmpFemaleHealthPerc: getVal("trainEmpFemaleHealthPerc"), trainEmpFemaleSkillNo: getVal("trainEmpFemaleSkillNo"), trainEmpFemaleSkillPerc: getVal("trainEmpFemaleSkillPerc"),
        trainEmpGenTotal: getVal("trainEmpGenTotal"), trainEmpGenHealthNo: getVal("trainEmpGenHealthNo"), trainEmpGenHealthPerc: getVal("trainEmpGenHealthPerc"), trainEmpGenSkillNo: getVal("trainEmpGenSkillNo"), trainEmpGenSkillPerc: getVal("trainEmpGenSkillPerc"),

        trainEmpMaleTotalPrev: getVal("trainEmpMaleTotalPrev"), trainEmpMaleHealthNoPrev: getVal("trainEmpMaleHealthNoPrev"), trainEmpMaleHealthPercPrev: getVal("trainEmpMaleHealthPercPrev"), trainEmpMaleSkillNoPrev: getVal("trainEmpMaleSkillNoPrev"), trainEmpMaleSkillPercPrev: getVal("trainEmpMaleSkillPercPrev"),
        trainEmpFemaleTotalPrev: getVal("trainEmpFemaleTotalPrev"), trainEmpFemaleHealthNoPrev: getVal("trainEmpFemaleHealthNoPrev"), trainEmpFemaleHealthPercPrev: getVal("trainEmpFemaleHealthPercPrev"), trainEmpFemaleSkillNoPrev: getVal("trainEmpFemaleSkillNoPrev"), trainEmpFemaleSkillPercPrev: getVal("trainEmpFemaleSkillPercPrev"),
        trainEmpGenTotalPrev: getVal("trainEmpGenTotalPrev"), trainEmpGenHealthNoPrev: getVal("trainEmpGenHealthNoPrev"), trainEmpGenHealthPercPrev: getVal("trainEmpGenHealthPercPrev"), trainEmpGenSkillNoPrev: getVal("trainEmpGenSkillNoPrev"), trainEmpGenSkillPercPrev: getVal("trainEmpGenSkillPercPrev"),

        trainWorkMaleTotal: getVal("trainWorkMaleTotal"), trainWorkMaleHealthNo: getVal("trainWorkMaleHealthNo"), trainWorkMaleHealthPerc: getVal("trainWorkMaleHealthPerc"), trainWorkMaleSkillNo: getVal("trainWorkMaleSkillNo"), trainWorkMaleSkillPerc: getVal("trainWorkMaleSkillPerc"),
        trainWorkFemaleTotal: getVal("trainWorkFemaleTotal"), trainWorkFemaleHealthNo: getVal("trainWorkFemaleHealthNo"), trainWorkFemaleHealthPerc: getVal("trainWorkFemaleHealthPerc"), trainWorkFemaleSkillNo: getVal("trainWorkFemaleSkillNo"), trainWorkFemaleSkillPerc: getVal("trainWorkFemaleSkillPerc"),
        trainWorkGenTotal: getVal("trainWorkGenTotal"), trainWorkGenHealthNo: getVal("trainWorkGenHealthNo"), trainWorkGenHealthPerc: getVal("trainWorkGenHealthPerc"), trainWorkGenSkillNo: getVal("trainWorkGenSkillNo"), trainWorkGenSkillPerc: getVal("trainWorkGenSkillPerc"),

        trainWorkMaleTotalPrev: getVal("trainWorkMaleTotalPrev"), trainWorkMaleHealthNoPrev: getVal("trainWorkMaleHealthNoPrev"), trainWorkMaleHealthPercPrev: getVal("trainWorkMaleHealthPercPrev"), trainWorkMaleSkillNoPrev: getVal("trainWorkMaleSkillNoPrev"), trainWorkMaleSkillPercPrev: getVal("trainWorkMaleSkillPercPrev"),
        trainWorkFemaleTotalPrev: getVal("trainWorkFemaleTotalPrev"), trainWorkFemaleHealthNoPrev: getVal("trainWorkFemaleHealthNoPrev"), trainWorkFemaleHealthPercPrev: getVal("trainWorkFemaleHealthPercPrev"), trainWorkFemaleSkillNoPrev: getVal("trainWorkFemaleSkillNoPrev"), trainWorkFemaleSkillPercPrev: getVal("trainWorkFemaleSkillPercPrev"),
        trainWorkGenTotalPrev: getVal("trainWorkGenTotalPrev"), trainWorkGenHealthNoPrev: getVal("trainWorkGenHealthNoPrev"), trainWorkGenHealthPercPrev: getVal("trainWorkGenHealthPercPrev"), trainWorkGenSkillNoPrev: getVal("trainWorkGenSkillNoPrev"), trainWorkGenSkillPercPrev: getVal("trainWorkGenSkillPercPrev"),

        trainingDetailsNote: getVal("trainingDetailsNote"),

        // --- Q9 PERFORMANCE REVIEWS ---
        reviewDetailsNote: getVal("reviewDetailsNote"),

        // Employees
        revEmpMaleTotal: getVal("revEmpMaleTotal"), revEmpMaleCovered: getVal("revEmpMaleCovered"), revEmpMalePerc: getVal("revEmpMalePerc"),
        revEmpFemTotal: getVal("revEmpFemTotal"), revEmpFemCovered: getVal("revEmpFemCovered"), revEmpFemPerc: getVal("revEmpFemPerc"),
        revEmpOthTotal: getVal("revEmpOthTotal"), revEmpOthCovered: getVal("revEmpOthCovered"), revEmpOthPerc: getVal("revEmpOthPerc"),
        revEmpGenTotal: getVal("revEmpGenTotal"), revEmpGenCovered: getVal("revEmpGenCovered"), revEmpGenPerc: getVal("revEmpGenPerc"),

        revEmpMaleTotalPrev: getVal("revEmpMaleTotalPrev"), revEmpMaleCoveredPrev: getVal("revEmpMaleCoveredPrev"), revEmpMalePercPrev: getVal("revEmpMalePercPrev"),
        revEmpFemTotalPrev: getVal("revEmpFemTotalPrev"), revEmpFemCoveredPrev: getVal("revEmpFemCoveredPrev"), revEmpFemPercPrev: getVal("revEmpFemPercPrev"),
        revEmpOthTotalPrev: getVal("revEmpOthTotalPrev"), revEmpOthCoveredPrev: getVal("revEmpOthCoveredPrev"), revEmpOthPercPrev: getVal("revEmpOthPercPrev"),
        revEmpGenTotalPrev: getVal("revEmpGenTotalPrev"), revEmpGenCoveredPrev: getVal("revEmpGenCoveredPrev"), revEmpGenPercPrev: getVal("revEmpGenPercPrev"),

        // Workers
        revWorkMaleTotal: getVal("revWorkMaleTotal"), revWorkMaleCovered: getVal("revWorkMaleCovered"), revWorkMalePerc: getVal("revWorkMalePerc"),
        revWorkFemTotal: getVal("revWorkFemTotal"), revWorkFemCovered: getVal("revWorkFemCovered"), revWorkFemPerc: getVal("revWorkFemPerc"),
        revWorkOthTotal: getVal("revWorkOthTotal"), revWorkOthCovered: getVal("revWorkOthCovered"), revWorkOthPerc: getVal("revWorkOthPerc"),
        revWorkGenTotal: getVal("revWorkGenTotal"), revWorkGenCovered: getVal("revWorkGenCovered"), revWorkGenPerc: getVal("revWorkGenPerc"),

        revWorkMaleTotalPrev: getVal("revWorkMaleTotalPrev"), revWorkMaleCoveredPrev: getVal("revWorkMaleCoveredPrev"), revWorkMalePercPrev: getVal("revWorkMalePercPrev"),
        revWorkFemTotalPrev: getVal("revWorkFemTotalPrev"), revWorkFemCoveredPrev: getVal("revWorkFemCoveredPrev"), revWorkFemPercPrev: getVal("revWorkFemPercPrev"),
        revWorkOthTotalPrev: getVal("revWorkOthTotalPrev"), revWorkOthCoveredPrev: getVal("revWorkOthCoveredPrev"), revWorkOthPercPrev: getVal("revWorkOthPercPrev"),
        revWorkGenTotalPrev: getVal("revWorkGenTotalPrev"), revWorkGenCoveredPrev: getVal("revWorkGenCoveredPrev"), revWorkGenPercPrev: getVal("revWorkGenPercPrev"),

        // --- Principle 3 Q10 ---
        healthSafetySystem: getVal("healthSafetySystem"),
        hazardIdentification: getVal("hazardIdentification"),
        hazardReporting: getVal("hazardReporting"),
        medicalAccess: getVal("medicalAccess"),

        // --- Principle 3 Q11 ---
        safetyLtifrEmpCurr: getVal("safetyLtifrEmpCurr"), safetyLtifrEmpPrev: getVal("safetyLtifrEmpPrev"),
        safetyLtifrWorkCurr: getVal("safetyLtifrWorkCurr"), safetyLtifrWorkPrev: getVal("safetyLtifrWorkPrev"),

        safetyTotalInjEmpCurr: getVal("safetyTotalInjEmpCurr"), safetyTotalInjEmpPrev: getVal("safetyTotalInjEmpPrev"),
        safetyTotalInjWorkCurr: getVal("safetyTotalInjWorkCurr"), safetyTotalInjWorkPrev: getVal("safetyTotalInjWorkPrev"),

        safetyFatalEmpCurr: getVal("safetyFatalEmpCurr"), safetyFatalEmpPrev: getVal("safetyFatalEmpPrev"),
        safetyFatalWorkCurr: getVal("safetyFatalWorkCurr"), safetyFatalWorkPrev: getVal("safetyFatalWorkPrev"),

        safetyHighConEmpCurr: getVal("safetyHighConEmpCurr"), safetyHighConEmpPrev: getVal("safetyHighConEmpPrev"),
        safetyHighConWorkCurr: getVal("safetyHighConWorkCurr"), safetyHighConWorkPrev: getVal("safetyHighConWorkPrev"),

        safetyPermDisEmpCurr: getVal("safetyPermDisEmpCurr"), safetyPermDisEmpPrev: getVal("safetyPermDisEmpPrev"),
        safetyPermDisWorkCurr: getVal("safetyPermDisWorkCurr"), safetyPermDisWorkPrev: getVal("safetyPermDisWorkPrev"),

        safetyIncidentsNote: getVal("safetyIncidentsNote"),

        // --- Q12 ---
        safetyMeasures: Array.from(document.querySelectorAll(".safety-measure-item")).map(item => ({
            heading: item.querySelector(".sm-head").value,
            description: item.querySelector(".sm-desc").value
        })),

        // --- Principle 3 Q13 ---
        wcFiledCurr: getVal("wcFiledCurr"), wcPendingCurr: getVal("wcPendingCurr"), wcRemarksCurr: getVal("wcRemarksCurr"),
        wcFiledPrev: getVal("wcFiledPrev"), wcPendingPrev: getVal("wcPendingPrev"), wcRemarksPrev: getVal("wcRemarksPrev"),

        hsFiledCurr: getVal("hsFiledCurr"), hsPendingCurr: getVal("hsPendingCurr"), hsRemarksCurr: getVal("hsRemarksCurr"),
        hsFiledPrev: getVal("hsFiledPrev"), hsPendingPrev: getVal("hsPendingPrev"), hsRemarksPrev: getVal("hsRemarksPrev"),

        workerComplaintsNote: getVal("workerComplaintsNote"),

        // --- Principle 3 Q14 ---
        assessmentHealthPerc: getVal("assessmentHealthPerc"),
        assessmentWorkingPerc: getVal("assessmentWorkingPerc"),
        assessmentNote: getVal("assessmentNote"),

        // --- Principle 3 Q15 ---
        safetyCorrectiveActions: getVal("safetyCorrectiveActions"),

        // --- Principle 3 Leadership 1 ---
        lifeInsuranceEmployees: getVal("lifeInsuranceEmployees"),
        lifeInsuranceWorkers: getVal("lifeInsuranceWorkers"),
        lifeInsuranceDetails: getVal("lifeInsuranceDetails"),

        // --- Principle 3 Leadership 2 ---
        statutoryDuesMeasures: getVal("statutoryDuesMeasures"),

        // --- Principle 3 Leadership 3 ---
        rehabEmpAffCurr: getVal("rehabEmpAffCurr"), rehabEmpAffPrev: getVal("rehabEmpAffPrev"),
        rehabEmpPlacedCurr: getVal("rehabEmpPlacedCurr"), rehabEmpPlacedPrev: getVal("rehabEmpPlacedPrev"),

        rehabWorkAffCurr: getVal("rehabWorkAffCurr"), rehabWorkAffPrev: getVal("rehabWorkAffPrev"),
        rehabWorkPlacedCurr: getVal("rehabWorkPlacedCurr"), rehabWorkPlacedPrev: getVal("rehabWorkPlacedPrev"),

        rehabilitationNote: getVal("rehabilitationNote"),

        // --- Principle 3 Leadership 4 ---
        transitionAssistanceDetails: getVal("transitionAssistanceDetails"),

        // --- Principle 3 Leadership 5 ---
        valueChainAssessmentNote: getVal("valueChainAssessmentNote"),
        vcHealthSafetyPerc: getVal("vcHealthSafetyPerc"),
        vcWorkingCondPerc: getVal("vcWorkingCondPerc"),

        // --- Principle 3 Leadership 6 ---
        vcCorrectiveActionIntro: getVal("vcCorrectiveActionIntro"),
        vcCorrectiveActions: Array.from(document.querySelectorAll(".vc-action-item")).map(item => ({
            actionText: item.querySelector(".vc-act-text").value
        })),

                transitionAssistanceYN: getVal("transitionAssistanceYN"),
                transitionAssistanceDetails: getVal("transitionAssistanceDetails"),

                valueChainAssessmentNote: getVal("valueChainAssessmentNote"),

        // --- Principle 4 Q1 ---
        principle4Q1Intro: getVal("principle4Q1Intro"),
        principle4Q1Conclusion: getVal("principle4Q1Conclusion"),

        principle4Q1Points: Array.from(document.querySelectorAll(".p4-pt-input")).map(input => input.value),

        // --- Principle 4 Q2 ---
        stakeholderNote: getVal("stakeholderNote"),
        stakeholderEngagements: Array.from(document.querySelectorAll("#stakeholderTable tbody tr")).map(row => ({
            groupName: row.querySelector(".st-name").value,
            isVulnerable: row.querySelector(".st-vul").value,
            channels: row.querySelector(".st-chan").value,
            frequency: row.querySelector(".st-freq").value,
            purpose: row.querySelector(".st-purp").value
        })),

        // --- Principle 4 Leadership 1 ---
        consultationProcessDetails: getVal("consultationProcessDetails"),

        // --- Principle 4 Leadership 2 ---
        stakeholderConsultationUsed: getVal("stakeholderConsultationUsed"),
        stakeholderConsultationDetails: getVal("stakeholderConsultationDetails"),

        // --- Principle 4 Leadership 3 ---
        vulnerableGroupIntro: getVal("vulnerableGroupIntro"),
        vulnerableGroupConclusion: getVal("vulnerableGroupConclusion"),
        vulnerableGroupActions: Array.from(document.querySelectorAll(".vul-act-input")).map(input => input.value),

        // --- PRINCIPLE 5: HUMAN RIGHTS TRAINING ---

        // 1. Employees - Permanent
        hrEmpPermTotalCurr: getVal("hrEmpPermTotalCurr"),
        hrEmpPermCovCurr: getVal("hrEmpPermCovCurr"),
        hrEmpPermPercCurr: getVal("hrEmpPermPercCurr"),
        hrEmpPermTotalPrev: getVal("hrEmpPermTotalPrev"),
        hrEmpPermCovPrev: getVal("hrEmpPermCovPrev"),
        hrEmpPermPercPrev: getVal("hrEmpPermPercPrev"),

        // 2. Employees - Temporary (Other than Permanent)
        hrEmpTempTotalCurr: getVal("hrEmpTempTotalCurr"),
        hrEmpTempCovCurr: getVal("hrEmpTempCovCurr"),
        hrEmpTempPercCurr: getVal("hrEmpTempPercCurr"),
        hrEmpTempTotalPrev: getVal("hrEmpTempTotalPrev"),
        hrEmpTempCovPrev: getVal("hrEmpTempCovPrev"),
        hrEmpTempPercPrev: getVal("hrEmpTempPercPrev"),

        // 3. Employees - Total
        hrEmpGrandTotalCurr: getVal("hrEmpGrandTotalCurr"),
        hrEmpGrandCovCurr: getVal("hrEmpGrandCovCurr"),
        hrEmpGrandPercCurr: getVal("hrEmpGrandPercCurr"),
        hrEmpGrandTotalPrev: getVal("hrEmpGrandTotalPrev"),
        hrEmpGrandCovPrev: getVal("hrEmpGrandCovPrev"),
        hrEmpGrandPercPrev: getVal("hrEmpGrandPercPrev"),

        // 4. Workers - Permanent
        hrWorkPermTotalCurr: getVal("hrWorkPermTotalCurr"),
        hrWorkPermCovCurr: getVal("hrWorkPermCovCurr"),
        hrWorkPermPercCurr: getVal("hrWorkPermPercCurr"),
        hrWorkPermTotalPrev: getVal("hrWorkPermTotalPrev"),
        hrWorkPermCovPrev: getVal("hrWorkPermCovPrev"),
        hrWorkPermPercPrev: getVal("hrWorkPermPercPrev"),

        // 5. Workers - Temporary (Other than Permanent)
        hrWorkTempTotalCurr: getVal("hrWorkTempTotalCurr"),
        hrWorkTempCovCurr: getVal("hrWorkTempCovCurr"),
        hrWorkTempPercCurr: getVal("hrWorkTempPercCurr"),
        hrWorkTempTotalPrev: getVal("hrWorkTempTotalPrev"),
        hrWorkTempCovPrev: getVal("hrWorkTempCovPrev"),
        hrWorkTempPercPrev: getVal("hrWorkTempPercPrev"),

        // 6. Workers - Total
        hrWorkGrandTotalCurr: getVal("hrWorkGrandTotalCurr"),
        hrWorkGrandCovCurr: getVal("hrWorkGrandCovCurr"),
        hrWorkGrandPercCurr: getVal("hrWorkGrandPercCurr"),
        hrWorkGrandTotalPrev: getVal("hrWorkGrandTotalPrev"),
        hrWorkGrandCovPrev: getVal("hrWorkGrandCovPrev"),
        hrWorkGrandPercPrev: getVal("hrWorkGrandPercPrev"),

        // Note
        hrTrainingNote: getVal("hrTrainingNote"),

        // --- SECTION C: P5 Q2 (MINIMUM WAGES) ---
        minWageNote: getVal("minWageNote"),

        // 1. EMPLOYEES - PERMANENT
        mwEmpPermMaleTotalCurr: getVal("mwEmpPermMaleTotalCurr"), mwEmpPermMaleEqNoCurr: getVal("mwEmpPermMaleEqNoCurr"), mwEmpPermMaleEqPercCurr: getVal("mwEmpPermMaleEqPercCurr"), mwEmpPermMaleMoreNoCurr: getVal("mwEmpPermMaleMoreNoCurr"), mwEmpPermMaleMorePercCurr: getVal("mwEmpPermMaleMorePercCurr"),
        mwEmpPermMaleTotalPrev: getVal("mwEmpPermMaleTotalPrev"), mwEmpPermMaleEqNoPrev: getVal("mwEmpPermMaleEqNoPrev"), mwEmpPermMaleEqPercPrev: getVal("mwEmpPermMaleEqPercPrev"), mwEmpPermMaleMoreNoPrev: getVal("mwEmpPermMaleMoreNoPrev"), mwEmpPermMaleMorePercPrev: getVal("mwEmpPermMaleMorePercPrev"),

        mwEmpPermFemTotalCurr: getVal("mwEmpPermFemTotalCurr"), mwEmpPermFemEqNoCurr: getVal("mwEmpPermFemEqNoCurr"), mwEmpPermFemEqPercCurr: getVal("mwEmpPermFemEqPercCurr"), mwEmpPermFemMoreNoCurr: getVal("mwEmpPermFemMoreNoCurr"), mwEmpPermFemMorePercCurr: getVal("mwEmpPermFemMorePercCurr"),
        mwEmpPermFemTotalPrev: getVal("mwEmpPermFemTotalPrev"), mwEmpPermFemEqNoPrev: getVal("mwEmpPermFemEqNoPrev"), mwEmpPermFemEqPercPrev: getVal("mwEmpPermFemEqPercPrev"), mwEmpPermFemMoreNoPrev: getVal("mwEmpPermFemMoreNoPrev"), mwEmpPermFemMorePercPrev: getVal("mwEmpPermFemMorePercPrev"),

        mwEmpPermOthTotalCurr: getVal("mwEmpPermOthTotalCurr"), mwEmpPermOthEqNoCurr: getVal("mwEmpPermOthEqNoCurr"), mwEmpPermOthEqPercCurr: getVal("mwEmpPermOthEqPercCurr"), mwEmpPermOthMoreNoCurr: getVal("mwEmpPermOthMoreNoCurr"), mwEmpPermOthMorePercCurr: getVal("mwEmpPermOthMorePercCurr"),
        mwEmpPermOthTotalPrev: getVal("mwEmpPermOthTotalPrev"), mwEmpPermOthEqNoPrev: getVal("mwEmpPermOthEqNoPrev"), mwEmpPermOthEqPercPrev: getVal("mwEmpPermOthEqPercPrev"), mwEmpPermOthMoreNoPrev: getVal("mwEmpPermOthMoreNoPrev"), mwEmpPermOthMorePercPrev: getVal("mwEmpPermOthMorePercPrev"),

        // 2. EMPLOYEES - TEMPORARY
        mwEmpTempMaleTotalCurr: getVal("mwEmpTempMaleTotalCurr"), mwEmpTempMaleEqNoCurr: getVal("mwEmpTempMaleEqNoCurr"), mwEmpTempMaleEqPercCurr: getVal("mwEmpTempMaleEqPercCurr"), mwEmpTempMaleMoreNoCurr: getVal("mwEmpTempMaleMoreNoCurr"), mwEmpTempMaleMorePercCurr: getVal("mwEmpTempMaleMorePercCurr"),
        mwEmpTempMaleTotalPrev: getVal("mwEmpTempMaleTotalPrev"), mwEmpTempMaleEqNoPrev: getVal("mwEmpTempMaleEqNoPrev"), mwEmpTempMaleEqPercPrev: getVal("mwEmpTempMaleEqPercPrev"), mwEmpTempMaleMoreNoPrev: getVal("mwEmpTempMaleMoreNoPrev"), mwEmpTempMaleMorePercPrev: getVal("mwEmpTempMaleMorePercPrev"),

        mwEmpTempFemTotalCurr: getVal("mwEmpTempFemTotalCurr"), mwEmpTempFemEqNoCurr: getVal("mwEmpTempFemEqNoCurr"), mwEmpTempFemEqPercCurr: getVal("mwEmpTempFemEqPercCurr"), mwEmpTempFemMoreNoCurr: getVal("mwEmpTempFemMoreNoCurr"), mwEmpTempFemMorePercCurr: getVal("mwEmpTempFemMorePercCurr"),
        mwEmpTempFemTotalPrev: getVal("mwEmpTempFemTotalPrev"), mwEmpTempFemEqNoPrev: getVal("mwEmpTempFemEqNoPrev"), mwEmpTempFemEqPercPrev: getVal("mwEmpTempFemEqPercPrev"), mwEmpTempFemMoreNoPrev: getVal("mwEmpTempFemMoreNoPrev"), mwEmpTempFemMorePercPrev: getVal("mwEmpTempFemMorePercPrev"),

        mwEmpTempOthTotalCurr: getVal("mwEmpTempOthTotalCurr"), mwEmpTempOthEqNoCurr: getVal("mwEmpTempOthEqNoCurr"), mwEmpTempOthEqPercCurr: getVal("mwEmpTempOthEqPercCurr"), mwEmpTempOthMoreNoCurr: getVal("mwEmpTempOthMoreNoCurr"), mwEmpTempOthMorePercCurr: getVal("mwEmpTempOthMorePercCurr"),
        mwEmpTempOthTotalPrev: getVal("mwEmpTempOthTotalPrev"), mwEmpTempOthEqNoPrev: getVal("mwEmpTempOthEqNoPrev"), mwEmpTempOthEqPercPrev: getVal("mwEmpTempOthEqPercPrev"), mwEmpTempOthMoreNoPrev: getVal("mwEmpTempOthMoreNoPrev"), mwEmpTempOthMorePercPrev: getVal("mwEmpTempOthMorePercPrev"),

        // 3. WORKERS - PERMANENT
        mwWorkPermMaleTotalCurr: getVal("mwWorkPermMaleTotalCurr"), mwWorkPermMaleEqNoCurr: getVal("mwWorkPermMaleEqNoCurr"), mwWorkPermMaleEqPercCurr: getVal("mwWorkPermMaleEqPercCurr"), mwWorkPermMaleMoreNoCurr: getVal("mwWorkPermMaleMoreNoCurr"), mwWorkPermMaleMorePercCurr: getVal("mwWorkPermMaleMorePercCurr"),
        mwWorkPermMaleTotalPrev: getVal("mwWorkPermMaleTotalPrev"), mwWorkPermMaleEqNoPrev: getVal("mwWorkPermMaleEqNoPrev"), mwWorkPermMaleEqPercPrev: getVal("mwWorkPermMaleEqPercPrev"), mwWorkPermMaleMoreNoPrev: getVal("mwWorkPermMaleMoreNoPrev"), mwWorkPermMaleMorePercPrev: getVal("mwWorkPermMaleMorePercPrev"),

        mwWorkPermFemTotalCurr: getVal("mwWorkPermFemTotalCurr"), mwWorkPermFemEqNoCurr: getVal("mwWorkPermFemEqNoCurr"), mwWorkPermFemEqPercCurr: getVal("mwWorkPermFemEqPercCurr"), mwWorkPermFemMoreNoCurr: getVal("mwWorkPermFemMoreNoCurr"), mwWorkPermFemMorePercCurr: getVal("mwWorkPermFemMorePercCurr"),
        mwWorkPermFemTotalPrev: getVal("mwWorkPermFemTotalPrev"), mwWorkPermFemEqNoPrev: getVal("mwWorkPermFemEqNoPrev"), mwWorkPermFemEqPercPrev: getVal("mwWorkPermFemEqPercPrev"), mwWorkPermFemMoreNoPrev: getVal("mwWorkPermFemMoreNoPrev"), mwWorkPermFemMorePercPrev: getVal("mwWorkPermFemMorePercPrev"),

        mwWorkPermOthTotalCurr: getVal("mwWorkPermOthTotalCurr"), mwWorkPermOthEqNoCurr: getVal("mwWorkPermOthEqNoCurr"), mwWorkPermOthEqPercCurr: getVal("mwWorkPermOthEqPercCurr"), mwWorkPermOthMoreNoCurr: getVal("mwWorkPermOthMoreNoCurr"), mwWorkPermOthMorePercCurr: getVal("mwWorkPermOthMorePercCurr"),
        mwWorkPermOthTotalPrev: getVal("mwWorkPermOthTotalPrev"), mwWorkPermOthEqNoPrev: getVal("mwWorkPermOthEqNoPrev"), mwWorkPermOthEqPercPrev: getVal("mwWorkPermOthEqPercPrev"), mwWorkPermOthMoreNoPrev: getVal("mwWorkPermOthMoreNoPrev"), mwWorkPermOthMorePercPrev: getVal("mwWorkPermOthMorePercPrev"),

        // 4. WORKERS - TEMPORARY
        mwWorkTempMaleTotalCurr: getVal("mwWorkTempMaleTotalCurr"), mwWorkTempMaleEqNoCurr: getVal("mwWorkTempMaleEqNoCurr"), mwWorkTempMaleEqPercCurr: getVal("mwWorkTempMaleEqPercCurr"), mwWorkTempMaleMoreNoCurr: getVal("mwWorkTempMaleMoreNoCurr"), mwWorkTempMaleMorePercCurr: getVal("mwWorkTempMaleMorePercCurr"),
        mwWorkTempMaleTotalPrev: getVal("mwWorkTempMaleTotalPrev"), mwWorkTempMaleEqNoPrev: getVal("mwWorkTempMaleEqNoPrev"), mwWorkTempMaleEqPercPrev: getVal("mwWorkTempMaleEqPercPrev"), mwWorkTempMaleMoreNoPrev: getVal("mwWorkTempMaleMoreNoPrev"), mwWorkTempMaleMorePercPrev: getVal("mwWorkTempMaleMorePercPrev"),

        mwWorkTempFemTotalCurr: getVal("mwWorkTempFemTotalCurr"), mwWorkTempFemEqNoCurr: getVal("mwWorkTempFemEqNoCurr"), mwWorkTempFemEqPercCurr: getVal("mwWorkTempFemEqPercCurr"), mwWorkTempFemMoreNoCurr: getVal("mwWorkTempFemMoreNoCurr"), mwWorkTempFemMorePercCurr: getVal("mwWorkTempFemMorePercCurr"),
        mwWorkTempFemTotalPrev: getVal("mwWorkTempFemTotalPrev"), mwWorkTempFemEqNoPrev: getVal("mwWorkTempFemEqNoPrev"), mwWorkTempFemEqPercPrev: getVal("mwWorkTempFemEqPercPrev"), mwWorkTempFemMoreNoPrev: getVal("mwWorkTempFemMoreNoPrev"), mwWorkTempFemMorePercPrev: getVal("mwWorkTempFemMorePercPrev"),

        mwWorkTempOthTotalCurr: getVal("mwWorkTempOthTotalCurr"), mwWorkTempOthEqNoCurr: getVal("mwWorkTempOthEqNoCurr"), mwWorkTempOthEqPercCurr: getVal("mwWorkTempOthEqPercCurr"), mwWorkTempOthMoreNoCurr: getVal("mwWorkTempOthMoreNoCurr"), mwWorkTempOthMorePercCurr: getVal("mwWorkTempOthMorePercCurr"),
        mwWorkTempOthTotalPrev: getVal("mwWorkTempOthTotalPrev"), mwWorkTempOthEqNoPrev: getVal("mwWorkTempOthEqNoPrev"), mwWorkTempOthEqPercPrev: getVal("mwWorkTempOthEqPercPrev"), mwWorkTempOthMoreNoPrev: getVal("mwWorkTempOthMoreNoPrev"), mwWorkTempOthMorePercPrev: getVal("mwWorkTempOthMorePercPrev"),

        // --- Principle 5 Q3 ---
        remBodMaleNum: getVal("remBodMaleNum"), remBodMaleMedian: getVal("remBodMaleMedian"),
        remBodFemNum: getVal("remBodFemNum"), remBodFemMedian: getVal("remBodFemMedian"),
        remKmpMaleNum: getVal("remKmpMaleNum"), remKmpMaleMedian: getVal("remKmpMaleMedian"),
        remKmpFemNum: getVal("remKmpFemNum"), remKmpFemMedian: getVal("remKmpFemMedian"),

        remEmpMaleNum: getVal("remEmpMaleNum"), remEmpMaleMedian: getVal("remEmpMaleMedian"),
        remEmpFemNum: getVal("remEmpFemNum"), remEmpFemMedian: getVal("remEmpFemMedian"),

        remWorkMaleNum: getVal("remWorkMaleNum"), remWorkMaleMedian: getVal("remWorkMaleMedian"),
        remWorkFemNum: getVal("remWorkFemNum"), remWorkFemMedian: getVal("remWorkFemMedian"),

        remunerationNote: getVal("remunerationNote"),

        // --- Principle 5 Q2.b ---
        grossWagesFemalePercCurr: getVal("grossWagesFemalePercCurr"),
        grossWagesFemalePercPrev: getVal("grossWagesFemalePercPrev"),
        grossWagesNote: getVal("grossWagesNote"),

         // --- Principle 5 Q4 ---
         humanRightsFocalPoint: getVal("humanRightsFocalPoint"),
         humanRightsFocalDetails: getVal("humanRightsFocalDetails"),

         // --- Principle 5 Q5 ---
         humanRightsGrievanceMechanism: getVal("humanRightsGrievanceMechanism"),

         // --- Principle 5 Q6 ---
                 compShFiledCurr: getVal("compShFiledCurr"), compShPendingCurr: getVal("compShPendingCurr"), compShRemarksCurr: getVal("compShRemarksCurr"),
                 compShFiledPrev: getVal("compShFiledPrev"), compShPendingPrev: getVal("compShPendingPrev"), compShRemarksPrev: getVal("compShRemarksPrev"),

                 compDiscrimFiledCurr: getVal("compDiscrimFiledCurr"), compDiscrimPendingCurr: getVal("compDiscrimPendingCurr"), compDiscrimRemarksCurr: getVal("compDiscrimRemarksCurr"),
                 compDiscrimFiledPrev: getVal("compDiscrimFiledPrev"), compDiscrimPendingPrev: getVal("compDiscrimPendingPrev"), compDiscrimRemarksPrev: getVal("compDiscrimRemarksPrev"),

                 compChildFiledCurr: getVal("compChildFiledCurr"), compChildPendingCurr: getVal("compChildPendingCurr"), compChildRemarksCurr: getVal("compChildRemarksCurr"),
                 compChildFiledPrev: getVal("compChildFiledPrev"), compChildPendingPrev: getVal("compChildPendingPrev"), compChildRemarksPrev: getVal("compChildRemarksPrev"),

                 compForcedFiledCurr: getVal("compForcedFiledCurr"), compForcedPendingCurr: getVal("compForcedPendingCurr"), compForcedRemarksCurr: getVal("compForcedRemarksCurr"),
                 compForcedFiledPrev: getVal("compForcedFiledPrev"), compForcedPendingPrev: getVal("compForcedPendingPrev"), compForcedRemarksPrev: getVal("compForcedRemarksPrev"),

                 compWagesFiledCurr: getVal("compWagesFiledCurr"), compWagesPendingCurr: getVal("compWagesPendingCurr"), compWagesRemarksCurr: getVal("compWagesRemarksCurr"),
                 compWagesFiledPrev: getVal("compWagesFiledPrev"), compWagesPendingPrev: getVal("compWagesPendingPrev"), compWagesRemarksPrev: getVal("compWagesRemarksPrev"),

                 compOtherHrFiledCurr: getVal("compOtherHrFiledCurr"), compOtherHrPendingCurr: getVal("compOtherHrPendingCurr"), compOtherHrRemarksCurr: getVal("compOtherHrRemarksCurr"),
                 compOtherHrFiledPrev: getVal("compOtherHrFiledPrev"), compOtherHrPendingPrev: getVal("compOtherHrPendingPrev"), compOtherHrRemarksPrev: getVal("compOtherHrRemarksPrev"),

                 hrComplaintsNote: getVal("hrComplaintsNote"),

         // --- Principle 5 Q7 ---
                 poshTotalCurr: getVal("poshTotalCurr"), poshTotalPrev: getVal("poshTotalPrev"),
                 poshPercCurr: getVal("poshPercCurr"), poshPercPrev: getVal("poshPercPrev"),
                 poshUpheldCurr: getVal("poshUpheldCurr"), poshUpheldPrev: getVal("poshUpheldPrev"),
                 poshNote: getVal("poshNote"),

          // --- Principle 5 Q8 ---
                 protectionMechanismsIntro: getVal("protectionMechanismsIntro"),
                 protectionMechanismsList: Array.from(document.querySelectorAll(".prot-mech-input")).map(input => input.value),
         // --- Principle 5 Q9 ---
                 humanRightsContracts: getVal("humanRightsContracts"),
                 humanRightsContractsDetails: getVal("humanRightsContractsDetails"),
          // --- Principle 5 Q10 ---
                 assessChildLabourPerc: getVal("assessChildLabourPerc"),
                 assessForcedLabourPerc: getVal("assessForcedLabourPerc"),
                 assessSexualHarassmentPerc: getVal("assessSexualHarassmentPerc"),
                 assessDiscriminationPerc: getVal("assessDiscriminationPerc"),
                 assessWagesPerc: getVal("assessWagesPerc"),
                 assessOthersPerc: getVal("assessOthersPerc"),
                 assessmentsP5Note: getVal("assessmentsP5Note"),
           // --- Principle 5 Q11 ---
                  assessCorrectiveIntro: getVal("assessCorrectiveIntro"),
                  assessCorrectiveActions: Array.from(document.querySelectorAll(".ac-action-input")).map(input => input.value),
         //princple 5 leadership
         //q1 and q2
         p5LeadProcessModIntro: getVal("p5LeadProcessModIntro"),
             p5LeadProcessModList: Array.from(document.querySelectorAll(".p5-mod-input")).map(input => input.value),

             p5LeadDueDiligenceScope: getVal("p5LeadDueDiligenceScope"),
             p5LeadDueDiligenceIssues: Array.from(document.querySelectorAll(".p5-issue-input")).map(input => input.value),
             p5LeadDueDiligenceHolders: Array.from(document.querySelectorAll(".p5-holder-input")).map(input => input.value),
         //q3
         p5LeadPremisesAccess: getVal("p5LeadPremisesAccess"),
         //q4
          p5LeadValueChainAssessment: getVal("p5LeadValueChainAssessment"),
         //q5
         p5LeadValueChainCorrectiveActions: getVal("p5LeadValueChainCorrectiveActions"),
         p5LeadVcAssessShPerc: getVal("p5LeadVcAssessShPerc"),
             p5LeadVcAssessDiscrimPerc: getVal("p5LeadVcAssessDiscrimPerc"),
             p5LeadVcAssessChildPerc: getVal("p5LeadVcAssessChildPerc"),
             p5LeadVcAssessForcedPerc: getVal("p5LeadVcAssessForcedPerc"),
             p5LeadVcAssessWagesPerc: getVal("p5LeadVcAssessWagesPerc"),
             p5LeadVcAssessOthersPerc: getVal("p5LeadVcAssessOthersPerc"),
    };

    // CALL BACKEND
    fetch("http://localhost:8080/api/report/generate", {
        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": "Bearer " + token },
        body: JSON.stringify(formData)
    })
        .then(async res => {
            if(res.ok) return res.blob();
            const text = await res.text();
            throw new Error("Server Error: " + text);
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "BRSR_Report_Final.docx";
            document.body.appendChild(a);
            a.click();
            a.remove();

            alert("Report Completed Successfully!");
            window.location.href = "industry_homepage.html";
        })
        .catch(err => alert("Error: " + err.message));
}
//helper for generate function principle 3
function scrapeWellBeingTable(tableId) {
    return Array.from(document.querySelectorAll(tableId + " tbody tr")).map(row => ({
        category: row.querySelector(".wb-cat").value,
        totalA: row.querySelector(".wb-tot").value,
        healthNo: row.querySelector(".wb-h-no").value,
        healthPerc: row.querySelector(".wb-h-pc").value,
        accidentNo: row.querySelector(".wb-a-no").value,
        accidentPerc: row.querySelector(".wb-a-pc").value,
        maternityNo: row.querySelector(".wb-m-no").value,
        maternityPerc: row.querySelector(".wb-m-pc").value,
        paternityNo: row.querySelector(".wb-p-no").value,
        paternityPerc: row.querySelector(".wb-p-pc").value,
        daycareNo: row.querySelector(".wb-d-no").value,
        daycarePerc: row.querySelector(".wb-d-pc").value
    }));
}

function saveAndNext() {
    const token = localStorage.getItem("token");

        // Check Mode
        const isRiskTableMode = document.querySelector('input[name="riskMode"][value="table"]').checked;
        let finalRiskList = [];
        let finalRiskNote = "";

        if (isRiskTableMode) {
            finalRiskList = Array.from(document.querySelectorAll("#riskTable tbody tr")).map(row => ({
                productName: row.querySelector(".rk-name").value,
                riskDescription: row.querySelector(".rk-desc").value,
                actionTaken: row.querySelector(".rk-act").value
            }));
        } else {
            finalRiskNote = document.getElementById("lcaRiskNote").value;
        }


        const formData = {
            id: parseInt(currentReportId),

            // --- Principle 6 (Environment) ---
            electricityConsumption: getVal("electricityConsumption"),
            fuelConsumption: getVal("fuelConsumption"),
            energyIntensity: getVal("energyIntensity"),
            scope1Emissions: getVal("scope1Emissions"),
            scope2Emissions: getVal("scope2Emissions"),
            emissionIntensity: getVal("emissionIntensity"),
            plasticWaste: getVal("plasticWaste"),
            eWaste: getVal("eWaste"),
            hazardousWaste: getVal("hazardousWaste"),
            totalWasteGenerated: getVal("totalWasteGenerated"),
            totalWasteRecycled: getVal("totalWasteRecycled"),

            // --- Principle 1 (Training) ---
            trainingPrograms: Array.from(document.querySelectorAll("#trainingTable tbody tr")).map(row => ({
                segment: row.querySelector(".tp-seg") ? row.querySelector(".tp-seg").value : "",
                totalPrograms: row.querySelector(".tp-num") ? row.querySelector(".tp-num").value : "",
                topicsCovered: row.querySelector(".tp-top") ? row.querySelector(".tp-top").value : "",
                percentageCovered: row.querySelector(".tp-perc") ? row.querySelector(".tp-perc").value : ""
            })),
            trainingNote: getVal("trainingNote"),

            // --- Question 2 (Legal) ---
            legalActionNote: getVal("legalActionNote"),

            // Safe check for checkbox
            nonMonetaryNA: document.getElementById("nonMonetaryNA") ? document.getElementById("nonMonetaryNA").checked : false,

            legalActions: Array.from(document.querySelectorAll("#legalTable tbody tr")).map(row => ({
                type: row.querySelector(".la-type") ? row.querySelector(".la-type").value : "",
                principle: row.querySelector(".la-prin") ? row.querySelector(".la-prin").value : "",
                agency: row.querySelector(".la-agency") ? row.querySelector(".la-agency").value : "",
                amount: row.querySelector(".la-amt") ? row.querySelector(".la-amt").value : "",
                brief: row.querySelector(".la-brief") ? row.querySelector(".la-brief").value : "",
                appeal: row.querySelector(".la-app") ? row.querySelector(".la-app").value : ""
            })),

            // --- SECTION C Q3 ---
            appealDetails: Array.from(document.querySelectorAll("#appealTable tbody tr")).map(row => ({
                caseDetails: row.querySelector(".ap-case").value,
                agencyName: row.querySelector(".ap-agency").value
            })),

            // --- Question 4 (Anti-Corruption) ---
            antiCorruptionPolicy: getVal("antiCorruptionPolicy"),
            antiCorruptionLink: getVal("antiCorruptionLink"),
            antiCorruptionDetails: getVal("antiCorruptionDetails"),

            // --- Question 5 ---
            disciplinaryFyCurrentHeader: getVal("disciplinaryFyCurrentHeader"),
            disciplinaryFyPreviousHeader: getVal("disciplinaryFyPreviousHeader"),

            discDirectorsCurr: getVal("discDirectorsCurr"),
            discKmpsCurr: getVal("discKmpsCurr"),
            discEmployeesCurr: getVal("discEmployeesCurr"),
            discWorkersCurr: getVal("discWorkersCurr"),

            discDirectorsPrev: getVal("discDirectorsPrev"),
            discKmpsPrev: getVal("discKmpsPrev"),
            discEmployeesPrev: getVal("discEmployeesPrev"),
            discWorkersPrev: getVal("discWorkersPrev"),

            // --- Question 6 ---
            coiDirectorsCurrNum: getVal("coiDirectorsCurrNum"),
            coiDirectorsCurrRem: getVal("coiDirectorsCurrRem"),
            coiDirectorsPrevNum: getVal("coiDirectorsPrevNum"),
            coiDirectorsPrevRem: getVal("coiDirectorsPrevRem"),

            coiKmpsCurrNum: getVal("coiKmpsCurrNum"),
            coiKmpsCurrRem: getVal("coiKmpsCurrRem"),
            coiKmpsPrevNum: getVal("coiKmpsPrevNum"),
            coiKmpsPrevRem: getVal("coiKmpsPrevRem"),

            // --- Question 7 ---
            correctiveActionDetails: getVal("correctiveActionDetails"),

            // --- Q8 Accounts Payable ---
                    accountsPayableCurr: getVal("accountsPayableCurr"),
                    accountsPayablePrev: getVal("accountsPayablePrev"),

                    // --- Q9 Openness of Business ---
                    purTradingPercCurr: getVal("purTradingPercCurr"), purTradingPercPrev: getVal("purTradingPercPrev"),
                    purTradingNumCurr: getVal("purTradingNumCurr"), purTradingNumPrev: getVal("purTradingNumPrev"),
                    purTop10PercCurr: getVal("purTop10PercCurr"), purTop10PercPrev: getVal("purTop10PercPrev"),

                    salesDealerPercCurr: getVal("salesDealerPercCurr"), salesDealerPercPrev: getVal("salesDealerPercPrev"),
                    salesDealerNumCurr: getVal("salesDealerNumCurr"), salesDealerNumPrev: getVal("salesDealerNumPrev"),
                    salesTop10PercCurr: getVal("salesTop10PercCurr"), salesTop10PercPrev: getVal("salesTop10PercPrev"),

                    rptPurCurr: getVal("rptPurCurr"), rptPurPrev: getVal("rptPurPrev"),
                    rptSalesCurr: getVal("rptSalesCurr"), rptSalesPrev: getVal("rptSalesPrev"),
                    rptLoansCurr: getVal("rptLoansCurr"), rptLoansPrev: getVal("rptLoansPrev"),
                    rptInvestCurr: getVal("rptInvestCurr"), rptInvestPrev: getVal("rptInvestPrev"),
                    opennessNote: getVal("opennessNote"),

            // --- Leadership Indicators ---
            leadershipIndicatorNote: getVal("leadershipIndicatorNote"),

            leadershipAwarenessPrograms: Array.from(document.querySelectorAll("#leadershipTable tbody tr")).map(row => ({
                totalCount: row.querySelector(".li-count").value,
                topic: row.querySelector(".li-topic").value,
                percentage: row.querySelector(".li-perc").value
            })),

            conflictOfInterestProcess: getVal("conflictOfInterestProcess"),
            conflictOfInterestDetails: getVal("conflictOfInterestDetails"),

            // Map the inputs to the shared FY fields in DTO
            fyCurrent: document.getElementById("p2FyCurrent").value,
            fyPrevious: document.getElementById("p2FyPrevious").value,

            // ... Principle 2 Data ...
            rdCurrentYear: getVal("rdCurrentYear"),
            rdPreviousYear: getVal("rdPreviousYear"),
            rdDetails: getVal("rdDetails"),
            capexCurrentYear: getVal("capexCurrentYear"),
            capexPreviousYear: getVal("capexPreviousYear"),
            capexDetails: getVal("capexDetails"),
            principle2Note: getVal("principle2Note"),

            // --- Principle 2 Q2 ---
            sustainableSourcingProcedures: getVal("sustainableSourcingProcedures"),
            sustainableSourcingDetails: getVal("sustainableSourcingDetails"),
            sustainableSourcingPercentage: getVal("sustainableSourcingPercentage"),
            sustainableSourcingRemarks: getVal("sustainableSourcingRemarks"),

            // --- Principle 2 Q3 ---
            reclaimProcessDetails: getVal("reclaimProcessDetails"),

            // --- Principle 2 Q4 ---
            eprDetails: getVal("eprDetails"),

            // --- Leadership LCA PRINCIPLE 2---

            // --- Principle 2 Leadership NA Flags ---
                    p2Lead1NA: document.getElementById("p2Lead1NA") ? document.getElementById("p2Lead1NA").checked : false,
                    p2Lead2NA: document.getElementById("p2Lead2NA") ? document.getElementById("p2Lead2NA").checked : false,
                    p2Lead3NA: document.getElementById("p2Lead3NA") ? document.getElementById("p2Lead3NA").checked : false,
                    p2Lead4NA: document.getElementById("p2Lead4NA") ? document.getElementById("p2Lead4NA").checked : false,
                    p2Lead5NA: document.getElementById("p2Lead5NA") ? document.getElementById("p2Lead5NA").checked : false,

            lcaNote: getVal("lcaNote"),
            lcaEntries: Array.from(document.querySelectorAll("#lcaTable tbody tr")).map(row => ({
                nicCode: row.querySelector(".lca-nic").value,
                productName: row.querySelector(".lca-name").value,
                turnoverPercentage: row.querySelector(".lca-turn").value,
                boundary: row.querySelector(".lca-bound").value,
                independentAgency: row.querySelector(".lca-agency").value,
                publicDomainResult: row.querySelector(".lca-res").value
            })),

            // --- Principle 2 leader Q2 ---
            lcaRiskNote: finalRiskNote,
            lcaRisks: finalRiskList,

            // --- Principle 2 leader Q3 ---
            recycledInputNote: getVal("recycledInputNote"),
            recycledInputs: Array.from(document.querySelectorAll("#recycledTable tbody tr")).map(row => ({
                materialName: row.querySelector(".rec-mat").value,
                currentFyPercentage: row.querySelector(".rec-cur").value,
                previousFyPercentage: row.querySelector(".rec-prev").value
            })),

            // --- Q4 (Fixed fields) ---
            plasticReusedCurr: getVal("plasticReusedCurr"), plasticRecycledCurr: getVal("plasticRecycledCurr"), plasticDisposedCurr: getVal("plasticDisposedCurr"),
            plasticReusedPrev: getVal("plasticReusedPrev"), plasticRecycledPrev: getVal("plasticRecycledPrev"), plasticDisposedPrev: getVal("plasticDisposedPrev"),

            ewasteReusedCurr: getVal("ewasteReusedCurr"), ewasteRecycledCurr: getVal("ewasteRecycledCurr"), ewasteDisposedCurr: getVal("ewasteDisposedCurr"),
            ewasteReusedPrev: getVal("ewasteReusedPrev"), ewasteRecycledPrev: getVal("ewasteRecycledPrev"), ewasteDisposedPrev: getVal("ewasteDisposedPrev"),

            hazardReusedCurr: getVal("hazardReusedCurr"), hazardRecycledCurr: getVal("hazardRecycledCurr"), hazardDisposedCurr: getVal("hazardDisposedCurr"),
            hazardReusedPrev: getVal("hazardReusedPrev"), hazardRecycledPrev: getVal("hazardRecycledPrev"), hazardDisposedPrev: getVal("hazardDisposedPrev"),

            otherReusedCurr: getVal("otherReusedCurr"), otherRecycledCurr: getVal("otherRecycledCurr"), otherDisposedCurr: getVal("otherDisposedCurr"),
            otherReusedPrev: getVal("otherReusedPrev"), otherRecycledPrev: getVal("otherRecycledPrev"), otherDisposedPrev: getVal("otherDisposedPrev"),

            reclaimedWasteNote: getVal("reclaimedWasteNote"),

            // --- Q5 (List) ---
            reclaimedPercentageNote: getVal("reclaimedPercentageNote"),
            reclaimedPercentages: Array.from(document.querySelectorAll("#reclaimedPercTable tbody tr")).map(row => ({
                category: row.querySelector(".rp-cat").value,
                percentage: row.querySelector(".rp-perc").value
            })),

            // --- Principle 3 ---
            wellBeingNote: getVal("wellBeingNote"),
            employeeWellBeing: scrapeWellBeingTable("#empWellBeingTable"),
            workerWellBeing: scrapeWellBeingTable("#workWellBeingTable"),

            // --- Principle 3 Q2 ---
            retirementBenefitNote: getVal("retirementBenefitNote"),
            retirementBenefits: Array.from(document.querySelectorAll("#retirementTable tbody tr")).map(row => ({
                benefits: row.querySelector(".rb-name").value,
                currEmpCovered: row.querySelector(".rb-ce").value,
                currWorkCovered: row.querySelector(".rb-cw").value,
                currDeducted: row.querySelector(".rb-cd").value,
                prevEmpCovered: row.querySelector(".rb-pe").value,
                prevWorkCovered: row.querySelector(".rb-pw").value,
                prevDeducted: row.querySelector(".rb-pd").value
            })),

            // --- Principle 3 Q3 ---
            accessibilityDetails: getVal("accessibilityDetails"),

            // --- Principle 3 Q4 ---
            equalOppPolicy: getVal("equalOppPolicy"),
            equalOppLink: getVal("equalOppLink"),
            equalOppDetails: getVal("equalOppDetails"),

            // --- Principle 3 Q5 ---
            plEmpMaleReturn: getVal("plEmpMaleReturn"), plEmpMaleRetain: getVal("plEmpMaleRetain"),
            plWorkMaleReturn: getVal("plWorkMaleReturn"), plWorkMaleRetain: getVal("plWorkMaleRetain"),

            plEmpFemaleReturn: getVal("plEmpFemaleReturn"), plEmpFemaleRetain: getVal("plEmpFemaleRetain"),
            plWorkFemaleReturn: getVal("plWorkFemaleReturn"), plWorkFemaleRetain: getVal("plWorkFemaleRetain"),

            plEmpTotalReturn: getVal("plEmpTotalReturn"), plEmpTotalRetain: getVal("plEmpTotalRetain"),
            plWorkTotalReturn: getVal("plWorkTotalReturn"), plWorkTotalRetain: getVal("plWorkTotalRetain"),

            parentalLeaveNote: getVal("parentalLeaveNote"),

            // --- Principle 3 Q6 ---
            grievancePermWorkers: getVal("grievancePermWorkers"),
            grievanceTempWorkers: getVal("grievanceTempWorkers"),
            grievancePermEmployees: getVal("grievancePermEmployees"),
            grievanceTempEmployees: getVal("grievanceTempEmployees"),

            // --- SECTION C: P3 Q7 (UNION MEMBERSHIP) ---

            // 1. Current Employees
            unionCurrEmpTotalA: getVal("unionCurrEmpTotalA"),
            unionCurrEmpUnionB: getVal("unionCurrEmpUnionB"),
            unionCurrEmpPerc: getVal("unionCurrEmpPerc"),

            unionCurrEmpMaleTotal: getVal("unionCurrEmpMaleTotal"),
            unionCurrEmpMaleUnion: getVal("unionCurrEmpMaleUnion"),
            unionCurrEmpMalePerc: getVal("unionCurrEmpMalePerc"),

            unionCurrEmpFemaleTotal: getVal("unionCurrEmpFemaleTotal"),
            unionCurrEmpFemaleUnion: getVal("unionCurrEmpFemaleUnion"),
            unionCurrEmpFemalePerc: getVal("unionCurrEmpFemalePerc"),

            // 2. Current Workers
            unionCurrWorkTotalA: getVal("unionCurrWorkTotalA"),
            unionCurrWorkUnionB: getVal("unionCurrWorkUnionB"),
            unionCurrWorkPerc: getVal("unionCurrWorkPerc"),

            unionCurrWorkMaleTotal: getVal("unionCurrWorkMaleTotal"),
            unionCurrWorkMaleUnion: getVal("unionCurrWorkMaleUnion"),
            unionCurrWorkMalePerc: getVal("unionCurrWorkMalePerc"),

            unionCurrWorkFemaleTotal: getVal("unionCurrWorkFemaleTotal"),
            unionCurrWorkFemaleUnion: getVal("unionCurrWorkFemaleUnion"),
            unionCurrWorkFemalePerc: getVal("unionCurrWorkFemalePerc"),

            // 3. Previous Employees
            unionPrevEmpTotalC: getVal("unionPrevEmpTotalC"),
            unionPrevEmpUnionD: getVal("unionPrevEmpUnionD"),
            unionPrevEmpPerc: getVal("unionPrevEmpPerc"),

            unionPrevEmpMaleTotal: getVal("unionPrevEmpMaleTotal"),
            unionPrevEmpMaleUnion: getVal("unionPrevEmpMaleUnion"),
            unionPrevEmpMalePerc: getVal("unionPrevEmpMalePerc"),

            unionPrevEmpFemaleTotal: getVal("unionPrevEmpFemaleTotal"),
            unionPrevEmpFemaleUnion: getVal("unionPrevEmpFemaleUnion"),
            unionPrevEmpFemalePerc: getVal("unionPrevEmpFemalePerc"),

            // 4. Previous Workers
            unionPrevWorkTotalC: getVal("unionPrevWorkTotalC"),
            unionPrevWorkUnionD: getVal("unionPrevWorkUnionD"),
            unionPrevWorkPerc: getVal("unionPrevWorkPerc"),

            unionPrevWorkMaleTotal: getVal("unionPrevWorkMaleTotal"),
            unionPrevWorkMaleUnion: getVal("unionPrevWorkMaleUnion"),
            unionPrevWorkMalePerc: getVal("unionPrevWorkMalePerc"),

            unionPrevWorkFemaleTotal: getVal("unionPrevWorkFemaleTotal"),
            unionPrevWorkFemaleUnion: getVal("unionPrevWorkFemaleUnion"),
            unionPrevWorkFemalePerc: getVal("unionPrevWorkFemalePerc"),

            // Note
            unionMembershipNote: getVal("unionMembershipNote"),

            // MAPPING
            trainEmpMaleTotal: getVal("trainEmpMaleTotal"), trainEmpMaleHealthNo: getVal("trainEmpMaleHealthNo"), trainEmpMaleHealthPerc: getVal("trainEmpMaleHealthPerc"), trainEmpMaleSkillNo: getVal("trainEmpMaleSkillNo"), trainEmpMaleSkillPerc: getVal("trainEmpMaleSkillPerc"),
            trainEmpFemaleTotal: getVal("trainEmpFemaleTotal"), trainEmpFemaleHealthNo: getVal("trainEmpFemaleHealthNo"), trainEmpFemaleHealthPerc: getVal("trainEmpFemaleHealthPerc"), trainEmpFemaleSkillNo: getVal("trainEmpFemaleSkillNo"), trainEmpFemaleSkillPerc: getVal("trainEmpFemaleSkillPerc"),
            trainEmpGenTotal: getVal("trainEmpGenTotal"), trainEmpGenHealthNo: getVal("trainEmpGenHealthNo"), trainEmpGenHealthPerc: getVal("trainEmpGenHealthPerc"), trainEmpGenSkillNo: getVal("trainEmpGenSkillNo"), trainEmpGenSkillPerc: getVal("trainEmpGenSkillPerc"),

            trainEmpMaleTotalPrev: getVal("trainEmpMaleTotalPrev"), trainEmpMaleHealthNoPrev: getVal("trainEmpMaleHealthNoPrev"), trainEmpMaleHealthPercPrev: getVal("trainEmpMaleHealthPercPrev"), trainEmpMaleSkillNoPrev: getVal("trainEmpMaleSkillNoPrev"), trainEmpMaleSkillPercPrev: getVal("trainEmpMaleSkillPercPrev"),
            trainEmpFemaleTotalPrev: getVal("trainEmpFemaleTotalPrev"), trainEmpFemaleHealthNoPrev: getVal("trainEmpFemaleHealthNoPrev"), trainEmpFemaleHealthPercPrev: getVal("trainEmpFemaleHealthPercPrev"), trainEmpFemaleSkillNoPrev: getVal("trainEmpFemaleSkillNoPrev"), trainEmpFemaleSkillPercPrev: getVal("trainEmpFemaleSkillPercPrev"),
            trainEmpGenTotalPrev: getVal("trainEmpGenTotalPrev"), trainEmpGenHealthNoPrev: getVal("trainEmpGenHealthNoPrev"), trainEmpGenHealthPercPrev: getVal("trainEmpGenHealthPercPrev"), trainEmpGenSkillNoPrev: getVal("trainEmpGenSkillNoPrev"), trainEmpGenSkillPercPrev: getVal("trainEmpGenSkillPercPrev"),

            trainWorkMaleTotal: getVal("trainWorkMaleTotal"), trainWorkMaleHealthNo: getVal("trainWorkMaleHealthNo"), trainWorkMaleHealthPerc: getVal("trainWorkMaleHealthPerc"), trainWorkMaleSkillNo: getVal("trainWorkMaleSkillNo"), trainWorkMaleSkillPerc: getVal("trainWorkMaleSkillPerc"),
            trainWorkFemaleTotal: getVal("trainWorkFemaleTotal"), trainWorkFemaleHealthNo: getVal("trainWorkFemaleHealthNo"), trainWorkFemaleHealthPerc: getVal("trainWorkFemaleHealthPerc"), trainWorkFemaleSkillNo: getVal("trainWorkFemaleSkillNo"), trainWorkFemaleSkillPerc: getVal("trainWorkFemaleSkillPerc"),
            trainWorkGenTotal: getVal("trainWorkGenTotal"), trainWorkGenHealthNo: getVal("trainWorkGenHealthNo"), trainWorkGenHealthPerc: getVal("trainWorkGenHealthPerc"), trainWorkGenSkillNo: getVal("trainWorkGenSkillNo"), trainWorkGenSkillPerc: getVal("trainWorkGenSkillPerc"),

            trainWorkMaleTotalPrev: getVal("trainWorkMaleTotalPrev"), trainWorkMaleHealthNoPrev: getVal("trainWorkMaleHealthNoPrev"), trainWorkMaleHealthPercPrev: getVal("trainWorkMaleHealthPercPrev"), trainWorkMaleSkillNoPrev: getVal("trainWorkMaleSkillNoPrev"), trainWorkMaleSkillPercPrev: getVal("trainWorkMaleSkillPercPrev"),
            trainWorkFemaleTotalPrev: getVal("trainWorkFemaleTotalPrev"), trainWorkFemaleHealthNoPrev: getVal("trainWorkFemaleHealthNoPrev"), trainWorkFemaleHealthPercPrev: getVal("trainWorkFemaleHealthPercPrev"), trainWorkFemaleSkillNoPrev: getVal("trainWorkFemaleSkillNoPrev"), trainWorkFemaleSkillPercPrev: getVal("trainWorkFemaleSkillPercPrev"),
            trainWorkGenTotalPrev: getVal("trainWorkGenTotalPrev"), trainWorkGenHealthNoPrev: getVal("trainWorkGenHealthNoPrev"), trainWorkGenHealthPercPrev: getVal("trainWorkGenHealthPercPrev"), trainWorkGenSkillNoPrev: getVal("trainWorkGenSkillNoPrev"), trainWorkGenSkillPercPrev: getVal("trainWorkGenSkillPercPrev"),

            trainingDetailsNote: getVal("trainingDetailsNote"),

            // --- Q9 PERFORMANCE REVIEWS ---
            reviewDetailsNote: getVal("reviewDetailsNote"),

            // Employees
            revEmpMaleTotal: getVal("revEmpMaleTotal"), revEmpMaleCovered: getVal("revEmpMaleCovered"), revEmpMalePerc: getVal("revEmpMalePerc"),
            revEmpFemTotal: getVal("revEmpFemTotal"), revEmpFemCovered: getVal("revEmpFemCovered"), revEmpFemPerc: getVal("revEmpFemPerc"),
            revEmpOthTotal: getVal("revEmpOthTotal"), revEmpOthCovered: getVal("revEmpOthCovered"), revEmpOthPerc: getVal("revEmpOthPerc"),
            revEmpGenTotal: getVal("revEmpGenTotal"), revEmpGenCovered: getVal("revEmpGenCovered"), revEmpGenPerc: getVal("revEmpGenPerc"),

            revEmpMaleTotalPrev: getVal("revEmpMaleTotalPrev"), revEmpMaleCoveredPrev: getVal("revEmpMaleCoveredPrev"), revEmpMalePercPrev: getVal("revEmpMalePercPrev"),
            revEmpFemTotalPrev: getVal("revEmpFemTotalPrev"), revEmpFemCoveredPrev: getVal("revEmpFemCoveredPrev"), revEmpFemPercPrev: getVal("revEmpFemPercPrev"),
            revEmpOthTotalPrev: getVal("revEmpOthTotalPrev"), revEmpOthCoveredPrev: getVal("revEmpOthCoveredPrev"), revEmpOthPercPrev: getVal("revEmpOthPercPrev"),
            revEmpGenTotalPrev: getVal("revEmpGenTotalPrev"), revEmpGenCoveredPrev: getVal("revEmpGenCoveredPrev"), revEmpGenPercPrev: getVal("revEmpGenPercPrev"),

            // Workers
            revWorkMaleTotal: getVal("revWorkMaleTotal"), revWorkMaleCovered: getVal("revWorkMaleCovered"), revWorkMalePerc: getVal("revWorkMalePerc"),
            revWorkFemTotal: getVal("revWorkFemTotal"), revWorkFemCovered: getVal("revWorkFemCovered"), revWorkFemPerc: getVal("revWorkFemPerc"),
            revWorkOthTotal: getVal("revWorkOthTotal"), revWorkOthCovered: getVal("revWorkOthCovered"), revWorkOthPerc: getVal("revWorkOthPerc"),
            revWorkGenTotal: getVal("revWorkGenTotal"), revWorkGenCovered: getVal("revWorkGenCovered"), revWorkGenPerc: getVal("revWorkGenPerc"),

            revWorkMaleTotalPrev: getVal("revWorkMaleTotalPrev"), revWorkMaleCoveredPrev: getVal("revWorkMaleCoveredPrev"), revWorkMalePercPrev: getVal("revWorkMalePercPrev"),
            revWorkFemTotalPrev: getVal("revWorkFemTotalPrev"), revWorkFemCoveredPrev: getVal("revWorkFemCoveredPrev"), revWorkFemPercPrev: getVal("revWorkFemPercPrev"),
            revWorkOthTotalPrev: getVal("revWorkOthTotalPrev"), revWorkOthCoveredPrev: getVal("revWorkOthCoveredPrev"), revWorkOthPercPrev: getVal("revWorkOthPercPrev"),
            revWorkGenTotalPrev: getVal("revWorkGenTotalPrev"), revWorkGenCoveredPrev: getVal("revWorkGenCoveredPrev"), revWorkGenPercPrev: getVal("revWorkGenPercPrev"),

            // --- Principle 3 Q10 ---
            healthSafetySystem: getVal("healthSafetySystem"),
            hazardIdentification: getVal("hazardIdentification"),
            hazardReporting: getVal("hazardReporting"),
            medicalAccess: getVal("medicalAccess"),

            // --- Principle 3 Q11 ---
            safetyLtifrEmpCurr: getVal("safetyLtifrEmpCurr"), safetyLtifrEmpPrev: getVal("safetyLtifrEmpPrev"),
            safetyLtifrWorkCurr: getVal("safetyLtifrWorkCurr"), safetyLtifrWorkPrev: getVal("safetyLtifrWorkPrev"),

            safetyTotalInjEmpCurr: getVal("safetyTotalInjEmpCurr"), safetyTotalInjEmpPrev: getVal("safetyTotalInjEmpPrev"),
            safetyTotalInjWorkCurr: getVal("safetyTotalInjWorkCurr"), safetyTotalInjWorkPrev: getVal("safetyTotalInjWorkPrev"),

            safetyFatalEmpCurr: getVal("safetyFatalEmpCurr"), safetyFatalEmpPrev: getVal("safetyFatalEmpPrev"),
            safetyFatalWorkCurr: getVal("safetyFatalWorkCurr"), safetyFatalWorkPrev: getVal("safetyFatalWorkPrev"),

            safetyHighConEmpCurr: getVal("safetyHighConEmpCurr"), safetyHighConEmpPrev: getVal("safetyHighConEmpPrev"),
            safetyHighConWorkCurr: getVal("safetyHighConWorkCurr"), safetyHighConWorkPrev: getVal("safetyHighConWorkPrev"),

            safetyPermDisEmpCurr: getVal("safetyPermDisEmpCurr"), safetyPermDisEmpPrev: getVal("safetyPermDisEmpPrev"),
            safetyPermDisWorkCurr: getVal("safetyPermDisWorkCurr"), safetyPermDisWorkPrev: getVal("safetyPermDisWorkPrev"),

            safetyIncidentsNote: getVal("safetyIncidentsNote"),

            // --- Q12 ---
            safetyMeasures: Array.from(document.querySelectorAll(".safety-measure-item")).map(item => ({
                heading: item.querySelector(".sm-head").value,
                description: item.querySelector(".sm-desc").value
            })),

            // --- Principle 3 Q13 ---
            wcFiledCurr: getVal("wcFiledCurr"), wcPendingCurr: getVal("wcPendingCurr"), wcRemarksCurr: getVal("wcRemarksCurr"),
            wcFiledPrev: getVal("wcFiledPrev"), wcPendingPrev: getVal("wcPendingPrev"), wcRemarksPrev: getVal("wcRemarksPrev"),

            hsFiledCurr: getVal("hsFiledCurr"), hsPendingCurr: getVal("hsPendingCurr"), hsRemarksCurr: getVal("hsRemarksCurr"),
            hsFiledPrev: getVal("hsFiledPrev"), hsPendingPrev: getVal("hsPendingPrev"), hsRemarksPrev: getVal("hsRemarksPrev"),

            workerComplaintsNote: getVal("workerComplaintsNote"),

            // --- Principle 3 Q14 ---
            assessmentHealthPerc: getVal("assessmentHealthPerc"),
            assessmentWorkingPerc: getVal("assessmentWorkingPerc"),
            assessmentNote: getVal("assessmentNote"),

            // --- Principle 3 Q15 ---
            safetyCorrectiveActions: getVal("safetyCorrectiveActions"),

            // --- Principle 3 Leadership 1 ---
            lifeInsuranceEmployees: getVal("lifeInsuranceEmployees"),
            lifeInsuranceWorkers: getVal("lifeInsuranceWorkers"),
            lifeInsuranceDetails: getVal("lifeInsuranceDetails"),

            // --- Principle 3 Leadership 2 ---
            statutoryDuesMeasures: getVal("statutoryDuesMeasures"),

            // --- Principle 3 Leadership 3 ---
            rehabEmpAffCurr: getVal("rehabEmpAffCurr"), rehabEmpAffPrev: getVal("rehabEmpAffPrev"),
            rehabEmpPlacedCurr: getVal("rehabEmpPlacedCurr"), rehabEmpPlacedPrev: getVal("rehabEmpPlacedPrev"),

            rehabWorkAffCurr: getVal("rehabWorkAffCurr"), rehabWorkAffPrev: getVal("rehabWorkAffPrev"),
            rehabWorkPlacedCurr: getVal("rehabWorkPlacedCurr"), rehabWorkPlacedPrev: getVal("rehabWorkPlacedPrev"),

            rehabilitationNote: getVal("rehabilitationNote"),

            // --- Principle 3 Leadership 4 ---
            transitionAssistanceDetails: getVal("transitionAssistanceDetails"),

            // --- Principle 3 Leadership 5 ---
            valueChainAssessmentNote: getVal("valueChainAssessmentNote"),
            vcHealthSafetyPerc: getVal("vcHealthSafetyPerc"),
            vcWorkingCondPerc: getVal("vcWorkingCondPerc"),

            // --- Principle 3 Leadership 6 ---
            vcCorrectiveActionIntro: getVal("vcCorrectiveActionIntro"),
            vcCorrectiveActions: Array.from(document.querySelectorAll(".vc-action-item")).map(item => ({
                actionText: item.querySelector(".vc-act-text").value
            })),

            // --- Principle 4 Q1 ---
            principle4Q1Intro: getVal("principle4Q1Intro"),
            principle4Q1Conclusion: getVal("principle4Q1Conclusion"),

            principle4Q1Points: Array.from(document.querySelectorAll(".p4-pt-input")).map(input => input.value),

            // --- Principle 4 Q2 ---
            stakeholderNote: getVal("stakeholderNote"),
            stakeholderEngagements: Array.from(document.querySelectorAll("#stakeholderTable tbody tr")).map(row => ({
                groupName: row.querySelector(".st-name").value,
                isVulnerable: row.querySelector(".st-vul").value,
                channels: row.querySelector(".st-chan").value,
                frequency: row.querySelector(".st-freq").value,
                purpose: row.querySelector(".st-purp").value
            })),

            // --- Principle 4 Leadership 1 ---
            consultationProcessDetails: getVal("consultationProcessDetails"),

            // --- Principle 4 Leadership 2 ---
            stakeholderConsultationUsed: getVal("stakeholderConsultationUsed"),
            stakeholderConsultationDetails: getVal("stakeholderConsultationDetails"),

            // --- Principle 4 Leadership 3 ---
            vulnerableGroupIntro: getVal("vulnerableGroupIntro"),
            vulnerableGroupConclusion: getVal("vulnerableGroupConclusion"),
            vulnerableGroupActions: Array.from(document.querySelectorAll(".vul-act-input")).map(input => input.value),

            // --- PRINCIPLE 5: HUMAN RIGHTS TRAINING ---

            // 1. Employees - Permanent
            hrEmpPermTotalCurr: getVal("hrEmpPermTotalCurr"),
            hrEmpPermCovCurr: getVal("hrEmpPermCovCurr"),
            hrEmpPermPercCurr: getVal("hrEmpPermPercCurr"),
            hrEmpPermTotalPrev: getVal("hrEmpPermTotalPrev"),
            hrEmpPermCovPrev: getVal("hrEmpPermCovPrev"),
            hrEmpPermPercPrev: getVal("hrEmpPermPercPrev"),

            // 2. Employees - Temporary (Other than Permanent)
            hrEmpTempTotalCurr: getVal("hrEmpTempTotalCurr"),
            hrEmpTempCovCurr: getVal("hrEmpTempCovCurr"),
            hrEmpTempPercCurr: getVal("hrEmpTempPercCurr"),
            hrEmpTempTotalPrev: getVal("hrEmpTempTotalPrev"),
            hrEmpTempCovPrev: getVal("hrEmpTempCovPrev"),
            hrEmpTempPercPrev: getVal("hrEmpTempPercPrev"),

            // 3. Employees - Total
            hrEmpGrandTotalCurr: getVal("hrEmpGrandTotalCurr"),
            hrEmpGrandCovCurr: getVal("hrEmpGrandCovCurr"),
            hrEmpGrandPercCurr: getVal("hrEmpGrandPercCurr"),
            hrEmpGrandTotalPrev: getVal("hrEmpGrandTotalPrev"),
            hrEmpGrandCovPrev: getVal("hrEmpGrandCovPrev"),
            hrEmpGrandPercPrev: getVal("hrEmpGrandPercPrev"),

            // 4. Workers - Permanent
            hrWorkPermTotalCurr: getVal("hrWorkPermTotalCurr"),
            hrWorkPermCovCurr: getVal("hrWorkPermCovCurr"),
            hrWorkPermPercCurr: getVal("hrWorkPermPercCurr"),
            hrWorkPermTotalPrev: getVal("hrWorkPermTotalPrev"),
            hrWorkPermCovPrev: getVal("hrWorkPermCovPrev"),
            hrWorkPermPercPrev: getVal("hrWorkPermPercPrev"),

            // 5. Workers - Temporary (Other than Permanent)
            hrWorkTempTotalCurr: getVal("hrWorkTempTotalCurr"),
            hrWorkTempCovCurr: getVal("hrWorkTempCovCurr"),
            hrWorkTempPercCurr: getVal("hrWorkTempPercCurr"),
            hrWorkTempTotalPrev: getVal("hrWorkTempTotalPrev"),
            hrWorkTempCovPrev: getVal("hrWorkTempCovPrev"),
            hrWorkTempPercPrev: getVal("hrWorkTempPercPrev"),

            // 6. Workers - Total
            hrWorkGrandTotalCurr: getVal("hrWorkGrandTotalCurr"),
            hrWorkGrandCovCurr: getVal("hrWorkGrandCovCurr"),
            hrWorkGrandPercCurr: getVal("hrWorkGrandPercCurr"),
            hrWorkGrandTotalPrev: getVal("hrWorkGrandTotalPrev"),
            hrWorkGrandCovPrev: getVal("hrWorkGrandCovPrev"),
            hrWorkGrandPercPrev: getVal("hrWorkGrandPercPrev"),

            // Note
            hrTrainingNote: getVal("hrTrainingNote"),

            // --- SECTION C: P5 Q2 (MINIMUM WAGES) ---
            minWageNote: getVal("minWageNote"),

            // 1. EMPLOYEES - PERMANENT
            mwEmpPermMaleTotalCurr: getVal("mwEmpPermMaleTotalCurr"), mwEmpPermMaleEqNoCurr: getVal("mwEmpPermMaleEqNoCurr"), mwEmpPermMaleEqPercCurr: getVal("mwEmpPermMaleEqPercCurr"), mwEmpPermMaleMoreNoCurr: getVal("mwEmpPermMaleMoreNoCurr"), mwEmpPermMaleMorePercCurr: getVal("mwEmpPermMaleMorePercCurr"),
            mwEmpPermMaleTotalPrev: getVal("mwEmpPermMaleTotalPrev"), mwEmpPermMaleEqNoPrev: getVal("mwEmpPermMaleEqNoPrev"), mwEmpPermMaleEqPercPrev: getVal("mwEmpPermMaleEqPercPrev"), mwEmpPermMaleMoreNoPrev: getVal("mwEmpPermMaleMoreNoPrev"), mwEmpPermMaleMorePercPrev: getVal("mwEmpPermMaleMorePercPrev"),

            mwEmpPermFemTotalCurr: getVal("mwEmpPermFemTotalCurr"), mwEmpPermFemEqNoCurr: getVal("mwEmpPermFemEqNoCurr"), mwEmpPermFemEqPercCurr: getVal("mwEmpPermFemEqPercCurr"), mwEmpPermFemMoreNoCurr: getVal("mwEmpPermFemMoreNoCurr"), mwEmpPermFemMorePercCurr: getVal("mwEmpPermFemMorePercCurr"),
            mwEmpPermFemTotalPrev: getVal("mwEmpPermFemTotalPrev"), mwEmpPermFemEqNoPrev: getVal("mwEmpPermFemEqNoPrev"), mwEmpPermFemEqPercPrev: getVal("mwEmpPermFemEqPercPrev"), mwEmpPermFemMoreNoPrev: getVal("mwEmpPermFemMoreNoPrev"), mwEmpPermFemMorePercPrev: getVal("mwEmpPermFemMorePercPrev"),

            mwEmpPermOthTotalCurr: getVal("mwEmpPermOthTotalCurr"), mwEmpPermOthEqNoCurr: getVal("mwEmpPermOthEqNoCurr"), mwEmpPermOthEqPercCurr: getVal("mwEmpPermOthEqPercCurr"), mwEmpPermOthMoreNoCurr: getVal("mwEmpPermOthMoreNoCurr"), mwEmpPermOthMorePercCurr: getVal("mwEmpPermOthMorePercCurr"),
            mwEmpPermOthTotalPrev: getVal("mwEmpPermOthTotalPrev"), mwEmpPermOthEqNoPrev: getVal("mwEmpPermOthEqNoPrev"), mwEmpPermOthEqPercPrev: getVal("mwEmpPermOthEqPercPrev"), mwEmpPermOthMoreNoPrev: getVal("mwEmpPermOthMoreNoPrev"), mwEmpPermOthMorePercPrev: getVal("mwEmpPermOthMorePercPrev"),

            // 2. EMPLOYEES - TEMPORARY
            mwEmpTempMaleTotalCurr: getVal("mwEmpTempMaleTotalCurr"), mwEmpTempMaleEqNoCurr: getVal("mwEmpTempMaleEqNoCurr"), mwEmpTempMaleEqPercCurr: getVal("mwEmpTempMaleEqPercCurr"), mwEmpTempMaleMoreNoCurr: getVal("mwEmpTempMaleMoreNoCurr"), mwEmpTempMaleMorePercCurr: getVal("mwEmpTempMaleMorePercCurr"),
            mwEmpTempMaleTotalPrev: getVal("mwEmpTempMaleTotalPrev"), mwEmpTempMaleEqNoPrev: getVal("mwEmpTempMaleEqNoPrev"), mwEmpTempMaleEqPercPrev: getVal("mwEmpTempMaleEqPercPrev"), mwEmpTempMaleMoreNoPrev: getVal("mwEmpTempMaleMoreNoPrev"), mwEmpTempMaleMorePercPrev: getVal("mwEmpTempMaleMorePercPrev"),

            mwEmpTempFemTotalCurr: getVal("mwEmpTempFemTotalCurr"), mwEmpTempFemEqNoCurr: getVal("mwEmpTempFemEqNoCurr"), mwEmpTempFemEqPercCurr: getVal("mwEmpTempFemEqPercCurr"), mwEmpTempFemMoreNoCurr: getVal("mwEmpTempFemMoreNoCurr"), mwEmpTempFemMorePercCurr: getVal("mwEmpTempFemMorePercCurr"),
            mwEmpTempFemTotalPrev: getVal("mwEmpTempFemTotalPrev"), mwEmpTempFemEqNoPrev: getVal("mwEmpTempFemEqNoPrev"), mwEmpTempFemEqPercPrev: getVal("mwEmpTempFemEqPercPrev"), mwEmpTempFemMoreNoPrev: getVal("mwEmpTempFemMoreNoPrev"), mwEmpTempFemMorePercPrev: getVal("mwEmpTempFemMorePercPrev"),

            mwEmpTempOthTotalCurr: getVal("mwEmpTempOthTotalCurr"), mwEmpTempOthEqNoCurr: getVal("mwEmpTempOthEqNoCurr"), mwEmpTempOthEqPercCurr: getVal("mwEmpTempOthEqPercCurr"), mwEmpTempOthMoreNoCurr: getVal("mwEmpTempOthMoreNoCurr"), mwEmpTempOthMorePercCurr: getVal("mwEmpTempOthMorePercCurr"),
            mwEmpTempOthTotalPrev: getVal("mwEmpTempOthTotalPrev"), mwEmpTempOthEqNoPrev: getVal("mwEmpTempOthEqNoPrev"), mwEmpTempOthEqPercPrev: getVal("mwEmpTempOthEqPercPrev"), mwEmpTempOthMoreNoPrev: getVal("mwEmpTempOthMoreNoPrev"), mwEmpTempOthMorePercPrev: getVal("mwEmpTempOthMorePercPrev"),

            // 3. WORKERS - PERMANENT
            mwWorkPermMaleTotalCurr: getVal("mwWorkPermMaleTotalCurr"), mwWorkPermMaleEqNoCurr: getVal("mwWorkPermMaleEqNoCurr"), mwWorkPermMaleEqPercCurr: getVal("mwWorkPermMaleEqPercCurr"), mwWorkPermMaleMoreNoCurr: getVal("mwWorkPermMaleMoreNoCurr"), mwWorkPermMaleMorePercCurr: getVal("mwWorkPermMaleMorePercCurr"),
            mwWorkPermMaleTotalPrev: getVal("mwWorkPermMaleTotalPrev"), mwWorkPermMaleEqNoPrev: getVal("mwWorkPermMaleEqNoPrev"), mwWorkPermMaleEqPercPrev: getVal("mwWorkPermMaleEqPercPrev"), mwWorkPermMaleMoreNoPrev: getVal("mwWorkPermMaleMoreNoPrev"), mwWorkPermMaleMorePercPrev: getVal("mwWorkPermMaleMorePercPrev"),

            mwWorkPermFemTotalCurr: getVal("mwWorkPermFemTotalCurr"), mwWorkPermFemEqNoCurr: getVal("mwWorkPermFemEqNoCurr"), mwWorkPermFemEqPercCurr: getVal("mwWorkPermFemEqPercCurr"), mwWorkPermFemMoreNoCurr: getVal("mwWorkPermFemMoreNoCurr"), mwWorkPermFemMorePercCurr: getVal("mwWorkPermFemMorePercCurr"),
            mwWorkPermFemTotalPrev: getVal("mwWorkPermFemTotalPrev"), mwWorkPermFemEqNoPrev: getVal("mwWorkPermFemEqNoPrev"), mwWorkPermFemEqPercPrev: getVal("mwWorkPermFemEqPercPrev"), mwWorkPermFemMoreNoPrev: getVal("mwWorkPermFemMoreNoPrev"), mwWorkPermFemMorePercPrev: getVal("mwWorkPermFemMorePercPrev"),

            mwWorkPermOthTotalCurr: getVal("mwWorkPermOthTotalCurr"), mwWorkPermOthEqNoCurr: getVal("mwWorkPermOthEqNoCurr"), mwWorkPermOthEqPercCurr: getVal("mwWorkPermOthEqPercCurr"), mwWorkPermOthMoreNoCurr: getVal("mwWorkPermOthMoreNoCurr"), mwWorkPermOthMorePercCurr: getVal("mwWorkPermOthMorePercCurr"),
            mwWorkPermOthTotalPrev: getVal("mwWorkPermOthTotalPrev"), mwWorkPermOthEqNoPrev: getVal("mwWorkPermOthEqNoPrev"), mwWorkPermOthEqPercPrev: getVal("mwWorkPermOthEqPercPrev"), mwWorkPermOthMoreNoPrev: getVal("mwWorkPermOthMoreNoPrev"), mwWorkPermOthMorePercPrev: getVal("mwWorkPermOthMorePercPrev"),

            // 4. WORKERS - TEMPORARY
            mwWorkTempMaleTotalCurr: getVal("mwWorkTempMaleTotalCurr"), mwWorkTempMaleEqNoCurr: getVal("mwWorkTempMaleEqNoCurr"), mwWorkTempMaleEqPercCurr: getVal("mwWorkTempMaleEqPercCurr"), mwWorkTempMaleMoreNoCurr: getVal("mwWorkTempMaleMoreNoCurr"), mwWorkTempMaleMorePercCurr: getVal("mwWorkTempMaleMorePercCurr"),
            mwWorkTempMaleTotalPrev: getVal("mwWorkTempMaleTotalPrev"), mwWorkTempMaleEqNoPrev: getVal("mwWorkTempMaleEqNoPrev"), mwWorkTempMaleEqPercPrev: getVal("mwWorkTempMaleEqPercPrev"), mwWorkTempMaleMoreNoPrev: getVal("mwWorkTempMaleMoreNoPrev"), mwWorkTempMaleMorePercPrev: getVal("mwWorkTempMaleMorePercPrev"),

            mwWorkTempFemTotalCurr: getVal("mwWorkTempFemTotalCurr"), mwWorkTempFemEqNoCurr: getVal("mwWorkTempFemEqNoCurr"), mwWorkTempFemEqPercCurr: getVal("mwWorkTempFemEqPercCurr"), mwWorkTempFemMoreNoCurr: getVal("mwWorkTempFemMoreNoCurr"), mwWorkTempFemMorePercCurr: getVal("mwWorkTempFemMorePercCurr"),
            mwWorkTempFemTotalPrev: getVal("mwWorkTempFemTotalPrev"), mwWorkTempFemEqNoPrev: getVal("mwWorkTempFemEqNoPrev"), mwWorkTempFemEqPercPrev: getVal("mwWorkTempFemEqPercPrev"), mwWorkTempFemMoreNoPrev: getVal("mwWorkTempFemMoreNoPrev"), mwWorkTempFemMorePercPrev: getVal("mwWorkTempFemMorePercPrev"),

            mwWorkTempOthTotalCurr: getVal("mwWorkTempOthTotalCurr"), mwWorkTempOthEqNoCurr: getVal("mwWorkTempOthEqNoCurr"), mwWorkTempOthEqPercCurr: getVal("mwWorkTempOthEqPercCurr"), mwWorkTempOthMoreNoCurr: getVal("mwWorkTempOthMoreNoCurr"), mwWorkTempOthMorePercCurr: getVal("mwWorkTempOthMorePercCurr"),
            mwWorkTempOthTotalPrev: getVal("mwWorkTempOthTotalPrev"), mwWorkTempOthEqNoPrev: getVal("mwWorkTempOthEqNoPrev"), mwWorkTempOthEqPercPrev: getVal("mwWorkTempOthEqPercPrev"), mwWorkTempOthMoreNoPrev: getVal("mwWorkTempOthMoreNoPrev"), mwWorkTempOthMorePercPrev: getVal("mwWorkTempOthMorePercPrev"),

            // --- Principle 5 Q3 ---
            remBodMaleNum: getVal("remBodMaleNum"), remBodMaleMedian: getVal("remBodMaleMedian"),
            remBodFemNum: getVal("remBodFemNum"), remBodFemMedian: getVal("remBodFemMedian"),
            remKmpMaleNum: getVal("remKmpMaleNum"), remKmpMaleMedian: getVal("remKmpMaleMedian"),
            remKmpFemNum: getVal("remKmpFemNum"), remKmpFemMedian: getVal("remKmpFemMedian"),

            remEmpMaleNum: getVal("remEmpMaleNum"), remEmpMaleMedian: getVal("remEmpMaleMedian"),
            remEmpFemNum: getVal("remEmpFemNum"), remEmpFemMedian: getVal("remEmpFemMedian"),

            remWorkMaleNum: getVal("remWorkMaleNum"), remWorkMaleMedian: getVal("remWorkMaleMedian"),
            remWorkFemNum: getVal("remWorkFemNum"), remWorkFemMedian: getVal("remWorkFemMedian"),

            remunerationNote: getVal("remunerationNote"),

            // --- Principle 5 Q2.b ---
            grossWagesFemalePercCurr: getVal("grossWagesFemalePercCurr"),
            grossWagesFemalePercPrev: getVal("grossWagesFemalePercPrev"),
            grossWagesNote: getVal("grossWagesNote"),

             // --- Principle 5 Q4 ---
             humanRightsFocalPoint: getVal("humanRightsFocalPoint"),
             humanRightsFocalDetails: getVal("humanRightsFocalDetails"),

             // --- Principle 5 Q5 ---
             humanRightsGrievanceMechanism: getVal("humanRightsGrievanceMechanism"),

             // --- Principle 5 Q6 ---
                     compShFiledCurr: getVal("compShFiledCurr"), compShPendingCurr: getVal("compShPendingCurr"), compShRemarksCurr: getVal("compShRemarksCurr"),
                     compShFiledPrev: getVal("compShFiledPrev"), compShPendingPrev: getVal("compShPendingPrev"), compShRemarksPrev: getVal("compShRemarksPrev"),

                     compDiscrimFiledCurr: getVal("compDiscrimFiledCurr"), compDiscrimPendingCurr: getVal("compDiscrimPendingCurr"), compDiscrimRemarksCurr: getVal("compDiscrimRemarksCurr"),
                     compDiscrimFiledPrev: getVal("compDiscrimFiledPrev"), compDiscrimPendingPrev: getVal("compDiscrimPendingPrev"), compDiscrimRemarksPrev: getVal("compDiscrimRemarksPrev"),

                     compChildFiledCurr: getVal("compChildFiledCurr"), compChildPendingCurr: getVal("compChildPendingCurr"), compChildRemarksCurr: getVal("compChildRemarksCurr"),
                     compChildFiledPrev: getVal("compChildFiledPrev"), compChildPendingPrev: getVal("compChildPendingPrev"), compChildRemarksPrev: getVal("compChildRemarksPrev"),

                     compForcedFiledCurr: getVal("compForcedFiledCurr"), compForcedPendingCurr: getVal("compForcedPendingCurr"), compForcedRemarksCurr: getVal("compForcedRemarksCurr"),
                     compForcedFiledPrev: getVal("compForcedFiledPrev"), compForcedPendingPrev: getVal("compForcedPendingPrev"), compForcedRemarksPrev: getVal("compForcedRemarksPrev"),

                     compWagesFiledCurr: getVal("compWagesFiledCurr"), compWagesPendingCurr: getVal("compWagesPendingCurr"), compWagesRemarksCurr: getVal("compWagesRemarksCurr"),
                     compWagesFiledPrev: getVal("compWagesFiledPrev"), compWagesPendingPrev: getVal("compWagesPendingPrev"), compWagesRemarksPrev: getVal("compWagesRemarksPrev"),

                     compOtherHrFiledCurr: getVal("compOtherHrFiledCurr"), compOtherHrPendingCurr: getVal("compOtherHrPendingCurr"), compOtherHrRemarksCurr: getVal("compOtherHrRemarksCurr"),
                     compOtherHrFiledPrev: getVal("compOtherHrFiledPrev"), compOtherHrPendingPrev: getVal("compOtherHrPendingPrev"), compOtherHrRemarksPrev: getVal("compOtherHrRemarksPrev"),

             // --- Principle 5 Q7 ---
                     poshTotalCurr: getVal("poshTotalCurr"), poshTotalPrev: getVal("poshTotalPrev"),
                     poshPercCurr: getVal("poshPercCurr"), poshPercPrev: getVal("poshPercPrev"),
                     poshUpheldCurr: getVal("poshUpheldCurr"), poshUpheldPrev: getVal("poshUpheldPrev"),
                     poshNote: getVal("poshNote"),

              // --- Principle 5 Q8 ---
                     protectionMechanismsIntro: getVal("protectionMechanismsIntro"),
                     protectionMechanismsList: Array.from(document.querySelectorAll(".prot-mech-input")).map(input => input.value),
             // --- Principle 5 Q9 ---
                     humanRightsContracts: getVal("humanRightsContracts"),
                     humanRightsContractsDetails: getVal("humanRightsContractsDetails"),
              // --- Principle 5 Q10 ---
                     assessChildLabourPerc: getVal("assessChildLabourPerc"),
                     assessForcedLabourPerc: getVal("assessForcedLabourPerc"),
                     assessSexualHarassmentPerc: getVal("assessSexualHarassmentPerc"),
                     assessDiscriminationPerc: getVal("assessDiscriminationPerc"),
                     assessWagesPerc: getVal("assessWagesPerc"),
                     assessOthersPerc: getVal("assessOthersPerc"),
                     assessmentsP5Note: getVal("assessmentsP5Note"),
               // --- Principle 5 Q11 ---
                      assessCorrectiveIntro: getVal("assessCorrectiveIntro"),
                      assessCorrectiveActions: Array.from(document.querySelectorAll(".ac-action-input")).map(input => input.value),
             //princple 5 leadership
             //q1 and q2
             p5LeadProcessModIntro: getVal("p5LeadProcessModIntro"),
                 p5LeadProcessModList: Array.from(document.querySelectorAll(".p5-mod-input")).map(input => input.value),

                 p5LeadDueDiligenceScope: getVal("p5LeadDueDiligenceScope"),
                 p5LeadDueDiligenceIssues: Array.from(document.querySelectorAll(".p5-issue-input")).map(input => input.value),
                 p5LeadDueDiligenceHolders: Array.from(document.querySelectorAll(".p5-holder-input")).map(input => input.value),
             //q3
             p5LeadPremisesAccess: getVal("p5LeadPremisesAccess"),
             //q4
              p5LeadValueChainAssessment: getVal("p5LeadValueChainAssessment"),
             //q5
             p5LeadValueChainCorrectiveActions: getVal("p5LeadValueChainCorrectiveActions"),
        };

    // Save to DB
    fetch("http://localhost:8080/api/report/save", {  // Endpoint that calls reportService.saveReportToDb
        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": "Bearer " + token },
        body: JSON.stringify(formData)
    })
    .then(res => {
        if (res.ok) {
            window.location.href = "create_report_4.html"; // Redirect to new page
        } else {
            alert("Failed to save data.");
        }
    })
    .catch(err => console.error("Error:", err));
}

// --- GENERATE AI NOTE FROM USER INPUT ---
async function generateNoteFromInput(fieldId, sectionContext) {
    const textarea = document.getElementById(fieldId);
    if (!textarea) {
        console.error("Could not find textarea with ID:", fieldId);
        return;
    }

    const userInput = textarea.value.trim();

    // Check if the user actually typed something
    if (!userInput || userInput.length < 3) {
        alert("Please type a few keywords first (e.g., 'Safety, POSH, software training').");
        return;
    }

    // UI Feedback
    textarea.disabled = true;
    const originalPlaceholder = textarea.placeholder;
    textarea.placeholder = "Gemini is writing...";
    textarea.value = ""; // Clear the box while loading

    // Build the prompt for Gemini
    const promptText = `You are an expert ESG consultant writing a BRSR report.
    Expand the following rough notes into a professional, concise paragraph (1-2 sentences) for the '${sectionContext}' section.
    Notes: ${userInput}`;

    try {
        const response = await fetch("http://localhost:8080/api/ai/generate-esg", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            body: JSON.stringify({ prompt: promptText })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        // Populate the text area and remove any asterisks (**) Gemini sometimes adds
        if (data.text) {
            textarea.value = data.text.replace(/\*\*/g, '');
        } else {
            textarea.value = "Error: AI returned an empty response.";
        }

    } catch (err) {
        console.error("AI Error:", err);
        textarea.value = "Error: Could not connect to AI service. Please check if your Spring Boot backend is running and connected to the internet.";
    } finally {
        // Restore UI
        textarea.disabled = false;
        textarea.placeholder = originalPlaceholder;
    }
}

// --- SMART CELL-LEVEL AI FOR TRAINING TOPICS ---
// --- SMART CELL-LEVEL AI FOR TRAINING TOPICS ---
async function generateTrainingTopicsAI(buttonElement) {
    const container = buttonElement.parentElement;
    const textarea = container.nextElementSibling;
    const row = buttonElement.closest('tr');

    // Safety check: Make sure this button is actually inside a table row!
    if (!row) {
        alert("Error: This specific AI button must be placed inside the Training table rows.");
        return;
    }

    // Grab the segment name to give Gemini context
    const segmentInput = row.querySelector('.tp-seg');
    const segment = segmentInput && segmentInput.value ? segmentInput.value : "employees";

    const userInput = textarea.value.trim();

    if (!userInput || userInput.length < 3) {
        alert("Please type a few keywords first (e.g., 'Safety, Code of conduct, POSH').");
        return;
    }

    textarea.disabled = true;
    const originalPlaceholder = textarea.placeholder;
    textarea.placeholder = "Gemini is writing...";
    textarea.value = "";

    const promptText = `You are an expert ESG consultant writing a BRSR report.
    Expand the following rough notes into a professional, concise description (1 to 2 sentences max) of the training topics and principles covered specifically for the '${segment}' segment.
    Notes: ${userInput}`;

    try {
        const response = await fetch("http://localhost:8080/api/ai/generate-esg", {
            method: "POST",
            headers: { "Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("token") },
            body: JSON.stringify({ prompt: promptText })
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const data = await response.json();
        textarea.value = data.text ? data.text.replace(/\*\*/g, '') : "Error: AI returned empty response.";
    } catch (err) {
        console.error("AI Error:", err);
        textarea.value = "Error: Could not connect to AI service.";
    } finally {
        textarea.disabled = false;
        textarea.placeholder = originalPlaceholder;
    }
}

// --- SMART CELL-LEVEL AI FOR LEGAL BRIEFS ---
async function generateLegalBriefAI(buttonElement) {
    const container = buttonElement.parentElement;
    const textarea = container.nextElementSibling;
    const row = buttonElement.closest('tr');

    // Safety check: Make sure this button is actually inside a table row!
    if (!row) {
        alert("Error: This specific AI button must be placed inside the Fines & Penalties table rows.");
        return;
    }

    const typeInput = row.querySelector('.la-type');
    const agencyInput = row.querySelector('.la-agency');

    const actionType = typeInput && typeInput.value ? typeInput.value : "Penalty/Action";
    const agency = agencyInput && agencyInput.value ? agencyInput.value : "Regulatory Authority";

    const userInput = textarea.value.trim();

    if (!userInput || userInput.length < 3) {
        alert("Please type a few keywords first (e.g., 'Late filing', 'Minor safety violation').");
        return;
    }

    textarea.disabled = true;
    const originalPlaceholder = textarea.placeholder;
    textarea.placeholder = "Gemini is generating brief...";
    textarea.value = "";

    const promptText = `You are an expert corporate lawyer and ESG consultant writing a BRSR report.
    Expand the following rough notes into a professional, formal, and concise description (1 to 2 sentences max) of the legal case/penalty.
    Context -> Action Type: ${actionType}, Regulatory Agency: ${agency}.
    Notes to expand: ${userInput}`;

    try {
        const response = await fetch("http://localhost:8080/api/ai/generate-esg", {
            method: "POST",
            headers: { "Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("token") },
            body: JSON.stringify({ prompt: promptText })
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const data = await response.json();
        textarea.value = data.text ? data.text.replace(/\*\*/g, '') : "Error: AI returned empty response.";
    } catch (err) {
        console.error("AI Error:", err);
        textarea.value = "Error: Could not connect to AI service.";
    } finally {
        textarea.disabled = false;
        textarea.placeholder = originalPlaceholder;
    }
}

// --- SMART CELL-LEVEL AI FOR STATIC TABLE REMARKS (Used in Q6) ---
async function generateRowRemarkAI(buttonElement) {
    const container = buttonElement.parentElement;
    const textarea = container.nextElementSibling;
    const row = buttonElement.closest('tr');

    // Grab the category (Directors or KMPs) from the very first column
    const category = row.querySelector('td').innerText.trim();
    const userInput = textarea.value.trim();

    if (!userInput || userInput.length < 2) {
        alert("Please type a few keywords first (e.g., 'NIL', 'Resolved instantly').");
        return;
    }

    textarea.disabled = true;
    const originalPlaceholder = textarea.placeholder;
    textarea.placeholder = "Gemini is writing...";
    textarea.value = "";

    const promptText = `You are an expert ESG consultant writing a BRSR report.
    Expand the following rough notes into a professional, formal, and concise remark (1 sentence max) regarding Conflict of Interest complaints for ${category}. If the input is just "NIL" or "None", just output "NIL".
    Notes: ${userInput}`;

    try {
        const response = await fetch("http://localhost:8080/api/ai/generate-esg", {
            method: "POST",
            headers: { "Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("token") },
            body: JSON.stringify({ prompt: promptText })
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const data = await response.json();
        textarea.value = data.text ? data.text.replace(/\*\*/g, '') : "Error: AI returned empty response.";
    } catch (err) {
        console.error("AI Error:", err);
        textarea.value = "Error: Could not connect to AI service.";
    } finally {
        textarea.disabled = false;
        textarea.placeholder = originalPlaceholder;
    }
}

// --- UPDATED P2 LEADERSHIP 2: RISK ROW (WITH AI BUTTONS) ---
function addRiskRow(data = null) {
    const tbody = document.querySelector("#riskTable tbody");
    const d = data || {};
    const row = `
        <tr>
            <td><input type="text" class="rk-name" value="${d.productName || ''}" placeholder="Product Name"></td>
            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;"><button type="button" class="btn-ai-mini" onclick="generateTableCellAI(this, 'Description of social or environmental risk identified in LCA')">✨ Expand</button></div>
                <textarea class="rk-desc" rows="3" style="width:100%; border:1px solid #e2e8f0; border-radius:4px; padding:4px;" placeholder="Keywords...">${d.riskDescription || ''}</textarea>
            </td>
            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;"><button type="button" class="btn-ai-mini" onclick="generateTableCellAI(this, 'Action taken to mitigate social or environmental risk')">✨ Expand</button></div>
                <textarea class="rk-act" rows="3" style="width:100%; border:1px solid #e2e8f0; border-radius:4px; padding:4px;" placeholder="Keywords...">${d.actionTaken || ''}</textarea>
            </td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- GENERIC CELL-LEVEL AI GENERATOR (For R&D, Capex, and Risks) ---
// This function dynamically generates 1-2 sentences for ANY table cell based on the context passed in.
async function generateTableCellAI(buttonElement, aiContext) {
    const textarea = buttonElement.parentElement.nextElementSibling;
    const userInput = textarea.value.trim();

    if (!userInput || userInput.length < 3) {
        alert("Please type a few keywords first (e.g., 'Implemented solar', 'Water recycling setup').");
        return;
    }

    // UI Feedback
    textarea.disabled = true;
    const originalPlaceholder = textarea.placeholder;
    textarea.placeholder = "Gemini is generating...";
    textarea.value = "";

    const promptText = `You are an expert ESG consultant writing a BRSR report.
    Expand the following rough notes into a professional, concise description (1-2 sentences max) specifically regarding: ${aiContext}.
    Notes: ${userInput}`;

    try {
        const response = await fetch("http://localhost:8080/api/ai/generate-esg", {
            method: "POST",
            headers: { "Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("token") },
            body: JSON.stringify({ prompt: promptText })
        });

        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        const data = await response.json();

        // Remove markdown asterisks
        textarea.value = data.text ? data.text.replace(/\*\*/g, '') : "Error: AI returned empty response.";
    } catch (err) {
        console.error("AI Error:", err);
        textarea.value = "Error connecting to AI service.";
    } finally {
        textarea.disabled = false;
        textarea.placeholder = originalPlaceholder;
    }
}

//--- ROW MANAGEMENT (Q12 SAFETY MEASURES) ---
function addSafetyMeasureRow(data = null) {
    const container = document.getElementById("safetyMeasuresContainer");
    const d = data || {};

    const div = document.createElement("div");
    div.className = "safety-measure-item form-group";
    div.style.marginBottom = "15px";
    div.style.padding = "15px";
    div.style.background = "#f8fafc";
    div.style.border = "1px solid #e2e8f0";
    div.style.borderRadius = "6px";

    div.innerHTML = `
        <div style="display:flex; justify-content:space-between; margin-bottom:10px;">
            <label style="font-weight:bold; color:#108a55;">Strategy Heading</label>
            <span style="color:red; cursor:pointer; font-weight:bold;" onclick="this.parentElement.parentElement.remove()">X Remove</span>
        </div>
        <input type="text" class="sm-head" value="${d.heading || ''}" placeholder="e.g. Enhance Leadership Capability" style="width:100%; margin-bottom:10px;">

        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:5px;">
            <label style="font-size:12px; margin:0;">Description / Details</label>
            <button type="button" class="btn-ai-mini" onclick="generateTableCellAI(this, 'Safety and healthy workplace measure details')">✨ Expand</button>
        </div>
        <textarea class="sm-desc" rows="3" style="width:100%; border:1px solid #e2e8f0; border-radius:4px; padding:4px;" placeholder="Keywords (e.g. 'Safety tours conducted monthly')...">${d.description || ''}</textarea>
    `;
    container.appendChild(div);
}

// --- ROW MANAGEMENT (LEADERSHIP 6: VC ACTIONS) ---
function addVcActionRow(data = null) {
    const container = document.getElementById("vcActionsContainer");
    const d = data || {};

    const div = document.createElement("div");
    div.className = "vc-action-item form-group";
    div.style.marginBottom = "15px";
    div.style.padding = "15px";
    div.style.background = "#f8fafc";
    div.style.border = "1px solid #e2e8f0";
    div.style.borderRadius = "6px";

    div.innerHTML = `
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:5px;">
            <label style="font-size:13px; margin:0; color:#0284c7; font-weight:bold;">» Action Item</label>
            <div style="display:flex; gap:15px; align-items:center;">
                <button type="button" class="btn-ai-mini" onclick="generateTableCellAI(this, 'Corrective action taken to address value chain partner risks')">✨ Expand</button>
                <span style="color:red; cursor:pointer; font-weight:bold; font-size: 12px;" onclick="this.parentElement.parentElement.parentElement.remove()">X Remove</span>
            </div>
        </div>
        <textarea class="vc-act-text" rows="2" style="width:100%; border:1px solid #e2e8f0; border-radius:4px; padding:4px;" placeholder="Keywords (e.g. 'Terminated contracts with non-compliant vendors')...">${d.actionText || ''}</textarea>
    `;
    container.appendChild(div);
}

// --- TOGGLE N/A SECTIONS ---
function toggleNaSection(containerId, isChecked) {
    const container = document.getElementById(containerId);
    if (container) {
        if (isChecked) {
            container.style.opacity = "0.4";
            container.style.pointerEvents = "none";
        } else {
            container.style.opacity = "1";
            container.style.pointerEvents = "auto";
        }
    }
}