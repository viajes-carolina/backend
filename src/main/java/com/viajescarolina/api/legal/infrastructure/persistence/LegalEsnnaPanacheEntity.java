package com.viajescarolina.api.legal.infrastructure.persistence;

import com.viajescarolina.api.legal.domain.LegalEsnna;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "legal_esnna")
public class LegalEsnnaPanacheEntity extends AbstractLegalPageEntity {

    @Column(name = "declaration_eyebrow", nullable = false)
    public String declarationEyebrow;

    @Column(name = "declaration_title", nullable = false)
    public String declarationTitle;

    @Column(name = "declaration_body", nullable = false, columnDefinition = "TEXT")
    public String declarationBody;

    public LegalEsnna toDomain() {
        LegalEsnna domain = copyCommonTo(new LegalEsnna());
        domain.setDeclarationEyebrow(declarationEyebrow);
        domain.setDeclarationTitle(declarationTitle);
        domain.setDeclarationBody(declarationBody);
        return domain;
    }

    public static LegalEsnnaPanacheEntity fromDomain(LegalEsnna domain) {
        LegalEsnnaPanacheEntity entity = new LegalEsnnaPanacheEntity();
        entity.copyFrom(domain);
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        return entity;
    }

    public void copyFrom(LegalEsnna domain) {
        copyCommonFrom(domain);
        this.declarationEyebrow = domain.getDeclarationEyebrow();
        this.declarationTitle = domain.getDeclarationTitle();
        this.declarationBody = domain.getDeclarationBody();
    }
}
