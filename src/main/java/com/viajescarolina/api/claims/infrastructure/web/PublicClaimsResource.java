package com.viajescarolina.api.claims.infrastructure.web;

import com.viajescarolina.api.claims.application.dto.ClaimAttachmentDTO;
import com.viajescarolina.api.claims.application.dto.ClaimRecordDTO;
import com.viajescarolina.api.claims.application.dto.SubmitClaimRequest;
import com.viajescarolina.api.claims.application.usecase.GenerateClaimConstanciaPdfUseCase;
import com.viajescarolina.api.claims.application.usecase.GetClaimByCodeUseCase;
import com.viajescarolina.api.claims.application.usecase.SubmitClaimUseCase;
import com.viajescarolina.api.claims.application.usecase.UploadClaimAttachmentUseCase;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import java.io.FileInputStream;
import java.io.InputStream;

@Path("/api/public/v1/claims")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Libro de Reclamaciones", description = "Endpoints públicos para registro y consulta de reclamos")
public class PublicClaimsResource {

    private final SubmitClaimUseCase submitClaimUseCase;
    private final GetClaimByCodeUseCase getClaimByCodeUseCase;
    private final UploadClaimAttachmentUseCase uploadClaimAttachmentUseCase;
    private final GenerateClaimConstanciaPdfUseCase generateClaimConstanciaPdfUseCase;

    @Inject
    public PublicClaimsResource(SubmitClaimUseCase submitClaimUseCase,
                                 GetClaimByCodeUseCase getClaimByCodeUseCase,
                                 UploadClaimAttachmentUseCase uploadClaimAttachmentUseCase,
                                 GenerateClaimConstanciaPdfUseCase generateClaimConstanciaPdfUseCase) {
        this.submitClaimUseCase = submitClaimUseCase;
        this.getClaimByCodeUseCase = getClaimByCodeUseCase;
        this.uploadClaimAttachmentUseCase = uploadClaimAttachmentUseCase;
        this.generateClaimConstanciaPdfUseCase = generateClaimConstanciaPdfUseCase;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Registrar Hoja de Reclamación", description = "Registra un nuevo reclamo o queja en el Libro de Reclamaciones virtual y genera un código correlativo oficial")
    public Response submitClaim(@Valid SubmitClaimRequest request, @Context HttpServerRequest httpRequest) {
        String clientIp = httpRequest != null && httpRequest.remoteAddress() != null
                ? httpRequest.remoteAddress().host()
                : "127.0.0.1";

        try {
            ClaimRecordDTO created = submitClaimUseCase.execute(request, clientIp);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{claimCode}")
    @Operation(summary = "Consultar Estado de Reclamo por Código", description = "Obtiene el detalle y estado de una hoja de reclamación mediante su código único REC-YYYY-XXXX")
    public Response getClaimByCode(@PathParam("claimCode") String claimCode) {
        return getClaimByCodeUseCase.execute(claimCode)
                .map(claim -> Response.ok(claim).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Hoja de reclamación no encontrada con el código especificado: " + claimCode))
                        .build());
    }

    @POST
    @Path("/{id}/attachments")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Adjuntar evidencia a una Hoja de Reclamación", description = "Sube un archivo de evidencia (PDF, PNG o JPEG) asociado a un reclamo recién registrado, identificado por su ID numérico")
    public Response uploadAttachment(@PathParam("id") Long id, @RestForm("file") FileUpload file) {
        if (file == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("El archivo es obligatorio"))
                    .build();
        }

        try (InputStream is = new FileInputStream(file.uploadedFile().toFile())) {
            String originalName = file.fileName();
            String contentType = file.contentType() != null ? file.contentType() : "application/octet-stream";
            long size = file.size();

            ClaimAttachmentDTO saved = uploadClaimAttachmentUseCase.execute(id, originalName, contentType, is, size);
            return Response.status(Response.Status.CREATED).entity(saved).build();
        } catch (jakarta.ws.rs.WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al procesar archivo: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{claimCode}/constancia.pdf")
    @Produces("application/pdf")
    @Operation(summary = "Descargar Constancia de Registro (PDF)", description = "Genera y descarga la constancia PDF de una hoja de reclamación. Requiere el número de documento del titular como medida de seguridad, ya que el código de folio es correlativo y adivinable")
    public Response getConstanciaPdf(@PathParam("claimCode") String claimCode,
                                      @QueryParam("documentNumber") String documentNumber) {
        return generateClaimConstanciaPdfUseCase.execute(claimCode, documentNumber)
                .map(pdfBytes -> Response.ok(pdfBytes)
                        .type("application/pdf")
                        .header("Content-Disposition", "attachment; filename=\"constancia-" + claimCode + ".pdf\"")
                        .build())
                // .type(JSON) explícito: el método declara @Produces("application/pdf"), y sin fijar
                // el tipo aquí RESTEasy intentaría escribir este ErrorResponse (record) como PDF y
                // fallaría con 500 en vez de devolver el 404 esperado.
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(new ErrorResponse("No se encontró una hoja de reclamación con ese código y número de documento"))
                        .build());
    }

    public record ErrorResponse(String message) {}
}
