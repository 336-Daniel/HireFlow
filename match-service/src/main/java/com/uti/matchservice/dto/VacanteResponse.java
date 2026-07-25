package com.uti.matchservice.dto;

import java.time.LocalDateTime;

public record VacanteResponse(
        Long id,
        String recruiterUsername,
        String jobTitle,
        String requirements,
        String status,
        LocalDateTime createdAt
) {
}
