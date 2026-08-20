package com.viajescarolina.api.settings.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateWhatsAppChannelRequest(
    @NotBlank(message = "La etiqueta es obligatoria")
    String label,

    @NotBlank(message = "El número E.164 es obligatorio")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Formato E.164 inválido (ej: +51987654321)")
    String e164Number,

    @NotBlank(message = "El número para mostrar es obligatorio")
    String displayNumber,

    String defaultMessage,

    boolean active
) {}
