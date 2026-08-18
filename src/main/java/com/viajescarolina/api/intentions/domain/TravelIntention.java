package com.viajescarolina.api.intentions.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TravelIntention {

    private Long id;
    private String slug;
    private String title;
    private String tagline;
    private String iconName;
    private List<String> featuredDestinations;
    private String whatsappMessageTemplate;
    private Long coverMediaId;
    private Integer displayOrder;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public TravelIntention(
            Long id,
            String slug,
            String title,
            String tagline,
            String iconName,
            List<String> featuredDestinations,
            String whatsappMessageTemplate,
            Long coverMediaId,
            Integer displayOrder,
            boolean active,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.tagline = tagline;
        this.iconName = iconName != null ? iconName : "SunIcon";
        this.featuredDestinations = featuredDestinations != null ? featuredDestinations : new ArrayList<>();
        this.whatsappMessageTemplate = whatsappMessageTemplate;
        this.coverMediaId = coverMediaId;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.active = active;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public static TravelIntention create(
            String slug,
            String title,
            String tagline,
            String iconName,
            List<String> featuredDestinations,
            String whatsappMessageTemplate,
            Long coverMediaId,
            Integer displayOrder) {
        return new TravelIntention(
                null,
                slug,
                title,
                tagline,
                iconName,
                featuredDestinations,
                whatsappMessageTemplate,
                coverMediaId,
                displayOrder,
                true,
                Instant.now(),
                Instant.now()
        );
    }

    public void update(
            String slug,
            String title,
            String tagline,
            String iconName,
            List<String> featuredDestinations,
            String whatsappMessageTemplate,
            Long coverMediaId,
            Integer displayOrder,
            boolean active) {
        this.slug = slug;
        this.title = title;
        this.tagline = tagline;
        this.iconName = iconName;
        this.featuredDestinations = featuredDestinations;
        this.whatsappMessageTemplate = whatsappMessageTemplate;
        this.coverMediaId = coverMediaId;
        this.displayOrder = displayOrder;
        this.active = active;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getTitle() { return title; }
    public String getTagline() { return tagline; }
    public String getIconName() { return iconName; }
    public List<String> getFeaturedDestinations() { return featuredDestinations; }
    public String getWhatsappMessageTemplate() { return whatsappMessageTemplate; }
    public Long getCoverMediaId() { return coverMediaId; }
    public Integer getDisplayOrder() { return displayOrder; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
