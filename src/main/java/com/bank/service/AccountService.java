package com.bank.service;

import com.bank.dto.CreateAccountRequest;
import com.bank.entity.Account;

import java.util.List;

public interface AccountService {

    void createAccount(CreateAccountRequest request, String username);
    List<Account> getUserAccounts(String username);

}