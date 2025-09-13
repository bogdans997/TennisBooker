package com.example.tennisbokker.security;

import com.example.tennisbokker.repository.AppointmentRepository;
import com.example.tennisbokker.repository.ClubRepository;
import com.example.tennisbokker.repository.CourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("guards")
@RequiredArgsConstructor
public class Guards {

    private final ClubRepository clubs;
    private final AppointmentRepository appts;
    private final CourtRepository courts;

    public boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isSelf(Authentication auth, UUID userId) {
        if (auth == null) return false;
        var p = (UserPrincipal) auth.getPrincipal();
        return p.id().equals(userId);
    }

    public boolean ownsClub(Authentication auth, UUID clubId) {
        if (isAdmin(auth)) return true;
        var p = (UserPrincipal) auth.getPrincipal();
        return clubs.findById(clubId)
                .map(c -> c.getOwner().getId().equals(p.id()))
                .orElse(false);
    }

    public boolean ownsAppointment(Authentication auth, UUID apptId) {
        if (isAdmin(auth)) return true;
        var p = (UserPrincipal) auth.getPrincipal();
        return appts.findById(apptId)
                .map(a -> a.getBookedBy().getId().equals(p.id()))
                .orElse(false);
    }

    public boolean ownsCourt(Authentication auth, UUID courtId) {
        if (isAdmin(auth)) return true;
        var p = (UserPrincipal) auth.getPrincipal();
        return courts.findById(courtId)
                .map(ct -> ct.getClub().getOwner().getId().equals(p.id()))
                .orElse(false);
    }

}
