package com.viajescarolina.api.claims.infrastructure.persistence;

import com.viajescarolina.api.claims.domain.ClaimRecord;
import com.viajescarolina.api.claims.domain.ClaimRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheClaimRepository implements ClaimRepository, PanacheRepositoryBase<ClaimPanacheEntity, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public ClaimRecord save(ClaimRecord domain) {
        ClaimPanacheEntity entity = ClaimPanacheEntity.fromDomain(domain);
        if (entity.id == null) {
            persist(entity);
        } else {
            entity = em.merge(entity);
        }
        return entity.toDomain();
    }

    @Override
    public Optional<ClaimRecord> findClaimById(Long id) {
        return findByIdOptional(id).map(ClaimPanacheEntity::toDomain);
    }

    @Override
    public Optional<ClaimRecord> findByCode(String claimCode) {
        ClaimPanacheEntity entity = find("claimCode", claimCode).firstResult();
        return Optional.ofNullable(entity).map(ClaimPanacheEntity::toDomain);
    }

    @Override
    public List<ClaimRecord> findAllClaims(String status) {
        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            return find("status = ?1 ORDER BY createdAt DESC", status.toUpperCase())
                    .stream()
                    .map(ClaimPanacheEntity::toDomain)
                    .toList();
        }
        return find("ORDER BY createdAt DESC")
                .stream()
                .map(ClaimPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public String generateNextClaimCode() {
        try {
            Object res = em.createNativeQuery("SELECT nextval('claim_code_seq')").getSingleResult();
            long nextVal = res instanceof Number ? ((Number) res).longValue() : 1L;
            int year = Year.now().getValue();
            return String.format("REC-%d-%04d", year, nextVal);
        } catch (Exception e) {
            int year = Year.now().getValue();
            long random = (long) (Math.random() * 9000) + 1000;
            return String.format("REC-%d-%04d", year, random);
        }
    }
}
