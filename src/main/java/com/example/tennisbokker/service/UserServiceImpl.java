package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.UserCreateRequest;
import com.example.tennisbokker.dto.UserResponseDto;
import com.example.tennisbokker.dto.UserUpdateRequest;
import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.Role;
import com.example.tennisbokker.mapper.UserMapper;
import com.example.tennisbokker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserResponseDto findById(UUID id) {
        User u = repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found: " + id));
        return UserMapper.toResponse(u);
    }

    @Override
    public Page<UserResponseDto> findAll(Role role, Pageable pageable) {
        Page<User> page = (role == null)
                ? repo.findAll(pageable)
                : repo.findAllByRole(role, pageable);
        return page.map(UserMapper::toResponse);
    }

    @Override
    public UserResponseDto create(UserCreateRequest request) {
        String email = request.email().trim().toLowerCase();
        if (repo.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already in use: " + email);
        }

        User u = UserMapper.fromCreate(request);
        if (u.getRole() == null) u.setRole(Role.PLAYER);

        // If/when you add a PasswordEncoder, hash here:
        // if (u.getPassword() != null) u.setPassword(encoder.encode(u.getPassword()));

        User saved = repo.save(u);
        return UserMapper.toResponse(saved);
    }

    @Override
    public UserResponseDto update(UUID id, UserUpdateRequest request) {
        User existing = repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found: " + id));

        UserMapper.applyUpdate(existing, request);

        // If/when you add a PasswordEncoder:
        // if (request.password() != null && !request.password().isBlank())
        //     existing.setPassword(encoder.encode(request.password()));

        User saved = repo.save(existing);
        return UserMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        repo.deleteById(id);
    }
}