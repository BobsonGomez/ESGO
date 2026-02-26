package com.esgo.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

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
}