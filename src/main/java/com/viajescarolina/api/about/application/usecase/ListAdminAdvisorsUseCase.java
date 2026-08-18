package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.application.dto.TravelAdvisorDTO;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ListAdminAdvisorsUseCase {
    private final TravelAdvisorRepository advisorRepository;
    private final GetPublicAboutUseCase getPublicAboutUseCase;

    public ListAdminAdvisorsUseCase(TravelAdvisorRepository advisorRepository, GetPublicAboutUseCase getPublicAboutUseCase) {
        this.advisorRepository = advisorRepository;
        this.getPublicAboutUseCase = getPublicAboutUseCase;
    }

    public List<TravelAdvisorDTO> execute() {
        return advisorRepository.listAdminAll().stream()
            .map(getPublicAboutUseCase::toAdvisorDTO)
            .toList();
    }
}
