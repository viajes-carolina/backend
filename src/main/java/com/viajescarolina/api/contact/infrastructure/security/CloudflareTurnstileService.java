package com.viajescarolina.api.contact.infrastructure.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.contact.domain.TurnstileVerificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

@ApplicationScoped
public class CloudflareTurnstileService implements TurnstileVerificationService {

    private static final Logger LOG = Logger.getLogger(CloudflareTurnstileService.class);
    private static final String SITEVERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    @ConfigProperty(name = "turnstile.secret-key")
    Optional<String> secretKey;

    @ConfigProperty(name = "turnstile.enabled", defaultValue = "false")
    boolean enabled;

    @Inject
    ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Override
    public boolean verifyToken(String token, String remoteIp) {
        if (!enabled) {
            // Verificación deshabilitada explícitamente (uso local/dev sin credenciales de Cloudflare).
            return true;
        }

        if (token == null || token.isBlank() || secretKey.isEmpty() || secretKey.get().isBlank()) {
            return false;
        }

        try {
            StringBuilder form = new StringBuilder()
                    .append("secret=").append(URLEncoder.encode(secretKey.get(), StandardCharsets.UTF_8))
                    .append("&response=").append(URLEncoder.encode(token, StandardCharsets.UTF_8));
            if (remoteIp != null && !remoteIp.isBlank()) {
                form.append("&remoteip=").append(URLEncoder.encode(remoteIp, StandardCharsets.UTF_8));
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SITEVERIFY_URL))
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode json = objectMapper.readTree(response.body());
            return json.path("success").asBoolean(false);
        } catch (Exception e) {
            LOG.error("Error verificando token de Cloudflare Turnstile", e);
            return false;
        }
    }
}
