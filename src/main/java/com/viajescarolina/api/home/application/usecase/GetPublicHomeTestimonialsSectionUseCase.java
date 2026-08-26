package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeTestimonialsSectionDTO;
import com.viajescarolina.api.home.domain.HomeTestimonialsSection;
import com.viajescarolina.api.home.domain.HomeTestimonialsSectionRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.media.domain.MediaResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
        MediaResolver.ResolvedMedia blob = MediaResolver.resolve(
                config.getBlobMediaId(),
                config.getBlobMediaUrl(),
                config.getBlobFocalX(),
                config.getBlobFocalY(),
                mediaRepository
        );
        // Sin fallback a foto demo: vacío se queda vacío, el frontend público ya
        // sabe pintar su placeholder de gradiente cuando no hay imageUrl.

        return new HomeTestimonialsSectionDTO(
                config.getId(),
                config.getBadgeText(),
                config.getTitle(),
                config.getSubtitle(),
                config.getBlobMediaId(),
                blob.url(),
                blob.focalX(),
                blob.focalY()
        );
    }
}
