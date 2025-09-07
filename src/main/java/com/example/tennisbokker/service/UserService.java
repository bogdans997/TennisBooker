package com.example.tennisbokker.service;

import com.example.tennisbokker.dto.UserCreateRequest;
import com.example.tennisbokker.dto.UserResponseDto;
import com.example.tennisbokker.dto.UserUpdateRequest;
import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto findById(UUID id);
    Page<UserResponseDto> findAll(Role role, Pageable pageable);
    UserResponseDto create(UserCreateRequest request);
    UserResponseDto update(UUID id, UserUpdateRequest request);
    void delete(UUID id);
}