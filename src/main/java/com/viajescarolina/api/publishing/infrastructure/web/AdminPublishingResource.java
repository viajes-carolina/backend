package com.viajescarolina.api.publishing.infrastructure.web;

import com.viajescarolina.api.publishing.application.dto.PublishRequest;
import com.viajescarolina.api.publishing.application.dto.PublishResponse;
import com.viajescarolina.api.publishing.application.usecase.GetPublishingStatusUseCase;
import com.viajescarolina.api.publishing.application.usecase.TriggerPublishUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/admin/v1/publishing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Publishing & ISR", description = "Publicación On-Demand, revalidación de caché Next.js y Draft Previews")
public class AdminPublishingResource {

    private final TriggerPublishUseCase triggerPublishUseCase;
    private final GetPublishingStatusUseCase getPublishingStatusUseCase;

    @Inject
    JsonWebToken jwt;

    public AdminPublishingResource(TriggerPublishUseCase triggerPublishUseCase,
                                   GetPublishingStatusUseCase getPublishingStatusUseCase) {
        this.triggerPublishUseCase = triggerPublishUseCase;
        this.getPublishingStatusUseCase = getPublishingStatusUseCase;
    }

    @POST
    @Path("/publish")
    @Operation(summary = "Disparar publicación y revalidación ISR On-Demand", description = "Invalida la caché de Next.js y actualiza la versión pública del sitio")
    public PublishResponse publish(PublishRequest req) {
        // El actor se deriva del JWT validado, nunca de un header enviado por el cliente (ver SEC-013).
        String adminUsername = jwt.getClaim("upn");
        return triggerPublishUseCase.execute(req, adminUsername);
    }

    @GET
    @Path("/status")
    @Operation(
            summary = "Consultar estado de la última publicación del sitio",
            description = """
                    Describe la ÚLTIMA publicación realmente ejecutada, leída del registro de auditoría \
                    que escribe /publish (entityType PUBLISHING). No refleja la hora de la consulta.

                    Valores de `status`:
                    - `SUCCESS`: la última publicación se completó y el webhook de revalidación ISR respondió 2xx.
                    - `FAILED`: la última publicación quedó registrada pero su webhook falló; el sitio público puede estar desactualizado.
                    - `NEVER_PUBLISHED`: no hay ninguna publicación registrada. `publishedAt` y `triggeredBy` van en null \
                    (con `quarkus.jackson.serialization-inclusion=non-null` ambos campos se omiten del JSON) y \
                    `revalidatedTags` viene vacío: no se inventa ninguna fecha.
                    - `UNKNOWN`: existe registro de publicación pero su detalle de auditoría no es legible, \
                    así que no se afirma ni éxito ni fallo.

                    `publishedAt`, `triggeredBy` y `revalidatedTags` corresponden a esa última publicación registrada.\
                    """)
    public PublishResponse getStatus() {
        return getPublishingStatusUseCase.execute();
    }
}
