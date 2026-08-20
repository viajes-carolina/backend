package com.viajescarolina.api.contact.infrastructure.web;

import com.viajescarolina.api.contact.application.dto.ContactInquiryDTO;
import com.viajescarolina.api.contact.application.dto.UpdateInquiryStatusRequest;
import com.viajescarolina.api.contact.application.usecase.ListAdminInquiriesUseCase;
import com.viajescarolina.api.contact.application.usecase.UpdateInquiryStatusUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/admin/v1/inquiries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Inquiries", description = "Endpoints administrativos para bandeja de leads y solicitudes de contacto")
public class AdminInquiryResource {

    private final ListAdminInquiriesUseCase listAdminInquiriesUseCase;
    private final UpdateInquiryStatusUseCase updateInquiryStatusUseCase;

    public AdminInquiryResource(ListAdminInquiriesUseCase listAdminInquiriesUseCase,
                                UpdateInquiryStatusUseCase updateInquiryStatusUseCase) {
        this.listAdminInquiriesUseCase = listAdminInquiriesUseCase;
        this.updateInquiryStatusUseCase = updateInquiryStatusUseCase;
    }

    @GET
    @Operation(summary = "Listar todas las solicitudes de contacto con filtro opcional de estado")
    public List<ContactInquiryDTO> listInquiries(@QueryParam("status") String status) {
        return listAdminInquiriesUseCase.execute(status);
    }

    @PATCH
    @Path("/{id}/status")
    @Operation(summary = "Actualizar estado de una solicitud de contacto (NEW, IN_PROGRESS, CONTACTED, ARCHIVED)")
    public ContactInquiryDTO updateStatus(@PathParam("id") Long id, @Valid UpdateInquiryStatusRequest request) {
        return updateInquiryStatusUseCase.execute(id, request.status());
    }
}
