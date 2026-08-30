package com.viajescarolina.api.legal.infrastructure.persistence;

import com.viajescarolina.api.legal.domain.LegalPrivacy;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "legal_privacy")
public class LegalPrivacyPanacheEntity extends AbstractLegalPageEntity {

    public LegalPrivacy toDomain() {
        return copyCommonTo(new LegalPrivacy());
    }

    public static LegalPrivacyPanacheEntity fromDomain(LegalPrivacy domain) {
        LegalPrivacyPanacheEntity entity = new LegalPrivacyPanacheEntity();
        entity.copyFrom(domain);
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        return entity;
    }

    public void copyFrom(LegalPrivacy domain) {
        copyCommonFrom(domain);
    }
}
