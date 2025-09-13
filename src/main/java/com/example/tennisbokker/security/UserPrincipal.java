package com.example.tennisbokker.security;

import com.example.tennisbokker.entity.enums.Role;

import java.util.UUID;

public record UserPrincipal(
        UUID id,
        String email,
        String fullName,
        Role role,
        String firebaseUid
) {
}
