package com.example.consultationapp.service;

import com.example.consultationapp.domain.User;

public interface UserService {

    User findByUsername(String username);

    User save(User user);

}
