package com.viajescarolina.api.intentions.infrastructure.web;

import com.viajescarolina.api.intentions.application.dto.TravelIntentionDTO;
import com.viajescarolina.api.intentions.application.usecase.ListPublicTravelIntentionsUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/public/v1/home/intentions")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Public Travel Intentions", description = "Endpoint público de intenciones de viaje para la portada web")
public class PublicTravelIntentionResource {

    private final ListPublicTravelIntentionsUseCase listUseCase;

    @Inject
    public PublicTravelIntentionResource(ListPublicTravelIntentionsUseCase listUseCase) {
        this.listUseCase = listUseCase;
    }

    @GET
    @Operation(summary = "Listar intenciones de viaje activas", description = "Retorna lista de intenciones de viaje activas para selector en portada")
    public Response getActiveIntentions() {
        List<TravelIntentionDTO> list = listUseCase.execute();
        return Response.ok(list)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=60, s-maxage=300")
                .build();
    }
}
