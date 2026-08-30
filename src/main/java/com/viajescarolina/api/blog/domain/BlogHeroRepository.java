package com.viajescarolina.api.blog.domain;

import java.util.Optional;

public interface BlogHeroRepository {
    Optional<BlogHero> get();
    BlogHero save(BlogHero hero);
}
