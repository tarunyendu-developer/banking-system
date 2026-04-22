package com.bank.service.impl;

import com.bank.dto.AccountResponse;
import com.bank.dto.CreateAccountRequest;
import com.bank.entity.Account;
import com.bank.entity.User;
import com.bank.exception.UserNotFoundException;
import com.bank.repository.AccountRepository;
import com.bank.repository.UserRepository;
import com.bank.service.AccountService;
import com.bank.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    @Override
    public void createAccount(CreateAccountRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String accountNumber = "ACC" + (100000 + new java.util.Random().nextInt(900000));

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setUser(user);
        account.setAccountType(Account.AccountType.valueOf(request.getAccountType()));
        account.setBalance(java.math.BigDecimal.ZERO);
        account.setIsActive(true);

        accountRepository.save(account);

        //  LOG
        auditService.log(username, "CREATE_ACCOUNT", "Account created: " + accountNumber, "SUCCESS");
    }

    @Override
    public List<AccountResponse> getUserAccounts(String username) {

        List<Account> accounts = accountRepository.findByUserUsername(username);

        return accounts.stream().map(acc -> AccountResponse.builder()
                .accountNumber(acc.getAccountNumber())
                .accountType(acc.getAccountType().name())
                .balance(acc.getBalance())
                .isActive(acc.getIsActive())
                .build()
        ).toList();
    }
}