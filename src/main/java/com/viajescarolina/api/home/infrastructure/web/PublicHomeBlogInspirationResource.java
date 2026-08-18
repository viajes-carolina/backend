package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.PublicHomeBlogInspirationResponse;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeBlogInspirationUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/home/blog-inspiration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Public Home", description = "Endpoints públicos de portada y contenidos de inicio")
public class PublicHomeBlogInspirationResource {

    @Inject
    GetPublicHomeBlogInspirationUseCase getPublicHomeBlogInspirationUseCase;

    @GET
    @Operation(summary = "Obtener sección de inspiración de blog para portada", description = "Retorna configuración editorial y los artículos más recientes del blog para la sección de inspiración en home")
    public Response getBlogInspiration() {
        PublicHomeBlogInspirationResponse response = getPublicHomeBlogInspirationUseCase.execute();
        return Response.ok(response).build();
    }
}
