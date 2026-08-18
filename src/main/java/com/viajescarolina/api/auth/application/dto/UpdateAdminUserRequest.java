package com.viajescarolina.api.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateAdminUserRequest(
    @NotBlank(message = "El nombre de usuario es obligatorio")
    String username,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato de email no es válido")
    String email,

    String password, // opcional en actualización

    @NotBlank(message = "El nombre completo es obligatorio")
    String fullName,

    String role,

    Boolean active
) {}
