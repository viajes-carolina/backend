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

    @Column(name = "hero_badge", nullable = false)
    public String heroBadge;

    @Column(name = "hero_title", nullable = false)
    public String heroTitle;

    @Column(name = "hero_subtitle", nullable = false, columnDefinition = "TEXT")
    public String heroSubtitle;

    @Column(name = "whatsapp_box_title", nullable = false)
    public String whatsappBoxTitle;

    @Column(name = "whatsapp_box_subtitle", nullable = false, columnDefinition = "TEXT")
    public String whatsappBoxSubtitle;

    @Column(name = "form_title", nullable = false)
    public String formTitle;

    @Column(name = "form_subtitle", nullable = false, columnDefinition = "TEXT")
    public String formSubtitle;

    @Column(name = "revision", nullable = false)
    public int revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public ContactPage toDomain() {
        return new ContactPage(
            id,
            heroBadge,
            heroTitle,
            heroSubtitle,
            whatsappBoxTitle,
            whatsappBoxSubtitle,
            formTitle,
            formSubtitle,
            revision,
            createdAt,
            updatedAt
        );
    }

    public static ContactPagePanacheEntity fromDomain(ContactPage domain) {
        ContactPagePanacheEntity entity = new ContactPagePanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1;
        entity.heroBadge = domain.getHeroBadge();
        entity.heroTitle = domain.getHeroTitle();
        entity.heroSubtitle = domain.getHeroSubtitle();
        entity.whatsappBoxTitle = domain.getWhatsappBoxTitle();
        entity.whatsappBoxSubtitle = domain.getWhatsappBoxSubtitle();
        entity.formTitle = domain.getFormTitle();
        entity.formSubtitle = domain.getFormSubtitle();
        entity.revision = domain.getRevision();
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        entity.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : Instant.now();
        return entity;
    }
}
