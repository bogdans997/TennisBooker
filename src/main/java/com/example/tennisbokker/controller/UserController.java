package com.example.tennisbokker.controller;

import com.example.tennisbokker.dto.UserCreateRequest;
import com.example.tennisbokker.dto.UserResponseDto;
import com.example.tennisbokker.dto.UserUpdateRequest;
import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.Role;
import com.example.tennisbokker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAll(
            @RequestParam(required = false) Role role,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.findAll(role, pageable));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponseDto created = userService.create(request);
        return ResponseEntity
                .created(URI.create("/api/v1/users/" + created.id()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}