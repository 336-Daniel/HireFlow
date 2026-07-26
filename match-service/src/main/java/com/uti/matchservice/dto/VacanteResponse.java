package com.uti.matchservice.dto;

import java.time.LocalDateTime;

public record VacanteResponse(
        Long id,
        String reclutadorUsername,
        String titulo,
        String descripcion,
        String requisitos,
        boolean activa
) {}

