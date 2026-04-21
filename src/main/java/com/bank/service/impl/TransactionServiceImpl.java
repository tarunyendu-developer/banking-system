package com.bank.service.impl;

import com.bank.dto.TransferRequest;
import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public void transfer(TransferRequest request, String username) {

        //  Get sender account
        Account fromAccount = accountRepository
                .findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        //  Get receiver account
        Account toAccount = accountRepository
                .findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        //  Check sender belongs to logged-in user
        if (!fromAccount.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to account");
        }

        //  Check sufficient balance
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        //  Deduct money
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));

        //  Add money
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        //   Save accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        //  Save transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionRef(UUID.randomUUID().toString());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(request.getAmount());
        transaction.setStatus(Transaction.TransactionStatus.SUCCESS);

        transactionRepository.save(transaction);
    }
}