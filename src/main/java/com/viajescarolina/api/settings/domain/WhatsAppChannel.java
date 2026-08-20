package com.viajescarolina.api.settings.domain;

import java.time.Instant;

public class WhatsAppChannel {
    private final Integer id;
    private String label;
    private String e164Number;
    private String displayNumber;
    private String defaultMessage;
    private boolean isPrimary;
    private boolean active;
    private int revision;
    private final Instant createdAt;
    private Instant updatedAt;

    public WhatsAppChannel(
            Integer id,
            String label,
            String e164Number,
            String displayNumber,
            String defaultMessage,
            boolean isPrimary,
            boolean active,
            int revision,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.label = label;
        this.e164Number = e164Number;
        this.displayNumber = displayNumber;
        this.defaultMessage = defaultMessage;
        this.isPrimary = isPrimary;
        this.active = active;
        this.revision = revision;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public void update(String label, String e164Number, String displayNumber, String defaultMessage, boolean active) {
        this.label = label;
        this.e164Number = e164Number;
        this.displayNumber = displayNumber;
        this.defaultMessage = defaultMessage;
        this.active = active;
        this.revision++;
        this.updatedAt = Instant.now();
    }

    public Integer getId() { return id; }
    public String getLabel() { return label; }
    public String getE164Number() { return e164Number; }
    public String getDisplayNumber() { return displayNumber; }
    public String getDefaultMessage() { return defaultMessage; }
    public boolean isPrimary() { return isPrimary; }
    public boolean isActive() { return active; }
    public int getRevision() { return revision; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
