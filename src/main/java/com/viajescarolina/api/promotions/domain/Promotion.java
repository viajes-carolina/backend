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
    private List<String> inclusions;
    private List<String> exclusions;
    private String whatsappMessageTemplate;
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
            List<String> inclusions,
            List<String> exclusions,
            String whatsappMessageTemplate,
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
        this.inclusions = inclusions != null ? inclusions : new ArrayList<>();
        this.exclusions = exclusions != null ? exclusions : new ArrayList<>();
        this.whatsappMessageTemplate = whatsappMessageTemplate;
        this.active = active;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
        this.source = source != null ? source : "MANUAL";
        this.facebookPostId = facebookPostId;
        this.facebookPermalinkUrl = facebookPermalinkUrl;
    }

    /**
     * Crea una promoción nueva, autorada por el admin desde el formulario estructurado
     * (título, precio, fechas, foto, inclusiones). Siempre nace activa y de origen MANUAL;
     * el resultado de la publicación en Facebook (si tiene éxito) se adjunta después vía
     * {@link #setFacebookPublishResult(String, String)}.
     */
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
            List<String> inclusions,
            List<String> exclusions,
            String whatsappMessageTemplate) {
        Instant now = Instant.now();
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
                inclusions,
                exclusions,
                whatsappMessageTemplate,
                true,
                now,
                now,
                "MANUAL",
                null,
                null);
    }

    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = Instant.now();
    }

    /**
     * Adjunta el resultado de publicar esta promoción como post con foto en la Página de
     * Facebook (ver {@code FacebookGraphClient.publishPhoto}). Se llama después de guardar
     * la promoción, solo si el intento de publicación tuvo éxito — el publish es best-effort
     * y nunca condiciona la creación de la promoción en sí.
     */
    public void setFacebookPublishResult(String facebookPostId, String facebookPermalinkUrl) {
        this.facebookPostId = facebookPostId;
        this.facebookPermalinkUrl = facebookPermalinkUrl;
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
    public List<String> getInclusions() { return inclusions; }
    public List<String> getExclusions() { return exclusions; }
    public String getWhatsappMessageTemplate() { return whatsappMessageTemplate; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getSource() { return source; }
    public String getFacebookPostId() { return facebookPostId; }
    public String getFacebookPermalinkUrl() { return facebookPermalinkUrl; }
}
