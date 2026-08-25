package com.viajescarolina.api.contact.application.usecase;

import com.viajescarolina.api.contact.application.dto.ContactPageDTO;
import com.viajescarolina.api.contact.application.dto.PublicContactResponse;
import com.viajescarolina.api.contact.application.dto.StarterPhraseDTO;
import com.viajescarolina.api.contact.domain.ContactPage;
import com.viajescarolina.api.contact.domain.ContactPageRepository;
import com.viajescarolina.api.settings.domain.OfficeLocation;
import com.viajescarolina.api.settings.domain.OfficeRepository;
import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class GetPublicContactUseCase {
    private final ContactPageRepository contactPageRepository;
    private final SettingsRepository settingsRepository;
    private final WhatsAppRepository whatsAppRepository;
    private final OfficeRepository officeRepository;

    public GetPublicContactUseCase(ContactPageRepository contactPageRepository,
                                  SettingsRepository settingsRepository,
                                  WhatsAppRepository whatsAppRepository,
                                  OfficeRepository officeRepository) {
        this.contactPageRepository = contactPageRepository;
        this.settingsRepository = settingsRepository;
        this.whatsAppRepository = whatsAppRepository;
        this.officeRepository = officeRepository;
    }

    public PublicContactResponse execute() {
        ContactPage page = contactPageRepository.findSingleton()
            .orElseThrow(() -> new IllegalStateException("Datos de página Contacto no inicializados"));

        SiteSettings settings = settingsRepository.findSiteSettings().orElse(null);
        WhatsAppChannel waChannel = whatsAppRepository.findChannel().orElse(null);
        OfficeLocation office = officeRepository.findOffice().orElse(null);

        String primaryPhone = waChannel != null && waChannel.getDisplayNumber() != null ? waChannel.getDisplayNumber() : "+51 987 654 321";
        String whatsappPhone = waChannel != null && waChannel.getE164Number() != null ? waChannel.getE164Number() : "+51987654321";
        String contactEmail = settings != null ? settings.getContactEmail() : "contacto@viajescarolina.com";
        String officeAddress = office != null ? office.getAddressLine() + ", " + office.getDistrict() + ", " + office.getCity() : "Av. Larco 101, Oficina 502, Miraflores, Lima";
        String officeHours = office != null ? office.getScheduleWeekdays() : "Lunes a Viernes: 9:00 AM – 7:00 PM";
        String officeGoogleMapsUrl = office != null ? office.getGoogleMapsUrl() : null;
        BigDecimal officeLatitude = office != null ? office.getLatitude() : null;
        BigDecimal officeLongitude = office != null ? office.getLongitude() : null;

        return new PublicContactResponse(
            toPageDTO(page),
            primaryPhone,
            whatsappPhone,
            contactEmail,
            officeAddress,
            officeHours,
            officeGoogleMapsUrl,
            officeLatitude,
            officeLongitude
        );
    }

    public ContactPageDTO toPageDTO(ContactPage p) {
        return new ContactPageDTO(
            p.getId(),
            p.getHeroBadge(),
            p.getHeroTitle(),
            p.getHeroSubtitle(),
            p.getHeroCtaText(),
            p.getHeroNoteText(),
            p.getHeroCtaMessage(),
            p.getHeroChatLabel(),
            p.getHeroChatBubble1(),
            p.getHeroChatBubble2(),
            p.getHeroChatBubble3(),
            p.getStartersBadge(),
            p.getStartersTitle(),
            p.getStartersSubtitle(),
            p.getStartersClosing(),
            toStarterPhraseDTOs(p.getStarterPhrases()),
            p.getOfficeSectionBadge(),
            p.getOfficeSectionTitle(),
            p.getOfficeSectionSubtitle(),
            p.getOfficeMapTitle(),
            p.getOfficeMapSubtitle(),
            p.getOfficeVisitNote(),
            p.getOfficeMapEyebrow(),
            p.getOfficeMapPinTitle(),
            p.getOfficeMapPinSubtitle(),
            p.getOfficeMapsLinkText(),
            p.getOfficeLocationLabel(),
            p.getOfficeVisitLabel(),
            p.getOfficeVisitCtaText(),
            p.getOfficeVisitCtaMessage(),
            p.getRevision(),
            p.getUpdatedAt()
        );
    }

    private static List<StarterPhraseDTO> toStarterPhraseDTOs(List<ContactPage.StarterPhrase> phrases) {
        if (phrases == null) return List.of();
        return phrases.stream().map(s -> new StarterPhraseDTO(s.quote(), s.support())).toList();
    }
}
