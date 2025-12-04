package com.esgo.backend.controller;

import com.esgo.backend.model.User;
import com.esgo.backend.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // Tells Spring this class handles API requests (JSON)
@RequestMapping("/api") // All endpoints here start with /api
@CrossOrigin(origins = "*") // CRITICAL: Allows your HTML file to talk to this Java server
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // --- REGISTER ENDPOINT ---
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists!";
        }

        // Save the user to MySQL
        userRepository.save(user);
        return "User registered successfully!";
    }

    // --- LOGIN ENDPOINT ---
    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // Find user in DB
        User user = userRepository.findByUsername(username);

        // Logic Check
        if (user != null && user.getPassword().equals(password)) {
            // Login Success: Return the role so frontend knows where to redirect
            return Map.of("status", "success", "role", user.getRole()); 
        } else {
            return Map.of("status", "failure", "message", "Invalid credentials");
        }
    }
}