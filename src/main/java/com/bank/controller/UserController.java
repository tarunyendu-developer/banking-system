package com.bank.controller;

import com.bank.dto.LoginRequest;
import com.bank.dto.RegisterRequest;
import com.bank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //  Register User API
    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody RegisterRequest request) {

        userService.registerUser(request);

        return "User registered successfully ";
    }
    //Login API
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        userService.login(request);

        return "Login successful ✅";
    }
}