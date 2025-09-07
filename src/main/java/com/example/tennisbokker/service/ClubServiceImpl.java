package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.ResponseClubDto;
import com.example.tennisbokker.dto.CreateClubRequest;
import com.example.tennisbokker.entity.Club;
import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.mapper.ClubMapper;
import com.example.tennisbokker.repository.ClubRepository;
import com.example.tennisbokker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    public ClubServiceImpl(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseClubDto findById(UUID id) {
        return clubRepository.findByIdWithOwner(id)
                .map(ClubMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<ResponseClubDto> findAll() {
        return clubRepository.findAllWithOwner().stream()
                .map(club -> new ResponseClubDto(
                        club.getId(),
                        club.getName(),
                        club.getLocation(),
                        club.getWorkingHours(),
                        club.getDescription(),
                        club.getOwner() != null ? club.getOwner().getFullName() : null
                ))
                .toList();
    }

    @Override
    public Club create(CreateClubRequest createClubRequest) {
        User owner = userRepository.findById(createClubRequest.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        Club club = new Club();
        club.setName(createClubRequest.getName());
        club.setLocation(createClubRequest.getLocation());
        club.setWorkingHours(createClubRequest.getWorkingHours());
        club.setDescription(createClubRequest.getDescription());
        club.setOwner(owner);
        return clubRepository.save(club);
    }

    @Override
    public Club update(UUID id, Club club) {
        if (!clubRepository.existsById(id)) {
            return null;
        }
        club.setId(id);
        return clubRepository.save(club);
    }

    @Override
    public void delete(UUID id) {
        clubRepository.deleteById(id);
    }
}