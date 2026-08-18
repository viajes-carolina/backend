package com.viajescarolina.api.common.infrastructure.web;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.Instant;
import java.util.Map;

@Path("/api/public/v1/info")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "System Info", description = "Información del estado y versión del backend")
public class ApiInfoResource {

    @GET
    @Operation(summary = "Obtener metadatos de la API", description = "Retorna el nombre, versión y estado del servicio")
    public Map<String, Object> getApiInfo() {
        return Map.of(
            "name", "Viajes Carolina API",
            "version", "1.0.0",
            "status", "UP",
            "architecture", "Hexagonal Architecture with Quarkus 3.x & Java 25 LTS",
            "timestamp", Instant.now().toString()
        );
    }
}
