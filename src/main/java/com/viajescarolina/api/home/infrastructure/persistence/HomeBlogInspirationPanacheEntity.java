package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomeBlogInspiration;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "home_blog_inspiration")
public class HomeBlogInspirationPanacheEntity {

    @Id
    @Column(name = "id")
    public Long id = 1L;

    @Column(name = "badge_text", nullable = false)
    public String badgeText;

    @Column(name = "title_highlight", nullable = false)
    public String titleHighlight;

    @Column(name = "title_accent", nullable = false)
    public String titleAccent;

    @Column(name = "subtitle", nullable = false, columnDefinition = "TEXT")
    public String subtitle;

    @Column(name = "cta_text", nullable = false)
    public String ctaText;

    @Column(name = "cta_url", nullable = false)
    public String ctaUrl;

    @Column(name = "posts_limit", nullable = false)
    public Integer postsLimit;

    @Column(name = "active", nullable = false)
    public Boolean active;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public static HomeBlogInspirationPanacheEntity fromDomain(HomeBlogInspiration domain) {
        HomeBlogInspirationPanacheEntity entity = new HomeBlogInspirationPanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1L;
        entity.badgeText = domain.getBadgeText();
        entity.titleHighlight = domain.getTitleHighlight();
        entity.titleAccent = domain.getTitleAccent();
        entity.subtitle = domain.getSubtitle();
        entity.ctaText = domain.getCtaText();
        entity.ctaUrl = domain.getCtaUrl();
        entity.postsLimit = domain.getPostsLimit() != null ? domain.getPostsLimit() : 3;
        entity.active = domain.getActive() != null ? domain.getActive() : true;
        entity.createdAt = Instant.now();
        entity.updatedAt = Instant.now();
        return entity;
    }

    public HomeBlogInspiration toDomain() {
        return new HomeBlogInspiration(
                id,
                badgeText,
                titleHighlight,
                titleAccent,
                subtitle,
                ctaText,
                ctaUrl,
                postsLimit,
                active
        );
    }
}
