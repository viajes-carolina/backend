package com.viajescarolina.api.claims.application.dto;

import com.viajescarolina.api.claims.domain.ClaimRecord;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ClaimRecordDTO(
    Long id,
    String claimCode,
    String fullName,
    String documentType,
    String documentNumber,
    String email,
    String phone,
    String address,
    boolean isMinor,
    String parentName,
    String parentDocument,
    String contractedType,
    BigDecimal claimedAmount,
    String currency,
    String description,
    String claimType,
    String consumerDetail,
    String consumerRequest,
    String status,
    String responseNotes,
    OffsetDateTime responseAt,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static ClaimRecordDTO fromDomain(ClaimRecord entity) {
        return new ClaimRecordDTO(
            entity.getId(),
            entity.getClaimCode(),
            entity.getFullName(),
            entity.getDocumentType(),
            entity.getDocumentNumber(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getAddress(),
            entity.isMinor(),
            entity.getParentName(),
            entity.getParentDocument(),
            entity.getContractedType(),
            entity.getClaimedAmount(),
            entity.getCurrency(),
            entity.getDescription(),
            entity.getClaimType(),
            entity.getConsumerDetail(),
            entity.getConsumerRequest(),
            entity.getStatus(),
            entity.getResponseNotes(),
            entity.getResponseAt(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
