package com.uti.matchservice.repository;

import com.uti.matchservice.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    // 1. Listar postulaciones por vacante, ordenadas del mejor score al peor (descendente).
    // Esto hace que los candidatos con 85% o 90% aparezcan automáticamente arriba en la tabla del reclutador.
    List<Match> findByVacanteIdOrderByIaMatchScoreDesc(Long vacanteId);

    // 2. Buscar todas las postulaciones hechas por un candidato específico (historial personal).
    List<Match> findByCandidatoUsername(String candidatoUsername);

    // 3. Regla de negocio: Evita duplicados (valida si el candidato ya postuló a esa misma vacante).
    boolean existsByVacanteIdAndCandidatoUsername(Long vacanteId, String candidatoUsername);
}