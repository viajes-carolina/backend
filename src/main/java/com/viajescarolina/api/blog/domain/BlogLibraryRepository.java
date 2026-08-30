package com.viajescarolina.api.blog.domain;

import java.util.Optional;

public interface BlogLibraryRepository {
    Optional<BlogLibrary> get();
    BlogLibrary save(BlogLibrary library);
}
