package com.viajescarolina.api.auth.domain;

import java.util.List;
import java.util.Optional;

public interface AdminUserRepository {
    Optional<AdminUser> findUserById(Long id);
    Optional<AdminUser> findByUsername(String username);
    Optional<AdminUser> findByEmail(String email);
    List<AdminUser> listAllUsers();
    AdminUser save(AdminUser user);
}
