package com.viajescarolina.api.settings.infrastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "site_settings")
public class SiteSettingsPanacheEntity extends PanacheEntityBase {

    @Id
    public Integer id = 1;

    @Column(name = "site_name", nullable = false, length = 120)
    public String siteName;

    @Column(name = "brand_tagline", nullable = false, length = 200)
    public String brandTagline;

    @Column(name = "contact_email", nullable = false, length = 150)
    public String contactEmail;

    @Column(name = "primary_phone", nullable = false, length = 30)
    public String primaryPhone;

    @Column(name = "logo_media_id")
    public Integer logoMediaId;

    @Column(name = "favicon_media_id")
    public Integer faviconMediaId;

    @Column(name = "facebook_url", length = 255)
    public String facebookUrl;

    @Column(name = "instagram_url", length = 255)
    public String instagramUrl;

    @Column(name = "tiktok_url", length = 255)
    public String tiktokUrl;

    @Column(name = "revision", nullable = false)
    public int revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt = Instant.now();
}
