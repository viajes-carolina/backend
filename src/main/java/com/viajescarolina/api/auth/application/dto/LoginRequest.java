package com.viajescarolina.api.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "El nombre de usuario o email es obligatorio")
    String usernameOrEmail,

    @NotBlank(message = "La contraseña es obligatoria")
    String password
) {}
