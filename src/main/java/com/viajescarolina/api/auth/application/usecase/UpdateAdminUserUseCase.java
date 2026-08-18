package com.viajescarolina.api.auth.application.usecase;

import com.viajescarolina.api.auth.application.dto.AdminUserDTO;
import com.viajescarolina.api.auth.application.dto.UpdateAdminUserRequest;
import com.viajescarolina.api.auth.domain.AdminUser;
import com.viajescarolina.api.auth.domain.AdminUserRepository;
import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import com.viajescarolina.api.auth.domain.PasswordHasher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class UpdateAdminUserUseCase {

    private final AdminUserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AuditLogRepository auditLogRepository;

    public UpdateAdminUserUseCase(AdminUserRepository userRepository, PasswordHasher passwordHasher, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AdminUserDTO execute(Long id, UpdateAdminUserRequest req, String adminUsername) {
        AdminUser user = userRepository.findUserById(id)
                .orElseThrow(() -> new WebApplicationException("Usuario no encontrado.", Response.Status.NOT_FOUND));

        Optional<AdminUser> byUsername = userRepository.findByUsername(req.username().trim().toLowerCase());
        if (byUsername.isPresent() && !byUsername.get().getId().equals(id)) {
            throw new WebApplicationException("El nombre de usuario ya está en uso.", Response.Status.CONFLICT);
        }

        Optional<AdminUser> byEmail = userRepository.findByEmail(req.email().trim().toLowerCase());
        if (byEmail.isPresent() && !byEmail.get().getId().equals(id)) {
            throw new WebApplicationException("El correo ya está en uso.", Response.Status.CONFLICT);
        }

        user.setUsername(req.username().trim().toLowerCase());
        user.setEmail(req.email().trim().toLowerCase());
        user.setFullName(req.fullName().trim());

        if (req.role() != null) {
            user.setRole(req.role());
        }

        if (req.active() != null) {
            user.setActive(req.active());
        }

        if (req.password() != null && !req.password().isBlank()) {
            user.setPasswordHash(passwordHasher.hash(req.password()));
        }

        AdminUser saved = userRepository.save(user);

        try {
            AuditLog log = new AuditLog(
                    null,
                    null,
                    adminUsername != null ? adminUsername : "SYSTEM",
                    "UPDATE_ADMIN_USER",
                    "USER",
                    String.valueOf(saved.getId()),
                    null,
                    String.format("{\"updatedUsername\": \"%s\", \"role\": \"%s\"}", saved.getUsername(), saved.getRole()),
                    Instant.now()
            );
            auditLogRepository.save(log);
        } catch (Exception ignored) {}

        return new AdminUserDTO(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getFullName(),
                saved.getRole(),
                saved.isActive(),
                saved.getLastLoginAt(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
