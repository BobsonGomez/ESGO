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
});

// --- 3. LOGOUT FUNCTION ---
function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

// --- 4. SUBMIT REPORT LOGIC ---
function submitReport() {
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
}