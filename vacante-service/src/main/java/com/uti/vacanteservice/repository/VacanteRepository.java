package com.uti.vacanteservice.repository;

import com.uti.vacanteservice.model.Vacante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacanteRepository extends JpaRepository<Vacante, Long> {

    // el candidsto solo vea las vacantes abiertas
    List<Vacante> findByActivaTrue();

    // el reclutador vea las vacantes que el mismo publico
    List<Vacante> findByReclutadorUsername(String reclutadorUsername);

    // busqueda publica: el candidato ve solo las vacantes activas de un reclutador especifico
    List<Vacante> findByReclutadorUsernameAndActivaTrue(String reclutadorUsername);
}
