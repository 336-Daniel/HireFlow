package com.uti.candidatoservice.dto;

// IMPORTANTE: match-service necesitara un record identico a este
// (mismos campos) en com.uti.matchservice.dto.CandidatoResponse
// para poder deserializar la respuesta de este microservicio.
public record CandidatoResponse(
        Long id,
        String candidatoUsername,
        String fullName,
        String cvText,
        String mainSkills
) {}
