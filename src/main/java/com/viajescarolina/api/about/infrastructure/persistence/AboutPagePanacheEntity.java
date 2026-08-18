package com.viajescarolina.api.about.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.about.domain.AboutPage;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "about_page")
public class AboutPagePanacheEntity extends PanacheEntityBase {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    public Integer id = 1;

    @Column(name = "hero_badge", nullable = false)
    public String heroBadge;

    @Column(name = "hero_title", nullable = false)
    public String heroTitle;

    @Column(name = "hero_subtitle", nullable = false, columnDefinition = "TEXT")
    public String heroSubtitle;

    @Column(name = "hero_media_id")
    public Long heroMediaId;

    @Column(name = "story_title", nullable = false)
    public String storyTitle;

    @Column(name = "story_body", nullable = false, columnDefinition = "TEXT")
    public String storyBody;

    @Column(name = "story_media_id")
    public Long storyMediaId;

    @Column(name = "mission_title", nullable = false)
    public String missionTitle;

    @Column(name = "mission_body", nullable = false, columnDefinition = "TEXT")
    public String missionBody;

    @Column(name = "vision_title", nullable = false)
    public String visionTitle;

    @Column(name = "vision_body", nullable = false, columnDefinition = "TEXT")
    public String visionBody;

    @Column(name = "values_json", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String valuesJson;

    @Column(name = "experience_years", nullable = false)
    public int experienceYears;

    @Column(name = "happy_travelers", nullable = false)
    public int happyTravelers;

    @Column(name = "destinations_count", nullable = false)
    public int destinationsCount;

    @Column(name = "satisfaction_rate_percent", nullable = false)
    public int satisfactionRatePercent;

    @Column(name = "revision", nullable = false)
    public int revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public AboutPage toDomain(String heroMediaUrl, String storyMediaUrl) {
        List<String> valuesList = new ArrayList<>();
        if (valuesJson != null && !valuesJson.isBlank()) {
            try {
                valuesList = OBJECT_MAPPER.readValue(valuesJson, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                valuesList = new ArrayList<>();
            }
        }
        return new AboutPage(
            id,
            heroBadge,
            heroTitle,
            heroSubtitle,
            heroMediaId,
            heroMediaUrl,
            storyTitle,
            storyBody,
            storyMediaId,
            storyMediaUrl,
            missionTitle,
            missionBody,
            visionTitle,
            visionBody,
            valuesList,
            experienceYears,
            happyTravelers,
            destinationsCount,
            satisfactionRatePercent,
            revision,
            createdAt,
            updatedAt
        );
    }

    public static AboutPagePanacheEntity fromDomain(AboutPage domain) {
        AboutPagePanacheEntity entity = new AboutPagePanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1;
        entity.heroBadge = domain.getHeroBadge();
        entity.heroTitle = domain.getHeroTitle();
        entity.heroSubtitle = domain.getHeroSubtitle();
        entity.heroMediaId = domain.getHeroMediaId();
        entity.storyTitle = domain.getStoryTitle();
        entity.storyBody = domain.getStoryBody();
        entity.storyMediaId = domain.getStoryMediaId();
        entity.missionTitle = domain.getMissionTitle();
        entity.missionBody = domain.getMissionBody();
        entity.visionTitle = domain.getVisionTitle();
        entity.visionBody = domain.getVisionBody();
        try {
            entity.valuesJson = OBJECT_MAPPER.writeValueAsString(domain.getValues());
        } catch (Exception e) {
            entity.valuesJson = "[]";
        }
        entity.experienceYears = domain.getExperienceYears();
        entity.happyTravelers = domain.getHappyTravelers();
        entity.destinationsCount = domain.getDestinationsCount();
        entity.satisfactionRatePercent = domain.getSatisfactionRatePercent();
        entity.revision = domain.getRevision();
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        entity.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : Instant.now();
        return entity;
    }
}
