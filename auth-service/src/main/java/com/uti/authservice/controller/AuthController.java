package com.uti.authservice.controller;

import com.uti.authservice.dto.LoginRequestDto;
import com.uti.authservice.dto.RegisterRequestDto;
import com.uti.authservice.dto.TokenResponseDto;
import com.uti.authservice.service.KeycloakAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KeycloakAdminService keycloakAdminService;
    private final WebClient webClient;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.public-client-id}")
    private String publicClientId;

    @Value("${keycloak.public-client-secret}")
    private String publicClientSecret;

    public AuthController(KeycloakAdminService keycloakAdminService, WebClient webClient) {
        this.keycloakAdminService = keycloakAdminService;
        this.webClient = webClient;
    }

    // Registro publico: el candidato o reclutador elige su propio rol
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto request) {
        // Creamos el usuario en keycloak
        String userId = keycloakAdminService.createUser(
                request.username(),
                request.email(),
                request.firstName(),
                request.lastName(),
                request.password()
        );

        // A diferencia del proyecto de referencia (rol fijo "PATIENT"), aqui usamos
        // el rol que el propio usuario eligio en el formulario de registro
        keycloakAdminService.assignRealmRole(userId, request.role());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Login publico: devuelve el token que Keycloak genero
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", publicClientId);
        formData.add("client_secret", publicClientSecret);
        formData.add("username", request.username());
        formData.add("password", request.password());

        Map<String, Object> response = webClient.post()
                .uri(tokenUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        // Extraer los 3 campos relevantes de la respuesta de Keycloak
        TokenResponseDto tokenResponse = new TokenResponseDto(
                (String) response.get("access_token"),
                (String) response.get("refresh_token"),
                ((Number) response.get("expires_in")).longValue()
        );
        return ResponseEntity.ok(tokenResponse);
    }
}
