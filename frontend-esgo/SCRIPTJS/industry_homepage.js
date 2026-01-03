document.addEventListener("DOMContentLoaded", function() {

    // --- 1. SECURITY CHECK ---
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("currentUsername"); // Or however you stored it

    if (!token) {
        window.location.replace("login.html");
        return;
    }

    // --- 2. FETCH USER DETAILS (For Welcome Message) ---
    // If you stored the fullname during login, you can use that.
    // Otherwise, fetch it again:
    const displayElement = document.getElementById("usernameDisplay");
    if (displayElement) {
        // Fallback to username if fullname isn't saved
        displayElement.innerText = localStorage.getItem("currentUser") || username || "User";
    }

    // --- 3. LOAD THE REPORTS ---
    fetchReportsList();
});

// --- MAIN FUNCTIONS ---

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

// 1. Fetch & Display Reports
function fetchReportsList() {
    const token = localStorage.getItem("token");
    const container = document.getElementById("reportListContainer");
    const countDisplay = document.getElementById("reportCountDisplay");

    fetch("http://localhost:8080/api/report/list", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) throw new Error("Failed to fetch reports");
        return response.json();
    })
    .then(reports => {
        // Update the big number on the dashboard
        if(countDisplay) {
            countDisplay.innerText = reports.length;
        }

        // Clear "Loading..." text
        container.innerHTML = "";

        if (reports.length === 0) {
            container.innerHTML = "<p style='color:#888; padding:20px; font-style:italic;'>No reports generated yet.</p>";
            return;
        }

        // Generate Cards
        reports.forEach(report => {
            // Format Date (assuming report.createdDate is a string or timestamp)
            const dateStr = new Date(report.createdDate).toLocaleDateString();

            const html = `
            <div class="card report-item" id="report-${report.id}">
                <div class="report-left">
                    <div class="file-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#16a34a" stroke-width="2">
                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                            <polyline points="14 2 14 8 20 8"></polyline>
                        </svg>
                    </div>
                    <div class="report-details">
                        <h4>${report.reportName || "Untitled Report"}</h4>
                        <p>${report.reportType || "BRSR"} • ${dateStr}</p>
                    </div>
                </div>

                <div class="action-buttons-group">
                    <!-- EDIT -->
                    <div class="icon-btn edit" title="Edit Report" onclick="window.location.href='create_report.html?edit=${report.id}'">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V8z"></path>
                            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                        </svg>
                    </div>

                    <!-- DOWNLOAD -->
                    <div class="icon-btn download" title="Download" onclick="downloadReport(${report.id})">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                            <polyline points="7 10 12 15 17 10"></polyline>
                            <line x1="12" y1="15" x2="12" y2="3"></line>
                        </svg>
                    </div>

                    <!-- DELETE -->
                    <div class="icon-btn delete" title="Delete" onclick="deleteReport(${report.id})">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <polyline points="3 6 5 6 21 6"></polyline>
                            <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                            <line x1="10" y1="11" x2="10" y2="17"></line>
                            <line x1="14" y1="11" x2="14" y2="17"></line>
                        </svg>
                    </div>
                </div>
            </div>
            `;
            container.innerHTML += html;
        });
    })
    .catch(err => {
        console.error("Error loading reports:", err);
        container.innerHTML = "<p style='color:red; padding:20px;'>Error connecting to server.</p>";
    });
}

// 2. Download Report
function downloadReport(id) {
    const token = localStorage.getItem("token");
    // Note: This endpoint (/api/report/download/{id}) must exist in ReportController
    fetch(`http://localhost:8080/api/report/download/${id}`, {
        method: "GET",
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => {
        if(res.ok) return res.blob();
        throw new Error("Download failed");
    })
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = `ESG_Report_${id}.docx`;
        document.body.appendChild(a);
        a.click();
        a.remove();
    })
    .catch(err => alert("Error downloading: " + err));
}

// 3. Delete Report
function deleteReport(id) {
    if(!confirm("Are you sure you want to delete this report?")) return;

    const token = localStorage.getItem("token");
    fetch(`http://localhost:8080/api/report/${id}`, {
        method: "DELETE",
        headers: { "Authorization": "Bearer " + token }
    })
    .then(response => {
        if(response.ok) {
            // Remove from UI immediately
            const card = document.getElementById(`report-${id}`);
            if(card) card.remove();

            // Update counter
            const countDisplay = document.getElementById("reportCountDisplay");
            if(countDisplay) {
                let val = parseInt(countDisplay.innerText);
                countDisplay.innerText = Math.max(0, val - 1);
            }
        } else {
            alert("Failed to delete report.");
        }
    })
    .catch(err => console.error(err));
}


// --- PUBLISH / INVESTOR MODAL LOGIC (Preserved from your previous step) ---

const modal = document.getElementById("publishModal");

// OPEN MODAL
async function openPublishModal() {
    modal.style.display = "block";
    const token = localStorage.getItem("token");

    // Reset UI states
    document.getElementById("publishForm").reset();
    document.getElementById("modalTitle").innerText = "Publish Company Profile";
    document.getElementById("btnPublish").innerText = "Publish to Investors";
    document.getElementById("btnDeleteProfile").style.display = "none";
    document.getElementById("currentFileText").style.display = "none";

    // Fetch existing profile
    try {
        const response = await fetch('http://localhost:8080/api/investor/my-profile', {
            headers: { 'Authorization': 'Bearer ' + token }
        });

        if (response.status === 200) {
            const data = await response.json();

            // PRE-FILL FORM
            document.getElementById('compName').value = data.companyName;
            document.getElementById('compSector').value = data.sector;
            document.getElementById('compLocation').value = data.location;
            document.getElementById('scoreE').value = data.scoreE;
            document.getElementById('scoreS').value = data.scoreS;
            document.getElementById('scoreG').value = data.scoreG;

            // Show Edit UI
            document.getElementById("modalTitle").innerText = "Edit Company Profile";
            document.getElementById("btnPublish").innerText = "Update Profile";
            document.getElementById("btnDeleteProfile").style.display = "block";

            if(data.reportFileName) {
                document.getElementById("currentFileText").style.display = "block";
                document.getElementById("fileNameDisplay").innerText = data.reportFileName;
            }
        }
    } catch (error) {
        console.log("No existing profile found or error fetching it.");
    }
}

function closePublishModal() {
    modal.style.display = "none";
}

// HANDLE PUBLISH / UPDATE
async function handlePublish(event) {
    event.preventDefault();
    const token = localStorage.getItem("token");

    const e = parseInt(document.getElementById('scoreE').value);
    const s = parseInt(document.getElementById('scoreS').value);
    const g = parseInt(document.getElementById('scoreG').value);
    const avg = Math.round((e + s + g) / 3);

    // Use FormData for Files
    const formData = new FormData();
    formData.append("companyName", document.getElementById('compName').value);
    formData.append("sector", document.getElementById('compSector').value);
    formData.append("location", document.getElementById('compLocation').value);
    formData.append("scoreE", e);
    formData.append("scoreS", s);
    formData.append("scoreG", g);
    formData.append("scoreAvg", avg);

    // Append file only if selected
    const fileInput = document.getElementById('reportFile');
    if (fileInput.files.length > 0) {
        formData.append("file", fileInput.files[0]);
    }

    try {
        const response = await fetch('http://localhost:8080/api/investor/publish', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            body: formData
        });

        if (response.ok) {
            alert("Profile Published/Updated Successfully!");
            closePublishModal();
        } else {
            alert("Failed to publish.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Error connecting to server.");
    }
}

// DELETE PROFILE
async function deleteProfile() {
    if(!confirm("Are you sure you want to remove your company from the Investor Portal?")) return;

    const token = localStorage.getItem("token");

    try {
        const response = await fetch('http://localhost:8080/api/investor/publish', {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        });

        if (response.ok) {
            alert("Profile removed successfully.");
            closePublishModal();
        } else {
            alert("Failed to delete.");
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

// Close modal if user clicks outside
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}