package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.ClubCreateRequest;
import com.example.tennisbokker.dto.ClubResponseDto;
import com.example.tennisbokker.dto.ClubUpdateRequest;
import com.example.tennisbokker.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(clubService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ClubResponseDto>> list(
            @RequestParam(required = false) UUID ownerId,
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        return ResponseEntity.ok(clubService.findAll(ownerId, q, pageable));
    }

    @PostMapping
    public ResponseEntity<ClubResponseDto> create(@Valid @RequestBody ClubCreateRequest request) {
        ClubResponseDto created = clubService.create(request);
        return ResponseEntity
                .created(URI.create("/api/v1/clubs/" + created.id()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ClubUpdateRequest request
    ) {
        return ResponseEntity.ok(clubService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clubService.delete(id);
        return ResponseEntity.noContent().build();
    }
}