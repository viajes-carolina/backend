package com.viajescarolina.api.settings.application.usecase;

import com.viajescarolina.api.settings.application.dto.UpdateWhatsAppChannelRequest;
import com.viajescarolina.api.settings.application.dto.WhatsAppChannelDTO;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import com.viajescarolina.api.common.audit.Audited;
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

    @Audited(action = "UPDATE_WHATSAPP_CHANNEL", entityType = "WHATSAPP_CHANNEL")
    @Transactional
    public WhatsAppChannelDTO execute(UpdateWhatsAppChannelRequest request) {
        WhatsAppChannel channel = whatsAppRepository.findChannel()
                .orElseGet(() -> new WhatsAppChannel(null, request.label(), request.e164Number(), request.displayNumber(),
                        request.defaultMessage(), true, request.active(), 0, null, null));

        channel.update(request.label(), request.e164Number(), request.displayNumber(), request.defaultMessage(), request.active());
        WhatsAppChannel saved = whatsAppRepository.saveChannel(channel);

        return new WhatsAppChannelDTO(
                saved.getId(),
                saved.getLabel(),
                saved.getE164Number(),
                saved.getDisplayNumber(),
                saved.getDefaultMessage(),
                saved.isPrimary(),
                saved.isActive(),
                saved.getRevision(),
                saved.getUpdatedAt()
        );
    }
}
