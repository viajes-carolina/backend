package com.viajescarolina.api.settings.domain;

import java.time.Instant;

public class WhatsAppAction {
    private final Integer id;
    private final String actionKey;
    private String label;
    private String messageTemplate;
    private String description;
    private final Instant createdAt;
    private Instant updatedAt;

    public WhatsAppAction(
            Integer id,
            String actionKey,
            String label,
            String messageTemplate,
            String description,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.actionKey = actionKey;
        this.label = label;
        this.messageTemplate = messageTemplate;
        this.description = description;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public void update(String label, String messageTemplate, String description) {
        this.label = label;
        this.messageTemplate = messageTemplate;
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public Integer getId() { return id; }
    public String getActionKey() { return actionKey; }
    public String getLabel() { return label; }
    public String getMessageTemplate() { return messageTemplate; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
