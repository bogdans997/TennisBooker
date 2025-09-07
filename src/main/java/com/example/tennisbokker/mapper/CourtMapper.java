package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.CourtDto;
import com.example.tennisbokker.dto.ResponseCourtDto;
import com.example.tennisbokker.entity.Court;
import com.example.tennisbokker.entity.Club;

public class CourtMapper {
    public static ResponseCourtDto toDto(Court court) {
        return new ResponseCourtDto(
                court.getId(),
                court.getName(),
                court.getSurfaceType(),
                court.getPriceSingle(),
                court.getPriceDouble(),
                court.getClub() != null ? court.getClub().getId() : null,
                court.getClub() != null ? court.getClub().getName() : null
        );
    }

    public static Court toEntity(CourtDto dto) {
        if (dto == null) return null;
        Court court = new Court();
        court.setId(dto.getId());
        court.setName(dto.getName());
        court.setSurfaceType(dto.getSurfaceType());
        court.setPriceSingle(dto.getPriceSingle());
        court.setPriceDouble(dto.getPriceDouble());
        if (dto.getClubId() != null) {
            Club club = new Club();
            club.setId(dto.getClubId());
            court.setClub(club);
        }
        return court;
    }
}