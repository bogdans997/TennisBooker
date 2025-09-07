package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.CreateOrUpdateMatchResultRequest;
import com.example.tennisbokker.dto.ResponseMatchResultDto;
import com.example.tennisbokker.entity.MatchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MatchResultService {
    ResponseMatchResultDto findByAppointmentId(UUID appointmentId);
    ResponseMatchResultDto findByMatchId(UUID id);
    ResponseMatchResultDto update(UUID appointmentId, CreateOrUpdateMatchResultRequest req);
    void delete(UUID appointmentId); // deletes the result (keeps appointment)

    Page<ResponseMatchResultDto> listForUser(UUID userId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<ResponseMatchResultDto> listRange(LocalDateTime from, LocalDateTime to, UUID clubId);
}