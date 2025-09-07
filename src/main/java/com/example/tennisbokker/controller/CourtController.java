package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.CreateCourtRequest;
import com.example.tennisbokker.dto.ResponseCourtDto;
import com.example.tennisbokker.entity.Court;
import com.example.tennisbokker.service.CourtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courts")
public class CourtController {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCourtDto> getCourtById(@PathVariable UUID id) {
        return ResponseEntity.ok(courtService.findById(id));
    }

    @GetMapping
    public List<ResponseCourtDto> getAllCourts() {
        return courtService.findAll();
    }

    @PostMapping("/club/{clubId}")
    public ResponseEntity<ResponseCourtDto> createCourt(
            @PathVariable("clubId") UUID clubId,
            @RequestBody CreateCourtRequest body) {
        ResponseCourtDto created = courtService.createForClub(clubId, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCourtDto> updateCourt(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID clubId, // optional: move court to another club
            @Valid @RequestBody CreateCourtRequest body) {
        return ResponseEntity.ok(courtService.update(id, body, clubId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable UUID id) {
        courtService.delete(id);
        return ResponseEntity.noContent().build();
    }
}