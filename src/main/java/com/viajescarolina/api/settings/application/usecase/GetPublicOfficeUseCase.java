package com.viajescarolina.api.settings.application.usecase;

import com.viajescarolina.api.settings.application.dto.PublicOfficeResponse;
import com.viajescarolina.api.settings.domain.OfficeLocation;
import com.viajescarolina.api.settings.domain.OfficeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;

@ApplicationScoped
public class GetPublicOfficeUseCase {

    private final OfficeRepository officeRepository;

    @Inject
    public GetPublicOfficeUseCase(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    public PublicOfficeResponse execute() {
        OfficeLocation office = officeRepository.findOffice()
                .orElseGet(() -> new OfficeLocation(
                        1,
                        "Av. Larco 101, Oficina 502",
                        "Miraflores",
                        "Lima",
                        "Perú",
                        "15074",
                        "A media cuadra del Parque Kennedy",
                        new BigDecimal("-12.1215430"),
                        new BigDecimal("-77.0298760"),
                        "https://maps.google.com/?q=Miraflores,Lima,Peru",
                        null,
                        "Lunes a Viernes: 9:00 AM – 7:00 PM",
                        "Sábados: 9:00 AM – 2:00 PM",
                        true,
                        1,
                        null,
                        null
                ));

        String fullAddress = String.format("%s, %s, %s", office.getAddressLine(), office.getDistrict(), office.getCity());

        return new PublicOfficeResponse(
                fullAddress,
                office.getDistrict(),
                office.getCity(),
                office.getCountry(),
                office.getReferenceLandmark(),
                office.getLatitude(),
                office.getLongitude(),
                office.getGoogleMapsUrl(),
                office.getEmbedMapsUrl(),
                office.getScheduleWeekdays(),
                office.getScheduleSaturdays(),
                office.isActive()
        );
    }
}
