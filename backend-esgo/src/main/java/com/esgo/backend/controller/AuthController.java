package com.esgo.backend.controller;

import com.esgo.backend.model.User;
import com.esgo.backend.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
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
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("role", user.getRole());
            response.put("fullname", user.getFullname());

            // Send the new flag status
            response.put("hasGeneratedReport", user.hasGeneratedReport());

            return response;
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "failure");
            response.put("message", "Invalid credentials");
            return response;
        }
    }

    // --- NEW ENDPOINT: Call this when they click "Generate Report" ---
    @PostMapping("/report/complete")
    public String completeReport(@RequestBody Map<String, String> data) {
        String username = data.get("username");

        User user = userRepository.findByUsername(username);

        if (user != null) {
            user.setHasGeneratedReport(true); // NOW we mark it as done
            userRepository.save(user);
            return "Report status updated";
        }
        return "User not found";
    }
}