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

    // Hero (01)
    @Column(name = "hero_badge", nullable = false)
    public String heroBadge;

    @Column(name = "hero_title", nullable = false)
    public String heroTitle;

    @Column(name = "hero_subtitle", nullable = false, columnDefinition = "TEXT")
    public String heroSubtitle;

    @Column(name = "hero_card_badge")
    public String heroCardBadge;

    @Column(name = "hero_card_title")
    public String heroCardTitle;

    @Column(name = "hero_card_location", nullable = false)
    public String heroCardLocation;

    @Column(name = "hero_card_detail", nullable = false, columnDefinition = "TEXT")
    public String heroCardDetail;

    @Column(name = "hero_note_text")
    public String heroNoteText;

    // Nuestra forma de trabajar (02)
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

    // Quién está detrás (03)
    @Column(name = "advisors_badge", nullable = false)
    public String advisorsBadge;

    @Column(name = "advisors_highlights_json", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String advisorsHighlightsJson = "[]";

    @Column(name = "revision", nullable = false)
    public int revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public AboutPage toDomain() {
        AboutPage domain = new AboutPage();
        domain.setId(id);

        domain.setHeroBadge(heroBadge);
        domain.setHeroTitle(heroTitle);
        domain.setHeroSubtitle(heroSubtitle);
        domain.setHeroCardBadge(heroCardBadge);
        domain.setHeroCardTitle(heroCardTitle);
        domain.setHeroCardLocation(heroCardLocation);
        domain.setHeroCardDetail(heroCardDetail);
        domain.setHeroNoteText(heroNoteText);

        domain.setAccompanyBadge(accompanyBadge);
        domain.setAccompanyTitle(accompanyTitle);
        domain.setAccompanySubtitle(accompanySubtitle);
        domain.setAccompanySteps(readJsonList(accompanyStepsJson, new TypeReference<List<AboutPage.AccompanyStep>>() {}));
        domain.setAccompanyQuote(accompanyQuote);

        domain.setAdvisorsBadge(advisorsBadge);
        domain.setAdvisorsHighlights(readJsonList(advisorsHighlightsJson, new TypeReference<List<AboutPage.AccompanyStep>>() {}));

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
        this.heroCardBadge = domain.getHeroCardBadge();
        this.heroCardTitle = domain.getHeroCardTitle();
        this.heroCardLocation = domain.getHeroCardLocation();
        this.heroCardDetail = domain.getHeroCardDetail();
        this.heroNoteText = domain.getHeroNoteText();

        this.accompanyBadge = domain.getAccompanyBadge();
        this.accompanyTitle = domain.getAccompanyTitle();
        this.accompanySubtitle = domain.getAccompanySubtitle();
        this.accompanyStepsJson = writeJson(domain.getAccompanySteps());
        this.accompanyQuote = domain.getAccompanyQuote();

        this.advisorsBadge = domain.getAdvisorsBadge();
        this.advisorsHighlightsJson = writeJson(domain.getAdvisorsHighlights());

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
