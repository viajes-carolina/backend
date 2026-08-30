package com.viajescarolina.api.settings.infrastructure.web;

import com.viajescarolina.api.settings.application.dto.SiteSettingsDTO;
import com.viajescarolina.api.settings.application.dto.UpdateSiteSettingsRequest;
import com.viajescarolina.api.settings.application.usecase.UpdateSiteSettingsUseCase;
import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import jakarta.annotation.security.PermitAll;
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

@Path("/api/admin/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Settings", description = "Endpoints administrativos para configuración del sitio y canal WhatsApp")
public class AdminSettingsResource {

    private final SettingsRepository settingsRepository;
    private final WhatsAppRepository whatsAppRepository;
    private final UpdateSiteSettingsUseCase updateSiteSettingsUseCase;

    @Inject
    public AdminSettingsResource(
            SettingsRepository settingsRepository,
            WhatsAppRepository whatsAppRepository,
            UpdateSiteSettingsUseCase updateSiteSettingsUseCase) {
        this.settingsRepository = settingsRepository;
        this.whatsAppRepository = whatsAppRepository;
        this.updateSiteSettingsUseCase = updateSiteSettingsUseCase;
    }

    @GET
    @Path("/settings")
    @PermitAll
    @Operation(summary = "Obtener configuración completa del sitio", description = "Retorna todos los campos de configuración; lectura pública porque la web pública consume este mismo endpoint (identidad, contacto, redes, RUC/razón social, WhatsApp)")
    public Response getSettings() {
        SiteSettings settings = settingsRepository.findSiteSettings()
                .orElseGet(() -> new SiteSettings(1, "Viajes Carolina", "El viaje comienza aquí", "contacto@viajescarolina.com", null, null, null, null, null, "VIAJES CAROLINA S.A.C.", "20601234567", null, null, "Lima, Perú", 1, null, null));

        WhatsAppChannel channel = whatsAppRepository.findChannel()
                .orElseGet(() -> new WhatsAppChannel(null, "Línea Principal", "+51987654321", "+51 987 654 321",
                        "Hola Viajes Carolina, deseo asesoría personalizada para mi próximo viaje.", true, true, 1, null, null));

        SiteSettingsDTO dto = new SiteSettingsDTO(
                settings.getId(),
                settings.getSiteName(),
                settings.getBrandTagline(),
                settings.getContactEmail(),
                settings.getLogoMediaId(),
                settings.getFaviconMediaId(),
                settings.getFacebookUrl(),
                settings.getInstagramUrl(),
                settings.getTiktokUrl(),
                settings.getLegalCompanyName(),
                settings.getTaxId(),
                settings.getMinceturCertificateUrl(),
                settings.getMinceturRegistrationNumber(),
                settings.getMinceturLocation(),
                channel.getDisplayNumber(),
                channel.getE164Number(),
                channel.getDisplayNumber(),
                channel.getDefaultMessage(),
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
}
