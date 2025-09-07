package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.CreateCourtRequest;
import com.example.tennisbokker.dto.ResponseCourtDto;
import com.example.tennisbokker.entity.Club;
import com.example.tennisbokker.entity.Court;
import com.example.tennisbokker.mapper.CourtMapper;
import com.example.tennisbokker.repository.ClubRepository;
import com.example.tennisbokker.repository.CourtRepository;
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
    public ResponseCourtDto createForClub(UUID clubId, CreateCourtRequest req) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found"));

        Court court = new Court();
        court.setName(req.name());
        court.setSurfaceType(req.surfaceType());
        court.setPriceSingle(req.priceSingle());
        court.setPriceDouble(req.priceDouble());
        court.setClub(club);                    // <-- attach the club

        Court saved = courtRepository.save(court);
        return CourtMapper.toDto(saved);
    }

    @Override
    public ResponseCourtDto update(UUID id, CreateCourtRequest req, UUID clubId) {
        Court court = courtRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Court not found"));

        if (clubId != null) {
            Club club = clubRepository.findById(clubId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found"));
            court.setClub(club);
        }

        court.setName(req.name());
        court.setSurfaceType(req.surfaceType());
        court.setPriceSingle(req.priceSingle());
        court.setPriceDouble(req.priceDouble());

        return CourtMapper.toDto(courtRepository.save(court));
    }

    @Override
    public void delete(UUID id) {
        courtRepository.deleteById(id);
    }
}
