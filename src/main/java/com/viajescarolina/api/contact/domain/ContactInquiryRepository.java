package com.viajescarolina.api.contact.domain;

import java.util.List;
import java.util.Optional;

public interface ContactInquiryRepository {
    List<ContactInquiry> listAdminAll(String statusFilter);
    Optional<ContactInquiry> findInquiryById(Long id);
    ContactInquiry save(ContactInquiry inquiry);
    void delete(Long id);
}
