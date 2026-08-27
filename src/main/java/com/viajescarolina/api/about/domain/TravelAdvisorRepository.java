package com.viajescarolina.api.about.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TravelAdvisorRepository {
    List<TravelAdvisor> listPublicActive();
    List<TravelAdvisor> listAdminAll();
    Optional<TravelAdvisor> findAdvisorById(Long id);

    /**
     * Resuelve varias asesoras (con su foto ya incluida) en una sola consulta
     * batch más una única consulta batch adicional a medios, evitando repetir
     * el patrón {@code enrichWithPhoto} una vez por asesora dentro de un loop.
     */
    List<TravelAdvisor> findAdvisorsByIds(Collection<Long> ids);

    TravelAdvisor save(TravelAdvisor advisor);
    void delete(Long id);
}
