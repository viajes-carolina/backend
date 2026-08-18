package com.viajescarolina.api.trust.domain;

import java.util.List;
import java.util.Optional;

public interface FaqRepository {

    List<FaqItem> findAllActive();

    List<FaqItem> findAllFaqs();

    Optional<FaqItem> findFaqById(Long id);

    FaqItem save(FaqItem faqItem);

    void delete(Long id);
}
