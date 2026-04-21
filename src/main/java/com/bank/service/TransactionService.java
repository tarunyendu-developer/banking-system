package com.bank.service;

import com.bank.dto.TransactionResponse;
import com.bank.dto.TransferRequest;

import java.util.List;

public interface TransactionService {

    void transfer(TransferRequest request, String username);
    List<TransactionResponse> getTransactionHistory(String accountNumber, String username);
}