package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.CreateCourtRequest;
import com.example.tennisbokker.dto.ResponseCourtDto;
import com.example.tennisbokker.dto.UpdateCourtRequest;
import com.example.tennisbokker.entity.Club;
import com.example.tennisbokker.entity.Court;
import com.example.tennisbokker.mapper.CourtMapper;
import com.example.tennisbokker.repository.ClubRepository;
import com.example.tennisbokker.repository.CourtRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final ClubRepository clubRepository;

    public CourtServiceImpl(CourtRepository courtRepository,  ClubRepository clubRepository) {
        this.courtRepository = courtRepository;
        this.clubRepository = clubRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseCourtDto findById(UUID id) {
        Court court = courtRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Court not found"));
        return CourtMapper.toDto(court);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseCourtDto> findAll() {
        return courtRepository.findAll().stream().map(CourtMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseCourtDto> findAllByClub(UUID clubId) {
        return courtRepository.findByClub_Id(clubId).stream().map(CourtMapper::toDto).toList();
    }

    @Override
    @Transactional
    public ResponseCourtDto createForClub(UUID clubId, CreateCourtRequest req) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found"));

        if (courtRepository.existsByClub_IdAndNameIgnoreCase(clubId, req.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Court name already exists for this club");
        }

        Court court = Court.builder()
                .name(req.name())
                .surfaceType(req.surfaceType())
                .priceSingle(req.priceSingle())
                .priceDouble(req.priceDouble())
                .club(club)
                .build();

        return CourtMapper.toDto(courtRepository.save(court));
    }

    @Override
    @Transactional
    public ResponseCourtDto update(UUID id, UpdateCourtRequest req, UUID clubId) {
        Court court = courtRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Court not found"));

        if (clubId != null && (court.getClub() == null || !clubId.equals(court.getClub().getId()))) {
            Club club = clubRepository.findById(clubId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found"));
            court.setClub(club);
        }

        // If name changes, re-check uniqueness within the (possibly new) club
        UUID effectiveClubId = court.getClub().getId();
        if (!court.getName().equalsIgnoreCase(req.name())
                && courtRepository.existsByClub_IdAndNameIgnoreCase(effectiveClubId, req.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Court name already exists for this club");
        }

        court.setName(req.name());
        court.setSurfaceType(req.surfaceType());
        court.setPriceSingle(req.priceSingle());
        court.setPriceDouble(req.priceDouble());

        return CourtMapper.toDto(courtRepository.save(court));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        try {
            courtRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete court referenced by other records");
        }
    }
}
