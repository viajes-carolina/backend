package com.viajescarolina.api.publishing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import com.viajescarolina.api.publishing.application.dto.PublishResponse;
import com.viajescarolina.api.publishing.application.usecase.GetPublishingStatusUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Estado de publicación del sitio. Lógica pura sobre la bitácora: se prueba sin Quarkus, con una
 * bitácora falsa, para poder fijar fechas exactas y comprobar que la respuesta las respeta.
 *
 * <p>El defecto que originó este caso de uso: el endpoint rellenaba {@code publishedAt} con
 * {@code Instant.now()} y {@code triggeredBy} con {@code "SYSTEM"}, de modo que el panel siempre
 * decía que el sitio "acababa de publicarse" aunque llevara semanas sin publicarse. Los dos
 * primeros tests son exactamente la red que impide que eso vuelva.</p>
 */
@DisplayName("Publicación — estado leído de la bitácora, sin fechas inventadas")
class GetPublishingStatusUseCaseTest {

    private static final Instant PUBLICACION_REAL = Instant.parse("2026-08-14T09:15:30Z");

    private final BitacoraFalsa bitacora = new BitacoraFalsa();
    private final GetPublishingStatusUseCase caso =
            new GetPublishingStatusUseCase(bitacora, new ObjectMapper());

    @Test
    @DisplayName("Con publicaciones registradas devuelve la fecha REAL de la última, no la de ahora")
    void devuelveLaFechaRealDeLaUltimaPublicacion() {
        bitacora.registrar("carolina", PUBLICACION_REAL,
                "{\"target\":\"ALL\",\"tags\":[\"promotions\",\"home\"],\"webhookSucceeded\":true,\"webhookDetail\":\"200 OK\"}");

        PublishResponse estado = caso.execute();

        assertEquals(GetPublishingStatusUseCase.STATUS_SUCCESS, estado.status());
        assertEquals(PUBLICACION_REAL, estado.publishedAt(),
                "publishedAt debe ser la fecha del registro de auditoría, no Instant.now()");
        assertEquals("carolina", estado.triggeredBy(),
                "El operador sale del registro real, no de un \"SYSTEM\" inventado");
        assertEquals(List.of("promotions", "home"), estado.revalidatedTags());
        assertTrue(estado.message().contains("carolina"));
    }

    @Test
    @DisplayName("Con varias publicaciones se queda con la última que devuelve el repositorio")
    void seQuedaConLaUltimaPublicacion() {
        // El repositorio ya ordena por createdAt DESC y limita a 1: el caso de uso debe pedir
        // exactamente un registro y usar ese, sin traerse la bitácora entera.
        bitacora.registrar("carolina", PUBLICACION_REAL, "{\"webhookSucceeded\":true}");

        PublishResponse estado = caso.execute();

        assertEquals(1, bitacora.ultimoLimitePedido,
                "Debe pedirse un solo registro a la bitácora (LIMIT 1 en base de datos)");
        assertEquals("PUBLISHING", bitacora.ultimoTipoPedido);
        assertEquals(PUBLICACION_REAL, estado.publishedAt());
    }

    @Test
    @DisplayName("Sin ninguna publicación registrada dice NEVER_PUBLISHED y no inventa fecha")
    void sinPublicacionesNoInventaFecha() {
        PublishResponse estado = caso.execute();

        assertEquals(GetPublishingStatusUseCase.STATUS_NEVER_PUBLISHED, estado.status());
        assertNull(estado.publishedAt(), "Si nunca se publicó, no puede haber fecha de publicación");
        assertNull(estado.triggeredBy(), "Si nunca se publicó, no hay operador que atribuir");
        assertEquals(List.of(), estado.revalidatedTags());
        assertTrue(estado.message().contains("nunca se ha publicado"));
    }

    @Test
    @DisplayName("Si el webhook de revalidación falló, el estado es FAILED con su fecha real")
    void publicacionConWebhookFallidoEsFailed() {
        bitacora.registrar("admin", PUBLICACION_REAL,
                "{\"tags\":[\"home\"],\"webhookSucceeded\":false,\"webhookDetail\":\"503 Service Unavailable\"}");

        PublishResponse estado = caso.execute();

        assertEquals(GetPublishingStatusUseCase.STATUS_FAILED, estado.status());
        assertEquals(PUBLICACION_REAL, estado.publishedAt());
        assertEquals("admin", estado.triggeredBy());
        assertTrue(estado.message().contains("503 Service Unavailable"));
        assertTrue(estado.message().contains("desactualizado"));
    }

    @Test
    @DisplayName("Con un detalle de auditoría ilegible no se afirma ni éxito ni fallo")
    void detalleIlegibleEsUnknown() {
        bitacora.registrar("admin", PUBLICACION_REAL, "esto-no-es-json");

        PublishResponse estado = caso.execute();

        assertEquals(GetPublishingStatusUseCase.STATUS_UNKNOWN, estado.status());
        assertEquals(PUBLICACION_REAL, estado.publishedAt(),
                "Aunque el detalle no se pueda leer, la fecha del registro sí es un hecho");
        assertEquals("admin", estado.triggeredBy());
    }

    @Test
    @DisplayName("Sin el campo webhookSucceeded tampoco se afirma éxito")
    void sinElCampoDeResultadoEsUnknown() {
        bitacora.registrar("admin", PUBLICACION_REAL, "{\"tags\":[\"home\"]}");

        assertEquals(GetPublishingStatusUseCase.STATUS_UNKNOWN, caso.execute().status());
    }

    @Test
    @DisplayName("Los tags no textuales se descartan en lugar de romper la respuesta")
    void ignoraTagsMalFormados() {
        bitacora.registrar("admin", PUBLICACION_REAL,
                "{\"tags\":[\"home\",42,null,\"blog\"],\"webhookSucceeded\":true}");

        assertEquals(List.of("home", "blog"), caso.execute().revalidatedTags());
    }

    @Test
    @DisplayName("Un detalle sin tags devuelve una lista vacía, nunca null")
    void sinTagsDevuelveListaVacia() {
        bitacora.registrar("admin", PUBLICACION_REAL, "{\"webhookSucceeded\":true}");

        assertEquals(List.of(), caso.execute().revalidatedTags());
    }

    // ---------------------------------------------------------------------------------

    /** Bitácora en memoria: permite fijar fechas exactas y observar qué se le pidió. */
    private static final class BitacoraFalsa implements AuditLogRepository {

        private final List<AuditLog> registros = new ArrayList<>();
        private int ultimoLimitePedido;
        private String ultimoTipoPedido;

        void registrar(String usuario, Instant cuando, String detallesJson) {
            registros.add(0, new AuditLog(
                    (long) (registros.size() + 1), null, usuario,
                    "PUBLISH_ON_DEMAND_ISR", "PUBLISHING", "ALL", null, detallesJson, cuando));
        }

        @Override
        public List<AuditLog> listRecent(int limit) {
            return registros.stream().limit(limit).toList();
        }

        @Override
        public List<AuditLog> listByEntityType(String entityType, int limit) {
            ultimoLimitePedido = limit;
            ultimoTipoPedido = entityType;
            return registros.stream()
                    .filter(r -> entityType.equals(r.getEntityType()))
                    .limit(limit)
                    .toList();
        }

        @Override
        public AuditLog save(AuditLog log) {
            registros.add(0, log);
            return log;
        }
    }
}
