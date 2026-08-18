package com.viajescarolina.api.settings.domain;

import java.util.List;
import java.util.Optional;

public interface WhatsAppRepository {
    Optional<WhatsAppChannel> findChannel();
    WhatsAppChannel saveChannel(WhatsAppChannel channel);
    List<WhatsAppAction> findAllActions();
    Optional<WhatsAppAction> findActionByKey(String actionKey);
    WhatsAppAction saveAction(WhatsAppAction action);
}
