package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.trust.application.dto.FaqItemDTO;
import com.viajescarolina.api.trust.application.dto.PublicTrustResponse;
import com.viajescarolina.api.trust.application.dto.TestimonialDTO;
import com.viajescarolina.api.trust.domain.FaqItem;
import com.viajescarolina.api.trust.domain.FaqRepository;
import com.viajescarolina.api.trust.domain.Testimonial;
import com.viajescarolina.api.trust.domain.TestimonialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GetPublicTrustUseCase {

    private final TestimonialRepository testimonialRepository;
    private final FaqRepository faqRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public GetPublicTrustUseCase(
            TestimonialRepository testimonialRepository,
            FaqRepository faqRepository,
            MediaRepository mediaRepository) {
        this.testimonialRepository = testimonialRepository;
        this.faqRepository = faqRepository;
        this.mediaRepository = mediaRepository;
    }

    public PublicTrustResponse execute() {
        List<TestimonialDTO> testimonials = testimonialRepository.findAllActive().stream()
                .map(t -> mapTestimonialToDTO(t, mediaRepository))
                .toList();

        List<FaqItemDTO> faqs = faqRepository.findAllActive().stream()
                .map(GetPublicTrustUseCase::mapFaqToDTO)
                .toList();

        return new PublicTrustResponse(testimonials, faqs);
    }

    public static TestimonialDTO mapTestimonialToDTO(Testimonial testimonial, MediaRepository mediaRepository) {
        String avatarUrl = null;
        if (testimonial.getAvatarMediaId() != null) {
            Optional<MediaAsset> asset = mediaRepository.findMediaById(testimonial.getAvatarMediaId());
            if (asset.isPresent()) {
                avatarUrl = asset.get().getStoragePath();
            }
        }

        return new TestimonialDTO(
                testimonial.getId(),
                testimonial.getClientName(),
                testimonial.getClientLocation(),
                testimonial.getTripDestination(),
                testimonial.getComment(),
                testimonial.getRating(),
                testimonial.getAvatarMediaId(),
                avatarUrl,
                testimonial.isConsentConfirmed(),
                testimonial.getDisplayOrder(),
                testimonial.isActive(),
                testimonial.getCreatedAt(),
                testimonial.getUpdatedAt()
        );
    }

    public static FaqItemDTO mapFaqToDTO(FaqItem faq) {
        return new FaqItemDTO(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getCategory(),
                faq.getDisplayOrder(),
                faq.isActive(),
                faq.getCreatedAt(),
                faq.getUpdatedAt()
        );
    }
}
