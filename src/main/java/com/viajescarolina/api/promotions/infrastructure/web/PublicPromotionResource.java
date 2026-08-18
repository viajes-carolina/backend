package com.viajescarolina.api.promotions.infrastructure.web;

import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.application.usecase.GetPromotionBySlugUseCase;
import com.viajescarolina.api.promotions.application.usecase.ListFeaturedPromotionsUseCase;
import com.viajescarolina.api.promotions.application.usecase.ListPublicPromotionsUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
    private final ListPublicPromotionsUseCase listUseCase;
    private final GetPromotionBySlugUseCase slugUseCase;

    @Inject
    public PublicPromotionResource(
            ListFeaturedPromotionsUseCase featuredUseCase,
            ListPublicPromotionsUseCase listUseCase,
            GetPromotionBySlugUseCase slugUseCase) {
        this.featuredUseCase = featuredUseCase;
        this.listUseCase = listUseCase;
        this.slugUseCase = slugUseCase;
    }

    @GET
    @Path("/featured")
    @Operation(summary = "Obtener promociones destacadas", description = "Retorna promociones marcadas como destacadas para la portada")
    public Response getFeatured() {
        List<PromotionDTO> list = featuredUseCase.execute();
        return Response.ok(list)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=60, s-maxage=300")
                .build();
    }

    @GET
    @Operation(summary = "Listar todas las promociones activas", description = "Retorna catálogo completo de promociones")
    public Response getAll() {
        List<PromotionDTO> list = listUseCase.execute();
        return Response.ok(list)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=60, s-maxage=300")
                .build();
    }

    @GET
    @Path("/{slug}")
    @Operation(summary = "Obtener promoción por slug", description = "Retorna detalles completos de una promoción específica")
    public Response getBySlug(@PathParam("slug") String slug) {
        PromotionDTO dto = slugUseCase.execute(slug);
        return Response.ok(dto)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=60, s-maxage=300")
                .build();
    }
}
