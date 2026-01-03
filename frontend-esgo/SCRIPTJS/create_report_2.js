const currentReportId = localStorage.getItem("currentReportId"); // Shared ID

document.addEventListener("DOMContentLoaded", function() {
    if (!currentReportId) {
        alert("No report started. Redirecting to Part 1.");
        window.location.href = "create_report.html";
        return;
    }

    // --- CHECK FOR EDIT MODE ---
    // If we are editing an existing report, we should fetch its Section B data to pre-fill the form!
    // (This part was missing in your code, which is why the form is blank on load)
    fetchDataForPart2();
});

function fetchDataForPart2() {
    const token = localStorage.getItem("token");
    fetch(`http://localhost:8080/api/report/${currentReportId}`, {
        method: "GET",
        headers: { "Authorization": "Bearer " + token }
    })
        .then(res => res.json())
        .then(data => {
            // Pre-fill Governance
            if(document.getElementById("governanceStatement"))
                document.getElementById("governanceStatement").value = data.governanceStatement || "";
            if(document.getElementById("oversightAuthority"))
                document.getElementById("oversightAuthority").value = data.oversightAuthority || "";

            // Pre-fill Matrix
            const tbody = document.querySelector("#policyTable tbody");
            tbody.innerHTML = "";

            if (data.policyMappings && data.policyMappings.length > 0) {
                data.policyMappings.forEach(pol => addPolicyRow(pol.policyName, pol));
            } else {
                // Default rows if empty
                addPolicyRow("Ethics & Anti-Bribery Policy");
                addPolicyRow("Employee Well-being Policy");
                addPolicyRow("Environment Policy");
                addPolicyRow("Human Rights Policy");
            }
        })
        .catch(err => console.error("Failed to load Part 2 data", err));
}

function addPolicyRow(name = "", data = null) {
    const tbody = document.querySelector("#policyTable tbody");

    // Helper to check the box if data is true
    const check = (idx) => (data && data["p"+idx]) ? "checked" : "";

    const row = `
        <tr>
            <td><input type="text" class="policy-input pol-name" value="${name}" placeholder="Enter Policy Name"></td>
            ${[1,2,3,4,5,6,7,8,9].map(i => `<td><input type="checkbox" class="p${i}" ${check(i)}></td>`).join('')}
            <td class="btn-remove" onclick="this.parentElement.remove()">X</td>
        </tr>
    `;
    tbody.insertAdjacentHTML('beforeend', row);
}

function saveAndNext() {
    const token = localStorage.getItem("token");

    // 1. Scrape the Matrix
    const policyList = Array.from(document.querySelectorAll("#policyTable tbody tr")).map(row => ({
        policyName: row.querySelector(".pol-name").value,
        p1: row.querySelector(".p1").checked,
        p2: row.querySelector(".p2").checked,
        p3: row.querySelector(".p3").checked,
        p4: row.querySelector(".p4").checked,
        p5: row.querySelector(".p5").checked,
        p6: row.querySelector(".p6").checked,
        p7: row.querySelector(".p7").checked,
        p8: row.querySelector(".p8").checked,
        p9: row.querySelector(".p9").checked
    }));

    const formData = {
        id: parseInt(currentReportId),
        policyMappings: policyList,
        governanceStatement: document.getElementById("governanceStatement").value,
        oversightAuthority: document.getElementById("oversightAuthority").value
    };

    // 2. Call SAVE (Not Generate)
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
            if(data.status === "success") {
                // Move to Part 3
                window.location.href = "create_report_3.html";
            } else {
                alert("Save failed");
            }
        })
        .catch(err => alert("Error: " + err.message));
}