package com.viajescarolina.api.promotions.application.dto;

import jakarta.validation.constraints.NotNull;

public record SetPromotionActiveRequest(
        @NotNull(message = "El campo active es obligatorio")
        Boolean active
) {}
