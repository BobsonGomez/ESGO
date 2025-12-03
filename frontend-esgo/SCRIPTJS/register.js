function register() {
    const fullname = document.getElementById("fullname").value;
    const email = document.getElementById("email").value;
    const username = document.getElementById("username").value;
    const userRole = document.getElementById("userRole").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (!fullname || !email || !username || !userRole || !password || !confirmPassword) {
        alert("Please fill out all fields.");
        return;
    }

    if (password !== confirmPassword) {
        alert("Passwords do not match!");
        return;
    }

    // Prepare data to send to Java
    const registrationData = {
        fullname: fullname,
        email: email,
        username: username,
        password: password,
        role: userRole
    };

    // Send Data to Backend
    fetch("http://localhost:8080/api/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(registrationData)
    })
    .then(response => response.text()) // Backend returns plain text message
    .then(message => {
        if (message === "User registered successfully!") {
            alert(message);
            window.location.href = "login.html";
        } else {
            alert("Error: " + message);
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("Could not connect to server.");
    });
}