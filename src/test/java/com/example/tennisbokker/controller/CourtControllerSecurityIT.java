package com.example.tennisbokker.controller;

import com.example.tennisbokker.entity.Club;
import com.example.tennisbokker.entity.Court;
import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.Role;
import com.example.tennisbokker.entity.enums.SurfaceType;
import com.example.tennisbokker.repository.ClubRepository;
import com.example.tennisbokker.repository.CourtRepository;
import com.example.tennisbokker.repository.UserRepository;
import com.example.tennisbokker.security.TokenVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CourtController security rules using a stubbed TokenVerifier.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CourtControllerSecurityIT {

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository users;
    @Autowired
    ClubRepository clubs;
    @Autowired
    CourtRepository courts;
    @Autowired
    ObjectMapper objectMapper;

    UUID club1Id;
    UUID club2Id;
    UUID court1Id;

    @TestConfiguration
    static class StubTokenVerifierConfig {
        @Bean
        @Primary
        TokenVerifier tokenVerifier() {
            return idToken -> switch (idToken) {
                case "owner-token" -> new TokenVerifier.DecodedToken("uid-owner", "owner@example.com", "Owner One");
                case "player-token" -> new TokenVerifier.DecodedToken("uid-player", "player@example.com", "Player One");
                case "admin-token" -> new TokenVerifier.DecodedToken("uid-admin", "admin@example.com", "Admin One");
                default -> throw new IllegalArgumentException("Unknown stub token: " + idToken);
            };
        }
    }

    @BeforeEach
    void setup() {
        // Clean slate
        courts.deleteAll();
        clubs.deleteAll();
        users.deleteAll();

        // Seed users
        users.save(user("owner@example.com", "Owner One", Role.CLUB_OWNER));
        users.save(user("player@example.com", "Player One", Role.PLAYER));
        users.save(user("admin@example.com", "Admin One", Role.ADMIN));
        users.save(user("owner2@example.com", "Owner Two", Role.CLUB_OWNER));

        // Seed clubs
        var owner = users.findByEmail("owner@example.com".toLowerCase(Locale.ROOT)).orElseThrow();
        var owner2 = users.findByEmail("owner2@example.com".toLowerCase(Locale.ROOT)).orElseThrow();

        var c1 = new Club();
        c1.setName("Club One");
        c1.setLocation("Belgrade");
        c1.setOwner(owner);
        c1.setWorkingHours("08:00-22:00");
        clubs.save(c1);
        club1Id = c1.getId();

        var c2 = new Club();
        c2.setName("Club Two");
        c2.setLocation("Novi Sad");
        c2.setOwner(owner2);
        c2.setWorkingHours("08:00-22:00");
        clubs.save(c2);
        club2Id = c2.getId();

        // Seed a court under club1
        var ct = new Court();
        ct.setName("Court A");
        ct.setClub(c1);
        ct.setSurfaceType(SurfaceType.CLAY); // adjust if enum differs
        ct.setPriceSingle(new BigDecimal("10.00"));
        ct.setPriceDouble(new BigDecimal("15.00"));
        courts.save(ct);
        court1Id = ct.getId();
    }

    /* ---------- TESTS ---------- */

    @Test
    void owner_can_create_court_in_his_club() throws Exception {
        var body = """
            {
              "name": "Court B",
              "surfaceType": "CLAY",
              "priceSingle": 12.00,
              "priceDouble": 18.00
            }
            """;

        mvc.perform(post("/api/v1/clubs/{clubId}/courts", club1Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer owner-token"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("Court B"));

        assertThat(courts.findAll().size()).isGreaterThan(1);
    }

    @Test
    void player_cannot_create_court_in_someones_club() throws Exception {
        var body = """
            {
              "name": "Court C",
              "surfaceType": "HARD",
              "priceSingle": 11.00,
              "priceDouble": 16.50
            }
            """;

        mvc.perform(post("/api/v1/clubs/{clubId}/courts", club1Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer player-token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void owner_can_update_court_in_his_club() throws Exception {
        var body = """
            {
              "name": "Court A - Updated",
              "surfaceType": "CLAY",
              "priceSingle": 13.00,
              "priceDouble": 19.00
            }
            """;

        mvc.perform(put("/api/v1/courts/{id}", court1Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer owner-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Court A - Updated"));
    }

    @Test
    void owner_cannot_move_court_to_other_owners_club() throws Exception {
        var body = """
            {
              "name": "Court A",
              "surfaceType": "CLAY",
              "priceSingle": 10.00,
              "priceDouble": 15.00
            }
            """;

        // Attempt to move court1 from club1 to club2 while authenticated as owner of club1 (not owner of club2)
        mvc.perform(put("/api/v1/courts/{id}?clubId={clubId}", court1Id, club2Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer owner-token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_can_move_court_between_clubs() throws Exception {
        var body = """
            {
              "name": "Court A",
              "surfaceType": "CLAY",
              "priceSingle": 10.00,
              "priceDouble": 15.00
            }
            """;

        mvc.perform(put("/api/v1/courts/{id}?clubId={clubId}", court1Id, club2Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer admin-token"))
                .andExpect(status().isOk());
    }

    /* ---------- helpers ---------- */

    private static User user(String email, String name, Role role) {
        var u = new User();
        u.setEmail(email.toLowerCase(Locale.ROOT));
        u.setFullName(name);
        u.setRole(role);
        return u;
    }
}
