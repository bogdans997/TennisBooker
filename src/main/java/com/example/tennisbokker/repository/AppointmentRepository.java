package com.example.tennisbokker.repository;

import com.example.tennisbokker.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    long countOverlapsOnCourt(@Param("courtId") UUID courtId,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end);

    @Query("""
            select count(a) from Appointment a
            where a.coach.id = :coachId
              and a.endTime > :start
              and a.startTime < :end
            """)
    long countOverlapsForCoach(@Param("coachId") UUID coachId,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    @Query("""
            select count(a) from Appointment a
            where a.bookedBy.id = :userId
              and a.endTime > :start
              and a.startTime < :end
            """)
    long countOverlapsForUser(@Param("userId") UUID userId,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end);

    @Query("""
            select count(a) from Appointment a
            where a.bookedBy.id = :userId
              and a.startTime > :now
            """)
    long countUpcomingForUser(@Param("userId") UUID userId,
                              @Param("now") LocalDateTime now);

    @Query("""
            select a from Appointment a
              join a.court c
              join c.club club
            where (:courtId is null or c.id = :courtId)
              and (:clubId is null or club.id = :clubId)
              and (:coachId is null or a.coach.id = :coachId)
              and (:bookedById is null or a.bookedBy.id = :bookedById)
              and (:fromTs is null or a.endTime >= :fromTs)
              and (:toTs is null or a.startTime <= :toTs)
            """)
    Page<Appointment> search(@Param("courtId") UUID courtId,
                             @Param("clubId") UUID clubId,
                             @Param("coachId") UUID coachId,
                             @Param("bookedById") UUID bookedById,
                             @Param("fromTs") LocalDateTime fromTs,
                             @Param("toTs") LocalDateTime toTs,
                             Pageable pageable);
}