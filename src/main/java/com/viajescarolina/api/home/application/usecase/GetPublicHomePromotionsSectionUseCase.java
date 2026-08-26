package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomePromotionsSectionDTO;
import com.viajescarolina.api.home.domain.HomePromotionsSection;
import com.viajescarolina.api.home.domain.HomePromotionsSectionRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class GetPublicHomePromotionsSectionUseCase {

    @Inject
    HomePromotionsSectionRepository promotionsSectionRepository;

    @Inject
    MediaRepository mediaRepository;

    public HomePromotionsSectionDTO execute() {
        HomePromotionsSection config = promotionsSectionRepository.get().orElseGet(() -> {
            HomePromotionsSection fallback = new HomePromotionsSection(
                    1L,
                    "02 · Viajes para empezar a imaginar",
                    "Algunas formas de vivir tu próximo viaje",
                    "Experiencias que podemos ajustar a tus tiempos, compañía y presupuesto.",
                    "¿Cuál de estos viajes te gustaría vivir?",
                    "Cuéntanos cuál te gustó",
                    "Hola Viajes Carolina, me gustaría conversar sobre una de sus promociones."
            );
            fallback.setBottomCtaEyebrow("SI NINGUNO ENCAJA EXACTAMENTE");
            fallback.setBottomCtaCopy("Fechas, presupuesto y tipo de viaje: una asesora prepara opciones reales para ti.");
            return fallback;
        });

        return mapToDTO(config, mediaRepository);
    }

    public static HomePromotionsSectionDTO mapToDTO(HomePromotionsSection config, MediaRepository mediaRepository) {
        String mediaUrl = config.getMediaUrl();
        Double focalX = config.getMediaFocalX() != null ? config.getMediaFocalX() : 50.0;
        Double focalY = config.getMediaFocalY() != null ? config.getMediaFocalY() : 50.0;

        if ((mediaUrl == null || mediaUrl.isBlank()) && config.getMediaId() != null) {
            Optional<MediaAsset> asset = mediaRepository.findMediaById(config.getMediaId());
            if (asset.isPresent()) {
                mediaUrl = asset.get().getStoragePath();
                if (asset.get().getFocalX() != null) {
                    focalX = asset.get().getFocalX().doubleValue();
                }
                if (asset.get().getFocalY() != null) {
                    focalY = asset.get().getFocalY().doubleValue();
                }
            }
        }
        // Sin fallback a foto demo: vacío se queda vacío, el frontend público
        // pinta un placeholder de gradiente cuando no hay imageUrl.

        return new HomePromotionsSectionDTO(
                config.getId(),
                config.getBadgeText(),
                config.getTitle(),
                config.getSubtitle(),
                config.getBottomCtaQuestion(),
                config.getBottomCtaEyebrow(),
                config.getBottomCtaCopy(),
                config.getBottomCtaWhatsappText(),
                config.getBottomCtaWhatsappMessage(),
                config.getMediaId(),
                mediaUrl,
                focalX,
                focalY
        );
    }
}
