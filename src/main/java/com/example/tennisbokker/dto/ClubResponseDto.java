package com.example.tennisbokker.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClubResponseDto(
        UUID id,
        String name,
        String location,
        String workingHours,
        String description,
        UUID ownerId,
        String ownerFullName,
        Integer courtsCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
