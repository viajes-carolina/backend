package com.viajescarolina.api.promotions.infrastructure.web;

import com.viajescarolina.api.promotions.application.dto.CreateOrUpdatePromotionRequest;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.application.dto.SetPromotionActiveRequest;
import com.viajescarolina.api.promotions.application.usecase.CreatePromotionUseCase;
import com.viajescarolina.api.promotions.application.usecase.DeletePromotionUseCase;
import com.viajescarolina.api.promotions.application.usecase.ListAdminPromotionsUseCase;
import com.viajescarolina.api.promotions.application.usecase.ListAdminPromotionsUseCase.PromotionsPageResponse;
import com.viajescarolina.api.promotions.application.usecase.SetPromotionActiveUseCase;
import com.viajescarolina.api.promotions.application.usecase.UpdatePromotionUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/admin/v1/promotions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Promotions", description = "Gestión centralizada de paquetes turísticos y promociones")
public class AdminPromotionResource {

    private final ListAdminPromotionsUseCase listUseCase;
    private final SetPromotionActiveUseCase setActiveUseCase;
    private final CreatePromotionUseCase createUseCase;
    private final UpdatePromotionUseCase updateUseCase;
    private final DeletePromotionUseCase deleteUseCase;

    @Inject
    public AdminPromotionResource(
            ListAdminPromotionsUseCase listUseCase,
            SetPromotionActiveUseCase setActiveUseCase,
            CreatePromotionUseCase createUseCase,
            UpdatePromotionUseCase updateUseCase,
            DeletePromotionUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.setActiveUseCase = setActiveUseCase;
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @GET
    @Operation(summary = "Listar promociones (paginado, con búsqueda y filtros)",
            description = """
                    Retorna UNA página del catálogo de promociones para el panel. La búsqueda, los \
                    filtros, el orden y el recorte se resuelven en la base de datos: el panel nunca \
                    descarga el catálogo entero para quedarse con una página.

                    Respuesta: { items, total, page, size, summary }. `total` cuenta las filas que \
                    cumplen los filtros (alimenta la paginación); `summary` cuenta el catálogo \
                    COMPLETO sin filtros (alimenta las métricas de la cabecera, que siguen \
                    describiendo todo aunque haya un filtro puesto).

                    Cada fila incluye `featuredInHome`: si es una de las promociones que Inicio \
                    muestra ahora mismo. Es un dato derivado (las 3 activas más recientes) que solo \
                    el servidor puede calcular, porque el panel únicamente ve su página.

                    Un valor no reconocido en `status`, `source` o `featured` responde 400; vacío \
                    o ausente significa "sin filtrar".""")
    public Response getAll(
            @Parameter(description = "Página, base 0. Un valor negativo se trata como 0.")
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Filas por página. Se acota a 100 como máximo; `size` en la respuesta indica el valor realmente aplicado.")
            @QueryParam("size") @DefaultValue("15") int size,
            @Parameter(description = "Texto libre a buscar en título, destino y resumen, sin distinguir mayúsculas de minúsculas.")
            @QueryParam("search") String search,
            @Parameter(description = "VISIBLE (active=true) · OCULTA (active=false) · VENCIDA (validUntil < hoy) · vacío = todas")
            @QueryParam("status") String status,
            @Parameter(description = "MANUAL · FACEBOOK · vacío = todas")
            @QueryParam("source") String source,
            @Parameter(description = "SI (está en portada) · NO (no lo está) · vacío = todas")
            @QueryParam("featured") String featured) {
        PromotionsPageResponse pagina = listUseCase.execute(page, size, search, status, source, featured);
        return Response.ok(pagina).build();
    }

    @POST
    @Operation(summary = "Crear promoción",
            description = "Crea una promoción desde el formulario estructurado del admin y, en modo best-effort, "
                    + "publica el mismo contenido como post con foto en la Página de Facebook")
    public Response create(@Valid CreateOrUpdatePromotionRequest request) {
        PromotionDTO created = createUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar promoción",
            description = "Corrige el contenido de una promoción existente con el mismo formulario de creación. "
                    + "Conserva el slug (es la URL pública ya compartida), la visibilidad (active), el origen "
                    + "(source) y el post de Facebook asociado; no republica nada en Facebook. "
                    + "Responde 404 si el ID no existe.")
    public Response update(@PathParam("id") Long id, @Valid CreateOrUpdatePromotionRequest request) {
        PromotionDTO updated = updateUseCase.execute(id, request);
        return Response.ok(updated).build();
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
