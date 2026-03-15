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
import java.util.Base64;
import com.esgo.backend.dto.PublishProfileRequest;

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

    // 3. PUBLISH (With Base64 File Upload)
    @PostMapping(value = "/publish", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> publishProfile(
            Principal principal,
            @RequestBody PublishProfileRequest request
    ) {
        try {
            User user = userRepo.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            IndustryProfile profile = profileRepo.findByUser(user).orElse(new IndustryProfile());

            // Set Fields
            profile.setUser(user);
            profile.setCompanyName(request.getCompanyName());
            profile.setSector(request.getSector());
            profile.setAddress(request.getAddress());
            profile.setPhone(request.getPhone());
            profile.setEmail(request.getEmail());
            profile.setDescription(request.getDescription());

            // Parse Scores
            profile.setScoreE(safeDouble(request.getScoreE()));
            profile.setScoreS(safeDouble(request.getScoreS()));
            profile.setScoreG(safeDouble(request.getScoreG()));
            profile.setScoreAvg(safeDouble(request.getScoreAvg()));

            // Handle Base64 File Upload
            if (request.getReportFileBase64() != null && !request.getReportFileBase64().isEmpty()) {
                System.out.println("Processing file: " + request.getReportFileName());

                String base64Data = request.getReportFileBase64();
                // JS sends data like "data:application/pdf;base64,JVBERi0xLjcKC..."
                // We must strip the prefix before decoding
                if (base64Data.contains(",")) {
                    base64Data = base64Data.split(",")[1];
                }

                byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
                profile.setReportFileName(request.getReportFileName());
                profile.setReportFile(decodedBytes);
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