package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.CreateOrUpdateMatchResultRequest;
import com.example.tennisbokker.dto.MatchParticipantDto;
import com.example.tennisbokker.dto.ResponseMatchResultDto;
import com.example.tennisbokker.entity.Appointment;
import com.example.tennisbokker.entity.MatchPlayer;
import com.example.tennisbokker.entity.MatchResult;
import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.AppointmentType;
import com.example.tennisbokker.mapper.MatchResultMapper;
import com.example.tennisbokker.repository.AppointmentRepository;
import com.example.tennisbokker.repository.MatchResultRepository;
import com.example.tennisbokker.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchResultServiceImpl implements MatchResultService {

    private final MatchResultRepository matchResultRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public MatchResultServiceImpl(MatchResultRepository matchResultRepository, AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.matchResultRepository = matchResultRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ResponseMatchResultDto findByAppointmentId(UUID appointmentId) {
        MatchResult mr = matchResultRepository.findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MatchResult not found"));
        return MatchResultMapper.toDto(mr);
    }

    @Transactional(readOnly = true)
    public ResponseMatchResultDto findByMatchId(UUID resultId) {
        MatchResult mr = matchResultRepository.findById(resultId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MatchResult not found"));
        return MatchResultMapper.toDto(mr);
    }

    public ResponseMatchResultDto update(UUID appointmentId, CreateOrUpdateMatchResultRequest req) {
        MatchResult mr = matchResultRepository.findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MatchResult not found"));

        // Basic fields
        mr.setResultText(req.resultText());
        mr.setPhotoUrl(req.photoUrl());

        // Participants handling (optional)
        if (req.participants() != null) {
            Appointment appt = mr.getAppointment();
            AppointmentType type = appt.getType();

            // Load users and build MatchPlayer list
            List<MatchPlayer> newList = req.participants().stream()
                    .map(p -> toMatchPlayer(p, mr))
                    .toList();

            validateParticipants(newList, type);

            mr.setParticipants(newList);
        }

        return MatchResultMapper.toDto(mr);
    }

    public void delete(UUID appointmentId) {
        MatchResult mr = matchResultRepository.findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MatchResult not found"));
        matchResultRepository.delete(mr);
    }

    private MatchPlayer toMatchPlayer(MatchParticipantDto dto, MatchResult mr) {
        User u = userRepository.findById(dto.playerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found: " + dto.playerId()));
        MatchPlayer mp = new MatchPlayer();
        mp.setMatchResult(mr);
        mp.setPlayer(u);
        mp.setTeam(dto.team());
        mp.setSlot(dto.slot());
        return mp;
    }

    private void validateParticipants(List<MatchPlayer> list, AppointmentType type) {
        // No duplicates
        Set<UUID> unique = new HashSet<>();
        for (MatchPlayer mp : list) {
            UUID pid = mp.getPlayer().getId();
            if (!unique.add(pid)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate player: " + pid);
            }
        }

        // Valid team/slot combos
        for (MatchPlayer mp : list) {
            if (mp.getSlot() == null || (mp.getSlot() != 1 && mp.getSlot() != 2)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "slot must be 1 or 2");
            }
        }

        // Count per team
        Map<?, Long> byTeam = list.stream()
                .collect(Collectors.groupingBy(MatchPlayer::getTeam, Collectors.counting()));

        switch (type) {
            case SINGLE -> {
                // Exactly one per team, slot=1 only
                if (list.size() != 2 || byTeam.values().stream().anyMatch(c -> c != 1)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SINGLE requires exactly 2 players (1 per team)");
                }
                if (list.stream().anyMatch(mp -> mp.getSlot() != 1)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SINGLE only allows slot=1");
                }
            }
            case DOUBLE -> {
                // Exactly 2 per team, slots 1 and 2
                if (list.size() != 4 || byTeam.values().stream().anyMatch(c -> c != 2)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DOUBLE requires exactly 4 players (2 per team)");
                }
                // Ensure each team has slots 1 and 2
                var grouped = list.stream().collect(Collectors.groupingBy(MatchPlayer::getTeam));
                grouped.forEach((team, players) -> {
                    var slots = players.stream().map(MatchPlayer::getSlot).collect(Collectors.toSet());
                    if (!(slots.contains(1) && slots.contains(2))) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each team must have slots 1 and 2");
                    }
                });
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<ResponseMatchResultDto> listForUser(UUID userId, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        LocalDateTime f = (from != null) ? from : LocalDateTime.now().minusYears(10);
        LocalDateTime t = (to   != null) ? to   : LocalDateTime.now().plusYears(1);
        Page<MatchResult> page = matchResultRepository.findUserTimeline(userId, f, t, pageable);
        return page.map(MatchResultMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<ResponseMatchResultDto> listRange(LocalDateTime from, LocalDateTime to, UUID clubId) {
        LocalDateTime f = (from != null) ? from : LocalDateTime.now().minusYears(10);
        LocalDateTime t = (to   != null) ? to   : LocalDateTime.now().plusYears(1);
        return matchResultRepository.findByRange(f, t, clubId).stream().map(MatchResultMapper::toDto).toList();
    }
}