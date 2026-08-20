package com.viajescarolina.api.media.infrastructure.web;

import com.viajescarolina.api.media.application.dto.MediaAssetDTO;
import com.viajescarolina.api.media.application.dto.UpdateMediaFocalPointRequest;
import com.viajescarolina.api.media.application.usecase.DeleteMediaAssetUseCase;
import com.viajescarolina.api.media.application.usecase.GetMediaAssetUseCase;
import com.viajescarolina.api.media.application.usecase.ListMediaAssetsUseCase;
import com.viajescarolina.api.media.application.usecase.UpdateMediaFocalPointUseCase;
import com.viajescarolina.api.media.application.usecase.UploadMediaAssetUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import java.io.FileInputStream;
import java.io.InputStream;

@Path("/api/admin/v1/media")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Media", description = "Gestión centralizada de activos multimedia, biblioteca de imágenes y puntos focales")
public class AdminMediaResource {

    private final UploadMediaAssetUseCase uploadUseCase;
    private final GetMediaAssetUseCase getUseCase;
    private final ListMediaAssetsUseCase listUseCase;
    private final UpdateMediaFocalPointUseCase updateFocalPointUseCase;
    private final DeleteMediaAssetUseCase deleteUseCase;

    @Inject
    public AdminMediaResource(
            UploadMediaAssetUseCase uploadUseCase,
            GetMediaAssetUseCase getUseCase,
            ListMediaAssetsUseCase listUseCase,
            UpdateMediaFocalPointUseCase updateFocalPointUseCase,
            DeleteMediaAssetUseCase deleteUseCase) {
        this.uploadUseCase = uploadUseCase;
        this.getUseCase = getUseCase;
        this.listUseCase = listUseCase;
        this.updateFocalPointUseCase = updateFocalPointUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @GET
    @Operation(summary = "Listar activos multimedia", description = "Retorna lista paginada de imágenes activas con soporte de filtrado")
    public Response listMedia(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("24") int size,
            @QueryParam("mimeType") String mimeType) {
        ListMediaAssetsUseCase.MediaPageResponse response = listUseCase.execute(page, size, mimeType);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener activo por ID", description = "Retorna los metadatos completos y variantes de una imagen")
    public Response getMedia(@PathParam("id") Long id) {
        MediaAssetDTO dto = getUseCase.execute(id);
        return Response.ok(dto).build();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Subir activo multimedia", description = "Recibe un archivo multipart, valida que sea una imagen real, la redimensiona/recomprime si corresponde y calcula sus dimensiones finales")
    public Response uploadMedia(
            @RestForm("file") FileUpload file,
            @RestForm("altText") String altText,
            @RestForm("caption") String caption) {
        if (file == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("El archivo es obligatorio").build();
        }

        try (InputStream is = new FileInputStream(file.uploadedFile().toFile())) {
            String originalName = file.fileName();
            String contentType = file.contentType() != null ? file.contentType() : "application/octet-stream";
            long size = file.size();

            MediaAssetDTO saved = uploadUseCase.execute(originalName, contentType, is, size, altText, caption);
            return Response.status(Response.Status.CREATED).entity(saved).build();
        } catch (jakarta.ws.rs.WebApplicationException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al procesar archivo: " + e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}/focal-point")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Actualizar punto focal y metadatos", description = "Actualiza las coordenadas porcentuales X e Y (0..100) para responsive focal rendering")
    public Response updateFocalPoint(
            @PathParam("id") Long id,
            @Valid UpdateMediaFocalPointRequest request) {
        MediaAssetDTO updated = updateFocalPointUseCase.execute(id, request);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar activo multimedia", description = "Desactiva lógicamente el activo multimedia")
    public Response deleteMedia(@PathParam("id") Long id) {
        deleteUseCase.execute(id);
        return Response.noContent().build();
    }
}
