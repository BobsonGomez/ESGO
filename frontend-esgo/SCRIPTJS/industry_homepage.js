// --- START: FINAL UNIFIED NAVBAR CODE (ROLE-AWARE) ---

function buildNavbar(userRole, activePage) {
    const navbar = document.getElementById('mainNavbar');
    const username = localStorage.getItem("currentUsername") || "User";
    if (!navbar) return;

    let finalLinks = []; // This will hold the links to be displayed

    // Define the links that are available to all users
    const baseLinks = [
        { name: 'Industries', href: 'investor.html', id: 'industries' },
        { name: 'Messages', href: 'chat.html', id: 'messages' },
        { name: 'Profile', href: 'profile.html', id: 'profile' } // This now ALWAYS points to the profile update page
    ];

    // Create the final link structure based on the user's role
    if (userRole === 'industry') {
        finalLinks = [
            // An industry user gets a "Dashboard" link first
            { name: 'Dashboard', href: 'industry_homepage.html', id: 'dashboard' },
            ...baseLinks // Then add all the common links
        ];
    } else { // An investor user does NOT get a dashboard link
        finalLinks = baseLinks;
    }

    // Generate the HTML for the links, adding the 'active' class to the current page
    let navLinksHTML = finalLinks.map(link =>
        `<a href="${link.href}" class="${link.id === activePage ? 'active' : ''}">${link.name}</a>`
    ).join('');

    // The complete navbar HTML structure
    navbar.innerHTML = `
        <div class="logo">ESG Invest</div>
        <div class="nav-links">${navLinksHTML}</div>
        <div class="nav-actions">
            <span class="welcome-text">Welcome, ${username}</span>
            <button class="btn-logout" onclick="logout()">Logout</button>
        </div>
    `;
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

// --- END: FINAL UNIFIED NAVBAR CODE ---

document.addEventListener("DOMContentLoaded", function() {
    buildNavbar('industry', 'dashboard');
    localStorage.setItem('userRole', 'industry'); // Also store the role for other pages
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("currentUsername");

    if (!token) {
        window.location.replace("login.html");
        return;
    }

    const displayElement = document.getElementById("usernameDisplay");
    if (displayElement) {
        displayElement.innerText = localStorage.getItem("currentUser") || username || "User";
    }

    fetchReportsList();
    checkExistingProfile();

    fetchDashboardData();
});

// --- MAIN FUNCTIONS ---
// Check existing profile for dashboard button state
async function checkExistingProfile() {
    const token = localStorage.getItem("token");
    const dashboardBtn = document.getElementById("dashboardPublishBtn");

    try {
        const response = await fetch('http://localhost:8080/api/investor/my-profile', {
            headers: { 'Authorization': 'Bearer ' + token }
        });

        if (response.status === 200) {
            if(dashboardBtn) {
                dashboardBtn.innerText = "Edit Publication";
            }
        } else {
            if(dashboardBtn) {
                dashboardBtn.innerText = "Publish Profile";
            }
        }
    } catch (error) {
        console.log("Error checking profile status.");
    }
}

// 1. Fetch & Display Reports
function fetchReportsList() {
    const token = localStorage.getItem("token");
    const container = document.getElementById("reportListContainer");
    const countDisplay = document.getElementById("reportCountDisplay");

    fetch("http://localhost:8080/api/report/list", {
        method: "GET",
        headers: { "Authorization": "Bearer " + token }
    })
    .then(response => response.json())
    .then(reports => {
        if(countDisplay) countDisplay.innerText = reports.length;
        container.innerHTML = "";

        if (reports.length === 0) {
            container.innerHTML = "<p style='color:#888; padding:20px;'>No reports generated yet.</p>";
            return;
        }

        reports.forEach(report => {
            const dateStr = new Date(report.createdDate).toLocaleDateString();
            const html = `
            <div class="card report-item" id="report-${report.id}">
                <div class="report-left">
                    <div class="file-icon"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#16a34a" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path></svg></div>
                    <div class="report-details"><h4>${report.reportName || "Untitled"}</h4><p>${report.reportType} • ${dateStr}</p></div>
                </div>
                <div class="action-buttons-group">
                    <div class="icon-btn edit" onclick="window.location.href='create_report.html?edit=${report.id}'">✏️</div>
                    <div class="icon-btn download" onclick="downloadReport(${report.id})">⬇️</div>
                    <div class="icon-btn delete" onclick="deleteReport(${report.id})">🗑️</div>
                </div>
            </div>`;
            container.innerHTML += html;
        });
    })
    .catch(err => container.innerHTML = "<p>Error loading reports.</p>");
}

async function downloadReport(id) {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Please login first.");
        return;
    }

    try {
        // Show user something is happening
        document.body.style.cursor = "wait";

        const response = await fetch(`http://localhost:8080/api/report/download/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (response.ok) {
            // 1. Convert the response to a Blob (Binary File)
            const blob = await response.blob();

            // 2. Create a temporary URL for the Blob
            const url = window.URL.createObjectURL(blob);

            // 3. Create a hidden link and click it to trigger download
            const a = document.createElement('a');
            a.href = url;
            // Use the ID in the filename, or get filename from headers if possible
            a.download = `BRSR_Report_${id}.docx`;
            document.body.appendChild(a);
            a.click();

            // 4. Cleanup
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
        } else {
            alert("Failed to download report.");
        }
    } catch (error) {
        console.error("Download error:", error);
        alert("Error downloading file.");
    } finally {
        document.body.style.cursor = "default";
    }
}

async function deleteReport(id) {
    if (!confirm("Are you sure you want to delete this report permanently?")) {
        return;
    }

    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`http://localhost:8080/api/report/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (response.ok) {
            // 1. Remove the item from the HTML list visually
            const item = document.getElementById(`report-${id}`);
            if (item) {
                item.style.transition = "opacity 0.3s, transform 0.3s";
                item.style.opacity = "0";
                item.style.transform = "translateX(20px)";
                setTimeout(() => item.remove(), 300);
            }

            // 2. Update the counter at the top
            const countDisplay = document.getElementById("reportCountDisplay");
            if(countDisplay) {
                let current = parseInt(countDisplay.innerText);
                if(!isNaN(current) && current > 0) {
                    countDisplay.innerText = current - 1;
                }
            }

            // 3. Show empty state if that was the last one
            const container = document.getElementById("reportListContainer");
            if (container.children.length <= 1) { // <= 1 because we haven't removed the element yet
                 setTimeout(() => {
                     if(container.children.length === 0) {
                        container.innerHTML = "<p style='color:#888; padding:20px;'>No reports generated yet.</p>";
                     }
                 }, 350);
            }

        } else {
            const errText = await response.text();
            alert("Failed to delete: " + errText);
        }
    } catch (error) {
        console.error("Delete error:", error);
        alert("Error connecting to server.");
    }
}

// --- PUBLISH / INVESTOR MODAL LOGIC ---
const modal = document.getElementById("publishModal");

async function openPublishModal() {
    modal.style.display = "block";
    const token = localStorage.getItem("token");

    // Default UI
    document.getElementById("publishForm").reset();
    document.getElementById("modalTitle").innerText = "Publish Company Profile";
    document.getElementById("btnPublish").innerText = "Publish to Investors";
    document.getElementById("btnDeleteProfile").style.display = "none";
    document.getElementById("currentFileText").style.display = "none";

    // 1. Check for AI Generated Scores (Priority 1)
    const storedScores = localStorage.getItem("latestGeneratedScores");
    let aiScores = null;
    if (storedScores) {
        aiScores = JSON.parse(storedScores);
        // Fill Visible Inputs
        document.getElementById("showScoreE").value = aiScores.e;
        document.getElementById("showScoreS").value = aiScores.s;
        document.getElementById("showScoreG").value = aiScores.g;
        document.getElementById("showScoreAvg").value = aiScores.avg;
        document.getElementById("scoreNote").innerText = "Note: Populated from your latest AI Analysis.";
    }

    // 2. Fetch existing profile (Priority 2 - but don't overwrite AI scores if they are new)
    try {
        const response = await fetch('http://localhost:8080/api/investor/my-profile', {
            headers: { 'Authorization': 'Bearer ' + token }
        });

        if (response.status === 200) {
            const data = await response.json();

            // Fill Text Fields
            document.getElementById('compName').value = data.companyName || "";
            document.getElementById('compSector').value = data.sector || "Technology";
            document.getElementById('compDesc').value = data.description || "";
            document.getElementById('compAddress').value = data.address || "";
            document.getElementById('compPhone').value = data.phone || "";
            document.getElementById('compEmail').value = data.email || "";

            // Fill Scores ONLY if we didn't just generate a new one
            if (!aiScores) {
                document.getElementById("showScoreE").value = data.scoreE;
                document.getElementById("showScoreS").value = data.scoreS;
                document.getElementById("showScoreG").value = data.scoreG;
                document.getElementById("showScoreAvg").value = data.scoreAvg;
                document.getElementById("scoreNote").innerText = "Note: Showing currently published scores.";
            }

            // Edit Mode UI
            document.getElementById("modalTitle").innerText = "Edit Company Profile";
            document.getElementById("btnPublish").innerText = "Update Profile";
            document.getElementById("btnDeleteProfile").style.display = "block";

            if(data.reportFileName) {
                document.getElementById("currentFileText").style.display = "block";
                document.getElementById("fileNameDisplay").innerText = data.reportFileName;
            }
        }
    } catch (error) {
        console.log("No existing profile found.");
    }
}

function closePublishModal() {
    modal.style.display = "none";
}

async function handlePublish(event) {
    if(event) event.preventDefault();
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Session expired. Please login.");
        window.location.href = "login.html";
        return;
    }

    const btn = document.getElementById("btnPublish");
    const originalText = btn.innerText;
    btn.disabled = true;
    btn.innerText = "Uploading...";

    // Helper function to read the file as a Base64 string
    const getBase64 = (file) => new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });

    try {
        let fileBase64 = null;
        let fileName = null;

        // Grab the file if the user selected one
        const fileInput = document.getElementById('reportFile');
        if (fileInput.files.length > 0) {
            const file = fileInput.files[0];
            fileName = file.name;
            fileBase64 = await getBase64(file); // Convert to Base64 String
        }

        // Build standard JSON payload instead of FormData
        const payload = {
            companyName: document.getElementById('compName').value,
            sector: document.getElementById('compSector').value,
            description: document.getElementById('compDesc').value,
            address: document.getElementById('compAddress').value,
            phone: document.getElementById('compPhone').value,
            email: document.getElementById('compEmail').value,
            scoreE: document.getElementById('showScoreE').value,
            scoreS: document.getElementById('showScoreS').value,
            scoreG: document.getElementById('showScoreG').value,
            scoreAvg: document.getElementById('showScoreAvg').value,
            reportFileName: fileName,
            reportFileBase64: fileBase64
        };

        const response = await fetch('http://localhost:8080/api/investor/publish', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json' // Force JSON
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Success! Profile and Report Published.");
            localStorage.removeItem("latestGeneratedScores");
            closePublishModal();
            checkExistingProfile();
        } else {
            const txt = await response.text();
            alert("Failed: " + txt);
        }
    } catch (error) {
        console.error(error);
        alert("Connection Error");
    } finally {
        btn.disabled = false;
        btn.innerText = originalText;
    }
}

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
            const dashboardBtn = document.getElementById("dashboardPublishBtn");
            if(dashboardBtn) {
                dashboardBtn.innerText = "Publish Profile";
                dashboardBtn.style.backgroundColor = "#27ae60";
            }
        } else {
            alert("Failed to delete.");
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

async function fetchDashboardData() {
    const token = localStorage.getItem("token");
    const scoreDisplay = document.getElementById("esgScoreDisplay");
    if (!scoreDisplay) return;

    // First check: Did they just generate a new AI score that hasn't been published yet?
    const storedScores = localStorage.getItem("latestGeneratedScores");
    if (storedScores) {
        try {
            const parsedScores = JSON.parse(storedScores);
            scoreDisplay.innerText = parsedScores.avg || "--";
            scoreDisplay.style.color = "#27ae60"; // Optional: Make it green to indicate it's new
            return; // Exit early so we show the new unpublished score
        } catch (e) {
            console.error("Error parsing local scores");
        }
    }

    // Second check: If no new local score, fetch their officially published profile
    try {
        const response = await fetch('http://localhost:8080/api/investor/my-profile', {
            headers: { 'Authorization': 'Bearer ' + token }
        });

        if (response.status === 200) {
            const data = await response.json();
            scoreDisplay.innerText = data.scoreAvg || "--";
            scoreDisplay.style.color = ""; // Reset color
        } else {
            scoreDisplay.innerText = "--";
        }
    } catch (error) {
        console.log("Error fetching profile for dashboard score:", error);
        scoreDisplay.innerText = "--";
    }
}

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

