package com.viajescarolina.api.publishing.infrastructure.web;

import com.viajescarolina.api.publishing.application.dto.PublishRequest;
import com.viajescarolina.api.publishing.application.dto.PublishResponse;
import com.viajescarolina.api.publishing.application.usecase.TriggerPublishUseCase;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.Instant;
import java.util.List;

@Path("/api/admin/v1/publishing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Admin Publishing & ISR", description = "Publicación On-Demand, revalidación de caché Next.js y Draft Previews")
public class AdminPublishingResource {

    private final TriggerPublishUseCase triggerPublishUseCase;

    public AdminPublishingResource(TriggerPublishUseCase triggerPublishUseCase) {
        this.triggerPublishUseCase = triggerPublishUseCase;
    }

    @POST
    @Path("/publish")
    @Operation(summary = "Disparar publicación y revalidación ISR On-Demand", description = "Invalida la caché de Next.js y actualiza la versión pública del sitio")
    public PublishResponse publish(PublishRequest req, @HeaderParam("X-Admin-User") String adminUsername) {
        return triggerPublishUseCase.execute(req, adminUsername);
    }

    @GET
    @Path("/status")
    @Operation(summary = "Consultar estado de publicación del sitio")
    public PublishResponse getStatus() {
        return new PublishResponse(
                "READY",
                List.of("all", "home", "promotions", "blog", "about", "contact", "reclamaciones"),
                Instant.now(),
                "SYSTEM",
                "Motor de publicación ISR On-Demand activo y en espera de eventos."
        );
    }
}
