package com.ecommerce.presentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce Onion Architecture API")
                        .description("API REST para sistema de e-commerce implementado com Onion Architecture (Arquitetura Cebola)")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Prof. Danilo Aparecido")
                                .email("suporte@torneseumprogramador.com.br")
                                .url("https://www.torneseumprogramador.com.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.ecommerce-onion.com")
                                .description("Servidor de Produção")
                ));
    }
} 