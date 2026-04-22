package com.bank.controller;

import com.bank.dto.AccountResponse;
import com.bank.dto.ApiResponse;
import com.bank.dto.CreateAccountRequest;
import com.bank.entity.Account;
import com.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Creating Account
    @PostMapping
    public String createAccount(@RequestBody CreateAccountRequest request, Authentication authentication) {
        String username = authentication.getName();
        accountService.createAccount(request, username);
        return "Account created successfully ";
    }
    // Get Accounts
    @GetMapping
    public ApiResponse<List<AccountResponse>> getAccounts(Authentication auth) {
        List<AccountResponse> accounts = accountService.getUserAccounts(auth.getName());
        return ApiResponse.<List<AccountResponse>>builder()
                .status("SUCCESS")
                .message("Accounts fetched successfully")
                .data(accounts)
                .build();
    }
}