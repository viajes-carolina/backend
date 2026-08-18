package com.viajescarolina.api.promotions.domain;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository {

    List<Promotion> findFeatured();

    List<Promotion> findAllActive();

    List<Promotion> findAllPromotions();

    Optional<Promotion> findPromotionById(Long id);

    Optional<Promotion> findBySlug(String slug);

    Promotion save(Promotion promotion);

    void delete(Long id);
}
