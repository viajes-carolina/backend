package com.viajescarolina.api.search.infrastructure.adapter.in.rest;

import com.viajescarolina.api.search.application.dto.GlobalSearchResponseDto;
import com.viajescarolina.api.search.application.usecase.PerformGlobalSearchUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Public Search", description = "Motor de búsqueda global y fuzzy search con trigramas")
public class PublicSearchResource {

    private final PerformGlobalSearchUseCase searchUseCase;

    @Inject
    public PublicSearchResource(PerformGlobalSearchUseCase searchUseCase) {
        this.searchUseCase = searchUseCase;
    }

    @GET
    @Operation(summary = "Realizar búsqueda global unificada en promociones, blog, intenciones y destinos")
    public Response search(
            @QueryParam("q") @DefaultValue("") String query,
            @QueryParam("type") @DefaultValue("ALL") String type,
            @QueryParam("limit") @DefaultValue("20") Integer limit
    ) {
        GlobalSearchResponseDto response = searchUseCase.execute(query, type, limit);
        return Response.ok(response).build();
    }
}
