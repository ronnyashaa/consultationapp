package com.example.consultationapp.controller;

import com.example.consultationapp.domain.User;
import com.example.consultationapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String password,
                           @RequestParam String username,
                           @RequestParam String email) {
        if (password.isBlank() || username.isBlank() || email.isBlank()) {
            String message = "Обов'язково введіть щоб у поля";
            return "redirect:/auth/register" + "?error=" +
                    URLEncoder.encode(message, StandardCharsets.UTF_8);
        }
        User user = new User();
        user.setPassword(encoder.encode(password));
        user.setUsername(username);
        user.setEmail(email);
        userService.save(user);
        return "redirect:/auth/login";
    }

}
