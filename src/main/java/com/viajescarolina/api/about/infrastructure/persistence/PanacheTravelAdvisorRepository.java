package com.viajescarolina.api.about.infrastructure.persistence;

import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.media.infrastructure.persistence.MediaAssetPanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheTravelAdvisorRepository implements PanacheRepositoryBase<TravelAdvisorPanacheEntity, Long>, TravelAdvisorRepository {

    @Override
    public List<TravelAdvisor> listPublicActive() {
        return find("active = true order by displayOrder asc, id asc")
            .list()
            .stream()
            .map(this::enrichWithPhoto)
            .toList();
    }

    @Override
    public List<TravelAdvisor> listAdminAll() {
        return find("order by displayOrder asc, id asc")
            .list()
            .stream()
            .map(this::enrichWithPhoto)
            .toList();
    }

    @Override
    public Optional<TravelAdvisor> findAdvisorById(Long id) {
        return findByIdOptional(id).map(this::enrichWithPhoto);
    }

    @Override
    public TravelAdvisor save(TravelAdvisor advisor) {
        TravelAdvisorPanacheEntity entity = TravelAdvisorPanacheEntity.fromDomain(advisor);
        if (entity.id == null) {
            persist(entity);
            return enrichWithPhoto(entity);
        } else {
            entity = getEntityManager().merge(entity);
            return enrichWithPhoto(entity);
        }
    }

    @Override
    public void delete(Long id) {
        deleteById(id);
    }

    private TravelAdvisor enrichWithPhoto(TravelAdvisorPanacheEntity entity) {
        String photoUrl = null;
        if (entity.photoMediaId != null) {
            MediaAssetPanacheEntity media = MediaAssetPanacheEntity.findById(entity.photoMediaId);
            if (media != null) {
                photoUrl = media.storagePath;
            }
        }
        return entity.toDomain(photoUrl);
    }
}
