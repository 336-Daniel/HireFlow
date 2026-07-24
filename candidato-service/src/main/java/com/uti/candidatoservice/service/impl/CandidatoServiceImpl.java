package com.uti.candidatoservice.service.impl;

import com.uti.candidatoservice.dto.CandidatoRequest;
import com.uti.candidatoservice.dto.CandidatoResponse;
import com.uti.candidatoservice.exception.DuplicateResourceException;
import com.uti.candidatoservice.exception.ResourceNotfoundException;
import com.uti.candidatoservice.mapper.CandidatoMapper;
import com.uti.candidatoservice.model.Candidato;
import com.uti.candidatoservice.repository.CandidatoRepository;
import com.uti.candidatoservice.service.CandidatoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidatoServiceImpl implements CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final CandidatoMapper candidatoMapper;

    @Override
    @Transactional
    public CandidatoResponse createProfile(CandidatoRequest request) {
        log.info("creando perfil de candidato para: {}", request.candidatoUsername());

        if (candidatoRepository.existsByCandidatoUsername(request.candidatoUsername())) {
            throw new DuplicateResourceException(
                    "Ya existe un perfil de candidato para el usuario: " + request.candidatoUsername());
        }

        Candidato candidato = candidatoMapper.toEntity(request);
        Candidato saved = candidatoRepository.save(candidato);

        log.info("Perfil de candidato creado exitosamente con el id: {}", saved.getId());
        return candidatoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CandidatoResponse updateProfile(String candidatoUsername, CandidatoRequest request) {
        log.info("actualizando perfil de candidato: {}", candidatoUsername);

        Candidato candidato = candidatoRepository.findByCandidatoUsername(candidatoUsername)
                .orElseThrow(() -> new ResourceNotfoundException(
                        "No existe un perfil de candidato para el usuario: " + candidatoUsername));

        candidatoMapper.updateEntityFromRequest(candidato, request);
        Candidato updated = candidatoRepository.save(candidato);

        log.info("Perfil de candidato actualizado exitosamente: {}", candidatoUsername);
        return candidatoMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public CandidatoResponse getCandidatoByUsername(String candidatoUsername) {
        log.info("fetching candidato by username: {}", candidatoUsername);
        Candidato candidato = candidatoRepository.findByCandidatoUsername(candidatoUsername)
                .orElseThrow(() -> new ResourceNotfoundException(
                        "Candidato no encontrado con username: " + candidatoUsername));
        return candidatoMapper.toResponse(candidato);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidatoResponse> getAllCandidatos() {
        log.info("fetching all candidatos");
        return candidatoRepository.findAll()
                .stream()
                .map(candidatoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCandidato(String candidatoUsername) {
        log.info("eliminando perfil de candidato: {}", candidatoUsername);
        Candidato candidato = candidatoRepository.findByCandidatoUsername(candidatoUsername)
                .orElseThrow(() -> new ResourceNotfoundException(
                        "No existe un perfil de candidato para el usuario: " + candidatoUsername));
        candidatoRepository.delete(candidato);
        log.info("Perfil de candidato eliminado exitosamente: {}", candidatoUsername);
    }
}
