package com.example.tennisbokker.mapper;

import com.example.tennisbokker.dto.UserDto;
import com.example.tennisbokker.entity.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setPreferredLanguage(user.getPreferredLanguage());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        user.setPreferredLanguage(dto.getPreferredLanguage());
        user.setCreatedAt(dto.getCreatedAt());
        // Password is not set from DTO for security reasons
        return user;
    }
}