package com.viajescarolina.api.auth.application.dto;

import java.time.Instant;

public record AdminUserDTO(
    Long id,
    String username,
    String email,
    String fullName,
    String role,
    boolean active,
    Instant lastLoginAt,
    Instant createdAt,
    Instant updatedAt
) {}
