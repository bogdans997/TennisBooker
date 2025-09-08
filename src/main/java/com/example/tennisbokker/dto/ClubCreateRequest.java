package com.example.tennisbokker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ClubCreateRequest(
        @NotBlank @Size(max = 160) String name,
        @NotBlank @Size(max = 200) String location,
        @Size(max = 120) String workingHours,
        @Size(max = 5000) String description,
        @NotNull UUID ownerId
) {
}
