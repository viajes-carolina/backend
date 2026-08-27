package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.trust.application.dto.TestimonialDTO;
import com.viajescarolina.api.trust.domain.Testimonial;
import com.viajescarolina.api.trust.domain.TestimonialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

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
        List<Testimonial> allTestimonials = testimonialRepository.findAllTestimonials();

        // Batch-resolve de avatares UNA sola vez para todo el listado admin.
        Map<Long, MediaAsset> avatarMediaById = GetPublicTrustUseCase.resolveAvatarMediaMap(allTestimonials, mediaRepository);

        return allTestimonials.stream()
                .map(t -> GetPublicTrustUseCase.mapTestimonialToDTO(t, avatarMediaById))
                .toList();
    }
}
