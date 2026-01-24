package com.esgo.backend.controller;

import com.esgo.backend.dto.BrsrReportRequest;
import com.esgo.backend.model.Report;
import com.esgo.backend.security.JwtUtil;
import com.esgo.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private JwtUtil jwtUtil;

    // 1. Generate, Save, and Download (The main button)
    @PostMapping("/generate")
    public ResponseEntity<InputStreamResource> generateAndSave(
            @RequestHeader("Authorization") String token,
            @RequestBody BrsrReportRequest request) throws IOException {

        String username = jwtUtil.extractUsername(token.substring(7));

        // 1. Save/Merge the new data (Part 2) into the DB
        Long savedReportId = reportService.saveReportToDb(request, username);

        // 2. CRITICAL STEP:
        // Throw away the 'request' object (which has nulls).
        // Fetch the COMPLETE data from the database (which has Part 1 + Part 2).
        BrsrReportRequest fullData = reportService.getReportDataById(savedReportId);

        // 3. Generate Word Doc using the FULL Data
        ByteArrayInputStream bis = reportService.generateBrsrReport(fullData);

        HttpHeaders headers = new HttpHeaders();
        // Use company name from DB data, fallback to "Draft"
        String cName = (fullData.getCompanyName() != null && !fullData.getCompanyName().isEmpty())
                ? fullData.getCompanyName() : "Draft";

        headers.add("Content-Disposition", "attachment; filename=BRSR_Report_" + cName + ".docx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bis));
    }

    // 2. Get List of Reports for Dashboard
    @GetMapping("/list")
    public List<Report> listReports(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        return reportService.getUserReports(username);
    }

    // 3. Re-Download an old report
    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadOldReport(@PathVariable Long id) throws IOException {

        // Get saved data from DB
        BrsrReportRequest savedData = reportService.getReportDataById(id);

        // Regenerate the file
        ByteArrayInputStream bis = reportService.generateBrsrReport(savedData);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=BRSR_Report_" + id + ".docx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bis));
    }

    // 4. Get full details of a specific report (for editing)
    @GetMapping("/{id}")
    public ResponseEntity<BrsrReportRequest> getReportDetails(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        BrsrReportRequest data = reportService.getReportDataById(id);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReport(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            // 1. Who is asking? (Extract username from token)
            String username = jwtUtil.extractUsername(token.substring(7));

            // 2. Pass both ID and Username to the service
            reportService.deleteReport(id, username);

            return ResponseEntity.ok("Report deleted successfully");

        } catch (RuntimeException e) {
            // If report belongs to someone else, return 403 Forbidden
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    // 5. SAVE DRAFT (Returns ID, does not download file)
    // --- NEW ENDPOINT FOR SAVING DRAFTS ---
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveDraft(
            @RequestHeader("Authorization") String token,
            @RequestBody BrsrReportRequest request) {

        String username = jwtUtil.extractUsername(token.substring(7));

        // Call the service and get the ID of the saved/updated report
        Long reportId = reportService.saveReportToDb(request, username);

        // Return a JSON response with the ID
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("reportId", reportId);

        return ResponseEntity.ok(response);
    }
}