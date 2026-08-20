package com.viajescarolina.api.settings.application.usecase;

import com.viajescarolina.api.settings.application.dto.WhatsAppChannelDTO;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetWhatsAppChannelUseCase {

    private final WhatsAppRepository whatsAppRepository;

    @Inject
    public GetWhatsAppChannelUseCase(WhatsAppRepository whatsAppRepository) {
        this.whatsAppRepository = whatsAppRepository;
    }

    public WhatsAppChannelDTO execute() {
        WhatsAppChannel channel = whatsAppRepository.findChannel()
                .orElseGet(() -> new WhatsAppChannel(null, "Línea Principal", "+51987654321", "+51 987 654 321",
                        "Hola Viajes Carolina, deseo asesoría personalizada para mi próximo viaje.", true, true, 1, null, null));

        return new WhatsAppChannelDTO(
                channel.getId(),
                channel.getLabel(),
                channel.getE164Number(),
                channel.getDisplayNumber(),
                channel.getDefaultMessage(),
                channel.isPrimary(),
                channel.isActive(),
                channel.getRevision(),
                channel.getUpdatedAt()
        );
    }
}
