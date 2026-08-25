package com.viajescarolina.api.promotions.infrastructure.facebook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

/**
 * Cliente de la Graph API de Facebook para publicar el contenido de una promoción (foto +
 * caption en formato controlado, compuesto desde los campos estructurados que llena el
 * admin) como post en la Página de Viajes Carolina. Fail-safe: cualquier problema
 * (credenciales ausentes, publicación deshabilitada, error de red, respuesta inesperada) se
 * traduce en {@code Optional.empty()} en vez de propagar una excepción — crear la promoción
 * en el dominio nunca debe fallar por un problema de la API externa.
 */
@ApplicationScoped
public class FacebookGraphClient {

    private static final Logger LOG = Logger.getLogger(FacebookGraphClient.class);
    private static final String GRAPH_API_BASE_URL = "https://graph.facebook.com/v19.0";
    private static final String LINE_BREAK = "\r\n";

    @ConfigProperty(name = "viajescarolina.facebook.page-id")
    Optional<String> pageId;

    @ConfigProperty(name = "viajescarolina.facebook.page-access-token")
    Optional<String> pageAccessToken;

    @ConfigProperty(name = "viajescarolina.facebook.publish-enabled", defaultValue = "false")
    boolean publishEnabled;

    @Inject
    ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    /**
     * Publica una foto con caption en la Página vía {@code POST /{page-id}/photos}
     * (multipart/form-data, construido a mano — Java no trae soporte multipart nativo y el
     * proyecto no tiene ninguna librería HTTP de terceros que lo simplifique). Nunca lanza:
     * sin credenciales o con la publicación deshabilitada retorna {@code Optional.empty()} sin
     * llamar a la API; cualquier error HTTP o de red también retorna {@code Optional.empty()}
     * (logueado con {@code LOG.error}).
     */
    public Optional<FacebookPublishResult> publishPhoto(byte[] photoBytes, String mimeType, String caption) {
        if (!publishEnabled || pageId.isEmpty() || pageId.get().isBlank()
                || pageAccessToken.isEmpty() || pageAccessToken.get().isBlank()) {
            LOG.debug("Publicación en Facebook deshabilitada o sin credenciales configuradas; se omite la llamada a la Graph API.");
            return Optional.empty();
        }
        if (photoBytes == null || photoBytes.length == 0) {
            LOG.debug("No hay bytes de foto para publicar en Facebook; se omite la llamada a la Graph API.");
            return Optional.empty();
        }

        String boundary = "----ViajesCarolinaBoundary" + UUID.randomUUID();
        try {
            byte[] body = buildMultipartBody(boundary, photoBytes, mimeType, caption, pageAccessToken.get());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GRAPH_API_BASE_URL + "/" + pageId.get() + "/photos"))
                    .timeout(Duration.ofSeconds(15))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                LOG.error("La Graph API de Facebook respondió con estado inesperado al publicar la foto: HTTP "
                        + response.statusCode() + " — " + response.body());
                return Optional.empty();
            }

            JsonNode root = objectMapper.readTree(response.body());
            String postId = root.hasNonNull("post_id") ? root.get("post_id").asText()
                    : root.hasNonNull("id") ? root.get("id").asText() : null;
            if (postId == null || postId.isBlank()) {
                LOG.error("La Graph API de Facebook no devolvió un id de post al publicar la foto: " + response.body());
                return Optional.empty();
            }

            // /{page-id}/photos no devuelve permalink_url directo (a diferencia de /posts) —
            // se construye a partir del post id, que sí resuelve a la publicación en facebook.com.
            String permalinkUrl = "https://www.facebook.com/" + postId;
            return Optional.of(new FacebookPublishResult(postId, permalinkUrl));
        } catch (Exception e) {
            LOG.error("Error publicando la foto de la promoción en la Página de Facebook", e);
            return Optional.empty();
        }
    }

    private byte[] buildMultipartBody(String boundary, byte[] photoBytes, String mimeType, String caption, String accessToken)
            throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        writeFormField(out, boundary, "caption", caption != null ? caption : "");
        writeFormField(out, boundary, "access_token", accessToken);

        out.write(("--" + boundary + LINE_BREAK).getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Disposition: form-data; name=\"source\"; filename=\"promotion.jpg\"" + LINE_BREAK)
                .getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Type: " + (mimeType != null ? mimeType : "image/jpeg") + LINE_BREAK + LINE_BREAK)
                .getBytes(StandardCharsets.UTF_8));
        out.write(photoBytes);
        out.write(LINE_BREAK.getBytes(StandardCharsets.UTF_8));

        out.write(("--" + boundary + "--" + LINE_BREAK).getBytes(StandardCharsets.UTF_8));
        return out.toByteArray();
    }

    private void writeFormField(ByteArrayOutputStream out, String boundary, String name, String value) throws IOException {
        out.write(("--" + boundary + LINE_BREAK).getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_BREAK + LINE_BREAK)
                .getBytes(StandardCharsets.UTF_8));
        out.write((value + LINE_BREAK).getBytes(StandardCharsets.UTF_8));
    }
}
