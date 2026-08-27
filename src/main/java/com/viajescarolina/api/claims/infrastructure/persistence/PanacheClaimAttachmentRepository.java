package com.viajescarolina.api.claims.infrastructure.persistence;

import com.viajescarolina.api.claims.domain.ClaimAttachment;
import com.viajescarolina.api.claims.domain.ClaimAttachmentRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collection;
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
    public List<ClaimAttachment> findByClaimIds(Collection<Long> claimIds) {
        if (claimIds == null || claimIds.isEmpty()) {
            return List.of();
        }
        // Nota de perfiling (JFR, sesión de carga): Hibernate 7.2.14 lanza (y
        // captura internamente) un CoercionException por cada bind de un
        // parámetro Collection en una cláusula "in" vía HQL — ocurre igual con
        // List concreta; es un costo interno del binder de este framework/
        // versión, no un error de uso. No afecta el resultado ni la latencia
        // real. Se deja con List concreta por buena práctica.
        List<Long> idList = claimIds instanceof List<Long> list ? list : List.copyOf(claimIds);
        return find("claimId in ?1 ORDER BY createdAt ASC", idList)
                .stream()
                .map(ClaimAttachmentPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<ClaimAttachment> findAttachmentById(Long id) {
        return findByIdOptional(id).map(ClaimAttachmentPanacheEntity::toDomain);
    }
}
