package com.uti.matchservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${candidato.service.url}")
    private String candidatoServiceUrl;

    @Value("${vacante.service.url}")
    private String vacanteServiceUrl;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;



    @Bean(name = "candidatoWebClientBean")
    public WebClient candidatoWebClient() {
        return WebClient.builder()
                .baseUrl(candidatoServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean(name = "vacanteWebClientBean")
    public WebClient vacanteWebClient() {
        return WebClient.builder()
                .baseUrl(vacanteServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    // Bean para la Inteligencia Artificial
    @Bean(name = "geminiWebClient")
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl(geminiApiUrl)
                .defaultHeader("Content-Type", "application/json")
                // Inyectamos la llave de Google en los headers por seguridad
                .defaultHeader("x-goog-api-key", geminiApiKey)
                .build();
    }
}