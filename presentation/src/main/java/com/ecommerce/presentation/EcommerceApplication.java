package com.ecommerce.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ecommerce")
@EntityScan("com.ecommerce.domain")
@EnableJpaRepositories("com.ecommerce.infrastructure.repository")
public class EcommerceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
} 