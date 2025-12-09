// 1. Get the username from LocalStorage (Saved during login.js)
const userName = localStorage.getItem("currentUser") || "error";
document.getElementById("welcomeHeader").innerText = "Welcome, " + userName + "!";

// 2. Button Logic
function goToReport() {
    console.log("Redirecting to Report Creation...");
    // 1. Get the username
    const username = localStorage.getItem("currentUsername");

    // 2. Tell Backend: "This user just finished a report"
    fetch("http://localhost:8080/api/report/complete", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: username })
    })
        .then(response => response.text())
        .then(message => {
            console.log("Server says:", message);
            alert("Report Generated Successfully!");

            // 3. NOW redirect to the dashboard
            window.location.href = "industry.html";
        })
        .catch(error => console.error("Error:", error));
    // Create this HTML file later
    window.location.href = "industry_homepage.html";

}

function skipToHome() {
    // Redirect to the industry homepage
    window.location.href = "industry_homepage.html";
}

function submitReport() {
}