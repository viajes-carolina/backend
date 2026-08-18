package com.viajescarolina.api.contact.application.dto;

import java.time.Instant;

public record ContactPageDTO(
    Integer id,
    String heroBadge,
    String heroTitle,
    String heroSubtitle,
    String whatsappBoxTitle,
    String whatsappBoxSubtitle,
    String formTitle,
    String formSubtitle,
    int revision,
    Instant updatedAt
) {}
