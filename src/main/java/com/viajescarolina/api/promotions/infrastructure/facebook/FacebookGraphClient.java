package com.viajescarolina.api.promotions.infrastructure.facebook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cliente de la Graph API de Facebook para leer el feed público de la Página
 * de Viajes Carolina. Fail-closed: cualquier problema (credenciales ausentes,
 * error de red, respuesta inesperada) se traduce en una lista vacía en vez de
 * propagar una excepción — la sincronización nunca debe tumbar el backend.
 */
@ApplicationScoped
public class FacebookGraphClient {

    private static final Logger LOG = Logger.getLogger(FacebookGraphClient.class);
    private static final String GRAPH_API_BASE_URL = "https://graph.facebook.com/v19.0";

    @ConfigProperty(name = "viajescarolina.facebook.page-id")
    Optional<String> pageId;

    @ConfigProperty(name = "viajescarolina.facebook.page-access-token")
    Optional<String> pageAccessToken;

    @ConfigProperty(name = "viajescarolina.facebook.sync-enabled", defaultValue = "false")
    boolean syncEnabled;

    @Inject
    ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    public List<FacebookPost> fetchRecentPagePosts() {
        if (!syncEnabled || pageId.isEmpty() || pageId.get().isBlank()
                || pageAccessToken.isEmpty() || pageAccessToken.get().isBlank()) {
            // Estado esperado en la mayoría de los entornos (sin credenciales configuradas
            // o sincronización deshabilitada explícitamente): no es un error.
            LOG.debug("Sincronización de Facebook deshabilitada o sin credenciales configuradas; se omite la llamada a la Graph API.");
            return List.of();
        }

        try {
            String url = GRAPH_API_BASE_URL + "/" + pageId.get()
                    + "/feed?fields=id,message,created_time,permalink_url,attachments%7Bmedia%7D"
                    + "&access_token=" + URLEncoder.encode(pageAccessToken.get(), StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                LOG.error("La Graph API de Facebook respondió con estado inesperado: HTTP " + response.statusCode());
                return List.of();
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode data = root.path("data");
            if (!data.isArray()) {
                return List.of();
            }

            List<FacebookPost> posts = new ArrayList<>();
            for (JsonNode item : data) {
                String id = item.path("id").asText(null);
                if (id == null || id.isBlank()) {
                    continue;
                }
                String message = item.hasNonNull("message") ? item.get("message").asText() : null;
                String createdTime = item.path("created_time").asText(null);
                String permalinkUrl = item.path("permalink_url").asText(null);
                String photoUrl = extractPhotoUrl(item);

                posts.add(new FacebookPost(id, message, createdTime, permalinkUrl, photoUrl));
            }
            return posts;
        } catch (Exception e) {
            LOG.error("Error consultando el feed de la Página de Facebook", e);
            return List.of();
        }
    }

    private String extractPhotoUrl(JsonNode postItem) {
        JsonNode attachmentsData = postItem.path("attachments").path("data");
        if (!attachmentsData.isArray() || attachmentsData.isEmpty()) {
            return null;
        }
        JsonNode firstAttachment = attachmentsData.get(0);
        JsonNode src = firstAttachment.path("media").path("image").path("src");
        return src.isMissingNode() || src.isNull() ? null : src.asText(null);
    }
}
