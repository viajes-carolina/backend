package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomeHeroDTO;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeHeroUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/home/hero")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Public Home Hero", description = "Endpoint público para renderizado de la sección Hero")
public class PublicHomeHeroResource {

    private final GetPublicHomeHeroUseCase getUseCase;

    @Inject
    public PublicHomeHeroResource(GetPublicHomeHeroUseCase getUseCase) {
        this.getUseCase = getUseCase;
    }

    @GET
    @Operation(summary = "Obtener Hero de Inicio para Web Pública", description = "Retorna configuración pública del Hero con encabezados de caché")
    public Response getPublicHero() {
        HomeHeroDTO dto = getUseCase.execute();
        return Response.ok(dto)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=60, s-maxage=300")
                .build();
    }
}
