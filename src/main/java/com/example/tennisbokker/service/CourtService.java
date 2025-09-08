package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.CourtCreateRequest;
import com.example.tennisbokker.dto.CourtResponseDto;
import com.example.tennisbokker.dto.CourtUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface CourtService {
    CourtResponseDto findById(UUID id);
    List<CourtResponseDto> findAll();
    List<CourtResponseDto> findAllByClub(UUID clubId);
    CourtResponseDto createForClub(UUID clubId, CourtCreateRequest court);
    CourtResponseDto update(UUID id, CourtUpdateRequest req, UUID clubId);
    void delete(UUID id);
}