package com.viajescarolina.api.auth.infrastructure.persistence;

import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PanacheAuditLogRepository implements AuditLogRepository, PanacheRepositoryBase<AuditLogPanacheEntity, Long> {

    @Override
    public List<AuditLog> listRecent(int limit) {
        return find("ORDER BY createdAt DESC")
                .page(0, Math.max(1, limit))
                .list()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> listByEntityType(String entityType, int limit) {
        return find("entityType = ?1 ORDER BY createdAt DESC", entityType)
                .page(0, Math.max(1, limit))
                .list()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AuditLog save(AuditLog log) {
        AuditLogPanacheEntity entity = new AuditLogPanacheEntity();
        entity.userId = log.getUserId();
        entity.username = log.getUsername();
        entity.action = log.getAction();
        entity.entityType = log.getEntityType();
        entity.entityId = log.getEntityId();
        entity.ipHash = log.getIpHash();
        entity.detailsJson = log.getDetailsJson();

        persist(entity);
        return toDomain(entity);
    }

    private AuditLog toDomain(AuditLogPanacheEntity e) {
        if (e == null) return null;
        return new AuditLog(
                e.id,
                e.userId,
                e.username,
                e.action,
                e.entityType,
                e.entityId,
                e.ipHash,
                e.detailsJson,
                e.createdAt
        );
    }
}
