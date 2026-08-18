package com.viajescarolina.api.contact.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateContactPageRequest(
    @NotBlank String heroBadge,
    @NotBlank String heroTitle,
    @NotBlank String heroSubtitle,
    @NotBlank String whatsappBoxTitle,
    @NotBlank String whatsappBoxSubtitle,
    @NotBlank String formTitle,
    @NotBlank String formSubtitle
) {}
