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
    private String location;

    private int scoreE;
    private int scoreS;
    private int scoreG;
    private int scoreAvg;

    // --- NEW: File Storage ---
    private String reportFileName;

    @Lob // Large Object for storing file data
    @Column(length = 10000000) // Allow large files
    private byte[] reportFile;

    // --- Link to User (One User = One Profile) ---
    @OneToOne
    @JoinColumn(name = "user_id", unique = true) // Unique ensures 1-to-1
    private User user;
}