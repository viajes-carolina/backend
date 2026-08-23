package com.viajescarolina.api.home.domain;

import java.util.Optional;

public interface HomePromotionsSectionRepository {
    Optional<HomePromotionsSection> get();
    HomePromotionsSection save(HomePromotionsSection section);
}
