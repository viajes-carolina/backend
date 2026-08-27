package com.viajescarolina.api.about.infrastructure.persistence;

import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.media.infrastructure.persistence.MediaAssetPanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class PanacheTravelAdvisorRepository implements PanacheRepositoryBase<TravelAdvisorPanacheEntity, Long>, TravelAdvisorRepository {

    @Inject
    MediaRepository mediaRepository;

    @Override
    public List<TravelAdvisor> listPublicActive() {
        List<TravelAdvisorPanacheEntity> entities = find("active = true order by displayOrder asc, id asc").list();
        return enrichWithPhotosBatch(entities);
    }

    @Override
    public List<TravelAdvisor> listAdminAll() {
        List<TravelAdvisorPanacheEntity> entities = find("order by displayOrder asc, id asc").list();
        return enrichWithPhotosBatch(entities);
    }

    @Override
    public Optional<TravelAdvisor> findAdvisorById(Long id) {
        return findByIdOptional(id).map(this::enrichWithPhoto);
    }

    @Override
    public List<TravelAdvisor> findAdvisorsByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        // Nota de perfiling (JFR, sesión de carga): Hibernate 7.2.14 lanza (y
        // captura internamente) un CoercionException por cada bind de un
        // parámetro Collection en una cláusula "in" vía HQL — ocurre igual con
        // List concreta o parámetro nombrado, es un costo interno del binder de
        // este framework/versión, no un error de uso. No afecta el resultado ni
        // se ve reflejado en la latencia real medida (p95 se mantiene en el
        // rango de milisegundos de un solo dígito). Se deja con List concreta
        // por buena práctica, sin perseguir más allá dado el impacto real bajo.
        List<Long> idList = ids instanceof List<Long> list ? list : List.copyOf(ids);
        List<TravelAdvisorPanacheEntity> entities = find("id in ?1", idList).list();
        return enrichWithPhotosBatch(entities);
    }

    /**
     * Resuelve las fotos de una lista de asesoras en una sola consulta batch (evita el
     * patrón N+1 de invocar {@link #enrichWithPhoto} dentro de un {@code .map()} por fila).
     * Pensado para llamarse con el resultado ya paginado/filtrado de una query de listado.
     */
    private List<TravelAdvisor> enrichWithPhotosBatch(List<TravelAdvisorPanacheEntity> entities) {
        if (entities.isEmpty()) {
            return List.of();
        }

        List<Long> photoMediaIds = entities.stream()
                .map(e -> e.photoMediaId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, MediaAsset> mediaById = mediaRepository.findMediaByIds(photoMediaIds).stream()
                .collect(Collectors.toMap(MediaAsset::getId, Function.identity()));

        return entities.stream()
                .map(e -> {
                    String photoUrl = e.photoMediaId != null
                            ? Optional.ofNullable(mediaById.get(e.photoMediaId)).map(MediaAsset::getStoragePath).orElse(null)
                            : null;
                    return e.toDomain(photoUrl);
                })
                .toList();
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
