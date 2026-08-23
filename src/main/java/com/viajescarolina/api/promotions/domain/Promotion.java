package com.viajescarolina.api.promotions.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Promotion {

    private Long id;
    private String slug;
    private String title;
    private String destination;
    private String summary;
    private BigDecimal priceUsd;
    private BigDecimal pricePen;
    private Integer durationDays;
    private Integer durationNights;
    private String departureCity;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private Long featuredMediaId;
    private boolean featured;
    private List<String> inclusions;
    private List<String> exclusions;
    private String whatsappMessageTemplate;
    private Integer displayOrder;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String source;
    private String facebookPostId;
    private String facebookPermalinkUrl;

    public Promotion(
            Long id,
            String slug,
            String title,
            String destination,
            String summary,
            BigDecimal priceUsd,
            BigDecimal pricePen,
            Integer durationDays,
            Integer durationNights,
            String departureCity,
            LocalDate validFrom,
            LocalDate validUntil,
            Long featuredMediaId,
            boolean featured,
            List<String> inclusions,
            List<String> exclusions,
            String whatsappMessageTemplate,
            Integer displayOrder,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            String source,
            String facebookPostId,
            String facebookPermalinkUrl) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.destination = destination;
        this.summary = summary;
        this.priceUsd = priceUsd;
        this.pricePen = pricePen;
        this.durationDays = durationDays;
        this.durationNights = durationNights;
        this.departureCity = departureCity != null ? departureCity : "Lima";
        this.validFrom = validFrom != null ? validFrom : LocalDate.now();
        this.validUntil = validUntil != null ? validUntil : LocalDate.now().plusMonths(6);
        this.featuredMediaId = featuredMediaId;
        this.featured = featured;
        this.inclusions = inclusions != null ? inclusions : new ArrayList<>();
        this.exclusions = exclusions != null ? exclusions : new ArrayList<>();
        this.whatsappMessageTemplate = whatsappMessageTemplate;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.active = active;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
        this.source = source != null ? source : "MANUAL";
        this.facebookPostId = facebookPostId;
        this.facebookPermalinkUrl = facebookPermalinkUrl;
    }

    public static Promotion create(
            String slug,
            String title,
            String destination,
            String summary,
            BigDecimal priceUsd,
            BigDecimal pricePen,
            Integer durationDays,
            Integer durationNights,
            String departureCity,
            LocalDate validFrom,
            LocalDate validUntil,
            Long featuredMediaId,
            boolean featured,
            List<String> inclusions,
            List<String> exclusions,
            String whatsappMessageTemplate,
            Integer displayOrder) {
        return new Promotion(
                null,
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
                featured,
                inclusions,
                exclusions,
                whatsappMessageTemplate,
                displayOrder,
                true,
                Instant.now(),
                Instant.now(),
                "MANUAL",
                null,
                null
        );
    }

    public void update(
            String slug,
            String title,
            String destination,
            String summary,
            BigDecimal priceUsd,
            BigDecimal pricePen,
            Integer durationDays,
            Integer durationNights,
            String departureCity,
            LocalDate validFrom,
            LocalDate validUntil,
            Long featuredMediaId,
            boolean featured,
            List<String> inclusions,
            List<String> exclusions,
            String whatsappMessageTemplate,
            Integer displayOrder,
            boolean active) {
        this.slug = slug;
        this.title = title;
        this.destination = destination;
        this.summary = summary;
        this.priceUsd = priceUsd;
        this.pricePen = pricePen;
        this.durationDays = durationDays;
        this.durationNights = durationNights;
        this.departureCity = departureCity;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.featuredMediaId = featuredMediaId;
        this.featured = featured;
        this.inclusions = inclusions;
        this.exclusions = exclusions;
        this.whatsappMessageTemplate = whatsappMessageTemplate;
        this.displayOrder = displayOrder;
        this.active = active;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getTitle() { return title; }
    public String getDestination() { return destination; }
    public String getSummary() { return summary; }
    public BigDecimal getPriceUsd() { return priceUsd; }
    public BigDecimal getPricePen() { return pricePen; }
    public Integer getDurationDays() { return durationDays; }
    public Integer getDurationNights() { return durationNights; }
    public String getDepartureCity() { return departureCity; }
    public LocalDate getValidFrom() { return validFrom; }
    public LocalDate getValidUntil() { return validUntil; }
    public Long getFeaturedMediaId() { return featuredMediaId; }
    public boolean isFeatured() { return featured; }
    public List<String> getInclusions() { return inclusions; }
    public List<String> getExclusions() { return exclusions; }
    public String getWhatsappMessageTemplate() { return whatsappMessageTemplate; }
    public Integer getDisplayOrder() { return displayOrder; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getSource() { return source; }
    public String getFacebookPostId() { return facebookPostId; }
    public String getFacebookPermalinkUrl() { return facebookPermalinkUrl; }
}
