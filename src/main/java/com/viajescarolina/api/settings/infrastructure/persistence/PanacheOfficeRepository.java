package com.viajescarolina.api.settings.infrastructure.persistence;

import com.viajescarolina.api.settings.domain.OfficeLocation;
import com.viajescarolina.api.settings.domain.OfficeRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class PanacheOfficeRepository implements OfficeRepository {

    @Override
    public Optional<OfficeLocation> findOffice() {
        OfficeLocationPanacheEntity entity = OfficeLocationPanacheEntity.findById(1);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(entity));
    }

    @Override
    public OfficeLocation save(OfficeLocation office) {
        OfficeLocationPanacheEntity entity = OfficeLocationPanacheEntity.findById(1);
        if (entity == null) {
            entity = new OfficeLocationPanacheEntity();
            entity.id = 1;
        }

        entity.addressLine = office.getAddressLine();
        entity.district = office.getDistrict();
        entity.city = office.getCity();
        entity.country = office.getCountry();
        entity.postalCode = office.getPostalCode();
        entity.referenceLandmark = office.getReferenceLandmark();
        entity.latitude = office.getLatitude();
        entity.longitude = office.getLongitude();
        entity.googleMapsUrl = office.getGoogleMapsUrl();
        entity.embedMapsUrl = office.getEmbedMapsUrl();
        entity.scheduleWeekdays = office.getScheduleWeekdays();
        entity.scheduleSaturdays = office.getScheduleSaturdays();
        entity.active = office.isActive();
        entity.revision = office.getRevision();
        entity.updatedAt = office.getUpdatedAt();

        entity.persist();
        return toDomain(entity);
    }

    private OfficeLocation toDomain(OfficeLocationPanacheEntity entity) {
        return new OfficeLocation(
                entity.id,
                entity.addressLine,
                entity.district,
                entity.city,
                entity.country,
                entity.postalCode,
                entity.referenceLandmark,
                entity.latitude,
                entity.longitude,
                entity.googleMapsUrl,
                entity.embedMapsUrl,
                entity.scheduleWeekdays,
                entity.scheduleSaturdays,
                entity.active,
                entity.revision,
                entity.createdAt,
                entity.updatedAt
        );
    }
}
