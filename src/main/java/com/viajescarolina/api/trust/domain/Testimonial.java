package com.viajescarolina.api.trust.domain;

import java.time.Instant;

public class Testimonial {

    private Long id;
    private String clientName;
    private String clientLocation;
    private String tripDestination;
    private String comment;
    private Integer rating;
    private Long avatarMediaId;
    private boolean consentConfirmed;
    private Integer displayOrder;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public Testimonial(
            Long id,
            String clientName,
            String clientLocation,
            String tripDestination,
            String comment,
            Integer rating,
            Long avatarMediaId,
            boolean consentConfirmed,
            Integer displayOrder,
            boolean active,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.clientName = clientName;
        this.clientLocation = clientLocation;
        this.tripDestination = tripDestination;
        this.comment = comment;
        this.rating = (rating != null && rating >= 1 && rating <= 5) ? rating : 5;
        this.avatarMediaId = avatarMediaId;
        this.consentConfirmed = consentConfirmed;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.active = active;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public static Testimonial create(
            String clientName,
            String clientLocation,
            String tripDestination,
            String comment,
            Integer rating,
            Long avatarMediaId,
            boolean consentConfirmed,
            Integer displayOrder) {
        return new Testimonial(
                null,
                clientName,
                clientLocation,
                tripDestination,
                comment,
                rating,
                avatarMediaId,
                consentConfirmed,
                displayOrder,
                true,
                Instant.now(),
                Instant.now()
        );
    }

    public void update(
            String clientName,
            String clientLocation,
            String tripDestination,
            String comment,
            Integer rating,
            Long avatarMediaId,
            boolean consentConfirmed,
            Integer displayOrder,
            boolean active) {
        this.clientName = clientName;
        this.clientLocation = clientLocation;
        this.tripDestination = tripDestination;
        this.comment = comment;
        this.rating = (rating != null && rating >= 1 && rating <= 5) ? rating : 5;
        this.avatarMediaId = avatarMediaId;
        this.consentConfirmed = consentConfirmed;
        this.displayOrder = displayOrder != null ? displayOrder : this.displayOrder;
        this.active = active;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getClientName() { return clientName; }
    public String getClientLocation() { return clientLocation; }
    public String getTripDestination() { return tripDestination; }
    public String getComment() { return comment; }
    public Integer getRating() { return rating; }
    public Long getAvatarMediaId() { return avatarMediaId; }
    public boolean isConsentConfirmed() { return consentConfirmed; }
    public Integer getDisplayOrder() { return displayOrder; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
