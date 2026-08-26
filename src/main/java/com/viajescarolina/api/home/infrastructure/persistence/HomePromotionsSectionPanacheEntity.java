package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomePromotionsSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "home_promotions_section")
public class HomePromotionsSectionPanacheEntity {

    @Id
    @Column(name = "id")
    public Long id = 1L;

    @Column(name = "badge_text", nullable = false)
    public String badgeText;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "subtitle", nullable = false, columnDefinition = "TEXT")
    public String subtitle;

    @Column(name = "bottom_cta_question", nullable = false)
    public String bottomCtaQuestion;

    @Column(name = "bottom_cta_eyebrow", nullable = false)
    public String bottomCtaEyebrow;

    @Column(name = "bottom_cta_copy", nullable = false, columnDefinition = "TEXT")
    public String bottomCtaCopy;

    @Column(name = "bottom_cta_whatsapp_text", nullable = false)
    public String bottomCtaWhatsappText;

    @Column(name = "bottom_cta_whatsapp_message", nullable = false)
    public String bottomCtaWhatsappMessage;

    @Column(name = "media_id")
    public Long mediaId;

    @Column(name = "media_url", length = 500)
    public String mediaUrl;

    @Column(name = "media_focal_x")
    public Double mediaFocalX = 50.0;

    @Column(name = "media_focal_y")
    public Double mediaFocalY = 50.0;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public static HomePromotionsSectionPanacheEntity fromDomain(HomePromotionsSection domain) {
        HomePromotionsSectionPanacheEntity entity = new HomePromotionsSectionPanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1L;
        entity.badgeText = domain.getBadgeText();
        entity.title = domain.getTitle();
        entity.subtitle = domain.getSubtitle();
        entity.bottomCtaQuestion = domain.getBottomCtaQuestion();
        entity.bottomCtaEyebrow = domain.getBottomCtaEyebrow();
        entity.bottomCtaCopy = domain.getBottomCtaCopy();
        entity.bottomCtaWhatsappText = domain.getBottomCtaWhatsappText();
        entity.bottomCtaWhatsappMessage = domain.getBottomCtaWhatsappMessage();
        entity.mediaId = domain.getMediaId();
        entity.mediaUrl = domain.getMediaUrl();
        entity.mediaFocalX = domain.getMediaFocalX() != null ? domain.getMediaFocalX() : 50.0;
        entity.mediaFocalY = domain.getMediaFocalY() != null ? domain.getMediaFocalY() : 50.0;
        entity.createdAt = Instant.now();
        entity.updatedAt = Instant.now();
        return entity;
    }

    public HomePromotionsSection toDomain() {
        HomePromotionsSection domain = new HomePromotionsSection(
                id,
                badgeText,
                title,
                subtitle,
                bottomCtaQuestion,
                bottomCtaWhatsappText,
                bottomCtaWhatsappMessage,
                mediaId,
                mediaUrl,
                mediaFocalX,
                mediaFocalY
        );
        domain.setBottomCtaEyebrow(bottomCtaEyebrow);
        domain.setBottomCtaCopy(bottomCtaCopy);
        return domain;
    }
}
