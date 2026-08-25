package com.viajescarolina.api.contact.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.contact.domain.ContactPage;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact_page")
public class ContactPagePanacheEntity extends PanacheEntityBase {

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

    @Column(name = "hero_cta_text")
    public String heroCtaText;

    @Column(name = "hero_note_text")
    public String heroNoteText;

    @Column(name = "hero_cta_message", columnDefinition = "TEXT")
    public String heroCtaMessage;

    @Column(name = "hero_chat_label")
    public String heroChatLabel;

    @Column(name = "hero_chat_bubble_1", columnDefinition = "TEXT")
    public String heroChatBubble1;

    @Column(name = "hero_chat_bubble_2", columnDefinition = "TEXT")
    public String heroChatBubble2;

    @Column(name = "hero_chat_bubble_3", columnDefinition = "TEXT")
    public String heroChatBubble3;

    // Cómo empezar
    @Column(name = "starters_badge")
    public String startersBadge;

    @Column(name = "starters_title")
    public String startersTitle;

    @Column(name = "starters_subtitle", columnDefinition = "TEXT")
    public String startersSubtitle;

    @Column(name = "starters_closing")
    public String startersClosing;

    @Column(name = "starter_phrases_json", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String starterPhrasesJson = "[]";

    // Oficina y Google Maps
    @Column(name = "office_section_badge")
    public String officeSectionBadge;

    @Column(name = "office_section_title")
    public String officeSectionTitle;

    @Column(name = "office_section_subtitle", columnDefinition = "TEXT")
    public String officeSectionSubtitle;

    @Column(name = "office_map_title")
    public String officeMapTitle;

    @Column(name = "office_map_subtitle", columnDefinition = "TEXT")
    public String officeMapSubtitle;

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

    @Column(name = "office_location_label")
    public String officeLocationLabel;

    @Column(name = "office_visit_label")
    public String officeVisitLabel;

    @Column(name = "office_visit_cta_text")
    public String officeVisitCtaText;

    @Column(name = "office_visit_cta_message", columnDefinition = "TEXT")
    public String officeVisitCtaMessage;

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
        domain.setHeroChatLabel(heroChatLabel);
        domain.setHeroChatBubble1(heroChatBubble1);
        domain.setHeroChatBubble2(heroChatBubble2);
        domain.setHeroChatBubble3(heroChatBubble3);

        domain.setStartersBadge(startersBadge);
        domain.setStartersTitle(startersTitle);
        domain.setStartersSubtitle(startersSubtitle);
        domain.setStartersClosing(startersClosing);
        domain.setStarterPhrases(readJsonList(starterPhrasesJson, new TypeReference<List<ContactPage.StarterPhrase>>() {}));

        domain.setOfficeSectionBadge(officeSectionBadge);
        domain.setOfficeSectionTitle(officeSectionTitle);
        domain.setOfficeSectionSubtitle(officeSectionSubtitle);
        domain.setOfficeMapTitle(officeMapTitle);
        domain.setOfficeMapSubtitle(officeMapSubtitle);
        domain.setOfficeVisitNote(officeVisitNote);
        domain.setOfficeMapEyebrow(officeMapEyebrow);
        domain.setOfficeMapPinTitle(officeMapPinTitle);
        domain.setOfficeMapPinSubtitle(officeMapPinSubtitle);
        domain.setOfficeMapsLinkText(officeMapsLinkText);
        domain.setOfficeLocationLabel(officeLocationLabel);
        domain.setOfficeVisitLabel(officeVisitLabel);
        domain.setOfficeVisitCtaText(officeVisitCtaText);
        domain.setOfficeVisitCtaMessage(officeVisitCtaMessage);

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
        this.heroChatLabel = domain.getHeroChatLabel();
        this.heroChatBubble1 = domain.getHeroChatBubble1();
        this.heroChatBubble2 = domain.getHeroChatBubble2();
        this.heroChatBubble3 = domain.getHeroChatBubble3();

        this.startersBadge = domain.getStartersBadge();
        this.startersTitle = domain.getStartersTitle();
        this.startersSubtitle = domain.getStartersSubtitle();
        this.startersClosing = domain.getStartersClosing();
        this.starterPhrasesJson = writeJson(domain.getStarterPhrases());

        this.officeSectionBadge = domain.getOfficeSectionBadge();
        this.officeSectionTitle = domain.getOfficeSectionTitle();
        this.officeSectionSubtitle = domain.getOfficeSectionSubtitle();
        this.officeMapTitle = domain.getOfficeMapTitle();
        this.officeMapSubtitle = domain.getOfficeMapSubtitle();
        this.officeVisitNote = domain.getOfficeVisitNote();
        this.officeMapEyebrow = domain.getOfficeMapEyebrow();
        this.officeMapPinTitle = domain.getOfficeMapPinTitle();
        this.officeMapPinSubtitle = domain.getOfficeMapPinSubtitle();
        this.officeMapsLinkText = domain.getOfficeMapsLinkText();
        this.officeLocationLabel = domain.getOfficeLocationLabel();
        this.officeVisitLabel = domain.getOfficeVisitLabel();
        this.officeVisitCtaText = domain.getOfficeVisitCtaText();
        this.officeVisitCtaMessage = domain.getOfficeVisitCtaMessage();

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
