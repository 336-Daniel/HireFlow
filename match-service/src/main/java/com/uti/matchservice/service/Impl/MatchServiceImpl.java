package com.uti.matchservice.service.Impl;

import com.uti.matchservice.client.CandidatoWebClient;
import com.uti.matchservice.client.GeminiApiClient;
import com.uti.matchservice.client.VacanteWebClient;
import com.uti.matchservice.dto.CandidatoResponse;
import com.uti.matchservice.dto.MatchRequest;
import com.uti.matchservice.dto.MatchResponse;
import com.uti.matchservice.dto.VacanteResponse;
import com.uti.matchservice.exception.DuplicateResourceException;
import com.uti.matchservice.exception.ResourceNotfoundException;
import com.uti.matchservice.mapper.MatchMapper;
import com.uti.matchservice.model.Match;
import com.uti.matchservice.repository.MatchRepository;
import com.uti.matchservice.service.MatchService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;

    private final CandidatoWebClient candidatoWebClient;
    private final VacanteWebClient vacanteWebClient;

    private final GeminiApiClient geminiApiClient;

    @Override
    @Transactional
    @CircuitBreaker(name = "externalServices", fallbackMethod = "createMatchFallback")
    @Retry(name = "externalServices")
    public MatchResponse createMatch(MatchRequest request, String candidatoUsername) {
        log.info("Iniciando postulación a la vacante id: {} para el usuario: {}", request.vacanteId(), candidatoUsername);

        // 1. Regla de Negocio: Evitar postulaciones duplicadas
        if (matchRepository.existsByVacanteIdAndCandidatoUsername(request.vacanteId(), candidatoUsername)) {
            throw new DuplicateResourceException("El candidato " + candidatoUsername + " ya se ha postulado a la vacante con ID: " + request.vacanteId());
        }

        // 2. Validar que la vacante existe trayendo sus datos
        log.info("Verificando existencia de la vacante via WebClient...");
        VacanteResponse vacante = vacanteWebClient.getVacanteById(request.vacanteId());

        // 3. Obtener el perfil del candidato
        log.info("Obteniendo detalles del candidato via WebClient...");
        CandidatoResponse candidato = candidatoWebClient.getCandidatoByUsername(candidatoUsername);

        // 4. Mapear la entidad inicial (score y feedback inician en null)
        Match match = matchMapper.toEntity(request, candidatoUsername);

        // 5. Llamada a la IA (Aislada en un try-catch para no romper el flujo principal)
        try {
            log.info("Llamando a la IA de Gemini para calcular el match...");

            // Unimos los campos clave de tus DTOs para darle el mejor contexto posible a la IA
            String perfilCandidato = String.format("Nombre: %s\nHabilidades Principales: %s\nExperiencia/CV: %s",
                    candidato.fullName(), candidato.mainSkills(), candidato.cvText());

            String descripcionVacante = String.format("Puesto: %s\nRequisitos: %s",
                    vacante.jobTitle(), vacante.requirements());

            // Llamamos a nuestro cliente de Gemini
            var iaResult = geminiApiClient.evaluateMatch(perfilCandidato, descripcionVacante);

            match.setIaMatchScore(iaResult.score());
            match.setIaFeedback(iaResult.feedback());

        } catch (Exception ex) {
            log.warn("La IA de Gemini falló o no está disponible. Guardando postulación con estado pendiente. Error: {}", ex.getMessage());
            match.setIaMatchScore(null);
            match.setIaFeedback("Evaluación de IA pendiente (Servicio temporalmente inactivo).");
        }

        // 6. Guardar en Base de Datos
        Match savedMatch = matchRepository.save(match);
        log.info("Postulación creada exitosamente con ID: {}", savedMatch.getId());

        return matchMapper.toResponse(savedMatch);
    }

    // Fallback Method para createMatch (Se activa si se caen Candidatos o Vacantes)
    public MatchResponse createMatchFallback(MatchRequest request, String candidatoUsername, Throwable throwable) {
        log.warn("Circuit Breaker ABIERTO en createMatch. Razón: {}", throwable.getMessage());

        // Si el error fue por duplicado, queremos que el usuario vea ese mensaje exacto
        if (throwable instanceof DuplicateResourceException) {
            throw (DuplicateResourceException) throwable;
        }

        // Si fue un error de comunicación, lanzamos un error general del servidor
        throw new RuntimeException("No se pudo procesar la postulación porque los servicios externos (Vacantes/Candidatos) no están disponibles. Intente más tarde.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getMatchesByVacanteId(Long vacanteId) {
        log.info("Obteniendo postulaciones (ordenadas por score) para la vacante: {}", vacanteId);
        return matchRepository.findByVacanteIdOrderByIaMatchScoreDesc(vacanteId)
                .stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getMatchesByCandidatoUsername(String candidatoUsername) {
        log.info("Obteniendo historial de postulaciones para el candidato: {}", candidatoUsername);
        return matchRepository.findByCandidatoUsername(candidatoUsername)
                .stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MatchResponse getMatchById(Long matchId) {
        log.info("Buscando detalle de la postulación ID: {}", matchId);
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotfoundException("Postulación no encontrada con el ID: " + matchId));
        return matchMapper.toResponse(match);
    }
}