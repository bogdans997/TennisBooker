package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String fullName,
        String email,
        String phoneNumber,
        Role role,
        String preferredLanguage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
