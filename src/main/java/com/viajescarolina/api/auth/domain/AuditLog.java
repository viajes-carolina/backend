package com.viajescarolina.api.auth.domain;

import java.time.Instant;

public class AuditLog {
    private Long id;
    private Long userId;
    private String username;
    private String action;
    private String entityType;
    private String entityId;
    private String ipHash;
    private String detailsJson;
    private Instant createdAt;

    public AuditLog() {}

    public AuditLog(Long id, Long userId, String username, String action, String entityType, String entityId, String ipHash, String detailsJson, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username != null ? username : "SYSTEM";
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.ipHash = ipHash;
        this.detailsJson = detailsJson != null ? detailsJson : "{}";
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public String getIpHash() { return ipHash; }
    public void setIpHash(String ipHash) { this.ipHash = ipHash; }

    public String getDetailsJson() { return detailsJson; }
    public void setDetailsJson(String detailsJson) { this.detailsJson = detailsJson; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
