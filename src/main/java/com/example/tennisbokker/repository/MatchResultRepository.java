package com.example.tennisbokker.repository;

import com.example.tennisbokker.entity.MatchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, UUID> {

    Optional<MatchResult> findByAppointment_Id(UUID appointmentId);

    boolean existsByAppointment_Id(UUID appointmentId);

    // Fetch graph to avoid N+1 / Lazy problems during mapping
    @EntityGraph(attributePaths = {
            "appointment",
            "appointment.court",
            "appointment.court.club",
            "participants",
            "participants.player"
    })
    @Query("""
      select distinct mr from MatchResult mr
      join mr.appointment a
      left join mr.participants mp
      where (mp.player.id = :userId or a.bookedBy.id = :userId)
        and a.startTime >= :from and a.startTime < :to
      order by a.startTime desc
    """)
    Page<MatchResult> findUserTimeline(UUID userId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    @EntityGraph(attributePaths = {
            "appointment",
            "appointment.court",
            "appointment.court.club",
            "participants",
            "participants.player"
    })
    @Query("""
      select mr from MatchResult mr
      join mr.appointment a
      where a.startTime >= :from and a.startTime < :to
        and (:clubId is null or a.court.club.id = :clubId)
      order by a.startTime asc
    """)
    List<MatchResult> findByRange(LocalDateTime from, LocalDateTime to, UUID clubId);
}