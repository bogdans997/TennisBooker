package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.ResponseCourtDto;
import com.example.tennisbokker.entity.Court;

public final class CourtMapper {

    private CourtMapper() {}

    public static ResponseCourtDto toDto(Court c) {
        return new ResponseCourtDto(
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