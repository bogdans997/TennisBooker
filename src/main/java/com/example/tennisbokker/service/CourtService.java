package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.CreateCourtRequest;
import com.example.tennisbokker.dto.ResponseCourtDto;
import com.example.tennisbokker.dto.UpdateCourtRequest;
import com.example.tennisbokker.entity.Court;

import java.util.List;
import java.util.UUID;

public interface CourtService {
    ResponseCourtDto findById(UUID id);
    List<ResponseCourtDto> findAll();
    List<ResponseCourtDto> findAllByClub(UUID clubId);
    ResponseCourtDto createForClub(UUID clubId, CreateCourtRequest court);
    ResponseCourtDto update(UUID id, UpdateCourtRequest req, UUID clubId);
    void delete(UUID id);
}