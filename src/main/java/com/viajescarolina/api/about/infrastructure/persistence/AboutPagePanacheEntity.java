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

    // Hero
    @Column(name = "hero_badge", nullable = false)
    public String heroBadge;

    @Column(name = "hero_title", nullable = false)
    public String heroTitle;

    @Column(name = "hero_subtitle", nullable = false, columnDefinition = "TEXT")
    public String heroSubtitle;

    @Column(name = "hero_media_id")
    public Long heroMediaId;

    @Column(name = "hero_focal_x")
    public Double heroFocalX = 50.0;

    @Column(name = "hero_focal_y")
    public Double heroFocalY = 50.0;

    @Column(name = "hero_card_badge")
    public String heroCardBadge;

    @Column(name = "hero_card_title")
    public String heroCardTitle;

    @Column(name = "hero_note_text")
    public String heroNoteText;

    // Historia
    @Column(name = "story_title", nullable = false)
    public String storyTitle;

    @Column(name = "story_body", nullable = false, columnDefinition = "TEXT")
    public String storyBody;

    @Column(name = "story_media_id")
    public Long storyMediaId;

    @Column(name = "story_focal_x")
    public Double storyFocalX = 50.0;

    @Column(name = "story_focal_y")
    public Double storyFocalY = 50.0;

    // Misión
    @Column(name = "mission_title", nullable = false)
    public String missionTitle;

    @Column(name = "mission_body", nullable = false, columnDefinition = "TEXT")
    public String missionBody;

    @Column(name = "mission_quote", columnDefinition = "TEXT")
    public String missionQuote;

    @Column(name = "journey_steps_json", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String journeyStepsJson = "[]";

    @Column(name = "values_json", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String valuesJson;

    // Cómo te acompañamos
    @Column(name = "accompany_badge")
    public String accompanyBadge;

    @Column(name = "accompany_title")
    public String accompanyTitle;

    @Column(name = "accompany_subtitle", columnDefinition = "TEXT")
    public String accompanySubtitle;

    @Column(name = "accompany_steps_json", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String accompanyStepsJson = "[]";

    @Column(name = "accompany_quote", columnDefinition = "TEXT")
    public String accompanyQuote;

    @Column(name = "accompany_quote_attribution")
    public String accompanyQuoteAttribution;

    // Experiencias que humanizan
    @Column(name = "moments_badge")
    public String momentsBadge;

    @Column(name = "moments_title")
    public String momentsTitle;

    @Column(name = "moments_subtitle", columnDefinition = "TEXT")
    public String momentsSubtitle;

    @Column(name = "moments_media_id")
    public Long momentsMediaId;

    @Column(name = "moments_focal_x")
    public Double momentsFocalX = 50.0;

    @Column(name = "moments_focal_y")
    public Double momentsFocalY = 50.0;

    @Column(name = "moments_json", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String momentsJson = "[]";

    // Una persona al otro lado
    @Column(name = "human_badge")
    public String humanBadge;

    @Column(name = "human_title")
    public String humanTitle;

    @Column(name = "human_subtitle", columnDefinition = "TEXT")
    public String humanSubtitle;

    @Column(name = "human_tagline")
    public String humanTagline;

    @Column(name = "revision", nullable = false)
    public int revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public AboutPage toDomain(String heroMediaUrl, String storyMediaUrl, String momentsMediaUrl) {
        AboutPage domain = new AboutPage();
        domain.setId(id);

        domain.setHeroBadge(heroBadge);
        domain.setHeroTitle(heroTitle);
        domain.setHeroSubtitle(heroSubtitle);
        domain.setHeroMediaId(heroMediaId);
        domain.setHeroMediaUrl(heroMediaUrl);
        domain.setHeroFocalX(heroFocalX);
        domain.setHeroFocalY(heroFocalY);
        domain.setHeroCardBadge(heroCardBadge);
        domain.setHeroCardTitle(heroCardTitle);
        domain.setHeroNoteText(heroNoteText);

        domain.setStoryTitle(storyTitle);
        domain.setStoryBody(storyBody);
        domain.setStoryMediaId(storyMediaId);
        domain.setStoryMediaUrl(storyMediaUrl);
        domain.setStoryFocalX(storyFocalX);
        domain.setStoryFocalY(storyFocalY);

        domain.setMissionTitle(missionTitle);
        domain.setMissionBody(missionBody);
        domain.setMissionQuote(missionQuote);
        domain.setJourneySteps(readJsonList(journeyStepsJson, new TypeReference<List<AboutPage.JourneyStep>>() {}));

        domain.setValues(readJsonList(valuesJson, new TypeReference<List<String>>() {}));

        domain.setAccompanyBadge(accompanyBadge);
        domain.setAccompanyTitle(accompanyTitle);
        domain.setAccompanySubtitle(accompanySubtitle);
        domain.setAccompanySteps(readJsonList(accompanyStepsJson, new TypeReference<List<AboutPage.AccompanyStep>>() {}));
        domain.setAccompanyQuote(accompanyQuote);
        domain.setAccompanyQuoteAttribution(accompanyQuoteAttribution);

        domain.setMomentsBadge(momentsBadge);
        domain.setMomentsTitle(momentsTitle);
        domain.setMomentsSubtitle(momentsSubtitle);
        domain.setMomentsMediaId(momentsMediaId);
        domain.setMomentsMediaUrl(momentsMediaUrl);
        domain.setMomentsFocalX(momentsFocalX);
        domain.setMomentsFocalY(momentsFocalY);
        domain.setMoments(readJsonList(momentsJson, new TypeReference<List<AboutPage.Moment>>() {}));

        domain.setHumanBadge(humanBadge);
        domain.setHumanTitle(humanTitle);
        domain.setHumanSubtitle(humanSubtitle);
        domain.setHumanTagline(humanTagline);

        domain.setRevision(revision);
        domain.setCreatedAt(createdAt);
        domain.setUpdatedAt(updatedAt);
        return domain;
    }

    public static AboutPagePanacheEntity fromDomain(AboutPage domain) {
        AboutPagePanacheEntity entity = new AboutPagePanacheEntity();
        entity.copyFrom(domain);
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        return entity;
    }

    /** Copia todos los campos editables del dominio hacia esta entidad (usado en creación y actualización). */
    public void copyFrom(AboutPage domain) {
        this.id = domain.getId() != null ? domain.getId() : 1;

        this.heroBadge = domain.getHeroBadge();
        this.heroTitle = domain.getHeroTitle();
        this.heroSubtitle = domain.getHeroSubtitle();
        this.heroMediaId = domain.getHeroMediaId();
        this.heroFocalX = domain.getHeroFocalX();
        this.heroFocalY = domain.getHeroFocalY();
        this.heroCardBadge = domain.getHeroCardBadge();
        this.heroCardTitle = domain.getHeroCardTitle();
        this.heroNoteText = domain.getHeroNoteText();

        this.storyTitle = domain.getStoryTitle();
        this.storyBody = domain.getStoryBody();
        this.storyMediaId = domain.getStoryMediaId();
        this.storyFocalX = domain.getStoryFocalX();
        this.storyFocalY = domain.getStoryFocalY();

        this.missionTitle = domain.getMissionTitle();
        this.missionBody = domain.getMissionBody();
        this.missionQuote = domain.getMissionQuote();
        this.journeyStepsJson = writeJson(domain.getJourneySteps());

        this.valuesJson = writeJson(domain.getValues());

        this.accompanyBadge = domain.getAccompanyBadge();
        this.accompanyTitle = domain.getAccompanyTitle();
        this.accompanySubtitle = domain.getAccompanySubtitle();
        this.accompanyStepsJson = writeJson(domain.getAccompanySteps());
        this.accompanyQuote = domain.getAccompanyQuote();
        this.accompanyQuoteAttribution = domain.getAccompanyQuoteAttribution();

        this.momentsBadge = domain.getMomentsBadge();
        this.momentsTitle = domain.getMomentsTitle();
        this.momentsSubtitle = domain.getMomentsSubtitle();
        this.momentsMediaId = domain.getMomentsMediaId();
        this.momentsFocalX = domain.getMomentsFocalX();
        this.momentsFocalY = domain.getMomentsFocalY();
        this.momentsJson = writeJson(domain.getMoments());

        this.humanBadge = domain.getHumanBadge();
        this.humanTitle = domain.getHumanTitle();
        this.humanSubtitle = domain.getHumanSubtitle();
        this.humanTagline = domain.getHumanTagline();

        this.revision = domain.getRevision();
        this.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : Instant.now();
    }

    private static <T> List<T> readJsonList(String json, TypeReference<List<T>> type) {
        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static String writeJson(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value != null ? value : List.of());
        } catch (Exception e) {
            return "[]";
        }
    }
}
