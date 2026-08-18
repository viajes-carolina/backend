package com.viajescarolina.api.settings.infrastructure.web;

import com.viajescarolina.api.settings.application.dto.SiteSettingsDTO;
import com.viajescarolina.api.settings.application.dto.UpdateSiteSettingsRequest;
import com.viajescarolina.api.settings.application.dto.UpdateWhatsAppChannelRequest;
import com.viajescarolina.api.settings.application.dto.WhatsAppChannelDTO;
import com.viajescarolina.api.settings.application.usecase.GetWhatsAppChannelUseCase;
import com.viajescarolina.api.settings.application.usecase.UpdateSiteSettingsUseCase;
import com.viajescarolina.api.settings.application.usecase.UpdateWhatsAppChannelUseCase;
import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
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

@Path("/api/admin/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Admin Settings", description = "Endpoints administrativos para configuración del sitio y canal WhatsApp")
public class AdminSettingsResource {

    private final SettingsRepository settingsRepository;
    private final UpdateSiteSettingsUseCase updateSiteSettingsUseCase;
    private final GetWhatsAppChannelUseCase getWhatsAppChannelUseCase;
    private final UpdateWhatsAppChannelUseCase updateWhatsAppChannelUseCase;

    @Inject
    public AdminSettingsResource(
            SettingsRepository settingsRepository,
            UpdateSiteSettingsUseCase updateSiteSettingsUseCase,
            GetWhatsAppChannelUseCase getWhatsAppChannelUseCase,
            UpdateWhatsAppChannelUseCase updateWhatsAppChannelUseCase) {
        this.settingsRepository = settingsRepository;
        this.updateSiteSettingsUseCase = updateSiteSettingsUseCase;
        this.getWhatsAppChannelUseCase = getWhatsAppChannelUseCase;
        this.updateWhatsAppChannelUseCase = updateWhatsAppChannelUseCase;
    }

    @GET
    @Path("/settings")
    @Operation(summary = "Obtener configuración completa del sitio", description = "Retorna todos los campos de configuración para el panel admin")
    public Response getSettings() {
        SiteSettings settings = settingsRepository.findSiteSettings()
                .orElseGet(() -> new SiteSettings(1, "Viajes Carolina", "El viaje comienza aquí", "contacto@viajescarolina.com", "+51 987 654 321", null, null, null, null, null, 1, null, null));

        SiteSettingsDTO dto = new SiteSettingsDTO(
                settings.getId(),
                settings.getSiteName(),
                settings.getBrandTagline(),
                settings.getContactEmail(),
                settings.getPrimaryPhone(),
                settings.getLogoMediaId(),
                settings.getFaviconMediaId(),
                settings.getFacebookUrl(),
                settings.getInstagramUrl(),
                settings.getTiktokUrl(),
                settings.getRevision(),
                settings.getUpdatedAt()
        );

        return Response.ok(dto).build();
    }

    @PUT
    @Path("/settings")
    @Operation(summary = "Actualizar configuración del sitio", description = "Actualiza los datos de identidad y contacto del sitio")
    public Response updateSettings(@Valid UpdateSiteSettingsRequest request) {
        SiteSettingsDTO updated = updateSiteSettingsUseCase.execute(request);
        return Response.ok(updated).build();
    }

    @GET
    @Path("/whatsapp")
    @Operation(summary = "Obtener canal WhatsApp", description = "Retorna el número de WhatsApp actual y su estado")
    public Response getWhatsAppChannel() {
        WhatsAppChannelDTO dto = getWhatsAppChannelUseCase.execute();
        return Response.ok(dto).build();
    }

    @PUT
    @Path("/whatsapp")
    @Operation(summary = "Actualizar canal WhatsApp", description = "Actualiza el número de WhatsApp centralizado")
    public Response updateWhatsAppChannel(@Valid UpdateWhatsAppChannelRequest request) {
        WhatsAppChannelDTO updated = updateWhatsAppChannelUseCase.execute(request);
        return Response.ok(updated).build();
    }
}
