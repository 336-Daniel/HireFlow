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


    @Bean(name = "candidatoWebClient")
    public WebClient candidatoWebClient() {
        return WebClient.builder()
                .baseUrl(candidatoServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }


    @Bean(name = "vacanteWebClient")
    public WebClient vacanteWebClient() {
        return WebClient.builder()
                .baseUrl(vacanteServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
