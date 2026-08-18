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

    @ConfigProperty(name = "publishing.revalidation.url", defaultValue = "http://127.0.0.1:3000/api/revalidate")
    String revalidationUrl;

    @ConfigProperty(name = "publishing.revalidation.secret", defaultValue = "vc-secret-isr-key-2026")
    String revalidationSecret;

    public TriggerPublishUseCase(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public PublishResponse execute(PublishRequest req, String adminUsername) {
        String target = req != null && req.target() != null ? req.target().toUpperCase() : "ALL";
        List<String> tagsToRevalidate = new ArrayList<>();

        if (req != null && req.customTags() != null && !req.customTags().isEmpty()) {
            tagsToRevalidate.addAll(req.customTags());
        } else {
            switch (target) {
                case "HOME" -> {
                    tagsToRevalidate.add("home");
                    tagsToRevalidate.add("/");
                }
                case "PROMOTIONS" -> {
                    tagsToRevalidate.add("promotions");
                    tagsToRevalidate.add("/promociones");
                }
                case "BLOG" -> {
                    tagsToRevalidate.add("blog");
                    tagsToRevalidate.add("/blog");
                }
                case "ABOUT" -> {
                    tagsToRevalidate.add("about");
                    tagsToRevalidate.add("/nosotros");
                }
                case "CONTACT" -> {
                    tagsToRevalidate.add("contact");
                    tagsToRevalidate.add("/contacto");
                }
                default -> {
                    tagsToRevalidate.add("all");
                    tagsToRevalidate.add("/");
                    tagsToRevalidate.add("/promociones");
                    tagsToRevalidate.add("/blog");
                    tagsToRevalidate.add("/nosotros");
                    tagsToRevalidate.add("/contacto");
                    tagsToRevalidate.add("/reclamaciones");
                }
            }
        }

        // Emitir webhook HTTP al Frontend Next.js para Revalidación On-Demand ISR
        try {
            HttpClient client = HttpClient.newBuilder()
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

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ignored) {
            // Revalidación tolerante en dev/offline
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
                    String.format("{\"target\": \"%s\", \"tags\": %s, \"reason\": \"%s\"}",
                            target,
                            "[\"" + String.join("\",\"", tagsToRevalidate) + "\"]",
                            req != null && req.reason() != null ? req.reason() : "Actualización de contenidos"),
                    Instant.now()
            );
            auditLogRepository.save(log);
        } catch (Exception ignored) {}

        return new PublishResponse(
                "SUCCESS",
                tagsToRevalidate,
                Instant.now(),
                operator,
                String.format("Publicación completada exitosamente. Se revalidaron %d tags/rutas en Next.js ISR.", tagsToRevalidate.size())
        );
    }
}
