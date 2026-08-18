package com.viajescarolina.api.settings.infrastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "whatsapp_channel")
public class WhatsAppChannelPanacheEntity extends PanacheEntityBase {

    @Id
    public Integer id = 1;

    @Column(name = "e164_number", nullable = false, length = 20)
    public String e164Number;

    @Column(name = "display_number", nullable = false, length = 30)
    public String displayNumber;

    @Column(name = "is_active", nullable = false)
    public boolean active = true;

    @Column(name = "revision", nullable = false)
    public int revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt = Instant.now();
}
