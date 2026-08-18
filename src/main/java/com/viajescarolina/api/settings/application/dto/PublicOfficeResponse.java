package com.viajescarolina.api.settings.application.dto;

import java.math.BigDecimal;

public record PublicOfficeResponse(
    String fullAddress,
    String district,
    String city,
    String country,
    String referenceLandmark,
    BigDecimal latitude,
    BigDecimal longitude,
    String googleMapsUrl,
    String embedMapsUrl,
    String scheduleWeekdays,
    String scheduleSaturdays,
    boolean active
) {}
