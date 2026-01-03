package com.esgo.backend.controller;

import java.io.ByteArrayInputStream;
import com.esgo.backend.model.User;
import com.esgo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.esgo.backend.security.JwtUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.esgo.backend.dto.BrsrReportRequest;
import com.esgo.backend.service.ReportService;
import com.esgo.backend.model.Report;
import com.esgo.backend.repository.ReportRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional; // Import Optional

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportRepository reportRepository;

    // --- REGISTER ENDPOINT ---
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // FIX 1: Check if present
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists!";
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        return "User registered successfully!";
    }

    // --- LOGIN ENDPOINT ---
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // FIX 2: Unwrap Optional, return null if empty
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {

            String token = jwtUtil.generateToken(username);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("role", user.getRole());
            response.put("fullname", user.getFullname());
            response.put("hasGeneratedReport", user.hasGeneratedReport());
            response.put("token", token);

            return response;

        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "failure");
            response.put("message", "Invalid credentials");
            return response;
        }
    }

    // --- REPORT COMPLETE ENDPOINT ---
    @PostMapping("/report/complete")
    public String completeReport(@RequestBody Map<String, String> data) {
        String username = data.get("username");

        // FIX 3: Unwrap Optional
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            user.setHasGeneratedReport(true);
            userRepository.save(user);
            return "Report status updated";
        }
        return "User not found";
    }

    @GetMapping("/user/{username}")
    public Map<String, Object> getUserDashboard(@PathVariable String username) {

        // FIX 4: Unwrap Optional
        User user = userRepository.findByUsername(username).orElse(null);

        Map<String, Object> response = new HashMap<>();

        if (user != null) {
            response.put("status", "success");
            response.put("fullname", user.getFullname());
            response.put("email", user.getEmail());

            List<Report> myReports = reportRepository.findByUserOrderByIdDesc(user);
            response.put("reportsCount", myReports.size());
            response.put("esgScore", "8.5/10");
        } else {
            response.put("status", "error");
            response.put("message", "User not found");
        }

        return response;
    }
}