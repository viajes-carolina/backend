package com.viajescarolina.api.settings.domain;

import java.math.BigDecimal;
import java.time.Instant;

public class OfficeLocation {
    private final Integer id;
    private String addressLine;
    private String district;
    private String city;
    private String country;
    private String postalCode;
    private String referenceLandmark;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String googleMapsUrl;
    private String embedMapsUrl;
    private String scheduleWeekdays;
    private String scheduleSaturdays;
    private boolean active;
    private int revision;
    private final Instant createdAt;
    private Instant updatedAt;

    public OfficeLocation(
            Integer id,
            String addressLine,
            String district,
            String city,
            String country,
            String postalCode,
            String referenceLandmark,
            BigDecimal latitude,
            BigDecimal longitude,
            String googleMapsUrl,
            String embedMapsUrl,
            String scheduleWeekdays,
            String scheduleSaturdays,
            boolean active,
            int revision,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.addressLine = addressLine;
        this.district = district;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.referenceLandmark = referenceLandmark;
        this.latitude = latitude;
        this.longitude = longitude;
        this.googleMapsUrl = googleMapsUrl;
        this.embedMapsUrl = embedMapsUrl;
        this.scheduleWeekdays = scheduleWeekdays;
        this.scheduleSaturdays = scheduleSaturdays;
        this.active = active;
        this.revision = revision;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public void update(
            String addressLine,
            String district,
            String city,
            String country,
            String postalCode,
            String referenceLandmark,
            BigDecimal latitude,
            BigDecimal longitude,
            String googleMapsUrl,
            String embedMapsUrl,
            String scheduleWeekdays,
            String scheduleSaturdays,
            boolean active) {
        this.addressLine = addressLine;
        this.district = district;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.referenceLandmark = referenceLandmark;
        this.latitude = latitude;
        this.longitude = longitude;
        this.googleMapsUrl = googleMapsUrl;
        this.embedMapsUrl = embedMapsUrl;
        this.scheduleWeekdays = scheduleWeekdays;
        this.scheduleSaturdays = scheduleSaturdays;
        this.active = active;
        this.revision++;
        this.updatedAt = Instant.now();
    }

    public Integer getId() { return id; }
    public String getAddressLine() { return addressLine; }
    public String getDistrict() { return district; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getPostalCode() { return postalCode; }
    public String getReferenceLandmark() { return referenceLandmark; }
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public String getGoogleMapsUrl() { return googleMapsUrl; }
    public String getEmbedMapsUrl() { return embedMapsUrl; }
    public String getScheduleWeekdays() { return scheduleWeekdays; }
    public String getScheduleSaturdays() { return scheduleSaturdays; }
    public boolean isActive() { return active; }
    public int getRevision() { return revision; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
