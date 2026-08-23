package com.viajescarolina.api.home.domain;

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
    private String backgroundMediaUrl;
    private Double backgroundFocalX;
    private Double backgroundFocalY;
    private Long secondaryMedia1Id;
    private String secondaryMedia1Url;
    private Double secondaryMedia1FocalX;
    private Double secondaryMedia1FocalY;
    private Long secondaryMedia2Id;
    private String secondaryMedia2Url;
    private Double secondaryMedia2FocalX;
    private Double secondaryMedia2FocalY;
    private Long secondaryMedia3Id;
    private String secondaryMedia3Url;
    private Double secondaryMedia3FocalX;
    private Double secondaryMedia3FocalY;
    private String trustStatText;
    private String eyebrowText;
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
            String backgroundMediaUrl,
            Double backgroundFocalX,
            Double backgroundFocalY,
            Long secondaryMedia1Id,
            String secondaryMedia1Url,
            Double secondaryMedia1FocalX,
            Double secondaryMedia1FocalY,
            Long secondaryMedia2Id,
            String secondaryMedia2Url,
            Double secondaryMedia2FocalX,
            Double secondaryMedia2FocalY,
            Long secondaryMedia3Id,
            String secondaryMedia3Url,
            Double secondaryMedia3FocalX,
            Double secondaryMedia3FocalY,
            String trustStatText,
            String eyebrowText,
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
        this.backgroundMediaUrl = backgroundMediaUrl;
        this.backgroundFocalX = backgroundFocalX != null ? backgroundFocalX : 50.0;
        this.backgroundFocalY = backgroundFocalY != null ? backgroundFocalY : 50.0;
        this.secondaryMedia1Id = secondaryMedia1Id;
        this.secondaryMedia1Url = secondaryMedia1Url;
        this.secondaryMedia1FocalX = secondaryMedia1FocalX != null ? secondaryMedia1FocalX : 50.0;
        this.secondaryMedia1FocalY = secondaryMedia1FocalY != null ? secondaryMedia1FocalY : 50.0;
        this.secondaryMedia2Id = secondaryMedia2Id;
        this.secondaryMedia2Url = secondaryMedia2Url;
        this.secondaryMedia2FocalX = secondaryMedia2FocalX != null ? secondaryMedia2FocalX : 50.0;
        this.secondaryMedia2FocalY = secondaryMedia2FocalY != null ? secondaryMedia2FocalY : 50.0;
        this.secondaryMedia3Id = secondaryMedia3Id;
        this.secondaryMedia3Url = secondaryMedia3Url;
        this.secondaryMedia3FocalX = secondaryMedia3FocalX != null ? secondaryMedia3FocalX : 50.0;
        this.secondaryMedia3FocalY = secondaryMedia3FocalY != null ? secondaryMedia3FocalY : 50.0;
        this.trustStatText = trustStatText;
        this.eyebrowText = eyebrowText;
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
            String backgroundMediaUrl,
            Double backgroundFocalX,
            Double backgroundFocalY,
            Long secondaryMedia1Id,
            String secondaryMedia1Url,
            Double secondaryMedia1FocalX,
            Double secondaryMedia1FocalY,
            Long secondaryMedia2Id,
            String secondaryMedia2Url,
            Double secondaryMedia2FocalX,
            Double secondaryMedia2FocalY,
            Long secondaryMedia3Id,
            String secondaryMedia3Url,
            Double secondaryMedia3FocalX,
            Double secondaryMedia3FocalY,
            String trustStatText,
            String eyebrowText) {
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
        if (backgroundMediaUrl != null && !backgroundMediaUrl.isBlank()) this.backgroundMediaUrl = backgroundMediaUrl;
        if (backgroundFocalX != null) this.backgroundFocalX = backgroundFocalX;
        if (backgroundFocalY != null) this.backgroundFocalY = backgroundFocalY;
        this.secondaryMedia1Id = secondaryMedia1Id;
        if (secondaryMedia1Url != null && !secondaryMedia1Url.isBlank()) this.secondaryMedia1Url = secondaryMedia1Url;
        if (secondaryMedia1FocalX != null) this.secondaryMedia1FocalX = secondaryMedia1FocalX;
        if (secondaryMedia1FocalY != null) this.secondaryMedia1FocalY = secondaryMedia1FocalY;
        this.secondaryMedia2Id = secondaryMedia2Id;
        if (secondaryMedia2Url != null && !secondaryMedia2Url.isBlank()) this.secondaryMedia2Url = secondaryMedia2Url;
        if (secondaryMedia2FocalX != null) this.secondaryMedia2FocalX = secondaryMedia2FocalX;
        if (secondaryMedia2FocalY != null) this.secondaryMedia2FocalY = secondaryMedia2FocalY;
        this.secondaryMedia3Id = secondaryMedia3Id;
        if (secondaryMedia3Url != null && !secondaryMedia3Url.isBlank()) this.secondaryMedia3Url = secondaryMedia3Url;
        if (secondaryMedia3FocalX != null) this.secondaryMedia3FocalX = secondaryMedia3FocalX;
        if (secondaryMedia3FocalY != null) this.secondaryMedia3FocalY = secondaryMedia3FocalY;
        this.trustStatText = trustStatText;
        this.eyebrowText = eyebrowText;
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
    public String getBackgroundMediaUrl() { return backgroundMediaUrl; }
    public Double getBackgroundFocalX() { return backgroundFocalX; }
    public Double getBackgroundFocalY() { return backgroundFocalY; }
    public Long getSecondaryMedia1Id() { return secondaryMedia1Id; }
    public String getSecondaryMedia1Url() { return secondaryMedia1Url; }
    public Double getSecondaryMedia1FocalX() { return secondaryMedia1FocalX; }
    public Double getSecondaryMedia1FocalY() { return secondaryMedia1FocalY; }
    public Long getSecondaryMedia2Id() { return secondaryMedia2Id; }
    public String getSecondaryMedia2Url() { return secondaryMedia2Url; }
    public Double getSecondaryMedia2FocalX() { return secondaryMedia2FocalX; }
    public Double getSecondaryMedia2FocalY() { return secondaryMedia2FocalY; }
    public Long getSecondaryMedia3Id() { return secondaryMedia3Id; }
    public String getSecondaryMedia3Url() { return secondaryMedia3Url; }
    public Double getSecondaryMedia3FocalX() { return secondaryMedia3FocalX; }
    public Double getSecondaryMedia3FocalY() { return secondaryMedia3FocalY; }
    public String getTrustStatText() { return trustStatText; }
    public String getEyebrowText() { return eyebrowText; }
    public Integer getRevision() { return revision; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
