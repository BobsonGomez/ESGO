package com.esgo.backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;
    private String email;
    private String username;
    private String password;
    private String role;
    private boolean hasGeneratedReport = false;

    @Lob
    @JsonIgnore // Prevents the heavy image data from being sent in the /me endpoint
    @Column(length = 10000000) // Increased to ~10MB to prevent save failures
    private byte[] profilePicture;

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean hasGeneratedReport() { return hasGeneratedReport; }
    public void setHasGeneratedReport(boolean hasGeneratedReport) {
        this.hasGeneratedReport = hasGeneratedReport;
    }

    public byte[] getProfilePicture() { return profilePicture; }
    public void setProfilePicture(byte[] profilePicture) { this.profilePicture = profilePicture; }
}