package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.AppointmentCreateRequest;
import com.example.tennisbokker.dto.AppointmentResponseDto;
import com.example.tennisbokker.dto.AppointmentUpdateRequest;
import com.example.tennisbokker.entity.Appointment;
import com.example.tennisbokker.entity.Court;
import com.example.tennisbokker.entity.MatchResult;
import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.AppointmentType;
import com.example.tennisbokker.mapper.AppointmentMapper;
import com.example.tennisbokker.repository.AppointmentRepository;
import com.example.tennisbokker.repository.CourtRepository;
import com.example.tennisbokker.repository.MatchResultRepository;
import com.example.tennisbokker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;
    private final MatchResultRepository matchResultRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, UserRepository userRepository, CourtRepository courtRepository, MatchResultRepository matchResultRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.courtRepository = courtRepository;
        this.matchResultRepository = matchResultRepository;
    }

    @Override
    public AppointmentResponseDto findById(UUID id) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found: " + id));
        return AppointmentMapper.toResponse(a);
    }

    @Override
    public Page<AppointmentResponseDto> findAll(UUID courtId, UUID clubId, UUID coachId, UUID bookedById,
                                                LocalDateTime from, LocalDateTime to, Pageable pageable) {
        Page<Appointment> page = appointmentRepository.search(courtId, clubId, coachId, bookedById, from, to, pageable);
        return page.map(AppointmentMapper::toResponse);
    }

    @Override
    public AppointmentResponseDto create(UUID courtId, UUID bookedByUserId, AppointmentCreateRequest req) {
        validateTimes(req.startTime(), req.endTime());

        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new EntityNotFoundException("Court not found: " + courtId));

        User bookedBy = userRepository.findById(bookedByUserId)
                .orElseThrow(() -> new EntityNotFoundException("User (bookedBy) not found: " + bookedByUserId));

        User coach = null;
        if (req.coachId() != null) {
            coach = userRepository.findById(req.coachId())
                    .orElseThrow(() -> new EntityNotFoundException("Coach not found: " + req.coachId()));
            // Optionally validate coach role == COACH here
        }

        checkOverlaps(courtId, req.startTime(), req.endTime(), coach, bookedBy);

        BigDecimal price = (req.price() != null) ? req.price() : derivePrice(court, req.type());

        Appointment a = Appointment.builder()
                .court(court)
                .coach(coach)
                .bookedBy(bookedBy)
                .startTime(req.startTime())
                .endTime(req.endTime())
                .type(req.type())
                .price(price)
                .build();

        Appointment saved = appointmentRepository.save(a);

        // Auto-create empty MatchResult if not exists
        if (!matchResultRepository.existsByAppointment_Id(saved.getId())) {
            MatchResult mr = new MatchResult();
            mr.setAppointment(saved);
            matchResultRepository.save(mr);
        }

        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponseDto update(UUID id, AppointmentUpdateRequest req) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found: " + id));

        validateTimes(req.startTime(), req.endTime());

        User coach = null;
        if (req.coachId() != null) {
            coach = userRepository.findById(req.coachId())
                    .orElseThrow(() -> new EntityNotFoundException("Coach not found: " + req.coachId()));
        }

        // Re-check overlap against others (excluding this appt). Simple approach: count overlaps and subtract self if found
        long courtOverlaps = appointmentRepository.countOverlapsOnCourt(existing.getCourt().getId(), req.startTime(), req.endTime());
        if (courtOverlaps > 0 && (req.startTime().isAfter(existing.getEndTime()) || req.endTime().isBefore(existing.getStartTime()))) {
            // not same window as before, so conflict is real
            throw new DataIntegrityViolationException("Court is already booked for this time");
        }
        if (coach != null) {
            long coachOverlaps = appointmentRepository.countOverlapsForCoach(coach.getId(), req.startTime(), req.endTime());
            if (coachOverlaps > 0 && (existing.getCoach() == null || !coach.getId().equals(existing.getCoach().getId()))) {
                throw new DataIntegrityViolationException("Coach is not available for this time");
            }
        }

        long userOverlap = appointmentRepository.countOverlapsForUser(existing.getBookedBy().getId(), req.startTime(), req.endTime());
        if (userOverlap > 0 && !(req.startTime().equals(existing.getStartTime()) && req.endTime().equals(existing.getEndTime()))) {
            throw new DataIntegrityViolationException("User already has an appointment in this time window");
        }

        existing.setStartTime(req.startTime());
        existing.setEndTime(req.endTime());
        existing.setType(req.type());
        existing.setCoach(coach);
        existing.setPrice(req.price() != null ? req.price() : derivePrice(existing.getCourt(), req.type()));

        Appointment saved = appointmentRepository.save(existing);
        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!appointmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Appointment not found: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    /* Helpers */

    private static void validateTimes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new DataIntegrityViolationException("endTime must be after startTime");
        }
    }

    private void checkOverlaps(UUID courtId, LocalDateTime start, LocalDateTime end, User coach, User bookedBy) {
        if (appointmentRepository.countOverlapsOnCourt(courtId, start, end) > 0) {
            throw new DataIntegrityViolationException("Court is already booked for this time");
        }
        if (coach != null && appointmentRepository.countOverlapsForCoach(coach.getId(), start, end) > 0) {
            throw new DataIntegrityViolationException("Coach is not available for this time");
        }
        if (appointmentRepository.countUpcomingForUser(bookedBy.getId(), LocalDateTime.now()) > 0) {
            throw new DataIntegrityViolationException("User already has an upcoming appointment");
        }
        if (appointmentRepository.countOverlapsForUser(bookedBy.getId(), start, end) > 0) {
            throw new DataIntegrityViolationException("User already has an appointment in this time window");
        }
    }

    private static BigDecimal derivePrice(Court court, AppointmentType type) {
        return switch (type) {
            case SINGLE -> court.getPriceSingle();
            case DOUBLE -> court.getPriceDouble();
        };
    }
}