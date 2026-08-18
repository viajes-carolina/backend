package com.viajescarolina.api.auth.application.dto;

public record CreateAuditLogRequest(
    Long userId,
    String username,
    String action,
    String entityType,
    String entityId,
    String ipAddress,
    String detailsJson
) {}
