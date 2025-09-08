package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.CreateOrUpdateMatchResultRequest;
import com.example.tennisbokker.dto.MatchResultResponseDto;
import com.example.tennisbokker.service.MatchResultService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class MatchResultController {

    private final MatchResultService service;

    public MatchResultController(MatchResultService service) {
        this.service = service;
    }

    // --- Single item endpoints bound to an appointment ---
    @GetMapping("/appointments/{appointmentId}/match-result")
    public ResponseEntity<MatchResultResponseDto> get(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(service.findByAppointmentId(appointmentId));
    }

    @PutMapping("/appointments/{appointmentId}/match-result")
    public ResponseEntity<MatchResultResponseDto> update(
            @PathVariable UUID appointmentId,
            @Valid @RequestBody CreateOrUpdateMatchResultRequest body) {
        return ResponseEntity.ok(service.update(appointmentId, body));
    }

    @DeleteMapping("/appointments/{appointmentId}/match-result")
    public ResponseEntity<Void> delete(@PathVariable UUID appointmentId) {
        service.delete(appointmentId);
        return ResponseEntity.noContent().build();
    }

    // --- Timeline for the authenticated user (newest → oldest) ---
    @GetMapping("/me/match-results")
    public Page<MatchResultResponseDto> myResults(
            @AuthenticationPrincipal(expression = "id") UUID me,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size); // order handled in repo query
        return service.listForUser(me, from, to, pageable);
    }

    // --- Timeline for any user (admin / debug) ---
    @GetMapping("/users/{userId}/match-results")
    public Page<MatchResultResponseDto> userResults(
            @PathVariable UUID userId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return service.listForUser(userId, from, to, pageable);
    }

    // --- Range endpoint for calendar feeds ---
    @GetMapping("/match-results")
    public List<MatchResultResponseDto> resultsInRange(
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(required = false) UUID clubId) {
        return service.listRange(from, to, clubId);
    }
}