package com.viajescarolina.api.auth.application.usecase;

import com.viajescarolina.api.auth.application.dto.AdminUserDTO;
import com.viajescarolina.api.auth.application.dto.LoginRequest;
import com.viajescarolina.api.auth.application.dto.LoginResponse;
import com.viajescarolina.api.auth.domain.AdminUser;
import com.viajescarolina.api.auth.domain.AdminUserRepository;
import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import com.viajescarolina.api.auth.domain.PasswordHasher;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class LoginAdminUseCase {

    /** Duración de una sesión administrativa estándar: 1 hora. */
    private static final long SESSION_TTL_SECONDS = 3600L;

    /** Duración cuando el usuario marca "Mantener mi sesión": 30 días. */
    private static final long EXTENDED_SESSION_TTL_SECONDS = 2592000L;

    private final AdminUserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AuthAuditRecorder auditRecorder;

    public LoginAdminUseCase(AdminUserRepository userRepository, PasswordHasher passwordHasher, AuthAuditRecorder auditRecorder) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.auditRecorder = auditRecorder;
    }

    @Transactional
    public LoginResponse execute(LoginRequest req, String ipAddress) {
        String identifier = req.usernameOrEmail().trim().toLowerCase();

        Optional<AdminUser> userOpt = userRepository.findByUsername(identifier);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(identifier);
        }

        if (userOpt.isEmpty()) {
            recordAudit(null, identifier, "LOGIN_FAILED", "USER_NOT_FOUND", ipAddress);
            throw new WebApplicationException("Credenciales inválidas.", Response.Status.UNAUTHORIZED);
        }

        AdminUser user = userOpt.get();

        if (!user.isActive()) {
            recordAudit(user.getId(), user.getUsername(), "LOGIN_FAILED", "USER_INACTIVE", ipAddress);
            throw new WebApplicationException("La cuenta de usuario se encuentra inactiva.", Response.Status.FORBIDDEN);
        }

        boolean valid = passwordHasher.verify(req.password(), user.getPasswordHash());
        if (!valid) {
            recordAudit(user.getId(), user.getUsername(), "LOGIN_FAILED", "INVALID_PASSWORD", ipAddress);
            throw new WebApplicationException("Credenciales inválidas.", Response.Status.UNAUTHORIZED);
        }

        user.setLastLoginAt(Instant.now());
        userRepository.save(user);

        recordAudit(user.getId(), user.getUsername(), "LOGIN_SUCCESS", "SUCCESS", ipAddress);

        // Este valor viaja en LoginResponse.expiresInSeconds y AdminAuthResource lo reutiliza
        // como maxAge de la cookie vc_admin_jwt, por lo que la cookie y el JWT siempre expiran juntos.
        // rememberMe es opcional: null (clientes antiguos) equivale a sesión estándar.
        long expiresInSeconds = Boolean.TRUE.equals(req.rememberMe())
                ? EXTENDED_SESSION_TTL_SECONDS
                : SESSION_TTL_SECONDS;

        String token;
        try {
            token = Jwt.issuer("https://viajescarolina.com/issuer")
                    .upn(user.getUsername())
                    .subject(String.valueOf(user.getId()))
                    .groups(Set.of(user.getRole()))
                    .claim("email", user.getEmail())
                    .claim("fullName", user.getFullName())
                    .expiresIn(Duration.ofSeconds(expiresInSeconds))
                    .sign();
        } catch (Exception e) {
            recordAudit(user.getId(), user.getUsername(), "LOGIN_FAILED", "JWT_SIGNING_ERROR", ipAddress);
            throw new WebApplicationException("No se pudo generar la sesión.", Response.Status.INTERNAL_SERVER_ERROR);
        }

        AdminUserDTO dto = new AdminUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.isActive(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        return new LoginResponse(token, "Bearer", expiresInSeconds, dto);
    }

    /** Delega en {@link AuthAuditRecorder}, que audita en transacción propia. */
    private void recordAudit(Long userId, String username, String action, String result, String ipAddress) {
        auditRecorder.record(userId, username, action, result, ipAddress);
    }
}
