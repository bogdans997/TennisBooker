package com.example.tennisbokker.entity;

import com.example.tennisbokker.entity.enums.TeamSide;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "match_players",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_match_player_unique", columnNames = {"match_result_id", "player_id"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_result_id", nullable = false)
    private MatchResult matchResult;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private User player;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private TeamSide team; // TEAM_A or TEAM_B

    // 1 or 2 (for doubles); for singles you'll only use slot=1 on each team
    @Column(nullable = false)
    private Integer slot;
}
