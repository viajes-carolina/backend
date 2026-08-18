package com.viajescarolina.api.auth.application.dto;

public record LoginResponse(
    String token,
    String tokenType,
    long expiresInSeconds,
    AdminUserDTO user
) {}
