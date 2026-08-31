package com.viajescarolina.api.publishing.application.usecase;

import com.viajescarolina.api.auth.domain.AuditLog;
import com.viajescarolina.api.auth.domain.AuditLogRepository;
import com.viajescarolina.api.publishing.application.dto.PublishRequest;
import com.viajescarolina.api.publishing.application.dto.PublishResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TriggerPublishUseCase {

    private final AuditLogRepository auditLogRepository;

    @ConfigProperty(name = "viajescarolina.publishing.revalidation-url")
    String revalidationUrl;

    @ConfigProperty(name = "viajescarolina.publishing.revalidation-secret")
    String revalidationSecret;

    public TriggerPublishUseCase(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Todas las páginas del sitio público, tal como están montadas en {@code apps/web}.
     *
     * <p>El objetivo ALL revalidaba solo cinco y se dejaba fuera las cinco páginas legales,
     * así que cambiar el aviso de privacidad o la constancia MINCETUR no se veía nunca.</p>
     */
    private static final List<String> TODAS_LAS_PAGINAS = List.of(
            "/", "/blog", "/nosotros", "/contacto", "/reclamaciones", "/buscar",
            "/terminos", "/privacidad", "/cookies", "/compromiso-esnna", "/constancia-mincetur");

    /**
     * Rutas públicas que hay que revalidar para un objetivo.
     *
     * <p><b>Solo rutas reales.</b> Antes cada objetivo enviaba además una etiqueta suelta
     * ({@code "home"}, {@code "promotions"}, {@code "all"}…) que el webhook traducía a
     * {@code /home}, {@code /promotions} y {@code /all}: rutas que no existen. Ninguna
     * revalidaba nada y el panel las contaba como publicadas igualmente.</p>
     *
     * <p>Los objetivos son generosos a propósito. Un artículo del blog también alimenta el
     * bloque de inspiración de la portada, y una asesora aparece en «Nosotros», en la firma
     * de sus artículos y en los testimonios: afinar más la lista es justo lo que hace que a
     * alguien se le olvide una página.</p>
     */
    private static List<String> pathsFor(String target) {
        return switch (target) {
            // Las promociones se publican en la PORTADA: no hay ninguna ruta /promociones.
            case "HOME", "PROMOTIONS" -> List.of("/");
            case "BLOG" -> List.of("/blog", "/");
            case "ABOUT" -> List.of("/nosotros", "/blog", "/");
            case "CONTACT" -> List.of("/contacto", "/");
            default -> TODAS_LAS_PAGINAS;
        };
    }

    @Transactional
    public PublishResponse execute(PublishRequest req, String adminUsername) {
        String target = req != null && req.target() != null ? req.target().toUpperCase() : "ALL";
        List<String> tagsToRevalidate = new ArrayList<>();

        if (req != null && req.customTags() != null && !req.customTags().isEmpty()) {
            tagsToRevalidate.addAll(req.customTags());
        } else {
            tagsToRevalidate.addAll(pathsFor(target));
        }

        // Emitir webhook HTTP al Frontend Next.js para Revalidación On-Demand ISR.
        // Se espera la respuesta real (antes era sendAsync sin await ni chequeo de status,
        // así que el endpoint SIEMPRE reportaba "SUCCESS" aunque el webhook fallara con 401
        // o el frontend estuviera caído — ver hallazgo de Fase B Ciclo 14).
        boolean webhookSucceeded;
        String webhookDetail;
        try {
            // HTTP_1_1 explícito: el servidor de desarrollo de Next.js solo habla HTTP/1.1 y la
            // negociación automática de HTTP/2 del HttpClient del JDK falla intermitentemente
            // contra él con "header parser received no bytes".
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(3))
                    .build();

            String jsonPayload = String.format("{\"secret\": \"%s\", \"tags\": %s}",
                    revalidationSecret,
                    "[\"" + String.join("\",\"", tagsToRevalidate) + "\"]");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(revalidationUrl))
                    .header("Content-Type", "application/json")
                    .header("X-Revalidation-Secret", revalidationSecret)
                    .timeout(Duration.ofSeconds(5))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            webhookSucceeded = response.statusCode() >= 200 && response.statusCode() < 300;
            webhookDetail = "HTTP " + response.statusCode();
        } catch (Exception e) {
            webhookSucceeded = false;
            webhookDetail = e.getClass().getSimpleName() + ": " + e.getMessage();
        }

        String operator = adminUsername != null && !adminUsername.isBlank() ? adminUsername : "admin";

        // Registrar en Auditoría de Gobernanza
        try {
            AuditLog log = new AuditLog(
                    null,
                    null,
                    operator,
                    "PUBLISH_ON_DEMAND_ISR",
                    "PUBLISHING",
                    target,
                    null,
                    String.format("{\"target\": \"%s\", \"tags\": %s, \"reason\": \"%s\", \"webhookSucceeded\": %b, \"webhookDetail\": \"%s\"}",
                            target,
                            "[\"" + String.join("\",\"", tagsToRevalidate) + "\"]",
                            req != null && req.reason() != null ? req.reason() : "Actualización de contenidos",
                            webhookSucceeded,
                            webhookDetail.replace("\"", "'")),
                    Instant.now()
            );
            auditLogRepository.save(log);
        } catch (Exception ignored) {}

        return webhookSucceeded
                ? new PublishResponse(
                        "SUCCESS",
                        tagsToRevalidate,
                        Instant.now(),
                        operator,
                        String.format("Publicación completada exitosamente. Se revalidaron %d tags/rutas en Next.js ISR (%s).", tagsToRevalidate.size(), webhookDetail)
                )
                : new PublishResponse(
                        "FAILED",
                        tagsToRevalidate,
                        Instant.now(),
                        operator,
                        "El webhook de revalidación no respondió correctamente (" + webhookDetail + "). Los cambios NO se reflejaron vía ISR."
                );
    }
}
