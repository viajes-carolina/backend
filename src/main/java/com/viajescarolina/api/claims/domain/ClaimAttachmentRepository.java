package com.viajescarolina.api.claims.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ClaimAttachmentRepository {
    ClaimAttachment save(ClaimAttachment attachment);
    List<ClaimAttachment> findByClaimId(Long claimId);

    /**
     * Resuelve los adjuntos de varios reclamos en una sola consulta batch (evita el patrón
     * N+1 de invocar {@link #findByClaimId(Long)} dentro de un loop por reclamo).
     */
    List<ClaimAttachment> findByClaimIds(Collection<Long> claimIds);

    // Nombrado "findAttachmentById" (no "findById") a propósito: al implementar esta interfaz
    // junto con PanacheRepositoryBase<ClaimAttachmentPanacheEntity, Long> en la misma clase,
    // un método findById(Long) con tipo de retorno distinto colisionaría con el default method
    // findById(Id) de Panache (mismo patrón ya usado por ClaimRepository.findClaimById).
    Optional<ClaimAttachment> findAttachmentById(Long id);
}
