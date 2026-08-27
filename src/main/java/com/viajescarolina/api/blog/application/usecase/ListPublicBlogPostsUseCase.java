package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogCategoryDTO;
import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.dto.PublicBlogResponse;
import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class ListPublicBlogPostsUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    BlogCategoryRepository categoryRepository;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    TravelAdvisorRepository travelAdvisorRepository;

    public PublicBlogResponse execute(String categorySlug, String search, int page, int size) {
        List<BlogPost> posts = postRepository.findPublicPosts(categorySlug, search, null, page, size);
        long total = postRepository.countPublicPosts(categorySlug, search, null);
        int totalPages = (int) Math.ceil((double) total / (size > 0 ? size : 10));

        // Get featured post if no specific search/filter is active
        List<BlogPost> featuredList = List.of();
        if ((categorySlug == null || categorySlug.isBlank()) && (search == null || search.isBlank())) {
            featuredList = postRepository.findPublicPosts(null, null, true, 0, 1);
        }

        // Batch-resolve portadas y asesoras UNA sola vez para el listado + el destacado,
        // en vez de una query por cada post dentro del .map() (evita el N+1).
        List<BlogPost> allPostsForBatch = new ArrayList<>(posts);
        allPostsForBatch.addAll(featuredList);
        Map<Long, MediaAsset> mediaById = resolveMediaMap(allPostsForBatch, mediaRepository);
        Map<Long, TravelAdvisor> advisorById = resolveAdvisorMap(allPostsForBatch, travelAdvisorRepository);

        List<BlogPostDTO> postDTOs = posts.stream().map(p -> mapToDTO(p, mediaById, advisorById, false)).toList();

        List<BlogCategory> categories = categoryRepository.findAllActive();
        List<BlogCategoryDTO> categoryDTOs = categories.stream().map(this::toCategoryDTO).toList();

        BlogPostDTO featuredDTO = featuredList.isEmpty() ? null : mapToDTO(featuredList.getFirst(), mediaById, advisorById, false);

        return new PublicBlogResponse(postDTOs, categoryDTOs, featuredDTO, total, page, size, totalPages);
    }

    /**
     * Recolecta los {@code coverMediaId} (no nulos) de una lista de posts y los resuelve
     * en una sola consulta batch. Pensado para llamarse una vez antes de mapear una lista
     * completa de posts a DTO, nunca dentro de un loop por post.
     */
    public static Map<Long, MediaAsset> resolveMediaMap(List<BlogPost> posts, MediaRepository mediaRepository) {
        Set<Long> coverMediaIds = posts.stream()
                .map(BlogPost::getCoverMediaId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (coverMediaIds.isEmpty()) {
            return Map.of();
        }
        return mediaRepository.findMediaByIds(coverMediaIds).stream()
                .collect(Collectors.toMap(MediaAsset::getId, Function.identity()));
    }

    /**
     * Recolecta los {@code authorAdvisorId} (no nulos) de una lista de posts y los resuelve
     * en una sola consulta batch (que ya trae la foto de cada asesora resuelta). Pensado
     * para llamarse una vez antes de mapear una lista completa de posts a DTO.
     */
    public static Map<Long, TravelAdvisor> resolveAdvisorMap(List<BlogPost> posts, TravelAdvisorRepository travelAdvisorRepository) {
        Set<Long> advisorIds = posts.stream()
                .map(BlogPost::getAuthorAdvisorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (advisorIds.isEmpty()) {
            return Map.of();
        }
        return travelAdvisorRepository.findAdvisorsByIds(advisorIds).stream()
                .collect(Collectors.toMap(TravelAdvisor::getId, Function.identity()));
    }

    /**
     * Mapea un {@link BlogPost} de dominio a su DTO usando los mapas de medios y asesoras
     * YA resueltos en batch (ver {@link #resolveMediaMap} / {@link #resolveAdvisorMap}).
     * Este método permanece estático y puro: solo hace lookups en memoria, sin acceso a BD.
     *
     * @param includeBody si es {@code false}, fuerza {@code contentMarkdown = null} en el DTO
     *                    resultante (contextos de listado/tarjeta); si es {@code true}, conserva
     *                    el markdown completo (solo el post principal de detalle).
     */
    public static BlogPostDTO mapToDTO(BlogPost p, Map<Long, MediaAsset> mediaById, Map<Long, TravelAdvisor> advisorById, boolean includeBody) {
        String coverUrl = p.getCoverMediaId() != null
                ? Optional.ofNullable(mediaById.get(p.getCoverMediaId())).map(MediaAsset::getStoragePath).orElse(null)
                : null;

        String authorName = null;
        String authorAvatarUrl = null;
        if (p.getAuthorAdvisorId() != null) {
            TravelAdvisor advisor = advisorById.get(p.getAuthorAdvisorId());
            if (advisor != null) {
                authorName = advisor.getFullName();
                authorAvatarUrl = advisor.getPhotoMediaUrl();
            }
        }

        return new BlogPostDTO(
                p.getId(), p.getSlug(), p.getTitle(), p.getSummary(), includeBody ? p.getContentMarkdown() : null,
                p.getCategoryId(), p.getCategoryName(), p.getCategorySlug(),
                p.getCoverMediaId(), coverUrl,
                p.getCoverFocalX() != null ? p.getCoverFocalX() : 50.0,
                p.getCoverFocalY() != null ? p.getCoverFocalY() : 50.0,
                p.getAuthorAdvisorId(), authorName, authorAvatarUrl,
                p.getReadingTimeMinutes(), p.getTags(), p.getStatus(), p.getPublishedAt(),
                p.getViewCount(), p.getIsFeatured(), p.getActive(), p.getCreatedAt(), p.getUpdatedAt()
        );
    }

    private BlogCategoryDTO toCategoryDTO(BlogCategory c) {
        return new BlogCategoryDTO(
                c.getId(),
                c.getName(),
                c.getSlug(),
                c.getDescription(),
                c.getDisplayOrder(),
                c.getActive(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
