package com.viajescarolina.api.promotions.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.promotions.domain.Promotion;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promotion")
public class PromotionPanacheEntity extends PanacheEntityBase {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "slug", nullable = false, unique = true)
    public String slug;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "destination", nullable = false)
    public String destination;

    @Column(name = "summary", nullable = false, columnDefinition = "TEXT")
    public String summary;

    @Column(name = "price_usd", nullable = false)
    public BigDecimal priceUsd;

    @Column(name = "price_pen")
    public BigDecimal pricePen;

    @Column(name = "duration_days", nullable = false)
    public Integer durationDays;

    @Column(name = "duration_nights", nullable = false)
    public Integer durationNights;

    @Column(name = "departure_city", nullable = false)
    public String departureCity = "Lima";

    @Column(name = "valid_from", nullable = false)
    public LocalDate validFrom;

    @Column(name = "valid_until", nullable = false)
    public LocalDate validUntil;

    @Column(name = "featured_media_id")
    public Long featuredMediaId;

    @Column(name = "is_featured", nullable = false)
    public boolean isFeatured = true;

    @Column(name = "inclusions", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String inclusions;

    @Column(name = "exclusions", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String exclusions;

    @Column(name = "whatsapp_message_template", length = 500)
    public String whatsappMessageTemplate;

    @Column(name = "display_order", nullable = false)
    public Integer displayOrder = 0;

    @Column(name = "active", nullable = false)
    public boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public Promotion toDomain() {
        List<String> incList = new ArrayList<>();
        List<String> excList = new ArrayList<>();
        if (inclusions != null && !inclusions.isBlank()) {
            try {
                incList = OBJECT_MAPPER.readValue(inclusions, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                incList = new ArrayList<>();
            }
        }
        if (exclusions != null && !exclusions.isBlank()) {
            try {
                excList = OBJECT_MAPPER.readValue(exclusions, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                excList = new ArrayList<>();
            }
        }
        return new Promotion(
                id,
                slug,
                title,
                destination,
                summary,
                priceUsd,
                pricePen,
                durationDays,
                durationNights,
                departureCity,
                validFrom,
                validUntil,
                featuredMediaId,
                isFeatured,
                incList,
                excList,
                whatsappMessageTemplate,
                displayOrder,
                active,
                createdAt,
                updatedAt
        );
    }

    public static PromotionPanacheEntity fromDomain(Promotion domain) {
        PromotionPanacheEntity entity = new PromotionPanacheEntity();
        entity.id = domain.getId();
        entity.slug = domain.getSlug();
        entity.title = domain.getTitle();
        entity.destination = domain.getDestination();
        entity.summary = domain.getSummary();
        entity.priceUsd = domain.getPriceUsd();
        entity.pricePen = domain.getPricePen();
        entity.durationDays = domain.getDurationDays();
        entity.durationNights = domain.getDurationNights();
        entity.departureCity = domain.getDepartureCity();
        entity.validFrom = domain.getValidFrom();
        entity.validUntil = domain.getValidUntil();
        entity.featuredMediaId = domain.getFeaturedMediaId();
        entity.isFeatured = domain.isFeatured();
        try {
            entity.inclusions = OBJECT_MAPPER.writeValueAsString(domain.getInclusions());
        } catch (Exception e) {
            entity.inclusions = "[]";
        }
        try {
            entity.exclusions = OBJECT_MAPPER.writeValueAsString(domain.getExclusions());
        } catch (Exception e) {
            entity.exclusions = "[]";
        }
        entity.whatsappMessageTemplate = domain.getWhatsappMessageTemplate();
        entity.displayOrder = domain.getDisplayOrder();
        entity.active = domain.isActive();
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }
}
