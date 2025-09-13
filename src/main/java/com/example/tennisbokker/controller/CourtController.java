package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.CourtCreateRequest;
import com.example.tennisbokker.dto.CourtResponseDto;
import com.example.tennisbokker.dto.CourtUpdateRequest;
import com.example.tennisbokker.service.CourtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class CourtController {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping("/courts")
    public List<CourtResponseDto> getAllCourts() {
        return courtService.findAll();
    }

    // GET /courts/{id}
    @GetMapping("/courts/{id}")
    public CourtResponseDto getCourtById(@PathVariable UUID id) {
        return courtService.findById(id);
    }

    // GET /clubs/{clubId}/courts
    @GetMapping("/clubs/{clubId}/courts")
    public List<CourtResponseDto> getCourtsByClub(@PathVariable UUID clubId) {
        return courtService.findAllByClub(clubId);
    }

    // POST /clubs/{clubId}/courts
    @PreAuthorize("@guards.isAdmin(authentication) or @guards.ownsClub(authentication, #clubId)")
    @PostMapping("/clubs/{clubId}/courts")
    public ResponseEntity<CourtResponseDto> createCourt(
            @PathVariable UUID clubId,
            @Valid @RequestBody CourtCreateRequest req,
            UriComponentsBuilder uriBuilder
    ) {
        CourtResponseDto created = courtService.createForClub(clubId, req);
        URI location = uriBuilder.path("/courts/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // PUT /courts/{id}?clubId=...
    @PreAuthorize("""
        @guards.isAdmin(authentication) or
        ( #clubId != null ? @guards.ownsClub(authentication, #clubId) : @guards.ownsCourt(authentication, #id) )
    """)
    @PutMapping("/courts/{id}")
    public ResponseEntity<CourtResponseDto> updateCourt(
            @PathVariable UUID id,
            @Valid @RequestBody CourtUpdateRequest req,
            @RequestParam(required = false) UUID clubId
    ) {
        CourtResponseDto updated = courtService.update(id, req, clubId);
        return ResponseEntity.ok(updated);
    }

    // DELETE /courts/{id}
    @PreAuthorize("@guards.isAdmin(authentication) or @guards.ownsCourt(authentication, #id)")
    @DeleteMapping("/courts/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable UUID id) {
        courtService.delete(id);
        return ResponseEntity.noContent().build();
    }
}