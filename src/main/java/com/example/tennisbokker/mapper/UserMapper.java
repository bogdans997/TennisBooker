package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.UserCreateRequest;
import com.example.tennisbokker.dto.UserResponseDto;
import com.example.tennisbokker.dto.UserUpdateRequest;
import com.example.tennisbokker.entity.User;

public final class UserMapper {
    private UserMapper() {}

    public static UserResponseDto toResponse(User u) {
        if (u == null) return null;
        return new UserResponseDto(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.getPhoneNumber(),
                u.getRole(),
                u.getPreferredLanguage(),
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }

    public static User fromCreate(UserCreateRequest r) {
        if (r == null) return null;
        return User.builder()
                .fullName(r.fullName())
                .email(r.email())
                .password(r.password()) // service may hash or ignore depending on auth strategy
                .phoneNumber(r.phoneNumber())
                .role(r.role()) // service will default if null
                .preferredLanguage(r.preferredLanguage())
                .build();
    }

    public static void applyUpdate(User existing, UserUpdateRequest r) {
        existing.setFullName(r.fullName());
        existing.setPhoneNumber(r.phoneNumber());
        if (r.role() != null) existing.setRole(r.role());
        if (r.preferredLanguage() != null) existing.setPreferredLanguage(r.preferredLanguage());
        if (r.password() != null && !r.password().isBlank()) {
            existing.setPassword(r.password());
        }
    }
}