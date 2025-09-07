package com.example.tennisbokker.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrUpdateMatchResultRequest(
        String resultText,
        String photoUrl,     // optional
        @Size(max = 4) List<MatchParticipantDto> participants // optional; 2 for SINGLE, 4 for DOUBLE
) {
}
