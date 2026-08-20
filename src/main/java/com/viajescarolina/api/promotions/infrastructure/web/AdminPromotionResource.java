package com.viajescarolina.api.promotions.infrastructure.web;

import com.viajescarolina.api.promotions.application.dto.CreateOrUpdatePromotionRequest;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.application.usecase.CreatePromotionUseCase;
import com.viajescarolina.api.promotions.application.usecase.DeletePromotionUseCase;
import com.viajescarolina.api.promotions.application.usecase.ListAdminPromotionsUseCase;
import com.viajescarolina.api.promotions.application.usecase.UpdatePromotionUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/admin/v1/promotions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Promotions", description = "Gestión centralizada de paquetes turísticos y promociones")
public class AdminPromotionResource {

    private final ListAdminPromotionsUseCase listUseCase;
    private final CreatePromotionUseCase createUseCase;
    private final UpdatePromotionUseCase updateUseCase;
    private final DeletePromotionUseCase deleteUseCase;

    @Inject
    public AdminPromotionResource(
            ListAdminPromotionsUseCase listUseCase,
            CreatePromotionUseCase createUseCase,
            UpdatePromotionUseCase updateUseCase,
            DeletePromotionUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @GET
    @Operation(summary = "Listar todas las promociones", description = "Retorna lista de promociones para administración")
    public Response getAll() {
        List<PromotionDTO> list = listUseCase.execute();
        return Response.ok(list).build();
    }

    @POST
    @Operation(summary = "Crear nueva promoción", description = "Registra un nuevo paquete turístico o promoción")
    public Response create(@Valid CreateOrUpdatePromotionRequest request) {
        PromotionDTO created = createUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar promoción", description = "Actualiza detalles, precios, fotos e inclusiones")
    public Response update(@PathParam("id") Long id, @Valid CreateOrUpdatePromotionRequest request) {
        PromotionDTO updated = updateUseCase.execute(id, request);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Desactivar promoción", description = "Realiza soft-delete / desactivación de una promoción")
    public Response delete(@PathParam("id") Long id) {
        deleteUseCase.execute(id);
        return Response.noContent().build();
    }
}
