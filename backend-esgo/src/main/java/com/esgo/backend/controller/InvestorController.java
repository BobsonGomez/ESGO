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
import java.util.List; // <--- MAKE SURE THIS IMPORT IS HERE
import java.util.Optional;

@RestController
@RequestMapping("/api/investor")
@CrossOrigin(origins = "*")
public class InvestorController {

    @Autowired
    private IndustryProfileRepository profileRepo;

    @Autowired
    private UserRepository userRepo;

    // --- 4. NEW: Get All Industries (This was missing!) ---
    @GetMapping("/industries")
    public ResponseEntity<List<IndustryProfile>> getAllIndustries() {
        // This returns ALL profiles for the Investor Grid
        return ResponseEntity.ok(profileRepo.findAll());
    }

    // 1. Get Current User's Profile (For Editing)
    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile(Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(403).body("User not found");

        Optional<IndustryProfile> profile = profileRepo.findByUser(user);
        return profile.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    // 2. Publish or Update Profile (With File)
    @PostMapping(value = "/publish", consumes = {"multipart/form-data"})
    public ResponseEntity<?> publishProfile(
            Principal principal,
            @RequestParam("companyName") String companyName,
            @RequestParam("sector") String sector,
            @RequestParam("location") String location,
            @RequestParam("scoreE") int e,
            @RequestParam("scoreS") int s,
            @RequestParam("scoreG") int g,
            @RequestParam("scoreAvg") int avg,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {

        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        IndustryProfile profile = profileRepo.findByUser(user).orElse(new IndustryProfile());

        profile.setUser(user);
        profile.setCompanyName(companyName);
        profile.setSector(sector);
        profile.setLocation(location);
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

    // 3. Delete Profile
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