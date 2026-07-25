package com.uti.matchservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uti.matchservice.dto.gemini.GeminiRequest;
import com.uti.matchservice.dto.gemini.GeminiResponse;
import com.uti.matchservice.exception.GeminiApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
@Slf4j
public class GeminiApiClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    // Spring inyecta automáticamente el WebClient de Gemini y el ObjectMapper
    public GeminiApiClient(@Qualifier("geminiWebClient") WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public IaEvaluation evaluateMatch(String perfilCandidato, String descripcionVacante) {
        log.info("Llamando a la IA de Gemini para evaluar candidato vs vacante...");

        // 1. Armamos el PROMPT estricto
        String prompt = String.format(
                "Eres un experto en Recursos Humanos. Evalúa qué tan bien se adapta el siguiente candidato a la vacante.\n" +
                        "Perfil del Candidato: %s\n" +
                        "Descripción de la Vacante: %s\n" +
                        "Responde EXCLUSIVAMENTE con un objeto JSON válido con esta estructura exacta (sin comillas invertidas, sin formato markdown y sin texto extra):\n" +
                        "{\"score\": 85, \"feedback\": \"Tu explicación breve del por qué de la nota\"}",
                perfilCandidato, descripcionVacante
        );

        // 2. Construimos el cuerpo de la petición basándonos en los DTOs que creamos
        GeminiRequest.Part part = new GeminiRequest.Part(prompt);
        GeminiRequest.Content content = new GeminiRequest.Content(List.of(part));
        GeminiRequest requestBody = new GeminiRequest(List.of(content));

        try {
            // 3. Hacemos la llamada HTTP a Google
            GeminiResponse response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .block();

            // 4. Extraemos y procesamos la respuesta
            if (response != null && !response.candidates().isEmpty()) {
                String responseText = response.candidates().get(0).content().parts().get(0).text();

                // Limpieza de seguridad: a veces las IA envuelven el JSON en bloques de código markdown ```json ... ```
                responseText = responseText.replaceAll("```json", "").replaceAll("```", "").trim();

                // 5. Convertimos el texto JSON a variables de Java
                JsonNode jsonNode = objectMapper.readTree(responseText);
                int score = jsonNode.get("score").asInt();
                String feedback = jsonNode.get("feedback").asText();

                return new IaEvaluation(score, feedback);
            } else {
                throw new GeminiApiException("La respuesta de Gemini llegó vacía.");
            }

        } catch (WebClientResponseException ex) {
            log.error("Error HTTP al llamar a Gemini: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new GeminiApiException("Error de comunicación con la API de Gemini", ex);
        } catch (Exception ex) {
            log.error("Error procesando la evaluación de Gemini: {}", ex.getMessage());
            throw new GeminiApiException("Error interno al interpretar la respuesta de la IA", ex);
        }
    }

    // Record interno para devolver los dos valores (score y feedback) empaquetados
    public record IaEvaluation(Integer score, String feedback) {}
}