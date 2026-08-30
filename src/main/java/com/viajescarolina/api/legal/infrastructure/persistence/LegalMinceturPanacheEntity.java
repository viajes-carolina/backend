package com.viajescarolina.api.legal.infrastructure.persistence;

import com.viajescarolina.api.legal.domain.LegalMincetur;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "legal_mincetur")
public class LegalMinceturPanacheEntity extends AbstractLegalPageEntity {

    @Column(name = "verification_eyebrow", nullable = false)
    public String verificationEyebrow;

    @Column(name = "verification_button_label", nullable = false)
    public String verificationButtonLabel;

    @Column(name = "verification_note", nullable = false, columnDefinition = "TEXT")
    public String verificationNote;

    public LegalMincetur toDomain() {
        LegalMincetur domain = copyCommonTo(new LegalMincetur());
        domain.setVerificationEyebrow(verificationEyebrow);
        domain.setVerificationButtonLabel(verificationButtonLabel);
        domain.setVerificationNote(verificationNote);
        return domain;
    }

    public static LegalMinceturPanacheEntity fromDomain(LegalMincetur domain) {
        LegalMinceturPanacheEntity entity = new LegalMinceturPanacheEntity();
        entity.copyFrom(domain);
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        return entity;
    }

    public void copyFrom(LegalMincetur domain) {
        copyCommonFrom(domain);
        this.verificationEyebrow = domain.getVerificationEyebrow();
        this.verificationButtonLabel = domain.getVerificationButtonLabel();
        this.verificationNote = domain.getVerificationNote();
    }
}
