package com.uti.candidatoservice.controller;

import com.uti.candidatoservice.dto.CandidatoRequest;
import com.uti.candidatoservice.dto.CandidatoResponse;
import com.uti.candidatoservice.service.CandidatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidatos")
@RequiredArgsConstructor
public class CandidatoController {

    private final CandidatoService candidatoService;

    @PostMapping
    public ResponseEntity<CandidatoResponse> createProfile(@Valid @RequestBody CandidatoRequest request) {
        CandidatoResponse response = candidatoService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // TODO: cuando se integre Keycloak, {username} se reemplazara por el username
    // extraido del JWT, para que un candidato solo pueda editar su propio perfil.
    @PutMapping("/{username}")
    public ResponseEntity<CandidatoResponse> updateProfile(
            @PathVariable String username,
            @Valid @RequestBody CandidatoRequest request) {
        CandidatoResponse response = candidatoService.updateProfile(username, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<CandidatoResponse> getCandidatoByUsername(@PathVariable String username) {
        CandidatoResponse response = candidatoService.getCandidatoByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CandidatoResponse>> getAllCandidatos() {
        return ResponseEntity.ok(candidatoService.getAllCandidatos());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteCandidato(@PathVariable String username) {
        candidatoService.deleteCandidato(username);
        return ResponseEntity.noContent().build();
    }
}
