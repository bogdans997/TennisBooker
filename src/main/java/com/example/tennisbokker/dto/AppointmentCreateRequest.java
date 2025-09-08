package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.AppointmentType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentCreateRequest(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull AppointmentType type,  // SINGLE or DOUBLE
        UUID coachId,                   // optional
        BigDecimal price                // optional; if null, derive from court by type
) {
}
