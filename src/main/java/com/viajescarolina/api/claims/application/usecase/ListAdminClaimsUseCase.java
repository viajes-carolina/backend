package com.viajescarolina.api.claims.application.usecase;

import com.viajescarolina.api.claims.application.dto.ClaimAttachmentDTO;
import com.viajescarolina.api.claims.application.dto.ClaimRecordDTO;
import com.viajescarolina.api.claims.domain.ClaimAttachment;
import com.viajescarolina.api.claims.domain.ClaimAttachmentRepository;
import com.viajescarolina.api.claims.domain.ClaimRecord;
import com.viajescarolina.api.claims.domain.ClaimRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ListAdminClaimsUseCase {

    private final ClaimRepository claimRepository;
    private final ClaimAttachmentRepository attachmentRepository;

    @Inject
    public ListAdminClaimsUseCase(ClaimRepository claimRepository, ClaimAttachmentRepository attachmentRepository) {
        this.claimRepository = claimRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public List<ClaimRecordDTO> execute(String status) {
        List<ClaimRecord> claims = claimRepository.findAllClaims(status);

        // Batch-resolve los adjuntos de TODOS los reclamos en una sola consulta
        // (WHERE claim_id IN (...)), en vez de una query por reclamo dentro del .map().
        List<Long> claimIds = claims.stream().map(ClaimRecord::getId).toList();
        Map<Long, List<ClaimAttachment>> attachmentsByClaimId = attachmentRepository.findByClaimIds(claimIds).stream()
                .collect(Collectors.groupingBy(ClaimAttachment::getClaimId));

        return claims.stream()
                .map(claim -> {
                    List<ClaimAttachmentDTO> attachments = attachmentsByClaimId
                            .getOrDefault(claim.getId(), List.of())
                            .stream()
                            .map(ClaimAttachmentDTO::fromDomain)
                            .toList();
                    return ClaimRecordDTO.fromDomain(claim, attachments);
                })
                .toList();
    }
}
