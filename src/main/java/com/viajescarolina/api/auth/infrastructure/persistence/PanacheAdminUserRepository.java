package com.viajescarolina.api.auth.infrastructure.persistence;

import com.viajescarolina.api.auth.domain.AdminUser;
import com.viajescarolina.api.auth.domain.AdminUserRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PanacheAdminUserRepository implements AdminUserRepository, PanacheRepositoryBase<AdminUserPanacheEntity, Long> {

    @Override
    public Optional<AdminUser> findUserById(Long id) {
        AdminUserPanacheEntity entity = findById(id);
        return Optional.ofNullable(toDomain(entity));
    }

    @Override
    public Optional<AdminUser> findByUsername(String username) {
        AdminUserPanacheEntity entity = find("username", username).firstResult();
        return Optional.ofNullable(toDomain(entity));
    }

    @Override
    public Optional<AdminUser> findByEmail(String email) {
        AdminUserPanacheEntity entity = find("email", email).firstResult();
        return Optional.ofNullable(toDomain(entity));
    }

    @Override
    public List<AdminUser> listAllUsers() {
        return listAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdminUser save(AdminUser user) {
        AdminUserPanacheEntity entity;
        if (user.getId() != null) {
            entity = findById(user.getId());
            if (entity == null) {
                entity = new AdminUserPanacheEntity();
            }
        } else {
            entity = new AdminUserPanacheEntity();
        }

        entity.username = user.getUsername();
        entity.email = user.getEmail();
        entity.passwordHash = user.getPasswordHash();
        entity.fullName = user.getFullName();
        entity.role = user.getRole();
        entity.active = user.isActive();
        entity.lastLoginAt = user.getLastLoginAt();

        if (entity.id == null) {
            persist(entity);
        }

        return toDomain(entity);
    }

    private AdminUser toDomain(AdminUserPanacheEntity e) {
        if (e == null) return null;
        return new AdminUser(
                e.id,
                e.username,
                e.email,
                e.passwordHash,
                e.fullName,
                e.role,
                e.active,
                e.lastLoginAt,
                e.createdAt,
                e.updatedAt
        );
    }
}
