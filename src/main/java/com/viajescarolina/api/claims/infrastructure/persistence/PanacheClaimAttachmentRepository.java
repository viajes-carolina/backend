package com.viajescarolina.api.claims.infrastructure.persistence;

import com.viajescarolina.api.claims.domain.ClaimAttachment;
import com.viajescarolina.api.claims.domain.ClaimAttachmentRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheClaimAttachmentRepository implements ClaimAttachmentRepository, PanacheRepositoryBase<ClaimAttachmentPanacheEntity, Long> {

    @Override
    public ClaimAttachment save(ClaimAttachment domain) {
        ClaimAttachmentPanacheEntity entity = ClaimAttachmentPanacheEntity.fromDomain(domain);
        if (entity.id == null) {
            persist(entity);
        } else {
            entity = getEntityManager().merge(entity);
        }
        return entity.toDomain();
    }

    @Override
    public List<ClaimAttachment> findByClaimId(Long claimId) {
        return find("claimId = ?1 ORDER BY createdAt ASC", claimId)
                .stream()
                .map(ClaimAttachmentPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<ClaimAttachment> findAttachmentById(Long id) {
        return findByIdOptional(id).map(ClaimAttachmentPanacheEntity::toDomain);
    }
}
