package com.viajescarolina.api.claims.infrastructure.persistence;

import com.viajescarolina.api.claims.domain.ClaimRecord;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "claim_record")
public class ClaimPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "claim_code", nullable = false, unique = true, length = 32)
    public String claimCode;

    @Column(name = "full_name", nullable = false, length = 150)
    public String fullName;

    @Column(name = "document_type", nullable = false, length = 20)
    public String documentType;

    @Column(name = "document_number", nullable = false, length = 30)
    public String documentNumber;

    @Column(nullable = false, length = 150)
    public String email;

    @Column(nullable = false, length = 50)
    public String phone;

    @Column(nullable = false, length = 255)
    public String address;

    @Column(name = "is_minor", nullable = false)
    public boolean isMinor;

    @Column(name = "parent_name", length = 150)
    public String parentName;

    @Column(name = "parent_document", length = 30)
    public String parentDocument;

    @Column(name = "contracted_type", nullable = false, length = 20)
    public String contractedType;

    @Column(name = "claimed_amount", precision = 12, scale = 2)
    public BigDecimal claimedAmount;

    @Column(nullable = false, length = 10)
    public String currency;

    @Column(nullable = false, length = 2000)
    public String description;

    @Column(name = "claim_type", nullable = false, length = 20)
    public String claimType;

    @Column(name = "consumer_detail", nullable = false, length = 3000)
    public String consumerDetail;

    @Column(name = "consumer_request", nullable = false, length = 2000)
    public String consumerRequest;

    @Column(nullable = false, length = 30)
    public String status;

    @Column(name = "response_notes", length = 3000)
    public String responseNotes;

    @Column(name = "response_at")
    public OffsetDateTime responseAt;

    @Column(name = "turnstile_token", length = 255)
    public String turnstileToken;

    @Column(name = "client_ip_hash", length = 64)
    public String clientIpHash;

    @Column(name = "created_at", nullable = false)
    public OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    public OffsetDateTime updatedAt;

    public ClaimRecord toDomain() {
        return new ClaimRecord(
            id, claimCode, fullName, documentType, documentNumber,
            email, phone, address, isMinor, parentName,
            parentDocument, contractedType, claimedAmount, currency,
            description, claimType, consumerDetail, consumerRequest,
            status, responseNotes, responseAt, turnstileToken,
            clientIpHash, createdAt, updatedAt
        );
    }

    public static ClaimPanacheEntity fromDomain(ClaimRecord domain) {
        ClaimPanacheEntity entity = new ClaimPanacheEntity();
        entity.id = domain.getId();
        entity.claimCode = domain.getClaimCode();
        entity.fullName = domain.getFullName();
        entity.documentType = domain.getDocumentType();
        entity.documentNumber = domain.getDocumentNumber();
        entity.email = domain.getEmail();
        entity.phone = domain.getPhone();
        entity.address = domain.getAddress();
        entity.isMinor = domain.isMinor();
        entity.parentName = domain.getParentName();
        entity.parentDocument = domain.getParentDocument();
        entity.contractedType = domain.getContractedType();
        entity.claimedAmount = domain.getClaimedAmount();
        entity.currency = domain.getCurrency();
        entity.description = domain.getDescription();
        entity.claimType = domain.getClaimType();
        entity.consumerDetail = domain.getConsumerDetail();
        entity.consumerRequest = domain.getConsumerRequest();
        entity.status = domain.getStatus();
        entity.responseNotes = domain.getResponseNotes();
        entity.responseAt = domain.getResponseAt();
        entity.turnstileToken = domain.getTurnstileToken();
        entity.clientIpHash = domain.getClientIpHash();
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : OffsetDateTime.now();
        entity.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : OffsetDateTime.now();
        return entity;
    }
}
