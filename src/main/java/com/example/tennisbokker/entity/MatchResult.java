package com.example.tennisbokker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "match_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(nullable = false)
    private String resultText;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "matchResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchPlayer> participants = new ArrayList<>();

    public void setParticipants(List<MatchPlayer> players) {
        this.participants.clear();
        if (players != null) {
            players.forEach(p -> p.setMatchResult(this));
            this.participants.addAll(players);
        }
    }
}