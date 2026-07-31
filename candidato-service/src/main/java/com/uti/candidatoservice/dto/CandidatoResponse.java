package com.uti.candidatoservice.dto;


public record CandidatoResponse(
        Long id,
        String candidatoUsername,
        String fullName,
        String cvText,
        String mainSkills,
        String aniosExp
) {}
