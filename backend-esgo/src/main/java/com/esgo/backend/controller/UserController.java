package com.esgo.backend.controller;

import com.esgo.backend.model.User;
import com.esgo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Endpoint to get the currently logged-in user's details
    @GetMapping("/me")
    public ResponseEntity<User> getMyDetails(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to update user details (fullname and profile picture)
    @Transactional
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(Principal principal,
                                           @RequestParam("fullname") String fullname,
                                           @RequestParam(value = "pfp", required = false) MultipartFile profilePictureFile) throws IOException {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullname(fullname);

        if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
            user.setProfilePicture(profilePictureFile.getBytes());
        }

        userRepository.save(user);

        return ResponseEntity.ok("Profile updated successfully");
    }

    // Endpoint to serve a user's profile picture
    @GetMapping(value = "/{username}/pfp")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || user.getProfilePicture() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Serves as image
                .body(user.getProfilePicture());
    }
}