const currentReportId = localStorage.getItem("currentReportId");

document.addEventListener("DOMContentLoaded", function() {
    if (!currentReportId) {
        alert("Session lost. Please restart.");
        window.location.href = "create_report.html";
        return;
    }
    loadData();
});

function loadData() {
    const token = localStorage.getItem("token");
    fetch(`http://localhost:8080/api/report/${currentReportId}`, {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(data => {
        // ================= P6 (Q1 - Q6 UPDATED) =================
        // P6 Q1
        setVal("p6Q1RenElecCurr", data.p6Q1RenElecCurr); setVal("p6Q1RenElecPrev", data.p6Q1RenElecPrev);
        setVal("p6Q1RenFuelCurr", data.p6Q1RenFuelCurr); setVal("p6Q1RenFuelPrev", data.p6Q1RenFuelPrev);
        setVal("p6Q1RenOtherCurr", data.p6Q1RenOtherCurr); setVal("p6Q1RenOtherPrev", data.p6Q1RenOtherPrev);
        setVal("p6Q1RenTotalCurr", data.p6Q1RenTotalCurr); setVal("p6Q1RenTotalPrev", data.p6Q1RenTotalPrev);
        setVal("p6Q1NonRenElecCurr", data.p6Q1NonRenElecCurr); setVal("p6Q1NonRenElecPrev", data.p6Q1NonRenElecPrev);
        setVal("p6Q1NonRenFuelCurr", data.p6Q1NonRenFuelCurr); setVal("p6Q1NonRenFuelPrev", data.p6Q1NonRenFuelPrev);
        setVal("p6Q1NonRenOtherCurr", data.p6Q1NonRenOtherCurr); setVal("p6Q1NonRenOtherPrev", data.p6Q1NonRenOtherPrev);
        setVal("p6Q1NonRenTotalCurr", data.p6Q1NonRenTotalCurr); setVal("p6Q1NonRenTotalPrev", data.p6Q1NonRenTotalPrev);
        setVal("p6Q1GrandTotalCurr", data.p6Q1GrandTotalCurr); setVal("p6Q1GrandTotalPrev", data.p6Q1GrandTotalPrev);
        setVal("p6Q1IntTurnoverCurr", data.p6Q1IntTurnoverCurr); setVal("p6Q1IntTurnoverPrev", data.p6Q1IntTurnoverPrev);
        setVal("p6Q1IntPppCurr", data.p6Q1IntPppCurr); setVal("p6Q1IntPppPrev", data.p6Q1IntPppPrev);
        setVal("p6Q1IntPhysicalCurr", data.p6Q1IntPhysicalCurr); setVal("p6Q1IntPhysicalPrev", data.p6Q1IntPhysicalPrev);
        setVal("p6Q1IntOptCurr", data.p6Q1IntOptCurr); setVal("p6Q1IntOptPrev", data.p6Q1IntOptPrev);
        setVal("p6Q1AssuranceNote", data.p6Q1AssuranceNote);

        // P6 Q2
        setVal("p6Q2PatDetails", data.p6Q2PatDetails);

        // P6 Q3
        setVal("p6Q3SurfaceCurr", data.p6Q3SurfaceCurr); setVal("p6Q3SurfacePrev", data.p6Q3SurfacePrev);
        setVal("p6Q3GroundCurr", data.p6Q3GroundCurr); setVal("p6Q3GroundPrev", data.p6Q3GroundPrev);
        setVal("p6Q3ThirdPartyCurr", data.p6Q3ThirdPartyCurr); setVal("p6Q3ThirdPartyPrev", data.p6Q3ThirdPartyPrev);
        setVal("p6Q3SeaCurr", data.p6Q3SeaCurr); setVal("p6Q3SeaPrev", data.p6Q3SeaPrev);
        setVal("p6Q3OthersCurr", data.p6Q3OthersCurr); setVal("p6Q3OthersPrev", data.p6Q3OthersPrev);
        setVal("p6Q3TotalWithCurr", data.p6Q3TotalWithCurr); setVal("p6Q3TotalWithPrev", data.p6Q3TotalWithPrev);
        setVal("p6Q3TotalConsCurr", data.p6Q3TotalConsCurr); setVal("p6Q3TotalConsPrev", data.p6Q3TotalConsPrev);
        setVal("p6Q3IntTurnoverCurr", data.p6Q3IntTurnoverCurr); setVal("p6Q3IntTurnoverPrev", data.p6Q3IntTurnoverPrev);
        setVal("p6Q3IntPppCurr", data.p6Q3IntPppCurr); setVal("p6Q3IntPppPrev", data.p6Q3IntPppPrev);
        setVal("p6Q3IntPhysicalCurr", data.p6Q3IntPhysicalCurr); setVal("p6Q3IntPhysicalPrev", data.p6Q3IntPhysicalPrev);
        setVal("p6Q3IntOptCurr", data.p6Q3IntOptCurr); setVal("p6Q3IntOptPrev", data.p6Q3IntOptPrev);
        setVal("p6Q3AssuranceNote", data.p6Q3AssuranceNote);

        // P6 Q4
        setVal("p6Q4SurfNoCurr", data.p6Q4SurfNoCurr); setVal("p6Q4SurfNoPrev", data.p6Q4SurfNoPrev);
        setVal("p6Q4SurfWithCurr", data.p6Q4SurfWithCurr); setVal("p6Q4SurfWithPrev", data.p6Q4SurfWithPrev); setVal("p6Q4SurfLevel", data.p6Q4SurfLevel);
        setVal("p6Q4GroundNoCurr", data.p6Q4GroundNoCurr); setVal("p6Q4GroundNoPrev", data.p6Q4GroundNoPrev);
        setVal("p6Q4GroundWithCurr", data.p6Q4GroundWithCurr); setVal("p6Q4GroundWithPrev", data.p6Q4GroundWithPrev); setVal("p6Q4GroundLevel", data.p6Q4GroundLevel);
        setVal("p6Q4SeaNoCurr", data.p6Q4SeaNoCurr); setVal("p6Q4SeaNoPrev", data.p6Q4SeaNoPrev);
        setVal("p6Q4SeaWithCurr", data.p6Q4SeaWithCurr); setVal("p6Q4SeaWithPrev", data.p6Q4SeaWithPrev); setVal("p6Q4SeaLevel", data.p6Q4SeaLevel);
        setVal("p6Q4ThirdNoCurr", data.p6Q4ThirdNoCurr); setVal("p6Q4ThirdNoPrev", data.p6Q4ThirdNoPrev);
        setVal("p6Q4ThirdWithCurr", data.p6Q4ThirdWithCurr); setVal("p6Q4ThirdWithPrev", data.p6Q4ThirdWithPrev); setVal("p6Q4ThirdLevel", data.p6Q4ThirdLevel);
        setVal("p6Q4OtherNoCurr", data.p6Q4OtherNoCurr); setVal("p6Q4OtherNoPrev", data.p6Q4OtherNoPrev);
        setVal("p6Q4OtherWithCurr", data.p6Q4OtherWithCurr); setVal("p6Q4OtherWithPrev", data.p6Q4OtherWithPrev); setVal("p6Q4OtherLevel", data.p6Q4OtherLevel);
        setVal("p6Q4TotalCurr", data.p6Q4TotalCurr); setVal("p6Q4TotalPrev", data.p6Q4TotalPrev);
        setVal("p6Q4AssuranceNote", data.p6Q4AssuranceNote);

        // P6 Q5
        setVal("p6Q5ZldDetails", data.p6Q5ZldDetails);

        // P6 Q6
        setVal("p6Q6NoxUnit", data.p6Q6NoxUnit); setVal("p6Q6NoxCurr", data.p6Q6NoxCurr); setVal("p6Q6NoxPrev", data.p6Q6NoxPrev);
        setVal("p6Q6SoxUnit", data.p6Q6SoxUnit); setVal("p6Q6SoxCurr", data.p6Q6SoxCurr); setVal("p6Q6SoxPrev", data.p6Q6SoxPrev);
        setVal("p6Q6PmUnit", data.p6Q6PmUnit); setVal("p6Q6PmCurr", data.p6Q6PmCurr); setVal("p6Q6PmPrev", data.p6Q6PmPrev);
        setVal("p6Q6PopUnit", data.p6Q6PopUnit); setVal("p6Q6PopCurr", data.p6Q6PopCurr); setVal("p6Q6PopPrev", data.p6Q6PopPrev);
        setVal("p6Q6VocUnit", data.p6Q6VocUnit); setVal("p6Q6VocCurr", data.p6Q6VocCurr); setVal("p6Q6VocPrev", data.p6Q6VocPrev);
        setVal("p6Q6HapUnit", data.p6Q6HapUnit); setVal("p6Q6HapCurr", data.p6Q6HapCurr); setVal("p6Q6HapPrev", data.p6Q6HapPrev);
        setVal("p6Q6OtherName", data.p6Q6OtherName); setVal("p6Q6OtherUnit", data.p6Q6OtherUnit); setVal("p6Q6OtherCurr", data.p6Q6OtherCurr); setVal("p6Q6OtherPrev", data.p6Q6OtherPrev);
        setVal("p6Q6AssuranceNote", data.p6Q6AssuranceNote);

        //leadership p6
                if(data.p6LeadQ4InitiativesList) data.p6LeadQ4InitiativesList.forEach(r => addP6LeadQ4Row(r));


        // P7 Load
                                if(document.getElementById("p7Q1aAffiliations")) setVal("p7Q1aAffiliations", data.p7Q1aAffiliations);
                                if (data.p7Q1bList && data.p7Q1bList.length > 0) data.p7Q1bList.forEach(r => addP7Q1bRow(r)); else addP7Q1bRow();
                                if (data.p7Q2List && data.p7Q2List.length > 0) data.p7Q2List.forEach(r => addP7Q2Row(r)); else addP7Q2Row();
                                if (data.p7LeadQ1List && data.p7LeadQ1List.length > 0) data.p7LeadQ1List.forEach(r => addP7LeadQ1Row(r)); else addP7LeadQ1Row();

        // P8 Load
                if (data.p8Q1SiaList && data.p8Q1SiaList.length > 0) data.p8Q1SiaList.forEach(r => addP8Q1Row(r)); else addP8Q1Row();
                if (data.p8Q2RrList && data.p8Q2RrList.length > 0) data.p8Q2RrList.forEach(r => addP8Q2Row(r)); else addP8Q2Row();
                setVal("p8Q3GrievanceMech", data.p8Q3GrievanceMech);
                setVal("p8Q4MsmeCurr", data.p8Q4MsmeCurr); setVal("p8Q4MsmePrev", data.p8Q4MsmePrev);
                setVal("p8Q4IndiaCurr", data.p8Q4IndiaCurr); setVal("p8Q4IndiaPrev", data.p8Q4IndiaPrev);
                setVal("p8Q5RuralCurr", data.p8Q5RuralCurr); setVal("p8Q5RuralPrev", data.p8Q5RuralPrev);
                setVal("p8Q5SemiCurr", data.p8Q5SemiCurr); setVal("p8Q5SemiPrev", data.p8Q5SemiPrev);
                setVal("p8Q5UrbanCurr", data.p8Q5UrbanCurr); setVal("p8Q5UrbanPrev", data.p8Q5UrbanPrev);
                setVal("p8Q5MetroCurr", data.p8Q5MetroCurr); setVal("p8Q5MetroPrev", data.p8Q5MetroPrev);

        // P9
        setVal("p9ConsumerComplaintMech", data.p9ConsumerComplaintMech);
        setVal("p9DataPrivacyCurr", data.p9DataPrivacyCurr); setVal("p9DataPrivacyPrev", data.p9DataPrivacyPrev);
        setVal("p9AdvertisingCurr", data.p9AdvertisingCurr); setVal("p9AdvertisingPrev", data.p9AdvertisingPrev);
        setVal("p9CyberCurr", data.p9CyberCurr); setVal("p9CyberPrev", data.p9CyberPrev);
        setVal("p9DeliveryCurr", data.p9DeliveryCurr); setVal("p9DeliveryPrev", data.p9DeliveryPrev);
        setVal("p9RestrictiveCurr", data.p9RestrictiveCurr); setVal("p9RestrictivePrev", data.p9RestrictivePrev);
        setVal("p9UnfairCurr", data.p9UnfairCurr); setVal("p9UnfairPrev", data.p9UnfairPrev);
        setVal("p9OtherCurr", data.p9OtherCurr); setVal("p9OtherPrev", data.p9OtherPrev);
        setVal("p9RecallVoluntary", data.p9RecallVoluntary);
        setVal("p9RecallForced", data.p9RecallForced);
        setVal("p9CyberSecurityPolicy", data.p9CyberSecurityPolicy);
        setVal("p9CorrectiveActions", data.p9CorrectiveActions);
        setVal("p9InfoChannels", data.p9InfoChannels);
        setVal("p9SafeUsageSteps", data.p9SafeUsageSteps);
        setVal("p9DisruptionInfo", data.p9DisruptionInfo);
        setVal("p9ProductInfoDisplay", data.p9ProductInfoDisplay);
        setVal("p9CustomerSatSurvey", data.p9CustomerSatSurvey);


    });
}

function setVal(id, val) { if(document.getElementById(id)) document.getElementById(id).value = val || ""; }
function getVal(id) { return document.getElementById(id) ? document.getElementById(id).value : ""; }

// --- ROW FUNCTIONS ---
function addWasteRow(d={}) {
    const tbody = document.querySelector("#p6WasteTable tbody");
    const row = `<tr>
        <td><input type="text" class="w-cat" value="${d.category||''}" placeholder="Cat"></td>
        <td><input type="text" class="w-cg" value="${d.currGenerated||''}"></td>
        <td><input type="text" class="w-cr" value="${d.currRecycled||''}"></td>
        <td><input type="text" class="w-cd" value="${d.currDisposed||''}"></td>
        <td><input type="text" class="w-pg" value="${d.prevGenerated||''}"></td>
        <td><input type="text" class="w-pr" value="${d.prevRecycled||''}"></td>
        <td><input type="text" class="w-pd" value="${d.prevDisposed||''}"></td>
        <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addP7Row(d={}) {
    const tbody = document.querySelector("#p7AssocTable tbody");
    const row = `<tr><td><input type="text" class="p7-nm" value="${d.name||''}"></td><td><input type="text" class="p7-rc" value="${d.reach||''}"></td><td class="btn-remove" onclick="this.parentElement.remove()">X</td></tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}
function addP7PolicyRow(d={}) {
    const tbody = document.querySelector("#p7PolicyTable tbody");
    const row = `<tr><td><textarea class="p7-pol">${d.policyAdvocated||''}</textarea></td><td><textarea class="p7-meth">${d.methodResorted||''}</textarea></td><td><textarea class="p7-lnk">${d.webLink||''}</textarea></td><td class="btn-remove" onclick="this.parentElement.remove()">X</td></tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}
function addP8RrRow(d={}) {
    const tbody = document.querySelector("#p8RrTable tbody");
    const row = `<tr><td><input type="text" class="rr-nm" value="${d.projectName||''}"></td><td><input type="text" class="rr-st" value="${d.state||''}"></td><td><input type="text" class="rr-dt" value="${d.district||''}"></td><td><input type="text" class="rr-pf" value="${d.noOfPafs||''}"></td><td><input type="text" class="rr-pc" value="${d.percCovered||''}"></td><td class="btn-remove" onclick="this.parentElement.remove()">X</td></tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}
function addP8AspRow(d={}) {
    const tbody = document.querySelector("#p8AspTable tbody");
    const row = `<tr><td><input type="text" class="asp-st" value="${d.state||''}"></td><td><input type="text" class="asp-dt" value="${d.district||''}"></td><td><input type="text" class="asp-am" value="${d.amountSpent||''}"></td><td class="btn-remove" onclick="this.parentElement.remove()">X</td></tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}
function addP8BenRow(d={}) {
    const tbody = document.querySelector("#p8BenTable tbody");
    const row = `<tr><td><input type="text" class="ben-pj" value="${d.csrProject||''}"></td><td><input type="text" class="ben-no" value="${d.noBenefitted||''}"></td><td><input type="text" class="ben-pc" value="${d.percVulnerable||''}"></td><td class="btn-remove" onclick="this.parentElement.remove()">X</td></tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

// Generate AI Note function (added so your AI buttons work)
async function generateNoteFromInput(inputId, context) {
    const inputEl = document.getElementById(inputId);
    const keywords = inputEl.value;

    if (!keywords.trim()) {
        alert("Please enter some keywords or an outline first.");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/api/ai/generate-esg", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ prompt: `Generate a formal BRSR response for Principle 6 regarding: ${context}. Base it on these keywords/thoughts: ${keywords}. Keep it strictly professional, factual, and concise.` })
        });

        const data = await response.json();
        if (data && data.text) {
            inputEl.value = data.text;
        } else {
            alert("AI generation failed.");
        }
    } catch (err) {
        console.error("AI Error:", err);
        alert("Error connecting to AI service.");
    }
}

function generateFinalReport() {
    const token = localStorage.getItem("token");
    const formData = {
        id: parseInt(currentReportId),

        // ================= P6 (Q1 - Q6 UPDATED) =================
        // P6 Q1
        p6Q1RenElecCurr: getVal("p6Q1RenElecCurr"), p6Q1RenElecPrev: getVal("p6Q1RenElecPrev"),
        p6Q1RenFuelCurr: getVal("p6Q1RenFuelCurr"), p6Q1RenFuelPrev: getVal("p6Q1RenFuelPrev"),
        p6Q1RenOtherCurr: getVal("p6Q1RenOtherCurr"), p6Q1RenOtherPrev: getVal("p6Q1RenOtherPrev"),
        p6Q1RenTotalCurr: getVal("p6Q1RenTotalCurr"), p6Q1RenTotalPrev: getVal("p6Q1RenTotalPrev"),
        p6Q1NonRenElecCurr: getVal("p6Q1NonRenElecCurr"), p6Q1NonRenElecPrev: getVal("p6Q1NonRenElecPrev"),
        p6Q1NonRenFuelCurr: getVal("p6Q1NonRenFuelCurr"), p6Q1NonRenFuelPrev: getVal("p6Q1NonRenFuelPrev"),
        p6Q1NonRenOtherCurr: getVal("p6Q1NonRenOtherCurr"), p6Q1NonRenOtherPrev: getVal("p6Q1NonRenOtherPrev"),
        p6Q1NonRenTotalCurr: getVal("p6Q1NonRenTotalCurr"), p6Q1NonRenTotalPrev: getVal("p6Q1NonRenTotalPrev"),
        p6Q1GrandTotalCurr: getVal("p6Q1GrandTotalCurr"), p6Q1GrandTotalPrev: getVal("p6Q1GrandTotalPrev"),
        p6Q1IntTurnoverCurr: getVal("p6Q1IntTurnoverCurr"), p6Q1IntTurnoverPrev: getVal("p6Q1IntTurnoverPrev"),
        p6Q1IntPppCurr: getVal("p6Q1IntPppCurr"), p6Q1IntPppPrev: getVal("p6Q1IntPppPrev"),
        p6Q1IntPhysicalCurr: getVal("p6Q1IntPhysicalCurr"), p6Q1IntPhysicalPrev: getVal("p6Q1IntPhysicalPrev"),
        p6Q1IntOptCurr: getVal("p6Q1IntOptCurr"), p6Q1IntOptPrev: getVal("p6Q1IntOptPrev"),
        p6Q1AssuranceNote: getVal("p6Q1AssuranceNote"),

        // P6 Q2
        p6Q2PatDetails: getVal("p6Q2PatDetails"),

        // P6 Q3
        p6Q3SurfaceCurr: getVal("p6Q3SurfaceCurr"), p6Q3SurfacePrev: getVal("p6Q3SurfacePrev"),
        p6Q3GroundCurr: getVal("p6Q3GroundCurr"), p6Q3GroundPrev: getVal("p6Q3GroundPrev"),
        p6Q3ThirdPartyCurr: getVal("p6Q3ThirdPartyCurr"), p6Q3ThirdPartyPrev: getVal("p6Q3ThirdPartyPrev"),
        p6Q3SeaCurr: getVal("p6Q3SeaCurr"), p6Q3SeaPrev: getVal("p6Q3SeaPrev"),
        p6Q3OthersCurr: getVal("p6Q3OthersCurr"), p6Q3OthersPrev: getVal("p6Q3OthersPrev"),
        p6Q3TotalWithCurr: getVal("p6Q3TotalWithCurr"), p6Q3TotalWithPrev: getVal("p6Q3TotalWithPrev"),
        p6Q3TotalConsCurr: getVal("p6Q3TotalConsCurr"), p6Q3TotalConsPrev: getVal("p6Q3TotalConsPrev"),
        p6Q3IntTurnoverCurr: getVal("p6Q3IntTurnoverCurr"), p6Q3IntTurnoverPrev: getVal("p6Q3IntTurnoverPrev"),
        p6Q3IntPppCurr: getVal("p6Q3IntPppCurr"), p6Q3IntPppPrev: getVal("p6Q3IntPppPrev"),
        p6Q3IntPhysicalCurr: getVal("p6Q3IntPhysicalCurr"), p6Q3IntPhysicalPrev: getVal("p6Q3IntPhysicalPrev"),
        p6Q3IntOptCurr: getVal("p6Q3IntOptCurr"), p6Q3IntOptPrev: getVal("p6Q3IntOptPrev"),
        p6Q3AssuranceNote: getVal("p6Q3AssuranceNote"),

        // P6 Q4
        p6Q4SurfNoCurr: getVal("p6Q4SurfNoCurr"), p6Q4SurfNoPrev: getVal("p6Q4SurfNoPrev"),
        p6Q4SurfWithCurr: getVal("p6Q4SurfWithCurr"), p6Q4SurfWithPrev: getVal("p6Q4SurfWithPrev"), p6Q4SurfLevel: getVal("p6Q4SurfLevel"),
        p6Q4GroundNoCurr: getVal("p6Q4GroundNoCurr"), p6Q4GroundNoPrev: getVal("p6Q4GroundNoPrev"),
        p6Q4GroundWithCurr: getVal("p6Q4GroundWithCurr"), p6Q4GroundWithPrev: getVal("p6Q4GroundWithPrev"), p6Q4GroundLevel: getVal("p6Q4GroundLevel"),
        p6Q4SeaNoCurr: getVal("p6Q4SeaNoCurr"), p6Q4SeaNoPrev: getVal("p6Q4SeaNoPrev"),
        p6Q4SeaWithCurr: getVal("p6Q4SeaWithCurr"), p6Q4SeaWithPrev: getVal("p6Q4SeaWithPrev"), p6Q4SeaLevel: getVal("p6Q4SeaLevel"),
        p6Q4ThirdNoCurr: getVal("p6Q4ThirdNoCurr"), p6Q4ThirdNoPrev: getVal("p6Q4ThirdNoPrev"),
        p6Q4ThirdWithCurr: getVal("p6Q4ThirdWithCurr"), p6Q4ThirdWithPrev: getVal("p6Q4ThirdWithPrev"), p6Q4ThirdLevel: getVal("p6Q4ThirdLevel"),
        p6Q4OtherNoCurr: getVal("p6Q4OtherNoCurr"), p6Q4OtherNoPrev: getVal("p6Q4OtherNoPrev"),
        p6Q4OtherWithCurr: getVal("p6Q4OtherWithCurr"), p6Q4OtherWithPrev: getVal("p6Q4OtherWithPrev"), p6Q4OtherLevel: getVal("p6Q4OtherLevel"),
        p6Q4TotalCurr: getVal("p6Q4TotalCurr"), p6Q4TotalPrev: getVal("p6Q4TotalPrev"),
        p6Q4AssuranceNote: getVal("p6Q4AssuranceNote"),

        // P6 Q5
        p6Q5ZldDetails: getVal("p6Q5ZldDetails"),

        // P6 Q6
        p6Q6NoxUnit: getVal("p6Q6NoxUnit"), p6Q6NoxCurr: getVal("p6Q6NoxCurr"), p6Q6NoxPrev: getVal("p6Q6NoxPrev"),
        p6Q6SoxUnit: getVal("p6Q6SoxUnit"), p6Q6SoxCurr: getVal("p6Q6SoxCurr"), p6Q6SoxPrev: getVal("p6Q6SoxPrev"),
        p6Q6PmUnit: getVal("p6Q6PmUnit"), p6Q6PmCurr: getVal("p6Q6PmCurr"), p6Q6PmPrev: getVal("p6Q6PmPrev"),
        p6Q6PopUnit: getVal("p6Q6PopUnit"), p6Q6PopCurr: getVal("p6Q6PopCurr"), p6Q6PopPrev: getVal("p6Q6PopPrev"),
        p6Q6VocUnit: getVal("p6Q6VocUnit"), p6Q6VocCurr: getVal("p6Q6VocCurr"), p6Q6VocPrev: getVal("p6Q6VocPrev"),
        p6Q6HapUnit: getVal("p6Q6HapUnit"), p6Q6HapCurr: getVal("p6Q6HapCurr"), p6Q6HapPrev: getVal("p6Q6HapPrev"),
        p6Q6OtherName: getVal("p6Q6OtherName"), p6Q6OtherUnit: getVal("p6Q6OtherUnit"), p6Q6OtherCurr: getVal("p6Q6OtherCurr"), p6Q6OtherPrev: getVal("p6Q6OtherPrev"),
        p6Q6AssuranceNote: getVal("p6Q6AssuranceNote"),

        // P6 Q7
                p6Q7Scope1Unit: getVal("p6Q7Scope1Unit"), p6Q7Scope1Curr: getVal("p6Q7Scope1Curr"), p6Q7Scope1Prev: getVal("p6Q7Scope1Prev"),
                p6Q7Scope2Unit: getVal("p6Q7Scope2Unit"), p6Q7Scope2Curr: getVal("p6Q7Scope2Curr"), p6Q7Scope2Prev: getVal("p6Q7Scope2Prev"),
                p6Q7IntTurnoverCurr: getVal("p6Q7IntTurnoverCurr"), p6Q7IntTurnoverPrev: getVal("p6Q7IntTurnoverPrev"),
                p6Q7IntPppCurr: getVal("p6Q7IntPppCurr"), p6Q7IntPppPrev: getVal("p6Q7IntPppPrev"),
                p6Q7IntPhysCurr: getVal("p6Q7IntPhysCurr"), p6Q7IntPhysPrev: getVal("p6Q7IntPhysPrev"),
                p6Q7IntOptCurr: getVal("p6Q7IntOptCurr"), p6Q7IntOptPrev: getVal("p6Q7IntOptPrev"),
                p6Q7AssuranceNote: getVal("p6Q7AssuranceNote"),

                // P6 Q8
                p6Q8GhgProjectDetails: getVal("p6Q8GhgProjectDetails"),

                // P6 Q9
                p6Q9GenPlastCurr: getVal("p6Q9GenPlastCurr"), p6Q9GenPlastPrev: getVal("p6Q9GenPlastPrev"),
                p6Q9GenEwasteCurr: getVal("p6Q9GenEwasteCurr"), p6Q9GenEwastePrev: getVal("p6Q9GenEwastePrev"),
                p6Q9GenBioCurr: getVal("p6Q9GenBioCurr"), p6Q9GenBioPrev: getVal("p6Q9GenBioPrev"),
                p6Q9GenConstCurr: getVal("p6Q9GenConstCurr"), p6Q9GenConstPrev: getVal("p6Q9GenConstPrev"),
                p6Q9GenBattCurr: getVal("p6Q9GenBattCurr"), p6Q9GenBattPrev: getVal("p6Q9GenBattPrev"),
                p6Q9GenRadioCurr: getVal("p6Q9GenRadioCurr"), p6Q9GenRadioPrev: getVal("p6Q9GenRadioPrev"),
                p6Q9GenHazCurr: getVal("p6Q9GenHazCurr"), p6Q9GenHazPrev: getVal("p6Q9GenHazPrev"),
                p6Q9GenNonHazCurr: getVal("p6Q9GenNonHazCurr"), p6Q9GenNonHazPrev: getVal("p6Q9GenNonHazPrev"),
                p6Q9GenTotalCurr: getVal("p6Q9GenTotalCurr"), p6Q9GenTotalPrev: getVal("p6Q9GenTotalPrev"),
                p6Q9IntTurnCurr: getVal("p6Q9IntTurnCurr"), p6Q9IntTurnPrev: getVal("p6Q9IntTurnPrev"),
                p6Q9IntPppCurr: getVal("p6Q9IntPppCurr"), p6Q9IntPppPrev: getVal("p6Q9IntPppPrev"),
                p6Q9IntPhysCurr: getVal("p6Q9IntPhysCurr"), p6Q9IntPhysPrev: getVal("p6Q9IntPhysPrev"),
                p6Q9IntOptCurr: getVal("p6Q9IntOptCurr"), p6Q9IntOptPrev: getVal("p6Q9IntOptPrev"),
                p6Q9RecRecyCurr: getVal("p6Q9RecRecyCurr"), p6Q9RecRecyPrev: getVal("p6Q9RecRecyPrev"),
                p6Q9RecReuseCurr: getVal("p6Q9RecReuseCurr"), p6Q9RecReusePrev: getVal("p6Q9RecReusePrev"),
                p6Q9RecOthCurr: getVal("p6Q9RecOthCurr"), p6Q9RecOthPrev: getVal("p6Q9RecOthPrev"),
                p6Q9RecTotalCurr: getVal("p6Q9RecTotalCurr"), p6Q9RecTotalPrev: getVal("p6Q9RecTotalPrev"),
                p6Q9DispIncCurr: getVal("p6Q9DispIncCurr"), p6Q9DispIncPrev: getVal("p6Q9DispIncPrev"),
                p6Q9DispLandCurr: getVal("p6Q9DispLandCurr"), p6Q9DispLandPrev: getVal("p6Q9DispLandPrev"),
                p6Q9DispOthCurr: getVal("p6Q9DispOthCurr"), p6Q9DispOthPrev: getVal("p6Q9DispOthPrev"),
                p6Q9DispTotalCurr: getVal("p6Q9DispTotalCurr"), p6Q9DispTotalPrev: getVal("p6Q9DispTotalPrev"),
                p6Q9AssuranceNote: getVal("p6Q9AssuranceNote"),

        // P7
        p7AffiliationsCount: getVal("p7AffiliationsCount"),
        p7TradeAssociations: Array.from(document.querySelectorAll("#p7AssocTable tbody tr")).map(r => ({
            name: r.querySelector(".p7-nm").value, reach: r.querySelector(".p7-rc").value
        })),
        p7AntiCompetitiveDetails: getVal("p7AntiCompetitiveDetails"),
        p7PublicPolicyPositions: Array.from(document.querySelectorAll("#p7PolicyTable tbody tr")).map(r => ({
            policyAdvocated: r.querySelector(".p7-pol").value, methodResorted: r.querySelector(".p7-meth").value, webLink: r.querySelector(".p7-lnk").value
        })),

        // P8
        p8SiaDetails: getVal("p8SiaDetails"),
        p8GrievanceMechanism: getVal("p8GrievanceMechanism"),
        p8InputMsmeCurr: getVal("p8InputMsmeCurr"), p8InputMsmePrev: getVal("p8InputMsmePrev"),
        p8InputIndiaCurr: getVal("p8InputIndiaCurr"), p8InputIndiaPrev: getVal("p8InputIndiaPrev"),
        p8RandRProjects: Array.from(document.querySelectorAll("#p8RrTable tbody tr")).map(r => ({
            projectName: r.querySelector(".rr-nm").value, state: r.querySelector(".rr-st").value, district: r.querySelector(".rr-dt").value, noOfPafs: r.querySelector(".rr-pf").value, percCovered: r.querySelector(".rr-pc").value
        })),
        p8AspirationalDistricts: Array.from(document.querySelectorAll("#p8AspTable tbody tr")).map(r => ({
            state: r.querySelector(".asp-st").value, district: r.querySelector(".asp-dt").value, amountSpent: r.querySelector(".asp-am").value
        })),
        p8PreferentialProcurement: getVal("p8PreferentialProcurement"),
        p8MarginalizedGroups: getVal("p8MarginalizedGroups"),
        p8ProcurementPercentage: getVal("p8ProcurementPercentage"),
        p8CsrBeneficiaries: Array.from(document.querySelectorAll("#p8BenTable tbody tr")).map(r => ({
            csrProject: r.querySelector(".ben-pj").value, noBenefitted: r.querySelector(".ben-no").value, percVulnerable: r.querySelector(".ben-pc").value
        })),

        // P9
        p9ConsumerComplaintMech: getVal("p9ConsumerComplaintMech"),
        p9DataPrivacyCurr: getVal("p9DataPrivacyCurr"), p9DataPrivacyPrev: getVal("p9DataPrivacyPrev"),
        p9AdvertisingCurr: getVal("p9AdvertisingCurr"), p9AdvertisingPrev: getVal("p9AdvertisingPrev"),
        p9CyberCurr: getVal("p9CyberCurr"), p9CyberPrev: getVal("p9CyberPrev"),
        p9DeliveryCurr: getVal("p9DeliveryCurr"), p9DeliveryPrev: getVal("p9DeliveryPrev"),
        p9RestrictiveCurr: getVal("p9RestrictiveCurr"), p9RestrictivePrev: getVal("p9RestrictivePrev"),
        p9UnfairCurr: getVal("p9UnfairCurr"), p9UnfairPrev: getVal("p9UnfairPrev"),
        p9OtherCurr: getVal("p9OtherCurr"), p9OtherPrev: getVal("p9OtherPrev"),
        p9RecallVoluntary: getVal("p9RecallVoluntary"),
        p9RecallForced: getVal("p9RecallForced"),
        p9CyberSecurityPolicy: getVal("p9CyberSecurityPolicy"),
        p9CorrectiveActions: getVal("p9CorrectiveActions"),
        p9InfoChannels: getVal("p9InfoChannels"),
        p9SafeUsageSteps: getVal("p9SafeUsageSteps"),
        p9DisruptionInfo: getVal("p9DisruptionInfo"),
        p9ProductInfoDisplay: getVal("p9ProductInfoDisplay"),
        p9CustomerSatSurvey: getVal("p9CustomerSatSurvey"),

        // P6 Q10
                p6Q10WastePractices: getVal("p6Q10WastePractices"),

                // P6 Q11, Q12, Q13 (Dynamic Arrays)
                p6Q11EcoList: Array.from(document.querySelectorAll("#p6Q11EcoTable tbody tr")).map(r => ({
                    sNo: r.querySelector(".eco-sno").value, location: r.querySelector(".eco-loc").value,
                    type: r.querySelector(".eco-type").value, compliance: r.querySelector(".eco-comp").value
                })),
                p6Q12EiaList: Array.from(document.querySelectorAll("#p6Q12EiaTable tbody tr")).map(r => ({
                    name: r.querySelector(".eia-name").value, notifNo: r.querySelector(".eia-notif").value,
                    date: r.querySelector(".eia-date").value, independent: r.querySelector(".eia-indep").value,
                    publicDomain: r.querySelector(".eia-pub").value, link: r.querySelector(".eia-link").value
                })),
                p6Q13NonCompList: Array.from(document.querySelectorAll("#p6Q13NonCompTable tbody tr")).map(r => ({
                    sNo: r.querySelector(".nc-sno").value, law: r.querySelector(".nc-law").value,
                    details: r.querySelector(".nc-details").value, fines: r.querySelector(".nc-fines").value,
                    action: r.querySelector(".nc-action").value
                })),

                // P6 LEADERSHIP
                        p6LeadQ1Facilities: getVal("p6LeadQ1Facilities"),
                        p6LeadQ1WithSurfCurr: getVal("p6LeadQ1WithSurfCurr"), p6LeadQ1WithSurfPrev: getVal("p6LeadQ1WithSurfPrev"),
                        p6LeadQ1WithGroundCurr: getVal("p6LeadQ1WithGroundCurr"), p6LeadQ1WithGroundPrev: getVal("p6LeadQ1WithGroundPrev"),
                        p6LeadQ1WithThirdCurr: getVal("p6LeadQ1WithThirdCurr"), p6LeadQ1WithThirdPrev: getVal("p6LeadQ1WithThirdPrev"),
                        p6LeadQ1WithSeaCurr: getVal("p6LeadQ1WithSeaCurr"), p6LeadQ1WithSeaPrev: getVal("p6LeadQ1WithSeaPrev"),
                        p6LeadQ1WithOtherCurr: getVal("p6LeadQ1WithOtherCurr"), p6LeadQ1WithOtherPrev: getVal("p6LeadQ1WithOtherPrev"),
                        p6LeadQ1WithTotalCurr: getVal("p6LeadQ1WithTotalCurr"), p6LeadQ1WithTotalPrev: getVal("p6LeadQ1WithTotalPrev"),
                        p6LeadQ1ConsTotalCurr: getVal("p6LeadQ1ConsTotalCurr"), p6LeadQ1ConsTotalPrev: getVal("p6LeadQ1ConsTotalPrev"),
                        p6LeadQ1IntTurnCurr: getVal("p6LeadQ1IntTurnCurr"), p6LeadQ1IntTurnPrev: getVal("p6LeadQ1IntTurnPrev"),
                        p6LeadQ1IntOptCurr: getVal("p6LeadQ1IntOptCurr"), p6LeadQ1IntOptPrev: getVal("p6LeadQ1IntOptPrev"),
                        p6LeadQ1DisSurfNoCurr: getVal("p6LeadQ1DisSurfNoCurr"), p6LeadQ1DisSurfNoPrev: getVal("p6LeadQ1DisSurfNoPrev"),
                        p6LeadQ1DisSurfWithCurr: getVal("p6LeadQ1DisSurfWithCurr"), p6LeadQ1DisSurfWithPrev: getVal("p6LeadQ1DisSurfWithPrev"), p6LeadQ1DisSurfLevel: getVal("p6LeadQ1DisSurfLevel"),
                        p6LeadQ1DisGroundNoCurr: getVal("p6LeadQ1DisGroundNoCurr"), p6LeadQ1DisGroundNoPrev: getVal("p6LeadQ1DisGroundNoPrev"),
                        p6LeadQ1DisGroundWithCurr: getVal("p6LeadQ1DisGroundWithCurr"), p6LeadQ1DisGroundWithPrev: getVal("p6LeadQ1DisGroundWithPrev"), p6LeadQ1DisGroundLevel: getVal("p6LeadQ1DisGroundLevel"),
                        p6LeadQ1DisSeaNoCurr: getVal("p6LeadQ1DisSeaNoCurr"), p6LeadQ1DisSeaNoPrev: getVal("p6LeadQ1DisSeaNoPrev"),
                        p6LeadQ1DisSeaWithCurr: getVal("p6LeadQ1DisSeaWithCurr"), p6LeadQ1DisSeaWithPrev: getVal("p6LeadQ1DisSeaWithPrev"), p6LeadQ1DisSeaLevel: getVal("p6LeadQ1DisSeaLevel"),
                        p6LeadQ1DisThirdNoCurr: getVal("p6LeadQ1DisThirdNoCurr"), p6LeadQ1DisThirdNoPrev: getVal("p6LeadQ1DisThirdNoPrev"),
                        p6LeadQ1DisThirdWithCurr: getVal("p6LeadQ1DisThirdWithCurr"), p6LeadQ1DisThirdWithPrev: getVal("p6LeadQ1DisThirdWithPrev"), p6LeadQ1DisThirdLevel: getVal("p6LeadQ1DisThirdLevel"),
                        p6LeadQ1DisOtherNoCurr: getVal("p6LeadQ1DisOtherNoCurr"), p6LeadQ1DisOtherNoPrev: getVal("p6LeadQ1DisOtherNoPrev"),
                        p6LeadQ1DisOtherWithCurr: getVal("p6LeadQ1DisOtherWithCurr"), p6LeadQ1DisOtherWithPrev: getVal("p6LeadQ1DisOtherWithPrev"), p6LeadQ1DisOtherLevel: getVal("p6LeadQ1DisOtherLevel"),
                        p6LeadQ1DisTotalCurr: getVal("p6LeadQ1DisTotalCurr"), p6LeadQ1DisTotalPrev: getVal("p6LeadQ1DisTotalPrev"),
                        p6LeadQ1AssuranceNote: getVal("p6LeadQ1AssuranceNote"),
                        p6LeadQ2Scope3Curr: getVal("p6LeadQ2Scope3Curr"), p6LeadQ2Scope3Prev: getVal("p6LeadQ2Scope3Prev"),
                        p6LeadQ2IntTurnCurr: getVal("p6LeadQ2IntTurnCurr"), p6LeadQ2IntTurnPrev: getVal("p6LeadQ2IntTurnPrev"),
                        p6LeadQ2IntOptCurr: getVal("p6LeadQ2IntOptCurr"), p6LeadQ2IntOptPrev: getVal("p6LeadQ2IntOptPrev"),
                        p6LeadQ2AssuranceNote: getVal("p6LeadQ2AssuranceNote"),
                        p6LeadQ3EcoImpact: getVal("p6LeadQ3EcoImpact"),
                        p6LeadQ4InitiativesList: Array.from(document.querySelectorAll("#p6LeadQ4Table tbody tr")).map(r => ({
                            sNo: r.querySelector(".init-sno").value, initiative: r.querySelector(".init-name").value,
                            details: r.querySelector(".init-details").value, outcome: r.querySelector(".init-outcome").value
                        })),
                        p6LeadQ5DisasterPlan: getVal("p6LeadQ5DisasterPlan"),
                        p6LeadQ6ValueChainImpact: getVal("p6LeadQ6ValueChainImpact"),
                        p6LeadQ7ValueChainAssessed: getVal("p6LeadQ7ValueChainAssessed"),

                        // P7 Save
                                p7Q1aAffiliations: getVal("p7Q1aAffiliations"),
                                p7Q1bList: Array.from(document.querySelectorAll("#p7Q1bTable tbody tr")).map(r => ({
                                    sNo: r.querySelector(".assoc-sno").value,
                                    name: r.querySelector(".assoc-name").value,
                                    reach: r.querySelector(".assoc-reach").value
                                })),
                                p7Q2List: Array.from(document.querySelectorAll("#p7Q2Table tbody tr")).map(r => ({
                                    authority: r.querySelector(".ac-auth").value,
                                    brief: r.querySelector(".ac-brief").value,
                                    correctiveAction: r.querySelector(".ac-action").value
                                })),
                                p7LeadQ1List: Array.from(document.querySelectorAll("#p7LeadQ1Table tbody tr")).map(r => ({
                                    sNo: r.querySelector(".pol-sno").value,
                                    policy: r.querySelector(".pol-advocated").value,
                                    method: r.querySelector(".pol-method").value,
                                    publicDomain: r.querySelector(".pol-public").value,
                                    frequency: r.querySelector(".pol-freq").value,
                                    link: r.querySelector(".pol-link").value
                                })),

                                // P8 Save
                                        p8Q1SiaList: Array.from(document.querySelectorAll("#p8Q1SiaTable tbody tr")).map(r => ({
                                            name: r.querySelector(".sia-name").value, notifNo: r.querySelector(".sia-notif").value,
                                            date: r.querySelector(".sia-date").value, independent: r.querySelector(".sia-indep").value,
                                            publicDomain: r.querySelector(".sia-pub").value, link: r.querySelector(".sia-link").value
                                        })),
                                        p8Q2RrList: Array.from(document.querySelectorAll("#p8Q2RrTable tbody tr")).map(r => ({
                                            sNo: r.querySelector(".rr-sno").value, name: r.querySelector(".rr-name").value,
                                            state: r.querySelector(".rr-state").value, district: r.querySelector(".rr-district").value,
                                            pafs: r.querySelector(".rr-pafs").value, perc: r.querySelector(".rr-perc").value, amount: r.querySelector(".rr-amount").value
                                        })),
                                        p8Q3GrievanceMech: getVal("p8Q3GrievanceMech"),
                                        p8Q4MsmeCurr: getVal("p8Q4MsmeCurr"), p8Q4MsmePrev: getVal("p8Q4MsmePrev"),
                                        p8Q4IndiaCurr: getVal("p8Q4IndiaCurr"), p8Q4IndiaPrev: getVal("p8Q4IndiaPrev"),
                                        p8Q5RuralCurr: getVal("p8Q5RuralCurr"), p8Q5RuralPrev: getVal("p8Q5RuralPrev"),
                                        p8Q5SemiCurr: getVal("p8Q5SemiCurr"), p8Q5SemiPrev: getVal("p8Q5SemiPrev"),
                                        p8Q5UrbanCurr: getVal("p8Q5UrbanCurr"), p8Q5UrbanPrev: getVal("p8Q5UrbanPrev"),
                                        p8Q5MetroCurr: getVal("p8Q5MetroCurr"), p8Q5MetroPrev: getVal("p8Q5MetroPrev"),

    };

    fetch("http://localhost:8080/api/report/generate", {
        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": "Bearer " + token },
        body: JSON.stringify(formData)
    })
    .then(async res => {
        if(res.ok) return res.blob();
        throw new Error(await res.text());
    })
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "BRSR_Final_Report.docx";
        document.body.appendChild(a);
        a.click();
        a.remove();
        alert("Report Completed!");
        window.location.href = "industry_homepage.html";
    })
    .catch(err => alert("Error: " + err.message));
}

function addP6Q11Row(d={}) {
    const tbody = document.querySelector("#p6Q11EcoTable tbody");
    const rowCount = tbody.children.length + 1;
    const row = `<tr>
        <td><input type="text" class="eco-sno" value="${d.sNo || rowCount}" style="width:50px;"></td>
        <td><input type="text" class="eco-loc" value="${d.location || ''}"></td>
        <td><input type="text" class="eco-type" value="${d.type || ''}"></td>
        <td><input type="text" class="eco-comp" value="${d.compliance || ''}"></td>
        <td class="btn-remove" onclick="this.parentElement.remove()" style="cursor:pointer; color:red; text-align:center;">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addP6Q12Row(d={}) {
    const tbody = document.querySelector("#p6Q12EiaTable tbody");
    const row = `<tr>
        <td><input type="text" class="eia-name" value="${d.name || ''}"></td>
        <td><input type="text" class="eia-notif" value="${d.notifNo || ''}"></td>
        <td><input type="date" class="eia-date" value="${d.date || ''}"></td>
        <td><input type="text" class="eia-indep" value="${d.independent || ''}"></td>
        <td><input type="text" class="eia-pub" value="${d.publicDomain || ''}"></td>
        <td><input type="text" class="eia-link" value="${d.link || ''}"></td>
        <td class="btn-remove" onclick="this.parentElement.remove()" style="cursor:pointer; color:red; text-align:center;">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addP6Q13Row(d={}) {
    const tbody = document.querySelector("#p6Q13NonCompTable tbody");
    const rowCount = tbody.children.length + 1;
    const row = `<tr>
        <td><input type="text" class="nc-sno" value="${d.sNo || rowCount}" style="width:50px;"></td>
        <td><input type="text" class="nc-law" value="${d.law || ''}"></td>
        <td><input type="text" class="nc-details" value="${d.details || ''}"></td>
        <td><input type="text" class="nc-fines" value="${d.fines || ''}"></td>
        <td><input type="text" class="nc-action" value="${d.action || ''}"></td>
        <td class="btn-remove" onclick="this.parentElement.remove()" style="cursor:pointer; color:red; text-align:center;">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addP6LeadQ4Row(d={}) {
    const tbody = document.querySelector("#p6LeadQ4Table tbody");
    const rowCount = tbody.children.length + 1;
    const row = `<tr>
        <td><input type="text" class="init-sno" value="${d.sNo || rowCount}" style="width:50px;"></td>
        <td><input type="text" class="init-name" value="${d.initiative || ''}"></td>
        <td><textarea class="init-details">${d.details || ''}</textarea></td>
        <td><input type="text" class="init-outcome" value="${d.outcome || ''}"></td>
        <td class="btn-remove" onclick="this.parentElement.remove()" style="cursor:pointer; color:red; text-align:center;">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addP7Q1bRow(d={}) {
    const tbody = document.querySelector("#p7Q1bTable tbody");
    const rowCount = tbody.children.length + 1;
    const row = `<tr>
        <td><input type="text" class="assoc-sno" value="${d.sNo || rowCount}"></td>
        <td><input type="text" class="assoc-name" value="${d.name || ''}"></td>
        <td><input type="text" class="assoc-reach" value="${d.reach || ''}"></td>
        <td class="btn-remove" style="text-align:center; cursor:pointer; color:red;" onclick="this.parentElement.remove()">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addP7Q2Row(d={}) {
    const tbody = document.querySelector("#p7Q2Table tbody");
    const row = `<tr>
        <td><input type="text" class="ac-auth" value="${d.authority || ''}"></td>
        <td><textarea class="ac-brief" rows="2">${d.brief || ''}</textarea></td>
        <td><textarea class="ac-action" rows="2">${d.correctiveAction || ''}</textarea></td>
        <td class="btn-remove" style="text-align:center; cursor:pointer; color:red;" onclick="this.parentElement.remove()">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addP7LeadQ1Row(d={}) {
    const tbody = document.querySelector("#p7LeadQ1Table tbody");
    const rowCount = tbody.children.length + 1;
    const row = `<tr>
        <td><input type="text" class="pol-sno" value="${d.sNo || rowCount}"></td>
        <td><textarea class="pol-advocated" rows="2">${d.policy || ''}</textarea></td>
        <td><textarea class="pol-method" rows="2">${d.method || ''}</textarea></td>
        <td><input type="text" class="pol-public" value="${d.publicDomain || ''}"></td>
        <td><input type="text" class="pol-freq" value="${d.frequency || ''}"></td>
        <td><input type="text" class="pol-link" value="${d.link || ''}"></td>
        <td class="btn-remove" style="text-align:center; cursor:pointer; color:red;" onclick="this.parentElement.remove()">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addP8Q1Row(d={}) {
    const tbody = document.querySelector("#p8Q1SiaTable tbody");
    const row = `<tr>
        <td><textarea class="sia-name" rows="2">${d.name || ''}</textarea></td>
        <td><input type="text" class="sia-notif" value="${d.notifNo || ''}"></td>
        <td><input type="date" class="sia-date" value="${d.date || ''}"></td>
        <td><input type="text" class="sia-indep" value="${d.independent || ''}"></td>
        <td><input type="text" class="sia-pub" value="${d.publicDomain || ''}"></td>
        <td><input type="text" class="sia-link" value="${d.link || ''}"></td>
        <td class="btn-remove" style="text-align:center; cursor:pointer; color:red;" onclick="this.parentElement.remove()">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addP8Q2Row(d={}) {
    const tbody = document.querySelector("#p8Q2RrTable tbody");
    const rowCount = tbody.children.length + 1;
    const row = `<tr>
        <td><input type="text" class="rr-sno" value="${d.sNo || rowCount}"></td>
        <td><input type="text" class="rr-name" value="${d.name || ''}"></td>
        <td><input type="text" class="rr-state" value="${d.state || ''}"></td>
        <td><input type="text" class="rr-district" value="${d.district || ''}"></td>
        <td><input type="text" class="rr-pafs" value="${d.pafs || ''}"></td>
        <td><input type="text" class="rr-perc" value="${d.perc || ''}"></td>
        <td><input type="text" class="rr-amount" value="${d.amount || ''}"></td>
        <td class="btn-remove" style="text-align:center; cursor:pointer; color:red;" onclick="this.parentElement.remove()">X</td>
    </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}