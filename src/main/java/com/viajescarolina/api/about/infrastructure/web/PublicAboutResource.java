package com.viajescarolina.api.about.infrastructure.web;

import com.viajescarolina.api.about.application.dto.PublicAboutResponse;
import com.viajescarolina.api.about.application.usecase.GetPublicAboutUseCase;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/about")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Public About", description = "Endpoints públicos de información corporativa, historia y asesoras")
public class PublicAboutResource {

    private final GetPublicAboutUseCase getPublicAboutUseCase;

    public PublicAboutResource(GetPublicAboutUseCase getPublicAboutUseCase) {
        this.getPublicAboutUseCase = getPublicAboutUseCase;
    }

    @GET
    @Operation(summary = "Obtener contenido público de la página Nosotros (Historia, Misión, Visión, Stats y Asesoras)")
    public PublicAboutResponse getPublicAbout() {
        return getPublicAboutUseCase.execute();
    }
}
