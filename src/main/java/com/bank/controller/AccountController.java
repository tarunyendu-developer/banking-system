package com.bank.controller;

import com.bank.dto.CreateAccountRequest;
import com.bank.security.JwtUtil;
import com.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public String createAccount(@RequestBody CreateAccountRequest request,
                                @RequestHeader("Authorization") String authHeader) {

        //  Extract token
        String token = authHeader.substring(7);

        //  Extract username from JWT
        String username = jwtUtil.getUsernameFromToken(token);

        accountService.createAccount(request, username);

        return "Account created successfully 💰";
    }
}