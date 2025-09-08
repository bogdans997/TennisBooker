package com.example.tennisbokker.dto;

import com.example.tennisbokker.entity.enums.TeamSide;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MatchResultResponseDto(
        UUID id,
        UUID appointmentId,
        UUID courtId,
        UUID clubId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String resultText,
        String photoUrl,
        List<ParticipantOut> participants
) {
    public record ParticipantOut(UUID userId, String fullName, TeamSide team, Integer slot) {}
}
