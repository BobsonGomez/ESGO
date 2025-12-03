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

    fetch("http://localhost:8080/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginData)
    })
    .then(response => response.json()) // Backend returns a JSON Map
    .then(data => {
        if (data.status === "success") {
            alert("Login Successful! Welcome " + data.role);
            
            // Redirect based on role
            if (data.role === "industry") {
                // window.location.href = "industry_dashboard.html";
                console.log("Redirecting to Industry Dashboard...");
            } else if (data.role === "investor") {
                // window.location.href = "investor_dashboard.html";
                console.log("Redirecting to Investor Dashboard...");
            }
        } else {
            alert("Login Failed: " + data.message);
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("Could not connect to server.");
    });
}