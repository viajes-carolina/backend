package com.viajescarolina.api.auth.application.usecase;

import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.Instant;

/**
 * Escribe en la bitácora los eventos de autenticación en una transacción PROPIA.
 *
 * Existe como colaborador separado, y no como método privado de
 * {@link LoginAdminUseCase}, por una razón concreta: los intentos de acceso
 * fallidos terminan lanzando {@code WebApplicationException}, que es una
 * {@code RuntimeException} y revierte la transacción del login. Eso arrastraba
 * el INSERT del {@code LOGIN_FAILED} y lo borraba: en la base de desarrollo
 * había 247 {@code LOGIN_SUCCESS} y **cero** {@code LOGIN_FAILED}, con lo que un
 * ataque de fuerza bruta resultaba invisible. La única huella eran los huecos
 * que la secuencia de {@code audit_log} dejaba al revertirse.
 *
 * Un {@code @Transactional(REQUIRES_NEW)} sobre un método del propio use case
 * NO habría servido: al invocarse desde dentro de la misma clase, la llamada no
 * pasa por el interceptor de CDI y se habría ejecutado en la transacción del
 * login igualmente — un arreglo que parece hecho y no hace nada. Al vivir en
 * otro bean inyectado, la llamada sí atraviesa el proxy.
 */
@ApplicationScoped
public class AuthAuditRecorder {

    private final AuditLogRepository auditLogRepository;

    public AuthAuditRecorder(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * La bitácora nunca debe tumbar el flujo que la invoca: si el registro
     * falla, el login sigue su curso. Por eso la excepción se traga aquí, y no
     * en quien llama.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void record(Long userId, String username, String action, String result, String ipAddress) {
        try {
            AuditLog log = new AuditLog(
                    null,
                    userId,
                    username,
                    action,
                    "AUTH",
                    userId != null ? String.valueOf(userId) : "0",
                    ipAddress,
                    String.format("{\"result\": \"%s\"}", result),
                    Instant.now()
            );
            auditLogRepository.save(log);
        } catch (Exception ignored) {
        }
    }
}
