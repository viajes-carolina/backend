package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DeleteAdvisorUseCase {
    private final TravelAdvisorRepository advisorRepository;

    public DeleteAdvisorUseCase(TravelAdvisorRepository advisorRepository) {
        this.advisorRepository = advisorRepository;
    }

    @Audited(action = "DELETE_ADVISOR", entityType = "ADVISOR")
    @Transactional
    public void execute(Long id) {
        advisorRepository.delete(id);
    }
}
