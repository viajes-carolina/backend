package com.viajescarolina.api.intentions.domain;

import java.util.List;
import java.util.Optional;

public interface TravelIntentionRepository {

    List<TravelIntention> findAllActive();

    List<TravelIntention> findAllIntentions();

    Optional<TravelIntention> findIntentionById(Long id);

    Optional<TravelIntention> findBySlug(String slug);

    TravelIntention save(TravelIntention travelIntention);

    void delete(Long id);
}
