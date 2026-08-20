package com.viajescarolina.api.trust.infrastructure.web;

import com.viajescarolina.api.trust.application.dto.CreateOrUpdateTestimonialRequest;
import com.viajescarolina.api.trust.application.dto.TestimonialDTO;
import com.viajescarolina.api.trust.application.usecase.CreateTestimonialUseCase;
import com.viajescarolina.api.trust.application.usecase.DeleteTestimonialUseCase;
import com.viajescarolina.api.trust.application.usecase.ListAdminTestimonialsUseCase;
import com.viajescarolina.api.trust.application.usecase.UpdateTestimonialUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/admin/v1/testimonials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Testimonials", description = "Gestión administrativa de testimonios y opiniones de viajeros")
public class AdminTestimonialResource {

    private final ListAdminTestimonialsUseCase listUseCase;
    private final CreateTestimonialUseCase createUseCase;
    private final UpdateTestimonialUseCase updateUseCase;
    private final DeleteTestimonialUseCase deleteUseCase;

    @Inject
    public AdminTestimonialResource(
            ListAdminTestimonialsUseCase listUseCase,
            CreateTestimonialUseCase createUseCase,
            UpdateTestimonialUseCase updateUseCase,
            DeleteTestimonialUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @GET
    @Operation(summary = "Listar todos los testimonios", description = "Retorna lista de testimonios para administración")
    public Response getAll() {
        List<TestimonialDTO> list = listUseCase.execute();
        return Response.ok(list).build();
    }

    @POST
    @Operation(summary = "Crear nuevo testimonio", description = "Registra un testimonio con consentimiento de cliente")
    public Response create(@Valid CreateOrUpdateTestimonialRequest request) {
        TestimonialDTO created = createUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar testimonio", description = "Actualiza datos, calificación o visibilidad de testimonio")
    public Response update(@PathParam("id") Long id, @Valid CreateOrUpdateTestimonialRequest request) {
        TestimonialDTO updated = updateUseCase.execute(id, request);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Desactivar testimonio", description = "Realiza soft-delete de testimonio")
    public Response delete(@PathParam("id") Long id) {
        deleteUseCase.execute(id);
        return Response.noContent().build();
    }
}
