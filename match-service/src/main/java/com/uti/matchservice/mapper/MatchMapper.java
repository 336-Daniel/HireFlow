package com.uti.matchservice.mapper;

import com.uti.matchservice.dto.MatchHistorialResponse;
import com.uti.matchservice.dto.MatchRequest;
import com.uti.matchservice.dto.MatchResponse;
import com.uti.matchservice.dto.VacanteResponse;
import com.uti.matchservice.model.Match;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    // Convertir MatchRequest -> Match (Entidad)
    public Match toEntity(MatchRequest request, String candidatoUsername) {
         // Recibimos el username como un parámetro separado.
        return Match.builder()
                .vacanteId(request.vacanteId())
                .candidatoUsername(candidatoUsername)
                // iaMatchScore y iaFeedback quedan nulos hasta que la IA responda
                .build();
    }

    // Convertir Match -> MatchResponse
    public MatchResponse toResponse(Match match) {
        return new MatchResponse(
                match.getId(),
                match.getVacanteId(),
                match.getCandidatoUsername(),
                match.getApplicationDate(),
                match.getIaMatchScore(),
                match.getIaFeedback()
        );
    }

    // Convertir Match -> MatchHistorialResponse (vista del candidato, con info de la vacante)
    public MatchHistorialResponse toHistorialResponse(Match match, VacanteResponse vacante) {
        return new MatchHistorialResponse(
                match.getId(),
                match.getVacanteId(),
                match.getApplicationDate(),
                vacante != null ? vacante.titulo() : "Información de la vacante temporalmente no disponible",
                vacante != null ? vacante.descripcion() : "espere hasta que el servicio de vacantes regrese",
                vacante != null ? vacante.activa() : null
        );
    }
}