package com.uti.matchservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MatchRequest(

        @NotNull(message = "El ID de la vacante es obligatorio.")
        @Min(value = 1, message = "El ID de la vacante debe ser mayor a 0.")
        Long vacanteId

)
{}