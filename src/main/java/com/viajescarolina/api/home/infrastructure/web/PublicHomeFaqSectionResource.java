package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomeFaqSectionDTO;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeFaqSectionUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/home/faq-section")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Public Home", description = "Endpoints públicos de portada y contenidos de inicio")
public class PublicHomeFaqSectionResource {

    @Inject
    GetPublicHomeFaqSectionUseCase getPublicHomeFaqSectionUseCase;

    @GET
    @Operation(summary = "Obtener sección de preguntas frecuentes para portada", description = "Retorna configuración editorial del encabezado de la sección de preguntas frecuentes en home")
    public Response getFaqSection() {
        HomeFaqSectionDTO dto = getPublicHomeFaqSectionUseCase.execute();
        return Response.ok(dto).build();
    }
}
