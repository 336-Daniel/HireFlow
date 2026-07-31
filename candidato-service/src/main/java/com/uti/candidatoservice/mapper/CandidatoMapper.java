package com.uti.candidatoservice.mapper;

import com.uti.candidatoservice.dto.CandidatoRequest;
import com.uti.candidatoservice.dto.CandidatoResponse;
import com.uti.candidatoservice.model.Candidato;
import org.springframework.stereotype.Component;

@Component
public class CandidatoMapper {

    // Convertir CandidatoRequest -> Candidato (Entidad), para el registro inicial
    // candidatoUsername ya no viene del request: se pasa por separado (viene del JWT)
    public Candidato toEntity(CandidatoRequest request, String candidatoUsername) {
        return Candidato.builder()
                .candidatoUsername(candidatoUsername)
                .fullName(request.fullName())
                .cvText(request.cvText())
                .mainSkills(request.mainSkills())
                .aniosExp(request.aniosExp())
                .build();
    }

    // Actualiza una entidad existente con los datos del request (para el PUT)
    public void updateEntityFromRequest(Candidato candidato, CandidatoRequest request) {
        candidato.setFullName(request.fullName());
        candidato.setCvText(request.cvText());
        candidato.setMainSkills(request.mainSkills());
    }

    // Convertir Candidato -> CandidatoResponse
    public CandidatoResponse toResponse(Candidato candidato) {
        return new CandidatoResponse(
                candidato.getId(),
                candidato.getCandidatoUsername(),
                candidato.getFullName(),
                candidato.getCvText(),
                candidato.getMainSkills(),
                candidato.getAniosExp()
        );
    }
}
