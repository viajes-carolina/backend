package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.usecase.ListPublicBlogPostsUseCase;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.home.application.dto.HomeBlogInspirationDTO;
import com.viajescarolina.api.home.application.dto.PublicHomeBlogInspirationResponse;
import com.viajescarolina.api.home.domain.HomeBlogInspiration;
import com.viajescarolina.api.home.domain.HomeBlogInspirationRepository;
import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GetPublicHomeBlogInspirationUseCase {

    @Inject
    HomeBlogInspirationRepository inspirationRepository;

    @Inject
    BlogPostRepository blogPostRepository;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    TravelAdvisorRepository travelAdvisorRepository;

    public PublicHomeBlogInspirationResponse execute() {
        HomeBlogInspiration config = inspirationRepository.get().orElseGet(() -> new HomeBlogInspiration(
                1L,
                "Inspiración para tu viaje",
                "Consejos y guías",
                "para explorar el mundo",
                "Descubre recomendaciones de viaje, mejores temporadas, qué empacar y secretos locales de la mano de nuestras asesoras expertas.",
                "Ver todos los artículos del blog",
                "/blog",
                3,
                true
        ));

        int limit = config.getPostsLimit() != null && config.getPostsLimit() > 0 ? config.getPostsLimit() : 3;

        List<BlogPost> latestPosts = blogPostRepository.findPublicPosts(null, null, null, 0, limit);

        // Batch-resolve portadas y asesoras UNA sola vez para la sección de inspiración.
        Map<Long, MediaAsset> mediaById = ListPublicBlogPostsUseCase.resolveMediaMap(latestPosts, mediaRepository);
        Map<Long, TravelAdvisor> advisorById = ListPublicBlogPostsUseCase.resolveAdvisorMap(latestPosts, travelAdvisorRepository);

        List<BlogPostDTO> postDTOs = latestPosts.stream()
                .map(p -> ListPublicBlogPostsUseCase.mapToDTO(p, mediaById, advisorById, false))
                .toList();

        return new PublicHomeBlogInspirationResponse(HomeBlogInspirationDTO.fromDomain(config), postDTOs);
    }
}
