package com.viajescarolina.api.contact.infrastructure.persistence;

import com.viajescarolina.api.contact.domain.ContactPage;
import com.viajescarolina.api.contact.domain.ContactPageRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheContactPageRepository implements PanacheRepositoryBase<ContactPagePanacheEntity, Integer>, ContactPageRepository {

    @Override
    public Optional<ContactPage> findSingleton() {
        return findByIdOptional(1).map(ContactPagePanacheEntity::toDomain);
    }

    @Override
    public ContactPage save(ContactPage contactPage) {
        ContactPagePanacheEntity entity = findById(1);
        if (entity == null) {
            entity = ContactPagePanacheEntity.fromDomain(contactPage);
            persist(entity);
        } else {
            entity.heroBadge = contactPage.getHeroBadge();
            entity.heroTitle = contactPage.getHeroTitle();
            entity.heroSubtitle = contactPage.getHeroSubtitle();
            entity.whatsappBoxTitle = contactPage.getWhatsappBoxTitle();
            entity.whatsappBoxSubtitle = contactPage.getWhatsappBoxSubtitle();
            entity.formTitle = contactPage.getFormTitle();
            entity.formSubtitle = contactPage.getFormSubtitle();
            entity.revision = contactPage.getRevision();
            entity.updatedAt = contactPage.getUpdatedAt();
        }
        return entity.toDomain();
    }
}
