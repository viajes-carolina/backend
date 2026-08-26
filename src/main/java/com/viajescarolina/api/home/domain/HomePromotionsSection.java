package com.viajescarolina.api.home.domain;

public class HomePromotionsSection {
    private Long id;
    private String badgeText;
    private String title;
    private String subtitle;
    private String bottomCtaQuestion;
    private String bottomCtaEyebrow;
    private String bottomCtaCopy;
    private String bottomCtaWhatsappText;
    private String bottomCtaWhatsappMessage;

    public HomePromotionsSection() {
    }

    public HomePromotionsSection(
            Long id,
            String badgeText,
            String title,
            String subtitle,
            String bottomCtaQuestion,
            String bottomCtaWhatsappText,
            String bottomCtaWhatsappMessage
    ) {
        this.id = id;
        this.badgeText = badgeText;
        this.title = title;
        this.subtitle = subtitle;
        this.bottomCtaQuestion = bottomCtaQuestion;
        this.bottomCtaWhatsappText = bottomCtaWhatsappText;
        this.bottomCtaWhatsappMessage = bottomCtaWhatsappMessage;
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

    public String getBottomCtaQuestion() {
        return bottomCtaQuestion;
    }

    public void setBottomCtaQuestion(String bottomCtaQuestion) {
        this.bottomCtaQuestion = bottomCtaQuestion;
    }

    public String getBottomCtaEyebrow() {
        return bottomCtaEyebrow;
    }

    public void setBottomCtaEyebrow(String bottomCtaEyebrow) {
        this.bottomCtaEyebrow = bottomCtaEyebrow;
    }

    public String getBottomCtaCopy() {
        return bottomCtaCopy;
    }

    public void setBottomCtaCopy(String bottomCtaCopy) {
        this.bottomCtaCopy = bottomCtaCopy;
    }

    public String getBottomCtaWhatsappText() {
        return bottomCtaWhatsappText;
    }

    public void setBottomCtaWhatsappText(String bottomCtaWhatsappText) {
        this.bottomCtaWhatsappText = bottomCtaWhatsappText;
    }

    public String getBottomCtaWhatsappMessage() {
        return bottomCtaWhatsappMessage;
    }

    public void setBottomCtaWhatsappMessage(String bottomCtaWhatsappMessage) {
        this.bottomCtaWhatsappMessage = bottomCtaWhatsappMessage;
    }
}
