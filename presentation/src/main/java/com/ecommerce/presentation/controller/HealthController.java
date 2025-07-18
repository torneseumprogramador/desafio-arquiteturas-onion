package com.ecommerce.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        
        // Status da aplicação
        status.put("application", "UP");
        status.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Verificar conexão com banco de dados
        Map<String, Object> database = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) { // timeout de 5 segundos
                database.put("status", "UP");
                database.put("database", "Oracle XE");
                database.put("url", connection.getMetaData().getURL());
                database.put("driver", connection.getMetaData().getDriverName());
                database.put("version", connection.getMetaData().getDatabaseProductVersion());
            } else {
                database.put("status", "DOWN");
                database.put("error", "Connection is not valid");
            }
        } catch (SQLException e) {
            database.put("status", "DOWN");
            database.put("error", e.getMessage());
        }
        
        status.put("database", database);
        
        // Determinar status geral
        boolean allUp = "UP".equals(database.get("status"));
        healthStatus.put("status", allUp ? "UP" : "DOWN");
        healthStatus.put("details", status);
        
        return ResponseEntity.status(allUp ? 200 : 503).body(healthStatus);
    }
    
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "pong");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> databaseHealth() {
        Map<String, Object> databaseStatus = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                databaseStatus.put("status", "UP");
                databaseStatus.put("database", "Oracle XE");
                databaseStatus.put("url", connection.getMetaData().getURL());
                databaseStatus.put("driver", connection.getMetaData().getDriverName());
                databaseStatus.put("version", connection.getMetaData().getDatabaseProductVersion());
                databaseStatus.put("username", connection.getMetaData().getUserName());
                databaseStatus.put("readOnly", connection.isReadOnly());
                databaseStatus.put("autoCommit", connection.getAutoCommit());
                
                // Testar uma query simples
                try (var stmt = connection.createStatement()) {
                    stmt.execute("SELECT 1 FROM DUAL");
                    databaseStatus.put("queryTest", "SUCCESS");
                } catch (SQLException e) {
                    databaseStatus.put("queryTest", "FAILED");
                    databaseStatus.put("queryError", e.getMessage());
                }
            } else {
                databaseStatus.put("status", "DOWN");
                databaseStatus.put("error", "Connection is not valid");
            }
        } catch (SQLException e) {
            databaseStatus.put("status", "DOWN");
            databaseStatus.put("error", e.getMessage());
            databaseStatus.put("errorCode", e.getErrorCode());
            databaseStatus.put("sqlState", e.getSQLState());
        }
        
        return ResponseEntity.status("UP".equals(databaseStatus.get("status")) ? 200 : 503)
                           .body(databaseStatus);
    }
} 