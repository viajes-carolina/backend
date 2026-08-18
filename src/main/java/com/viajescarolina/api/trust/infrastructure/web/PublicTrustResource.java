package com.viajescarolina.api.trust.infrastructure.web;

import com.viajescarolina.api.trust.application.dto.PublicTrustResponse;
import com.viajescarolina.api.trust.application.usecase.GetPublicTrustUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/home/trust")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Public Trust", description = "Endpoints públicos de testimonios y preguntas frecuentes (FAQ)")
public class PublicTrustResource {

    private final GetPublicTrustUseCase trustUseCase;

    @Inject
    public PublicTrustResource(GetPublicTrustUseCase trustUseCase) {
        this.trustUseCase = trustUseCase;
    }

    @GET
    @Operation(summary = "Obtener testimonios y FAQ", description = "Retorna testimonios de clientes verificados y preguntas frecuentes de portada")
    public Response getTrustData() {
        PublicTrustResponse response = trustUseCase.execute();
        return Response.ok(response)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=60, s-maxage=300")
                .build();
    }
}
