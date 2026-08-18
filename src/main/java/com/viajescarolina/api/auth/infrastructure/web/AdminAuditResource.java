package com.viajescarolina.api.auth.infrastructure.web;

import com.viajescarolina.api.auth.application.dto.AuditLogDTO;
import com.viajescarolina.api.auth.application.usecase.ListAuditLogsUseCase;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/admin/v1/audit-logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Admin Governance & Audit", description = "Trazabilidad y registro de auditoría de acciones administrativas")
public class AdminAuditResource {

    private final ListAuditLogsUseCase listAuditLogsUseCase;

    public AdminAuditResource(ListAuditLogsUseCase listAuditLogsUseCase) {
        this.listAuditLogsUseCase = listAuditLogsUseCase;
    }

    @GET
    @Operation(summary = "Listar bitácora de auditoría reciente")
    public List<AuditLogDTO> listAuditLogs(
            @QueryParam("entityType") String entityType,
            @QueryParam("limit") @DefaultValue("50") int limit
    ) {
        return listAuditLogsUseCase.execute(entityType, limit);
    }
}
