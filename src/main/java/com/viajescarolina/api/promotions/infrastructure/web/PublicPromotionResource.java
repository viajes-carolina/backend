package com.viajescarolina.api.promotions.infrastructure.web;

import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.application.usecase.ListFeaturedPromotionsUseCase;
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

@Path("/api/public/v1/promotions")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Public Promotions", description = "Endpoints públicos de paquetes turísticos y promociones")
public class PublicPromotionResource {

    private final ListFeaturedPromotionsUseCase featuredUseCase;

    @Inject
    public PublicPromotionResource(ListFeaturedPromotionsUseCase featuredUseCase) {
        this.featuredUseCase = featuredUseCase;
    }

    @GET
    @Path("/featured")
    @Operation(summary = "Obtener promociones destacadas", description = "Retorna las 3 promociones activas más recientes para la portada")
    public Response getFeatured() {
        List<PromotionDTO> list = featuredUseCase.execute();
        return Response.ok(list)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=60, s-maxage=300")
                .build();
    }
}
