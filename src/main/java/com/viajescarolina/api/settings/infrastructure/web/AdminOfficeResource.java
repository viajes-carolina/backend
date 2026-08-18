package com.viajescarolina.api.settings.infrastructure.web;

import com.viajescarolina.api.settings.application.dto.OfficeLocationDTO;
import com.viajescarolina.api.settings.application.dto.UpdateOfficeLocationRequest;
import com.viajescarolina.api.settings.application.usecase.UpdateOfficeLocationUseCase;
import com.viajescarolina.api.settings.domain.OfficeLocation;
import com.viajescarolina.api.settings.domain.OfficeRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.math.BigDecimal;

@Path("/api/admin/v1/office")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Admin Office", description = "Endpoints administrativos para la gestión de oficina física")
public class AdminOfficeResource {

    private final OfficeRepository officeRepository;
    private final UpdateOfficeLocationUseCase updateOfficeLocationUseCase;

    @Inject
    public AdminOfficeResource(OfficeRepository officeRepository, UpdateOfficeLocationUseCase updateOfficeLocationUseCase) {
        this.officeRepository = officeRepository;
        this.updateOfficeLocationUseCase = updateOfficeLocationUseCase;
    }

    @GET
    @Operation(summary = "Obtener detalles de oficina", description = "Retorna todos los campos de configuración de la oficina física")
    public Response getOffice() {
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

        OfficeLocationDTO dto = new OfficeLocationDTO(
                office.getId(),
                office.getAddressLine(),
                office.getDistrict(),
                office.getCity(),
                office.getCountry(),
                office.getPostalCode(),
                office.getReferenceLandmark(),
                office.getLatitude(),
                office.getLongitude(),
                office.getGoogleMapsUrl(),
                office.getEmbedMapsUrl(),
                office.getScheduleWeekdays(),
                office.getScheduleSaturdays(),
                office.isActive(),
                office.getRevision(),
                office.getUpdatedAt()
        );

        return Response.ok(dto).build();
    }

    @PUT
    @Operation(summary = "Actualizar oficina", description = "Actualiza los datos de dirección, horarios y coordenadas de la oficina física")
    public Response updateOffice(@Valid UpdateOfficeLocationRequest request) {
        OfficeLocationDTO updated = updateOfficeLocationUseCase.execute(request);
        return Response.ok(updated).build();
    }
}
