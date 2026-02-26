package com.esgo.backend.controller;

import com.esgo.backend.model.IndustryProfile;
import com.esgo.backend.model.User;
import com.esgo.backend.repository.IndustryProfileRepository;
import com.esgo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/investor")
@CrossOrigin(origins = "*")
public class InvestorController {

    @Autowired
    private IndustryProfileRepository profileRepo;

    @Autowired
    private UserRepository userRepo;

    // 1. Get All (For Grid)
    @GetMapping("/industries")
    public ResponseEntity<List<IndustryProfile>> getAllIndustries() {
        List<IndustryProfile> profiles = profileRepo.findAll();
        profiles.sort((p1, p2) -> Double.compare(p2.getScoreAvg(), p1.getScoreAvg()));
        return ResponseEntity.ok(profiles);
    }

    // 2. Get My Profile (For Edit)
    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile(Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(403).body("User not found");
        return profileRepo.findByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    // 3. PUBLISH (With File Upload)
    @PostMapping(value = "/publish", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> publishProfile(
            Principal principal,
            @RequestParam("companyName") String companyName,
            @RequestParam("sector") String sector,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestParam(value = "scoreE", defaultValue = "0") String scoreE,
            @RequestParam(value = "scoreS", defaultValue = "0") String scoreS,
            @RequestParam(value = "scoreG", defaultValue = "0") String scoreG,
            @RequestParam(value = "scoreAvg", defaultValue = "0") String scoreAvg,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            User user = userRepo.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            IndustryProfile profile = profileRepo.findByUser(user).orElse(new IndustryProfile());

            // Set Fields
            profile.setUser(user);
            profile.setCompanyName(companyName);
            profile.setSector(sector);
            profile.setAddress(address);
            profile.setPhone(phone);
            profile.setEmail(email);
            profile.setDescription(description);

            // Parse Scores
            profile.setScoreE(safeDouble(scoreE));
            profile.setScoreS(safeDouble(scoreS));
            profile.setScoreG(safeDouble(scoreG));
            profile.setScoreAvg(safeDouble(scoreAvg));

            // Handle File Upload
            if (file != null && !file.isEmpty()) {
                System.out.println("Processing file: " + file.getOriginalFilename());
                profile.setReportFileName(file.getOriginalFilename());
                profile.setReportFile(file.getBytes());
            }

            profileRepo.save(profile);
            return ResponseEntity.ok("Profile Published Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    private double safeDouble(String val) {
        try { return Double.parseDouble(val); } catch (Exception e) { return 0.0; }
    }

    // 4. Download Report
    @GetMapping("/download-report/{id}")
    public ResponseEntity<byte[]> downloadProfileReport(@PathVariable Long id) {
        IndustryProfile profile = profileRepo.findById(id).orElse(null);
        if (profile == null || profile.getReportFile() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + profile.getReportFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(profile.getReportFile());
    }

    // 5. Delete
    @DeleteMapping("/publish")
    public ResponseEntity<?> deleteProfile(Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        profileRepo.findByUser(user).ifPresent(profileRepo::delete);
        return ResponseEntity.ok("Deleted");
    }
}