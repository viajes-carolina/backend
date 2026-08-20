package com.viajescarolina.api.about.infrastructure.web;

import com.viajescarolina.api.about.application.dto.CreateOrUpdateAdvisorRequest;
import com.viajescarolina.api.about.application.dto.TravelAdvisorDTO;
import com.viajescarolina.api.about.application.usecase.CreateAdvisorUseCase;
import com.viajescarolina.api.about.application.usecase.DeleteAdvisorUseCase;
import com.viajescarolina.api.about.application.usecase.ListAdminAdvisorsUseCase;
import com.viajescarolina.api.about.application.usecase.UpdateAdvisorUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/admin/v1/advisors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Advisors", description = "Endpoints administrativos para gestionar asesoras de viaje")
public class AdminAdvisorResource {

    private final ListAdminAdvisorsUseCase listAdminAdvisorsUseCase;
    private final CreateAdvisorUseCase createAdvisorUseCase;
    private final UpdateAdvisorUseCase updateAdvisorUseCase;
    private final DeleteAdvisorUseCase deleteAdvisorUseCase;

    public AdminAdvisorResource(ListAdminAdvisorsUseCase listAdminAdvisorsUseCase,
                                CreateAdvisorUseCase createAdvisorUseCase,
                                UpdateAdvisorUseCase updateAdvisorUseCase,
                                DeleteAdvisorUseCase deleteAdvisorUseCase) {
        this.listAdminAdvisorsUseCase = listAdminAdvisorsUseCase;
        this.createAdvisorUseCase = createAdvisorUseCase;
        this.updateAdvisorUseCase = updateAdvisorUseCase;
        this.deleteAdvisorUseCase = deleteAdvisorUseCase;
    }

    @GET
    @Operation(summary = "Listar todas las asesoras de viaje para el panel de administración")
    public List<TravelAdvisorDTO> listAdvisors() {
        return listAdminAdvisorsUseCase.execute();
    }

    @POST
    @Operation(summary = "Crear nueva asesora de viajes")
    public Response createAdvisor(@Valid CreateOrUpdateAdvisorRequest request) {
        TravelAdvisorDTO created = createAdvisorUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar asesora de viajes existente")
    public TravelAdvisorDTO updateAdvisor(@PathParam("id") Long id, @Valid CreateOrUpdateAdvisorRequest request) {
        return updateAdvisorUseCase.execute(id, request);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar asesora de viajes")
    public Response deleteAdvisor(@PathParam("id") Long id) {
        deleteAdvisorUseCase.execute(id);
        return Response.noContent().build();
    }
}
