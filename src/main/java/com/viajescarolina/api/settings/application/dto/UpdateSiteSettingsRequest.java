package com.viajescarolina.api.settings.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateSiteSettingsRequest(
    @NotBlank(message = "El nombre del sitio es obligatorio")
    @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
    String siteName,

    @Size(max = 200, message = "El tagline no puede exceder 200 caracteres")
    String brandTagline,

    @NotBlank(message = "El correo de contacto es obligatorio")
    @Email(message = "Formato de correo inválido")
    String contactEmail,

    Integer logoMediaId,
    Integer faviconMediaId,
    String facebookUrl,
    String instagramUrl,
    String tiktokUrl,

    @NotBlank(message = "La razón social es obligatoria (Libro de Reclamaciones, Ley N° 29571)")
    @Size(max = 200, message = "La razón social no puede exceder 200 caracteres")
    String legalCompanyName,

    @NotBlank(message = "El RUC es obligatorio (Libro de Reclamaciones, Ley N° 29571)")
    @Size(max = 20, message = "El RUC no puede exceder 20 caracteres")
    String taxId,

    @Size(max = 500, message = "El enlace no puede exceder 500 caracteres")
    String minceturCertificateUrl,

    // El único número de contacto del sitio (llamadas + WhatsApp), guardado en
    // whatsapp_channel — se edita desde este mismo formulario/endpoint por
    // conveniencia, pero vive en su propia tabla (ver DB-normalización: antes
    // este dato estaba duplicado entre site_settings.primary_phone y
    // whatsapp_channel sin garantía de que coincidieran).
    @NotBlank(message = "El número E.164 es obligatorio")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Formato E.164 inválido (ej: +51987654321)")
    String whatsappPhone,

    @NotBlank(message = "El número para mostrar es obligatorio")
    String whatsappDisplayNumber,

    String whatsappDefaultMessage
) {}
