package com.viajescarolina.api.contact.application.usecase;

import com.viajescarolina.api.contact.application.dto.ContactInquiryDTO;
import com.viajescarolina.api.contact.domain.ContactInquiry;
import com.viajescarolina.api.contact.domain.ContactInquiryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.Instant;

@ApplicationScoped
public class UpdateInquiryStatusUseCase {
    private final ContactInquiryRepository inquiryRepository;
    private final SubmitContactInquiryUseCase submitContactInquiryUseCase;

    public UpdateInquiryStatusUseCase(ContactInquiryRepository inquiryRepository,
                                    SubmitContactInquiryUseCase submitContactInquiryUseCase) {
        this.inquiryRepository = inquiryRepository;
        this.submitContactInquiryUseCase = submitContactInquiryUseCase;
    }

    @Transactional
    public ContactInquiryDTO execute(Long id, String newStatus) {
        ContactInquiry inquiry = inquiryRepository.findInquiryById(id)
            .orElseThrow(() -> new NotFoundException("Solicitud de contacto no encontrada con ID: " + id));

        inquiry.setStatus(newStatus.toUpperCase());
        inquiry.setUpdatedAt(Instant.now());

        ContactInquiry saved = inquiryRepository.save(inquiry);
        return submitContactInquiryUseCase.toDTO(saved);
    }
}
