package com.viajescarolina.api.settings.domain;

import java.util.Optional;

public interface WhatsAppRepository {
    Optional<WhatsAppChannel> findChannel();
    WhatsAppChannel saveChannel(WhatsAppChannel channel);
}
