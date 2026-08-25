package com.viajescarolina.api.about.domain;

import java.time.Instant;
import java.util.List;

public class AboutPage {

    /** Un paso de la ruta horizontal "de idea a recuerdo" de la sección Misión. */
    public record JourneyStep(String label) {}

    /** Un paso de la ruta vertical de la sección "Cómo te acompañamos". */
    public record AccompanyStep(String title, String body) {}

    /** Un momento numerado de la sección "Experiencias que humanizan". */
    public record Moment(String title, String body) {}

    private Integer id;

    // Hero
    private String heroBadge;
    private String heroTitle;
    private String heroSubtitle;
    private Long heroMediaId;
    private String heroMediaUrl;
    private Double heroFocalX;
    private Double heroFocalY;
    private String heroCardBadge;
    private String heroCardTitle;
    private String heroNoteText;

    // Historia
    private String storyTitle;
    private String storyBody;
    private Long storyMediaId;
    private String storyMediaUrl;
    private Double storyFocalX;
    private Double storyFocalY;

    // Misión
    private String missionTitle;
    private String missionBody;
    private String missionQuote;
    private List<JourneyStep> journeySteps;

    private List<String> values;

    // Cómo te acompañamos
    private String accompanyBadge;
    private String accompanyTitle;
    private String accompanySubtitle;
    private List<AccompanyStep> accompanySteps;
    private String accompanyQuote;
    private String accompanyQuoteAttribution;

    // Experiencias que humanizan
    private String momentsBadge;
    private String momentsTitle;
    private String momentsSubtitle;
    private Long momentsMediaId;
    private String momentsMediaUrl;
    private Double momentsFocalX;
    private Double momentsFocalY;
    private List<Moment> moments;

    // Una persona al otro lado
    private String humanBadge;
    private String humanTitle;
    private String humanSubtitle;
    private String humanTagline;

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

    public Long getHeroMediaId() { return heroMediaId; }
    public void setHeroMediaId(Long heroMediaId) { this.heroMediaId = heroMediaId; }

    public String getHeroMediaUrl() { return heroMediaUrl; }
    public void setHeroMediaUrl(String heroMediaUrl) { this.heroMediaUrl = heroMediaUrl; }

    public Double getHeroFocalX() { return heroFocalX; }
    public void setHeroFocalX(Double heroFocalX) { this.heroFocalX = heroFocalX; }

    public Double getHeroFocalY() { return heroFocalY; }
    public void setHeroFocalY(Double heroFocalY) { this.heroFocalY = heroFocalY; }

    public String getHeroCardBadge() { return heroCardBadge; }
    public void setHeroCardBadge(String heroCardBadge) { this.heroCardBadge = heroCardBadge; }

    public String getHeroCardTitle() { return heroCardTitle; }
    public void setHeroCardTitle(String heroCardTitle) { this.heroCardTitle = heroCardTitle; }

    public String getHeroNoteText() { return heroNoteText; }
    public void setHeroNoteText(String heroNoteText) { this.heroNoteText = heroNoteText; }

    public String getStoryTitle() { return storyTitle; }
    public void setStoryTitle(String storyTitle) { this.storyTitle = storyTitle; }

    public String getStoryBody() { return storyBody; }
    public void setStoryBody(String storyBody) { this.storyBody = storyBody; }

    public Long getStoryMediaId() { return storyMediaId; }
    public void setStoryMediaId(Long storyMediaId) { this.storyMediaId = storyMediaId; }

    public String getStoryMediaUrl() { return storyMediaUrl; }
    public void setStoryMediaUrl(String storyMediaUrl) { this.storyMediaUrl = storyMediaUrl; }

    public Double getStoryFocalX() { return storyFocalX; }
    public void setStoryFocalX(Double storyFocalX) { this.storyFocalX = storyFocalX; }

    public Double getStoryFocalY() { return storyFocalY; }
    public void setStoryFocalY(Double storyFocalY) { this.storyFocalY = storyFocalY; }

    public String getMissionTitle() { return missionTitle; }
    public void setMissionTitle(String missionTitle) { this.missionTitle = missionTitle; }

    public String getMissionBody() { return missionBody; }
    public void setMissionBody(String missionBody) { this.missionBody = missionBody; }

    public String getMissionQuote() { return missionQuote; }
    public void setMissionQuote(String missionQuote) { this.missionQuote = missionQuote; }

    public List<JourneyStep> getJourneySteps() { return journeySteps; }
    public void setJourneySteps(List<JourneyStep> journeySteps) { this.journeySteps = journeySteps; }

    public List<String> getValues() { return values; }
    public void setValues(List<String> values) { this.values = values; }

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

    public String getAccompanyQuoteAttribution() { return accompanyQuoteAttribution; }
    public void setAccompanyQuoteAttribution(String accompanyQuoteAttribution) { this.accompanyQuoteAttribution = accompanyQuoteAttribution; }

    public String getMomentsBadge() { return momentsBadge; }
    public void setMomentsBadge(String momentsBadge) { this.momentsBadge = momentsBadge; }

    public String getMomentsTitle() { return momentsTitle; }
    public void setMomentsTitle(String momentsTitle) { this.momentsTitle = momentsTitle; }

    public String getMomentsSubtitle() { return momentsSubtitle; }
    public void setMomentsSubtitle(String momentsSubtitle) { this.momentsSubtitle = momentsSubtitle; }

    public Long getMomentsMediaId() { return momentsMediaId; }
    public void setMomentsMediaId(Long momentsMediaId) { this.momentsMediaId = momentsMediaId; }

    public String getMomentsMediaUrl() { return momentsMediaUrl; }
    public void setMomentsMediaUrl(String momentsMediaUrl) { this.momentsMediaUrl = momentsMediaUrl; }

    public Double getMomentsFocalX() { return momentsFocalX; }
    public void setMomentsFocalX(Double momentsFocalX) { this.momentsFocalX = momentsFocalX; }

    public Double getMomentsFocalY() { return momentsFocalY; }
    public void setMomentsFocalY(Double momentsFocalY) { this.momentsFocalY = momentsFocalY; }

    public List<Moment> getMoments() { return moments; }
    public void setMoments(List<Moment> moments) { this.moments = moments; }

    public String getHumanBadge() { return humanBadge; }
    public void setHumanBadge(String humanBadge) { this.humanBadge = humanBadge; }

    public String getHumanTitle() { return humanTitle; }
    public void setHumanTitle(String humanTitle) { this.humanTitle = humanTitle; }

    public String getHumanSubtitle() { return humanSubtitle; }
    public void setHumanSubtitle(String humanSubtitle) { this.humanSubtitle = humanSubtitle; }

    public String getHumanTagline() { return humanTagline; }
    public void setHumanTagline(String humanTagline) { this.humanTagline = humanTagline; }

    public int getRevision() { return revision; }
    public void setRevision(int revision) { this.revision = revision; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
