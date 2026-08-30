package com.viajescarolina.api.legal.infrastructure.persistence;

import com.viajescarolina.api.legal.domain.LegalTerms;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "legal_terms")
public class LegalTermsPanacheEntity extends AbstractLegalPageEntity {

    public LegalTerms toDomain() {
        return copyCommonTo(new LegalTerms());
    }

    public static LegalTermsPanacheEntity fromDomain(LegalTerms domain) {
        LegalTermsPanacheEntity entity = new LegalTermsPanacheEntity();
        entity.copyFrom(domain);
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        return entity;
    }

    public void copyFrom(LegalTerms domain) {
        copyCommonFrom(domain);
    }
}
