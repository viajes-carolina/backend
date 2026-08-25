package com.viajescarolina.api.contact.application.usecase;

import com.viajescarolina.api.contact.application.dto.ContactPageDTO;
import com.viajescarolina.api.contact.application.dto.StarterPhraseDTO;
import com.viajescarolina.api.contact.application.dto.UpdateContactPageRequest;
import com.viajescarolina.api.contact.domain.ContactPage;
import com.viajescarolina.api.contact.domain.ContactPageRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class UpdateContactPageUseCase {
    private final ContactPageRepository contactPageRepository;
    private final GetPublicContactUseCase getPublicContactUseCase;

    public UpdateContactPageUseCase(ContactPageRepository contactPageRepository,
                                  GetPublicContactUseCase getPublicContactUseCase) {
        this.contactPageRepository = contactPageRepository;
        this.getPublicContactUseCase = getPublicContactUseCase;
    }

    @Audited(action = "UPDATE_CONTACT_PAGE", entityType = "CONTACT_PAGE")
    @Transactional
    public ContactPageDTO execute(UpdateContactPageRequest req) {
        ContactPage page = contactPageRepository.findSingleton()
            .orElseGet(() -> {
                ContactPage newP = new ContactPage();
                newP.setId(1);
                newP.setRevision(1);
                newP.setCreatedAt(Instant.now());
                return newP;
            });

        page.setHeroBadge(req.heroBadge());
        page.setHeroTitle(req.heroTitle());
        page.setHeroSubtitle(req.heroSubtitle());
        page.setHeroCtaText(req.heroCtaText());
        page.setHeroNoteText(req.heroNoteText());
        page.setHeroCtaMessage(req.heroCtaMessage());
        page.setHeroChatLabel(req.heroChatLabel());
        page.setHeroChatBubble1(req.heroChatBubble1());
        page.setHeroChatBubble2(req.heroChatBubble2());
        page.setHeroChatBubble3(req.heroChatBubble3());

        page.setStartersBadge(req.startersBadge());
        page.setStartersTitle(req.startersTitle());
        page.setStartersSubtitle(req.startersSubtitle());
        page.setStartersClosing(req.startersClosing());
        page.setStarterPhrases(toStarterPhrases(req.starterPhrases()));

        page.setOfficeSectionBadge(req.officeSectionBadge());
        page.setOfficeSectionTitle(req.officeSectionTitle());
        page.setOfficeSectionSubtitle(req.officeSectionSubtitle());
        page.setOfficeMapTitle(req.officeMapTitle());
        page.setOfficeMapSubtitle(req.officeMapSubtitle());
        page.setOfficeVisitNote(req.officeVisitNote());
        page.setOfficeMapEyebrow(req.officeMapEyebrow());
        page.setOfficeMapPinTitle(req.officeMapPinTitle());
        page.setOfficeMapPinSubtitle(req.officeMapPinSubtitle());
        page.setOfficeMapsLinkText(req.officeMapsLinkText());
        page.setOfficeLocationLabel(req.officeLocationLabel());
        page.setOfficeVisitLabel(req.officeVisitLabel());
        page.setOfficeVisitCtaText(req.officeVisitCtaText());
        page.setOfficeVisitCtaMessage(req.officeVisitCtaMessage());

        page.setRevision(page.getRevision() + 1);
        page.setUpdatedAt(Instant.now());

        ContactPage saved = contactPageRepository.save(page);
        return getPublicContactUseCase.toPageDTO(saved);
    }

    private static List<ContactPage.StarterPhrase> toStarterPhrases(List<StarterPhraseDTO> phrases) {
        if (phrases == null) return List.of();
        return phrases.stream().map(s -> new ContactPage.StarterPhrase(s.quote(), s.support())).toList();
    }
}
