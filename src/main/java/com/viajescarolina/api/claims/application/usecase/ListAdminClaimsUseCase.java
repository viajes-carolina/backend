package com.viajescarolina.api.claims.application.usecase;

import com.viajescarolina.api.claims.application.dto.ClaimAttachmentDTO;
import com.viajescarolina.api.claims.application.dto.ClaimRecordDTO;
import com.viajescarolina.api.claims.domain.ClaimAttachmentRepository;
import com.viajescarolina.api.claims.domain.ClaimRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

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
        return claimRepository.findAllClaims(status).stream()
                .map(claim -> {
                    List<ClaimAttachmentDTO> attachments = attachmentRepository.findByClaimId(claim.getId()).stream()
                            .map(ClaimAttachmentDTO::fromDomain)
                            .toList();
                    return ClaimRecordDTO.fromDomain(claim, attachments);
                })
                .toList();
    }
}
