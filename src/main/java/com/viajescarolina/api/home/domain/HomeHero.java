package com.viajescarolina.api.home.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class HomeHero {

    private Integer id;
    private String badgeText;
    private String titleHighlight;
    private String titleAccent;
    private String description;
    private String whatsappCtaText;
    private String whatsappMessageOverride;
    private String secondaryCtaText;
    private String secondaryCtaUrl;
    private List<String> trustIndicators;
    private Long backgroundMediaId;
    private String featuredCardBadge;
    private String featuredCardTitle;
    private String featuredCardSubtitle;
    private BigDecimal featuredCardPricePen;
    private String featuredCardOrigin;
    private Long featuredCardMediaId;
    private Integer revision;
    private Instant createdAt;
    private Instant updatedAt;

    public HomeHero(
            Integer id,
            String badgeText,
            String titleHighlight,
            String titleAccent,
            String description,
            String whatsappCtaText,
            String whatsappMessageOverride,
            String secondaryCtaText,
            String secondaryCtaUrl,
            List<String> trustIndicators,
            Long backgroundMediaId,
            String featuredCardBadge,
            String featuredCardTitle,
            String featuredCardSubtitle,
            BigDecimal featuredCardPricePen,
            String featuredCardOrigin,
            Long featuredCardMediaId,
            Integer revision,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id != null ? id : 1;
        this.badgeText = badgeText;
        this.titleHighlight = titleHighlight;
        this.titleAccent = titleAccent;
        this.description = description;
        this.whatsappCtaText = whatsappCtaText;
        this.whatsappMessageOverride = whatsappMessageOverride;
        this.secondaryCtaText = secondaryCtaText;
        this.secondaryCtaUrl = secondaryCtaUrl;
        this.trustIndicators = trustIndicators;
        this.backgroundMediaId = backgroundMediaId;
        this.featuredCardBadge = featuredCardBadge;
        this.featuredCardTitle = featuredCardTitle;
        this.featuredCardSubtitle = featuredCardSubtitle;
        this.featuredCardPricePen = featuredCardPricePen;
        this.featuredCardOrigin = featuredCardOrigin;
        this.featuredCardMediaId = featuredCardMediaId;
        this.revision = revision != null ? revision : 1;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public void update(
            String badgeText,
            String titleHighlight,
            String titleAccent,
            String description,
            String whatsappCtaText,
            String whatsappMessageOverride,
            String secondaryCtaText,
            String secondaryCtaUrl,
            List<String> trustIndicators,
            Long backgroundMediaId,
            String featuredCardBadge,
            String featuredCardTitle,
            String featuredCardSubtitle,
            BigDecimal featuredCardPricePen,
            String featuredCardOrigin,
            Long featuredCardMediaId) {
        this.badgeText = badgeText;
        this.titleHighlight = titleHighlight;
        this.titleAccent = titleAccent;
        this.description = description;
        this.whatsappCtaText = whatsappCtaText;
        this.whatsappMessageOverride = whatsappMessageOverride;
        this.secondaryCtaText = secondaryCtaText;
        this.secondaryCtaUrl = secondaryCtaUrl;
        if (trustIndicators != null) this.trustIndicators = trustIndicators;
        this.backgroundMediaId = backgroundMediaId;
        this.featuredCardBadge = featuredCardBadge;
        this.featuredCardTitle = featuredCardTitle;
        this.featuredCardSubtitle = featuredCardSubtitle;
        this.featuredCardPricePen = featuredCardPricePen;
        this.featuredCardOrigin = featuredCardOrigin;
        this.featuredCardMediaId = featuredCardMediaId;
        this.revision = (this.revision != null ? this.revision : 1) + 1;
        this.updatedAt = Instant.now();
    }

    public Integer getId() { return id; }
    public String getBadgeText() { return badgeText; }
    public String getTitleHighlight() { return titleHighlight; }
    public String getTitleAccent() { return titleAccent; }
    public String getDescription() { return description; }
    public String getWhatsappCtaText() { return whatsappCtaText; }
    public String getWhatsappMessageOverride() { return whatsappMessageOverride; }
    public String getSecondaryCtaText() { return secondaryCtaText; }
    public String getSecondaryCtaUrl() { return secondaryCtaUrl; }
    public List<String> getTrustIndicators() { return trustIndicators; }
    public Long getBackgroundMediaId() { return backgroundMediaId; }
    public String getFeaturedCardBadge() { return featuredCardBadge; }
    public String getFeaturedCardTitle() { return featuredCardTitle; }
    public String getFeaturedCardSubtitle() { return featuredCardSubtitle; }
    public BigDecimal getFeaturedCardPricePen() { return featuredCardPricePen; }
    public String getFeaturedCardOrigin() { return featuredCardOrigin; }
    public Long getFeaturedCardMediaId() { return featuredCardMediaId; }
    public Integer getRevision() { return revision; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
