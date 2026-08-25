package com.viajescarolina.api.contact.domain;

import java.time.Instant;
import java.util.List;

public class ContactPage {

    /** Una frase de ejemplo de la sección "Cómo empezar". */
    public record StarterPhrase(String quote, String support) {}

    private Integer id;

    // Hero
    private String heroBadge;
    private String heroTitle;
    private String heroSubtitle;
    private String heroCtaText;
    private String heroNoteText;
    private String heroCtaMessage;
    private String heroChatLabel;
    private String heroChatBubble1;
    private String heroChatBubble2;
    private String heroChatBubble3;

    // Cómo empezar
    private String startersBadge;
    private String startersTitle;
    private String startersSubtitle;
    private String startersClosing;
    private List<StarterPhrase> starterPhrases;

    // Oficina y Google Maps
    private String officeSectionBadge;
    private String officeSectionTitle;
    private String officeSectionSubtitle;
    private String officeMapTitle;
    private String officeMapSubtitle;
    private String officeVisitNote;
    private String officeMapEyebrow;
    private String officeMapPinTitle;
    private String officeMapPinSubtitle;
    private String officeMapsLinkText;
    private String officeLocationLabel;
    private String officeVisitLabel;
    private String officeVisitCtaText;
    private String officeVisitCtaMessage;

    private int revision;
    private Instant createdAt;
    private Instant updatedAt;

    public ContactPage() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getHeroBadge() { return heroBadge; }
    public void setHeroBadge(String heroBadge) { this.heroBadge = heroBadge; }

    public String getHeroTitle() { return heroTitle; }
    public void setHeroTitle(String heroTitle) { this.heroTitle = heroTitle; }

    public String getHeroSubtitle() { return heroSubtitle; }
    public void setHeroSubtitle(String heroSubtitle) { this.heroSubtitle = heroSubtitle; }

    public String getHeroCtaText() { return heroCtaText; }
    public void setHeroCtaText(String heroCtaText) { this.heroCtaText = heroCtaText; }

    public String getHeroNoteText() { return heroNoteText; }
    public void setHeroNoteText(String heroNoteText) { this.heroNoteText = heroNoteText; }

    public String getHeroCtaMessage() { return heroCtaMessage; }
    public void setHeroCtaMessage(String heroCtaMessage) { this.heroCtaMessage = heroCtaMessage; }

    public String getHeroChatLabel() { return heroChatLabel; }
    public void setHeroChatLabel(String heroChatLabel) { this.heroChatLabel = heroChatLabel; }

    public String getHeroChatBubble1() { return heroChatBubble1; }
    public void setHeroChatBubble1(String heroChatBubble1) { this.heroChatBubble1 = heroChatBubble1; }

    public String getHeroChatBubble2() { return heroChatBubble2; }
    public void setHeroChatBubble2(String heroChatBubble2) { this.heroChatBubble2 = heroChatBubble2; }

    public String getHeroChatBubble3() { return heroChatBubble3; }
    public void setHeroChatBubble3(String heroChatBubble3) { this.heroChatBubble3 = heroChatBubble3; }

    public String getStartersBadge() { return startersBadge; }
    public void setStartersBadge(String startersBadge) { this.startersBadge = startersBadge; }

    public String getStartersTitle() { return startersTitle; }
    public void setStartersTitle(String startersTitle) { this.startersTitle = startersTitle; }

    public String getStartersSubtitle() { return startersSubtitle; }
    public void setStartersSubtitle(String startersSubtitle) { this.startersSubtitle = startersSubtitle; }

    public String getStartersClosing() { return startersClosing; }
    public void setStartersClosing(String startersClosing) { this.startersClosing = startersClosing; }

    public List<StarterPhrase> getStarterPhrases() { return starterPhrases; }
    public void setStarterPhrases(List<StarterPhrase> starterPhrases) { this.starterPhrases = starterPhrases; }

    public String getOfficeSectionBadge() { return officeSectionBadge; }
    public void setOfficeSectionBadge(String officeSectionBadge) { this.officeSectionBadge = officeSectionBadge; }

    public String getOfficeSectionTitle() { return officeSectionTitle; }
    public void setOfficeSectionTitle(String officeSectionTitle) { this.officeSectionTitle = officeSectionTitle; }

    public String getOfficeSectionSubtitle() { return officeSectionSubtitle; }
    public void setOfficeSectionSubtitle(String officeSectionSubtitle) { this.officeSectionSubtitle = officeSectionSubtitle; }

    public String getOfficeMapTitle() { return officeMapTitle; }
    public void setOfficeMapTitle(String officeMapTitle) { this.officeMapTitle = officeMapTitle; }

    public String getOfficeMapSubtitle() { return officeMapSubtitle; }
    public void setOfficeMapSubtitle(String officeMapSubtitle) { this.officeMapSubtitle = officeMapSubtitle; }

    public String getOfficeVisitNote() { return officeVisitNote; }
    public void setOfficeVisitNote(String officeVisitNote) { this.officeVisitNote = officeVisitNote; }

    public String getOfficeMapEyebrow() { return officeMapEyebrow; }
    public void setOfficeMapEyebrow(String officeMapEyebrow) { this.officeMapEyebrow = officeMapEyebrow; }

    public String getOfficeMapPinTitle() { return officeMapPinTitle; }
    public void setOfficeMapPinTitle(String officeMapPinTitle) { this.officeMapPinTitle = officeMapPinTitle; }

    public String getOfficeMapPinSubtitle() { return officeMapPinSubtitle; }
    public void setOfficeMapPinSubtitle(String officeMapPinSubtitle) { this.officeMapPinSubtitle = officeMapPinSubtitle; }

    public String getOfficeMapsLinkText() { return officeMapsLinkText; }
    public void setOfficeMapsLinkText(String officeMapsLinkText) { this.officeMapsLinkText = officeMapsLinkText; }

    public String getOfficeLocationLabel() { return officeLocationLabel; }
    public void setOfficeLocationLabel(String officeLocationLabel) { this.officeLocationLabel = officeLocationLabel; }

    public String getOfficeVisitLabel() { return officeVisitLabel; }
    public void setOfficeVisitLabel(String officeVisitLabel) { this.officeVisitLabel = officeVisitLabel; }

    public String getOfficeVisitCtaText() { return officeVisitCtaText; }
    public void setOfficeVisitCtaText(String officeVisitCtaText) { this.officeVisitCtaText = officeVisitCtaText; }

    public String getOfficeVisitCtaMessage() { return officeVisitCtaMessage; }
    public void setOfficeVisitCtaMessage(String officeVisitCtaMessage) { this.officeVisitCtaMessage = officeVisitCtaMessage; }

    public int getRevision() { return revision; }
    public void setRevision(int revision) { this.revision = revision; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
