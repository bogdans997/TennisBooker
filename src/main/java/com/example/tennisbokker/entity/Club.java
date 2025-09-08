package com.example.tennisbokker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "clubs",
        uniqueConstraints = {
                // one owner cannot have two clubs with the same normalized name
                @UniqueConstraint(name = "uk_clubs_owner_normname", columnNames = {"owner_id", "name_normalized"})
        },
        indexes = {
                @Index(name = "idx_clubs_owner", columnList = "owner_id"),
                @Index(name = "idx_clubs_name_norm", columnList = "name_normalized"),
                @Index(name = "idx_clubs_location", columnList = "location")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 160)
    private String name;

    /**
     * Stored in lowercase for uniqueness per owner.
     */
    @Column(name = "name_normalized", nullable = false, length = 160)
    private String nameNormalized;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(name = "working_hours", length = 120)
    private String workingHours;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Court> courts = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    @PreUpdate
    void normalize() {
        if (name != null) name = name.trim();
        nameNormalized = (name == null ? null : name.toLowerCase());
        if (location != null) location = location.trim();
        if (workingHours != null) workingHours = workingHours.trim();
        if (description != null) description = description.trim();
    }
}