package com.example.tennisbokker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "match_results")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"appointment", "participants"})
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    // FIX: @Builder.Default requires an initializer. Also make it NOT NULL with a safe default.
    @Column(nullable = false)
    @Builder.Default
    private String resultText = "PENDING";

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "matchResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MatchPlayer> participants = new ArrayList<>();

    public void setParticipants(List<MatchPlayer> players) {
        this.participants.clear();
        if (players != null) {
            players.forEach(p -> p.setMatchResult(this));
            this.participants.addAll(players);
        }
    }

    public void addParticipant(MatchPlayer p) {
        if (p == null) return;
        p.setMatchResult(this);
        this.participants.add(p);
    }

    public void removeParticipant(MatchPlayer p) {
        if (p == null) return;
        this.participants.remove(p);
        p.setMatchResult(null);
    }
}