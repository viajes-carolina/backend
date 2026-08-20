package com.viajescarolina.api.intentions.infrastructure.web;

import com.viajescarolina.api.intentions.application.dto.CreateOrUpdateTravelIntentionRequest;
import com.viajescarolina.api.intentions.application.dto.TravelIntentionDTO;
import com.viajescarolina.api.intentions.application.usecase.CreateTravelIntentionUseCase;
import com.viajescarolina.api.intentions.application.usecase.DeleteTravelIntentionUseCase;
import com.viajescarolina.api.intentions.application.usecase.ListAdminTravelIntentionsUseCase;
import com.viajescarolina.api.intentions.application.usecase.UpdateTravelIntentionUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/admin/v1/intentions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Travel Intentions", description = "Gestión centralizada de intenciones de viaje")
public class AdminTravelIntentionResource {

    private final ListAdminTravelIntentionsUseCase listUseCase;
    private final CreateTravelIntentionUseCase createUseCase;
    private final UpdateTravelIntentionUseCase updateUseCase;
    private final DeleteTravelIntentionUseCase deleteUseCase;

    @Inject
    public AdminTravelIntentionResource(
            ListAdminTravelIntentionsUseCase listUseCase,
            CreateTravelIntentionUseCase createUseCase,
            UpdateTravelIntentionUseCase updateUseCase,
            DeleteTravelIntentionUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @GET
    @Operation(summary = "Listar todas las intenciones de viaje", description = "Retorna lista de intenciones de viaje para administración")
    public Response getAll() {
        List<TravelIntentionDTO> list = listUseCase.execute();
        return Response.ok(list).build();
    }

    @POST
    @Operation(summary = "Crear nueva intención de viaje", description = "Registra una nueva intención con destinos y plantilla de WhatsApp")
    public Response create(@Valid CreateOrUpdateTravelIntentionRequest request) {
        TravelIntentionDTO created = createUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar intención de viaje", description = "Actualiza los textos, destinos y orden de una intención")
    public Response update(@PathParam("id") Long id, @Valid CreateOrUpdateTravelIntentionRequest request) {
        TravelIntentionDTO updated = updateUseCase.execute(id, request);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar / Desactivar intención de viaje", description = "Desactiva una intención de viaje")
    public Response delete(@PathParam("id") Long id) {
        deleteUseCase.execute(id);
        return Response.noContent().build();
    }
}
