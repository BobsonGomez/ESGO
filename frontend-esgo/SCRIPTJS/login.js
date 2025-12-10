function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    if (username === "" || password === "") {
        alert("Please fill in all fields.");
        return;
    }

    const loginData = {
        username: username,
        password: password
    };

    // --- FIX START: correctly defined fetch request ---
    fetch("http://localhost:8080/api/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(loginData)
    }).then(response => {
            // Check if the server actually responded with OK
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            console.log("Server Response:", data); // Debugging line

            if (data.status === "success") {
                // Save info for the next pages
                const nameToSave = data.fullname || "Error";
                localStorage.setItem("currentUser",nameToSave);
                localStorage.setItem("token", data.token); // <--- SAVE THE PASSPORT
                localStorage.setItem("currentUsername", username);

                alert("Login Successful!");

                if (data.role === "industry") {
                    // LOGIC CHECK:
                    // If hasGeneratedReport is FALSE (or null/undefined), go to Welcome
                    if (data.hasGeneratedReport === true) {
                        window.location.href = "industry_homepage.html"; // Dashboard
                    } else {
                        window.location.href = "industry_welcome.html"; // Welcome/Report Page
                    }
                } else if (data.role === "investor") {
                    window.location.href = "investor.html";
                } else {
                    // Fallback if role is weird
                    alert("Unknown role: " + data.role);
                }

            } else {
                // If password was wrong
                alert("Login Failed: " + data.message);
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Could not connect to server. Is the Java Backend running?");
        });
}