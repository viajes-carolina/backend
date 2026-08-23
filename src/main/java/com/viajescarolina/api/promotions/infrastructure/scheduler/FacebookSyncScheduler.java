package com.viajescarolina.api.promotions.infrastructure.scheduler;

import com.viajescarolina.api.promotions.application.usecase.SyncFacebookPromotionsUseCase;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

/**
 * Dispara periódicamente {@link SyncFacebookPromotionsUseCase}. Aunque {@code FacebookGraphClient}
 * ya es fail-safe por sí mismo (retorna lista vacía sin credenciales), el chequeo de
 * {@code syncEnabled} aquí evita el ruido de intentarlo en cada tick cuando la sincronización
 * está deshabilitada explícitamente.
 */
@ApplicationScoped
public class FacebookSyncScheduler {

    private static final Logger LOG = Logger.getLogger(FacebookSyncScheduler.class);

    @Inject
    SyncFacebookPromotionsUseCase syncFacebookPromotionsUseCase;

    @ConfigProperty(name = "viajescarolina.facebook.sync-enabled", defaultValue = "false")
    boolean syncEnabled;

    @Scheduled(every = "{viajescarolina.facebook.sync-interval}")
    void sync() {
        if (!syncEnabled) {
            return;
        }
        int created = syncFacebookPromotionsUseCase.execute();
        LOG.info("Sincronización de Facebook completada: " + created + " promociones nuevas creadas.");
    }
}
