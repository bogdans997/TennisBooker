package com.example.tennisbokker.security;

import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.Role;
import com.example.tennisbokker.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Starts full context with our security filter
@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired UserRepository users;

    @TestConfiguration
    static class StubTokenVerifierConfig {
        @Bean
        @Primary
        TokenVerifier tokenVerifier() {
            // Always "verify" and return a fixed user
            return idToken -> new TokenVerifier.DecodedToken(
                    "uid-123", "owner@example.com", "Owner One");
        }
    }

    @BeforeEach
    void seedUser(@Autowired UserRepository repo) {
        // Ensure DB has a user with OWNER role that matches the stubbed email
        repo.findByEmail("owner@example.com".toLowerCase(Locale.ROOT))
                .orElseGet(() -> {
                    var u = new User();
                    u.setFullName("Owner One");
                    u.setEmail("owner@example.com");
                    u.setRole(Role.CLUB_OWNER);
                    return repo.save(u);
                });
    }

    @Test
    void me_returns_profile() throws Exception {
        mvc.perform(get("/api/v1/me").header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("owner@example.com"))
                .andExpect(jsonPath("$.role").value("CLUB_OWNER"))
                .andExpect(jsonPath("$.capabilities.canCreateClub").value(true));
    }
}
