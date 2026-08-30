package com.viajescarolina.api.legal.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateLegalEsnnaRequest(
    @NotBlank(message = "El eyebrow es obligatorio")
    String eyebrow,

    @NotBlank(message = "El título es obligatorio")
    String title,

    @NotBlank(message = "La introducción es obligatoria")
    String introduction,

    @NotBlank(message = "El label de control documental es obligatorio")
    String documentControlLabel,

    @NotBlank(message = "El texto de control documental es obligatorio")
    String documentControlText,

    @NotBlank(message = "El eyebrow de la declaración institucional es obligatorio")
    String declarationEyebrow,

    @NotBlank(message = "El título de la declaración institucional es obligatorio")
    String declarationTitle,

    @NotBlank(message = "El cuerpo de la declaración institucional es obligatorio")
    String declarationBody,

    @NotNull(message = "Las secciones son obligatorias")
    @Valid
    List<LegalSectionDTO> sections,

    @NotBlank(message = "El título de cierre es obligatorio")
    String closingTitle,

    @NotBlank(message = "El cuerpo de cierre es obligatorio")
    String closingBody,

    @NotBlank(message = "El label del enlace de cierre es obligatorio")
    String closingLinkLabel
) {}
