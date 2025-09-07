package com.example.tennisbokker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import com.example.tennisbokker.entity.enums.Role;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        indexes = {
                @Index(name = "idx_users_email", columnList = "email"),
                @Index(name = "idx_users_role", columnList = "role")
        }
)
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@ToString(exclude = "password") // never log password
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, length = 190) // keep under typical index limits
    private String email;

    /**
     * If you later fully outsource auth to Firebase, you can drop this column.
     * For now, keep it nullable to ease the transition.
     */
    @Column(name = "password_hash")
    private String password;

    @Column(name = "phone_number", length = 32)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Role role;

    /** ISO code like "en" or "sr"; keep flexible as string for early MVP */
    @Column(name = "preferred_language", length = 8)
    private String preferredLanguage;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    /** Normalize email before insert/update */
    @PrePersist @PreUpdate
    void normalize() {
        if (email != null) email = email.trim().toLowerCase();
        if (preferredLanguage != null) preferredLanguage = preferredLanguage.trim().toLowerCase();
        if (fullName != null) fullName = fullName.trim();
        if (phoneNumber != null) phoneNumber = phoneNumber.trim();
    }
}