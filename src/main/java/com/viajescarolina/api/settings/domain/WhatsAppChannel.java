package com.viajescarolina.api.settings.domain;

import java.time.Instant;

public class WhatsAppChannel {
    private final Integer id;
    private String e164Number;
    private String displayNumber;
    private boolean active;
    private int revision;
    private final Instant createdAt;
    private Instant updatedAt;

    public WhatsAppChannel(
            Integer id,
            String e164Number,
            String displayNumber,
            boolean active,
            int revision,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.e164Number = e164Number;
        this.displayNumber = displayNumber;
        this.active = active;
        this.revision = revision;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public void update(String e164Number, String displayNumber, boolean active) {
        this.e164Number = e164Number;
        this.displayNumber = displayNumber;
        this.active = active;
        this.revision++;
        this.updatedAt = Instant.now();
    }

    public Integer getId() { return id; }
    public String getE164Number() { return e164Number; }
    public String getDisplayNumber() { return displayNumber; }
    public boolean isActive() { return active; }
    public int getRevision() { return revision; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
