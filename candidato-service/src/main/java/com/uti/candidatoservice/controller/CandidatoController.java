package com.uti.candidatoservice.controller;

import com.uti.candidatoservice.dto.CandidatoRequest;
import com.uti.candidatoservice.dto.CandidatoResponse;
import com.uti.candidatoservice.service.CandidatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidatos")
@RequiredArgsConstructor
public class CandidatoController {

    private final CandidatoService candidatoService;

    @PostMapping
    public ResponseEntity<CandidatoResponse> createProfile(
            @Valid @RequestBody CandidatoRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        CandidatoResponse response = candidatoService.createProfile(request, extractUsername(jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<CandidatoResponse> updateProfile(
            @Valid @RequestBody CandidatoRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        CandidatoResponse response = candidatoService.updateProfile(request, extractUsername(jwt));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<CandidatoResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        CandidatoResponse response = candidatoService.getMyProfile(extractUsername(jwt));
        return ResponseEntity.ok(response);
    }

    // Usado por RECLUTADOR (ver perfil de quien postulo) y por match-service (obtener cvText/mainSkills para la IA)
    @GetMapping("/{username}")
    public ResponseEntity<CandidatoResponse> getCandidatoByUsername(@PathVariable String username) {
        CandidatoResponse response = candidatoService.getCandidatoByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CandidatoResponse>> getAllCandidatos() {
        return ResponseEntity.ok(candidatoService.getAllCandidatos());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMyProfile(@AuthenticationPrincipal Jwt jwt) {
        candidatoService.deleteMyProfile(extractUsername(jwt));
        return ResponseEntity.noContent().build();
    }

    // El username nunca viene del body ni de la URL en las acciones "propias": siempre del JWT
    private String extractUsername(Jwt jwt) {
        return jwt.getClaimAsString("preferred_username");
    }
}
