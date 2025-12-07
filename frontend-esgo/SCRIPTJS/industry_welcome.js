// 1. Get the username from LocalStorage (Saved during login.js)
const userName = localStorage.getItem("currentUser") || "error";
document.getElementById("welcomeHeader").innerText = "Welcome, " + userName + "!";

// 2. Button Logic
function goToReport() {
    console.log("Redirecting to Report Creation...");
    // Create this HTML file later
    window.location.href = "create_report.html";
}

function skipToHome() {
    // Redirect to the homepage
    window.location.href = "investor.html";
}