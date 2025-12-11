// --- Security Check on Load ---
document.addEventListener("DOMContentLoaded", function() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Please login first.");
        window.location.replace("login.html");
    }
});

// --- Main Report Generation Function ---
function generateReport() {
    const token = localStorage.getItem("token");
    const currentUsername = localStorage.getItem("currentUsername");

    if (!token) {
        window.location.replace("login.html");
        return;
    }

    // 1. Gather all data from the form
    const formData = {
        // Section A
        companyName: document.getElementById("companyName").value,
        cin: document.getElementById("cin").value,
        yearOfInc: document.getElementById("yearOfInc").value,
        reportingYear: document.getElementById("reportingYear").value,
        paidUpCapital: document.getElementById("paidUpCapital").value,
        registeredAddress: document.getElementById("registeredAddress").value,
        corporateAddress: document.getElementById("corporateAddress").value,
        email: document.getElementById("email").value,
        telephone: document.getElementById("telephone").value,
        website: document.getElementById("website").value,

        // Employee Details
        empMalePermanent: document.getElementById("empMalePermanent").value,
        empFemalePermanent: document.getElementById("empFemalePermanent").value,
        empTotalPermanent: document.getElementById("empTotalPermanent").value,
        workerMalePermanent: document.getElementById("workerMalePermanent").value,
        workerFemalePermanent: document.getElementById("workerFemalePermanent").value,
        workerTotalPermanent: document.getElementById("workerTotalPermanent").value,

        // Section C (Environment)
        electricityConsumption: document.getElementById("electricityConsumption").value,
        fuelConsumption: document.getElementById("fuelConsumption").value,
        energyIntensity: document.getElementById("energyIntensity").value,

        scope1Emissions: document.getElementById("scope1Emissions").value,
        scope2Emissions: document.getElementById("scope2Emissions").value,
        emissionIntensity: document.getElementById("emissionIntensity").value,

        plasticWaste: document.getElementById("plasticWaste").value,
        eWaste: document.getElementById("eWaste").value,
        hazardousWaste: document.getElementById("hazardousWaste").value,
        totalWasteGenerated: document.getElementById("totalWasteGenerated").value,
        totalWasteRecycled: document.getElementById("totalWasteRecycled").value
    };

    // 2. Call the Backend API
    fetch("http://localhost:8080/api/report/generate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(formData)
    })
        .then(res => {
            if(res.ok) return res.blob(); // Expecting a Binary File (Blob)
            throw new Error("Generation Failed (Server Error)");
        })
        .then(blob => {
            // 3. Trigger Download
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "BRSR_Report_" + formData.companyName + ".docx";
            document.body.appendChild(a);
            a.click();
            a.remove();

            // 4. Mark Report as Done in Backend
            updateStatus(currentUsername, token);
        })
        .catch(err => {
            console.error(err);
            alert("Error generating report: " + err.message);
        });
}

// --- Helper to Update User Status ---
function updateStatus(username, token) {
    fetch("http://localhost:8080/api/report/complete", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ username: username })
    }).then(() => {
        // Wait 1 second then redirect to Dashboard
        setTimeout(() => {
            alert("Report generated successfully! Redirecting to Dashboard...");
            window.location.href = "industry_homepage.html";
        }, 1000);
    }).catch(err => {
        console.error("Failed to update status", err);
        // Redirect anyway if the download worked
        window.location.href = "industry_homepage.html";
    });
}