package com.esgo.backend.controller;

import com.esgo.backend.model.User;
import com.esgo.backend.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.esgo.backend.security.JwtUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api") // All endpoints here start with /api
@CrossOrigin(origins = "*") // CRITICAL: Allows your HTML file to talk to this Java server
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    // --- REGISTER ENDPOINT ---
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists!";
        }

        // --- SECURITY CHANGE ---
        // Hash the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        // -----------------------

        userRepository.save(user);
        return "User registered successfully!";
    }

    // --- LOGIN ENDPOINT ---
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password"); // The raw password typed by user

        User user = userRepository.findByUsername(username);

        // --- SECURITY CHANGE ---
        // We use .matches(rawPassword, encryptedPasswordFromDB)
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {

            // ... (Rest of your code remains the same: Generate Token, Response, etc.) ...
            String token = jwtUtil.generateToken(username);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("role", user.getRole());
            response.put("fullname", user.getFullname());
            response.put("hasGeneratedReport", user.hasGeneratedReport());
            response.put("token", token);

            return response;

        } else {
            // Failure
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

    @GetMapping("/user/{username}")
    public Map<String, Object> getUserDashboard(@PathVariable String username) {

        User user = userRepository.findByUsername(username);

        Map<String, Object> response = new HashMap<>();

        if (user != null) {
            response.put("status", "success");
            response.put("fullname", user.getFullname());
            response.put("email", user.getEmail());

            // --- PLACEHOLDERS FOR FUTURE DATA ---
            // Later, you will calculate these from real database tables
            response.put("reportsCount", 24);
            response.put("esgScore", "8.5/10");
        } else {
            response.put("status", "error");
            response.put("message", "User not found");
        }

        return response;
    }
}