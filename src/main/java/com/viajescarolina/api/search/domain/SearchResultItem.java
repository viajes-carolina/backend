package com.viajescarolina.api.search.domain;

public class SearchResultItem {
    private String entityType;
    private Long entityId;
    private String entitySlug;
    private String title;
    private String subtitle;
    private String metadataInfo;
    private String imageUrl;
    private String targetUrl;
    private String badgeText;
    private Double score;

    public SearchResultItem() {
    }

    public SearchResultItem(String entityType, Long entityId, String entitySlug, String title,
                            String subtitle, String metadataInfo, String imageUrl,
                            String targetUrl, String badgeText, Double score) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.entitySlug = entitySlug;
        this.title = title;
        this.subtitle = subtitle;
        this.metadataInfo = metadataInfo;
        this.imageUrl = imageUrl;
        this.targetUrl = targetUrl;
        this.badgeText = badgeText;
        this.score = score;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntitySlug() {
        return entitySlug;
    }

    public void setEntitySlug(String entitySlug) {
        this.entitySlug = entitySlug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMetadataInfo() {
        return metadataInfo;
    }

    public void setMetadataInfo(String metadataInfo) {
        this.metadataInfo = metadataInfo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getBadgeText() {
        return badgeText;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
