package com.example.tennisbokker.repository;

import com.example.tennisbokker.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // Overlap if existing.end > new.start AND existing.start < new.end
    @Query("""
           select count(a) from Appointment a
           where a.court.id = :courtId
             and a.endTime > :start
             and a.startTime < :end
           """)
    long countOverlapsOnCourt(UUID courtId, LocalDateTime start, LocalDateTime end);

    @Query("""
           select count(a) from Appointment a
           where a.coach.id = :coachId
             and a.endTime > :start
             and a.startTime < :end
           """)
    long countOverlapsForCoach(UUID coachId, LocalDateTime start, LocalDateTime end);

    @Query("""
           select count(a) from Appointment a
           where a.bookedBy.id = :userId
             and a.endTime > :start
             and a.startTime < :end
           """)
    long countOverlapsForUser(UUID userId, LocalDateTime start, LocalDateTime end);

    // If you want to enforce "a user may have only 1 upcoming appointment" rule:
    @Query("""
           select count(a) from Appointment a
           where a.bookedBy.id = :userId
             and a.startTime > :now
           """)
    long countUpcomingForUser(UUID userId, LocalDateTime now);
}