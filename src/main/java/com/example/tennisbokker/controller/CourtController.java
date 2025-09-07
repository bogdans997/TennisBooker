package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.CreateCourtRequest;
import com.example.tennisbokker.dto.ResponseCourtDto;
import com.example.tennisbokker.dto.UpdateCourtRequest;
import com.example.tennisbokker.entity.Court;
import com.example.tennisbokker.service.CourtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<ResponseCourtDto> getAllCourts() {
        return courtService.findAll();
    }

    // GET /courts/{id}
    @GetMapping("/courts/{id}")
    public ResponseCourtDto getCourtById(@PathVariable UUID id) {
        return courtService.findById(id);
    }

    // GET /clubs/{clubId}/courts
    @GetMapping("/clubs/{clubId}/courts")
    public List<ResponseCourtDto> getCourtsByClub(@PathVariable UUID clubId) {
        return courtService.findAllByClub(clubId);
    }

    // POST /clubs/{clubId}/courts
    @PostMapping("/clubs/{clubId}/courts")
    public ResponseEntity<ResponseCourtDto> createCourt(
            @PathVariable UUID clubId,
            @Valid @RequestBody CreateCourtRequest req,
            UriComponentsBuilder uriBuilder
    ) {
        ResponseCourtDto created = courtService.createForClub(clubId, req);
        URI location = uriBuilder.path("/courts/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // PUT /courts/{id}?clubId=...
    @PutMapping("/courts/{id}")
    public ResponseEntity<ResponseCourtDto> updateCourt(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCourtRequest req,
            @RequestParam(required = false) UUID clubId
    ) {
        ResponseCourtDto updated = courtService.update(id, req, clubId);
        return ResponseEntity.ok(updated);
    }

    // DELETE /courts/{id}
    @DeleteMapping("/courts/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable UUID id) {
        courtService.delete(id);
        return ResponseEntity.noContent().build();
    }
}