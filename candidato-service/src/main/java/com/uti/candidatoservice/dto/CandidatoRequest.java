package com.uti.candidatoservice.dto;

import jakarta.validation.constraints.NotBlank;


public record CandidatoRequest(

        @NotBlank(message = "El nombre completo es obligatorio.")
        String fullName,

        @NotBlank(message = "El texto del CV es obligatorio.")
        String cvText,

        @NotBlank(message = "Las habilidades principales son obligatorias.")
        String mainSkills,

        @NotBlank(message = "Los años de experiencia son necesarios")
        String aniosExp





) {}
