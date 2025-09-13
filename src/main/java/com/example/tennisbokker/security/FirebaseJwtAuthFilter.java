package com.example.tennisbokker.security;

import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.Role;
import com.example.tennisbokker.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class FirebaseJwtAuthFilter extends OncePerRequestFilter {

    private final TokenVerifier tokenVerifier;
    private final UserRepository users;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = header.substring(7);
        try {
            var decoded = tokenVerifier.verify(token);
            String email = decoded.email();
            String uid = decoded.uid();
            String name = decoded.name();

            User user = (email != null)
                    ? users.findByEmail(email.toLowerCase(Locale.ROOT)).orElse(null)
                    : null;

            if (user == null) {
                user = new User();
                user.setFullName(name != null ? name : "New User");
                user.setEmail(email != null ? email.toLowerCase(Locale.ROOT) : (uid + "@unknown.local"));
                user.setRole(Role.PLAYER);
                user = users.save(user);
            }

            var principal = new UserPrincipal(user.getId(), user.getEmail(), user.getFullName(), user.getRole(), uid);

            var auth = new UsernamePasswordAuthenticationToken(
                    principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            SecurityContextHolder.clearContext(); // invalid token
        }
        chain.doFilter(req, res);
    }
}
