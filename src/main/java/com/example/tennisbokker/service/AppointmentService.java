package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.AppointmentCreateRequest;
import com.example.tennisbokker.dto.AppointmentResponseDto;
import com.example.tennisbokker.dto.AppointmentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AppointmentService {
    AppointmentResponseDto findById(UUID id);

    Page<AppointmentResponseDto> findAll(UUID courtId,
                                         UUID clubId,
                                         UUID coachId,
                                         UUID bookedById,
                                         LocalDateTime from,
                                         LocalDateTime to,
                                         Pageable pageable);

    AppointmentResponseDto create(UUID courtId, UUID bookedByUserId, AppointmentCreateRequest req);

    AppointmentResponseDto update(UUID id, AppointmentUpdateRequest req);

    void delete(UUID id);
}