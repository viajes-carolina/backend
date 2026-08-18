package com.viajescarolina.api.contact.application.usecase;

import com.viajescarolina.api.contact.application.dto.ContactInquiryDTO;
import com.viajescarolina.api.contact.domain.ContactInquiryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ListAdminInquiriesUseCase {
    private final ContactInquiryRepository inquiryRepository;
    private final SubmitContactInquiryUseCase submitContactInquiryUseCase;

    public ListAdminInquiriesUseCase(ContactInquiryRepository inquiryRepository,
                                   SubmitContactInquiryUseCase submitContactInquiryUseCase) {
        this.inquiryRepository = inquiryRepository;
        this.submitContactInquiryUseCase = submitContactInquiryUseCase;
    }

    public List<ContactInquiryDTO> execute(String statusFilter) {
        return inquiryRepository.listAdminAll(statusFilter).stream()
            .map(submitContactInquiryUseCase::toDTO)
            .toList();
    }
}
