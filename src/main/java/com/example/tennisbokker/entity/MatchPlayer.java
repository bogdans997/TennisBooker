package com.example.tennisbokker.entity;

import com.example.tennisbokker.entity.enums.TeamSide;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "match_players",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_match_player_unique", columnNames = {"match_result_id", "player_id"})
        })
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"matchResult", "player"})
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

    @Column(nullable = false)
    private Integer slot;  // 1 or 2
}
