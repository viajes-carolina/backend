package com.viajescarolina.api.claims.application.dto;

import com.viajescarolina.api.claims.domain.ClaimRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

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
    String relatedService,
    String reservationCode,
    LocalDate serviceDate,
    String responseChannel,
    String description,
    String claimType,
    String consumerDetail,
    String consumerRequest,
    String status,
    String responseNotes,
    OffsetDateTime responseAt,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    List<ClaimAttachmentDTO> attachments
) {
    public static ClaimRecordDTO fromDomain(ClaimRecord entity) {
        return fromDomain(entity, List.of());
    }

    public static ClaimRecordDTO fromDomain(ClaimRecord entity, List<ClaimAttachmentDTO> attachments) {
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
            entity.getRelatedService(),
            entity.getReservationCode(),
            entity.getServiceDate(),
            entity.getResponseChannel(),
            entity.getDescription(),
            entity.getClaimType(),
            entity.getConsumerDetail(),
            entity.getConsumerRequest(),
            entity.getStatus(),
            entity.getResponseNotes(),
            entity.getResponseAt(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            attachments != null ? attachments : List.of()
        );
    }
}
