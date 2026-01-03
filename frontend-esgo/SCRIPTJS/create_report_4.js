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
        // P6
        setVal("p6ElecConsumCurr", data.p6ElecConsumCurr); setVal("p6ElecConsumPrev", data.p6ElecConsumPrev);
        setVal("p6FuelConsumCurr", data.p6FuelConsumCurr); setVal("p6FuelConsumPrev", data.p6FuelConsumPrev);
        setVal("p6EnergyOtherCurr", data.p6EnergyOtherCurr); setVal("p6EnergyOtherPrev", data.p6EnergyOtherPrev);
        setVal("p6EnergyTotalCurr", data.p6EnergyTotalCurr); setVal("p6EnergyTotalPrev", data.p6EnergyTotalPrev);
        setVal("p6EnergyIntensityCurr", data.p6EnergyIntensityCurr); setVal("p6EnergyIntensityPrev", data.p6EnergyIntensityPrev);
        setVal("p6EnergyNote", data.p6EnergyNote);
        setVal("p6Scope1Curr", data.p6Scope1Curr); setVal("p6Scope1Prev", data.p6Scope1Prev);
        setVal("p6Scope2Curr", data.p6Scope2Curr); setVal("p6Scope2Prev", data.p6Scope2Prev);
        setVal("p6ScopeTotalCurr", data.p6ScopeTotalCurr); setVal("p6ScopeTotalPrev", data.p6ScopeTotalPrev);
        setVal("p6EmissionIntensityCurr", data.p6EmissionIntensityCurr); setVal("p6EmissionIntensityPrev", data.p6EmissionIntensityPrev);
        setVal("p6EmissionNote", data.p6EmissionNote);
        setVal("p6WaterWithdrawalCurr", data.p6WaterWithdrawalCurr); setVal("p6WaterWithdrawalPrev", data.p6WaterWithdrawalPrev);
        setVal("p6WaterConsumCurr", data.p6WaterConsumCurr); setVal("p6WaterConsumPrev", data.p6WaterConsumPrev);
        setVal("p6WaterNote", data.p6WaterNote);

        if (data.p6WasteManagementList) data.p6WasteManagementList.forEach(r => addWasteRow(r));
        else ["Plastic", "E-waste", "Bio-medical", "Hazardous", "Other"].forEach(c => addWasteRow({category: c}));
        setVal("p6WasteNote", data.p6WasteNote);

        // P7
        setVal("p7AffiliationsCount", data.p7AffiliationsCount);
        setVal("p7AntiCompetitiveDetails", data.p7AntiCompetitiveDetails);
        if(data.p7TradeAssociations) data.p7TradeAssociations.forEach(r => addP7Row(r));
        if(data.p7PublicPolicyPositions) data.p7PublicPolicyPositions.forEach(r => addP7PolicyRow(r));

        // P8
        setVal("p8SiaDetails", data.p8SiaDetails);
        setVal("p8GrievanceMechanism", data.p8GrievanceMechanism);
        setVal("p8InputMsmeCurr", data.p8InputMsmeCurr); setVal("p8InputMsmePrev", data.p8InputMsmePrev);
        setVal("p8InputIndiaCurr", data.p8InputIndiaCurr); setVal("p8InputIndiaPrev", data.p8InputIndiaPrev);
        if(data.p8RandRProjects) data.p8RandRProjects.forEach(r => addP8RrRow(r));
        if(data.p8AspirationalDistricts) data.p8AspirationalDistricts.forEach(r => addP8AspRow(r));
        setVal("p8PreferentialProcurement", data.p8PreferentialProcurement);
        setVal("p8MarginalizedGroups", data.p8MarginalizedGroups);
        setVal("p8ProcurementPercentage", data.p8ProcurementPercentage);
        if(data.p8CsrBeneficiaries) data.p8CsrBeneficiaries.forEach(r => addP8BenRow(r));

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

function generateFinalReport() {
    const token = localStorage.getItem("token");
    const formData = {
        id: parseInt(currentReportId),

        // P6
        p6ElecConsumCurr: getVal("p6ElecConsumCurr"), p6ElecConsumPrev: getVal("p6ElecConsumPrev"),
        p6FuelConsumCurr: getVal("p6FuelConsumCurr"), p6FuelConsumPrev: getVal("p6FuelConsumPrev"),
        p6EnergyOtherCurr: getVal("p6EnergyOtherCurr"), p6EnergyOtherPrev: getVal("p6EnergyOtherPrev"),
        p6EnergyTotalCurr: getVal("p6EnergyTotalCurr"), p6EnergyTotalPrev: getVal("p6EnergyTotalPrev"),
        p6EnergyIntensityCurr: getVal("p6EnergyIntensityCurr"), p6EnergyIntensityPrev: getVal("p6EnergyIntensityPrev"),
        p6EnergyNote: getVal("p6EnergyNote"),
        p6Scope1Curr: getVal("p6Scope1Curr"), p6Scope1Prev: getVal("p6Scope1Prev"),
        p6Scope2Curr: getVal("p6Scope2Curr"), p6Scope2Prev: getVal("p6Scope2Prev"),
        p6ScopeTotalCurr: getVal("p6ScopeTotalCurr"), p6ScopeTotalPrev: getVal("p6ScopeTotalPrev"),
        p6EmissionIntensityCurr: getVal("p6EmissionIntensityCurr"), p6EmissionIntensityPrev: getVal("p6EmissionIntensityPrev"),
        p6EmissionNote: getVal("p6EmissionNote"),
        p6WaterWithdrawalCurr: getVal("p6WaterWithdrawalCurr"), p6WaterWithdrawalPrev: getVal("p6WaterWithdrawalPrev"),
        p6WaterConsumCurr: getVal("p6WaterConsumCurr"), p6WaterConsumPrev: getVal("p6WaterConsumPrev"),
        p6WaterNote: getVal("p6WaterNote"),
        p6WasteManagementList: Array.from(document.querySelectorAll("#p6WasteTable tbody tr")).map(r => ({
            category: r.querySelector(".w-cat").value,
            currGenerated: r.querySelector(".w-cg").value, currRecycled: r.querySelector(".w-cr").value, currDisposed: r.querySelector(".w-cd").value,
            prevGenerated: r.querySelector(".w-pg").value, prevRecycled: r.querySelector(".w-pr").value, prevDisposed: r.querySelector(".w-pd").value
        })),
        p6WasteNote: getVal("p6WasteNote"),

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
        p9CustomerSatSurvey: getVal("p9CustomerSatSurvey")
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