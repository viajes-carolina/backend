package com.viajescarolina.api.claims.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SubmitClaimRequest(
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 150)
    String fullName,

    @NotBlank(message = "El tipo de documento es obligatorio")
    String documentType,

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 30)
    String documentNumber,

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Formato de correo inválido")
    String email,

    @NotBlank(message = "El teléfono de contacto es obligatorio")
    @Size(max = 50)
    String phone,

    @NotBlank(message = "La dirección de domicilio es obligatoria")
    @Size(max = 255)
    String address,

    boolean isMinor,
    String parentName,
    String parentDocument,

    // Ya no es obligatorio: el asistente público de 4 pasos no lo solicita.
    // SubmitClaimUseCase aplica "SERVICIO" como valor de dominio por defecto si llega null/blank.
    String contractedType, // PRODUCTO, SERVICIO

    BigDecimal claimedAmount,
    String currency,

    @Size(max = 30)
    String relatedService, // paquete, pasaje, alojamiento, asesoria, otro

    @Size(max = 60)
    String reservationCode,

    LocalDate serviceDate,

    String responseChannel, // EMAIL, CARTA — por defecto EMAIL

    @NotBlank(message = "La descripción del bien contratado es obligatoria")
    @Size(max = 2000)
    String description,

    @NotBlank(message = "El tipo de reclamo es obligatorio (QUEJA o RECLAMO)")
    String claimType, // QUEJA, RECLAMO

    @NotBlank(message = "El detalle del reclamo o queja es obligatorio")
    @Size(max = 3000)
    String consumerDetail,

    @NotBlank(message = "El pedido o solicitud concreta es obligatorio")
    @Size(max = 2000)
    String consumerRequest,

    String turnstileToken
) {}
