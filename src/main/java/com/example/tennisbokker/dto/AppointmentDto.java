package com.example.tennisbokker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.tennisbokker.entity.enums.AppointmentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private UUID id;
    private UUID courtId;
    private UUID coachId;
    private List<UUID> playerIds;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentType type;
    private BigDecimal price;
}