package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.application.dto.AboutPageDTO;
import com.viajescarolina.api.about.application.dto.PublicAboutResponse;
import com.viajescarolina.api.about.application.dto.TravelAdvisorDTO;
import com.viajescarolina.api.about.domain.AboutPage;
import com.viajescarolina.api.about.domain.AboutPageRepository;
import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class GetPublicAboutUseCase {
    private final AboutPageRepository aboutPageRepository;
    private final TravelAdvisorRepository advisorRepository;

    public GetPublicAboutUseCase(AboutPageRepository aboutPageRepository, TravelAdvisorRepository advisorRepository) {
        this.aboutPageRepository = aboutPageRepository;
        this.advisorRepository = advisorRepository;
    }

    public PublicAboutResponse execute() {
        AboutPage page = aboutPageRepository.findSingleton()
            .orElseThrow(() -> new IllegalStateException("Datos de página Nosotros no inicializados"));

        List<TravelAdvisorDTO> advisorDTOs = advisorRepository.listPublicActive().stream()
            .map(this::toAdvisorDTO)
            .toList();

        return new PublicAboutResponse(toPageDTO(page), advisorDTOs);
    }

    public AboutPageDTO toPageDTO(AboutPage p) {
        return new AboutPageDTO(
            p.getId(),
            p.getHeroBadge(),
            p.getHeroTitle(),
            p.getHeroSubtitle(),
            p.getHeroMediaId(),
            p.getHeroMediaUrl(),
            p.getStoryTitle(),
            p.getStoryBody(),
            p.getStoryMediaId(),
            p.getStoryMediaUrl(),
            p.getMissionTitle(),
            p.getMissionBody(),
            p.getVisionTitle(),
            p.getVisionBody(),
            p.getValues(),
            p.getExperienceYears(),
            p.getHappyTravelers(),
            p.getDestinationsCount(),
            p.getSatisfactionRatePercent(),
            p.getRevision(),
            p.getUpdatedAt()
        );
    }

    public TravelAdvisorDTO toAdvisorDTO(TravelAdvisor a) {
        return new TravelAdvisorDTO(
            a.getId(),
            a.getFullName(),
            a.getRoleTitle(),
            a.getSpecialty(),
            a.getBio(),
            a.getPhotoMediaId(),
            a.getPhotoMediaUrl(),
            a.getWhatsappPhone(),
            a.getWhatsappMessageTemplate(),
            a.getDisplayOrder(),
            a.isActive(),
            a.getCreatedAt(),
            a.getUpdatedAt()
        );
    }
}
