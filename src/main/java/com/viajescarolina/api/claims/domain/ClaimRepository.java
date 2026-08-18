package com.viajescarolina.api.claims.domain;

import java.util.List;
import java.util.Optional;

public interface ClaimRepository {
    ClaimRecord save(ClaimRecord claim);
    Optional<ClaimRecord> findClaimById(Long id);
    Optional<ClaimRecord> findByCode(String claimCode);
    List<ClaimRecord> findAllClaims(String status);
    String generateNextClaimCode();
}
