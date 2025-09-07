package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.CreateAppointmentRequest;
import com.example.tennisbokker.dto.ResponseAppointmentDto;
import com.example.tennisbokker.entity.*;
import com.example.tennisbokker.mapper.AppointmentMapper;
import com.example.tennisbokker.repository.AppointmentRepository;
import com.example.tennisbokker.repository.CourtRepository;
import com.example.tennisbokker.repository.MatchResultRepository;
import com.example.tennisbokker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;
    private final MatchResultRepository matchResultRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, UserRepository userRepository, CourtRepository courtRepository,  MatchResultRepository matchResultRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.courtRepository = courtRepository;
        this.matchResultRepository = matchResultRepository;
    }

    @Override
    public Appointment findById(UUID id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        return appointment.orElse(null);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public ResponseAppointmentDto create(UUID courtId, UUID bookedByUserId, CreateAppointmentRequest req) {
        // Basic validation
        if (!req.endTime().isAfter(req.startTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "endTime must be after startTime");
        }

        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Court not found"));
        Club club = court.getClub(); // for DTO only

        User bookedBy = userRepository.findById(bookedByUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User (bookedBy) not found"));

        User coach = null;
        if (req.coachId() != null) {
            coach = userRepository.findById(req.coachId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach not found"));
            // (optional) check coach role == COACH here
        }

        // Overlap checks
        if (appointmentRepository.countOverlapsOnCourt(courtId, req.startTime(), req.endTime()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Court is already booked for this time");
        }
        if (coach != null && appointmentRepository.countOverlapsForCoach(coach.getId(), req.startTime(), req.endTime()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Coach is not available for this time");
        }
        // optional policy: at most one upcoming appt for a user
        if (appointmentRepository.countUpcomingForUser(bookedBy.getId(), LocalDateTime.now()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has an upcoming appointment");
        }
        if (appointmentRepository.countOverlapsForUser(bookedBy.getId(), req.startTime(), req.endTime()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has an appointment in this time window");
        }

        // Price: use request if provided; else derive from court by type
        BigDecimal price = req.price();
        if (price == null) {
            price = switch (req.type()) {
                case SINGLE -> court.getPriceSingle();
                case DOUBLE -> court.getPriceDouble();
            };
        }

        Appointment a = new Appointment();
        a.setCourt(court);
        a.setCoach(coach);
        a.setBookedBy(bookedBy);
        a.setStartTime(req.startTime());
        a.setEndTime(req.endTime());
        a.setType(req.type());
        a.setPrice(price);

        Appointment saved = appointmentRepository.save(a);
        // ensure club is accessible for DTO (LAZY ok since court->club is in same session)
        if (club != null) saved.getCourt().setClub(club);

        // Auto-create an empty match result if not exists
        if (!matchResultRepository.existsByAppointment_Id(saved.getId())) {
            MatchResult mr = new MatchResult();
            mr.setAppointment(saved);
            matchResultRepository.save(mr);
        }

        return AppointmentMapper.toDto(saved);
    }

    @Override
    public Appointment update(UUID id, Appointment appointment) {
        if (!appointmentRepository.existsById(id)) {
            return null;
        }
        appointment.setId(id);
        return appointmentRepository.save(appointment);
    }

    @Override
    public void delete(UUID id) {
        appointmentRepository.deleteById(id);
    }
}