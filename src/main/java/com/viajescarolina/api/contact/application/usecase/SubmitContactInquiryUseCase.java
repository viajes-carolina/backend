package com.viajescarolina.api.contact.application.usecase;

import com.viajescarolina.api.contact.application.dto.ContactInquiryDTO;
import com.viajescarolina.api.contact.application.dto.SubmitContactInquiryRequest;
import com.viajescarolina.api.contact.domain.ContactInquiry;
import com.viajescarolina.api.contact.domain.ContactInquiryRepository;
import com.viajescarolina.api.contact.domain.TurnstileVerificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;

@ApplicationScoped
public class SubmitContactInquiryUseCase {
    private final ContactInquiryRepository inquiryRepository;
    private final TurnstileVerificationService turnstileService;

    public SubmitContactInquiryUseCase(ContactInquiryRepository inquiryRepository,
                                      TurnstileVerificationService turnstileService) {
        this.inquiryRepository = inquiryRepository;
        this.turnstileService = turnstileService;
    }

    @Transactional
    public ContactInquiryDTO execute(SubmitContactInquiryRequest req, String remoteIp) {
        boolean verified = turnstileService.verifyToken(req.turnstileToken(), remoteIp);
        if (!verified) {
            throw new WebApplicationException("Verificación anti-bot fallida.", Response.Status.FORBIDDEN);
        }

        String ipHash = null;
        if (remoteIp != null && !remoteIp.isBlank()) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(remoteIp.getBytes(StandardCharsets.UTF_8));
                ipHash = HexFormat.of().formatHex(hash);
            } catch (Exception e) {
                ipHash = null;
            }
        }

        ContactInquiry inquiry = new ContactInquiry();
        inquiry.setFullName(req.fullName());
        inquiry.setEmail(req.email());
        inquiry.setPhone(req.phone());
        inquiry.setDestinationOfInterest(req.destinationOfInterest());
        inquiry.setTravelDateApprox(req.travelDateApprox());
        inquiry.setTravelersCount(req.travelersCount() != null ? req.travelersCount() : 1);
        inquiry.setMessage(req.message());
        inquiry.setPreferredContactChannel(req.preferredContactChannel() != null ? req.preferredContactChannel() : "WHATSAPP");
        inquiry.setStatus("NEW");
        inquiry.setIpHash(ipHash);
        inquiry.setTurnstileVerified(verified);
        inquiry.setCreatedAt(Instant.now());
        inquiry.setUpdatedAt(Instant.now());

        ContactInquiry saved = inquiryRepository.save(inquiry);
        return toDTO(saved);
    }

    public ContactInquiryDTO toDTO(ContactInquiry i) {
        return new ContactInquiryDTO(
            i.getId(),
            i.getFullName(),
            i.getEmail(),
            i.getPhone(),
            i.getDestinationOfInterest(),
            i.getTravelDateApprox(),
            i.getTravelersCount(),
            i.getMessage(),
            i.getPreferredContactChannel(),
            i.getStatus(),
            i.isTurnstileVerified(),
            i.getCreatedAt(),
            i.getUpdatedAt()
        );
    }
}
