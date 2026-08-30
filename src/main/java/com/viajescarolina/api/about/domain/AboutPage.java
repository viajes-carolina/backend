package com.viajescarolina.api.about.domain;

import java.time.Instant;
import java.util.List;

public class AboutPage {

    /**
     * Un ítem de dos campos {title, body}. Se reutiliza tanto para la ruta
     * vertical de "Nuestra forma de trabajar" (accompanySteps) como para los
     * highlights de la sección de asesoras (advisorsHighlights).
     */
    public record AccompanyStep(String title, String body) {}

    private Integer id;

    // Hero (01)
    private String heroBadge;
    private String heroTitle;
    private String heroSubtitle;
    private String heroCardBadge;
    private String heroCardTitle;
    private String heroCardLocation;
    private String heroCardDetail;
    private String heroNoteText;

    // Nuestra forma de trabajar (02)
    private String accompanyBadge;
    private String accompanyTitle;
    private String accompanySubtitle;
    private List<AccompanyStep> accompanySteps;
    private String accompanyQuote;

    // Quién está detrás (03)
    private String advisorsBadge;
    private List<AccompanyStep> advisorsHighlights;

    private int revision;
    private Instant createdAt;
    private Instant updatedAt;

    public AboutPage() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getHeroBadge() { return heroBadge; }
    public void setHeroBadge(String heroBadge) { this.heroBadge = heroBadge; }

    public String getHeroTitle() { return heroTitle; }
    public void setHeroTitle(String heroTitle) { this.heroTitle = heroTitle; }

    public String getHeroSubtitle() { return heroSubtitle; }
    public void setHeroSubtitle(String heroSubtitle) { this.heroSubtitle = heroSubtitle; }

    public String getHeroCardBadge() { return heroCardBadge; }
    public void setHeroCardBadge(String heroCardBadge) { this.heroCardBadge = heroCardBadge; }

    public String getHeroCardTitle() { return heroCardTitle; }
    public void setHeroCardTitle(String heroCardTitle) { this.heroCardTitle = heroCardTitle; }

    public String getHeroCardLocation() { return heroCardLocation; }
    public void setHeroCardLocation(String heroCardLocation) { this.heroCardLocation = heroCardLocation; }

    public String getHeroCardDetail() { return heroCardDetail; }
    public void setHeroCardDetail(String heroCardDetail) { this.heroCardDetail = heroCardDetail; }

    public String getHeroNoteText() { return heroNoteText; }
    public void setHeroNoteText(String heroNoteText) { this.heroNoteText = heroNoteText; }

    public String getAccompanyBadge() { return accompanyBadge; }
    public void setAccompanyBadge(String accompanyBadge) { this.accompanyBadge = accompanyBadge; }

    public String getAccompanyTitle() { return accompanyTitle; }
    public void setAccompanyTitle(String accompanyTitle) { this.accompanyTitle = accompanyTitle; }

    public String getAccompanySubtitle() { return accompanySubtitle; }
    public void setAccompanySubtitle(String accompanySubtitle) { this.accompanySubtitle = accompanySubtitle; }

    public List<AccompanyStep> getAccompanySteps() { return accompanySteps; }
    public void setAccompanySteps(List<AccompanyStep> accompanySteps) { this.accompanySteps = accompanySteps; }

    public String getAccompanyQuote() { return accompanyQuote; }
    public void setAccompanyQuote(String accompanyQuote) { this.accompanyQuote = accompanyQuote; }

    public String getAdvisorsBadge() { return advisorsBadge; }
    public void setAdvisorsBadge(String advisorsBadge) { this.advisorsBadge = advisorsBadge; }

    public List<AccompanyStep> getAdvisorsHighlights() { return advisorsHighlights; }
    public void setAdvisorsHighlights(List<AccompanyStep> advisorsHighlights) { this.advisorsHighlights = advisorsHighlights; }

    public int getRevision() { return revision; }
    public void setRevision(int revision) { this.revision = revision; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
