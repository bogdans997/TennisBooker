package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.CourtResponseDto;
import com.example.tennisbokker.entity.Court;

public final class CourtMapper {

    private CourtMapper() {}

    public static CourtResponseDto toDto(Court c) {
        return new CourtResponseDto(
                c.getId(),
                c.getName(),
                c.getSurfaceType(),
                c.getPriceSingle(),
                c.getPriceDouble(),
                c.getClub() != null ? c.getClub().getId() : null,
                c.getClub() != null ? c.getClub().getName() : null
        );
    }
}