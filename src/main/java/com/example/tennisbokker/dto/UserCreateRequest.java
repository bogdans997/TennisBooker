package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank @Size(max = 120) String fullName,
        @NotBlank @Email @Size(max = 190) String email,
        @Size(min = 8, max = 200) String password, // optional if Firebase handles auth
        @Size(max = 32) String phoneNumber,
        Role role, // null -> default PLAYER
        @Pattern(regexp = "^[a-zA-Z-]{2,8}$") @Size(max = 8) String preferredLanguage // e.g. "en", "sr" or "en-US"
) {
}
