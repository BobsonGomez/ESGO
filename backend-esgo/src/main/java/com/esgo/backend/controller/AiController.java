package com.esgo.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @PostMapping("/analyze-file")
    public ResponseEntity<?> analyzeFile(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Save uploaded file to a temporary location
            Path tempFile = Files.createTempFile("esg_upload_", ".pdf");
            file.transferTo(tempFile.toFile());

            // 2. Locate the "ai-engine" folder (Sibling to backend-esgo)
            // We get the current directory (backend-esgo) and go to its parent
            File currentDir = new File(".");
            File aiEngineDir = new File(currentDir.getCanonicalFile().getParent(), "ai-engine");

            // Debugging: Print where we are looking
            System.out.println("Looking for AI Engine at: " + aiEngineDir.getAbsolutePath());

            if (!aiEngineDir.exists()) {
                return ResponseEntity.status(500).body("{\"error\": \"AI Engine folder not found. Expected at: " + aiEngineDir.getAbsolutePath() + "\"}");
            }

            // 3. Prepare the Python command
            ProcessBuilder pb = new ProcessBuilder(
                    "python",           // Change to "python3" if on Mac/Linux
                    "esg_inference.py",
                    tempFile.toAbsolutePath().toString()
            );

            // Set execution directory to ai-engine so it can find its models
            pb.directory(aiEngineDir);
            pb.redirectErrorStream(true);

            // 4. Run the process
            Process process = pb.start();

            // 5. Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            Files.deleteIfExists(tempFile);

            if (exitCode == 0) {
                return ResponseEntity.ok(output.toString());
            } else {
                return ResponseEntity.status(500).body("{\"error\": \"AI Engine Failed\", \"details\": \"" + output.toString().replace("\"", "'") + "\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\": \"Server Error: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/generate-esg")
    public ResponseEntity<?> generateContent(@RequestBody Map<String, String> payload) {
        try {
            // Use "prompt" or "keywords" based on what you send from JS
            String userPrompt = payload.get("prompt");

            String generatedText = callGeminiApi(userPrompt);

            // Clean the text: Remove any weird trailing newlines or extra quotes
            // that Gemini sometimes adds in raw mode
            generatedText = generatedText.trim();

            Map<String, String> response = new HashMap<>();
            response.put("text", generatedText);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    private String callGeminiApi(String userPrompt) throws Exception {
        String apiKey = "AIzaSyBqUOA9l0HY8Vo-4fvTsVjhdImkXjGO5F4";
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=" + apiKey;

        // Use a Map for the body to let Jackson handle escaping quotes in the userPrompt
        Map<String, Object> bodyMap = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", userPrompt)
                        ))
                )
        );

        String jsonInputString = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(bodyMap);

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        // PARSE the Gemini response properly
        // PARSE the Gemini response properly
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(response.body());

        // 1. Check if Google sent an error back
        if (root.has("error")) {
            String googleErrorMessage = root.path("error").path("message").asText();
            System.err.println("GEMINI API ERROR: " + googleErrorMessage);
            return "API Error: " + googleErrorMessage;
        }

        // 2. Path: candidates[0] -> content -> parts[0] -> text
        if (root.has("candidates") && root.get("candidates").size() > 0) {
            return root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();
        }

        return "Error: AI could not generate content. Raw response: " + response.body();
    }
}