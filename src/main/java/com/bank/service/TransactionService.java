package com.bank.service;

import com.bank.dto.TransferRequest;

public interface TransactionService {

    void transfer(TransferRequest request, String username);
}