package com.uti.matchservice.service;

import com.uti.matchservice.dto.MatchHistorialResponse;
import com.uti.matchservice.dto.MatchRequest;
import com.uti.matchservice.dto.MatchResponse;

import java.util.List;

public interface MatchService {


    // CREAR POSTULACIÓN (Flujo del Candidato + IA)
    MatchResponse createMatch(MatchRequest request, String candidatoUsername);

    //Devuelve la lista de candidatos que aplicaron a una vacante específica.
    List<MatchResponse> getMatchesByVacanteId(Long vacanteId);

    //Permite a un usuario ver a qué ofertas ha aplicado
    List<MatchHistorialResponse> getMatchesByCandidatoUsername(String candidatoUsername);

    // Por si se necesita buscar una postulación exacta por su ID
    MatchResponse getMatchById(Long matchId);

}