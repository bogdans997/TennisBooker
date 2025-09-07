package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.ResponseClubDto;
import com.example.tennisbokker.dto.CreateClubRequest;
import com.example.tennisbokker.entity.Club;
import com.example.tennisbokker.entity.User;

public class ClubMapper {
    public static ResponseClubDto toDto(Club club) {
        if (club == null) return null;
        ResponseClubDto dto = new ResponseClubDto();
        dto.setId(club.getId());
        dto.setName(club.getName());
        dto.setLocation(club.getLocation());
        dto.setWorkingHours(club.getWorkingHours());
        dto.setDescription(club.getDescription());
        dto.setOwnerFullName(club.getOwner() != null ? club.getOwner().getFullName() : null);
        return dto;
    }

    public static Club toEntity(CreateClubRequest dto) {
        if (dto == null) return null;
        Club club = new Club();
        club.setName(dto.getName());
        club.setLocation(dto.getLocation());
        club.setWorkingHours(dto.getWorkingHours());
        club.setDescription(dto.getDescription());
        if (dto.getOwnerId() != null) {
            User owner = new User();
            owner.setId(dto.getOwnerId());
            club.setOwner(owner);
    }
        return club;
    }
}