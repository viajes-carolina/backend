package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomePromotionsSectionDTO;
import com.viajescarolina.api.home.application.usecase.GetPublicHomePromotionsSectionUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/home/promotions-section")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Public Home", description = "Endpoints públicos de portada y contenidos de inicio")
public class PublicHomePromotionsSectionResource {

    @Inject
    GetPublicHomePromotionsSectionUseCase getPublicHomePromotionsSectionUseCase;

    @GET
    @Operation(summary = "Obtener sección de promociones para portada", description = "Retorna configuración editorial del encabezado y CTA de WhatsApp de la sección de promociones en home")
    public Response getPromotionsSection() {
        HomePromotionsSectionDTO dto = getPublicHomePromotionsSectionUseCase.execute();
        return Response.ok(dto).build();
    }
}
