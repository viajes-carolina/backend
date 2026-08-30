package com.viajescarolina.api.publishing.application.usecase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import com.viajescarolina.api.publishing.application.dto.PublishResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

/**
 * Consulta de solo lectura del estado de publicación del sitio.
 *
 * <p>El único hecho verificable sobre "cuándo se publicó" es el registro de auditoría que
 * {@link TriggerPublishUseCase} escribe en cada publicación
 * ({@code entityType = "PUBLISHING"}, {@code action = "PUBLISH_ON_DEMAND_ISR"}), cuyo
 * {@code detailsJson} contiene {@code target}, {@code tags}, {@code reason},
 * {@code webhookSucceeded} y {@code webhookDetail}.</p>
 *
 * <p>Antes, el endpoint de estado rellenaba {@code publishedAt} con {@code Instant.now()} y
 * {@code triggeredBy} con {@code "SYSTEM"}: datos inventados que hacían parecer que el sitio
 * acababa de publicarse en cada consulta. Aquí todo sale del último registro real, y si nunca
 * se ha publicado se dice explícitamente en lugar de fabricar una fecha.</p>
 *
 * <p>Sin {@code @Transactional}: no muta estado.</p>
 */
@ApplicationScoped
public class GetPublishingStatusUseCase {

    /** entityType con el que TriggerPublishUseCase registra cada publicación en la bitácora. */
    private static final String PUBLISHING_ENTITY_TYPE = "PUBLISHING";

    /** Campo booleano de detailsJson que indica si el webhook de revalidación respondió 2xx. */
    private static final String WEBHOOK_SUCCEEDED_FIELD = "webhookSucceeded";
    private static final String WEBHOOK_DETAIL_FIELD = "webhookDetail";
    private static final String TAGS_FIELD = "tags";

    /** La última publicación registrada se completó (webhook de revalidación 2xx). */
    public static final String STATUS_SUCCESS = "SUCCESS";
    /** La última publicación registrada falló: el sitio público puede estar desactualizado. */
    public static final String STATUS_FAILED = "FAILED";
    /** No existe ninguna publicación en la bitácora: el sitio nunca se ha publicado. */
    public static final String STATUS_NEVER_PUBLISHED = "NEVER_PUBLISHED";
    /** Hay registro de publicación pero su detalle no es legible; no se afirma éxito ni fallo. */
    public static final String STATUS_UNKNOWN = "UNKNOWN";

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public GetPublishingStatusUseCase(AuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    public PublishResponse execute() {
        // Se pide UN registro: el repositorio ordena por createdAt DESC y pagina en base de datos
        // (LIMIT 1 sobre idx_audit_log_entity_type), no se trae la bitácora entera para filtrarla.
        List<AuditLog> latest = auditLogRepository.listByEntityType(PUBLISHING_ENTITY_TYPE, 1);

        if (latest.isEmpty()) {
            // Nunca se ha publicado: publishedAt y triggeredBy quedan en null, no se inventa una fecha.
            return new PublishResponse(
                    STATUS_NEVER_PUBLISHED,
                    List.of(),
                    null,
                    null,
                    "El sitio nunca se ha publicado: no existe ningún registro de publicación en la bitácora de auditoría."
            );
        }

        AuditLog log = latest.get(0);
        JsonNode details = parseDetails(log.getDetailsJson());
        String operator = log.getUsername();
        String target = log.getEntityId() != null && !log.getEntityId().isBlank() ? log.getEntityId() : "ALL";
        List<String> tags = readTags(details);

        if (details == null || !details.hasNonNull(WEBHOOK_SUCCEEDED_FIELD)) {
            return new PublishResponse(
                    STATUS_UNKNOWN,
                    tags,
                    log.getCreatedAt(),
                    operator,
                    String.format(
                            "Existe un registro de publicación (%s, lanzada por %s) pero su detalle de auditoría no es legible: no se puede confirmar si la revalidación ISR se completó.",
                            target, operator)
            );
        }

        boolean succeeded = details.get(WEBHOOK_SUCCEEDED_FIELD).asBoolean(false);
        String webhookDetail = details.path(WEBHOOK_DETAIL_FIELD).asText("sin detalle");

        return succeeded
                ? new PublishResponse(
                        STATUS_SUCCESS,
                        tags,
                        log.getCreatedAt(),
                        operator,
                        String.format(
                                "Última publicación completada por %s sobre %s: se revalidaron %d tags/rutas en Next.js ISR (%s).",
                                operator, target, tags.size(), webhookDetail)
                )
                : new PublishResponse(
                        STATUS_FAILED,
                        tags,
                        log.getCreatedAt(),
                        operator,
                        String.format(
                                "La última publicación (%s, lanzada por %s) no se completó: el webhook de revalidación falló (%s). El sitio público puede estar desactualizado.",
                                target, operator, webhookDetail)
                );
    }

    /** Devuelve el detailsJson parseado, o null si no es un objeto JSON legible. */
    private JsonNode parseDetails(String detailsJson) {
        if (detailsJson == null || detailsJson.isBlank()) {
            return null;
        }
        try {
            JsonNode node = objectMapper.readTree(detailsJson);
            return node != null && node.isObject() ? node : null;
        } catch (Exception e) {
            return null;
        }
    }

    /** Tags/rutas realmente revalidadas en la última publicación; lista vacía si no constan. */
    private List<String> readTags(JsonNode details) {
        if (details == null) {
            return List.of();
        }
        JsonNode tagsNode = details.path(TAGS_FIELD);
        if (!tagsNode.isArray()) {
            return List.of();
        }
        List<String> tags = new ArrayList<>(tagsNode.size());
        tagsNode.forEach(tag -> {
            if (tag.isTextual()) {
                tags.add(tag.asText());
            }
        });
        return List.copyOf(tags);
    }
}
