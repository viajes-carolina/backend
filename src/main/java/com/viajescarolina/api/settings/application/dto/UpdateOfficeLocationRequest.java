package com.viajescarolina.api.settings.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record UpdateOfficeLocationRequest(
    @NotBlank(message = "La dirección es obligatoria")
    String addressLine,

    @NotBlank(message = "El distrito es obligatorio")
    String district,

    @NotBlank(message = "La ciudad es obligatoria")
    String city,

    @NotBlank(message = "El país es obligatorio")
    String country,

    String postalCode,
    String referenceLandmark,
    BigDecimal latitude,
    BigDecimal longitude,
    String googleMapsUrl,
    String embedMapsUrl,

    @NotBlank(message = "El horario de lunes a viernes es obligatorio")
    String scheduleWeekdays,

    @NotBlank(message = "El horario de sábados es obligatorio")
    String scheduleSaturdays,

    boolean active
) {}
