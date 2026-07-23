package com.uti.matchservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vacante_id", nullable = false)
    private Long vacanteId;

    @Column(name = "candidato_username", nullable = false)
    private String candidatoUsername;

    @Column(name = "application_Date", nullable = false)
    private LocalDateTime applicationDate;

    @Column(name = "ia_match_score")
    private Integer iaMatchScore;

    @Column(name = "ia_feedback", columnDefinition = "TEXT")
    private String iaFeedback;

    @PrePersist
    protected void onCreate() {
        this.applicationDate = LocalDateTime.now();
    }

}
