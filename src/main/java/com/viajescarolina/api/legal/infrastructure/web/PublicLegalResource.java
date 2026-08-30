package com.viajescarolina.api.legal.infrastructure.web;

import com.viajescarolina.api.legal.application.dto.LegalCookiesDTO;
import com.viajescarolina.api.legal.application.dto.LegalEsnnaDTO;
import com.viajescarolina.api.legal.application.dto.LegalMinceturDTO;
import com.viajescarolina.api.legal.application.dto.LegalPrivacyDTO;
import com.viajescarolina.api.legal.application.dto.LegalTermsDTO;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalCookiesUseCase;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalEsnnaUseCase;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalMinceturUseCase;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalPrivacyUseCase;
import com.viajescarolina.api.legal.application.usecase.GetPublicLegalTermsUseCase;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Endpoints públicos de las 5 páginas legales/institucionales del sitio:
 * Términos y condiciones, Política de privacidad, Política de cookies,
 * Compromiso contra la ESNNA y Constancia MINCETUR.
 */
@Path("/api/public/v1/legal")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Public Legal", description = "Endpoints públicos de páginas legales e institucionales")
public class PublicLegalResource {

    private final GetPublicLegalTermsUseCase getLegalTermsUseCase;
    private final GetPublicLegalPrivacyUseCase getLegalPrivacyUseCase;
    private final GetPublicLegalCookiesUseCase getLegalCookiesUseCase;
    private final GetPublicLegalEsnnaUseCase getLegalEsnnaUseCase;
    private final GetPublicLegalMinceturUseCase getLegalMinceturUseCase;

    public PublicLegalResource(
            GetPublicLegalTermsUseCase getLegalTermsUseCase,
            GetPublicLegalPrivacyUseCase getLegalPrivacyUseCase,
            GetPublicLegalCookiesUseCase getLegalCookiesUseCase,
            GetPublicLegalEsnnaUseCase getLegalEsnnaUseCase,
            GetPublicLegalMinceturUseCase getLegalMinceturUseCase) {
        this.getLegalTermsUseCase = getLegalTermsUseCase;
        this.getLegalPrivacyUseCase = getLegalPrivacyUseCase;
        this.getLegalCookiesUseCase = getLegalCookiesUseCase;
        this.getLegalEsnnaUseCase = getLegalEsnnaUseCase;
        this.getLegalMinceturUseCase = getLegalMinceturUseCase;
    }

    @GET
    @Path("/terminos")
    @Operation(summary = "Obtener contenido público de Términos y condiciones")
    public LegalTermsDTO getTerminos() {
        return getLegalTermsUseCase.execute();
    }

    @GET
    @Path("/privacidad")
    @Operation(summary = "Obtener contenido público de Política de privacidad")
    public LegalPrivacyDTO getPrivacidad() {
        return getLegalPrivacyUseCase.execute();
    }

    @GET
    @Path("/cookies")
    @Operation(summary = "Obtener contenido público de Política de cookies")
    public LegalCookiesDTO getCookies() {
        return getLegalCookiesUseCase.execute();
    }

    @GET
    @Path("/esnna")
    @Operation(summary = "Obtener contenido público de Compromiso contra la ESNNA")
    public LegalEsnnaDTO getEsnna() {
        return getLegalEsnnaUseCase.execute();
    }

    @GET
    @Path("/mincetur")
    @Operation(summary = "Obtener contenido público de Constancia MINCETUR")
    public LegalMinceturDTO getMincetur() {
        return getLegalMinceturUseCase.execute();
    }
}
