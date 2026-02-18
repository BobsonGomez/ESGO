package com.esgo.backend.controller;

import com.esgo.backend.model.IndustryProfile;
import com.esgo.backend.model.User;
import com.esgo.backend.repository.IndustryProfileRepository;
import com.esgo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // --- 1. Get All Industries (For Investor Grid) ---
    @GetMapping("/industries")
    public ResponseEntity<List<IndustryProfile>> getAllIndustries() {
        List<IndustryProfile> profiles = profileRepo.findAll();

        // Sort by Score Average Descending (Highest score first)
        profiles.sort((p1, p2) -> Double.compare(p2.getScoreAvg(), p1.getScoreAvg()));

        return ResponseEntity.ok(profiles);
    }

    // --- 2. Get Current User's Profile (For Editing) ---
    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile(Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(403).body("User not found");

        Optional<IndustryProfile> profile = profileRepo.findByUser(user);

        // If profile exists, return it. If not, return 204 No Content
        return profile.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping(value = "/publish", consumes = {"multipart/form-data"})
    public ResponseEntity<?> publishProfile(
            Principal principal,
            @RequestParam("companyName") String companyName,
            @RequestParam("sector") String sector,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam(value = "description", required = false) String description,

            // 1. Accept Double (Decimals)
            @RequestParam("scoreE") Double e,
            @RequestParam("scoreS") Double s,
            @RequestParam("scoreG") Double g,
            @RequestParam("scoreAvg") Double avg,

            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {

        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        IndustryProfile profile = profileRepo.findByUser(user).orElse(new IndustryProfile());

        profile.setUser(user);
        profile.setCompanyName(companyName);
        profile.setSector(sector);
        profile.setAddress(address);
        profile.setPhone(phone);
        profile.setEmail(email);
        profile.setDescription(description);

        // 2. Save directly as Double (No rounding needed anymore)
        profile.setScoreE(e);
        profile.setScoreS(s);
        profile.setScoreG(g);
        profile.setScoreAvg(avg);

        if (file != null && !file.isEmpty()) {
            profile.setReportFileName(file.getOriginalFilename());
            profile.setReportFile(file.getBytes());
        }

        profileRepo.save(profile);
        return ResponseEntity.ok("Profile Published/Updated Successfully");
    }

    // --- NEW: Download Profile Report Endpoint ---
    @GetMapping("/download-report/{id}")
    public ResponseEntity<byte[]> downloadProfileReport(@PathVariable Long id) {
        IndustryProfile profile = profileRepo.findById(id).orElse(null);
        if (profile == null || profile.getReportFile() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + profile.getReportFileName() + "\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                .body(profile.getReportFile());
    }

    // --- 4. Delete Profile ---
    @DeleteMapping("/publish")
    public ResponseEntity<?> deleteProfile(Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        Optional<IndustryProfile> profile = profileRepo.findByUser(user);

        if (profile.isPresent()) {
            profileRepo.delete(profile.get());
            return ResponseEntity.ok("Profile deleted");
        }
        return ResponseEntity.badRequest().body("No profile to delete");
    }
}