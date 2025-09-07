package com.example.tennisbokker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.tennisbokker.entity.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Role role;
    private String preferredLanguage;
    private LocalDateTime createdAt;
}