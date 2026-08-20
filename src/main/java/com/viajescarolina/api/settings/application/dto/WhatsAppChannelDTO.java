package com.viajescarolina.api.settings.application.dto;

import java.time.Instant;

public record WhatsAppChannelDTO(
    Integer id,
    String label,
    String e164Number,
    String displayNumber,
    String defaultMessage,
    boolean isPrimary,
    boolean active,
    int revision,
    Instant updatedAt
) {}
