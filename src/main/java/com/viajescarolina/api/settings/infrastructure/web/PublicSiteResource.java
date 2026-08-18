package com.viajescarolina.api.settings.infrastructure.web;

import com.viajescarolina.api.settings.application.dto.PublicSiteResponse;
import com.viajescarolina.api.settings.application.usecase.GetPublicSiteSettingsUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/site")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Public Site", description = "Endpoints públicos de configuración e identidad del sitio")
public class PublicSiteResource {

    private final GetPublicSiteSettingsUseCase getPublicSiteSettingsUseCase;

    @Inject
    public PublicSiteResource(GetPublicSiteSettingsUseCase getPublicSiteSettingsUseCase) {
        this.getPublicSiteSettingsUseCase = getPublicSiteSettingsUseCase;
    }

    @GET
    @Operation(summary = "Obtener configuración pública del sitio", description = "Retorna el nombre del sitio, número de WhatsApp activo, plantillas de acción y redes sociales")
    public Response getPublicSiteSettings() {
        PublicSiteResponse response = getPublicSiteSettingsUseCase.execute();
        return Response.ok(response).build();
    }
}
