package com.viajescarolina.api.settings.domain;

import java.time.Instant;

public class SiteSettings {
    private final Integer id;
    private String siteName;
    private String brandTagline;
    private String contactEmail;
    private Integer logoMediaId;
    private Integer faviconMediaId;
    private String facebookUrl;
    private String instagramUrl;
    private String tiktokUrl;
    private String legalCompanyName;
    private String taxId;
    private String minceturCertificateUrl;
    private int revision;
    private final Instant createdAt;
    private Instant updatedAt;

    public SiteSettings(
            Integer id,
            String siteName,
            String brandTagline,
            String contactEmail,
            Integer logoMediaId,
            Integer faviconMediaId,
            String facebookUrl,
            String instagramUrl,
            String tiktokUrl,
            String legalCompanyName,
            String taxId,
            String minceturCertificateUrl,
            int revision,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.siteName = siteName;
        this.brandTagline = brandTagline;
        this.contactEmail = contactEmail;
        this.logoMediaId = logoMediaId;
        this.faviconMediaId = faviconMediaId;
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
        this.tiktokUrl = tiktokUrl;
        this.legalCompanyName = legalCompanyName;
        this.taxId = taxId;
        this.minceturCertificateUrl = minceturCertificateUrl;
        this.revision = revision;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public void update(
            String siteName,
            String brandTagline,
            String contactEmail,
            Integer logoMediaId,
            Integer faviconMediaId,
            String facebookUrl,
            String instagramUrl,
            String tiktokUrl,
            String legalCompanyName,
            String taxId,
            String minceturCertificateUrl) {
        this.siteName = siteName;
        this.brandTagline = brandTagline;
        this.contactEmail = contactEmail;
        this.logoMediaId = logoMediaId;
        this.faviconMediaId = faviconMediaId;
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
        this.tiktokUrl = tiktokUrl;
        this.legalCompanyName = legalCompanyName;
        this.taxId = taxId;
        this.minceturCertificateUrl = minceturCertificateUrl;
        this.revision++;
        this.updatedAt = Instant.now();
    }

    public Integer getId() { return id; }
    public String getSiteName() { return siteName; }
    public String getBrandTagline() { return brandTagline; }
    public String getContactEmail() { return contactEmail; }
    public Integer getLogoMediaId() { return logoMediaId; }
    public Integer getFaviconMediaId() { return faviconMediaId; }
    public String getFacebookUrl() { return facebookUrl; }
    public String getInstagramUrl() { return instagramUrl; }
    public String getTiktokUrl() { return tiktokUrl; }
    public String getLegalCompanyName() { return legalCompanyName; }
    public String getTaxId() { return taxId; }
    public String getMinceturCertificateUrl() { return minceturCertificateUrl; }
    public int getRevision() { return revision; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
