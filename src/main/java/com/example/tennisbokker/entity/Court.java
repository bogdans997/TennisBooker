package com.example.tennisbokker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "courts",
    uniqueConstraints = @UniqueConstraint(name = "uk_court_name_per_club",columnNames = {"club_id", "name"}),
    indexes = @Index(name = "idx_courts_club_id", columnList = "club_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String surfaceType;

    @Column(name = "price_single")
    private BigDecimal priceSingle;

    @Column(name = "price_double")
    private BigDecimal priceDouble;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;
}