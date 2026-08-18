package com.viajescarolina.api.contact.domain;

import java.util.Optional;

public interface ContactPageRepository {
    Optional<ContactPage> findSingleton();
    ContactPage save(ContactPage contactPage);
}
