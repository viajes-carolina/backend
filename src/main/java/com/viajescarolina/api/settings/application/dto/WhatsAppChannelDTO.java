package com.viajescarolina.api.settings.application.dto;

import java.time.Instant;

public record WhatsAppChannelDTO(
    Integer id,
    String e164Number,
    String displayNumber,
    boolean active,
    int revision,
    Instant updatedAt
) {}
