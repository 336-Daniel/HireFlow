package com.uti.matchservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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

    // recibe los segundos como parámetro, para reusarlo con distintos timeouts
    private HttpClient httpClientConTimeout(int segundos) {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, segundos * 1000)
                .responseTimeout(Duration.ofSeconds(segundos))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(segundos, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(segundos, TimeUnit.SECONDS)));
    }

    @Bean(name = "candidatoWebClientBean")
    public WebClient candidatoWebClient() {
        return WebClient.builder()
                .baseUrl(candidatoServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClientConTimeout(3)))
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean(name = "vacanteWebClientBean")
    public WebClient vacanteWebClient() {
        return WebClient.builder()
                .baseUrl(vacanteServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClientConTimeout(3)))
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    // Gemini con timeout mas alto (10s), porque generar contenido con IA tarda mas que un GET normal
    @Bean(name = "geminiWebClient")
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl(geminiApiUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClientConTimeout(10)))
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("x-goog-api-key", geminiApiKey)
                .build();
    }
}