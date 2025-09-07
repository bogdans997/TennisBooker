package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank @Size(max = 120) String fullName,
        @Size(max = 32) String phoneNumber,
        Role role,
        @Pattern(regexp = "^[a-zA-Z-]{2,8}$") @Size(max = 8) String preferredLanguage,
        @Size(min = 8, max = 200) String password // optional; update only if provided
) {
}
