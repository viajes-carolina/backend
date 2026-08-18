package com.viajescarolina.api.claims.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateClaimStatusRequest(
    @NotBlank(message = "El estado es obligatorio")
    String status,

    @Size(max = 3000)
    String responseNotes
) {}
