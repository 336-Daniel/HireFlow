package com.uti.matchservice.dto;


public record VacanteResponse(
        Long id,
        String reclutadorUsername,
        String titulo,
        String descripcion,
        String requisitos,
        boolean activa
) {}

