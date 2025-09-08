package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.MatchResultCreateOrUpdateRequest;
import com.example.tennisbokker.dto.MatchResultResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MatchResultService {
    MatchResultResponseDto findByAppointmentId(UUID appointmentId);
    MatchResultResponseDto findByMatchId(UUID id);
    MatchResultResponseDto update(UUID appointmentId, MatchResultCreateOrUpdateRequest req);
    void delete(UUID appointmentId); // deletes the result (keeps appointment)

    Page<MatchResultResponseDto> listForUser(UUID userId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<MatchResultResponseDto> listRange(LocalDateTime from, LocalDateTime to, UUID clubId);
}