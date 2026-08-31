package com.viajescarolina.api.auth.infrastructure.web;

import com.viajescarolina.api.auth.application.dto.AdminUserDTO;
import com.viajescarolina.api.auth.application.dto.ChangeOwnPasswordRequest;
import com.viajescarolina.api.auth.application.dto.ChangeOwnPasswordResponse;
import com.viajescarolina.api.auth.application.dto.LoginRequest;
import com.viajescarolina.api.auth.application.dto.LoginResponse;
import com.viajescarolina.api.auth.application.usecase.ChangeOwnPasswordUseCase;
import com.viajescarolina.api.auth.application.usecase.LoginAdminUseCase;
import com.viajescarolina.api.auth.domain.AdminUser;
import com.viajescarolina.api.auth.domain.AdminUserRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;

@Path("/api/admin/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Admin Auth & Governance", description = "Autenticación administrativa, tokens JWT y cookies seguras")
public class AdminAuthResource {

    private final LoginAdminUseCase loginAdminUseCase;
    private final AdminUserRepository adminUserRepository;
    private final ChangeOwnPasswordUseCase changeOwnPasswordUseCase;

    @Inject
    JsonWebToken jwt;

    @ConfigProperty(name = "viajescarolina.security.cookie-secure", defaultValue = "true")
    boolean cookieSecure;

    public AdminAuthResource(LoginAdminUseCase loginAdminUseCase, AdminUserRepository adminUserRepository, ChangeOwnPasswordUseCase changeOwnPasswordUseCase) {
        this.loginAdminUseCase = loginAdminUseCase;
        this.adminUserRepository = adminUserRepository;
        this.changeOwnPasswordUseCase = changeOwnPasswordUseCase;
    }

    @POST
    @Path("/login")
    @PermitAll
    @Operation(summary = "Inicio de sesión administrativo", description = "Valida credenciales con Argon2id, genera JWT y cookie HttpOnly. Con rememberMe la sesión dura 30 días en vez de 1 hora")
    public Response login(@Valid LoginRequest req, @HeaderParam("X-Forwarded-For") String forwardedFor) {
        String clientIp = forwardedFor != null ? forwardedFor.split(",")[0].trim() : "127.0.0.1";
        LoginResponse result = loginAdminUseCase.execute(req, clientIp);

        NewCookie sessionCookie = new NewCookie.Builder("vc_admin_jwt")
                .value(result.token())
                .path("/")
                .maxAge((int) result.expiresInSeconds())
                .secure(cookieSecure)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.STRICT)
                .build();

        return Response.ok(result).cookie(sessionCookie).build();
    }

    @POST
    @Path("/logout")
    // El recurso declara `@Consumes(application/json)` a nivel de clase, pero este
    // endpoint NO lee cuerpo alguno. Con esa restricción, un logout enviado con
    // cualquier otro `Content-Type` (p. ej. `text/plain`, que es lo que pone un
    // `fetch` con un body de texto) se rechazaba con 415 SIN llegar al método:
    // no se emitía la cookie caducada y el mismo token seguía autenticando. Es
    // decir, el panel decía "sesión cerrada" y la sesión seguía viva.
    @Consumes(MediaType.WILDCARD)
    @RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
    @Operation(summary = "Cierre de sesión", description = "Invalida la cookie de sesión administrativa")
    public Response logout() {
        NewCookie expiredCookie = new NewCookie.Builder("vc_admin_jwt")
                .value("")
                .path("/")
                .maxAge(0)
                .secure(cookieSecure)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.STRICT)
                .build();

        return Response.ok().entity("{\"status\": \"LOGGED_OUT\"}").cookie(expiredCookie).build();
    }

    @PUT
    @Path("/password")
    @RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
    @Operation(summary = "Cambiar contraseña propia", description = "Permite al usuario autenticado rotar su propia contraseña, verificando la actual")
    public Response changePassword(@Valid ChangeOwnPasswordRequest req) {
        Long callerId;
        try {
            callerId = Long.valueOf(jwt.getSubject());
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        ChangeOwnPasswordResponse result = changeOwnPasswordUseCase.execute(callerId, req, jwt.getClaim("upn"));
        return Response.ok(result).build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
    @Operation(summary = "Obtener perfil del usuario autenticado")
    public Response getCurrentUser() {
        Long userId;
        try {
            userId = Long.valueOf(jwt.getSubject());
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return adminUserRepository.findUserById(userId)
                .map(AdminAuthResource::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.UNAUTHORIZED).build());
    }

    private static AdminUserDTO toDTO(AdminUser user) {
        return new AdminUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.isActive(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
