package com.example.consultationapp.service;

import com.example.consultationapp.domain.User;
import com.example.consultationapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User %s not found".formatted(username)));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}