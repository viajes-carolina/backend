package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomeTestimonialsSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "home_testimonials_section")
public class HomeTestimonialsSectionPanacheEntity {

    @Id
    @Column(name = "id")
    public Long id = 1L;

    @Column(name = "badge_text", nullable = false)
    public String badgeText;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "subtitle", nullable = false, columnDefinition = "TEXT")
    public String subtitle;

    @Column(name = "blob_media_id")
    public Long blobMediaId;

    @Column(name = "blob_media_url", length = 500)
    public String blobMediaUrl;

    @Column(name = "blob_focal_x")
    public Double blobFocalX;

    @Column(name = "blob_focal_y")
    public Double blobFocalY;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public static HomeTestimonialsSectionPanacheEntity fromDomain(HomeTestimonialsSection domain) {
        HomeTestimonialsSectionPanacheEntity entity = new HomeTestimonialsSectionPanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1L;
        entity.badgeText = domain.getBadgeText();
        entity.title = domain.getTitle();
        entity.subtitle = domain.getSubtitle();
        entity.blobMediaId = domain.getBlobMediaId();
        entity.blobMediaUrl = domain.getBlobMediaUrl();
        entity.blobFocalX = domain.getBlobFocalX();
        entity.blobFocalY = domain.getBlobFocalY();
        entity.createdAt = Instant.now();
        entity.updatedAt = Instant.now();
        return entity;
    }

    public HomeTestimonialsSection toDomain() {
        HomeTestimonialsSection domain = new HomeTestimonialsSection(
                id,
                badgeText,
                title,
                subtitle
        );
        domain.setBlobMediaId(blobMediaId);
        domain.setBlobMediaUrl(blobMediaUrl);
        domain.setBlobFocalX(blobFocalX);
        domain.setBlobFocalY(blobFocalY);
        return domain;
    }
}
