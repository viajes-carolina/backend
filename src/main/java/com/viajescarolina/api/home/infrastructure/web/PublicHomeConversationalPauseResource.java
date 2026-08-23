package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomeConversationalPauseDTO;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeConversationalPauseUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/home/conversational-pause")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Public Home", description = "Endpoints públicos de portada y contenidos de inicio")
public class PublicHomeConversationalPauseResource {

    @Inject
    GetPublicHomeConversationalPauseUseCase getPublicHomeConversationalPauseUseCase;

    @GET
    @Operation(summary = "Obtener sección de pausa conversacional para portada", description = "Retorna configuración editorial de la pausa conversacional entre Blog/Inspiración y Testimonios en home")
    public Response getConversationalPause() {
        HomeConversationalPauseDTO dto = getPublicHomeConversationalPauseUseCase.execute();
        return Response.ok(dto).build();
    }
}
