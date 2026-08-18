package com.viajescarolina.api.blog.application.dto;

import java.util.List;

public record BlogPostDetailResponse(
        BlogPostDTO post,
        List<BlogPostDTO> relatedPosts) {
}
