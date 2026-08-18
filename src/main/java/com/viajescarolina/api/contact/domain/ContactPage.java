package com.viajescarolina.api.contact.domain;

import java.time.Instant;

public class ContactPage {
    private Integer id;
    private String heroBadge;
    private String heroTitle;
    private String heroSubtitle;
    private String whatsappBoxTitle;
    private String whatsappBoxSubtitle;
    private String formTitle;
    private String formSubtitle;
    private int revision;
    private Instant createdAt;
    private Instant updatedAt;

    public ContactPage() {}

    public ContactPage(Integer id, String heroBadge, String heroTitle, String heroSubtitle,
                       String whatsappBoxTitle, String whatsappBoxSubtitle,
                       String formTitle, String formSubtitle, int revision,
                       Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.heroBadge = heroBadge;
        this.heroTitle = heroTitle;
        this.heroSubtitle = heroSubtitle;
        this.whatsappBoxTitle = whatsappBoxTitle;
        this.whatsappBoxSubtitle = whatsappBoxSubtitle;
        this.formTitle = formTitle;
        this.formSubtitle = formSubtitle;
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

    public String getWhatsappBoxTitle() { return whatsappBoxTitle; }
    public void setWhatsappBoxTitle(String whatsappBoxTitle) { this.whatsappBoxTitle = whatsappBoxTitle; }

    public String getWhatsappBoxSubtitle() { return whatsappBoxSubtitle; }
    public void setWhatsappBoxSubtitle(String whatsappBoxSubtitle) { this.whatsappBoxSubtitle = whatsappBoxSubtitle; }

    public String getFormTitle() { return formTitle; }
    public void setFormTitle(String formTitle) { this.formTitle = formTitle; }

    public String getFormSubtitle() { return formSubtitle; }
    public void setFormSubtitle(String formSubtitle) { this.formSubtitle = formSubtitle; }

    public int getRevision() { return revision; }
    public void setRevision(int revision) { this.revision = revision; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
