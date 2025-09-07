package com.example.tennisbokker.entity;

import com.example.tennisbokker.entity.enums.AppointmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "appointments",
        indexes = {
                @Index(name = "idx_appt_court_time", columnList = "court_id,startTime,endTime"),
                @Index(name = "idx_appt_coach_time", columnList = "coach_id,startTime,endTime"),
                @Index(name = "idx_appt_bookedby_time", columnList = "booked_by_id,startTime,endTime")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    // Optional: a training/lesson with a coach
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id")
    private User coach;

    // The user who made the booking (required)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booked_by_id", nullable = false)
    private User bookedBy;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type;

    private BigDecimal price;
}