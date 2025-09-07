package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.AppointmentDto;
import com.example.tennisbokker.dto.ResponseAppointmentDto;
import com.example.tennisbokker.entity.Appointment;
import com.example.tennisbokker.entity.Court;
import com.example.tennisbokker.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppointmentMapper {
    public static ResponseAppointmentDto toDto(Appointment a) {
        return new ResponseAppointmentDto(
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
                a.getPrice()
        );
    }
}