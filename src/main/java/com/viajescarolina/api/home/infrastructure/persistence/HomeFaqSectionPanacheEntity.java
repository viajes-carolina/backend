package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomeFaqSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "home_faq_section")
public class HomeFaqSectionPanacheEntity {

    @Id
    @Column(name = "id")
    public Long id = 1L;

    @Column(name = "badge_text", nullable = false)
    public String badgeText;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "subtitle", nullable = false, columnDefinition = "TEXT")
    public String subtitle;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public static HomeFaqSectionPanacheEntity fromDomain(HomeFaqSection domain) {
        HomeFaqSectionPanacheEntity entity = new HomeFaqSectionPanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1L;
        entity.badgeText = domain.getBadgeText();
        entity.title = domain.getTitle();
        entity.subtitle = domain.getSubtitle();
        entity.createdAt = Instant.now();
        entity.updatedAt = Instant.now();
        return entity;
    }

    public HomeFaqSection toDomain() {
        return new HomeFaqSection(
                id,
                badgeText,
                title,
                subtitle
        );
    }
}
