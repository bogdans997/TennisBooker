package com.example.tennisbokker.entity;

import com.example.tennisbokker.entity.enums.SurfaceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "courts",
        uniqueConstraints = @UniqueConstraint(name = "uk_court_name_per_club", columnNames = {"club_id", "name"}),
        indexes = @Index(name = "idx_courts_club_id", columnList = "club_id")
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private SurfaceType surfaceType;

    @Column(name = "price_single")
    private BigDecimal priceSingle;

    @Column(name = "price_double")
    private BigDecimal priceDouble;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "club_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Club club;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Appointment> appointments;
}