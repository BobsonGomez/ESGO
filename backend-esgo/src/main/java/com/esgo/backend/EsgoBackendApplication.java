package com.esgo.backend;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.*;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EsgoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsgoBackendApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // --- FIX: Force File Upload Limits Programmatically ---
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // Allow up to 100MB per file
        factory.setMaxFileSize(DataSize.ofMegabytes(100));
        // Allow up to 100MB for the whole request data
        factory.setMaxRequestSize(DataSize.ofMegabytes(100));
        return factory.createMultipartConfig();
    }
}