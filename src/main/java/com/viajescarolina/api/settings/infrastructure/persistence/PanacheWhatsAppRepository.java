package com.viajescarolina.api.settings.infrastructure.persistence;

import com.viajescarolina.api.settings.domain.WhatsAppAction;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<WhatsAppAction> findAllActions() {
        List<WhatsAppActionPanacheEntity> entities = WhatsAppActionPanacheEntity.listAll();
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<WhatsAppAction> findActionByKey(String actionKey) {
        WhatsAppActionPanacheEntity entity = WhatsAppActionPanacheEntity.find("actionKey", actionKey).firstResult();
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(entity));
    }

    @Override
    public WhatsAppAction saveAction(WhatsAppAction action) {
        WhatsAppActionPanacheEntity entity;
        if (action.getId() != null) {
            entity = WhatsAppActionPanacheEntity.findById(action.getId());
        } else {
            entity = new WhatsAppActionPanacheEntity();
            entity.actionKey = action.getActionKey();
        }

        if (entity == null) {
            entity = new WhatsAppActionPanacheEntity();
            entity.actionKey = action.getActionKey();
        }

        entity.label = action.getLabel();
        entity.messageTemplate = action.getMessageTemplate();
        entity.description = action.getDescription();
        entity.updatedAt = action.getUpdatedAt();

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

    private WhatsAppAction toDomain(WhatsAppActionPanacheEntity entity) {
        return new WhatsAppAction(
                entity.id,
                entity.actionKey,
                entity.label,
                entity.messageTemplate,
                entity.description,
                entity.createdAt,
                entity.updatedAt
        );
    }
}
