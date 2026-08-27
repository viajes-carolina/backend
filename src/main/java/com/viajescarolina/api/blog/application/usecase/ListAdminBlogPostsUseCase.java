package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ListAdminBlogPostsUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    TravelAdvisorRepository travelAdvisorRepository;

    public List<BlogPostDTO> execute(String status, String search, int page, int size) {
        List<BlogPost> posts = postRepository.findAllAdmin(status, search, page, size);

        // Batch-resolve portadas y asesoras UNA sola vez para todo el listado admin.
        Map<Long, MediaAsset> mediaById = ListPublicBlogPostsUseCase.resolveMediaMap(posts, mediaRepository);
        Map<Long, TravelAdvisor> advisorById = ListPublicBlogPostsUseCase.resolveAdvisorMap(posts, travelAdvisorRepository);

        // El admin no tiene un endpoint de detalle por id: BlogFormModal precarga
        // el formulario de "Editar" directamente desde esta misma lista, así que
        // el markdown completo debe venir siempre incluido aquí (a diferencia del
        // listado público, que sí puede omitirlo porque nunca lo renderiza).
        return posts.stream().map(p -> ListPublicBlogPostsUseCase.mapToDTO(p, mediaById, advisorById, true)).toList();
    }
}
