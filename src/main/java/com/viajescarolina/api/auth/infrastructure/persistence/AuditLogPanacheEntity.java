package com.viajescarolina.api.auth.infrastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_log")
public class AuditLogPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "user_id")
    public Long userId;

    @Column(nullable = false, length = 60)
    public String username;

    @Column(nullable = false, length = 100)
    public String action;

    @Column(name = "entity_type", nullable = false, length = 60)
    public String entityType;

    @Column(name = "entity_id", length = 60)
    public String entityId;

    @Column(name = "ip_hash", length = 64)
    public String ipHash;

    @Column(name = "details_json", columnDefinition = "jsonb")
    public String detailsJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        if (username == null) username = "SYSTEM";
        if (detailsJson == null) detailsJson = "{}";
    }
}
