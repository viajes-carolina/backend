package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeTestimonialsSectionDTO;
import com.viajescarolina.api.home.domain.HomeTestimonialsSection;
import com.viajescarolina.api.home.domain.HomeTestimonialsSectionRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateHomeTestimonialsSectionUseCase {

    @Inject
    HomeTestimonialsSectionRepository testimonialsSectionRepository;

    @Inject
    MediaRepository mediaRepository;

    @Audited(action = "UPDATE_HOME_TESTIMONIALS_SECTION", entityType = "HOME_TESTIMONIALS_SECTION")
    @Transactional
    public HomeTestimonialsSectionDTO execute(HomeTestimonialsSectionDTO dto) {
        HomeTestimonialsSection entity = testimonialsSectionRepository.get().orElseGet(() -> new HomeTestimonialsSection(
                1L,
                "05 · Historias reales",
                "Viajes que hoy se recuerdan así",
                "Cada fotografía guarda una experiencia que comenzó con una conversación."
        ));

        if (dto.badgeText() != null) entity.setBadgeText(dto.badgeText());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.subtitle() != null) entity.setSubtitle(dto.subtitle());

        if (dto.blobMediaId() != null) {
            Long validId = dto.blobMediaId() > 0 && mediaRepository.findMediaById(dto.blobMediaId()).isPresent()
                    ? dto.blobMediaId() : null;
            entity.setBlobMediaId(validId);
        }
        if (dto.blobMediaUrl() != null && !dto.blobMediaUrl().isBlank()) entity.setBlobMediaUrl(dto.blobMediaUrl());
        if (dto.blobFocalX() != null) entity.setBlobFocalX(dto.blobFocalX());
        if (dto.blobFocalY() != null) entity.setBlobFocalY(dto.blobFocalY());

        if (dto.polaroidMediaId() != null) {
            Long validId = dto.polaroidMediaId() > 0 && mediaRepository.findMediaById(dto.polaroidMediaId()).isPresent()
                    ? dto.polaroidMediaId() : null;
            entity.setPolaroidMediaId(validId);
        }
        if (dto.polaroidMediaUrl() != null && !dto.polaroidMediaUrl().isBlank()) entity.setPolaroidMediaUrl(dto.polaroidMediaUrl());
        if (dto.polaroidFocalX() != null) entity.setPolaroidFocalX(dto.polaroidFocalX());
        if (dto.polaroidFocalY() != null) entity.setPolaroidFocalY(dto.polaroidFocalY());

        HomeTestimonialsSection saved = testimonialsSectionRepository.save(entity);
        return GetPublicHomeTestimonialsSectionUseCase.mapToDTO(saved, mediaRepository);
    }
}
