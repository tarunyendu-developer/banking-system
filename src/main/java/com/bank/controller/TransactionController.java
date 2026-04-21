package com.bank.controller;

import com.bank.dto.TransferRequest;
import com.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    //  Transfer Money API
    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request, Authentication authentication) {
        String username = authentication.getName();
        transactionService.transfer(request, username);
        return "Money transferred successfully 💸";
    }
}