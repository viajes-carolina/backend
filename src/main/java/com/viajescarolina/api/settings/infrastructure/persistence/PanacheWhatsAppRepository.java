package com.viajescarolina.api.settings.infrastructure.persistence;

import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class PanacheWhatsAppRepository implements WhatsAppRepository {

    @Override
    public Optional<WhatsAppChannel> findChannel() {
        WhatsAppChannelPanacheEntity entity = WhatsAppChannelPanacheEntity.find("isPrimary", true).firstResult();
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(entity));
    }

    @Override
    public WhatsAppChannel saveChannel(WhatsAppChannel channel) {
        WhatsAppChannelPanacheEntity entity = channel.getId() != null
                ? WhatsAppChannelPanacheEntity.findById(channel.getId())
                : null;
        if (entity == null) {
            entity = new WhatsAppChannelPanacheEntity();
            entity.isPrimary = channel.isPrimary();
        }

        entity.label = channel.getLabel();
        entity.e164Number = channel.getE164Number();
        entity.displayNumber = channel.getDisplayNumber();
        entity.defaultMessage = channel.getDefaultMessage();
        entity.active = channel.isActive();
        entity.revision = channel.getRevision();
        entity.updatedAt = channel.getUpdatedAt();

        entity.persist();
        return toDomain(entity);
    }

    private WhatsAppChannel toDomain(WhatsAppChannelPanacheEntity entity) {
        return new WhatsAppChannel(
                entity.id,
                entity.label,
                entity.e164Number,
                entity.displayNumber,
                entity.defaultMessage,
                entity.isPrimary,
                entity.active,
                entity.revision,
                entity.createdAt,
                entity.updatedAt
        );
    }
}
