package com.viajescarolina.api.home.domain;

public class HomeConversationalPause {
    private Long id;
    private String badgeText;
    private String title;
    private String subtitle;
    private String whatsappCtaText;
    private String whatsappMessageTemplate;

    public HomeConversationalPause() {
    }

    public HomeConversationalPause(
            Long id,
            String badgeText,
            String title,
            String subtitle,
            String whatsappCtaText,
            String whatsappMessageTemplate
    ) {
        this.id = id;
        this.badgeText = badgeText;
        this.title = title;
        this.subtitle = subtitle;
        this.whatsappCtaText = whatsappCtaText;
        this.whatsappMessageTemplate = whatsappMessageTemplate;
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

    public String getWhatsappCtaText() {
        return whatsappCtaText;
    }

    public void setWhatsappCtaText(String whatsappCtaText) {
        this.whatsappCtaText = whatsappCtaText;
    }

    public String getWhatsappMessageTemplate() {
        return whatsappMessageTemplate;
    }

    public void setWhatsappMessageTemplate(String whatsappMessageTemplate) {
        this.whatsappMessageTemplate = whatsappMessageTemplate;
    }
}
