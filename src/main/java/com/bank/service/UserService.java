package com.bank.service;

import com.bank.dto.RegisterRequest;

public interface UserService {

    // Register new user
    void registerUser(RegisterRequest request);
}