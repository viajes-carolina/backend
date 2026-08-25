package com.viajescarolina.api.promotions.domain;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository {

    List<Promotion> findTopActiveByRecency(int limit);

    List<Promotion> findAllPromotions();

    long countActive();

    Optional<Promotion> findPromotionById(Long id);

    Optional<Promotion> findBySlug(String slug);

    Optional<Promotion> findByFacebookPostId(String facebookPostId);

    Promotion save(Promotion promotion);

    void delete(Long id);
}
