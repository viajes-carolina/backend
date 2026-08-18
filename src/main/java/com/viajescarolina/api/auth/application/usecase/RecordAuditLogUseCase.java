package com.viajescarolina.api.auth.application.usecase;

import com.viajescarolina.api.auth.application.dto.AuditLogDTO;
import com.viajescarolina.api.auth.application.dto.CreateAuditLogRequest;
import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;

@ApplicationScoped
public class RecordAuditLogUseCase {

    private final AuditLogRepository auditLogRepository;

    public RecordAuditLogUseCase(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AuditLogDTO execute(CreateAuditLogRequest req) {
        String ipHash = null;
        if (req.ipAddress() != null && !req.ipAddress().isBlank()) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(req.ipAddress().getBytes(StandardCharsets.UTF_8));
                ipHash = HexFormat.of().formatHex(hash);
            } catch (Exception ignored) {}
        }

        AuditLog log = new AuditLog(
                null,
                req.userId(),
                req.username() != null ? req.username() : "SYSTEM",
                req.action(),
                req.entityType(),
                req.entityId(),
                ipHash,
                req.detailsJson() != null ? req.detailsJson() : "{}",
                Instant.now()
        );

        AuditLog saved = auditLogRepository.save(log);

        return new AuditLogDTO(
                saved.getId(),
                saved.getUserId(),
                saved.getUsername(),
                saved.getAction(),
                saved.getEntityType(),
                saved.getEntityId(),
                saved.getIpHash(),
                saved.getDetailsJson(),
                saved.getCreatedAt()
        );
    }
}
