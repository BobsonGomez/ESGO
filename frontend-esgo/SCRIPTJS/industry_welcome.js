// --- 1. SECURITY GUARD ---
const token = localStorage.getItem("token");

if (!token) {
    // If no token exists, the user logged out. Kick them back to login.
    window.location.replace("login.html");
    throw new Error("Unauthorized access - Redirecting to login"); // Stops the rest of the script
}

// 1. Get the username from LocalStorage (Saved during login.js)
const userName = localStorage.getItem("currentUser") || "error";
const headerElement = document.getElementById("welcomeHeader");
if (headerElement) {
    headerElement.innerText = "Welcome, " + userName + "!";
}

// 2. Button Logic
function goToReport() {
    console.log("Redirecting to Report Form...");
    // Redirect to the detailed form we created
    window.location.href = "create_report.html";
}

function skipToHome() {
    // Redirect to the industry homepage
    window.location.replace("industry_homepage.html");
}

