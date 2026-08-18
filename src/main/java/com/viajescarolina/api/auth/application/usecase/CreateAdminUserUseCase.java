package com.viajescarolina.api.auth.application.usecase;

import com.viajescarolina.api.auth.application.dto.AdminUserDTO;
import com.viajescarolina.api.auth.application.dto.CreateAdminUserRequest;
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

@ApplicationScoped
public class CreateAdminUserUseCase {

    private final AdminUserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AuditLogRepository auditLogRepository;

    public CreateAdminUserUseCase(AdminUserRepository userRepository, PasswordHasher passwordHasher, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AdminUserDTO execute(CreateAdminUserRequest req, String adminUsername) {
        if (userRepository.findByUsername(req.username().trim().toLowerCase()).isPresent()) {
            throw new WebApplicationException("El nombre de usuario ya está registrado.", Response.Status.CONFLICT);
        }

        if (userRepository.findByEmail(req.email().trim().toLowerCase()).isPresent()) {
            throw new WebApplicationException("El correo electrónico ya está registrado.", Response.Status.CONFLICT);
        }

        String hashedPassword = passwordHasher.hash(req.password());

        AdminUser newUser = new AdminUser(
                null,
                req.username().trim().toLowerCase(),
                req.email().trim().toLowerCase(),
                hashedPassword,
                req.fullName().trim(),
                req.role() != null ? req.role() : "CONTENT_EDITOR",
                req.active() != null ? req.active() : true,
                null,
                Instant.now(),
                Instant.now()
        );

        AdminUser saved = userRepository.save(newUser);

        try {
            AuditLog log = new AuditLog(
                    null,
                    null,
                    adminUsername != null ? adminUsername : "SYSTEM",
                    "CREATE_ADMIN_USER",
                    "USER",
                    String.valueOf(saved.getId()),
                    null,
                    String.format("{\"createdUsername\": \"%s\", \"role\": \"%s\"}", saved.getUsername(), saved.getRole()),
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
