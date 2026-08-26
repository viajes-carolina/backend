package com.viajescarolina.api.media.domain;

import java.util.Optional;

/**
 * Resuelve la tripleta (mediaId, url almacenada, focalX/Y almacenados) a un valor final
 * listo para exponer al frontend público: si ya hay una URL almacenada, se respeta tal
 * cual; si no, y hay un mediaId, se busca el MediaAsset y se usa su storagePath/focal.
 * El focal cae a 50.0 (centro) cuando no hay ningún valor disponible.
 *
 * Centraliza un patrón que estaba duplicado 7 veces entre GetPublicHomeHeroUseCase
 * (fondo + 3 fotos secundarias), GetPublicHomePromotionsSectionUseCase y
 * GetPublicHomeTestimonialsSectionUseCase.
 *
 * Cualquier regla adicional específica de un caso de uso concreto (p.ej. el fallback a
 * una foto demo cuando el fondo del Hero sigue vacío tras resolver) queda fuera de este
 * helper y se aplica después de invocarlo, en el propio caso de uso.
 */
public final class MediaResolver {

    private static final double DEFAULT_FOCAL = 50.0;

    private MediaResolver() {
    }

    public record ResolvedMedia(String url, double focalX, double focalY) {
    }

    public static ResolvedMedia resolve(
            Long mediaId,
            String storedUrl,
            Double storedFocalX,
            Double storedFocalY,
            MediaRepository mediaRepository
    ) {
        String url = storedUrl;
        double focalX = storedFocalX != null ? storedFocalX : DEFAULT_FOCAL;
        double focalY = storedFocalY != null ? storedFocalY : DEFAULT_FOCAL;

        if ((url == null || url.isBlank()) && mediaId != null) {
            Optional<MediaAsset> asset = mediaRepository.findMediaById(mediaId);
            if (asset.isPresent()) {
                url = asset.get().getStoragePath();
                if (asset.get().getFocalX() != null) {
                    focalX = asset.get().getFocalX().doubleValue();
                }
                if (asset.get().getFocalY() != null) {
                    focalY = asset.get().getFocalY().doubleValue();
                }
            }
        }

        return new ResolvedMedia(url, focalX, focalY);
    }
}
