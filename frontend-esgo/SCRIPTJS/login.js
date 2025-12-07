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
        .then(response => response.json())
        .then(data => {
            if (data.status === "success") {

                // Save the name to LocalStorage
                // If data.fullname is null, we use "User" as a backup
                const nameToSave = data.fullname || "User";
                localStorage.setItem("currentUser", nameToSave);

                alert("Login Successful! Welcome " + nameToSave);

                //FIX 2: Check for First Login (Industry only)
                if (data.role === "industry") {
                    if (data.isFirstLogin) {
                        // Go to the Welcome/Report page
                        window.location.href = "industry_welcome.html";
                    } else {
                        // Go to the standard Industry Dashboard
                        window.location.href = "industry.html";
                    }
                } else if (data.role === "investor") {
                    window.location.href = "investor.html";
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