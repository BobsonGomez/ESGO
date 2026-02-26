package com.esgo.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "industry_profiles")
@Data
public class IndustryProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String sector;

    // --- UPDATED FIELDS ---
    private String address;
    private String phone;
    private String email;

    private double scoreE;
    private double scoreS;
    private double scoreG;
    private double scoreAvg;

    @Column(length = 5000) // Allow long text
    private String description;

    // --- NEW: File Storage ---
    private String reportFileName;

    @Lob // Large Object for storing file data
    @Column(length = 25000000) // Allow large files
    private byte[] reportFile;

    // --- Link to User (One User = One Profile) ---
    @OneToOne
    @JoinColumn(name = "user_id", unique = true) // Unique ensures 1-to-1
    private User user;
}