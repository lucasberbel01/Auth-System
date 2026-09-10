package com.lucasberbel01.loginsystem.dto;

import com.lucasberbel01.loginsystem.enums.UserRole;
import com.lucasberbel01.loginsystem.model.User;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        UserRole role
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}