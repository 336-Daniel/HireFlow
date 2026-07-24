package com.uti.candidatoservice.dto;

import jakarta.validation.constraints.NotBlank;

// TODO: cuando se integre Keycloak, quitar "candidatoUsername" de aqui.
// Ese dato pasara a obtenerse del JWT (claim "preferred_username") en el controller,
// nunca del cuerpo de la peticion (por seguridad).
public record CandidatoRequest(

        @NotBlank(message = "El username del candidato es obligatorio.")
        String candidatoUsername,

        @NotBlank(message = "El nombre completo es obligatorio.")
        String fullName,

        @NotBlank(message = "El texto del CV es obligatorio.")
        String cvText,

        @NotBlank(message = "Las habilidades principales son obligatorias.")
        String mainSkills

) {}
