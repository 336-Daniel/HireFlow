package com.uti.candidatoservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidatos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "candidato_username", nullable = false, unique = true)
    private String candidatoUsername;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "cv_text", columnDefinition = "TEXT")
    private String cvText;

    @Column(name = "main_skills")
    private String mainSkills;

    @Column(name = "anios_exp", nullable = false)
    private String aniosExp;


}
