package com.viajescarolina.api.auth.domain;

import java.util.List;

public interface AuditLogRepository {
    List<AuditLog> listRecent(int limit);
    List<AuditLog> listByEntityType(String entityType, int limit);
    AuditLog save(AuditLog log);
}
