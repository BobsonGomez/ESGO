package com.esgo.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "reports")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportName; // e.g. "Q4 2024 ESG Report"
    private String reportType; // e.g. "Comprehensive"
    private LocalDate createdDate;

    // We store the huge form data as a text string (JSON)
    // This allows us to re-generate the Word Doc later
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String reportDataJson;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore // Prevent infinite recursion when sending JSON
    private User user;
}

