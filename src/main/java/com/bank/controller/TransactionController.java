package com.bank.controller;

import com.bank.dto.ApiResponse;
import com.bank.dto.TransactionResponse;
import com.bank.dto.TransferRequest;
import com.bank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    //  Transfer Money API
    @PostMapping("/transfer")
    public ApiResponse<String> transfer(@Valid @RequestBody TransferRequest request, Authentication auth) {

        transactionService.transfer(request, auth.getName());

        return ApiResponse.<String>builder()
                .status("SUCCESS")
                .message("Money transferred successfully")
                .data(null)
                .build();
    }

    // Get History
    @GetMapping("/history/{accountNumber}")
    public List<TransactionResponse> getHistory(@PathVariable String accountNumber, Authentication authentication) {
        String username = authentication.getName();
        return transactionService.getTransactionHistory(accountNumber, username);
    }
}