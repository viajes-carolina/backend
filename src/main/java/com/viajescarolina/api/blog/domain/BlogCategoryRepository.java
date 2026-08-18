package com.viajescarolina.api.blog.domain;

import java.util.List;
import java.util.Optional;

public interface BlogCategoryRepository {
    List<BlogCategory> findAllActive();
    List<BlogCategory> findAllAdmin();
    Optional<BlogCategory> findCategoryById(Long id);
    Optional<BlogCategory> findBySlug(String slug);
    BlogCategory save(BlogCategory category);
    void delete(Long id);
}
