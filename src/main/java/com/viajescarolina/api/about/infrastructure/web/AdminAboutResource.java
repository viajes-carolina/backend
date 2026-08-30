package com.viajescarolina.api.about.infrastructure.web;

import com.viajescarolina.api.about.application.dto.AboutPageDTO;
import com.viajescarolina.api.about.application.dto.UpdateAboutPageRequest;
import com.viajescarolina.api.about.application.usecase.GetPublicAboutUseCase;
import com.viajescarolina.api.about.application.usecase.UpdateAboutPageUseCase;
import com.viajescarolina.api.about.domain.AboutPageRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/admin/v1/about")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin About", description = "Endpoints administrativos para gestionar la información de Nosotros")
public class AdminAboutResource {

    private final AboutPageRepository aboutPageRepository;
    private final GetPublicAboutUseCase getPublicAboutUseCase;
    private final UpdateAboutPageUseCase updateAboutPageUseCase;

    public AdminAboutResource(AboutPageRepository aboutPageRepository,
                              GetPublicAboutUseCase getPublicAboutUseCase,
                              UpdateAboutPageUseCase updateAboutPageUseCase) {
        this.aboutPageRepository = aboutPageRepository;
        this.getPublicAboutUseCase = getPublicAboutUseCase;
        this.updateAboutPageUseCase = updateAboutPageUseCase;
    }

    @GET
    @Operation(summary = "Obtener configuración de la página Nosotros para el panel de administración")
    public AboutPageDTO getAdminAbout() {
        return aboutPageRepository.findSingleton()
            .map(getPublicAboutUseCase::toPageDTO)
            .orElseThrow(() -> new NotFoundException("Configuración de Nosotros no encontrada"));
    }

    @PUT
    @Operation(summary = "Actualizar configuración de Nosotros (Hero, Nuestra forma de trabajar, Quién está detrás)")
    public AboutPageDTO updateAdminAbout(@Valid UpdateAboutPageRequest request) {
        return updateAboutPageUseCase.execute(request);
    }
}
