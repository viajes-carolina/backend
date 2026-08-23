package com.viajescarolina.api.auth.application.usecase;

import com.viajescarolina.api.auth.application.dto.ChangeOwnPasswordRequest;
import com.viajescarolina.api.auth.application.dto.ChangeOwnPasswordResponse;
import com.viajescarolina.api.auth.domain.AdminUser;
import com.viajescarolina.api.auth.domain.AdminUserRepository;
import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import com.viajescarolina.api.auth.domain.PasswordHasher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.Map;

@ApplicationScoped
public class ChangeOwnPasswordUseCase {

    private final AdminUserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AuditLogRepository auditLogRepository;

    public ChangeOwnPasswordUseCase(AdminUserRepository userRepository, PasswordHasher passwordHasher, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public ChangeOwnPasswordResponse execute(Long callerId, ChangeOwnPasswordRequest req, String actorUsername) {
        AdminUser user = userRepository.findUserById(callerId)
                .orElseThrow(() -> new WebApplicationException(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .entity(Map.of("message", "Sesión inválida."))
                                .type(MediaType.APPLICATION_JSON)
                                .build()));

        if (!passwordHasher.verify(req.currentPassword(), user.getPasswordHash())) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("message", "La contraseña actual no es correcta."))
                            .type(MediaType.APPLICATION_JSON)
                            .build());
        }

        user.setPasswordHash(passwordHasher.hash(req.newPassword()));
        AdminUser saved = userRepository.save(user);

        try {
            AuditLog log = new AuditLog(
                    null,
                    null,
                    actorUsername != null ? actorUsername : "SYSTEM",
                    "CHANGE_OWN_PASSWORD",
                    "USER",
                    String.valueOf(saved.getId()),
                    null,
                    "{}",
                    Instant.now()
            );
            auditLogRepository.save(log);
        } catch (Exception ignored) {}

        return new ChangeOwnPasswordResponse("PASSWORD_UPDATED");
    }
}
