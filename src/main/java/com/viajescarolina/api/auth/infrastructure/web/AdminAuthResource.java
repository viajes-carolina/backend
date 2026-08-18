package com.viajescarolina.api.auth.infrastructure.web;

import com.viajescarolina.api.auth.application.dto.AdminUserDTO;
import com.viajescarolina.api.auth.application.dto.LoginRequest;
import com.viajescarolina.api.auth.application.dto.LoginResponse;
import com.viajescarolina.api.auth.application.usecase.ListAdminUsersUseCase;
import com.viajescarolina.api.auth.application.usecase.LoginAdminUseCase;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/admin/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Admin Auth & Governance", description = "Autenticación administrativa, tokens JWT y cookies seguras")
public class AdminAuthResource {

    private final LoginAdminUseCase loginAdminUseCase;
    private final ListAdminUsersUseCase listAdminUsersUseCase;

    public AdminAuthResource(LoginAdminUseCase loginAdminUseCase, ListAdminUsersUseCase listAdminUsersUseCase) {
        this.loginAdminUseCase = loginAdminUseCase;
        this.listAdminUsersUseCase = listAdminUsersUseCase;
    }

    @POST
    @Path("/login")
    @Operation(summary = "Inicio de sesión administrativo", description = "Valida credenciales con Argon2id, genera JWT y cookie HttpOnly")
    public Response login(@Valid LoginRequest req, @HeaderParam("X-Forwarded-For") String forwardedFor) {
        String clientIp = forwardedFor != null ? forwardedFor.split(",")[0].trim() : "127.0.0.1";
        LoginResponse result = loginAdminUseCase.execute(req, clientIp);

        NewCookie sessionCookie = new NewCookie.Builder("vc_admin_jwt")
                .value(result.token())
                .path("/")
                .maxAge((int) result.expiresInSeconds())
                .secure(false) // Permitir en dev local
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.STRICT)
                .build();

        return Response.ok(result).cookie(sessionCookie).build();
    }

    @POST
    @Path("/logout")
    @Operation(summary = "Cierre de sesión", description = "Invalida la cookie de sesión administrativa")
    public Response logout() {
        NewCookie expiredCookie = new NewCookie.Builder("vc_admin_jwt")
                .value("")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.STRICT)
                .build();

        return Response.ok().entity("{\"status\": \"LOGGED_OUT\"}").cookie(expiredCookie).build();
    }

    @GET
    @Path("/me")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    public Response getCurrentUser(@HeaderParam("Authorization") String authHeader) {
        List<AdminUserDTO> users = listAdminUsersUseCase.execute();
        if (!users.isEmpty()) {
            return Response.ok(users.get(0)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
