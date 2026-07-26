package com.uti.vacanteservice.dto;

import jakarta.validation.constraints.NotBlank;


public record VacanteRequest(

        @NotBlank(message = "El titulo de la vacante es obligatorio.")
        String titulo,

        @NotBlank(message = "La descripcion de la vacante es obligatoria.")
        String descripcion,

        @NotBlank(message = "Los requisitos de la vacante son obligatorios.")
        String requisitos

) {}
