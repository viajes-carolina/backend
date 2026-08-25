package com.viajescarolina.api.contact.domain;

import java.time.Instant;

public class ContactPage {

    private Integer id;

    // Hero
    private String heroBadge;
    private String heroTitle;
    private String heroSubtitle;
    private String heroCtaText;
    private String heroNoteText;
    private String heroCtaMessage;
    private String heroInfoTitle;
    private String heroInfoWhatsappLabel;
    private String heroInfoWhatsappValue;
    private String heroInfoEmailLabel;
    private String heroInfoScheduleLabel;
    private String heroInfoOfficeLabel;

    // Oficina y Google Maps
    private String officeSectionBadge;
    private String officeSectionTitle;
    private String officeMapTitle;
    private String officeVisitNote;
    private String officeMapEyebrow;
    private String officeMapPinTitle;
    private String officeMapPinSubtitle;
    private String officeMapsLinkText;
    private String officeVisitLabel;

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

    public String getHeroInfoTitle() { return heroInfoTitle; }
    public void setHeroInfoTitle(String heroInfoTitle) { this.heroInfoTitle = heroInfoTitle; }

    public String getHeroInfoWhatsappLabel() { return heroInfoWhatsappLabel; }
    public void setHeroInfoWhatsappLabel(String heroInfoWhatsappLabel) { this.heroInfoWhatsappLabel = heroInfoWhatsappLabel; }

    public String getHeroInfoWhatsappValue() { return heroInfoWhatsappValue; }
    public void setHeroInfoWhatsappValue(String heroInfoWhatsappValue) { this.heroInfoWhatsappValue = heroInfoWhatsappValue; }

    public String getHeroInfoEmailLabel() { return heroInfoEmailLabel; }
    public void setHeroInfoEmailLabel(String heroInfoEmailLabel) { this.heroInfoEmailLabel = heroInfoEmailLabel; }

    public String getHeroInfoScheduleLabel() { return heroInfoScheduleLabel; }
    public void setHeroInfoScheduleLabel(String heroInfoScheduleLabel) { this.heroInfoScheduleLabel = heroInfoScheduleLabel; }

    public String getHeroInfoOfficeLabel() { return heroInfoOfficeLabel; }
    public void setHeroInfoOfficeLabel(String heroInfoOfficeLabel) { this.heroInfoOfficeLabel = heroInfoOfficeLabel; }

    public String getOfficeSectionBadge() { return officeSectionBadge; }
    public void setOfficeSectionBadge(String officeSectionBadge) { this.officeSectionBadge = officeSectionBadge; }

    public String getOfficeSectionTitle() { return officeSectionTitle; }
    public void setOfficeSectionTitle(String officeSectionTitle) { this.officeSectionTitle = officeSectionTitle; }

    public String getOfficeMapTitle() { return officeMapTitle; }
    public void setOfficeMapTitle(String officeMapTitle) { this.officeMapTitle = officeMapTitle; }

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

    public String getOfficeVisitLabel() { return officeVisitLabel; }
    public void setOfficeVisitLabel(String officeVisitLabel) { this.officeVisitLabel = officeVisitLabel; }

    public int getRevision() { return revision; }
    public void setRevision(int revision) { this.revision = revision; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
