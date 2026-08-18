package com.viajescarolina.api.claims.application.usecase;

import com.viajescarolina.api.claims.application.dto.ClaimRecordDTO;
import com.viajescarolina.api.claims.domain.ClaimRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ListAdminClaimsUseCase {

    private final ClaimRepository claimRepository;

    @Inject
    public ListAdminClaimsUseCase(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public List<ClaimRecordDTO> execute(String status) {
        return claimRepository.findAllClaims(status).stream()
                .map(ClaimRecordDTO::fromDomain)
                .toList();
    }
}
