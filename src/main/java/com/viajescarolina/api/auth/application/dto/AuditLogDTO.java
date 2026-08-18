package com.viajescarolina.api.auth.application.dto;

import java.time.Instant;

public record AuditLogDTO(
    Long id,
    Long userId,
    String username,
    String action,
    String entityType,
    String entityId,
    String ipHash,
    String detailsJson,
    Instant createdAt
) {}
