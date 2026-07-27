package com.uti.matchservice.dto;

import java.time.LocalDateTime;

// DTO de historial para el CANDIDATO - sin datos de evaluación de IA
public record MatchHistorialResponse(
        Long id,
        Long vacanteId,
        LocalDateTime applicationDate,
        String vacanteTitulo,
        String vacanteDescripcion,
        Boolean vacanteActiva
) {}