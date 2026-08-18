package com.viajescarolina.api.home.domain;

public class HomeBlogInspiration {
    private Long id;
    private String badgeText;
    private String titleHighlight;
    private String titleAccent;
    private String subtitle;
    private String ctaText;
    private String ctaUrl;
    private Integer postsLimit;
    private Boolean active;

    public HomeBlogInspiration() {
    }

    public HomeBlogInspiration(
            Long id,
            String badgeText,
            String titleHighlight,
            String titleAccent,
            String subtitle,
            String ctaText,
            String ctaUrl,
            Integer postsLimit,
            Boolean active
    ) {
        this.id = id;
        this.badgeText = badgeText;
        this.titleHighlight = titleHighlight;
        this.titleAccent = titleAccent;
        this.subtitle = subtitle;
        this.ctaText = ctaText;
        this.ctaUrl = ctaUrl;
        this.postsLimit = postsLimit;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBadgeText() {
        return badgeText;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
    }

    public String getTitleHighlight() {
        return titleHighlight;
    }

    public void setTitleHighlight(String titleHighlight) {
        this.titleHighlight = titleHighlight;
    }

    public String getTitleAccent() {
        return titleAccent;
    }

    public void setTitleAccent(String titleAccent) {
        this.titleAccent = titleAccent;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCtaText() {
        return ctaText;
    }

    public void setCtaText(String ctaText) {
        this.ctaText = ctaText;
    }

    public String getCtaUrl() {
        return ctaUrl;
    }

    public void setCtaUrl(String ctaUrl) {
        this.ctaUrl = ctaUrl;
    }

    public Integer getPostsLimit() {
        return postsLimit;
    }

    public void setPostsLimit(Integer postsLimit) {
        this.postsLimit = postsLimit;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
