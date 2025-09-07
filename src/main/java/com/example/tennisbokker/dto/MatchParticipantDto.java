package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.TeamSide;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MatchParticipantDto(
        @NotNull UUID playerId,
        @NotNull TeamSide team,
        @NotNull Integer slot // 1 or 2
) {
}
