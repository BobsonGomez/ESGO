document.addEventListener("DOMContentLoaded", function() {

    // --- 1. SECURITY CHECK ---
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("currentUsername");

    if (!token) {
        // If token is missing, kick them out immediately
        window.location.replace("login.html");
        return;
    }

    // --- 2. FETCH USER DATA (For Navbar) ---
    // This assumes the Investor is also a "User" in your system
    fetch("http://localhost:8080/api/user/" + (username || "current"), {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch user data");
        }
        return response.json();
    })
    .then(data => {
        // Update the Navbar "Profile" link with the name
        const profileLink = document.getElementById("userProfileLink");
        if (profileLink) {
            // Assuming the API returns 'fullname' like the industry page
            profileLink.innerText = `Profile (${data.fullname || data.username || "Investor"})`;
        }
    })
    .catch(error => {
        console.error("Error fetching user:", error);
        if (error.message.includes("403") || error.message.includes("401")) {
            logout();
        }
    });

    // --- 3. FETCH INDUSTRIES LIST ---
    loadIndustries(token);
});

// --- 4. LOGOUT FUNCTION ---
function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

// --- 5. GLOBAL VARIABLES FOR FILTERING ---
let allCompanies = [];

// --- 6. LOAD INDUSTRIES LOGIC ---
function loadIndustries(token) {
    const grid = document.getElementById('industryGrid');

    // Show loading state
    grid.innerHTML = "<p style='padding:20px; color:#666;'>Loading sustainable industries...</p>";

    fetch("http://localhost:8080/api/investor/industries", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch industries");
        }
        return response.json();
    })
    .then(companies => {
        allCompanies = companies; // Save for client-side filtering
        renderGrid(allCompanies);
    })
    .catch(error => {
        console.error("Error loading industries:", error);
        grid.innerHTML = "<p style='padding:20px; color:red;'>Error connecting to server. Please try again later.</p>";
    });
}

// --- 7. RENDER GRID (UI GENERATION) ---
function renderGrid(companies) {
    const grid = document.getElementById('industryGrid');
    grid.innerHTML = "";

    if (companies.length === 0) {
        grid.innerHTML = "<p style='padding:20px; color:#666; font-style:italic;'>No industries found matching your criteria.</p>";
        return;
    }

    companies.forEach(comp => {
        // Dynamic Image based on Sector (Visual Polish)
        let imgUrl = 'https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=500&q=60'; // Default Office
        if(comp.sector === 'Renewable Energy') imgUrl = 'https://images.unsplash.com/photo-1509391366360-2e959784a276?auto=format&fit=crop&w=500&q=60';
        if(comp.sector === 'Manufacturing') imgUrl = 'https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?auto=format&fit=crop&w=500&q=60';
        if(comp.sector === 'Technology') imgUrl = 'https://images.unsplash.com/photo-1519389950473-47ba0277781c?auto=format&fit=crop&w=500&q=60';
        if(comp.sector === 'Healthcare') imgUrl = 'https://images.unsplash.com/photo-1505751172876-fa1923c5c528?auto=format&fit=crop&w=500&q=60';
        if(comp.sector === 'Finance') imgUrl = 'https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?auto=format&fit=crop&w=500&q=60';

        // NOTE: Make sure variable names match your Java Entity (companyName, scoreAvg, etc.)
        const card = `
            <div class="card">
                <div class="card-header" style="background-image: url('${imgUrl}')">
                    <div class="score-badge">${comp.scoreAvg}</div>
                </div>
                <div class="card-body">
                    <div class="card-title">${comp.companyName}</div>
                    <div class="card-meta">
                        <span>🏢 ${comp.sector}</span>
                        <span>📍 ${comp.location}</span>
                    </div>
                    <div class="esg-row">
                        <div class="esg-item">
                            <span class="esg-label">E</span>
                            <span class="esg-val green">${comp.scoreE}</span>
                        </div>
                        <div class="esg-item">
                            <span class="esg-label">S</span>
                            <span class="esg-val green">${comp.scoreS}</span>
                        </div>
                        <div class="esg-item">
                            <span class="esg-label">G</span>
                            <span class="esg-val green">${comp.scoreG}</span>
                        </div>
                    </div>
                </div>
            </div>
        `;
        grid.innerHTML += card;
    });
}

// --- 8. CLIENT-SIDE SEARCH LOGIC ---
function filterIndustries() {
    const query = document.getElementById('searchInput').value.toLowerCase();

    const filtered = allCompanies.filter(comp =>
        (comp.companyName && comp.companyName.toLowerCase().includes(query)) ||
        (comp.sector && comp.sector.toLowerCase().includes(query)) ||
        (comp.location && comp.location.toLowerCase().includes(query))
    );

    renderGrid(filtered);
}

// --- 9. CLIENT-SIDE BUTTON FILTER LOGIC ---
function filterBySector(sector) {
    // Update active button visual
    const buttons = document.querySelectorAll('.filter-tag');
    buttons.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');

    if (sector === 'All') {
        renderGrid(allCompanies);
    } else {
        const filtered = allCompanies.filter(comp => comp.sector === sector);
        renderGrid(filtered);
    }
}