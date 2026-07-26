package com.uti.authservice.service.impl;

import com.uti.authservice.service.KeycloakAdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminServiceImpl implements KeycloakAdminService {
    private final WebClient webClient;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client-id}")
    private String adminClientId;

    @Value("${keycloak.admin-client-secret}")
    private String adminClientSecret;

    public KeycloakAdminServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    private String getServiceAccountToken() {
        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", adminClientId);
        formData.add("client_secret", adminClientSecret);

        Map<String, Object> response = webClient.post()
                .uri(tokenUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
        return (String) response.get("access_token");
    }

    @Override
    public String createUser(String username, String email, String firstName, String lastName, String password) {
        String serverToken = getServiceAccountToken();
        String createUserUrl = serverUrl + "/admin/realms/" + realm + "/users";
        Map<String, Object> credentials = Map.of(
                "type", "password",
                "value", password,
                "temporary", false
        );

        Map<String, Object> userPayload = Map.of(
                "username", username,
                "email", email,
                "firstName", firstName,
                "lastName", lastName,
                "enabled", true,
                "emailVerified", false,
                "credentials", List.of(credentials)
        );

        try {
            webClient.post()
                    .uri(createUserUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + serverToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(userPayload)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.Conflict ex) {
            throw new IllegalStateException("El usuario " + username + " ya existe en el sistema");
        }

        return getUserIdByUsername(username, serverToken);
    }

    private String getUserIdByUsername(String username, String serverToken) {
        String searchUrl = serverUrl + "/admin/realms/" + realm + "/users?username=" + username + "&exact=true";
        List<Map<String, Object>> users = webClient.get()
                .uri(searchUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + serverToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();

        if (users == null || users.isEmpty()) {
            throw new IllegalStateException("No se pudo encontrar el usuario recien creado: " + username);
        }

        return (String) users.get(0).get("id");
    }

    @Override
    public void assignRealmRole(String userId, String roleName) {
        String serverToken = getServiceAccountToken();
        String roleUrl = serverUrl + "/admin/realms/" + realm + "/roles/" + roleName;
        Map<String, Object> role = webClient.get()
                .uri(roleUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + serverToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        String assignUrl = serverUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";

        webClient.post()
                .uri(assignUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + serverToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(List.of(role))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
