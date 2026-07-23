package com.uti.matchservice.client;

import com.uti.matchservice.dto.VacanteResponse;
import com.uti.matchservice.exception.VacanteServiceException;
import com.uti.matchservice.exception.ResourceNotfoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class VacanteWebClient {

    private final WebClient webClient;


    public VacanteWebClient(@Qualifier("vacanteWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public VacanteResponse getVacanteById(Long vacanteId) {
        log.info("WebClient - llamando vacante-service: GET /api/v1/vacantes/{}", vacanteId);
        try {
            return webClient
                    .get()
                    .uri("/api/v1/vacantes/{id}", vacanteId)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404,
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new ResourceNotfoundException(
                                            "Vacante no encontrada en el vacante-service con id: " + vacanteId
                                    ))
                    )
                    .onStatus(
                            status -> status.is4xxClientError(),
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new VacanteServiceException(
                                            "Error de cliente desde vacante-service: " + body
                                    ))
                    )
                    .onStatus(
                            status -> status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new VacanteServiceException(
                                            "Error de servidor desde vacante-service: " + body
                                    ))
                    )
                    .bodyToMono(VacanteResponse.class)
                    .block();

        } catch (WebClientResponseException ex) {
            log.error("WebClient - Error HTTP desde vacante-service: {} {}", ex.getStatusCode(), ex.getMessage());
            throw new VacanteServiceException(
                    "Error al llamar el vacante-service: " + ex.getMessage(), ex);

        } catch (Exception ex) {
            log.error("WebClient - no se logró conectar con vacante-service: {}", ex.getMessage());
            throw new VacanteServiceException(
                    "No se logró conectar a vacante-service: " + ex.getMessage(), ex);
        }
    }
}