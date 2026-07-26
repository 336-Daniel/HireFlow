package com.uti.authservice.dto;

import jakarta.validation.constraints.NotBlank;

// DTO de entrada para el login intermediado
public record LoginRequestDto(

        @NotBlank(message = "El username es obligatorio")
        String username,

        @NotBlank(message = "La contrasena es obligatoria")
        String password
) {}
