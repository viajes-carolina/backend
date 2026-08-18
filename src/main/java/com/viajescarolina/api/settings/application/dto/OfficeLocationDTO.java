package com.viajescarolina.api.settings.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record OfficeLocationDTO(
    Integer id,
    String addressLine,
    String district,
    String city,
    String country,
    String postalCode,
    String referenceLandmark,
    BigDecimal latitude,
    BigDecimal longitude,
    String googleMapsUrl,
    String embedMapsUrl,
    String scheduleWeekdays,
    String scheduleSaturdays,
    boolean active,
    int revision,
    Instant updatedAt
) {}
