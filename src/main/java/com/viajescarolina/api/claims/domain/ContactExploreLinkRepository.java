package com.viajescarolina.api.claims.domain;

import java.util.List;
import java.util.Optional;

public interface ContactExploreLinkRepository {
    List<ContactExploreLink> findActiveOrdered();
    List<ContactExploreLink> findAllOrdered();
    Optional<ContactExploreLink> findLinkById(Long id);
    ContactExploreLink save(ContactExploreLink link);
    void deleteLinkById(Long id);
}
