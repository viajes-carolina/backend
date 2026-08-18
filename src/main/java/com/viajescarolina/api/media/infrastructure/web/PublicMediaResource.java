package com.viajescarolina.api.media.infrastructure.web;

import com.viajescarolina.api.media.application.dto.MediaAssetDTO;
import com.viajescarolina.api.media.application.usecase.GetMediaAssetUseCase;
import com.viajescarolina.api.media.domain.MediaStorageService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.io.InputStream;

@Path("/api/public/v1/media")
@Tag(name = "Public Media", description = "Acceso público y optimizado a activos multimedia")
public class PublicMediaResource {

    private final GetMediaAssetUseCase getUseCase;
    private final MediaStorageService storageService;

    @Inject
    public PublicMediaResource(GetMediaAssetUseCase getUseCase, MediaStorageService storageService) {
        this.getUseCase = getUseCase;
        this.storageService = storageService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener metadatos públicos del activo", description = "Retorna dimensiones, puntos focales y variantes")
    public Response getPublicMedia(@PathParam("id") Long id) {
        MediaAssetDTO dto = getUseCase.execute(id);
        return Response.ok(dto)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400, s-maxage=86400")
                .build();
    }

    @GET
    @Path("/{filename}/file")
    @Operation(summary = "Servir archivo binario multimedia", description = "Retorna el flujo de bytes de la imagen con caché inmutable")
    public Response getMediaFile(@PathParam("filename") String filename) {
        try {
            MediaAssetDTO dto = getUseCase.executeByFilename(filename);
            InputStream stream = storageService.retrieve(filename);
            return Response.ok(stream)
                    .type(dto.mimeType() != null ? dto.mimeType() : "image/webp")
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000, immutable")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Archivo no encontrado: " + filename).build();
        }
    }
}
