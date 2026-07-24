package com.uti.candidatoservice.repository;

import com.uti.candidatoservice.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    // Para buscar el perfil de un candidato por su username
    Optional<Candidato> findByCandidatoUsername(String candidatoUsername);

    // Regla de negocio: un username solo puede tener un perfil
    boolean existsByCandidatoUsername(String candidatoUsername);
}
