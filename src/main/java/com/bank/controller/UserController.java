package com.bank.controller;

import com.bank.dto.RegisterRequest;
import com.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //  Register User API
    @PostMapping("/register")
    public String registerUser(@RequestBody RegisterRequest request) {

        userService.registerUser(request);

        return "User registered successfully ";
    }
}