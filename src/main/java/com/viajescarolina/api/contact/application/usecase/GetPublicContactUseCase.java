package com.viajescarolina.api.contact.application.usecase;

import com.viajescarolina.api.contact.application.dto.ContactPageDTO;
import com.viajescarolina.api.contact.application.dto.PublicContactResponse;
import com.viajescarolina.api.contact.domain.ContactPage;
import com.viajescarolina.api.contact.domain.ContactPageRepository;
import com.viajescarolina.api.settings.domain.OfficeLocation;
import com.viajescarolina.api.settings.domain.OfficeRepository;
import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import jakarta.enterprise.context.ApplicationScoped;

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

        String primaryPhone = settings != null ? settings.getPrimaryPhone() : "+51 987 654 321";
        String whatsappPhone = waChannel != null && waChannel.getE164Number() != null ? waChannel.getE164Number() : "+51987654321";
        String contactEmail = settings != null ? settings.getContactEmail() : "contacto@viajescarolina.com";
        String officeAddress = office != null ? office.getAddressLine() + ", " + office.getDistrict() + ", " + office.getCity() : "Av. Larco 101, Oficina 502, Miraflores, Lima";
        String officeHours = office != null ? office.getScheduleWeekdays() : "Lunes a Viernes: 9:00 AM – 7:00 PM";

        return new PublicContactResponse(
            toPageDTO(page),
            primaryPhone,
            whatsappPhone,
            contactEmail,
            officeAddress,
            officeHours
        );
    }

    public ContactPageDTO toPageDTO(ContactPage p) {
        return new ContactPageDTO(
            p.getId(),
            p.getHeroBadge(),
            p.getHeroTitle(),
            p.getHeroSubtitle(),
            p.getWhatsappBoxTitle(),
            p.getWhatsappBoxSubtitle(),
            p.getFormTitle(),
            p.getFormSubtitle(),
            p.getRevision(),
            p.getUpdatedAt()
        );
    }
}
