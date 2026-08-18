package com.viajescarolina.api.intentions.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.intentions.domain.TravelIntention;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "travel_intention")
public class TravelIntentionPanacheEntity extends PanacheEntityBase {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "slug", nullable = false, unique = true)
    public String slug;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "tagline", nullable = false)
    public String tagline;

    @Column(name = "icon_name", nullable = false)
    public String iconName;

    @Column(name = "featured_destinations", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String featuredDestinations;

    @Column(name = "whatsapp_message_template", nullable = false, length = 500)
    public String whatsappMessageTemplate;

    @Column(name = "cover_media_id")
    public Long coverMediaId;

    @Column(name = "display_order", nullable = false)
    public Integer displayOrder = 0;

    @Column(name = "is_active", nullable = false)
    public boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public TravelIntention toDomain() {
        List<String> destList = new ArrayList<>();
        if (featuredDestinations != null && !featuredDestinations.isBlank()) {
            try {
                destList = OBJECT_MAPPER.readValue(featuredDestinations, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                destList = new ArrayList<>();
            }
        }
        return new TravelIntention(
                id,
                slug,
                title,
                tagline,
                iconName,
                destList,
                whatsappMessageTemplate,
                coverMediaId,
                displayOrder,
                isActive,
                createdAt,
                updatedAt
        );
    }

    public static TravelIntentionPanacheEntity fromDomain(TravelIntention domain) {
        TravelIntentionPanacheEntity entity = new TravelIntentionPanacheEntity();
        entity.id = domain.getId();
        entity.slug = domain.getSlug();
        entity.title = domain.getTitle();
        entity.tagline = domain.getTagline();
        entity.iconName = domain.getIconName();
        try {
            entity.featuredDestinations = OBJECT_MAPPER.writeValueAsString(domain.getFeaturedDestinations());
        } catch (Exception e) {
            entity.featuredDestinations = "[]";
        }
        entity.whatsappMessageTemplate = domain.getWhatsappMessageTemplate();
        entity.coverMediaId = domain.getCoverMediaId();
        entity.displayOrder = domain.getDisplayOrder();
        entity.isActive = domain.isActive();
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }
}
