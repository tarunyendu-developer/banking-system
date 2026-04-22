package com.bank.controller;

import com.bank.dto.ApiResponse;
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
    public ApiResponse<String> registerUser(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ApiResponse.<String>builder()
                .status("SUCCESS")
                .message("User registered successfully")
                .data(null)
                .build();
    }
    //Login API
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        return ApiResponse.<String>builder()
                .status("SUCCESS")
                .message("Login successful")
                .data(token)
                .build();
    }
}