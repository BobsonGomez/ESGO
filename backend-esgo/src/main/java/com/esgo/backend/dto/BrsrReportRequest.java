package com.esgo.backend.dto;

import lombok.Data;

@Data
public class BrsrReportRequest {
    // --- SECTION A: GENERAL DISCLOSURES ---
    private String cin;
    private String companyName;
    private String yearOfInc;
    private String registeredAddress;
    private String corporateAddress;
    private String email;
    private String telephone;
    private String website;
    private String reportingYear; // e.g. "2023-24"
    private String paidUpCapital;

    // --- EMPLOYEE DETAILS (For Section A, Table 18) ---
    private String empMalePermanent;
    private String empFemalePermanent;
    private String empTotalPermanent;
    private String workerMalePermanent;
    private String workerFemalePermanent;
    private String workerTotalPermanent;

    // --- SECTION C: PRINCIPLE 6 (ENVIRONMENT) ---
    // Energy
    private String electricityConsumption;
    private String fuelConsumption;
    private String energyIntensity;

    // Emissions
    private String scope1Emissions;
    private String scope2Emissions;
    private String emissionIntensity;

    // Waste
    private String plasticWaste;
    private String eWaste;
    private String hazardousWaste;
    private String totalWasteGenerated;
    private String totalWasteRecycled;
}