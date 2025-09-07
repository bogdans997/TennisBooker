package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.ResponseMatchResultDto;
import com.example.tennisbokker.entity.MatchPlayer;
import com.example.tennisbokker.entity.MatchResult;

import java.util.List;

public class MatchResultMapper {
    public static ResponseMatchResultDto toDto(MatchResult mr) {
        var appt = mr.getAppointment();
        var court = appt != null ? appt.getCourt() : null;
        var club = (court != null) ? court.getClub() : null;

        List<ResponseMatchResultDto.ParticipantOut> parts = mr.getParticipants().stream()
                .map(MatchResultMapper::toParticipant)
                .toList();

        return new ResponseMatchResultDto(
                mr.getId(),
                appt != null ? appt.getId() : null,
                court != null ? court.getId() : null,
                club != null ? club.getId() : null,
                appt != null ? appt.getStartTime() : null,
                appt != null ? appt.getEndTime() : null,
                mr.getResultText(),
                mr.getPhotoUrl(),
                parts
        );
    }

    private static ResponseMatchResultDto.ParticipantOut toParticipant(MatchPlayer p) {
        var u = p.getPlayer();
        return new ResponseMatchResultDto.ParticipantOut(
                u != null ? u.getId() : null,
                u != null ? u.getFullName() : null,
                p.getTeam(),
                p.getSlot()
        );
    }
}