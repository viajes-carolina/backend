package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.trust.application.dto.CreateOrUpdateTestimonialRequest;
import com.viajescarolina.api.trust.application.dto.TestimonialDTO;
import com.viajescarolina.api.trust.domain.Testimonial;
import com.viajescarolina.api.trust.domain.TestimonialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateTestimonialUseCase {

    private final TestimonialRepository testimonialRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public CreateTestimonialUseCase(
            TestimonialRepository testimonialRepository,
            MediaRepository mediaRepository) {
        this.testimonialRepository = testimonialRepository;
        this.mediaRepository = mediaRepository;
    }

    @Transactional
    public TestimonialDTO execute(CreateOrUpdateTestimonialRequest request) {
        Testimonial testimonial = Testimonial.create(
                request.clientName(),
                request.clientLocation(),
                request.tripDestination(),
                request.comment(),
                request.rating(),
                request.avatarMediaId(),
                request.consentConfirmed() != null ? request.consentConfirmed() : true,
                request.displayOrder()
        );

        Testimonial saved = testimonialRepository.save(testimonial);
        return GetPublicTrustUseCase.mapTestimonialToDTO(saved, mediaRepository);
    }
}
