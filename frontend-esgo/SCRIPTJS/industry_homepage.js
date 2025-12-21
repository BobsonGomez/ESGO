document.addEventListener("DOMContentLoaded", function() {

    // --- 1. SECURITY CHECK ---
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("currentUsername");

    if (!token || !username) {
        // If credentials are missing, kick them out immediately
        window.location.replace("login.html");
        return;
    }

    // --- 2. FETCH USER DATA (With Security Headers) ---
    fetch("http://localhost:8080/api/user/" + username, {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token, // <--- PASSPORT REQUIRED
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch user data (403/404)");
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "success") {
                // Update the HTML with real data from MySQL
                const displayElement = document.getElementById("usernameDisplay");
                if (displayElement) {
                    displayElement.innerText = data.fullname;
                }
            } else {
                console.error("User not found in DB");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            // If the token is invalid (expired), force logout
            if (error.message.includes("403")) {
                logout();
            }
            // Fallback for visual purposes only
            const localName = localStorage.getItem("currentUser") || "User";
            const displayElement = document.getElementById("usernameDisplay");
            if (displayElement) displayElement.innerText = localName;
        });
    // --- Fetch Reports List ---
    fetchReportsList();
});

// --- 3. LOGOUT FUNCTION ---
function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

// --- 4. SUBMIT REPORT LOGIC ---
/*function submitReport() {
    const username = localStorage.getItem("currentUsername");
    const token = localStorage.getItem("token"); // Get token for this request too

    if (!username || !token) {
        alert("Session error. Please login again.");
        window.location.href = "login.html";
        return;
    }

    // Call the backend to update the user's status
    fetch("http://localhost:8080/api/report/complete", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token // <--- PASSPORT REQUIRED HERE TOO
        },
        body: JSON.stringify({ username: username })
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            throw new Error("Server Request Failed");
        })
        .then(message => {
            console.log("Server response:", message);
            alert("Report Generated Successfully!");
            window.location.reload();
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Could not submit report. Check console.");
        });
}*/

// --- 4. FETCH REPORT LOGIC ---

function fetchReportsList() {
    const token = localStorage.getItem("token");
    const container = document.getElementById("reportListContainer");

    fetch("http://localhost:8080/api/report/list", {
        method: "GET",
        headers: { "Authorization": "Bearer " + token }
    })
        .then(response => response.json())
        .then(reports => {
            container.innerHTML = "";

            const countElement = document.getElementById("reportCountDisplay");
            if(countElement) {
                countElement.innerText = reports.length;
            }

            if (reports.length === 0) {
                container.innerHTML = "<p style='color:#888; padding:20px;'>No reports generated yet.</p>";
                return;
            }

            reports.forEach(report => {
                const date = new Date(report.createdDate).toLocaleDateString();

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
                            <h4>${report.reportName}</h4>
                            <p>${report.reportType} • ${date}</p>
                        </div>
                    </div>
                    
                    <!-- ACTION BUTTONS CONTAINER -->
                    <div class="action-buttons-group">
                        
                        <!-- 1. EDIT BUTTON -->
                        <div class="icon-btn edit" title="Edit Report" onclick="window.location.href='create_report.html?edit=${report.id}'">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V8z"></path>
                                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                            </svg>
                        </div>

                        <!-- 2. DOWNLOAD BUTTON -->
                        <div class="icon-btn download" title="Download" onclick="downloadReport(${report.id})">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                                <polyline points="7 10 12 15 17 10"></polyline>
                                <line x1="12" y1="15" x2="12" y2="3"></line>
                            </svg>
                        </div>

                        <!-- 3. DELETE BUTTON (New) -->
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

            document.querySelector(".value").innerText = reports.length;
        })
        .catch(err => console.error("Error loading reports:", err));
}

// Function to Re-Download a report
function downloadReport(id) {
    const token = localStorage.getItem("token");

    fetch(`http://localhost:8080/api/report/download/${id}`, {
        method: "GET",
        headers: { "Authorization": "Bearer " + token }
    })
        .then(res => res.blob())
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = `ESG_Report_${id}.docx`;
            document.body.appendChild(a);
            a.click();
            a.remove();
        })
        .catch(err => alert("Download failed: " + err));
}

// --- NEW DELETE FUNCTION ---
function deleteReport(id) {
    if(!confirm("Are you sure you want to delete this report?")) {
        return;
    }

    const token = localStorage.getItem("token");

    fetch(`http://localhost:8080/api/report/${id}`, {
        method: "DELETE",
        headers: { "Authorization": "Bearer " + token }
    })
        .then(response => {
            if(response.ok) {
                // 1. Remove Card
                const card = document.getElementById(`report-${id}`);
                if (card) card.remove();

                // 2. Update Counter (Safely)
                const countElem = document.getElementById("reportCountDisplay");
                if (countElem) {
                    let currentCount = parseInt(countElem.innerText);
                    if (isNaN(currentCount)) currentCount = 0;

                    const newCount = Math.max(0, currentCount - 1);
                    countElem.innerText = newCount.toString(); // Convert to string
                }

                // 3. Check if empty
                const container = document.getElementById("reportListContainer");
                if (container && container.children.length === 0) {
                    container.innerHTML = "<p style='color:#888; padding:20px; font-style:italic;'>No reports generated yet.</p>";
                }
            } else {
                alert("Failed to delete report.");
            }
        })
        .catch(err => console.error(err));
}