package com.viajescarolina.api.trust.infrastructure.persistence;

import com.viajescarolina.api.trust.domain.Testimonial;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "testimonial")
public class TestimonialPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "client_name", nullable = false)
    public String clientName;

    @Column(name = "client_location")
    public String clientLocation;

    @Column(name = "trip_destination", nullable = false)
    public String tripDestination;

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    public String comment;

    @Column(name = "rating", nullable = false)
    public Integer rating = 5;

    @Column(name = "avatar_media_id")
    public Long avatarMediaId;

    @Column(name = "consent_confirmed", nullable = false)
    public boolean consentConfirmed = true;

    @Column(name = "display_order", nullable = false)
    public Integer displayOrder = 0;

    @Column(name = "active", nullable = false)
    public boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public Testimonial toDomain() {
        return new Testimonial(
                id,
                clientName,
                clientLocation,
                tripDestination,
                comment,
                rating,
                avatarMediaId,
                consentConfirmed,
                displayOrder,
                active,
                createdAt,
                updatedAt
        );
    }

    public static TestimonialPanacheEntity fromDomain(Testimonial domain) {
        TestimonialPanacheEntity entity = new TestimonialPanacheEntity();
        entity.id = domain.getId();
        entity.clientName = domain.getClientName();
        entity.clientLocation = domain.getClientLocation();
        entity.tripDestination = domain.getTripDestination();
        entity.comment = domain.getComment();
        entity.rating = domain.getRating();
        entity.avatarMediaId = domain.getAvatarMediaId();
        entity.consentConfirmed = domain.isConsentConfirmed();
        entity.displayOrder = domain.getDisplayOrder();
        entity.active = domain.isActive();
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }
}
