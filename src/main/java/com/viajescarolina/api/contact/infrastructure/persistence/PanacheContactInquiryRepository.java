package com.viajescarolina.api.contact.infrastructure.persistence;

import com.viajescarolina.api.contact.domain.ContactInquiry;
import com.viajescarolina.api.contact.domain.ContactInquiryRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheContactInquiryRepository implements PanacheRepositoryBase<ContactInquiryPanacheEntity, Long>, ContactInquiryRepository {

    @Override
    public List<ContactInquiry> listAdminAll(String statusFilter) {
        if (statusFilter != null && !statusFilter.isBlank() && !statusFilter.equalsIgnoreCase("ALL")) {
            return find("status = ?1 order by createdAt desc", statusFilter.toUpperCase())
                .list()
                .stream()
                .map(ContactInquiryPanacheEntity::toDomain)
                .toList();
        }
        return find("order by createdAt desc")
            .list()
            .stream()
            .map(ContactInquiryPanacheEntity::toDomain)
            .toList();
    }

    @Override
    public Optional<ContactInquiry> findInquiryById(Long id) {
        return findByIdOptional(id).map(ContactInquiryPanacheEntity::toDomain);
    }

    @Override
    public ContactInquiry save(ContactInquiry inquiry) {
        ContactInquiryPanacheEntity entity = ContactInquiryPanacheEntity.fromDomain(inquiry);
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
