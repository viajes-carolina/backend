package com.viajescarolina.api.settings.infrastructure.web;

import com.viajescarolina.api.settings.application.dto.PublicOfficeResponse;
import com.viajescarolina.api.settings.application.usecase.GetPublicOfficeUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/office")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Public Office", description = "Endpoints públicos de ubicación y horarios de oficina")
public class PublicOfficeResource {

    private final GetPublicOfficeUseCase getPublicOfficeUseCase;

    @Inject
    public PublicOfficeResource(GetPublicOfficeUseCase getPublicOfficeUseCase) {
        this.getPublicOfficeUseCase = getPublicOfficeUseCase;
    }

    @GET
    @Operation(summary = "Obtener datos públicos de la oficina", description = "Retorna dirección física, horarios de atención y enlaces de mapa")
    public Response getPublicOffice() {
        PublicOfficeResponse response = getPublicOfficeUseCase.execute();
        return Response.ok(response).build();
    }
}
