package com.ecommerce.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "E-commerce Onion Architecture API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("architecture", "Onion Architecture");
        response.put("technology", "Java 23 + Spring Boot 3.4.0 + Oracle XE");
        
        // Endpoints da API
        Map<String, Object> apiEndpoints = new HashMap<>();
        apiEndpoints.put("users", "/api/users");
        apiEndpoints.put("products", "/api/products");
        apiEndpoints.put("orders", "/api/orders");
        response.put("api", apiEndpoints);
        
        // Endpoints de Health Check
        Map<String, Object> healthEndpoints = new HashMap<>();
        healthEndpoints.put("overall", "/health");
        healthEndpoints.put("ping", "/health/ping");
        healthEndpoints.put("database", "/health/database");
        response.put("health", healthEndpoints);
        
        // Documentação da API
        Map<String, Object> documentation = new HashMap<>();
        documentation.put("swagger-ui", "/swagger-ui/index.html");
        documentation.put("openapi-json", "/v3/api-docs");
        documentation.put("openapi-yaml", "/v3/api-docs.yaml");
        response.put("documentation", documentation);
        
        // Informações adicionais
        Map<String, Object> info = new HashMap<>();
        info.put("description", "Sistema de E-commerce com Onion Architecture");
        info.put("author", "Prof. Danilo Aparecido");
        info.put("course", "Arquiteturas de Software Modernas");
        info.put("platform", "Torne-se um Programador");
        response.put("info", info);
        
        return ResponseEntity.ok(response);
    }
}