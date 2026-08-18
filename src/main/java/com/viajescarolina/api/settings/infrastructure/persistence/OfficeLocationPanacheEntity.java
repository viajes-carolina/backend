package com.viajescarolina.api.settings.infrastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "office_location")
public class OfficeLocationPanacheEntity extends PanacheEntityBase {

    @Id
    public Integer id = 1;

    @Column(name = "address_line", nullable = false, length = 200)
    public String addressLine;

    @Column(name = "district", nullable = false, length = 100)
    public String district;

    @Column(name = "city", nullable = false, length = 100)
    public String city;

    @Column(name = "country", nullable = false, length = 100)
    public String country;

    @Column(name = "postal_code", length = 20)
    public String postalCode;

    @Column(name = "reference_landmark", length = 200)
    public String referenceLandmark;

    @Column(name = "latitude", precision = 10, scale = 7)
    public BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    public BigDecimal longitude;

    @Column(name = "google_maps_url", length = 500)
    public String googleMapsUrl;

    @Column(name = "embed_maps_url", length = 500)
    public String embedMapsUrl;

    @Column(name = "schedule_weekdays", nullable = false, length = 100)
    public String scheduleWeekdays;

    @Column(name = "schedule_saturdays", nullable = false, length = 100)
    public String scheduleSaturdays;

    @Column(name = "is_active", nullable = false)
    public boolean active = true;

    @Column(name = "revision", nullable = false)
    public int revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt = Instant.now();
}
