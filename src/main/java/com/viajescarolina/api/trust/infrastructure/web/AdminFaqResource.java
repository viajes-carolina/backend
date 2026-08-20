package com.viajescarolina.api.trust.infrastructure.web;

import com.viajescarolina.api.trust.application.dto.CreateOrUpdateFaqRequest;
import com.viajescarolina.api.trust.application.dto.FaqItemDTO;
import com.viajescarolina.api.trust.application.usecase.CreateFaqUseCase;
import com.viajescarolina.api.trust.application.usecase.DeleteFaqUseCase;
import com.viajescarolina.api.trust.application.usecase.ListAdminFaqsUseCase;
import com.viajescarolina.api.trust.application.usecase.UpdateFaqUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/admin/v1/faq")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin FAQ", description = "Gestión administrativa de preguntas frecuentes")
public class AdminFaqResource {

    private final ListAdminFaqsUseCase listUseCase;
    private final CreateFaqUseCase createUseCase;
    private final UpdateFaqUseCase updateUseCase;
    private final DeleteFaqUseCase deleteUseCase;

    @Inject
    public AdminFaqResource(
            ListAdminFaqsUseCase listUseCase,
            CreateFaqUseCase createUseCase,
            UpdateFaqUseCase updateUseCase,
            DeleteFaqUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @GET
    @Operation(summary = "Listar todas las FAQs", description = "Retorna lista de preguntas frecuentes para administración")
    public Response getAll() {
        List<FaqItemDTO> list = listUseCase.execute();
        return Response.ok(list).build();
    }

    @POST
    @Operation(summary = "Crear nueva FAQ", description = "Registra una nueva pregunta y respuesta")
    public Response create(@Valid CreateOrUpdateFaqRequest request) {
        FaqItemDTO created = createUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar FAQ", description = "Actualiza pregunta, respuesta, categoría u orden")
    public Response update(@PathParam("id") Long id, @Valid CreateOrUpdateFaqRequest request) {
        FaqItemDTO updated = updateUseCase.execute(id, request);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Desactivar FAQ", description = "Realiza soft-delete de FAQ")
    public Response delete(@PathParam("id") Long id) {
        deleteUseCase.execute(id);
        return Response.noContent().build();
    }
}
