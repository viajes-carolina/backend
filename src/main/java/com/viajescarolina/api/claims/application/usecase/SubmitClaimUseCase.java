package com.viajescarolina.api.claims.application.usecase;

import com.viajescarolina.api.claims.application.dto.ClaimRecordDTO;
import com.viajescarolina.api.claims.application.dto.SubmitClaimRequest;
import com.viajescarolina.api.claims.domain.ClaimRecord;
import com.viajescarolina.api.claims.domain.ClaimRepository;
import com.viajescarolina.api.contact.infrastructure.security.CloudflareTurnstileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.OffsetDateTime;

@ApplicationScoped
public class SubmitClaimUseCase {

    private final ClaimRepository claimRepository;
    private final CloudflareTurnstileService turnstileService;

    @Inject
    public SubmitClaimUseCase(ClaimRepository claimRepository, CloudflareTurnstileService turnstileService) {
        this.claimRepository = claimRepository;
        this.turnstileService = turnstileService;
    }

    @Transactional
    public ClaimRecordDTO execute(SubmitClaimRequest request, String clientIp) {
        // Validar Turnstile: el token es obligatorio, no se acepta su ausencia como válida.
        boolean valid = request.turnstileToken() != null
                && !request.turnstileToken().isBlank()
                && turnstileService.verifyToken(request.turnstileToken(), clientIp);
        if (!valid) {
            throw new IllegalArgumentException("Verificación de seguridad anti-bot inválida. Por favor intenta nuevamente.");
        }

        String claimCode = claimRepository.generateNextClaimCode();
        String ipHash = hashIp(clientIp);

        ClaimRecord record = new ClaimRecord(
            null,
            claimCode,
            request.fullName().trim(),
            request.documentType().toUpperCase().trim(),
            request.documentNumber().trim(),
            request.email().toLowerCase().trim(),
            request.phone().trim(),
            request.address().trim(),
            request.isMinor(),
            request.parentName() != null ? request.parentName().trim() : null,
            request.parentDocument() != null ? request.parentDocument().trim() : null,
            request.contractedType() != null && !request.contractedType().isBlank()
                    ? request.contractedType().toUpperCase().trim() : "SERVICIO",
            request.claimedAmount(),
            request.currency() != null && !request.currency().isBlank()
                    ? request.currency().toUpperCase().trim() : "PEN",
            request.relatedService() != null && !request.relatedService().isBlank()
                    ? request.relatedService().toLowerCase().trim() : null,
            request.reservationCode() != null && !request.reservationCode().isBlank()
                    ? request.reservationCode().trim() : null,
            request.serviceDate(),
            request.responseChannel() != null && !request.responseChannel().isBlank()
                    ? request.responseChannel().toUpperCase().trim() : "EMAIL",
            request.description().trim(),
            request.claimType() != null ? request.claimType().toUpperCase().trim() : "RECLAMO",
            request.consumerDetail().trim(),
            request.consumerRequest().trim(),
            "PENDING",
            null,
            null,
            request.turnstileToken(),
            ipHash,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );

        ClaimRecord saved = claimRepository.save(record);
        return ClaimRecordDTO.fromDomain(saved);
    }

    private String hashIp(String ip) {
        if (ip == null || ip.isBlank()) return "ANONYMOUS";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(ip.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return "HASH_ERROR";
        }
    }
}
