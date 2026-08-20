package com.viajescarolina.api.settings.application.usecase;

import com.viajescarolina.api.settings.application.dto.OfficeLocationDTO;
import com.viajescarolina.api.settings.application.dto.UpdateOfficeLocationRequest;
import com.viajescarolina.api.settings.domain.OfficeLocation;
import com.viajescarolina.api.settings.domain.OfficeRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateOfficeLocationUseCase {

    private final OfficeRepository officeRepository;

    @Inject
    public UpdateOfficeLocationUseCase(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Audited(action = "UPDATE_OFFICE_LOCATION", entityType = "OFFICE_LOCATION")
    @Transactional
    public OfficeLocationDTO execute(UpdateOfficeLocationRequest request) {
        OfficeLocation office = officeRepository.findOffice()
                .orElseGet(() -> new OfficeLocation(
                        1,
                        request.addressLine(),
                        request.district(),
                        request.city(),
                        request.country(),
                        request.postalCode(),
                        request.referenceLandmark(),
                        request.latitude(),
                        request.longitude(),
                        request.googleMapsUrl(),
                        request.embedMapsUrl(),
                        request.scheduleWeekdays(),
                        request.scheduleSaturdays(),
                        request.active(),
                        0,
                        null,
                        null
                ));

        office.update(
                request.addressLine(),
                request.district(),
                request.city(),
                request.country(),
                request.postalCode(),
                request.referenceLandmark(),
                request.latitude(),
                request.longitude(),
                request.googleMapsUrl(),
                request.embedMapsUrl(),
                request.scheduleWeekdays(),
                request.scheduleSaturdays(),
                request.active()
        );

        OfficeLocation saved = officeRepository.save(office);

        return new OfficeLocationDTO(
                saved.getId(),
                saved.getAddressLine(),
                saved.getDistrict(),
                saved.getCity(),
                saved.getCountry(),
                saved.getPostalCode(),
                saved.getReferenceLandmark(),
                saved.getLatitude(),
                saved.getLongitude(),
                saved.getGoogleMapsUrl(),
                saved.getEmbedMapsUrl(),
                saved.getScheduleWeekdays(),
                saved.getScheduleSaturdays(),
                saved.isActive(),
                saved.getRevision(),
                saved.getUpdatedAt()
        );
    }
}
