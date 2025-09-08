package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.ClubCreateRequest;
import com.example.tennisbokker.dto.ClubResponseDto;
import com.example.tennisbokker.dto.ClubUpdateRequest;
import com.example.tennisbokker.entity.Club;
import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.Role;
import com.example.tennisbokker.mapper.ClubMapper;
import com.example.tennisbokker.repository.ClubRepository;
import com.example.tennisbokker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public ClubResponseDto findById(UUID id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Club not found: " + id));
        return ClubMapper.toResponse(club);
    }

    @Override
    public Page<ClubResponseDto> findAll(UUID ownerId, String q, Pageable pageable) {
        Page<Club> page = clubRepository.search(ownerId, (q == null || q.isBlank()) ? null : q.trim(), pageable);
        return page.map(ClubMapper::toResponse);
    }

    @Override
    public ClubResponseDto create(ClubCreateRequest request) {
        UUID ownerId = request.ownerId();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found: " + ownerId));

        // Optional: enforce role == OWNER or ADMIN
        if (owner.getRole() != Role.CLUB_OWNER && owner.getRole() != Role.ADMIN) {
            throw new DataIntegrityViolationException("Owner user must have role OWNER or ADMIN");
        }

        String norm = request.name().trim().toLowerCase();
        if (clubRepository.existsByOwner_IdAndNameNormalized(owner.getId(), norm)) {
            throw new DataIntegrityViolationException("This owner already has a club named: " + request.name());
        }

        Club club = ClubMapper.fromCreate(request, owner);
        // nameNormalized is set by entity lifecycle
        Club saved = clubRepository.save(club);
        return ClubMapper.toResponse(saved);
    }

    @Override
    public ClubResponseDto update(UUID id, ClubUpdateRequest request) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Club not found: " + id));

        // If name changed, re-check uniqueness per owner
        String newNorm = request.name().trim().toLowerCase();
        if (!newNorm.equals(club.getNameNormalized())
                && clubRepository.existsByOwner_IdAndNameNormalized(club.getOwner().getId(), newNorm)) {
            throw new DataIntegrityViolationException(
                    "This owner already has a club named: " + request.name());
        }

        ClubMapper.applyUpdate(club, request);
        Club saved = clubRepository.save(club);
        return ClubMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!clubRepository.existsById(id)) {
            throw new EntityNotFoundException("Club not found: " + id);
        }
        clubRepository.deleteById(id);
    }
}