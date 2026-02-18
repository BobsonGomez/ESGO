package com.esgo.backend.service;

import com.esgo.backend.dto.BrsrReportRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class AiGenerationService {

    // POINT THIS TO YOUR PYTHON SCRIPT
    private static final String PYTHON_SERVICE_URL = "http://localhost:5000/generate";

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public AiGenerationService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS) // Give Python time to think
                .build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public String generateReportContent(BrsrReportRequest reportData) throws IOException {
        // 1. Convert Java Object to JSON
        String jsonData = objectMapper.writeValueAsString(reportData);

        // 2. Send JSON to Python
        RequestBody body = RequestBody.create(
                jsonData,
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(PYTHON_SERVICE_URL)
                .post(body)
                .build();

        // 3. Get Response from Python
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Python Service Failed: " + response.code());
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            if (jsonResponse.getBoolean("success")) {
                return jsonResponse.getString("content");
            } else {
                throw new IOException("Python Error: " + jsonResponse.optString("error"));
            }
        }
    }
}