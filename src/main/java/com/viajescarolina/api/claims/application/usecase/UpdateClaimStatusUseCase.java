package com.viajescarolina.api.claims.application.usecase;

import com.viajescarolina.api.claims.application.dto.ClaimRecordDTO;
import com.viajescarolina.api.claims.application.dto.UpdateClaimStatusRequest;
import com.viajescarolina.api.claims.domain.ClaimRecord;
import com.viajescarolina.api.claims.domain.ClaimRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class UpdateClaimStatusUseCase {

    private final ClaimRepository claimRepository;

    @Inject
    public UpdateClaimStatusUseCase(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @Transactional
    public Optional<ClaimRecordDTO> execute(Long id, UpdateClaimStatusRequest request) {
        Optional<ClaimRecord> existingOpt = claimRepository.findClaimById(id);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        ClaimRecord claim = existingOpt.get();
        claim.setStatus(request.status().toUpperCase().trim());
        if (request.responseNotes() != null) {
            claim.setResponseNotes(request.responseNotes().trim());
            claim.setResponseAt(OffsetDateTime.now());
        }
        claim.setUpdatedAt(OffsetDateTime.now());

        ClaimRecord saved = claimRepository.save(claim);
        return Optional.of(ClaimRecordDTO.fromDomain(saved));
    }
}
