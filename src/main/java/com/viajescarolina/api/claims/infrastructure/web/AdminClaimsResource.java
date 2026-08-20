package com.viajescarolina.api.claims.infrastructure.web;

import com.viajescarolina.api.claims.application.dto.ClaimRecordDTO;
import com.viajescarolina.api.claims.application.dto.UpdateClaimStatusRequest;
import com.viajescarolina.api.claims.application.usecase.ListAdminClaimsUseCase;
import com.viajescarolina.api.claims.application.usecase.UpdateClaimStatusUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/admin/v1/claims")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Claims", description = "Gestión administrativa del Libro de Reclamaciones")
public class AdminClaimsResource {

    private final ListAdminClaimsUseCase listAdminClaimsUseCase;
    private final UpdateClaimStatusUseCase updateClaimStatusUseCase;

    @Inject
    public AdminClaimsResource(ListAdminClaimsUseCase listAdminClaimsUseCase, UpdateClaimStatusUseCase updateClaimStatusUseCase) {
        this.listAdminClaimsUseCase = listAdminClaimsUseCase;
        this.updateClaimStatusUseCase = updateClaimStatusUseCase;
    }

    @GET
    @Operation(summary = "Listar Hojas de Reclamación", description = "Lista todas las hojas de reclamación registradas con filtro opcional por estado")
    public List<ClaimRecordDTO> listClaims(@QueryParam("status") String status) {
        return listAdminClaimsUseCase.execute(status);
    }

    @PATCH
    @Path("/{id}/status")
    @Operation(summary = "Actualizar Estado y Respuesta de Reclamo", description = "Actualiza el estado de atención y añade notas de respuesta formal a la hoja de reclamación")
    public Response updateStatus(@PathParam("id") Long id, @Valid UpdateClaimStatusRequest request) {
        return updateClaimStatusUseCase.execute(id, request)
                .map(claim -> Response.ok(claim).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Reclamo no encontrado con ID: " + id))
                        .build());
    }

    public record ErrorResponse(String message) {}
}
