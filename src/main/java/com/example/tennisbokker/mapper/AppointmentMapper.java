package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.AppointmentResponseDto;
import com.example.tennisbokker.entity.Appointment;

public final class AppointmentMapper {
    private AppointmentMapper() {
    }

    public static AppointmentResponseDto toResponse(Appointment a) {
        return new AppointmentResponseDto(
                a.getId(),
                a.getCourt() != null ? a.getCourt().getId() : null,
                (a.getCourt() != null && a.getCourt().getClub() != null) ? a.getCourt().getClub().getId() : null,
                a.getCourt() != null ? a.getCourt().getName() : null,
                a.getBookedBy() != null ? a.getBookedBy().getId() : null,
                a.getBookedBy() != null ? a.getBookedBy().getFullName() : null,
                a.getCoach() != null ? a.getCoach().getId() : null,
                a.getCoach() != null ? a.getCoach().getFullName() : null,
                a.getStartTime(),
                a.getEndTime(),
                a.getType(),
                a.getPrice(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}