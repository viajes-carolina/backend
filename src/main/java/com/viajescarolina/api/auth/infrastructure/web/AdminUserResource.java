package com.viajescarolina.api.auth.infrastructure.web;

import com.viajescarolina.api.auth.application.dto.AdminUserDTO;
import com.viajescarolina.api.auth.application.dto.CreateAdminUserRequest;
import com.viajescarolina.api.auth.application.dto.UpdateAdminUserRequest;
import com.viajescarolina.api.auth.application.usecase.CreateAdminUserUseCase;
import com.viajescarolina.api.auth.application.usecase.ListAdminUsersUseCase;
import com.viajescarolina.api.auth.application.usecase.UpdateAdminUserUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/admin/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("SUPER_ADMIN")
@Tag(name = "Admin Users & RBAC", description = "Gestión de usuarios y roles del panel administrativo")
public class AdminUserResource {

    private final ListAdminUsersUseCase listAdminUsersUseCase;
    private final CreateAdminUserUseCase createAdminUserUseCase;
    private final UpdateAdminUserUseCase updateAdminUserUseCase;

    @Inject
    JsonWebToken jwt;

    public AdminUserResource(ListAdminUsersUseCase listAdminUsersUseCase, CreateAdminUserUseCase createAdminUserUseCase, UpdateAdminUserUseCase updateAdminUserUseCase) {
        this.listAdminUsersUseCase = listAdminUsersUseCase;
        this.createAdminUserUseCase = createAdminUserUseCase;
        this.updateAdminUserUseCase = updateAdminUserUseCase;
    }

    @GET
    @Operation(summary = "Listar usuarios del panel")
    public List<AdminUserDTO> listUsers() {
        return listAdminUsersUseCase.execute();
    }

    @POST
    @Operation(summary = "Crear nuevo usuario administrativo")
    public Response createUser(@Valid CreateAdminUserRequest req) {
        // El actor se deriva del JWT validado, nunca de un header enviado por el cliente (ver SEC-013).
        AdminUserDTO created = createAdminUserUseCase.execute(req, jwt.getClaim("upn"));
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar usuario administrativo")
    public AdminUserDTO updateUser(@PathParam("id") Long id, @Valid UpdateAdminUserRequest req) {
        return updateAdminUserUseCase.execute(id, req, jwt.getClaim("upn"));
    }
}
