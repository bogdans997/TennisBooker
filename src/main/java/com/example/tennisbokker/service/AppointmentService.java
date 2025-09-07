package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.CreateAppointmentRequest;
import com.example.tennisbokker.dto.ResponseAppointmentDto;
import com.example.tennisbokker.entity.Appointment;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    Appointment findById(UUID id);
    List<Appointment> findAll();
    ResponseAppointmentDto create(UUID courtId, UUID bookedByUserId, CreateAppointmentRequest req);
    Appointment update(UUID id, Appointment appointment);
    void delete(UUID id);
}