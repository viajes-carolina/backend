package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomeBlogInspirationDTO;
import com.viajescarolina.api.home.application.dto.PublicHomeBlogInspirationResponse;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeBlogInspirationUseCase;
import com.viajescarolina.api.home.application.usecase.UpdateHomeBlogInspirationUseCase;
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

@Path("/api/admin/v1/home/blog-inspiration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Home", description = "Endpoints de administración de la portada")
public class AdminHomeBlogInspirationResource {

    @Inject
    GetPublicHomeBlogInspirationUseCase getPublicHomeBlogInspirationUseCase;

    @Inject
    UpdateHomeBlogInspirationUseCase updateHomeBlogInspirationUseCase;

    @GET
    @Operation(summary = "Obtener configuración de la sección de inspiración de blog", description = "Retorna la configuración editorial actual de la sección")
    public Response getConfiguration() {
        PublicHomeBlogInspirationResponse response = getPublicHomeBlogInspirationUseCase.execute();
        return Response.ok(response.config()).build();
    }

    @PUT
    @Operation(summary = "Actualizar configuración de la sección de inspiración de blog", description = "Actualiza los textos, límites y estado activo de la sección")
    public Response updateConfiguration(HomeBlogInspirationDTO dto) {
        HomeBlogInspirationDTO updated = updateHomeBlogInspirationUseCase.execute(dto);
        return Response.ok(updated).build();
    }
}
