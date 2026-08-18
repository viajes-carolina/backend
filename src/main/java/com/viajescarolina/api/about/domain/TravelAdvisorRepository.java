package com.viajescarolina.api.about.domain;

import java.util.List;
import java.util.Optional;

public interface TravelAdvisorRepository {
    List<TravelAdvisor> listPublicActive();
    List<TravelAdvisor> listAdminAll();
    Optional<TravelAdvisor> findAdvisorById(Long id);
    TravelAdvisor save(TravelAdvisor advisor);
    void delete(Long id);
}
