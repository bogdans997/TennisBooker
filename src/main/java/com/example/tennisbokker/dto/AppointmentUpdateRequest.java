package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.AppointmentType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentUpdateRequest(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull AppointmentType type,
        UUID coachId,         // can add/remove/change coach
        BigDecimal price      // optional; null -> re-derive
) {
}
