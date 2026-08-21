package com.viajescarolina.api.home.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.home.domain.HomeHero;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "home_hero")
public class HomeHeroPanacheEntity extends PanacheEntityBase {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    public Integer id = 1;

    @Column(name = "badge_text", nullable = false)
    public String badgeText;

    @Column(name = "title_highlight", nullable = false)
    public String titleHighlight;

    @Column(name = "title_accent", nullable = false)
    public String titleAccent;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    public String description;

    @Column(name = "whatsapp_cta_text", nullable = false)
    public String whatsappCtaText;

    @Column(name = "whatsapp_message_override")
    public String whatsappMessageOverride;

    @Column(name = "secondary_cta_text")
    public String secondaryCtaText;

    @Column(name = "secondary_cta_url")
    public String secondaryCtaUrl;

    @Column(name = "trust_indicators", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    public String trustIndicators;

    @Column(name = "background_media_id")
    public Long backgroundMediaId;

    @Column(name = "background_media_url", length = 500)
    public String backgroundMediaUrl;

    @Column(name = "background_focal_x")
    public Double backgroundFocalX = 50.0;

    @Column(name = "background_focal_y")
    public Double backgroundFocalY = 50.0;

    @Column(name = "featured_card_badge")
    public String featuredCardBadge;

    @Column(name = "featured_card_title")
    public String featuredCardTitle;

    @Column(name = "featured_card_subtitle")
    public String featuredCardSubtitle;

    @Column(name = "featured_card_price_pen")
    public BigDecimal featuredCardPricePen;

    @Column(name = "featured_card_origin")
    public String featuredCardOrigin;

    @Column(name = "featured_card_media_id")
    public Long featuredCardMediaId;

    @Column(name = "featured_card_media_url", length = 500)
    public String featuredCardMediaUrl;

    @Column(name = "secondary_media_1_id")
    public Long secondaryMedia1Id;

    @Column(name = "secondary_media_1_url", length = 500)
    public String secondaryMedia1Url;

    @Column(name = "secondary_media_1_focal_x")
    public Double secondaryMedia1FocalX = 50.0;

    @Column(name = "secondary_media_1_focal_y")
    public Double secondaryMedia1FocalY = 50.0;

    @Column(name = "secondary_media_2_id")
    public Long secondaryMedia2Id;

    @Column(name = "secondary_media_2_url", length = 500)
    public String secondaryMedia2Url;

    @Column(name = "secondary_media_2_focal_x")
    public Double secondaryMedia2FocalX = 50.0;

    @Column(name = "secondary_media_2_focal_y")
    public Double secondaryMedia2FocalY = 50.0;

    @Column(name = "secondary_media_3_id")
    public Long secondaryMedia3Id;

    @Column(name = "secondary_media_3_url", length = 500)
    public String secondaryMedia3Url;

    @Column(name = "secondary_media_3_focal_x")
    public Double secondaryMedia3FocalX = 50.0;

    @Column(name = "secondary_media_3_focal_y")
    public Double secondaryMedia3FocalY = 50.0;

    @Column(name = "secondary_media_4_id")
    public Long secondaryMedia4Id;

    @Column(name = "secondary_media_4_url", length = 500)
    public String secondaryMedia4Url;

    @Column(name = "secondary_media_4_focal_x")
    public Double secondaryMedia4FocalX = 50.0;

    @Column(name = "secondary_media_4_focal_y")
    public Double secondaryMedia4FocalY = 50.0;

    @Column(name = "trust_stat_text")
    public String trustStatText;

    @Column(name = "eyebrow_text")
    public String eyebrowText;

    @Column(name = "revision", nullable = false)
    public Integer revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public HomeHero toDomain() {
        List<String> list = new ArrayList<>();
        if (trustIndicators != null && !trustIndicators.isBlank()) {
            try {
                list = OBJECT_MAPPER.readValue(trustIndicators, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                list = List.of("Asesoría sin costo", "Respuesta rápida", "Acompañamiento real");
            }
        }
        return new HomeHero(
                id,
                badgeText,
                titleHighlight,
                titleAccent,
                description,
                whatsappCtaText,
                whatsappMessageOverride,
                secondaryCtaText,
                secondaryCtaUrl,
                list,
                backgroundMediaId,
                backgroundMediaUrl,
                backgroundFocalX,
                backgroundFocalY,
                featuredCardBadge,
                featuredCardTitle,
                featuredCardSubtitle,
                featuredCardPricePen,
                featuredCardOrigin,
                featuredCardMediaId,
                featuredCardMediaUrl,
                secondaryMedia1Id,
                secondaryMedia1Url,
                secondaryMedia1FocalX,
                secondaryMedia1FocalY,
                secondaryMedia2Id,
                secondaryMedia2Url,
                secondaryMedia2FocalX,
                secondaryMedia2FocalY,
                secondaryMedia3Id,
                secondaryMedia3Url,
                secondaryMedia3FocalX,
                secondaryMedia3FocalY,
                secondaryMedia4Id,
                secondaryMedia4Url,
                secondaryMedia4FocalX,
                secondaryMedia4FocalY,
                trustStatText,
                eyebrowText,
                revision,
                createdAt,
                updatedAt
        );
    }

    public static HomeHeroPanacheEntity fromDomain(HomeHero domain) {
        HomeHeroPanacheEntity entity = new HomeHeroPanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1;
        entity.badgeText = domain.getBadgeText();
        entity.titleHighlight = domain.getTitleHighlight();
        entity.titleAccent = domain.getTitleAccent();
        entity.description = domain.getDescription();
        entity.whatsappCtaText = domain.getWhatsappCtaText();
        entity.whatsappMessageOverride = domain.getWhatsappMessageOverride();
        entity.secondaryCtaText = domain.getSecondaryCtaText();
        entity.secondaryCtaUrl = domain.getSecondaryCtaUrl();
        try {
            entity.trustIndicators = OBJECT_MAPPER.writeValueAsString(domain.getTrustIndicators());
        } catch (Exception e) {
            entity.trustIndicators = "[]";
        }
        entity.backgroundMediaId = domain.getBackgroundMediaId();
        entity.backgroundMediaUrl = domain.getBackgroundMediaUrl();
        entity.backgroundFocalX = domain.getBackgroundFocalX();
        entity.backgroundFocalY = domain.getBackgroundFocalY();
        entity.featuredCardBadge = domain.getFeaturedCardBadge();
        entity.featuredCardTitle = domain.getFeaturedCardTitle();
        entity.featuredCardSubtitle = domain.getFeaturedCardSubtitle();
        entity.featuredCardPricePen = domain.getFeaturedCardPricePen();
        entity.featuredCardOrigin = domain.getFeaturedCardOrigin();
        entity.featuredCardMediaId = domain.getFeaturedCardMediaId();
        entity.featuredCardMediaUrl = domain.getFeaturedCardMediaUrl();
        entity.secondaryMedia1Id = domain.getSecondaryMedia1Id();
        entity.secondaryMedia1Url = domain.getSecondaryMedia1Url();
        entity.secondaryMedia1FocalX = domain.getSecondaryMedia1FocalX();
        entity.secondaryMedia1FocalY = domain.getSecondaryMedia1FocalY();
        entity.secondaryMedia2Id = domain.getSecondaryMedia2Id();
        entity.secondaryMedia2Url = domain.getSecondaryMedia2Url();
        entity.secondaryMedia2FocalX = domain.getSecondaryMedia2FocalX();
        entity.secondaryMedia2FocalY = domain.getSecondaryMedia2FocalY();
        entity.secondaryMedia3Id = domain.getSecondaryMedia3Id();
        entity.secondaryMedia3Url = domain.getSecondaryMedia3Url();
        entity.secondaryMedia3FocalX = domain.getSecondaryMedia3FocalX();
        entity.secondaryMedia3FocalY = domain.getSecondaryMedia3FocalY();
        entity.secondaryMedia4Id = domain.getSecondaryMedia4Id();
        entity.secondaryMedia4Url = domain.getSecondaryMedia4Url();
        entity.secondaryMedia4FocalX = domain.getSecondaryMedia4FocalX();
        entity.secondaryMedia4FocalY = domain.getSecondaryMedia4FocalY();
        entity.trustStatText = domain.getTrustStatText();
        entity.eyebrowText = domain.getEyebrowText();
        entity.revision = domain.getRevision();
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }
}
