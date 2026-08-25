package com.viajescarolina.api.promotions.infrastructure.web;

import com.viajescarolina.api.promotions.application.dto.CreateOrUpdatePromotionRequest;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.application.dto.SetPromotionActiveRequest;
import com.viajescarolina.api.promotions.application.usecase.CreatePromotionUseCase;
import com.viajescarolina.api.promotions.application.usecase.DeletePromotionUseCase;
import com.viajescarolina.api.promotions.application.usecase.ListAdminPromotionsUseCase;
import com.viajescarolina.api.promotions.application.usecase.SetPromotionActiveUseCase;
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
    private final SetPromotionActiveUseCase setActiveUseCase;
    private final CreatePromotionUseCase createUseCase;
    private final DeletePromotionUseCase deleteUseCase;

    @Inject
    public AdminPromotionResource(
            ListAdminPromotionsUseCase listUseCase,
            SetPromotionActiveUseCase setActiveUseCase,
            CreatePromotionUseCase createUseCase,
            DeletePromotionUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.setActiveUseCase = setActiveUseCase;
        this.createUseCase = createUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @GET
    @Operation(summary = "Listar todas las promociones", description = "Retorna lista de promociones para administración")
    public Response getAll() {
        List<PromotionDTO> list = listUseCase.execute();
        return Response.ok(list).build();
    }

    @POST
    @Operation(summary = "Crear promoción",
            description = "Crea una promoción desde el formulario estructurado del admin y, en modo best-effort, "
                    + "publica el mismo contenido como post con foto en la Página de Facebook")
    public Response create(@Valid CreateOrUpdatePromotionRequest request) {
        PromotionDTO created = createUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PATCH
    @Path("/{id}/active")
    @Operation(summary = "Mostrar u ocultar promoción", description = "Cambia la visibilidad pública de una promoción")
    public Response setActive(@PathParam("id") Long id, @Valid SetPromotionActiveRequest request) {
        PromotionDTO updated = setActiveUseCase.execute(id, request.active());
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Borrar promoción",
            description = "Elimina definitivamente una promoción. Rechaza el borrado si la promoción está "
                    + "activa y dejaría menos de 3 promociones activas para mostrar en Inicio.")
    public Response delete(@PathParam("id") Long id) {
        deleteUseCase.execute(id);
        return Response.noContent().build();
    }
}
