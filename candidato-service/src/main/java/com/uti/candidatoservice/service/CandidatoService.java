package com.uti.candidatoservice.service;

import com.uti.candidatoservice.dto.CandidatoRequest;
import com.uti.candidatoservice.dto.CandidatoResponse;

import java.util.List;

public interface CandidatoService {

    // CREAR PERFIL (una sola vez por username, el username viene del JWT)
    CandidatoResponse createProfile(CandidatoRequest request, String candidatoUsername);

    // ACTUALIZAR MI PROPIO PERFIL (el username viene del JWT)
    CandidatoResponse updateProfile(CandidatoRequest request, String candidatoUsername);

    // VER MI PROPIO PERFIL
    CandidatoResponse getMyProfile(String candidatoUsername);

    // VER EL PERFIL DE UN CANDIDATO POR USERNAME (usado por RECLUTADOR y por match-service)
    CandidatoResponse getCandidatoByUsername(String candidatoUsername);

    // LISTAR TODOS LOS CANDIDATOS (util para pruebas)
    List<CandidatoResponse> getAllCandidatos();

    // ELIMINAR MI PROPIO PERFIL (el username viene del JWT)
    void deleteMyProfile(String candidatoUsername);
}