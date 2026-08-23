package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomeTestimonialsSectionDTO;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeTestimonialsSectionUseCase;
import com.viajescarolina.api.home.application.usecase.UpdateHomeTestimonialsSectionUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/admin/v1/home/testimonials-section")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Home", description = "Endpoints de administración de la portada")
public class AdminHomeTestimonialsSectionResource {

    @Inject
    GetPublicHomeTestimonialsSectionUseCase getPublicHomeTestimonialsSectionUseCase;

    @Inject
    UpdateHomeTestimonialsSectionUseCase updateHomeTestimonialsSectionUseCase;

    @GET
    @Operation(summary = "Obtener configuración de la sección de testimonios", description = "Retorna la configuración editorial actual de la sección")
    public Response getConfiguration() {
        HomeTestimonialsSectionDTO dto = getPublicHomeTestimonialsSectionUseCase.execute();
        return Response.ok(dto).build();
    }

    @PUT
    @Operation(summary = "Actualizar configuración de la sección de testimonios", description = "Actualiza el badge, título y subtítulo del encabezado de la sección")
    public Response updateConfiguration(HomeTestimonialsSectionDTO dto) {
        HomeTestimonialsSectionDTO updated = updateHomeTestimonialsSectionUseCase.execute(dto);
        return Response.ok(updated).build();
    }
}
