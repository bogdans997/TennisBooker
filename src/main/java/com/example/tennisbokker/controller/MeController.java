package com.example.tennisbokker.controller;

import com.example.tennisbokker.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class MeController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        UserPrincipal u = (UserPrincipal) auth.getPrincipal();
        var caps = Map.of(
                "canCreateClub", u.role().name().equals("ADMIN") || u.role().name().equals("OWNER"),
                "canBookCourt", true,
                "canCoach", u.role().name().equals("COACH") || u.role().name().equals("ADMIN")
        );
        return ResponseEntity.ok(Map.of(
                "id", u.id(),
                "email", u.email(),
                "fullName", u.fullName(),
                "role", u.role(),
                "capabilities", caps
        ));
    }
}
