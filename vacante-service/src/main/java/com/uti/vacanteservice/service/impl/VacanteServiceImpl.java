package com.uti.vacanteservice.service.impl;

import com.uti.vacanteservice.dto.VacanteRequest;
import com.uti.vacanteservice.dto.VacanteResponse;
import com.uti.vacanteservice.exception.ResourceNotfoundException;
import com.uti.vacanteservice.mapper.VacanteMapper;
import com.uti.vacanteservice.model.Vacante;
import com.uti.vacanteservice.repository.VacanteRepository;
import com.uti.vacanteservice.service.VacanteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacanteServiceImpl implements VacanteService {

    private final VacanteRepository vacanteRepository;
    private final VacanteMapper vacanteMapper;

    @Override
    @Transactional
    public VacanteResponse createVacante(VacanteRequest request) {
        log.info("creando vacante para el reclutador: {}", request.reclutadorUsername());

        Vacante vacante = vacanteMapper.toEntity(request);
        Vacante saved = vacanteRepository.save(vacante);

        log.info("Vacante creada exitosamente con el id: {}", saved.getId());
        return vacanteMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public VacanteResponse updateVacante(Long id, VacanteRequest request) {
        log.info("actualizando vacante: {}", id);

        Vacante vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException(
                        "No existe una vacante con el id: " + id));

        vacanteMapper.updateEntityFromRequest(vacante, request);
        Vacante updated = vacanteRepository.save(vacante);

        log.info("Vacante actualizada exitosamente: {}", id);
        return vacanteMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public VacanteResponse getVacanteById(Long id) {
        log.info("fetching vacante by id: {}", id);
        Vacante vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException(
                        "Vacante no encontrada con id: " + id));
        return vacanteMapper.toResponse(vacante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacanteResponse> getAllVacantes() {
        log.info("fetching all vacantes");
        return vacanteRepository.findAll()
                .stream()
                .map(vacanteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacanteResponse> getVacantesActivas() {
        log.info("fetching vacantes activas");
        return vacanteRepository.findByActivaTrue()
                .stream()
                .map(vacanteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacanteResponse> getVacantesByReclutador(String reclutadorUsername) {
        log.info("fetching vacantes del reclutador: {}", reclutadorUsername);
        return vacanteRepository.findByReclutadorUsername(reclutadorUsername)
                .stream()
                .map(vacanteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VacanteResponse cerrarVacante(Long id) {
        log.info("cerrando vacante: {}", id);
        Vacante vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException(
                        "No existe una vacante con el id: " + id));
        vacante.setActiva(false);
        Vacante updated = vacanteRepository.save(vacante);
        log.info("Vacante cerrada exitosamente: {}", id);
        return vacanteMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteVacante(Long id) {
        log.info("eliminando vacante: {}", id);
        Vacante vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException(
                        "No existe una vacante con el id: " + id));
        vacanteRepository.delete(vacante);
        log.info("Vacante eliminada exitosamente: {}", id);
    }
}
