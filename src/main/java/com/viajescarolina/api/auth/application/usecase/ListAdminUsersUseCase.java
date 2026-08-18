package com.viajescarolina.api.auth.application.usecase;

import com.viajescarolina.api.auth.application.dto.AdminUserDTO;
import com.viajescarolina.api.auth.domain.AdminUser;
import com.viajescarolina.api.auth.domain.AdminUserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ListAdminUsersUseCase {

    private final AdminUserRepository userRepository;

    public ListAdminUsersUseCase(AdminUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AdminUserDTO> execute() {
        return userRepository.listAllUsers().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AdminUserDTO toDTO(AdminUser u) {
        return new AdminUserDTO(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getFullName(),
                u.getRole(),
                u.isActive(),
                u.getLastLoginAt(),
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }
}
