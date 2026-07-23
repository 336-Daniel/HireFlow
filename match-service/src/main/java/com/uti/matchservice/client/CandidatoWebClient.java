package com.uti.matchservice.client;

import com.uti.matchservice.dto.CandidatoResponse;
import com.uti.matchservice.exception.CandidatoServiceException;
import com.uti.matchservice.exception.ResourceNotfoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class CandidatoWebClient {

    private final WebClient webClient;

    // le decimos exactamente cuál WebClient del AppConfig usar
    public CandidatoWebClient(@Qualifier("candidatoWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public CandidatoResponse getCandidatoByUsername(String username) {
        log.info("WebClient - llamando candidato-service: GET /api/v1/candidatos/{}", username);
        try {
            return webClient
                    .get()
                    .uri("/api/v1/candidatos/{username}", username)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404,
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new ResourceNotfoundException(
                                            "Candidato no encontrado en el candidato-service con username: " + username
                                    ))
                    )
                    .onStatus(
                            status -> status.is4xxClientError(),
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new CandidatoServiceException(
                                            "Error de cliente desde candidato-service: " + body
                                    ))
                    )
                    .onStatus(
                            status -> status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new CandidatoServiceException(
                                            "Error de servidor desde candidato-service: " + body
                                    ))
                    )
                    .bodyToMono(CandidatoResponse.class)
                    .block();

        } catch (WebClientResponseException ex) {
            log.error("WebClient - Error HTTP desde candidato-service: {} {}", ex.getStatusCode(), ex.getMessage());
            throw new CandidatoServiceException(
                    "Error al llamar el candidato-service: " + ex.getMessage(), ex);

        } catch (Exception ex) {
            log.error("WebClient - no se logró conectar con candidato-service: {}", ex.getMessage());
            throw new CandidatoServiceException(
                    "No se logró conectar a candidato-service: " + ex.getMessage(), ex);
        }
    }
}