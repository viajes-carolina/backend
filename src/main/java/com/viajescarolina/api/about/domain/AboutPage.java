package com.viajescarolina.api.about.domain;

import java.time.Instant;
import java.util.List;

public class AboutPage {
    private Integer id;
    private String heroBadge;
    private String heroTitle;
    private String heroSubtitle;
    private Long heroMediaId;
    private String heroMediaUrl;
    private String storyTitle;
    private String storyBody;
    private Long storyMediaId;
    private String storyMediaUrl;
    private String missionTitle;
    private String missionBody;
    private String visionTitle;
    private String visionBody;
    private List<String> values;
    private int experienceYears;
    private int happyTravelers;
    private int destinationsCount;
    private int satisfactionRatePercent;
    private int revision;
    private Instant createdAt;
    private Instant updatedAt;

    public AboutPage() {}

    public AboutPage(Integer id, String heroBadge, String heroTitle, String heroSubtitle, Long heroMediaId,
                     String heroMediaUrl, String storyTitle, String storyBody, Long storyMediaId,
                     String storyMediaUrl, String missionTitle, String missionBody, String visionTitle,
                     String visionBody, List<String> values, int experienceYears, int happyTravelers,
                     int destinationsCount, int satisfactionRatePercent, int revision,
                     Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.heroBadge = heroBadge;
        this.heroTitle = heroTitle;
        this.heroSubtitle = heroSubtitle;
        this.heroMediaId = heroMediaId;
        this.heroMediaUrl = heroMediaUrl;
        this.storyTitle = storyTitle;
        this.storyBody = storyBody;
        this.storyMediaId = storyMediaId;
        this.storyMediaUrl = storyMediaUrl;
        this.missionTitle = missionTitle;
        this.missionBody = missionBody;
        this.visionTitle = visionTitle;
        this.visionBody = visionBody;
        this.values = values;
        this.experienceYears = experienceYears;
        this.happyTravelers = happyTravelers;
        this.destinationsCount = destinationsCount;
        this.satisfactionRatePercent = satisfactionRatePercent;
        this.revision = revision;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public String getStoryTitle() { return storyTitle; }
    public void setStoryTitle(String storyTitle) { this.storyTitle = storyTitle; }

    public String getStoryBody() { return storyBody; }
    public void setStoryBody(String storyBody) { this.storyBody = storyBody; }

    public Long getStoryMediaId() { return storyMediaId; }
    public void setStoryMediaId(Long storyMediaId) { this.storyMediaId = storyMediaId; }

    public String getStoryMediaUrl() { return storyMediaUrl; }
    public void setStoryMediaUrl(String storyMediaUrl) { this.storyMediaUrl = storyMediaUrl; }

    public String getMissionTitle() { return missionTitle; }
    public void setMissionTitle(String missionTitle) { this.missionTitle = missionTitle; }

    public String getMissionBody() { return missionBody; }
    public void setMissionBody(String missionBody) { this.missionBody = missionBody; }

    public String getVisionTitle() { return visionTitle; }
    public void setVisionTitle(String visionTitle) { this.visionTitle = visionTitle; }

    public String getVisionBody() { return visionBody; }
    public void setVisionBody(String visionBody) { this.visionBody = visionBody; }

    public List<String> getValues() { return values; }
    public void setValues(List<String> values) { this.values = values; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public int getHappyTravelers() { return happyTravelers; }
    public void setHappyTravelers(int happyTravelers) { this.happyTravelers = happyTravelers; }

    public int getDestinationsCount() { return destinationsCount; }
    public void setDestinationsCount(int destinationsCount) { this.destinationsCount = destinationsCount; }

    public int getSatisfactionRatePercent() { return satisfactionRatePercent; }
    public void setSatisfactionRatePercent(int satisfactionRatePercent) { this.satisfactionRatePercent = satisfactionRatePercent; }

    public int getRevision() { return revision; }
    public void setRevision(int revision) { this.revision = revision; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
