package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@ApplicationScoped
public class DeleteAdvisorUseCase {
    private final TravelAdvisorRepository advisorRepository;
    private final BlogPostRepository blogPostRepository;

    public DeleteAdvisorUseCase(TravelAdvisorRepository advisorRepository, BlogPostRepository blogPostRepository) {
        this.advisorRepository = advisorRepository;
        this.blogPostRepository = blogPostRepository;
    }

    @Audited(action = "DELETE_ADVISOR", entityType = "ADVISOR")
    @Transactional
    public void execute(Long id) {
        long associatedPosts = blogPostRepository.countByAuthorAdvisorId(id);
        if (associatedPosts > 0) {
            throw new WebApplicationException(
                    Response.status(Response.Status.CONFLICT)
                            .entity(Map.of("message", "No se puede eliminar la asesora: tiene " + associatedPosts
                                    + " artículo(s) del blog asociado(s)."))
                            .type(MediaType.APPLICATION_JSON)
                            .build());
        }

        advisorRepository.delete(id);
    }
}
