package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.application.dto.AboutPageDTO;
import com.viajescarolina.api.about.application.dto.AccompanyStepDTO;
import com.viajescarolina.api.about.application.dto.JourneyStepDTO;
import com.viajescarolina.api.about.application.dto.MomentDTO;
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
            p.getHeroFocalX(),
            p.getHeroFocalY(),
            p.getHeroCardBadge(),
            p.getHeroCardTitle(),
            p.getHeroNoteText(),
            p.getStoryTitle(),
            p.getStoryBody(),
            p.getStoryMediaId(),
            p.getStoryMediaUrl(),
            p.getStoryFocalX(),
            p.getStoryFocalY(),
            p.getMissionTitle(),
            p.getMissionBody(),
            p.getMissionQuote(),
            toJourneyStepDTOs(p.getJourneySteps()),
            p.getValues(),
            p.getAccompanyBadge(),
            p.getAccompanyTitle(),
            p.getAccompanySubtitle(),
            toAccompanyStepDTOs(p.getAccompanySteps()),
            p.getAccompanyQuote(),
            p.getAccompanyQuoteAttribution(),
            p.getMomentsBadge(),
            p.getMomentsTitle(),
            p.getMomentsSubtitle(),
            p.getMomentsMediaId(),
            p.getMomentsMediaUrl(),
            p.getMomentsFocalX(),
            p.getMomentsFocalY(),
            toMomentDTOs(p.getMoments()),
            p.getHumanBadge(),
            p.getHumanTitle(),
            p.getHumanSubtitle(),
            p.getHumanTagline(),
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
            a.getQuote(),
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

    private static List<JourneyStepDTO> toJourneyStepDTOs(List<AboutPage.JourneyStep> steps) {
        if (steps == null) return List.of();
        return steps.stream().map(s -> new JourneyStepDTO(s.label())).toList();
    }

    private static List<AccompanyStepDTO> toAccompanyStepDTOs(List<AboutPage.AccompanyStep> steps) {
        if (steps == null) return List.of();
        return steps.stream().map(s -> new AccompanyStepDTO(s.title(), s.body())).toList();
    }

    private static List<MomentDTO> toMomentDTOs(List<AboutPage.Moment> moments) {
        if (moments == null) return List.of();
        return moments.stream().map(m -> new MomentDTO(m.title(), m.body())).toList();
    }
}
