// ===================================================================
// UNIFIED NAVBAR CODE (ROLE-AWARE)
// ===================================================================
// --- START: FINAL UNIFIED NAVBAR CODE ---

function buildNavbar(userRole, activePage) {
    const navbar = document.getElementById('mainNavbar');
    const username = localStorage.getItem("currentUsername") || "User";
    if (!navbar) return;

    let finalLinks = []; // This will hold the links to be displayed

    // Define the links that are available to all users
    const baseLinks = [
        { name: 'Industries', href: 'investor.html', id: 'industries' },
        { name: 'Messages', href: 'chat.html', id: 'messages' },
        { name: 'Profile', href: 'profile.html', id: 'profile' }
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


// ===================================================================
// PAGE INITIALIZATION
// ===================================================================

document.addEventListener("DOMContentLoaded", function() {
    const token = localStorage.getItem("token");

    // Redirect if not logged in
    if (!token) {
        window.location.replace("login.html");
        return;
    }

    // 1. GET THE REAL ROLE (Don't overwrite it!)
    const userRole = localStorage.getItem('userRole') || 'investor';

    // 2. Build the navbar with the CORRECT role
    buildNavbar(userRole, 'industries');

    const username = localStorage.getItem("currentUsername");
    const displayElement = document.getElementById("usernameDisplay");
    if (displayElement && username) {
        displayElement.innerText = username;
    }

    // 3. Load the industry data
    loadIndustries(token);
});

let allCompanies = [];

function loadIndustries(token) {
    const grid = document.getElementById('industryGrid');
    if(grid) grid.innerHTML = "<p style='padding:20px; color:#666;'>Loading sustainable industries...</p>";

    fetch("http://localhost:8080/api/investor/industries", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) throw new Error("Failed to fetch industries");
        return response.json();
    })
    .then(companies => {
        allCompanies = companies;
        renderGrid(allCompanies);
    })
    .catch(error => {
        console.error("Error loading industries:", error);
        if(grid) grid.innerHTML = "<p style='padding:20px; color:red;'>Error connecting to server.</p>";
    });
}

function renderGrid(companies) {
    const grid = document.getElementById('industryGrid');
    if(!grid) return;
    grid.innerHTML = "";

    if (companies.length === 0) {
        grid.innerHTML = "<p style='padding:20px; color:#64748b; font-style:italic;'>No industries found matching your criteria.</p>";
        return;
    }

    companies.forEach(comp => {
        // Image Selection
        let imgUrl = 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=800&q=80';
        if(comp.sector === 'Renewable Energy') imgUrl = 'https://images.unsplash.com/photo-1466611653911-95081537e5b7?auto=format&fit=crop&w=800&q=80';
        if(comp.sector === 'Manufacturing') imgUrl = 'https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?auto=format&fit=crop&w=800&q=80';
        if(comp.sector === 'Technology') imgUrl = 'https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=800&q=80';
        if(comp.sector === 'Healthcare') imgUrl = 'https://images.unsplash.com/photo-1538108149393-fbbd8189718c?auto=format&fit=crop&w=800&q=80';
        if(comp.sector === 'Finance') imgUrl = 'https://images.unsplash.com/photo-1611974765270-ca1258634369?auto=format&fit=crop&w=800&q=80';

        // Helper to keep decimals (max 2 places) without rounding to integer
        const formatScore = (val) => {
            if (val === undefined || val === null) return '0';
            return Number(val).toFixed(2).replace(/\.00$/, ''); // Removes .00 if it's a whole number
        };

        // Color coding
        const getScoreClass = (score) => {
            if(score >= 75) return 'green';
            if(score >= 50) return 'mid';
            return 'low';
        };

        const card = `
            <div class="card" onclick="openCompanyModal(${comp.id})">
                <div class="card-header" style="background-image: url('${imgUrl}')">
                    <!-- Display Exact Average Score -->
                    <div class="score-badge">${formatScore(comp.scoreAvg)}</div>
                </div>
                <div class="card-body">
                    <div class="card-title">${comp.companyName}</div>

                    <div class="card-meta">
                        <span class="meta-tag">🏢 ${comp.sector}</span>
                        <span class="meta-tag">📍 ${comp.address ? comp.address.split(',')[0] : 'N/A'}</span>
                    </div>

                    <div class="esg-row">
                        <div class="esg-item">
                            <span class="esg-label">Env</span>
                            <!-- Display Exact E Score -->
                            <div class="esg-val ${getScoreClass(comp.scoreE)}">${formatScore(comp.scoreE)}</div>
                        </div>
                        <div class="esg-item">
                            <span class="esg-label">Soc</span>
                            <!-- Display Exact S Score -->
                            <div class="esg-val ${getScoreClass(comp.scoreS)}">${formatScore(comp.scoreS)}</div>
                        </div>
                        <div class="esg-item">
                            <span class="esg-label">Gov</span>
                            <!-- Display Exact G Score -->
                            <div class="esg-val ${getScoreClass(comp.scoreG)}">${formatScore(comp.scoreG)}</div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        grid.innerHTML += card;
    });
}

function filterIndustries() {
    const query = document.getElementById('searchInput').value.toLowerCase();
    const filtered = allCompanies.filter(comp =>
        (comp.companyName && comp.companyName.toLowerCase().includes(query)) ||
        (comp.sector && comp.sector.toLowerCase().includes(query)) ||
        (comp.address && comp.address.toLowerCase().includes(query))
    );
    renderGrid(filtered);
}

function filterBySector(sector) {
    const buttons = document.querySelectorAll('.filter-tag');
    if(buttons) buttons.forEach(btn => btn.classList.remove('active'));
    if(event && event.target) event.target.classList.add('active');

    if (sector === 'All') {
        renderGrid(allCompanies);
    } else {
        const filtered = allCompanies.filter(comp => comp.sector === sector);
        renderGrid(filtered);
    }
}

// --- COMPANY MODAL LOGIC ---
function openCompanyModal(id) {
    const company = allCompanies.find(c => c.id === id);
    if (!company) return;

    // 1. Populate Text Fields
    document.getElementById('modalCompanyName').innerText = company.companyName;
    document.getElementById('modalSectorBadge').innerText = company.sector;
    document.getElementById('modalAddress').innerText = "📍 " + (company.address || "N/A");
    document.getElementById('modalPhone').innerText = "📞 " + (company.phone || "N/A");
    document.getElementById('modalEmail').innerText = "✉️ " + (company.email || "N/A");

    // 2. Populate Description
    const desc = company.description ? company.description : "This company has not provided a detailed sustainability description yet.";
    document.getElementById('modalDescription').innerText = desc;

    // 3. Populate Scores (Using formatScore helper if available, or raw values)
    // Note: If you want formatted decimals in modal too, wrap these in formatScore()
    document.getElementById('modalScoreE').innerText = company.scoreE;
    document.getElementById('modalScoreS').innerText = company.scoreS;
    document.getElementById('modalScoreG').innerText = company.scoreG;

    // 4. Configure Download Button
    const btnDownload = document.getElementById('btnDownloadReport');
    if (company.reportFileName) {
        btnDownload.classList.remove("disabled-btn");
        btnDownload.disabled = false;
        btnDownload.innerHTML = "📄 Download Report";
        btnDownload.onclick = function() {
            downloadPublicReport(company.id, company.reportFileName);
        };
    } else {
        btnDownload.classList.add("disabled-btn");
        btnDownload.disabled = true;
        btnDownload.innerHTML = "📄 No Report Attached";
        btnDownload.onclick = null;
    }

    // 5. Configure Message Button (FIXED LOGIC)
    const btnMessage = document.getElementById('btnMessageCompany');
    const userRole = localStorage.getItem('userRole'); // Get current logged-in role

    if (btnMessage) {
        if (userRole === 'industry') {
            // HIDE button if the viewer is an Industry user
            btnMessage.style.display = 'none';
        } else {
            // SHOW button if the viewer is an Investor
            btnMessage.style.display = 'inline-block'; // Reset display in case it was hidden
            btnMessage.onclick = function() {
                if (company.user && company.user.id) {
                    window.location.href = `chat.html?startChatWith=${company.user.id}`;
                } else {
                    console.error("No User ID found for this company profile");
                    alert("Cannot message this company: Contact info missing.");
                }
            };
        }
    }

    // 6. Show Modal
    document.getElementById('companyModal').style.display = "block";
}

function closeCompanyModal() {
    document.getElementById('companyModal').style.display = "none";
}

function downloadPublicReport(id, fileName) {
    const token = localStorage.getItem("token");
    fetch(`http://localhost:8080/api/investor/download-report/${id}`, {
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
        a.download = fileName || "ESG_Report.docx";
        document.body.appendChild(a);
        a.click();
        a.remove();
    })
    .catch(err => alert("Error downloading report: " + err));
}

// Close modal if clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('companyModal');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}