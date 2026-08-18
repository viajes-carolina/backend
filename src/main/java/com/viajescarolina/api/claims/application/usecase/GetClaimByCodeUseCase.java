package com.viajescarolina.api.claims.application.usecase;

import com.viajescarolina.api.claims.application.dto.ClaimRecordDTO;
import com.viajescarolina.api.claims.domain.ClaimRecord;
import com.viajescarolina.api.claims.domain.ClaimRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class GetClaimByCodeUseCase {

    private final ClaimRepository claimRepository;

    @Inject
    public GetClaimByCodeUseCase(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public Optional<ClaimRecordDTO> execute(String claimCode) {
        return claimRepository.findByCode(claimCode).map(ClaimRecordDTO::fromDomain);
    }
}
