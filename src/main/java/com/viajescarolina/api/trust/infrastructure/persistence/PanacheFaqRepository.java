package com.viajescarolina.api.trust.infrastructure.persistence;

import com.viajescarolina.api.trust.domain.FaqItem;
import com.viajescarolina.api.trust.domain.FaqRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheFaqRepository implements FaqRepository, PanacheRepositoryBase<FaqPanacheEntity, Long> {

    @Override
    public List<FaqItem> findAllActive() {
        return find("active = true ORDER BY displayOrder ASC").stream()
                .map(FaqPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public List<FaqItem> findAllFaqs() {
        return list("ORDER BY displayOrder ASC").stream()
                .map(FaqPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<FaqItem> findFaqById(Long id) {
        return findByIdOptional(id).map(FaqPanacheEntity::toDomain);
    }

    @Override
    public FaqItem save(FaqItem faqItem) {
        FaqPanacheEntity entity = FaqPanacheEntity.fromDomain(faqItem);
        if (entity.id == null) {
            persist(entity);
            return entity.toDomain();
        } else {
            entity = getEntityManager().merge(entity);
            return entity.toDomain();
        }
    }

    @Override
    public void delete(Long id) {
        deleteById(id);
    }
}
