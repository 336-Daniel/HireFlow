package com.uti.authservice.dto;

// DTO de salida: lo que el cliente recibe tras un login exitoso
public record TokenResponseDto(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
