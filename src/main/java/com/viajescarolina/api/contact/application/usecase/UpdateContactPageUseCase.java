package com.viajescarolina.api.contact.application.usecase;

import com.viajescarolina.api.contact.application.dto.ContactPageDTO;
import com.viajescarolina.api.contact.application.dto.UpdateContactPageRequest;
import com.viajescarolina.api.contact.domain.ContactPage;
import com.viajescarolina.api.contact.domain.ContactPageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;

@ApplicationScoped
public class UpdateContactPageUseCase {
    private final ContactPageRepository contactPageRepository;
    private final GetPublicContactUseCase getPublicContactUseCase;

    public UpdateContactPageUseCase(ContactPageRepository contactPageRepository,
                                  GetPublicContactUseCase getPublicContactUseCase) {
        this.contactPageRepository = contactPageRepository;
        this.getPublicContactUseCase = getPublicContactUseCase;
    }

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
        page.setWhatsappBoxTitle(req.whatsappBoxTitle());
        page.setWhatsappBoxSubtitle(req.whatsappBoxSubtitle());
        page.setFormTitle(req.formTitle());
        page.setFormSubtitle(req.formSubtitle());
        page.setRevision(page.getRevision() + 1);
        page.setUpdatedAt(Instant.now());

        ContactPage saved = contactPageRepository.save(page);
        return getPublicContactUseCase.toPageDTO(saved);
    }
}
