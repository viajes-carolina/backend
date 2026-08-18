package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.trust.application.dto.TestimonialDTO;
import com.viajescarolina.api.trust.domain.TestimonialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ListAdminTestimonialsUseCase {

    private final TestimonialRepository testimonialRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public ListAdminTestimonialsUseCase(
            TestimonialRepository testimonialRepository,
            MediaRepository mediaRepository) {
        this.testimonialRepository = testimonialRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<TestimonialDTO> execute() {
        return testimonialRepository.findAllTestimonials().stream()
                .map(t -> GetPublicTrustUseCase.mapTestimonialToDTO(t, mediaRepository))
                .toList();
    }
}
