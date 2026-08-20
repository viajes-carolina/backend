package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.application.dto.CreateOrUpdateAdvisorRequest;
import com.viajescarolina.api.about.application.dto.TravelAdvisorDTO;
import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;

@ApplicationScoped
public class CreateAdvisorUseCase {
    private final TravelAdvisorRepository advisorRepository;
    private final GetPublicAboutUseCase getPublicAboutUseCase;

    public CreateAdvisorUseCase(TravelAdvisorRepository advisorRepository, GetPublicAboutUseCase getPublicAboutUseCase) {
        this.advisorRepository = advisorRepository;
        this.getPublicAboutUseCase = getPublicAboutUseCase;
    }

    @Audited(action = "CREATE_ADVISOR", entityType = "ADVISOR")
    @Transactional
    public TravelAdvisorDTO execute(CreateOrUpdateAdvisorRequest req) {
        TravelAdvisor advisor = new TravelAdvisor();
        advisor.setFullName(req.fullName());
        advisor.setRoleTitle(req.roleTitle());
        advisor.setSpecialty(req.specialty());
        advisor.setBio(req.bio());
        advisor.setPhotoMediaId(req.photoMediaId());
        advisor.setWhatsappPhone(req.whatsappPhone());
        advisor.setWhatsappMessageTemplate(req.whatsappMessageTemplate());
        advisor.setDisplayOrder(req.displayOrder() != null ? req.displayOrder() : 0);
        advisor.setActive(req.active() != null ? req.active() : true);
        advisor.setCreatedAt(Instant.now());
        advisor.setUpdatedAt(Instant.now());

        TravelAdvisor saved = advisorRepository.save(advisor);
        return getPublicAboutUseCase.toAdvisorDTO(saved);
    }
}
