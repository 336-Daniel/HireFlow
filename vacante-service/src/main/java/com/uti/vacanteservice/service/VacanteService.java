package com.uti.vacanteservice.service;

import com.uti.vacanteservice.dto.VacanteRequest;
import com.uti.vacanteservice.dto.VacanteResponse;

import java.util.List;

public interface VacanteService {

    // PUBLICAR VACANTE (el reclutadorUsername viene del JWT)
    VacanteResponse createVacante(VacanteRequest request, String reclutadorUsername);

    // ACTUALIZAR VACANTE EXISTENTE (solo el dueño puede hacerlo)
    VacanteResponse updateVacante(Long id, VacanteRequest request, String reclutadorUsername);

    // VER EL DETALLE DE UNA VACANTE POR ID
    VacanteResponse getVacanteById(Long id);

    // LISTAR TODAS LAS VACANTES (util para pruebas mientras no hay frontend)
    List<VacanteResponse> getAllVacantes();

    // LISTAR SOLO LAS VACANTES ACTIVAS (lo que ve el CANDIDATO)
    List<VacanteResponse> getVacantesActivas();

    // LISTAR LAS VACANTES PUBLICADAS POR UN RECLUTADOR
    List<VacanteResponse> getVacantesByReclutador(String reclutadorUsername);

    // CERRAR VACANTE (solo el dueño puede hacerlo)
    VacanteResponse cerrarVacante(Long id, String reclutadorUsername);

    // LISTAR SOLO LAS VACANTES ACTIVAS DE UN RECLUTADOR (busqueda pública del candidato)
    List<VacanteResponse> getVacantesActivasByReclutador(String reclutadorUsername);

    // ELIMINAR VACANTE (solo el dueño puede hacerlo)
    void deleteVacante(Long id, String reclutadorUsername);
}
