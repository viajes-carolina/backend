package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeTestimonialsSectionDTO;
import com.viajescarolina.api.home.domain.HomeTestimonialsSection;
import com.viajescarolina.api.home.domain.HomeTestimonialsSectionRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class GetPublicHomeTestimonialsSectionUseCase {

    @Inject
    HomeTestimonialsSectionRepository testimonialsSectionRepository;

    @Inject
    MediaRepository mediaRepository;

    public HomeTestimonialsSectionDTO execute() {
        HomeTestimonialsSection config = testimonialsSectionRepository.get().orElseGet(() -> new HomeTestimonialsSection(
                1L,
                "05 · Historias reales",
                "Viajes que hoy se recuerdan así",
                "Cada fotografía guarda una experiencia que comenzó con una conversación."
        ));

        return mapToDTO(config, mediaRepository);
    }

    public static HomeTestimonialsSectionDTO mapToDTO(HomeTestimonialsSection config, MediaRepository mediaRepository) {
        String blobUrl = config.getBlobMediaUrl();
        Double blobFocalX = config.getBlobFocalX() != null ? config.getBlobFocalX() : 50.0;
        Double blobFocalY = config.getBlobFocalY() != null ? config.getBlobFocalY() : 50.0;
        if ((blobUrl == null || blobUrl.isBlank()) && config.getBlobMediaId() != null) {
            Optional<MediaAsset> asset = mediaRepository.findMediaById(config.getBlobMediaId());
            if (asset.isPresent()) {
                blobUrl = asset.get().getStoragePath();
                if (asset.get().getFocalX() != null) blobFocalX = asset.get().getFocalX().doubleValue();
                if (asset.get().getFocalY() != null) blobFocalY = asset.get().getFocalY().doubleValue();
            }
        }

        String polaroidUrl = config.getPolaroidMediaUrl();
        Double polaroidFocalX = config.getPolaroidFocalX() != null ? config.getPolaroidFocalX() : 50.0;
        Double polaroidFocalY = config.getPolaroidFocalY() != null ? config.getPolaroidFocalY() : 50.0;
        if ((polaroidUrl == null || polaroidUrl.isBlank()) && config.getPolaroidMediaId() != null) {
            Optional<MediaAsset> asset = mediaRepository.findMediaById(config.getPolaroidMediaId());
            if (asset.isPresent()) {
                polaroidUrl = asset.get().getStoragePath();
                if (asset.get().getFocalX() != null) polaroidFocalX = asset.get().getFocalX().doubleValue();
                if (asset.get().getFocalY() != null) polaroidFocalY = asset.get().getFocalY().doubleValue();
            }
        }
        // Sin fallback a foto demo: vacío se queda vacío, el frontend público ya
        // sabe pintar su placeholder de gradiente cuando no hay imageUrl.

        return new HomeTestimonialsSectionDTO(
                config.getId(),
                config.getBadgeText(),
                config.getTitle(),
                config.getSubtitle(),
                config.getBlobMediaId(),
                blobUrl,
                blobFocalX,
                blobFocalY,
                config.getPolaroidMediaId(),
                polaroidUrl,
                polaroidFocalX,
                polaroidFocalY
        );
    }
}
