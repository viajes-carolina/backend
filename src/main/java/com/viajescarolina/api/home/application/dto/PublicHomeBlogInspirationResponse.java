package com.viajescarolina.api.home.application.dto;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import java.util.List;

public record PublicHomeBlogInspirationResponse(
        HomeBlogInspirationDTO config,
        List<BlogPostDTO> posts
) {
}
