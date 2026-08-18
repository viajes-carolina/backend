package com.viajescarolina.api.contact.infrastructure.web;

import com.viajescarolina.api.contact.application.dto.ContactInquiryDTO;
import com.viajescarolina.api.contact.application.dto.PublicContactResponse;
import com.viajescarolina.api.contact.application.dto.SubmitContactInquiryRequest;
import com.viajescarolina.api.contact.application.usecase.GetPublicContactUseCase;
import com.viajescarolina.api.contact.application.usecase.SubmitContactInquiryUseCase;
import io.vertx.core.http.HttpServerRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/public/v1/contact")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Public Contact", description = "Endpoints públicos de información de contacto y envío de solicitudes")
public class PublicContactResource {

    private final GetPublicContactUseCase getPublicContactUseCase;
    private final SubmitContactInquiryUseCase submitContactInquiryUseCase;

    public PublicContactResource(GetPublicContactUseCase getPublicContactUseCase,
                                SubmitContactInquiryUseCase submitContactInquiryUseCase) {
        this.getPublicContactUseCase = getPublicContactUseCase;
        this.submitContactInquiryUseCase = submitContactInquiryUseCase;
    }

    @GET
    @Operation(summary = "Obtener configuración y canales públicos de contacto")
    public PublicContactResponse getPublicContact() {
        return getPublicContactUseCase.execute();
    }

    @POST
    @Path("/inquiry")
    @Operation(summary = "Enviar solicitud de cotización o contacto con validación anti-bot")
    public Response submitInquiry(@Valid SubmitContactInquiryRequest request, @Context HttpServerRequest httpRequest) {
        String remoteIp = httpRequest.remoteAddress() != null ? httpRequest.remoteAddress().host() : null;
        ContactInquiryDTO result = submitContactInquiryUseCase.execute(request, remoteIp);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }
}
