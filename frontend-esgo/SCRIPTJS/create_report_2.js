const currentReportId = localStorage.getItem("currentReportId");

document.addEventListener("DOMContentLoaded", function() {
    if (!currentReportId) {
        alert("No report active.");
        window.location.href = "create_report.html";
        return;
    }
    // 1. Initialize the Y/N checkbox matrix for standard rows
        initUnifiedRow("row-1a", "q1a");
        initUnifiedRow("row-1b", "q1b");
        initUnifiedRow("row-2", "q2");
        initUnifiedRow("row-3", "q3");

        // 2. Initialize Q12 Matrix (Keep your existing Q12 logic)
        initQ12Table();
    fetchData();
});

// --- NEW: INIT Q12 TABLE ---
function initQ12Table() {
    const questions = [
        "The entity does not consider the Principles material to its business (Yes/No)",
        "The entity is not at a stage where it is in a position to formulate and implement the policies (Yes/No)",
        "The entity does not have the financial or/human and technical resources available (Yes/No)",
        "It is planned to be done in the next financial year (Yes/No)",
        "Any other reason (please specify)"
    ];

    const tbody = document.querySelector("#q12Table tbody");
    if(tbody.children.length === 0) { // Only init if empty
        questions.forEach((text, index) => {
            let row = `<tr><td class="q-col q12-text">${text}</td>`;
            for(let i=1; i<=9; i++) {
                row += `<td><input type="checkbox" class="q12-p${i}"></td>`;
            }
            row += `</tr>`;
            tbody.insertAdjacentHTML('beforeend', row);
        });
    }
}

// Helper to inject 9 checkboxes into a specific row
function initUnifiedRow(rowId, prefix) {
    const tr = document.getElementById(rowId);
    let cells = '';
    for(let i=1; i<=9; i++) {
        cells += `<td><input type="checkbox" class="${prefix}-p${i}"></td>`;
    }
    tr.insertAdjacentHTML('beforeend', cells);
}

// Q4: Dynamic ISO Row Adder
function addUnifiedISO(data = {}) {
    const name = data.name || "";
    const tbody = document.querySelector("#unifiedMatrixTable tbody");
    const check = (idx) => (data["p"+idx]) ? "checked" : "";

    // Find where to insert (right before row 5)
    const q5Row = document.getElementById("q5Unified").closest("tr");

    let rowHTML = `<tr class="iso-row">
        <td>
            <div style="display: flex; align-items: center; gap: 10px;">
                <span onclick="this.closest('tr').remove()" style="color:red; cursor:pointer; font-weight:bold;">X</span>
                <input type="text" class="text-input iso-name" value="${name}" placeholder="e.g. ISO 9001:2015" style="text-align: left;">
            </div>
        </td>`;

    for(let i=1; i<=9; i++) {
        rowHTML += `<td><input type="checkbox" class="iso-p${i}" ${check(i)}></td>`;
    }
    rowHTML += `</tr>`;

    q5Row.insertAdjacentHTML('beforebegin', rowHTML);
}

// --- DATA FETCHING (LOADING) ---
function fetchData() {
    const token = localStorage.getItem("token");
    fetch(`http://localhost:8080/api/report/${currentReportId}`, {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(data => {
        console.log("Loaded Section B Data:", data);

        // Helper to check the boxes for the matrix rows
        const setRowBooleans = (prefix, dataObj) => {
            if (!dataObj) return;
            for(let i=1; i<=9; i++) {
                const cb = document.querySelector(`.${prefix}-p${i}`);
                if(cb) cb.checked = dataObj["p"+i];
            }
        };

        // 1. Matrix Checkboxes (Q1a, 1b, 2, 3)
        setRowBooleans("q1a", data.q1a);
        setRowBooleans("q1b", data.q1b);
        setRowBooleans("q2", data.q2);
        setRowBooleans("q3", data.q3);

        // 2. Text fields & Links (Unified Matrix)
        if(data.q1WebLink) document.getElementById("unifiedWebLink").value = data.q1WebLink;
        if(data.q5Commitments) document.getElementById("q5Unified").value = data.q5Commitments;
        if(data.q6Performance) document.getElementById("q6Unified").value = data.q6Performance;

        // 3. ISO Rows (Q4)
        document.querySelectorAll(".iso-row").forEach(r => r.remove()); // Clear defaults
        if (data.q4Standards && data.q4Standards.length > 0) {
            data.q4Standards.forEach(s => addUnifiedISO(s));
        } else {
            addUnifiedISO({name: "ISO 9001: 2015"}); // Default starting row
        }

        // 4. Governance & Oversight (Q7, 8, 9, 10, 11) - FIXED!
        if(data.governanceStatement) document.getElementById("q7").value = data.governanceStatement;
        if(data.oversightAuthority) document.getElementById("q8").value = data.oversightAuthority;
        if(data.q9Committee) document.getElementById("q9").value = data.q9Committee;
        if(data.q10PerformanceReview) document.getElementById("q10_1").value = data.q10PerformanceReview;
        if(data.q10ComplianceReview) document.getElementById("q10_2").value = data.q10ComplianceReview;
        if(data.q11Assessment) document.getElementById("q11").value = data.q11Assessment;

        // 5. Q12 Matrix
        if(data.q12Reasons && data.q12Reasons.length > 0) {
            const rows = document.querySelectorAll("#q12Table tbody tr");
            data.q12Reasons.forEach((item, idx) => {
                if(rows[idx]) {
                    for(let i=1; i<=9; i++) {
                        const cb = rows[idx].querySelector(`.q12-p${i}`);
                        if (cb) cb.checked = item["p"+i];
                    }
                }
            });
        }
    })
    .catch(err => console.error("Error fetching data:", err));
}

// --- Q1 ROW BUILDER ---
function addQ1Row(data = {}) {
    const tbody = document.querySelector("#q1Table tbody");
    const check = (idx) => (data["p"+idx]) ? "checked" : "";
    const app = data.boardApproved || "Yes";
    const link = data.webLink || "";
    const name = data.name || "";

    const row = `
        <tr>
            <td><input type="text" class="text-input q1-name" value="${name}" placeholder="Policy Name"></td>
            ${[1,2,3,4,5,6,7,8,9].map(i => `<td><input type="checkbox" class="q1-p${i}" ${check(i)}></td>`).join('')}
            <td><select class="q1-app"><option ${app==='Yes'?'selected':''}>Yes</option><option ${app==='No'?'selected':''}>No</option></select></td>
            <td><input type="text" class="text-input q1-link" value="${link}" placeholder="URL"></td>
            <td onclick="this.parentElement.remove()" style="color:red; cursor:pointer;">X</td>
        </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- Q4 ROW BUILDER ---
function addQ4Row(data = {}) {
    const tbody = document.querySelector("#q4Table tbody");
    const check = (idx) => (data["p"+idx]) ? "checked" : "";
    const name = data.name || "";

    const row = `
        <tr>
            <td><input type="text" class="text-input q4-name" value="${name}" placeholder="Standard Name"></td>
            ${[1,2,3,4,5,6,7,8,9].map(i => `<td><input type="checkbox" class="q4-p${i}" ${check(i)}></td>`).join('')}
            <td onclick="this.parentElement.remove()" style="color:red; cursor:pointer;">X</td>
        </tr>`;
    tbody.insertAdjacentHTML('beforeend', row);
}

// --- DATA SAVING ---
function saveAndNext() {
    const token = localStorage.getItem("token");

    // Helper to scrape P1-P9 for a given row prefix
    const getRowBooleans = (prefix) => {
        let obj = {};
        for(let i=1; i<=9; i++) {
            const cb = document.querySelector(`.${prefix}-p${i}`);
            obj["p"+i] = cb ? cb.checked : false;
        }
        return obj;
    };

    // Scrape ISO rows
    const q4Standards = Array.from(document.querySelectorAll(".iso-row")).map(row => {
        let obj = { name: row.querySelector(".iso-name").value };
        for(let i=1; i<=9; i++) {
            obj["p"+i] = row.querySelector(`.iso-p${i}`).checked;
        }
        return obj;
    });

    // Scrape Q12 rows
    const q12Reasons = Array.from(document.querySelectorAll("#q12Table tbody tr")).map(row => {
        let obj = { questionText: row.querySelector(".q12-text").innerText };
        for(let i=1; i<=9; i++) {
            obj["p"+i] = row.querySelector(`.q12-p${i}`).checked;
        }
        return obj;
    });

    // Build the complete payload
    const formData = {
        id: parseInt(currentReportId),

        // Matrix Rows
        q1a: getRowBooleans("q1a"),
        q1b: getRowBooleans("q1b"),
        q1WebLink: document.getElementById("unifiedWebLink").value,
        q2: getRowBooleans("q2"),
        q3: getRowBooleans("q3"),

        q4Standards: q4Standards,
        q5Commitments: document.getElementById("q5Unified").value,
        q6Performance: document.getElementById("q6Unified").value,

        // Governance fields
        governanceStatement: document.getElementById("q7").value,
        oversightAuthority: document.getElementById("q8").value,
        q9Committee: document.getElementById("q9").value,
        q10PerformanceReview: document.getElementById("q10_1").value,
        q10ComplianceReview: document.getElementById("q10_2").value,
        q11Assessment: document.getElementById("q11").value,

        q12Reasons: q12Reasons
    };

    fetch("http://localhost:8080/api/report/save", {
        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": "Bearer " + token },
        body: JSON.stringify(formData)
    })
    .then(res => res.json())
    .then(data => {
        if(data.status === "success") {
            console.log("Successfully saved!");
            window.location.href = "create_report_3.html";
        } else {
            alert("Save failed");
        }
    })
    .catch(err => console.error("Error saving data:", err));
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