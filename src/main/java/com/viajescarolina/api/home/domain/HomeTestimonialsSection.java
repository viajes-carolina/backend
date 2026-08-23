package com.viajescarolina.api.home.domain;

public class HomeTestimonialsSection {
    private Long id;
    private String badgeText;
    private String title;
    private String subtitle;

    public HomeTestimonialsSection() {
    }

    public HomeTestimonialsSection(
            Long id,
            String badgeText,
            String title,
            String subtitle
    ) {
        this.id = id;
        this.badgeText = badgeText;
        this.title = title;
        this.subtitle = subtitle;
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
}
