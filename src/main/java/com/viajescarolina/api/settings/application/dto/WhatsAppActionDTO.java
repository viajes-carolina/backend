package com.viajescarolina.api.settings.application.dto;

import java.time.Instant;

public record WhatsAppActionDTO(
    Integer id,
    String actionKey,
    String label,
    String messageTemplate,
    String description,
    Instant updatedAt
) {}
