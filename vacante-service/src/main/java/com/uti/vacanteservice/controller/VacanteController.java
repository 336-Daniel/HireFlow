package com.uti.vacanteservice.controller;

import com.uti.vacanteservice.dto.VacanteRequest;
import com.uti.vacanteservice.dto.VacanteResponse;
import com.uti.vacanteservice.service.VacanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vacantes")
@RequiredArgsConstructor
public class VacanteController {

    private final VacanteService vacanteService;

    @PostMapping
    public ResponseEntity<VacanteResponse> createVacante(
            @Valid @RequestBody VacanteRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        VacanteResponse response = vacanteService.createVacante(request, extractUsername(jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // se valida que el reclutador del JWT sea el dueño de la vacante
    @PutMapping("/{id}")
    public ResponseEntity<VacanteResponse> updateVacante(
            @PathVariable Long id,
            @Valid @RequestBody VacanteRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        VacanteResponse response = vacanteService.updateVacante(id, request, extractUsername(jwt));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacanteResponse> getVacanteById(@PathVariable Long id) {
        VacanteResponse response = vacanteService.getVacanteById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VacanteResponse>> getAllVacantes() {
        return ResponseEntity.ok(vacanteService.getAllVacantes());
    }

    // EL CANDIDATO VERA al entrar a listar las vacantes disponibles
    @GetMapping("/activas")
    public ResponseEntity<List<VacanteResponse>> getVacantesActivas() {
        return ResponseEntity.ok(vacanteService.getVacantesActivas());
    }

    @GetMapping("/reclutador/{username}")
    public ResponseEntity<List<VacanteResponse>> getVacantesByReclutador(@PathVariable String username) {
        return ResponseEntity.ok(vacanteService.getVacantesByReclutador(username));
    }

    // solo el reclutador dueño de la vacante puede cerrarla
    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<VacanteResponse> cerrarVacante(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        VacanteResponse response = vacanteService.cerrarVacante(id, extractUsername(jwt));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacante(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        vacanteService.deleteVacante(id, extractUsername(jwt));
        return ResponseEntity.noContent().build();
    }

    // El username nunca viene del body ni de la URL en las acciones "propias": siempre del JWT
    private String extractUsername(Jwt jwt) {
        return jwt.getClaimAsString("preferred_username");
    }
}
