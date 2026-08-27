package com.viajescarolina.api.blog.domain;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository {
    List<BlogPost> findPublicPosts(String categorySlug, String search, Boolean isFeatured, int page, int size);
    long countPublicPosts(String categorySlug, String search, Boolean isFeatured);
    Optional<BlogPost> findPublicBySlug(String slug);
    List<BlogPost> findRelatedPosts(Long categoryId, Long excludePostId, int limit);
    List<BlogPost> findAllAdmin(String status, String search, int page, int size);
    long countAllAdmin(String status, String search);
    Optional<BlogPost> findPostById(Long id);
    BlogPost save(BlogPost post);
    void incrementViewCount(Long id);
    void delete(Long id);
    long countByAuthorAdvisorId(Long advisorId);
}
