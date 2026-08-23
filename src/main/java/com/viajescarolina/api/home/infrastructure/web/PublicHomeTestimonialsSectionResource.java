package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomeTestimonialsSectionDTO;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeTestimonialsSectionUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/home/testimonials-section")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Public Home", description = "Endpoints públicos de portada y contenidos de inicio")
public class PublicHomeTestimonialsSectionResource {

    @Inject
    GetPublicHomeTestimonialsSectionUseCase getPublicHomeTestimonialsSectionUseCase;

    @GET
    @Operation(summary = "Obtener sección de testimonios para portada", description = "Retorna configuración editorial del encabezado de la sección de testimonios en home")
    public Response getTestimonialsSection() {
        HomeTestimonialsSectionDTO dto = getPublicHomeTestimonialsSectionUseCase.execute();
        return Response.ok(dto).build();
    }
}
