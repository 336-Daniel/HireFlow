package com.uti.authservice.service;

public interface KeycloakAdminService {
    String createUser(String username, String email, String firstName, String lastName, String password);
    void assignRealmRole(String userId, String roleName);
}
