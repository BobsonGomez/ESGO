const currentReportId = localStorage.getItem("currentReportId");

document.addEventListener("DOMContentLoaded", function() {
    if (!currentReportId) {
        alert("No report active.");
        window.location.href = "create_report.html";
        return;
    }
    // Initialize Q12 Table (Fixed Rows)
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

function fetchData() {
    const token = localStorage.getItem("token");
    fetch(`http://localhost:8080/api/report/${currentReportId}`, {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(data => {
        // Text Areas
        if(data.q2Procedures) document.getElementById("q2").value = data.q2Procedures;
        if(data.q3ValueChain) document.getElementById("q3").value = data.q3ValueChain;
        if(data.q5Commitments) document.getElementById("q5").value = data.q5Commitments;
        if(data.q6Performance) document.getElementById("q6").value = data.q6Performance;
        if(data.governanceStatement) document.getElementById("q7").value = data.governanceStatement;
        if(data.oversightAuthority) document.getElementById("q8").value = data.oversightAuthority;

        // Q1 Table
        const tb1 = document.querySelector("#q1Table tbody");
        tb1.innerHTML = "";
        if (data.q1Policies && data.q1Policies.length > 0) {
            data.q1Policies.forEach(p => addQ1Row(p));
        } else {
            addQ1Row({name: "Ethics & Anti-Bribery Policy"}); // Default
        }

        // Q4 Table
        const tb4 = document.querySelector("#q4Table tbody");
        tb4.innerHTML = "";
        if (data.q4Standards && data.q4Standards.length > 0) {
            data.q4Standards.forEach(s => addQ4Row(s));
        } else {
            addQ4Row({name: "ISO 14001 Environmental Management"}); // Default
        }

         // NEW FIELDS
                if(data.q9Committee) document.getElementById("q9").value = data.q9Committee;
                if(data.q10PerformanceReview) document.getElementById("q10_1").value = data.q10PerformanceReview;
                if(data.q10ComplianceReview) document.getElementById("q10_2").value = data.q10ComplianceReview;
                if(data.q11Assessment) document.getElementById("q11").value = data.q11Assessment;

                // Q12 Matrix (Checkbox mapping)
                if(data.q12Reasons && data.q12Reasons.length > 0) {
                    const rows = document.querySelectorAll("#q12Table tbody tr");
                    data.q12Reasons.forEach((item, idx) => {
                        if(rows[idx]) {
                            for(let i=1; i<=9; i++) {
                                 rows[idx].querySelector(`.q12-p${i}`).checked = item["p"+i];
                            }
                        }
                    });
                }
    });
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

function saveAndNext() {
    const token = localStorage.getItem("token");

    // Scrape Q1
    const q1Policies = Array.from(document.querySelectorAll("#q1Table tbody tr")).map(row => ({
        name: row.querySelector(".q1-name").value,
        boardApproved: row.querySelector(".q1-app").value,
        webLink: row.querySelector(".q1-link").value,
        p1: row.querySelector(".q1-p1").checked,
        p2: row.querySelector(".q1-p2").checked,
        p3: row.querySelector(".q1-p3").checked,
        p4: row.querySelector(".q1-p4").checked,
        p5: row.querySelector(".q1-p5").checked,
        p6: row.querySelector(".q1-p6").checked,
        p7: row.querySelector(".q1-p7").checked,
        p8: row.querySelector(".q1-p8").checked,
        p9: row.querySelector(".q1-p9").checked
    }));

    // Scrape Q4
    const q4Standards = Array.from(document.querySelectorAll("#q4Table tbody tr")).map(row => ({
        name: row.querySelector(".q4-name").value,
        p1: row.querySelector(".q4-p1").checked,
        p2: row.querySelector(".q4-p2").checked,
        p3: row.querySelector(".q4-p3").checked,
        p4: row.querySelector(".q4-p4").checked,
        p5: row.querySelector(".q4-p5").checked,
        p6: row.querySelector(".q4-p6").checked,
        p7: row.querySelector(".q4-p7").checked,
        p8: row.querySelector(".q4-p8").checked,
        p9: row.querySelector(".q4-p9").checked
    }));

     // Scrape Q12
        const q12Reasons = Array.from(document.querySelectorAll("#q12Table tbody tr")).map(row => ({
            questionText: row.querySelector(".q12-text").innerText,
            p1: row.querySelector(".q12-p1").checked,
            p2: row.querySelector(".q12-p2").checked,
            p3: row.querySelector(".q12-p3").checked,
            p4: row.querySelector(".q12-p4").checked,
            p5: row.querySelector(".q12-p5").checked,
            p6: row.querySelector(".q12-p6").checked,
            p7: row.querySelector(".q12-p7").checked,
            p8: row.querySelector(".q12-p8").checked,
            p9: row.querySelector(".q12-p9").checked
        }));

    const formData = {
        id: parseInt(currentReportId),
        q1Policies: q1Policies,
        q2Procedures: document.getElementById("q2").value,
        q3ValueChain: document.getElementById("q3").value,
        q4Standards: q4Standards,
        q5Commitments: document.getElementById("q5").value,
        q6Performance: document.getElementById("q6").value,
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
        if(data.status === "success") window.location.href = "create_report_3.html";
        else alert("Save failed");
    });
}