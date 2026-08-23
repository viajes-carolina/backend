package com.viajescarolina.api.promotions.domain;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository {

    List<Promotion> findFeatured();

    List<Promotion> findAllActive();

    List<Promotion> findAllPromotions();

    Optional<Promotion> findPromotionById(Long id);

    Optional<Promotion> findBySlug(String slug);

    Optional<Promotion> findByFacebookPostId(String facebookPostId);

    Promotion save(Promotion promotion);

    void delete(Long id);

    List<Long> findGalleryMediaIds(Long promotionId);

    void replaceGalleryMedia(Long promotionId, List<Long> mediaAssetIds);
}
