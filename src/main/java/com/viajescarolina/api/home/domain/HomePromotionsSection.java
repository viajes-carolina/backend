package com.viajescarolina.api.home.domain;

public class HomePromotionsSection {
    private Long id;
    private String badgeText;
    private String title;
    private String subtitle;
    private String bottomCtaQuestion;
    private String bottomCtaWhatsappText;
    private String bottomCtaWhatsappMessage;
    private Long mediaId;
    private String mediaUrl;
    private Double mediaFocalX;
    private Double mediaFocalY;

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

    public HomePromotionsSection(
            Long id,
            String badgeText,
            String title,
            String subtitle,
            String bottomCtaQuestion,
            String bottomCtaWhatsappText,
            String bottomCtaWhatsappMessage,
            Long mediaId,
            String mediaUrl,
            Double mediaFocalX,
            Double mediaFocalY
    ) {
        this.id = id;
        this.badgeText = badgeText;
        this.title = title;
        this.subtitle = subtitle;
        this.bottomCtaQuestion = bottomCtaQuestion;
        this.bottomCtaWhatsappText = bottomCtaWhatsappText;
        this.bottomCtaWhatsappMessage = bottomCtaWhatsappMessage;
        this.mediaId = mediaId;
        this.mediaUrl = mediaUrl;
        this.mediaFocalX = mediaFocalX;
        this.mediaFocalY = mediaFocalY;
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

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Double getMediaFocalX() {
        return mediaFocalX;
    }

    public void setMediaFocalX(Double mediaFocalX) {
        this.mediaFocalX = mediaFocalX;
    }

    public Double getMediaFocalY() {
        return mediaFocalY;
    }

    public void setMediaFocalY(Double mediaFocalY) {
        this.mediaFocalY = mediaFocalY;
    }
}
