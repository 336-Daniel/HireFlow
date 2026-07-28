package com.uti.vacanteservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vacantes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vacante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Este dato se extrae del JWT (claim "preferred_username"), nunca del body
    @Column(name = "reclutador_username", nullable = false)
    private String reclutadorUsername;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "requisitos", columnDefinition = "TEXT")
    private String requisitos;

    // Permite al reclutador "cerrar" la vacante sin borrarla
    @Column(name = "activa", nullable = false)
    @Builder.Default
    private boolean activa = true;
}
