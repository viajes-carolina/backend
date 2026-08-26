package com.viajescarolina.api.home.domain;

public class HomeTestimonialsSection {
    private Long id;
    private String badgeText;
    private String title;
    private String subtitle;
    // Foto grande de fondo (el blob decorativo) — independiente de la foto Polaroid,
    // ambas configurables por el admin directo en esta sección (antes reutilizaban
    // una foto secundaria del Hero, sin ninguna forma de editarla desde aquí).
    private Long blobMediaId;
    private String blobMediaUrl;
    private Double blobFocalX;
    private Double blobFocalY;

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

    public Long getBlobMediaId() {
        return blobMediaId;
    }

    public void setBlobMediaId(Long blobMediaId) {
        this.blobMediaId = blobMediaId;
    }

    public String getBlobMediaUrl() {
        return blobMediaUrl;
    }

    public void setBlobMediaUrl(String blobMediaUrl) {
        this.blobMediaUrl = blobMediaUrl;
    }

    public Double getBlobFocalX() {
        return blobFocalX;
    }

    public void setBlobFocalX(Double blobFocalX) {
        this.blobFocalX = blobFocalX;
    }

    public Double getBlobFocalY() {
        return blobFocalY;
    }

    public void setBlobFocalY(Double blobFocalY) {
        this.blobFocalY = blobFocalY;
    }
}
