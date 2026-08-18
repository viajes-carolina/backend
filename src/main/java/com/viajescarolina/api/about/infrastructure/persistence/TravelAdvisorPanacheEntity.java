package com.viajescarolina.api.about.infrastructure.persistence;

import com.viajescarolina.api.about.domain.TravelAdvisor;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "travel_advisor")
public class TravelAdvisorPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "full_name", nullable = false)
    public String fullName;

    @Column(name = "role_title", nullable = false)
    public String roleTitle;

    @Column(name = "specialty", nullable = false)
    public String specialty;

    @Column(name = "bio", nullable = false, columnDefinition = "TEXT")
    public String bio;

    @Column(name = "photo_media_id")
    public Long photoMediaId;

    @Column(name = "whatsapp_phone", length = 50)
    public String whatsappPhone;

    @Column(name = "whatsapp_message_template", columnDefinition = "TEXT")
    public String whatsappMessageTemplate;

    @Column(name = "display_order", nullable = false)
    public int displayOrder = 0;

    @Column(name = "active", nullable = false)
    public boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public TravelAdvisor toDomain(String photoMediaUrl) {
        return new TravelAdvisor(
            id,
            fullName,
            roleTitle,
            specialty,
            bio,
            photoMediaId,
            photoMediaUrl,
            whatsappPhone,
            whatsappMessageTemplate,
            displayOrder,
            active,
            createdAt,
            updatedAt
        );
    }

    public static TravelAdvisorPanacheEntity fromDomain(TravelAdvisor domain) {
        TravelAdvisorPanacheEntity entity = new TravelAdvisorPanacheEntity();
        entity.id = domain.getId();
        entity.fullName = domain.getFullName();
        entity.roleTitle = domain.getRoleTitle();
        entity.specialty = domain.getSpecialty();
        entity.bio = domain.getBio();
        entity.photoMediaId = domain.getPhotoMediaId();
        entity.whatsappPhone = domain.getWhatsappPhone();
        entity.whatsappMessageTemplate = domain.getWhatsappMessageTemplate();
        entity.displayOrder = domain.getDisplayOrder();
        entity.active = domain.isActive();
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        entity.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : Instant.now();
        return entity;
    }
}
