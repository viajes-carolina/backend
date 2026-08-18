package com.viajescarolina.api.auth.application.usecase;

import com.viajescarolina.api.auth.application.dto.AuditLogDTO;
import com.viajescarolina.api.auth.application.dto.CreateAuditLogRequest;
import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ListAuditLogsUseCase {

    private final AuditLogRepository auditLogRepository;

    public ListAuditLogsUseCase(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public List<AuditLogDTO> execute(String entityType, int limit) {
        List<AuditLog> logs;
        if (entityType != null && !entityType.isBlank() && !"ALL".equalsIgnoreCase(entityType)) {
            logs = auditLogRepository.listByEntityType(entityType, limit);
        } else {
            logs = auditLogRepository.listRecent(limit);
        }

        return logs.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AuditLogDTO toDTO(AuditLog l) {
        return new AuditLogDTO(
                l.getId(),
                l.getUserId(),
                l.getUsername(),
                l.getAction(),
                l.getEntityType(),
                l.getEntityId(),
                l.getIpHash(),
                l.getDetailsJson(),
                l.getCreatedAt()
        );
    }
}
