package com.viajescarolina.api.common.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.lang.reflect.Method;
import java.time.Instant;

/**
 * Registra en audit_log toda invocación exitosa de un método anotado con @Audited.
 * Solo se ejecuta si ctx.proceed() no lanza excepción (una mutación fallida, cuya
 * transacción se revierte, no debe dejar una entrada de auditoría engañosa).
 * Cualquier fallo al registrar la auditoría en sí (reflexión, serialización, DB)
 * se atrapa y se loguea como warning, sin afectar la respuesta real al cliente.
 */
@Audited(action = "", entityType = "")
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class AuditInterceptor {

    private static final Logger LOG = Logger.getLogger(AuditInterceptor.class);

    @Inject
    AuditLogRepository auditLogRepository;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    JsonWebToken jwt;

    @AroundInvoke
    public Object audit(InvocationContext ctx) throws Exception {
        Object result = ctx.proceed();

        try {
            Audited meta = ctx.getMethod().getAnnotation(Audited.class);
            if (meta == null) {
                return result;
            }

            String username = jwt != null ? jwt.getClaim("upn") : null;
            if (username == null || username.isBlank()) {
                username = "SYSTEM";
            }

            String entityId = extractEntityId(result, ctx.getParameters());
            String detailsJson = serializeDetails(result, ctx.getParameters());

            AuditLog log = new AuditLog(
                    null,
                    null,
                    username,
                    meta.action(),
                    meta.entityType(),
                    entityId,
                    null,
                    detailsJson,
                    Instant.now()
            );
            auditLogRepository.save(log);
        } catch (Exception e) {
            LOG.warnf(e, "No se pudo registrar auditoría para %s", ctx.getMethod());
        }

        return result;
    }

    private String extractEntityId(Object result, Object[] params) {
        String fromResult = idFrom(result);
        if (fromResult != null) {
            return fromResult;
        }
        if (params != null) {
            for (Object p : params) {
                if (p instanceof Long || p instanceof Integer) {
                    return String.valueOf(p);
                }
            }
        }
        return null;
    }

    private String idFrom(Object target) {
        if (target == null) {
            return null;
        }
        for (String accessor : new String[] {"id", "getId"}) {
            try {
                Method m = target.getClass().getMethod(accessor);
                Object value = m.invoke(target);
                if (value != null) {
                    return String.valueOf(value);
                }
            } catch (Exception ignored) {
                // el accessor no existe o no aplica; se prueba el siguiente
            }
        }
        return null;
    }

    private String serializeDetails(Object result, Object[] params) {
        try {
            if (result != null) {
                return objectMapper.writeValueAsString(result);
            }
            if (params != null && params.length > 0) {
                return objectMapper.writeValueAsString(params);
            }
        } catch (Exception e) {
            LOG.warnf(e, "No se pudo serializar el detalle de auditoría");
        }
        return "{}";
    }
}
