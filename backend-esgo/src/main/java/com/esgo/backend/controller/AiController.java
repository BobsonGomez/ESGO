package com.esgo.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/analyze-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> analyzeFile(@RequestParam("file") MultipartFile file) throws IOException {

        // 1. Prepare to send file to Python
        String pythonUrl = "http://localhost:5000/analyze-file";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // --- CRITICAL FIX STARTS HERE ---
        // We must override getFilename(), otherwise Spring sends it as a nameless blob
        // and Python receives 0 bytes.
        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                // Returns "Tata_Report.pdf" instead of null
                return file.getOriginalFilename();
            }
        };
        // --- CRITICAL FIX ENDS HERE ---

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileAsResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            // 2. Call Python
            ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl, requestEntity, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            // Pass the actual error back to frontend
            return ResponseEntity.internalServerError().body("AI Engine Error: " + e.getMessage());
        }
    }
}