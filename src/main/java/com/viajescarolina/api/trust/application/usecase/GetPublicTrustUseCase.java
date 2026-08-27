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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        List<Testimonial> activeTestimonials = testimonialRepository.findAllActive();

        // Batch-resolve de avatares UNA sola vez para todos los testimonios, en vez de
        // una query por testimonio dentro del .map() (evita el N+1).
        Map<Long, MediaAsset> avatarMediaById = resolveAvatarMediaMap(activeTestimonials, mediaRepository);

        List<TestimonialDTO> testimonials = activeTestimonials.stream()
                .map(t -> mapTestimonialToDTO(t, avatarMediaById))
                .toList();

        List<FaqItemDTO> faqs = faqRepository.findAllActive().stream()
                .map(GetPublicTrustUseCase::mapFaqToDTO)
                .toList();

        return new PublicTrustResponse(testimonials, faqs);
    }

    /**
     * Recolecta los {@code avatarMediaId} (no nulos) de una lista de testimonios y los
     * resuelve en una sola consulta batch. Pensado para llamarse una vez antes de mapear
     * una lista completa de testimonios a DTO, nunca dentro de un loop por testimonio.
     */
    public static Map<Long, MediaAsset> resolveAvatarMediaMap(List<Testimonial> testimonials, MediaRepository mediaRepository) {
        Set<Long> avatarMediaIds = testimonials.stream()
                .map(Testimonial::getAvatarMediaId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (avatarMediaIds.isEmpty()) {
            return Map.of();
        }
        return mediaRepository.findMediaByIds(avatarMediaIds).stream()
                .collect(Collectors.toMap(MediaAsset::getId, Function.identity()));
    }

    public static TestimonialDTO mapTestimonialToDTO(Testimonial testimonial, Map<Long, MediaAsset> avatarMediaById) {
        String avatarUrl = testimonial.getAvatarMediaId() != null
                ? Optional.ofNullable(avatarMediaById.get(testimonial.getAvatarMediaId())).map(MediaAsset::getStoragePath).orElse(null)
                : null;

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
