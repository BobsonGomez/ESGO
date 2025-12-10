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
    console.log("Redirecting to Report Creation...");
    // 1. Get the username
    const username = localStorage.getItem("currentUsername");
    const token = localStorage.getItem("token");

    if (!token) {
        alert("You are not logged in!");
        window.location.href = "login.html";
        return;
    }
    // 2. Tell Backend: "This user has finished using up the welcome page display"
    fetch("http://localhost:8080/api/report/complete", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ username: username })
    })
        .then(response => {
            // Check if the backend actually accepted it
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("Server rejected request (403/500)");
            }
        })
        .then(message => {
            console.log("Server says:", message);
            // ONLY Redirect here, after success is confirmed
            window.location.replace("industry_homepage.html");
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Failed to update status. Check console.");
        });

}

function skipToHome() {
    // Redirect to the industry homepage
    window.location.replace("industry_homepage.html");
}

