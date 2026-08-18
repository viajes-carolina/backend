package com.viajescarolina.api.settings.application.usecase;

import com.viajescarolina.api.settings.application.dto.UpdateWhatsAppChannelRequest;
import com.viajescarolina.api.settings.application.dto.WhatsAppChannelDTO;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateWhatsAppChannelUseCase {

    private final WhatsAppRepository whatsAppRepository;

    @Inject
    public UpdateWhatsAppChannelUseCase(WhatsAppRepository whatsAppRepository) {
        this.whatsAppRepository = whatsAppRepository;
    }

    @Transactional
    public WhatsAppChannelDTO execute(UpdateWhatsAppChannelRequest request) {
        WhatsAppChannel channel = whatsAppRepository.findChannel()
                .orElseGet(() -> new WhatsAppChannel(1, request.e164Number(), request.displayNumber(), request.active(), 0, null, null));

        channel.update(request.e164Number(), request.displayNumber(), request.active());
        WhatsAppChannel saved = whatsAppRepository.saveChannel(channel);

        return new WhatsAppChannelDTO(
                saved.getId(),
                saved.getE164Number(),
                saved.getDisplayNumber(),
                saved.isActive(),
                saved.getRevision(),
                saved.getUpdatedAt()
        );
    }
}
