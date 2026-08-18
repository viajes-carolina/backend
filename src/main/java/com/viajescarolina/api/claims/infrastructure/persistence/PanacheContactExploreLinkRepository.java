package com.viajescarolina.api.claims.infrastructure.persistence;

import com.viajescarolina.api.claims.domain.ContactExploreLink;
import com.viajescarolina.api.claims.domain.ContactExploreLinkRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheContactExploreLinkRepository implements ContactExploreLinkRepository, PanacheRepositoryBase<ContactExploreLinkPanacheEntity, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ContactExploreLink> findActiveOrdered() {
        return find("active = true ORDER BY displayOrder ASC")
                .stream()
                .map(ContactExploreLinkPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public List<ContactExploreLink> findAllOrdered() {
        return find("ORDER BY displayOrder ASC")
                .stream()
                .map(ContactExploreLinkPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<ContactExploreLink> findLinkById(Long id) {
        return findByIdOptional(id).map(ContactExploreLinkPanacheEntity::toDomain);
    }

    @Override
    public ContactExploreLink save(ContactExploreLink link) {
        ContactExploreLinkPanacheEntity entity = ContactExploreLinkPanacheEntity.fromDomain(link);
        if (entity.id == null) {
            persist(entity);
        } else {
            entity = em.merge(entity);
        }
        return entity.toDomain();
    }

    @Override
    public void deleteLinkById(Long id) {
        deleteById(id);
    }
}
