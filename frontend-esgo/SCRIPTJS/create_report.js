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
                console.log("Data received from backend:", data); // Debugging

                // 1. Fill Standard Fields (Section A + Section 3)
                fillStandardFields(data);

                // 2. Fill Section 2 Tables (Business Activities)
                const actBody = document.querySelector("#activitiesTable tbody");
                actBody.innerHTML = ""; // Clear default empty rows first
                if (data.businessActivities && data.businessActivities.length > 0) {
                    data.businessActivities.forEach(act => addActivityRow(act));
                } else {
                    addActivityRow(); // Add 1 empty row if list is empty
                }

                // 3. Fill Section 2 Tables (Products)
                const prodBody = document.querySelector("#productsTable tbody");
                prodBody.innerHTML = ""; // Clear default empty rows first
                if (data.productsServices && data.productsServices.length > 0) {
                    // Set checkbox state
                    document.getElementById("includeConsolidated").checked = data.includeConsolidated;
                    toggleConsolidatedColumns(); // Refresh column visibility

                    data.productsServices.forEach(prod => addProductRow(prod));
                } else {
                    addProductRow(); // Add 1 empty row if list is empty
                }
            })
            .catch(err => console.error("Failed to load report data", err));

    } else {
        // === NEW REPORT MODE ===
        // Just add one empty row for each table to start
        addActivityRow();
        addProductRow();
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
        "plantsInternational", "officesInternational", "totalInternational"
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
function calcOpsTotal() {
    // National
    const pNat = parseInt(document.getElementById("plantsNational").value) || 0;
    const oNat = parseInt(document.getElementById("officesNational").value) || 0;
    document.getElementById("totalNational").value = pNat + oNat;

    // International
    const pInt = parseInt(document.getElementById("plantsInternational").value) || 0;
    const oInt = parseInt(document.getElementById("officesInternational").value) || 0;
    document.getElementById("totalInternational").value = pInt + oInt;
}

// --- MAIN GENERATE FUNCTION ---
function generateReport() {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.replace("login.html");
        return;
    }

    // 1. Gather Data (Section A & 3)
    const formData = {
        id: currentReportId,

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
        contactPersonName: document.getElementById("contactPersonName").value,
        contactPersonDesignation: document.getElementById("contactPersonDesignation").value,
        contactPersonAddress: document.getElementById("contactPersonAddress").value,
        contactPersonEmail: document.getElementById("contactPersonEmail").value,
        contactPersonTelephone: document.getElementById("contactPersonTelephone").value,

        // Section 3 Fields
        plantsNational: document.getElementById("plantsNational").value,
        officesNational: document.getElementById("officesNational").value,
        totalNational: document.getElementById("totalNational").value,
        plantsInternational: document.getElementById("plantsInternational").value,
        officesInternational: document.getElementById("officesInternational").value,
        totalInternational: document.getElementById("totalInternational").value,

        // 2. Gather Data (Section II - Lists)
        businessActivities: Array.from(document.querySelectorAll("#activitiesTable tbody tr")).map(row => ({
            descriptionMain: row.querySelector(".act-main").value,
            descriptionBusiness: row.querySelector(".act-bus").value,
            turnoverPercentage: row.querySelector(".act-turn").value
        })),

        includeConsolidated: document.getElementById("includeConsolidated").checked,

        productsServices: Array.from(document.querySelectorAll("#productsTable tbody tr")).map(row => ({
            productName: row.querySelector(".prod-name").value,
            nicCode: row.querySelector(".prod-nic").value,
            turnoverStandalone: row.querySelector(".prod-turn-s").value,
            percentageStandalone: row.querySelector(".prod-perc-s").value,
            turnoverConsolidated: row.querySelector(".prod-turn-c").value,
            percentageConsolidated: row.querySelector(".prod-perc-c").value
        }))
    };

    // 3. Send to Backend
    fetch("http://localhost:8080/api/report/generate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(formData)
    })
        .then(res => {
            if(res.ok) return res.blob();
            throw new Error("Generation Failed (Server Error)");
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