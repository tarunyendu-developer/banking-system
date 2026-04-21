package com.bank.service.impl;

import com.bank.dto.CreateAccountRequest;
import com.bank.entity.Account;
import com.bank.entity.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.UserRepository;
import com.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public void createAccount(CreateAccountRequest request, String username) {

        //  get user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  generate account number
        String accountNumber = "ACC" + (100000 + new Random().nextInt(900000));

        //  create account
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setUser(user);
        account.setAccountType(Account.AccountType.valueOf(request.getAccountType()));
        account.setBalance(BigDecimal.ZERO);
        account.setIsActive(true);

        accountRepository.save(account);
    }

    @Override
    public List<Account> getUserAccounts(String username) {

        return accountRepository.findByUserUsername(username);
    }
}