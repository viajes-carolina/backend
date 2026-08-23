package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomeConversationalPauseDTO;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeConversationalPauseUseCase;
import com.viajescarolina.api.home.application.usecase.UpdateHomeConversationalPauseUseCase;
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

@Path("/api/admin/v1/home/conversational-pause")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Home", description = "Endpoints de administración de la portada")
public class AdminHomeConversationalPauseResource {

    @Inject
    GetPublicHomeConversationalPauseUseCase getPublicHomeConversationalPauseUseCase;

    @Inject
    UpdateHomeConversationalPauseUseCase updateHomeConversationalPauseUseCase;

    @GET
    @Operation(summary = "Obtener configuración de la sección de pausa conversacional", description = "Retorna la configuración editorial actual de la sección")
    public Response getConfiguration() {
        HomeConversationalPauseDTO dto = getPublicHomeConversationalPauseUseCase.execute();
        return Response.ok(dto).build();
    }

    @PUT
    @Operation(summary = "Actualizar configuración de la sección de pausa conversacional", description = "Actualiza los textos y el CTA de WhatsApp de la sección")
    public Response updateConfiguration(HomeConversationalPauseDTO dto) {
        HomeConversationalPauseDTO updated = updateHomeConversationalPauseUseCase.execute(dto);
        return Response.ok(updated).build();
    }
}
