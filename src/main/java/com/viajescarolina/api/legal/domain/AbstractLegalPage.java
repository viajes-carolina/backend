package com.viajescarolina.api.legal.domain;

import java.time.Instant;
import java.util.List;

/**
 * Campos comunes a las 5 páginas legales/institucionales editables desde el
 * admin: hero (eyebrow/title/introduction), badge de control documental
 * (vigencia/última actualización), secciones numeradas de tamaño variable y
 * nota institucional de cierre. Cada página concreta (LegalTerms, LegalPrivacy,
 * LegalCookies, LegalEsnna, LegalMincetur) extiende esta clase y agrega sus
 * propios campos específicos cuando corresponde.
 */
public abstract class AbstractLegalPage {

    private Long id;
    private String eyebrow;
    private String title;
    private String introduction;
    private String documentControlLabel;
    private String documentControlText;
    private List<LegalSection> sections;
    private String closingTitle;
    private String closingBody;
    private String closingLinkLabel;
    private int revision;
    private Instant createdAt;
    private Instant updatedAt;

    protected AbstractLegalPage() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEyebrow() { return eyebrow; }
    public void setEyebrow(String eyebrow) { this.eyebrow = eyebrow; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }

    public String getDocumentControlLabel() { return documentControlLabel; }
    public void setDocumentControlLabel(String documentControlLabel) { this.documentControlLabel = documentControlLabel; }

    public String getDocumentControlText() { return documentControlText; }
    public void setDocumentControlText(String documentControlText) { this.documentControlText = documentControlText; }

    public List<LegalSection> getSections() { return sections; }
    public void setSections(List<LegalSection> sections) { this.sections = sections; }

    public String getClosingTitle() { return closingTitle; }
    public void setClosingTitle(String closingTitle) { this.closingTitle = closingTitle; }

    public String getClosingBody() { return closingBody; }
    public void setClosingBody(String closingBody) { this.closingBody = closingBody; }

    public String getClosingLinkLabel() { return closingLinkLabel; }
    public void setClosingLinkLabel(String closingLinkLabel) { this.closingLinkLabel = closingLinkLabel; }

    public int getRevision() { return revision; }
    public void setRevision(int revision) { this.revision = revision; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
