package com.viajescarolina.api.about.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrUpdateAdvisorRequest(
    @NotBlank String fullName,
    @NotBlank String roleTitle,
    @NotBlank String specialty,
    @NotBlank String bio,
    Long photoMediaId,
    String whatsappPhone,
    String whatsappMessageTemplate,
    Integer displayOrder,
    Boolean active
) {}
