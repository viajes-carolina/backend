package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.trust.application.dto.CreateOrUpdateTestimonialRequest;
import com.viajescarolina.api.trust.application.dto.TestimonialDTO;
import com.viajescarolina.api.trust.domain.Testimonial;
import com.viajescarolina.api.trust.domain.TestimonialRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UpdateTestimonialUseCase {

    private final TestimonialRepository testimonialRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public UpdateTestimonialUseCase(
            TestimonialRepository testimonialRepository,
            MediaRepository mediaRepository) {
        this.testimonialRepository = testimonialRepository;
        this.mediaRepository = mediaRepository;
    }

    @Audited(action = "UPDATE_TESTIMONIAL", entityType = "TESTIMONIAL")
    @Transactional
    public TestimonialDTO execute(Long id, CreateOrUpdateTestimonialRequest request) {
        Testimonial testimonial = testimonialRepository.findTestimonialById(id)
                .orElseThrow(() -> new NotFoundException("Testimonio no encontrado con ID: " + id));

        testimonial.update(
                request.clientName(),
                request.clientLocation(),
                request.tripDestination(),
                request.comment(),
                request.rating(),
                request.avatarMediaId(),
                request.consentConfirmed() != null ? request.consentConfirmed() : testimonial.isConsentConfirmed(),
                request.displayOrder(),
                request.active() != null ? request.active() : testimonial.isActive()
        );

        Testimonial saved = testimonialRepository.save(testimonial);

        // Testimonio único (respuesta de actualización, no un listado): mismo contrato
        // batch con una lista de un solo elemento.
        Map<Long, MediaAsset> avatarMediaById = GetPublicTrustUseCase.resolveAvatarMediaMap(List.of(saved), mediaRepository);
        return GetPublicTrustUseCase.mapTestimonialToDTO(saved, avatarMediaById);
    }
}
