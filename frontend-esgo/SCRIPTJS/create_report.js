// Global variable to store the ID
let currentReportId = null;

// --- Security Check & Load Data ---
document.addEventListener("DOMContentLoaded", function() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Please login first.");
        window.location.replace("login.html");
        return;
    }

    // --- CHECK FOR EDIT MODE ---
    const urlParams = new URLSearchParams(window.location.search);
    currentReportId = urlParams.get('edit');

    if (currentReportId) {
        // === EDIT MODE ===
        console.log("Edit mode active for report ID:", currentReportId);
        document.querySelector(".page-title").innerText = "Edit ESG Report";

        fetch(`http://localhost:8080/api/report/${currentReportId}`, {
            method: "GET",
            headers: { "Authorization": "Bearer " + token }
        })
            .then(res => res.json())
            .then(data => {
                console.log("Data received:", data);

                // 1. Fill Standard Fields (Section A, III, VI)
                fillStandardFields(data);

                // 2. Fill Section II: Business Activities
                const actBody = document.querySelector("#activitiesTable tbody");
                actBody.innerHTML = "";
                if (data.businessActivities && data.businessActivities.length > 0) {
                    data.businessActivities.forEach(act => addActivityRow(act));
                } else {
                    addActivityRow();
                }

                // 3. Fill Section II: Products
                const prodBody = document.querySelector("#productsTable tbody");
                prodBody.innerHTML = "";
                if (data.productsServices && data.productsServices.length > 0) {
                    document.getElementById("includeConsolidated").checked = data.includeConsolidated;
                    toggleConsolidatedColumns();
                    data.productsServices.forEach(prod => addProductRow(prod));
                } else {
                    addProductRow();
                }

                // 4. Fill Section IV: Women Representation
                setTimeout(() => {
                                    if (typeof calcOpsTotal === 'function') calcOpsTotal();
                                    if (typeof calcEmployeeTotals === 'function') calcEmployeeTotals();
                                    if (typeof calcWorkerTotals === 'function') calcWorkerTotals();
                                    if (typeof calcDiffAbledTotals === 'function') calcDiffAbledTotals();
                                }, 100);
                const womenBody = document.querySelector("#womenRepTable tbody");
                womenBody.innerHTML = "";
                if (data.womenRepresentationList && data.womenRepresentationList.length > 0) {
                    data.womenRepresentationList.forEach(item => addWomenRepRow(item));
                } else {
                    addDefaultWomenRows();
                }
                if(document.getElementById("womenRepresentationNotes"))
                    document.getElementById("womenRepresentationNotes").value = data.womenRepresentationNotes || "";

                // 5. Fill Section IV: Turnover Notes
                if(document.getElementById("turnoverNotes"))
                    document.getElementById("turnoverNotes").value = data.turnoverNotes || "";

                // 6. Fill Section V: Holding Companies
                // --- THIS WAS THE MISSING PART IN YOUR CODE ---
                const hcBody = document.querySelector("#holdingTable tbody");
                if (hcBody) {
                    hcBody.innerHTML = "";
                    if (data.holdingCompanies && data.holdingCompanies.length > 0) {
                        document.querySelector('input[name="holdingMode"][value="table"]').checked = true;
                        data.holdingCompanies.forEach(hc => addHoldingRow(hc));
                    } else {
                        if (data.holdingCompanyNote) {
                            document.querySelector('input[name="holdingMode"][value="note"]').checked = true;
                        } else {
                            addHoldingRow();
                        }
                    }
                    toggleHoldingMode();
                    if(document.getElementById("holdingCompanyNote"))
                        document.getElementById("holdingCompanyNote").value = data.holdingCompanyNote || "";
                }

                // Fill Section VII: Complaints
                const compBody = document.querySelector("#complaintsTable tbody");
                compBody.innerHTML = "";
                if (data.complaintsList && data.complaintsList.length > 0) {
                    data.complaintsList.forEach(c => addComplaintRow(c));
                } else {
                    addDefaultComplaintRows(); // Add standard rows
                }

                // Fill Headers
                if(document.getElementById("complaintsFyCurrentHeader"))
                    document.getElementById("complaintsFyCurrentHeader").value = data.complaintsFyCurrentHeader || "";
                if(document.getElementById("complaintsFyPreviousHeader"))
                    document.getElementById("complaintsFyPreviousHeader").value = data.complaintsFyPreviousHeader || "";

                // Fill Q24 Material Issues
                if(document.getElementById("materialIssuesNote"))
                    document.getElementById("materialIssuesNote").value = data.materialIssuesNote || "";

                const miBody = document.querySelector("#materialIssuesTable tbody");
                miBody.innerHTML = "";
                if (data.materialIssues && data.materialIssues.length > 0) {
                    data.materialIssues.forEach(mi => addMaterialIssueRow(mi));
                } else {
                    addMaterialIssueRow(); // Default row
                }

            })
            .catch(err => console.error("Failed to load report data", err));
    } else {
        // === NEW REPORT MODE ===
        addActivityRow();
        addProductRow();
        addDefaultWomenRows();
        addHoldingRow(); // Add empty row for Holding Companies
        toggleHoldingMode();
        addDefaultComplaintRows();
        addMaterialIssueRow();
    }
});

// --- HELPER: Fill Standard Inputs ---
function fillStandardFields(data) {
    const fields = [
        // Section A
        "cin", "companyName", "yearOfInc", "registeredAddress", "corporateAddress",
        "email", "telephone", "website", "reportingYear", "stockExchanges",
        "paidUpCapital", "reportingBoundary", "contactPersonName",
        "contactPersonDesignation", "contactPersonAddress", "contactPersonEmail",
        "contactPersonTelephone", "assuranceProviderName", "assuranceType",

        // Section 3 (Operations) - MUST BE IN THIS LIST TO SHOW UP
        "plantsNational", "officesNational", "totalNational",
        "plantsInternational", "officesInternational", "totalInternational",
        // NEW Q17 FIELDS
        "locationsNationalNumber", "locationsInternationalNumber",
        "contributionExports", "typesOfCustomers",

        // Section IV - Employees (18a)
        "empPermTotal", "empPermMaleNo", "empPermMalePerc", "empPermFemaleNo", "empPermFemalePerc", "empPermOtherNo", "empPermOtherPerc",
        "empTempTotal", "empTempMaleNo", "empTempMalePerc", "empTempFemaleNo", "empTempFemalePerc", "empTempOtherNo", "empTempOtherPerc",
        "workPermTotal", "workPermMaleNo", "workPermMalePerc", "workPermFemaleNo", "workPermFemalePerc", "workPermOtherNo", "workPermOtherPerc",
        "workTempTotal", "workTempMaleNo", "workTempMalePerc", "workTempFemaleNo", "workTempFemalePerc", "workTempOtherNo", "workTempOtherPerc",

        // Section IV - Diff Abled (18b)
        "daEmpPermTotal", "daEmpPermMaleNo", "daEmpPermMalePerc", "daEmpPermFemaleNo", "daEmpPermFemalePerc", "daEmpPermOtherNo", "daEmpPermOtherPerc",
        "daEmpTempTotal", "daEmpTempMaleNo", "daEmpTempMalePerc", "daEmpTempFemaleNo", "daEmpTempFemalePerc", "daEmpTempOtherNo", "daEmpTempOtherPerc",
        "daWorkPermTotal", "daWorkPermMaleNo", "daWorkPermMalePerc", "daWorkPermFemaleNo", "daWorkPermFemalePerc", "daWorkPermOtherNo", "daWorkPermOtherPerc",
        "daWorkTempTotal", "daWorkTempMaleNo", "daWorkTempMalePerc", "daWorkTempFemaleNo", "daWorkTempFemalePerc", "daWorkTempOtherNo", "daWorkTempOtherPerc",

        "employeeNotesA", "employeeNotesB",

        // Q20 Turnover
        "turnoverEmpCurrMale", "turnoverEmpCurrFemale", "turnoverEmpCurrTotal",
        "turnoverWorkCurrMale", "turnoverWorkCurrFemale", "turnoverWorkCurrTotal",
        "turnoverEmpPrevMale", "turnoverEmpPrevFemale", "turnoverEmpPrevTotal",
        "turnoverWorkPrevMale", "turnoverWorkPrevFemale", "turnoverWorkPrevTotal",
        "turnoverEmpPriorMale", "turnoverEmpPriorFemale", "turnoverEmpPriorTotal",
        "turnoverWorkPriorMale", "turnoverWorkPriorFemale", "turnoverWorkPriorTotal",
        "turnoverNotes",
        // NEW: Financial Year Headers
        "fyCurrent", "fyPrevious", "fyPrior",

        //section V and VI
        "holdingCompanyNote", "csrApplicable", "csrTurnover", "csrNetWorth",

        //section VII
        "complaintsFyCurrentHeader", "complaintsFyPreviousHeader",

    ];

    fields.forEach(id => {
        if(document.getElementById(id)) {
            // Use empty string if data[id] is null
            document.getElementById(id).value = data[id] || "";
        }
    });
}

// --- ROW MANAGEMENT (Updated to accept data) ---

function addActivityRow(data = null) {
    const tbody = document.querySelector("#activitiesTable tbody");

    // Logic: If data exists, use it. If null, use empty string.
    const descMain = data ? (data.descriptionMain || "") : "";
    const descBus = data ? (data.descriptionBusiness || "") : "";
    const turnover = data ? (data.turnoverPercentage || "") : "";

    const row = `
        <tr>
            <td><input type="text" class="act-main" value="${descMain}" placeholder="e.g. Manufacturing"></td>
            <td><input type="text" class="act-bus" value="${descBus}" placeholder="e.g. Metal Products"></td>
            <td><input type="text" class="act-turn" value="${turnover}" placeholder="94%"></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

function addProductRow(data = null) {
    const tbody = document.querySelector("#productsTable tbody");
    const isConsolidated = document.getElementById("includeConsolidated").checked;
    const displayStyle = isConsolidated ? "table-cell" : "none";

    // Extract Data safely
    const name = data ? (data.productName || "") : "";
    const nic = data ? (data.nicCode || "") : "";
    const turnS = data ? (data.turnoverStandalone || "") : "";
    const percS = data ? (data.percentageStandalone || "") : "";
    const turnC = data ? (data.turnoverConsolidated || "") : "";
    const percC = data ? (data.percentageConsolidated || "") : "";

    const row = `
        <tr>
            <td><input type="text" class="prod-name" value="${name}"></td>
            <td><input type="text" class="prod-nic" value="${nic}"></td>
            <td><input type="text" class="prod-turn-s" value="${turnS}"></td>
            <td><input type="text" class="prod-perc-s" value="${percS}"></td>
            <td class="cons-col" style="display:${displayStyle}"><input type="text" class="prod-turn-c" value="${turnC}"></td>
            <td class="cons-col" style="display:${displayStyle}"><input type="text" class="prod-perc-c" value="${percC}"></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

function toggleConsolidatedColumns() {
    const isChecked = document.getElementById("includeConsolidated").checked;
    const cells = document.querySelectorAll(".cons-col");
    cells.forEach(cell => {
        cell.style.display = isChecked ? "table-cell" : "none";
    });
}

// --- HELPER: Auto-calculate Totals for Section III ---
// --- CALCULATION LOGIC ---

function calcEmployeeTotals() {
    calculateRowAndPercent("empPerm");
    calculateRowAndPercent("empTemp");
    sumVerticalRows("empPerm", "empTemp", "empGrand");
}

function calcWorkerTotals() {
    calculateRowAndPercent("workPerm");
    calculateRowAndPercent("workTemp");
    sumVerticalRows("workPerm", "workTemp", "workGrand");
}

function calcDiffAbledTotals() {
    calculateRowAndPercent("daEmpPerm");
    calculateRowAndPercent("daEmpTemp");
    sumVerticalRows("daEmpPerm", "daEmpTemp", "daEmpGrand");

    calculateRowAndPercent("daWorkPerm");
    calculateRowAndPercent("daWorkTemp");
    sumVerticalRows("daWorkPerm", "daWorkTemp", "daWorkGrand");
}

// Helper: Calculate Percentages based on Manually Entered Total
function calculateRowAndPercent(prefix) {
    const m = parseFloat(document.getElementById(prefix + "MaleNo").value) || 0;
    const f = parseFloat(document.getElementById(prefix + "FemaleNo").value) || 0;

    // READ the Total (User Input), default to 0 if empty
    const total = parseFloat(document.getElementById(prefix + "Total").value) || 0;

    // Calculate Percentages
    const mPerc = total > 0 ? ((m / total) * 100).toFixed(1) : "0.0";
    const fPerc = total > 0 ? ((f / total) * 100).toFixed(1) : "0.0";

    document.getElementById(prefix + "MalePerc").value = mPerc;
    document.getElementById(prefix + "FemalePerc").value = fPerc;
}

// Helper: Vertical Calculation (Summing up the Totals from rows above)
function sumVerticalRows(row1Prefix, row2Prefix, resultPrefix) {
    // Sum Counts
    const mTotal = (parseFloat(document.getElementById(row1Prefix + "MaleNo").value) || 0) +
        (parseFloat(document.getElementById(row2Prefix + "MaleNo").value) || 0);

    const fTotal = (parseFloat(document.getElementById(row1Prefix + "FemaleNo").value) || 0) +
        (parseFloat(document.getElementById(row2Prefix + "FemaleNo").value) || 0);

    // Sum the Manually Entered Totals
    const grandTotal = (parseFloat(document.getElementById(row1Prefix + "Total").value) || 0) +
        (parseFloat(document.getElementById(row2Prefix + "Total").value) || 0);

    // Update Grand Total Fields
    document.getElementById(resultPrefix + "Total").value = grandTotal;
    document.getElementById(resultPrefix + "MaleNo").value = mTotal;
    document.getElementById(resultPrefix + "FemaleNo").value = fTotal;

    // Calculate Grand Total Percentages
    const mPerc = grandTotal > 0 ? ((mTotal / grandTotal) * 100).toFixed(1) : "0.0";
    const fPerc = grandTotal > 0 ? ((fTotal / grandTotal) * 100).toFixed(1) : "0.0";

    document.getElementById(resultPrefix + "MalePerc").value = mPerc;
    document.getElementById(resultPrefix + "FemalePerc").value = fPerc;
}

//helper
function addDefaultWomenRows() {
    addWomenRepRow({ category: "Board of Directors" });
    addWomenRepRow({ category: "Key Management Personnel" });
}

// --- ROW MANAGEMENT FOR Q19 ---
function addWomenRepRow(data = null) {
    const tbody = document.querySelector("#womenRepTable tbody");

    const cat = data ? (data.category || "") : "";
    const tot = data ? (data.totalA || "") : "";
    const fem = data ? (data.femaleNoB || "") : "";
    const per = data ? (data.femalePerc || "") : "";

    const row = `
        <tr>
            <td><input type="text" class="wr-cat" value="${cat}" placeholder="Category Name"></td>
            <td><input type="number" class="wr-tot" value="${tot}" oninput="calcWomenPerc(this)"></td>
            <td><input type="number" class="wr-fem" value="${fem}" oninput="calcWomenPerc(this)"></td>
            <td><input type="text" class="wr-perc" value="${per}" readonly style="background:#f0f0f0;"></td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- CALCULATION LOGIC ---
function calcWomenPerc(inputElement) {
    // Find parent row
    const row = inputElement.closest("tr");

    const total = parseFloat(row.querySelector(".wr-tot").value) || 0;
    const female = parseFloat(row.querySelector(".wr-fem").value) || 0;

    let perc = "0.0";
    if (total > 0) {
        perc = ((female / total) * 100).toFixed(1);
    }

    row.querySelector(".wr-perc").value = perc;
}

// --- TOGGLE FUNCTION ---
function toggleHoldingMode() {
    const isTable = document.querySelector('input[name="holdingMode"][value="table"]').checked;
    document.getElementById("holdingTableContainer").style.display = isTable ? "block" : "none";
    document.getElementById("holdingNoteContainer").style.display = isTable ? "none" : "block";
}

// --- ROW FUNCTION ---
function addHoldingRow(data = null) {
    const tbody = document.querySelector("#holdingTable tbody");
    const name = data ? (data.name || "") : "";
    const type = data ? (data.type || "") : "";
    const share = data ? (data.sharesHeld || "") : "";
    const part = data ? (data.participateBusinessResponsibility || "No") : "No";

    const row = `
        <tr>
            <td><input type="text" class="hc-name" value="${name}" placeholder="Company Name"></td>
            <td>
                <select class="hc-type">
                    <option value="Subsidiary" ${type === 'Subsidiary' ? 'selected' : ''}>Subsidiary</option>
                    <option value="Associate" ${type === 'Associate' ? 'selected' : ''}>Associate</option>
                    <option value="Joint Venture" ${type === 'Joint Venture' ? 'selected' : ''}>Joint Venture</option>
                    <option value="Holding" ${type === 'Holding' ? 'selected' : ''}>Holding</option>
                </select>
            </td>
            <td><input type="text" class="hc-share" value="${share}" placeholder="%"></td>
            <td>
                <select class="hc-part">
                    <option value="Yes" ${part === 'Yes' ? 'selected' : ''}>Yes</option>
                    <option value="No" ${part === 'No' ? 'selected' : ''}>No</option>
                </select>
            </td>
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- HELPER: Default Rows ---
function addDefaultComplaintRows() {
    addComplaintRow({ stakeholder: "Communities" });
    addComplaintRow({ stakeholder: "Investors/Shareholders" });
    addComplaintRow({ stakeholder: "Employees and Workers" });
    addComplaintRow({ stakeholder: "Customers" });
    addComplaintRow({ stakeholder: "Value Chain Partners" });
}

// --- ROW MANAGEMENT ---
function addComplaintRow(data = null) {
    const tbody = document.querySelector("#complaintsTable tbody");

    // Safety check nulls
    const d = data || {};

    const row = `
        <tr>
            <td><input type="text" class="c-stake" value="${d.stakeholder || ''}" placeholder="Group Name"></td>
            
            <!-- Large Text Area for Mechanism -->
            <td><textarea class="c-mech" rows="3" style="width:100%; font-size:12px;" placeholder="Enter mechanism details or link...">${d.mechanism || ''}</textarea></td>
            
            <!-- Current FY -->
            <td><input type="number" class="c-cur-f" value="${d.currFiled || ''}" style="width:60px;"></td>
            <td><input type="number" class="c-cur-p" value="${d.currPending || ''}" style="width:60px;"></td>
            <td><input type="text" class="c-cur-r" value="${d.currRemarks || ''}"></td>
            
            <!-- Previous FY -->
            <td><input type="number" class="c-pre-f" value="${d.prevFiled || ''}" style="width:60px;"></td>
            <td><input type="number" class="c-pre-p" value="${d.prevPending || ''}" style="width:60px;"></td>
            <td><input type="text" class="c-pre-r" value="${d.prevRemarks || ''}"></td>
            
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- ROW MANAGEMENT (Q24) ---
// --- ROW MANAGEMENT (Q26 Material Issues) ---
function addMaterialIssueRow(data = null) {
    const tbody = document.querySelector("#materialIssuesTable tbody");
    const d = data || {};

    // Dropdown helpers
    const selRisk = d.riskOrOpportunity === 'Risk' ? 'selected' : '';
    const selOpp = d.riskOrOpportunity === 'Opportunity' ? 'selected' : '';

    const selPos = d.financialImplications === 'Positive' ? 'selected' : '';
    const selNeg = d.financialImplications === 'Negative' ? 'selected' : '';

    const row = `
        <tr>
            <td><input type="text" class="mi-desc" value="${d.description || ''}" placeholder="e.g. Climate Change"></td>

            <td>
                <select class="mi-ro">
                    <option value="Risk" ${selRisk}>Risk</option>
                    <option value="Opportunity" ${selOpp}>Opportunity</option>
                </select>
            </td>

            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;">
                    <button type="button" class="btn-ai-mini" onclick="generateTableFieldAI(this, 'rationale')" style="background:none; border:none; color:#2563eb; font-size:11px; cursor:pointer; font-weight:bold;">✨ AI Expand</button>
                </div>
                <textarea class="mi-rat" rows="3" style="width:100%; font-size:12px;" placeholder="Keywords...">${d.rationale || ''}</textarea>
            </td>

            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;">
                    <button type="button" class="btn-ai-mini" onclick="generateTableFieldAI(this, 'approach')" style="background:none; border:none; color:#2563eb; font-size:11px; cursor:pointer; font-weight:bold;">✨ AI Expand</button>
                </div>
                <textarea class="mi-app" rows="3" style="width:100%; font-size:12px;" placeholder="Keywords...">${d.approach || ''}</textarea>
            </td>

            <td>
                <select class="mi-fin">
                    <option value="Positive" ${selPos}>Positive</option>
                    <option value="Negative" ${selNeg}>Negative</option>
                </select>
            </td>

            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- HELPER: Auto-calculate Totals for Section III (Operations) ---
function calcOpsTotal() {
    // National Calculation
    const plantsN = parseFloat(document.getElementById('plantsNational').value) || 0;
    const officesN = parseFloat(document.getElementById('officesNational').value) || 0;
    document.getElementById('totalNational').value = plantsN + officesN;

    // International Calculation
    const plantsI = parseFloat(document.getElementById('plantsInternational').value) || 0;
    const officesI = parseFloat(document.getElementById('officesInternational').value) || 0;
    document.getElementById('totalInternational').value = plantsI + officesI;
}

//generate ai content
async function generateAIContent(fieldId) {
    const textarea = document.getElementById(fieldId);
    const userInput = textarea.value;
    const companyName = document.getElementById('companyName').value || "the company";

    if (!userInput || userInput.length < 5) {
        alert("Please type a few keywords first (e.g., '20% to USA' or 'Retail B2B')");
        return;
    }

    textarea.placeholder = "Gemini is thinking...";

    try {
        const response = await fetch("http://localhost:8080/api/ai/generate-esg", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            body: JSON.stringify({
                prompt: `Write a professional ESG paragraph about ${fieldId} using these notes: ${userInput}`
            })
        });

        const data = await response.json();
        textarea.value = data.text; // This matches response.put("text", ...)
    } catch (err) {
        console.error("AI Error:", err);
        alert("Could not reach AI service.");
    }
}

// --- ROW MANAGEMENT ---
function addComplaintRow(data = null) {
    const tbody = document.querySelector("#complaintsTable tbody");

    // Safety check nulls
    const d = data || {};

    const row = `
        <tr>
            <td><input type="text" class="c-stake" value="${d.stakeholder || ''}" placeholder="Group Name"></td>

            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;">
                    <button type="button" class="btn-ai-mini" onclick="generateTableFieldAI(this, 'mechanism')" style="background:none; border:none; color:#2563eb; font-size:11px; cursor:pointer; font-weight:bold;">✨ AI Expand</button>
                </div>
                <textarea class="c-mech" rows="3" style="width:100%; font-size:12px;" placeholder="Keywords (e.g. 'portal and toll free number')...">${d.mechanism || ''}</textarea>
            </td>

            <td><input type="number" class="c-cur-f" value="${d.currFiled || ''}" style="width:60px;"></td>
            <td><input type="number" class="c-cur-p" value="${d.currPending || ''}" style="width:60px;"></td>
            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;">
                    <button type="button" class="btn-ai-mini" onclick="generateTableFieldAI(this, 'remarks')" style="background:none; border:none; color:#2563eb; font-size:11px; cursor:pointer; font-weight:bold;">✨ AI</button>
                </div>
                <input type="text" class="c-cur-r" value="${d.currRemarks || ''}" placeholder="Keywords...">
            </td>

            <td><input type="number" class="c-pre-f" value="${d.prevFiled || ''}" style="width:60px;"></td>
            <td><input type="number" class="c-pre-p" value="${d.prevPending || ''}" style="width:60px;"></td>
            <td>
                <div style="display:flex; justify-content:flex-end; margin-bottom:2px;">
                    <button type="button" class="btn-ai-mini" onclick="generateTableFieldAI(this, 'remarks')" style="background:none; border:none; color:#2563eb; font-size:11px; cursor:pointer; font-weight:bold;">✨ AI</button>
                </div>
                <input type="text" class="c-pre-r" value="${d.prevRemarks || ''}" placeholder="Keywords...">
            </td>

            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- SMART CELL-LEVEL AI GENERATOR ---
async function generateTableFieldAI(buttonElement, fieldType) {
    // 1. Find the input/textarea right below the clicked button's container
    const container = buttonElement.parentElement;
    const inputField = container.nextElementSibling;

    if (!inputField) {
        console.error("Could not find input field next to the button container.");
        return;
    }

    const userInput = inputField.value.trim();

    if (!userInput || userInput.length < 3) {
        alert("Please type a few keywords in the box first (e.g., 'portal and toll free').");
        return;
    }

    // 2. SAFELY get the context depending on which table we are in
    const row = buttonElement.closest('tr');

    // Attempt to find both elements (one will be null depending on the table)
    const stakeElem = row.querySelector('.c-stake');
    const issueElem = row.querySelector('.mi-desc');

    // Safely extract the value without crashing
    const stakeholder = stakeElem ? (stakeElem.value || "this stakeholder group") : "the stakeholder";
    const issue = issueElem ? (issueElem.value || "this material issue") : "the issue";

    // 3. UI Feedback
    inputField.disabled = true;
    const originalPlaceholder = inputField.placeholder;
    inputField.placeholder = "Gemini is writing...";

    // 4. Build the dynamic prompt based on which column was clicked
    let promptText = "";
    if (fieldType === 'mechanism') {
        promptText = `You are an expert ESG consultant writing a BRSR report. Expand the following notes into a concise, professional Grievance Redressal Mechanism description (1-2 sentences) specifically for '${stakeholder}'. Notes: ${userInput}`;
    } else if (fieldType === 'remarks') {
        promptText = `You are an expert ESG consultant writing a BRSR report. Expand the following notes into a very short, professional remark (1 sentence) explaining the status of complaints for '${stakeholder}'. Notes: ${userInput}`;
    } else if (fieldType === 'rationale') {
        promptText = `You are an expert ESG consultant writing a BRSR report. Expand these notes into a professional rationale (1-2 sentences) explaining why '${issue}' is a material risk or opportunity for the business. Notes: ${userInput}`;
    } else if (fieldType === 'approach') {
        promptText = `You are an expert ESG consultant writing a BRSR report. Expand these notes into a professional strategy (1-2 sentences) detailing how the company is mitigating the risk or adapting to the opportunity presented by '${issue}'. Notes: ${userInput}`;
    }

    // 5. Call the API
    try {
        const response = await fetch("http://localhost:8080/api/ai/generate-esg", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            body: JSON.stringify({ prompt: promptText })
        });

        const data = await response.json();
        inputField.value = data.text;
    } catch (err) {
        console.error("AI Error:", err);
        alert("Could not reach AI service.");
    } finally {
        // Restore UI
        inputField.disabled = false;
        inputField.placeholder = originalPlaceholder;
    }
}

// --- GENERATE AI NOTE FROM USER INPUT ---
async function generateNoteFromInput(fieldId, sectionContext) {
    const textarea = document.getElementById(fieldId);
    const userInput = textarea.value.trim();

    // Check if the user actually typed something
    if (!userInput || userInput.length < 3) {
        alert("Please type a few keywords first (e.g., 'provides health insurance' or 'wheelchair accessible').");
        return;
    }

    // UI Feedback
    textarea.disabled = true;
    const originalPlaceholder = textarea.placeholder;
    textarea.placeholder = "Gemini is writing...";

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

        const data = await response.json();
        textarea.value = data.text;
    } catch (err) {
        console.error("AI Error:", err);
        alert("Could not reach AI service.");
    } finally {
        // Restore UI
        textarea.disabled = false;
        textarea.placeholder = originalPlaceholder;
    }
}

// --- UNIVERSAL AI TABLE SUMMARIZER ---
async function generateNoteFromTable(tableId, noteId, sectionContext) {
    const table = document.getElementById(tableId);
    const textarea = document.getElementById(noteId);

    if (!table) {
        alert("Could not find the data table.");
        return;
    }

    // 1. Scrape the table data dynamically
    let tableData = "";
    const rows = table.querySelectorAll('tr');

    rows.forEach(row => {
        let rowData = [];
        const cells = row.querySelectorAll('th, td');

        cells.forEach(cell => {
            // Check if there is an input field inside the cell
            const input = cell.querySelector('input, select');
            if (input) {
                rowData.push(input.value || "0");
            } else {
                // Otherwise, just get the text (like headers or row labels)
                rowData.push(cell.innerText.trim().replace(/\n/g, ' '));
            }
        });

        if (rowData.length > 0) {
            tableData += rowData.join(" | ") + "\n";
        }
    });

    // 2. Build the prompt
    const promptText = `You are an expert ESG consultant writing a BRSR report.
    Analyze the following data table regarding '${sectionContext}'.
    Write a concise, professional summary note (1 to 2 sentences) highlighting the key insights.
    Do not just list the numbers; provide a corporate narrative.

    Table Data:
    ${tableData}`;

    // 3. Call the API
    textarea.placeholder = "Gemini is analyzing the table data...";
    textarea.disabled = true;

    try {
        const response = await fetch("http://localhost:8080/api/ai/generate-esg", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            body: JSON.stringify({ prompt: promptText })
        });

        const data = await response.json();
        textarea.value = data.text;
    } catch (err) {
        console.error("AI Error:", err);
        alert("Could not reach AI service.");
    } finally {
        textarea.disabled = false;
    }
}

// --- MAIN GENERATE FUNCTION ---
function saveAndNext() {
    const token = localStorage.getItem("token");

    if (!token) {
        window.location.replace("login.html");
        return;
    }

    // --- LOGIC: Check which mode is selected for Section V ---
    const isHoldingTableMode = document.querySelector('input[name="holdingMode"][value="table"]').checked;

    // Prepare variables based on selection
    let finalHoldingList = [];
    let finalHoldingNote = "";

    if (isHoldingTableMode) {
        // Mode A: Table -> Scrape the rows
        finalHoldingList = Array.from(document.querySelectorAll("#holdingTable tbody tr")).map(row => ({
            name: row.querySelector(".hc-name").value,
            type: row.querySelector(".hc-type").value,
            sharesHeld: row.querySelector(".hc-share").value,
            participateBusinessResponsibility: row.querySelector(".hc-part").value
        }));
    } else {
        // Mode B: Note -> Get text, keep list empty/null
        finalHoldingNote = document.getElementById("holdingCompanyNote") ? document.getElementById("holdingCompanyNote").value : "";
    }
    // ---------------------------------------------------------

    // 1. Gather Data
    const formData = {
        id: currentReportId,

        // --- SECTION A ---
        cin: document.getElementById("cin").value,
        companyName: document.getElementById("companyName").value,
        yearOfInc: document.getElementById("yearOfInc").value,
        registeredAddress: document.getElementById("registeredAddress").value,
        corporateAddress: document.getElementById("corporateAddress").value,
        email: document.getElementById("email").value,
        telephone: document.getElementById("telephone").value,
        website: document.getElementById("website").value,
        reportingYear: document.getElementById("reportingYear").value,
        stockExchanges: document.getElementById("stockExchanges").value,
        paidUpCapital: document.getElementById("paidUpCapital").value,
        reportingBoundary: document.getElementById("reportingBoundary").value,
        assuranceProviderName: document.getElementById("assuranceProviderName").value,
        assuranceType: document.getElementById("assuranceType").value,

        // Contact Person
        contactPersonName: document.getElementById("contactPersonName").value,
        contactPersonDesignation: document.getElementById("contactPersonDesignation").value,
        contactPersonAddress: document.getElementById("contactPersonAddress").value,
        contactPersonEmail: document.getElementById("contactPersonEmail").value,
        contactPersonTelephone: document.getElementById("contactPersonTelephone").value,

        // --- SECTION III ---
        plantsNational: document.getElementById("plantsNational").value,
        officesNational: document.getElementById("officesNational").value,
        totalNational: document.getElementById("totalNational").value,
        plantsInternational: document.getElementById("plantsInternational").value,
        officesInternational: document.getElementById("officesInternational").value,
        totalInternational: document.getElementById("totalInternational").value,
        locationsNationalNumber: document.getElementById("locationsNationalNumber").value,
        locationsInternationalNumber: document.getElementById("locationsInternationalNumber").value,
        contributionExports: document.getElementById("contributionExports").value,
        typesOfCustomers: document.getElementById("typesOfCustomers").value,

        // --- SECTION IV: EMPLOYEES (18.a) ---
        empPermTotal: document.getElementById("empPermTotal").value,
        empPermMaleNo: document.getElementById("empPermMaleNo").value,
        empPermMalePerc: document.getElementById("empPermMalePerc").value,
        empPermFemaleNo: document.getElementById("empPermFemaleNo").value,
        empPermFemalePerc: document.getElementById("empPermFemalePerc").value,
        empPermOtherNo: "0",
        empPermOtherPerc: "0",

        empTempTotal: document.getElementById("empTempTotal").value,
        empTempMaleNo: document.getElementById("empTempMaleNo").value,
        empTempMalePerc: document.getElementById("empTempMalePerc").value,
        empTempFemaleNo: document.getElementById("empTempFemaleNo").value,
        empTempFemalePerc: document.getElementById("empTempFemalePerc").value,
        empTempOtherNo: "0",
        empTempOtherPerc: "0",

        workPermTotal: document.getElementById("workPermTotal").value,
        workPermMaleNo: document.getElementById("workPermMaleNo").value,
        workPermMalePerc: document.getElementById("workPermMalePerc").value,
        workPermFemaleNo: document.getElementById("workPermFemaleNo").value,
        workPermFemalePerc: document.getElementById("workPermFemalePerc").value,
        workPermOtherNo: "0",
        workPermOtherPerc: "0",

        workTempTotal: document.getElementById("workTempTotal").value,
        workTempMaleNo: document.getElementById("workTempMaleNo").value,
        workTempMalePerc: document.getElementById("workTempMalePerc").value,
        workTempFemaleNo: document.getElementById("workTempFemaleNo").value,
        workTempFemalePerc: document.getElementById("workTempFemalePerc").value,
        workTempOtherNo: "0",
        workTempOtherPerc: "0",

        // --- SECTION IV: DIFFERENTLY ABLED (18.b) ---
        daEmpPermTotal: document.getElementById("daEmpPermTotal").value,
        daEmpPermMaleNo: document.getElementById("daEmpPermMaleNo").value,
        daEmpPermMalePerc: document.getElementById("daEmpPermMalePerc").value,
        daEmpPermFemaleNo: document.getElementById("daEmpPermFemaleNo").value,
        daEmpPermFemalePerc: document.getElementById("daEmpPermFemalePerc").value,
        daEmpPermOtherNo: "0",
        daEmpPermOtherPerc: "0",

        daEmpTempTotal: document.getElementById("daEmpTempTotal").value,
        daEmpTempMaleNo: document.getElementById("daEmpTempMaleNo").value,
        daEmpTempMalePerc: document.getElementById("daEmpTempMalePerc").value,
        daEmpTempFemaleNo: document.getElementById("daEmpTempFemaleNo").value,
        daEmpTempFemalePerc: document.getElementById("daEmpTempFemalePerc").value,
        daEmpTempOtherNo: "0",
        daEmpTempOtherPerc: "0",

        daWorkPermTotal: document.getElementById("daWorkPermTotal").value,
        daWorkPermMaleNo: document.getElementById("daWorkPermMaleNo").value,
        daWorkPermMalePerc: document.getElementById("daWorkPermMalePerc").value,
        daWorkPermFemaleNo: document.getElementById("daWorkPermFemaleNo").value,
        daWorkPermFemalePerc: document.getElementById("daWorkPermFemalePerc").value,
        daWorkPermOtherNo: "0",
        daWorkPermOtherPerc: "0",

        daWorkTempTotal: document.getElementById("daWorkTempTotal").value,
        daWorkTempMaleNo: document.getElementById("daWorkTempMaleNo").value,
        daWorkTempMalePerc: document.getElementById("daWorkTempMalePerc").value,
        daWorkTempFemaleNo: document.getElementById("daWorkTempFemaleNo").value,
        daWorkTempFemalePerc: document.getElementById("daWorkTempFemalePerc").value,
        daWorkTempOtherNo: "0",
        daWorkTempOtherPerc: "0",

        employeeNotesA: document.getElementById("employeeNotesA").value,
        employeeNotesB: document.getElementById("employeeNotesB").value,

        // --- Q19 ---
        womenRepresentationList: Array.from(document.querySelectorAll("#womenRepTable tbody tr")).map(row => ({
            category: row.querySelector(".wr-cat").value,
            totalA: row.querySelector(".wr-tot").value,
            femaleNoB: row.querySelector(".wr-fem").value,
            femalePerc: row.querySelector(".wr-perc").value
        })),
        womenRepresentationNotes: document.getElementById("womenRepresentationNotes").value,

        // --- Q20 MAPPING ---
        turnoverEmpCurrMale: document.getElementById("turnoverEmpCurrMale").value,
        turnoverEmpCurrFemale: document.getElementById("turnoverEmpCurrFemale").value,
        turnoverEmpCurrTotal: document.getElementById("turnoverEmpCurrTotal").value,
        turnoverWorkCurrMale: document.getElementById("turnoverWorkCurrMale").value,
        turnoverWorkCurrFemale: document.getElementById("turnoverWorkCurrFemale").value,
        turnoverWorkCurrTotal: document.getElementById("turnoverWorkCurrTotal").value,
        turnoverEmpPrevMale: document.getElementById("turnoverEmpPrevMale").value,
        turnoverEmpPrevFemale: document.getElementById("turnoverEmpPrevFemale").value,
        turnoverEmpPrevTotal: document.getElementById("turnoverEmpPrevTotal").value,
        turnoverWorkPrevMale: document.getElementById("turnoverWorkPrevMale").value,
        turnoverWorkPrevFemale: document.getElementById("turnoverWorkPrevFemale").value,
        turnoverWorkPrevTotal: document.getElementById("turnoverWorkPrevTotal").value,
        turnoverEmpPriorMale: document.getElementById("turnoverEmpPriorMale").value,
        turnoverEmpPriorFemale: document.getElementById("turnoverEmpPriorFemale").value,
        turnoverEmpPriorTotal: document.getElementById("turnoverEmpPriorTotal").value,
        turnoverWorkPriorMale: document.getElementById("turnoverWorkPriorMale").value,
        turnoverWorkPriorFemale: document.getElementById("turnoverWorkPriorFemale").value,
        turnoverWorkPriorTotal: document.getElementById("turnoverWorkPriorTotal").value,
        turnoverNotes: document.getElementById("turnoverNotes").value,

        fyCurrent: document.getElementById("fyCurrent").value,
        fyPrevious: document.getElementById("fyPrevious").value,
        fyPrior: document.getElementById("fyPrior").value,

        // --- SECTION V (Corrected) ---
        holdingCompanies: finalHoldingList, // USE THE VARIABLE WE CALCULATED AT TOP
        holdingCompanyNote: finalHoldingNote, // USE THE VARIABLE WE CALCULATED AT TOP

        // --- SECTION VI ---
        csrApplicable: document.getElementById("csrApplicable").value,
        csrTurnover: document.getElementById("csrTurnover").value,
        csrNetWorth: document.getElementById("csrNetWorth").value,

        // --- SECTION VII ---
        complaintsFyCurrentHeader: document.getElementById("complaintsFyCurrentHeader").value,
        complaintsFyPreviousHeader: document.getElementById("complaintsFyPreviousHeader").value,
        complaintsList: Array.from(document.querySelectorAll("#complaintsTable tbody tr")).map(row => ({
            stakeholder: row.querySelector(".c-stake").value,
            mechanism: row.querySelector(".c-mech").value,
            currFiled: row.querySelector(".c-cur-f").value,
            currPending: row.querySelector(".c-cur-p").value,
            currRemarks: row.querySelector(".c-cur-r").value,
            prevFiled: row.querySelector(".c-pre-f").value,
            prevPending: row.querySelector(".c-pre-p").value,
            prevRemarks: row.querySelector(".c-pre-r").value
        })),

        // --- SECTION 24 ---
        materialIssuesNote: document.getElementById("materialIssuesNote").value,
        materialIssues: Array.from(document.querySelectorAll("#materialIssuesTable tbody tr")).map(row => ({
            description: row.querySelector(".mi-desc").value,
            riskOrOpportunity: row.querySelector(".mi-ro").value,
            rationale: row.querySelector(".mi-rat").value,
            approach: row.querySelector(".mi-app").value,
            financialImplications: row.querySelector(".mi-fin").value
        })),

        // --- SECTION II ---
        businessActivities: Array.from(document.querySelectorAll("#activitiesTable tbody tr")).map(row => ({
            descriptionMain: row.querySelector(".act-main") ? row.querySelector(".act-main").value : "",
            descriptionBusiness: row.querySelector(".act-bus") ? row.querySelector(".act-bus").value : "",
            turnoverPercentage: row.querySelector(".act-turn") ? row.querySelector(".act-turn").value : ""
        })),

        includeConsolidated: document.getElementById("includeConsolidated").checked,
        productsServices: Array.from(document.querySelectorAll("#productsTable tbody tr")).map(row => ({
            productName: row.querySelector(".prod-name") ? row.querySelector(".prod-name").value : "",
            nicCode: row.querySelector(".prod-nic") ? row.querySelector(".prod-nic").value : "",
            turnoverStandalone: row.querySelector(".prod-turn-s") ? row.querySelector(".prod-turn-s").value : "",
            percentageStandalone: row.querySelector(".prod-perc-s") ? row.querySelector(".prod-perc-s").value : "",
            turnoverConsolidated: row.querySelector(".prod-turn-c") ? row.querySelector(".prod-turn-c").value : "",
            percentageConsolidated: row.querySelector(".prod-perc-c") ? row.querySelector(".prod-perc-c").value : ""
        }))
    };
    // 2. Call the NEW /save endpoint
    fetch("http://localhost:8080/api/report/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(formData)
    })
        .then(res => res.json())
        .then(data => {
            if (data.status === "success" && data.reportId) {
                console.log("Report draft saved with ID:", data.reportId);

                // 3. Save the ID returned by the backend to localStorage
                localStorage.setItem("currentReportId", data.reportId);

                // 4. Redirect to Part 2
                window.location.href = "create_report_2.html";
            } else {
                throw new Error("Failed to save draft.");
            }
        })
        .catch(err => {
            console.error(err);
            alert("Error saving progress: " + err.message);
        });
}