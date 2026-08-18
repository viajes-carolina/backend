package com.viajescarolina.api.settings.domain;

import java.time.Instant;

public class SiteSettings {
    private final Integer id;
    private String siteName;
    private String brandTagline;
    private String contactEmail;
    private String primaryPhone;
    private Integer logoMediaId;
    private Integer faviconMediaId;
    private String facebookUrl;
    private String instagramUrl;
    private String tiktokUrl;
    private int revision;
    private final Instant createdAt;
    private Instant updatedAt;

    public SiteSettings(
            Integer id,
            String siteName,
            String brandTagline,
            String contactEmail,
            String primaryPhone,
            Integer logoMediaId,
            Integer faviconMediaId,
            String facebookUrl,
            String instagramUrl,
            String tiktokUrl,
            int revision,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.siteName = siteName;
        this.brandTagline = brandTagline;
        this.contactEmail = contactEmail;
        this.primaryPhone = primaryPhone;
        this.logoMediaId = logoMediaId;
        this.faviconMediaId = faviconMediaId;
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
        this.tiktokUrl = tiktokUrl;
        this.revision = revision;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public void update(
            String siteName,
            String brandTagline,
            String contactEmail,
            String primaryPhone,
            Integer logoMediaId,
            Integer faviconMediaId,
            String facebookUrl,
            String instagramUrl,
            String tiktokUrl) {
        this.siteName = siteName;
        this.brandTagline = brandTagline;
        this.contactEmail = contactEmail;
        this.primaryPhone = primaryPhone;
        this.logoMediaId = logoMediaId;
        this.faviconMediaId = faviconMediaId;
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
        this.tiktokUrl = tiktokUrl;
        this.revision++;
        this.updatedAt = Instant.now();
    }

    public Integer getId() { return id; }
    public String getSiteName() { return siteName; }
    public String getBrandTagline() { return brandTagline; }
    public String getContactEmail() { return contactEmail; }
    public String getPrimaryPhone() { return primaryPhone; }
    public Integer getLogoMediaId() { return logoMediaId; }
    public Integer getFaviconMediaId() { return faviconMediaId; }
    public String getFacebookUrl() { return facebookUrl; }
    public String getInstagramUrl() { return instagramUrl; }
    public String getTiktokUrl() { return tiktokUrl; }
    public int getRevision() { return revision; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
