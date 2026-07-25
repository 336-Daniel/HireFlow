package com.uti.vacanteservice.service;

import com.uti.vacanteservice.dto.VacanteRequest;
import com.uti.vacanteservice.dto.VacanteResponse;

import java.util.List;

public interface VacanteService {

    // PUBLICAR VACANTE (el reclutador puede publicar varias)
    VacanteResponse createVacante(VacanteRequest request);

    // ACTUALIZAR VACANTE EXISTENTE
    VacanteResponse updateVacante(Long id, VacanteRequest request);

    // VER EL DETALLE DE UNA VACANTE POR ID
    VacanteResponse getVacanteById(Long id);

    // LISTAR TODAS LAS VACANTES (util para pruebas mientras no hay frontend)
    List<VacanteResponse> getAllVacantes();

    // LISTAR SOLO LAS VACANTES ACTIVAS (lo que ve el CANDIDATO)
    List<VacanteResponse> getVacantesActivas();

    // LISTAR LAS VACANTES PUBLICADAS POR UN RECLUTADOR
    List<VacanteResponse> getVacantesByReclutador(String reclutadorUsername);

    // CERRAR VACANTE (deja de ser visible para los candidatos, sin borrarla)
    VacanteResponse cerrarVacante(Long id);

    // ELIMINAR VACANTE
    void deleteVacante(Long id);
}
