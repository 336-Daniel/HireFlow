package com.uti.vacanteservice.dto;

public record VacanteResponse(
        Long id,
        String reclutadorUsername,
        String titulo,
        String descripcion,
        String requisitos,
        boolean activa
) {}
