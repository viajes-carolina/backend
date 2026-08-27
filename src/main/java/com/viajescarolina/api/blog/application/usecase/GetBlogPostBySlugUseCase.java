package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.dto.BlogPostDetailResponse;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GetBlogPostBySlugUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    TravelAdvisorRepository travelAdvisorRepository;

    public BlogPostDetailResponse execute(String slug) {
        BlogPost post = postRepository.findPublicBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Artículo de blog no encontrado: " + slug));

        // Increment view count
        postRepository.incrementViewCount(post.getId());
        post.setViewCount(post.getViewCount() + 1);

        // Related posts in the same category
        List<BlogPost> related = postRepository.findRelatedPosts(post.getCategoryId(), post.getId(), 3);

        // Batch-resolve portadas y asesoras UNA sola vez para el post principal + relacionados,
        // en vez de una query por cada uno dentro del .map() (evita el N+1).
        List<BlogPost> allPostsForBatch = new ArrayList<>(related);
        allPostsForBatch.add(post);
        Map<Long, MediaAsset> mediaById = ListPublicBlogPostsUseCase.resolveMediaMap(allPostsForBatch, mediaRepository);
        Map<Long, TravelAdvisor> advisorById = ListPublicBlogPostsUseCase.resolveAdvisorMap(allPostsForBatch, travelAdvisorRepository);

        List<BlogPostDTO> relatedDTOs = related.stream()
                .map(p -> ListPublicBlogPostsUseCase.mapToDTO(p, mediaById, advisorById, false))
                .toList();

        // Solo el post principal de detalle lleva el contentMarkdown completo.
        BlogPostDTO mainDTO = ListPublicBlogPostsUseCase.mapToDTO(post, mediaById, advisorById, true);

        return new BlogPostDetailResponse(mainDTO, relatedDTOs);
    }
}
