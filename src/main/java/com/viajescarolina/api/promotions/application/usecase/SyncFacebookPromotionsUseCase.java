package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.media.application.dto.MediaAssetDTO;
import com.viajescarolina.api.media.application.usecase.UploadMediaAssetUseCase;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import com.viajescarolina.api.promotions.infrastructure.facebook.FacebookGraphClient;
import com.viajescarolina.api.promotions.infrastructure.facebook.FacebookPost;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.Normalizer;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Sincroniza el feed de la Página de Facebook de Viajes Carolina: por cada post nuevo con
 * foto, descarga la imagen, la sube al pipeline existente de {@code media_asset} y crea una
 * {@link Promotion} INACTIVA (source=FACEBOOK) para que el staff la complete manualmente
 * (precio, duración, destino) y la active. Nunca se sobreescribe una promoción ya
 * sincronizada — se identifica por {@code facebookPostId}, que es único.
 */
@ApplicationScoped
public class SyncFacebookPromotionsUseCase {

    private static final Logger LOG = Logger.getLogger(SyncFacebookPromotionsUseCase.class);
    private static final String DEFAULT_TITLE = "Promoción desde Facebook — revisar";

    private final FacebookGraphClient facebookGraphClient;
    private final PromotionRepository promotionRepository;
    private final UploadMediaAssetUseCase uploadMediaAssetUseCase;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    @Inject
    public SyncFacebookPromotionsUseCase(
            FacebookGraphClient facebookGraphClient,
            PromotionRepository promotionRepository,
            UploadMediaAssetUseCase uploadMediaAssetUseCase) {
        this.facebookGraphClient = facebookGraphClient;
        this.promotionRepository = promotionRepository;
        this.uploadMediaAssetUseCase = uploadMediaAssetUseCase;
    }

    @Transactional
    public int execute() {
        List<FacebookPost> posts = facebookGraphClient.fetchRecentPagePosts();
        int created = 0;

        for (FacebookPost post : posts) {
            if (post.photoUrl() == null) {
                // Sin foto no hay nada que subir al pipeline de media_asset: se descarta.
                continue;
            }
            if (promotionRepository.findByFacebookPostId(post.id()).isPresent()) {
                // Ya sincronizado en una corrida anterior: nunca se pisa una promoción existente.
                continue;
            }

            byte[] photoBytes = downloadPhotoBytes(post.photoUrl());
            if (photoBytes == null || photoBytes.length == 0) {
                LOG.error("No se pudo descargar la foto del post de Facebook " + post.id() + "; se omite este post.");
                continue;
            }

            MediaAssetDTO uploadedMedia;
            try {
                uploadedMedia = uploadMediaAssetUseCase.execute(
                        "facebook-post-" + post.id() + ".jpg",
                        "image/jpeg",
                        new ByteArrayInputStream(photoBytes),
                        photoBytes.length,
                        "Foto sincronizada desde Facebook",
                        "Foto sincronizada desde Facebook"
                );
            } catch (Exception e) {
                LOG.error("Error subiendo la foto del post de Facebook " + post.id() + "; se omite este post.", e);
                continue;
            }

            String title = extractTitle(post.message());
            String summary = extractSummary(post.message());
            String slug = buildUniqueSlug(title, post.id());

            Promotion promotion = new Promotion(
                    null,
                    slug,
                    title,
                    "",
                    summary,
                    BigDecimal.ZERO,
                    null,
                    1,
                    0,
                    "Lima",
                    LocalDate.now(),
                    LocalDate.now().plusMonths(6),
                    uploadedMedia.id(),
                    false,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    null,
                    0,
                    false,
                    null,
                    null,
                    "FACEBOOK",
                    post.id(),
                    post.permalinkUrl()
            );

            promotionRepository.save(promotion);
            created++;
        }

        return created;
    }

    private byte[] downloadPhotoBytes(String photoUrl) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(photoUrl))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                LOG.error("Descarga de foto de Facebook respondió con estado inesperado: HTTP " + response.statusCode() + " (" + photoUrl + ")");
                return null;
            }
            return response.body();
        } catch (Exception e) {
            LOG.error("Error descargando la foto de Facebook: " + photoUrl, e);
            return null;
        }
    }

    private String extractTitle(String message) {
        if (message == null || message.isBlank()) {
            return DEFAULT_TITLE;
        }
        String firstLine = message.strip().split("\\R", 2)[0].strip();
        return firstLine.isBlank() ? DEFAULT_TITLE : firstLine;
    }

    private String extractSummary(String message) {
        if (message == null || message.isBlank()) {
            return "";
        }
        String[] parts = message.strip().split("\\R", 2);
        return parts.length > 1 ? parts[1].strip() : "";
    }

    private String buildUniqueSlug(String title, String facebookPostId) {
        String base = slugify(title);
        if (base.isBlank()) {
            base = "promocion-facebook";
        }

        String slug = base;
        if (promotionRepository.findBySlug(slug).isPresent()) {
            String shortSuffix = facebookPostId.length() > 6
                    ? facebookPostId.substring(facebookPostId.length() - 6)
                    : facebookPostId;
            slug = base + "-" + slugify(shortSuffix);
        }

        int attempt = 2;
        while (promotionRepository.findBySlug(slug).isPresent()) {
            slug = base + "-" + attempt;
            attempt++;
        }
        return slug;
    }

    private String slugify(String input) {
        if (input == null) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
