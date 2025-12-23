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
        "contactPersonTelephone",

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
            
            <td><textarea class="mi-rat" rows="3" style="width:100%">${d.rationale || ''}</textarea></td>
            <td><textarea class="mi-app" rows="3" style="width:100%">${d.approach || ''}</textarea></td>
            
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

// --- MAIN GENERATE FUNCTION ---
function generateReport() {
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

    // 2. Send to Backend
    fetch("http://localhost:8080/api/report/generate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
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
            a.download = "BRSR_Report_" + (formData.companyName || "Draft") + ".docx";
            document.body.appendChild(a);
            a.click();
            a.remove();

            alert("Report Generated and Saved!");
            window.location.href = "industry_homepage.html";
        })
        .catch(err => {
            console.error(err);
            alert("Error generating report: " + err.message);
        });
}