// ===================================================================
// UNIFIED NAVBAR CODE (ROLE-AWARE)
// ===================================================================
// --- START: FINAL UNIFIED NAVBAR CODE ---

function buildNavbar(userRole, activePage) {
    const navbar = document.getElementById('mainNavbar');
    const username = localStorage.getItem("currentUsername") || "User";
    if (!navbar) return;

    let finalLinks = [];

    const baseLinks = [
        { name: 'Industries', href: 'investor.html', id: 'industries' },
        { name: 'Messages', href: 'chat.html', id: 'messages' },
        { name: 'Profile', href: 'profile.html', id: 'profile' }
    ];

    if (userRole === 'industry') {
        finalLinks = [
            { name: 'Dashboard', href: 'industry_homepage.html', id: 'dashboard' },
            ...baseLinks
        ];
    } else { // investor role
        finalLinks = baseLinks;
    }

    let navLinksHTML = finalLinks.map(link =>
        `<a href="${link.href}" class="${link.id === activePage ? 'active' : ''}">${link.name}</a>`
    ).join('');

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


// --- PROFILE PAGE SPECIFIC CODE ---

document.addEventListener("DOMContentLoaded", function() {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    // Read the user's role from storage to build the correct navbar
    const userRole = localStorage.getItem('userRole') || 'investor'; // Default to investor

    // Build the correct navbar and highlight 'profile' as active
    buildNavbar(userRole, 'profile');

    // Now, load the user's profile information
    loadProfile();
});


async function loadProfile() {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("currentUsername");

    try {
        const response = await fetch('http://localhost:8080/api/user/me', {
            headers: { 'Authorization': 'Bearer ' + token }
        });
        const user = await response.json();

        document.getElementById('fullNameInput').value = user.fullname || '';
        document.getElementById('usernameInput').value = user.username;

        // Load profile picture, add cache-buster to show changes immediately
        const pfp = document.getElementById('pfpPreview');
        pfp.src = `http://localhost:8080/api/user/${user.username}/pfp?t=${new Date().getTime()}`;
        pfp.onerror = () => { pfp.src = 'https://via.placeholder.com/150'; }; // Fallback image if none exists

    } catch (error) {
        console.error("Failed to load profile", error);
        alert("Could not load your profile information.");
    }
}

function previewImage(event) {
    if (event.target.files && event.target.files[0]) {
        const reader = new FileReader();
        reader.onload = function(){
            const output = document.getElementById('pfpPreview');
            output.src = reader.result;
        };
        reader.readAsDataURL(event.target.files[0]);
    }
}

async function handleUpdate(event) {
    event.preventDefault();
    const token = localStorage.getItem("token");
    const formData = new FormData();
    const fileInput = document.getElementById('pfpInput');

    formData.append('fullname', document.getElementById('fullNameInput').value);

    if (fileInput.files.length > 0) {
        formData.append('pfp', fileInput.files[0]);
    }

    try {
        const response = await fetch('http://localhost:8080/api/user/update', {
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + token },
            body: formData
        });

        if (response.ok) {
            alert('Profile updated successfully!');
            // Update the user's full name in local storage if it's stored there for the welcome message
            localStorage.setItem('currentUser', document.getElementById('fullNameInput').value);
            loadProfile(); // Refresh profile data
        } else {
            alert('Failed to update profile.');
        }
    } catch (error) {
        console.error("Error updating profile:", error);
    }
}