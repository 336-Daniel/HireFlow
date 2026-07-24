package com.uti.matchservice.dto;

public record CandidatoResponse(
        Long id,
        String candidatoUsername,
        String fullName,
        String cvText,
        String mainSkills
) {}