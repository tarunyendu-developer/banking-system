package com.bank.service;

import com.bank.dto.CreateAccountRequest;

public interface AccountService {

    void createAccount(CreateAccountRequest request, String username);
}