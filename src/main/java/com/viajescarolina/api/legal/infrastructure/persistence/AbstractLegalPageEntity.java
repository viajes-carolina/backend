package com.viajescarolina.api.legal.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.viajescarolina.api.legal.domain.AbstractLegalPage;
import com.viajescarolina.api.legal.domain.LegalSection;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.List;

/**
 * Columnas comunes a las 5 páginas legales/institucionales (singleton id = 1):
 * eyebrow/title/introduction, badge de control documental, secciones
 * numeradas (JSONB) y nota institucional de cierre.
 */
@MappedSuperclass
public abstract class AbstractLegalPageEntity extends PanacheEntityBase {

    @Id
    public Long id = 1L;

    @Column(name = "eyebrow", nullable = false)
    public String eyebrow;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "introduction", nullable = false, columnDefinition = "TEXT")
    public String introduction;

    @Column(name = "document_control_label", nullable = false)
    public String documentControlLabel;

    @Column(name = "document_control_text", nullable = false)
    public String documentControlText;

    @Column(name = "sections_json", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String sectionsJson = "[]";

    @Column(name = "closing_title", nullable = false)
    public String closingTitle;

    @Column(name = "closing_body", nullable = false, columnDefinition = "TEXT")
    public String closingBody;

    @Column(name = "closing_link_label", nullable = false)
    public String closingLinkLabel;

    @Column(name = "revision", nullable = false)
    public int revision = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    protected void copyCommonFrom(AbstractLegalPage domain) {
        this.id = domain.getId() != null ? domain.getId() : 1L;
        this.eyebrow = domain.getEyebrow();
        this.title = domain.getTitle();
        this.introduction = domain.getIntroduction();
        this.documentControlLabel = domain.getDocumentControlLabel();
        this.documentControlText = domain.getDocumentControlText();
        this.sectionsJson = LegalJsonSupport.writeList(domain.getSections());
        this.closingTitle = domain.getClosingTitle();
        this.closingBody = domain.getClosingBody();
        this.closingLinkLabel = domain.getClosingLinkLabel();
        this.revision = domain.getRevision();
        this.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : Instant.now();
    }

    protected <T extends AbstractLegalPage> T copyCommonTo(T domain) {
        domain.setId(id);
        domain.setEyebrow(eyebrow);
        domain.setTitle(title);
        domain.setIntroduction(introduction);
        domain.setDocumentControlLabel(documentControlLabel);
        domain.setDocumentControlText(documentControlText);
        domain.setSections(LegalJsonSupport.readList(sectionsJson, new TypeReference<List<LegalSection>>() {}));
        domain.setClosingTitle(closingTitle);
        domain.setClosingBody(closingBody);
        domain.setClosingLinkLabel(closingLinkLabel);
        domain.setRevision(revision);
        domain.setCreatedAt(createdAt);
        domain.setUpdatedAt(updatedAt);
        return domain;
    }
}
