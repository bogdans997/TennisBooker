package com.example.tennisbokker.service;

import com.example.tennisbokker.entity.User;
import com.example.tennisbokker.entity.enums.Role;
import com.example.tennisbokker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.PLAYER); // Default role if not set
        }
        return userRepository.save(user);
    }

    @Override
    public User update(UUID id, User user) {
        if (!userRepository.existsById(id)) {
            return null;
        }
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}