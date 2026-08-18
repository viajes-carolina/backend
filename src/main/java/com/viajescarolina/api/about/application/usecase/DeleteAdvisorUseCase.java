package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DeleteAdvisorUseCase {
    private final TravelAdvisorRepository advisorRepository;

    public DeleteAdvisorUseCase(TravelAdvisorRepository advisorRepository) {
        this.advisorRepository = advisorRepository;
    }

    @Transactional
    public void execute(Long id) {
        advisorRepository.delete(id);
    }
}
