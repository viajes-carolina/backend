package com.viajescarolina.api.claims.infrastructure.web;

import com.viajescarolina.api.claims.application.dto.ClaimRecordDTO;
import com.viajescarolina.api.claims.application.dto.SubmitClaimRequest;
import com.viajescarolina.api.claims.application.usecase.GetClaimByCodeUseCase;
import com.viajescarolina.api.claims.application.usecase.SubmitClaimUseCase;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/claims")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Libro de Reclamaciones", description = "Endpoints públicos para registro y consulta de reclamos")
public class PublicClaimsResource {

    private final SubmitClaimUseCase submitClaimUseCase;
    private final GetClaimByCodeUseCase getClaimByCodeUseCase;

    @Inject
    public PublicClaimsResource(SubmitClaimUseCase submitClaimUseCase, GetClaimByCodeUseCase getClaimByCodeUseCase) {
        this.submitClaimUseCase = submitClaimUseCase;
        this.getClaimByCodeUseCase = getClaimByCodeUseCase;
    }

    @POST
    @Operation(summary = "Registrar Hoja de Reclamación", description = "Registra un nuevo reclamo o queja en el Libro de Reclamaciones virtual y genera un código correlativo oficial")
    public Response submitClaim(@Valid SubmitClaimRequest request, @Context HttpServerRequest httpRequest) {
        String clientIp = httpRequest != null && httpRequest.remoteAddress() != null
                ? httpRequest.remoteAddress().host()
                : "127.0.0.1";

        try {
            ClaimRecordDTO created = submitClaimUseCase.execute(request, clientIp);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{claimCode}")
    @Operation(summary = "Consultar Estado de Reclamo por Código", description = "Obtiene el detalle y estado de una hoja de reclamación mediante su código único REC-YYYY-XXXX")
    public Response getClaimByCode(@PathParam("claimCode") String claimCode) {
        return getClaimByCodeUseCase.execute(claimCode)
                .map(claim -> Response.ok(claim).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Hoja de reclamación no encontrada con el código especificado: " + claimCode))
                        .build());
    }

    public record ErrorResponse(String message) {}
}
