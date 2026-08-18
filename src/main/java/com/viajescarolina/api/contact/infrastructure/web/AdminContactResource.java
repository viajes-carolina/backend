package com.viajescarolina.api.contact.infrastructure.web;

import com.viajescarolina.api.contact.application.dto.ContactPageDTO;
import com.viajescarolina.api.contact.application.dto.UpdateContactPageRequest;
import com.viajescarolina.api.contact.application.usecase.GetPublicContactUseCase;
import com.viajescarolina.api.contact.application.usecase.UpdateContactPageUseCase;
import com.viajescarolina.api.contact.domain.ContactPageRepository;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/admin/v1/contact")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Admin Contact", description = "Endpoints administrativos para gestionar la página de Contacto")
public class AdminContactResource {

    private final ContactPageRepository contactPageRepository;
    private final GetPublicContactUseCase getPublicContactUseCase;
    private final UpdateContactPageUseCase updateContactPageUseCase;

    public AdminContactResource(ContactPageRepository contactPageRepository,
                                GetPublicContactUseCase getPublicContactUseCase,
                                UpdateContactPageUseCase updateContactPageUseCase) {
        this.contactPageRepository = contactPageRepository;
        this.getPublicContactUseCase = getPublicContactUseCase;
        this.updateContactPageUseCase = updateContactPageUseCase;
    }

    @GET
    @Operation(summary = "Obtener configuración de la página de Contacto para el backoffice")
    public ContactPageDTO getAdminContact() {
        return contactPageRepository.findSingleton()
            .map(getPublicContactUseCase::toPageDTO)
            .orElseThrow(() -> new NotFoundException("Configuración de Contacto no encontrada"));
    }

    @PUT
    @Operation(summary = "Actualizar configuración de la página de Contacto")
    public ContactPageDTO updateAdminContact(@Valid UpdateContactPageRequest request) {
        return updateContactPageUseCase.execute(request);
    }
}
