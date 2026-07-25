package com.uti.vacanteservice.mapper;

import com.uti.vacanteservice.dto.VacanteRequest;
import com.uti.vacanteservice.dto.VacanteResponse;
import com.uti.vacanteservice.model.Vacante;
import org.springframework.stereotype.Component;

@Component
public class VacanteMapper {

    // Convertir VacanteRequest a vacante para la publicaicon
    public Vacante toEntity(VacanteRequest request) {
        return Vacante.builder()
                .reclutadorUsername(request.reclutadorUsername())
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .requisitos(request.requisitos())
                .activa(true)
                .build();
    }

    // Actualiza una entidad existente con los datos del request
    public void updateEntityFromRequest(Vacante vacante, VacanteRequest request) {
        vacante.setTitulo(request.titulo());
        vacante.setDescripcion(request.descripcion());
        vacante.setRequisitos(request.requisitos());
    }

    // Convertir vacante a vacanteResponse
    public VacanteResponse toResponse(Vacante vacante) {
        return new VacanteResponse(
                vacante.getId(),
                vacante.getReclutadorUsername(),
                vacante.getTitulo(),
                vacante.getDescripcion(),
                vacante.getRequisitos(),
                vacante.isActiva()
        );
    }
}
