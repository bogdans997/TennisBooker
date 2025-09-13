package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.AppointmentCreateRequest;
import com.example.tennisbokker.dto.AppointmentResponseDto;
import com.example.tennisbokker.dto.AppointmentUpdateRequest;
import com.example.tennisbokker.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("@guards.isAdmin(authentication) or @guards.ownsAppointment(authentication, #id)")
    @GetMapping("/appointments/{id}")
    public ResponseEntity<AppointmentResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/appointments")
    public ResponseEntity<Page<AppointmentResponseDto>> list(
            @RequestParam(required = false) UUID courtId,
            @RequestParam(required = false) UUID clubId,
            @RequestParam(required = false) UUID coachId,
            @RequestParam(required = false) UUID bookedById,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            Pageable pageable
    ) {
        return ResponseEntity.ok(appointmentService.findAll(courtId, clubId, coachId, bookedById, from, to, pageable));
    }

    // Create under a court context: POST /api/v1/courts/{courtId}/appointments?bookedBy={userId}
    @PreAuthorize("@guards.isAdmin(authentication) or @guards.isSelf(authentication, #bookedByUserId)")
    @PostMapping("/courts/{courtId}/appointments")
    public ResponseEntity<AppointmentResponseDto> create(
            @PathVariable UUID courtId,
            @RequestParam("bookedBy") UUID bookedByUserId,
            @Valid @RequestBody AppointmentCreateRequest body
    ) {
        AppointmentResponseDto created = appointmentService.create(courtId, bookedByUserId, body);
        return ResponseEntity
                .created(URI.create("/api/v1/appointments/" + created.id()))
                .body(created);
    }

    @PreAuthorize("@guards.isAdmin(authentication) or @guards.ownsAppointment(authentication, #id)")
    @PutMapping("/appointments/{id}")
    public ResponseEntity<AppointmentResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentUpdateRequest body
    ) {
        return ResponseEntity.ok(appointmentService.update(id, body));
    }

    @PreAuthorize("@guards.isAdmin(authentication) or @guards.ownsAppointment(authentication, #id)")
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}