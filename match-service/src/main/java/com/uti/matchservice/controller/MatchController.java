package com.uti.matchservice.controller;

import com.uti.matchservice.dto.MatchRequest;
import com.uti.matchservice.dto.MatchResponse;
import com.uti.matchservice.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;


    @PostMapping
    public ResponseEntity<MatchResponse> createMatch(
            @Valid @RequestBody MatchRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        // extraemos el usuario directamente del token de Keycloak
        String candidatoUsername = jwt.getClaimAsString("preferred_username");

        MatchResponse created = matchService.createMatch(request, candidatoUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<MatchResponse>> getHistorialCandidato(
            @AuthenticationPrincipal Jwt jwt) {
        // el candidato solo puede ver sus propias postulaciones
        String candidatoUsername = jwt.getClaimAsString("preferred_username");

        List<MatchResponse> responses = matchService.getMatchesByCandidatoUsername(candidatoUsername);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/vacante/{vacanteId}")
    public ResponseEntity<List<MatchResponse>> getMatchesByVacante(
            @PathVariable Long vacanteId) {

        List<MatchResponse> responses = matchService.getMatchesByVacanteId(vacanteId);
        return ResponseEntity.ok(responses);
    }

    // Ver detalle específico
    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> getMatchById(
            @PathVariable Long id) {

        MatchResponse response = matchService.getMatchById(id);
        return ResponseEntity.ok(response);
    }
}