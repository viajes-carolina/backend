package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.application.dto.CreateOrUpdateAdvisorRequest;
import com.viajescarolina.api.about.application.dto.TravelAdvisorDTO;
import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.Instant;

@ApplicationScoped
public class UpdateAdvisorUseCase {
    private final TravelAdvisorRepository advisorRepository;
    private final GetPublicAboutUseCase getPublicAboutUseCase;

    public UpdateAdvisorUseCase(TravelAdvisorRepository advisorRepository, GetPublicAboutUseCase getPublicAboutUseCase) {
        this.advisorRepository = advisorRepository;
        this.getPublicAboutUseCase = getPublicAboutUseCase;
    }

    @Transactional
    public TravelAdvisorDTO execute(Long id, CreateOrUpdateAdvisorRequest req) {
        TravelAdvisor advisor = advisorRepository.findAdvisorById(id)
            .orElseThrow(() -> new NotFoundException("Asesora de viaje no encontrada con ID: " + id));

        advisor.setFullName(req.fullName());
        advisor.setRoleTitle(req.roleTitle());
        advisor.setSpecialty(req.specialty());
        advisor.setBio(req.bio());
        advisor.setPhotoMediaId(req.photoMediaId());
        advisor.setWhatsappPhone(req.whatsappPhone());
        advisor.setWhatsappMessageTemplate(req.whatsappMessageTemplate());
        if (req.displayOrder() != null) advisor.setDisplayOrder(req.displayOrder());
        if (req.active() != null) advisor.setActive(req.active());
        advisor.setUpdatedAt(Instant.now());

        TravelAdvisor saved = advisorRepository.save(advisor);
        return getPublicAboutUseCase.toAdvisorDTO(saved);
    }
}
