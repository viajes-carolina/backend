package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomeFaqSectionDTO;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeFaqSectionUseCase;
import com.viajescarolina.api.home.application.usecase.UpdateHomeFaqSectionUseCase;
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

@Path("/api/admin/v1/home/faq-section")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Home", description = "Endpoints de administración de la portada")
public class AdminHomeFaqSectionResource {

    @Inject
    GetPublicHomeFaqSectionUseCase getPublicHomeFaqSectionUseCase;

    @Inject
    UpdateHomeFaqSectionUseCase updateHomeFaqSectionUseCase;

    @GET
    @Operation(summary = "Obtener configuración de la sección de preguntas frecuentes", description = "Retorna la configuración editorial actual de la sección")
    public Response getConfiguration() {
        HomeFaqSectionDTO dto = getPublicHomeFaqSectionUseCase.execute();
        return Response.ok(dto).build();
    }

    @PUT
    @Operation(summary = "Actualizar configuración de la sección de preguntas frecuentes", description = "Actualiza el badge, título y subtítulo del encabezado de la sección")
    public Response updateConfiguration(HomeFaqSectionDTO dto) {
        HomeFaqSectionDTO updated = updateHomeFaqSectionUseCase.execute(dto);
        return Response.ok(updated).build();
    }
}
