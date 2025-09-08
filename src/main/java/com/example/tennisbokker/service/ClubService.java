package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.ClubCreateRequest;
import com.example.tennisbokker.dto.ClubResponseDto;
import com.example.tennisbokker.dto.ClubUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ClubService {
    ClubResponseDto findById(UUID id);

    Page<ClubResponseDto> findAll(UUID ownerId, String q, Pageable pageable);

    ClubResponseDto create(ClubCreateRequest request);

    ClubResponseDto update(UUID id, ClubUpdateRequest request);

    void delete(UUID id);
}