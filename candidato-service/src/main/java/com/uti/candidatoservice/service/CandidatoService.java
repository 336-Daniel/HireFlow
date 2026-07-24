package com.uti.candidatoservice.service;

import com.uti.candidatoservice.dto.CandidatoRequest;
import com.uti.candidatoservice.dto.CandidatoResponse;

import java.util.List;

public interface CandidatoService {

    // CREAR PERFIL (una sola vez por username)
    CandidatoResponse createProfile(CandidatoRequest request);

    // ACTUALIZAR PERFIL EXISTENTE
    CandidatoResponse updateProfile(String candidatoUsername, CandidatoRequest request);

    // VER EL PERFIL DE UN CANDIDATO POR USERNAME
    CandidatoResponse getCandidatoByUsername(String candidatoUsername);

    // LISTAR TODOS LOS CANDIDATOS (util para pruebas mientras no hay frontend)
    List<CandidatoResponse> getAllCandidatos();

    // ELIMINAR PERFIL
    void deleteCandidato(String candidatoUsername);
}
