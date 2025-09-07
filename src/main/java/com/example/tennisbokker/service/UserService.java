package com.example.tennisbokker.service;

import com.example.tennisbokker.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User findById(UUID id);
    List<User> findAll();
    User create(User user);
    User update(UUID id, User user);
    void delete(UUID id);
}