package com.uti.matchservice.dto;

import java.time.LocalDateTime;

public record MatchResponse(
        Long id,
        Long vacanteId,
        String candidatoUsername,
        LocalDateTime applicationDate,
        Integer iaMatchScore,
        String iaFeedback
) {
}