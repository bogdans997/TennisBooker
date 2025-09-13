package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.UserCreateRequest;
import com.example.tennisbokker.dto.UserResponseDto;
import com.example.tennisbokker.dto.UserUpdateRequest;
import com.example.tennisbokker.entity.enums.Role;
import com.example.tennisbokker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("@guards.isAdmin(authentication) or @guards.isSelf(authentication, #id)")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PreAuthorize("@guards.isAdmin(authentication)")
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAll(
            @RequestParam(required = false) Role role,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.findAll(role, pageable));
    }

    @PreAuthorize("@guards.isAdmin(authentication) or @guards.isSelf(authentication, #id)")
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponseDto created = userService.create(request);
        return ResponseEntity
                .created(URI.create("/api/v1/users/" + created.id()))
                .body(created);
    }

    @PreAuthorize("@guards.isAdmin(authentication) or @guards.isSelf(authentication, #id)")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @PreAuthorize("@guards.isAdmin(authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}