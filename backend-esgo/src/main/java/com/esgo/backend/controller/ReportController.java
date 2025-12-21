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
import java.util.List;

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

        // Extract username from token (Remove "Bearer ")
        String username = jwtUtil.extractUsername(token.substring(7));

        // Save to DB first
        reportService.saveReportToDb(request, username);

        // Generate Word Doc
        ByteArrayInputStream bis = reportService.generateBrsrReport(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=BRSR_Report.docx");

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
}