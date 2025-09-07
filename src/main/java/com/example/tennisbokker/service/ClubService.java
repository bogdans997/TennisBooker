package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.ResponseClubDto;
import com.example.tennisbokker.dto.CreateClubRequest;
import com.example.tennisbokker.entity.Club;

import java.util.List;
import java.util.UUID;

public interface ClubService {
    ResponseClubDto findById(UUID id);
    List<ResponseClubDto> findAll();
    Club create(CreateClubRequest createClubRequest);
    Club update(UUID id, Club club);
    void delete(UUID id);
}