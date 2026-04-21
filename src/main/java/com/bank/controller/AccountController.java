package com.bank.controller;

import com.bank.dto.AccountResponse;
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

    @PostMapping
    public String createAccount(@RequestBody CreateAccountRequest request, Authentication authentication) {
        String username = authentication.getName();
        accountService.createAccount(request, username);
        return "Account created successfully 💰";
    }

    @GetMapping
    public List<AccountResponse> getAccounts(Authentication authentication) {
        String username = authentication.getName();
        return accountService.getUserAccounts(username);
    }
}