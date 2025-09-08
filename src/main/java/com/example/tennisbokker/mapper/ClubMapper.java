package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.ClubCreateRequest;
import com.example.tennisbokker.dto.ClubResponseDto;
import com.example.tennisbokker.dto.ClubUpdateRequest;
import com.example.tennisbokker.entity.Club;
import com.example.tennisbokker.entity.User;

public final class ClubMapper {
    private ClubMapper() {
    }

    public static Club fromCreate(ClubCreateRequest r, User owner) {
        return Club.builder()
                .name(r.name())
                .location(r.location())
                .workingHours(r.workingHours())
                .description(r.description())
                .owner(owner)
                .build();
    }

    public static void applyUpdate(Club c, ClubUpdateRequest r) {
        c.setName(r.name());
        c.setLocation(r.location());
        c.setWorkingHours(r.workingHours());
        c.setDescription(r.description());
    }

    public static ClubResponseDto toResponse(Club c) {
        return new ClubResponseDto(
                c.getId(),
                c.getName(),
                c.getLocation(),
                c.getWorkingHours(),
                c.getDescription(),
                c.getOwner() != null ? c.getOwner().getId() : null,
                c.getOwner() != null ? c.getOwner().getFullName() : null,
                c.getCourts() != null ? c.getCourts().size() : 0,
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}