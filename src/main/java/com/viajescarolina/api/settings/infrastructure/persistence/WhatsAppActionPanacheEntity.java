package com.viajescarolina.api.settings.infrastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "whatsapp_action")
public class WhatsAppActionPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "action_key", nullable = false, unique = true, length = 50)
    public String actionKey;

    @Column(name = "label", nullable = false, length = 100)
    public String label;

    @Column(name = "message_template", nullable = false, columnDefinition = "TEXT")
    public String messageTemplate;

    @Column(name = "description", length = 255)
    public String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt = Instant.now();
}
