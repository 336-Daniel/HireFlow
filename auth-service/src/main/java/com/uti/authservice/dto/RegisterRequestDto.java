package com.uti.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// DTO de entrada para el auto-registro publico.
// A diferencia del proyecto de referencia (donde el rol PATIENT era fijo), en HireFlow
// el usuario SI elige su rol al registrarse, porque tanto CANDIDATO como RECLUTADOR
// son roles de auto-servicio (ninguno es un rol elevado como ADMIN).
public record RegisterRequestDto(

        @NotBlank(message = "El username es obligatorio")
        String username,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato valido")
        String email,

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotBlank(message = "La contrasena es obligatoria")
        @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "El rol es obligatorio")
        @Pattern(regexp = "^(CANDIDATO|RECLUTADOR)$", message = "El rol debe ser CANDIDATO o RECLUTADOR")
        String role
) {}
