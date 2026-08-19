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
        entity.revision = domain.getRevision();
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }
}
