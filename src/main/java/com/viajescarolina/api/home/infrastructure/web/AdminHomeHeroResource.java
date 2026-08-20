package com.viajescarolina.api.home.infrastructure.web;

import com.viajescarolina.api.home.application.dto.HomeHeroDTO;
import com.viajescarolina.api.home.application.dto.UpdateHomeHeroRequest;
import com.viajescarolina.api.home.application.usecase.GetPublicHomeHeroUseCase;
import com.viajescarolina.api.home.application.usecase.UpdateHomeHeroUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/admin/v1/home/hero")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Home Hero", description = "Gestión centralizada de la sección Hero de la página de inicio")
public class AdminHomeHeroResource {

    private final GetPublicHomeHeroUseCase getUseCase;
    private final UpdateHomeHeroUseCase updateUseCase;

    @Inject
    public AdminHomeHeroResource(GetPublicHomeHeroUseCase getUseCase, UpdateHomeHeroUseCase updateUseCase) {
        this.getUseCase = getUseCase;
        this.updateUseCase = updateUseCase;
    }

    @GET
    @Operation(summary = "Obtener configuración de Home Hero", description = "Retorna los textos, CTAs, indicadores de confianza y medios asociados")
    public Response getHero() {
        HomeHeroDTO dto = getUseCase.execute();
        return Response.ok(dto).build();
    }

    @PUT
    @Operation(summary = "Actualizar sección Home Hero", description = "Actualiza los textos, insignia, llamada a la acción y enlaces a medios")
    public Response updateHero(@Valid UpdateHomeHeroRequest request) {
        HomeHeroDTO updated = updateUseCase.execute(request);
        return Response.ok(updated).build();
    }
}
