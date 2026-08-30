package com.viajescarolina.api.legal.infrastructure.web;

import com.viajescarolina.api.legal.application.dto.LegalCookiesDTO;
import com.viajescarolina.api.legal.application.dto.LegalEsnnaDTO;
import com.viajescarolina.api.legal.application.dto.LegalMinceturDTO;
import com.viajescarolina.api.legal.application.dto.LegalPrivacyDTO;
import com.viajescarolina.api.legal.application.dto.LegalTermsDTO;
import com.viajescarolina.api.legal.application.dto.UpdateLegalCookiesRequest;
import com.viajescarolina.api.legal.application.dto.UpdateLegalEsnnaRequest;
import com.viajescarolina.api.legal.application.dto.UpdateLegalMinceturRequest;
import com.viajescarolina.api.legal.application.dto.UpdateLegalPrivacyRequest;
import com.viajescarolina.api.legal.application.dto.UpdateLegalTermsRequest;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalCookiesUseCase;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalEsnnaUseCase;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalMinceturUseCase;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalPrivacyUseCase;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalTermsUseCase;
import com.viajescarolina.api.legal.application.usecase.UpdateLegalCookiesUseCase;
import com.viajescarolina.api.legal.application.usecase.UpdateLegalEsnnaUseCase;
import com.viajescarolina.api.legal.application.usecase.UpdateLegalMinceturUseCase;
import com.viajescarolina.api.legal.application.usecase.UpdateLegalPrivacyUseCase;
import com.viajescarolina.api.legal.application.usecase.UpdateLegalTermsUseCase;
import com.viajescarolina.api.legal.domain.LegalCookiesRepository;
import com.viajescarolina.api.legal.domain.LegalEsnnaRepository;
import com.viajescarolina.api.legal.domain.LegalMinceturRepository;
import com.viajescarolina.api.legal.domain.LegalPrivacyRepository;
import com.viajescarolina.api.legal.domain.LegalTermsRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Endpoints administrativos para editar las 5 páginas legales/institucionales
 * del sitio: Términos y condiciones, Política de privacidad, Política de
 * cookies, Compromiso contra la ESNNA y Constancia MINCETUR.
 */
@Path("/api/admin/v1/legal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Legal", description = "Endpoints administrativos para gestionar páginas legales e institucionales")
public class AdminLegalResource {

    private final LegalTermsRepository legalTermsRepository;
    private final LegalPrivacyRepository legalPrivacyRepository;
    private final LegalCookiesRepository legalCookiesRepository;
    private final LegalEsnnaRepository legalEsnnaRepository;
    private final LegalMinceturRepository legalMinceturRepository;

    private final GetPublicLegalTermsUseCase getLegalTermsUseCase;
    private final GetPublicLegalPrivacyUseCase getLegalPrivacyUseCase;
    private final GetPublicLegalCookiesUseCase getLegalCookiesUseCase;
    private final GetPublicLegalEsnnaUseCase getLegalEsnnaUseCase;
    private final GetPublicLegalMinceturUseCase getLegalMinceturUseCase;

    private final UpdateLegalTermsUseCase updateLegalTermsUseCase;
    private final UpdateLegalPrivacyUseCase updateLegalPrivacyUseCase;
    private final UpdateLegalCookiesUseCase updateLegalCookiesUseCase;
    private final UpdateLegalEsnnaUseCase updateLegalEsnnaUseCase;
    private final UpdateLegalMinceturUseCase updateLegalMinceturUseCase;

    public AdminLegalResource(
            LegalTermsRepository legalTermsRepository,
            LegalPrivacyRepository legalPrivacyRepository,
            LegalCookiesRepository legalCookiesRepository,
            LegalEsnnaRepository legalEsnnaRepository,
            LegalMinceturRepository legalMinceturRepository,
            GetPublicLegalTermsUseCase getLegalTermsUseCase,
            GetPublicLegalPrivacyUseCase getLegalPrivacyUseCase,
            GetPublicLegalCookiesUseCase getLegalCookiesUseCase,
            GetPublicLegalEsnnaUseCase getLegalEsnnaUseCase,
            GetPublicLegalMinceturUseCase getLegalMinceturUseCase,
            UpdateLegalTermsUseCase updateLegalTermsUseCase,
            UpdateLegalPrivacyUseCase updateLegalPrivacyUseCase,
            UpdateLegalCookiesUseCase updateLegalCookiesUseCase,
            UpdateLegalEsnnaUseCase updateLegalEsnnaUseCase,
            UpdateLegalMinceturUseCase updateLegalMinceturUseCase) {
        this.legalTermsRepository = legalTermsRepository;
        this.legalPrivacyRepository = legalPrivacyRepository;
        this.legalCookiesRepository = legalCookiesRepository;
        this.legalEsnnaRepository = legalEsnnaRepository;
        this.legalMinceturRepository = legalMinceturRepository;
        this.getLegalTermsUseCase = getLegalTermsUseCase;
        this.getLegalPrivacyUseCase = getLegalPrivacyUseCase;
        this.getLegalCookiesUseCase = getLegalCookiesUseCase;
        this.getLegalEsnnaUseCase = getLegalEsnnaUseCase;
        this.getLegalMinceturUseCase = getLegalMinceturUseCase;
        this.updateLegalTermsUseCase = updateLegalTermsUseCase;
        this.updateLegalPrivacyUseCase = updateLegalPrivacyUseCase;
        this.updateLegalCookiesUseCase = updateLegalCookiesUseCase;
        this.updateLegalEsnnaUseCase = updateLegalEsnnaUseCase;
        this.updateLegalMinceturUseCase = updateLegalMinceturUseCase;
    }

    // ---- Términos y condiciones ----

    @GET
    @Path("/terminos")
    @Operation(summary = "Obtener Términos y condiciones para el panel de administración")
    public LegalTermsDTO getTerminos() {
        return legalTermsRepository.findSingleton()
            .map(getLegalTermsUseCase::toDTO)
            .orElseThrow(() -> new NotFoundException("Términos y condiciones no encontrados"));
    }

    @PUT
    @Path("/terminos")
    @Operation(summary = "Actualizar Términos y condiciones")
    public LegalTermsDTO updateTerminos(@Valid UpdateLegalTermsRequest request) {
        return updateLegalTermsUseCase.execute(request);
    }

    // ---- Política de privacidad ----

    @GET
    @Path("/privacidad")
    @Operation(summary = "Obtener Política de privacidad para el panel de administración")
    public LegalPrivacyDTO getPrivacidad() {
        return legalPrivacyRepository.findSingleton()
            .map(getLegalPrivacyUseCase::toDTO)
            .orElseThrow(() -> new NotFoundException("Política de privacidad no encontrada"));
    }

    @PUT
    @Path("/privacidad")
    @Operation(summary = "Actualizar Política de privacidad")
    public LegalPrivacyDTO updatePrivacidad(@Valid UpdateLegalPrivacyRequest request) {
        return updateLegalPrivacyUseCase.execute(request);
    }

    // ---- Política de cookies ----

    @GET
    @Path("/cookies")
    @Operation(summary = "Obtener Política de cookies para el panel de administración")
    public LegalCookiesDTO getCookies() {
        return legalCookiesRepository.findSingleton()
            .map(getLegalCookiesUseCase::toDTO)
            .orElseThrow(() -> new NotFoundException("Política de cookies no encontrada"));
    }

    @PUT
    @Path("/cookies")
    @Operation(summary = "Actualizar Política de cookies")
    public LegalCookiesDTO updateCookies(@Valid UpdateLegalCookiesRequest request) {
        return updateLegalCookiesUseCase.execute(request);
    }

    // ---- Compromiso contra la ESNNA ----

    @GET
    @Path("/esnna")
    @Operation(summary = "Obtener Compromiso contra la ESNNA para el panel de administración")
    public LegalEsnnaDTO getEsnna() {
        return legalEsnnaRepository.findSingleton()
            .map(getLegalEsnnaUseCase::toDTO)
            .orElseThrow(() -> new NotFoundException("Compromiso contra la ESNNA no encontrado"));
    }

    @PUT
    @Path("/esnna")
    @Operation(summary = "Actualizar Compromiso contra la ESNNA")
    public LegalEsnnaDTO updateEsnna(@Valid UpdateLegalEsnnaRequest request) {
        return updateLegalEsnnaUseCase.execute(request);
    }

    // ---- Constancia MINCETUR ----

    @GET
    @Path("/mincetur")
    @Operation(summary = "Obtener Constancia MINCETUR para el panel de administración")
    public LegalMinceturDTO getMincetur() {
        return legalMinceturRepository.findSingleton()
            .map(getLegalMinceturUseCase::toDTO)
            .orElseThrow(() -> new NotFoundException("Constancia MINCETUR no encontrada"));
    }

    @PUT
    @Path("/mincetur")
    @Operation(summary = "Actualizar Constancia MINCETUR")
    public LegalMinceturDTO updateMincetur(@Valid UpdateLegalMinceturRequest request) {
        return updateLegalMinceturUseCase.execute(request);
    }
}
