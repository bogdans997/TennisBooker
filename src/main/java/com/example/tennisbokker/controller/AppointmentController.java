package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.CreateAppointmentRequest;
import com.example.tennisbokker.dto.ResponseAppointmentDto;
import com.example.tennisbokker.entity.Appointment;
import com.example.tennisbokker.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID id) {
        Appointment appointment = appointmentService.findById(id);
        return appointment != null ? ResponseEntity.ok(appointment) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseAppointmentDto> createAppointment(
            @PathVariable UUID courtId,
            @Valid @RequestBody CreateAppointmentRequest body,
            @RequestParam UUID userId // <-- your SecurityUser has .getId()
    ) {
        ResponseAppointmentDto created = appointmentService.create(courtId, userId, body);
        URI location = URI.create("/appointments/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment) {
        Appointment updated = appointmentService.update(id, appointment);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}