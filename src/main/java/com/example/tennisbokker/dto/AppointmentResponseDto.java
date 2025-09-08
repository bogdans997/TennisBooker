package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.AppointmentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDto(
        UUID id,
        UUID courtId,
        UUID clubId,
        String courtName,
        UUID bookedById,
        String bookedByName,
        UUID coachId,
        String coachName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AppointmentType type,
        BigDecimal price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
