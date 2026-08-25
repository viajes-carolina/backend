package com.viajescarolina.api.contact.infrastructure.persistence;

import com.viajescarolina.api.contact.domain.ContactPage;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "contact_page")
public class ContactPagePanacheEntity extends PanacheEntityBase {

    @Id
    public Integer id = 1;

    // Hero
    @Column(name = "hero_badge", nullable = false)
    public String heroBadge;

    @Column(name = "hero_title", nullable = false)
    public String heroTitle;

    @Column(name = "hero_subtitle", nullable = false, columnDefinition = "TEXT")
    public String heroSubtitle;

    @Column(name = "hero_cta_text")
    public String heroCtaText;

    @Column(name = "hero_note_text")
    public String heroNoteText;

    @Column(name = "hero_cta_message", columnDefinition = "TEXT")
    public String heroCtaMessage;

    @Column(name = "hero_info_title")
    public String heroInfoTitle;

    @Column(name = "hero_info_whatsapp_label")
    public String heroInfoWhatsappLabel;

    @Column(name = "hero_info_whatsapp_value")
    public String heroInfoWhatsappValue;

    @Column(name = "hero_info_email_label")
    public String heroInfoEmailLabel;

    @Column(name = "hero_info_schedule_label")
    public String heroInfoScheduleLabel;

    @Column(name = "hero_info_office_label")
    public String heroInfoOfficeLabel;

    // Oficina y Google Maps
    @Column(name = "office_section_badge")
    public String officeSectionBadge;

    @Column(name = "office_section_title")
    public String officeSectionTitle;

    @Column(name = "office_map_title")
    public String officeMapTitle;

    @Column(name = "office_visit_note", columnDefinition = "TEXT")
    public String officeVisitNote;

    @Column(name = "office_map_eyebrow")
    public String officeMapEyebrow;

    @Column(name = "office_map_pin_title")
    public String officeMapPinTitle;

    @Column(name = "office_map_pin_subtitle")
    public String officeMapPinSubtitle;

    @Column(name = "office_maps_link_text")
    public String officeMapsLinkText;

    @Column(name = "office_visit_label")
    public String officeVisitLabel;

    @Column(name = "revision", nullable = false)
    public int revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public ContactPage toDomain() {
        ContactPage domain = new ContactPage();
        domain.setId(id);

        domain.setHeroBadge(heroBadge);
        domain.setHeroTitle(heroTitle);
        domain.setHeroSubtitle(heroSubtitle);
        domain.setHeroCtaText(heroCtaText);
        domain.setHeroNoteText(heroNoteText);
        domain.setHeroCtaMessage(heroCtaMessage);
        domain.setHeroInfoTitle(heroInfoTitle);
        domain.setHeroInfoWhatsappLabel(heroInfoWhatsappLabel);
        domain.setHeroInfoWhatsappValue(heroInfoWhatsappValue);
        domain.setHeroInfoEmailLabel(heroInfoEmailLabel);
        domain.setHeroInfoScheduleLabel(heroInfoScheduleLabel);
        domain.setHeroInfoOfficeLabel(heroInfoOfficeLabel);

        domain.setOfficeSectionBadge(officeSectionBadge);
        domain.setOfficeSectionTitle(officeSectionTitle);
        domain.setOfficeMapTitle(officeMapTitle);
        domain.setOfficeVisitNote(officeVisitNote);
        domain.setOfficeMapEyebrow(officeMapEyebrow);
        domain.setOfficeMapPinTitle(officeMapPinTitle);
        domain.setOfficeMapPinSubtitle(officeMapPinSubtitle);
        domain.setOfficeMapsLinkText(officeMapsLinkText);
        domain.setOfficeVisitLabel(officeVisitLabel);

        domain.setRevision(revision);
        domain.setCreatedAt(createdAt);
        domain.setUpdatedAt(updatedAt);
        return domain;
    }

    public static ContactPagePanacheEntity fromDomain(ContactPage domain) {
        ContactPagePanacheEntity entity = new ContactPagePanacheEntity();
        entity.copyFrom(domain);
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        return entity;
    }

    /** Copia todos los campos editables del dominio hacia esta entidad (usado en creación y actualización). */
    public void copyFrom(ContactPage domain) {
        this.id = domain.getId() != null ? domain.getId() : 1;

        this.heroBadge = domain.getHeroBadge();
        this.heroTitle = domain.getHeroTitle();
        this.heroSubtitle = domain.getHeroSubtitle();
        this.heroCtaText = domain.getHeroCtaText();
        this.heroNoteText = domain.getHeroNoteText();
        this.heroCtaMessage = domain.getHeroCtaMessage();
        this.heroInfoTitle = domain.getHeroInfoTitle();
        this.heroInfoWhatsappLabel = domain.getHeroInfoWhatsappLabel();
        this.heroInfoWhatsappValue = domain.getHeroInfoWhatsappValue();
        this.heroInfoEmailLabel = domain.getHeroInfoEmailLabel();
        this.heroInfoScheduleLabel = domain.getHeroInfoScheduleLabel();
        this.heroInfoOfficeLabel = domain.getHeroInfoOfficeLabel();

        this.officeSectionBadge = domain.getOfficeSectionBadge();
        this.officeSectionTitle = domain.getOfficeSectionTitle();
        this.officeMapTitle = domain.getOfficeMapTitle();
        this.officeVisitNote = domain.getOfficeVisitNote();
        this.officeMapEyebrow = domain.getOfficeMapEyebrow();
        this.officeMapPinTitle = domain.getOfficeMapPinTitle();
        this.officeMapPinSubtitle = domain.getOfficeMapPinSubtitle();
        this.officeMapsLinkText = domain.getOfficeMapsLinkText();
        this.officeVisitLabel = domain.getOfficeVisitLabel();

        this.revision = domain.getRevision();
        this.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : Instant.now();
    }
}
