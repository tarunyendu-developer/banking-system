package com.bank.service.impl;

import com.bank.dto.TransactionResponse;
import com.bank.dto.TransferRequest;
import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public void transfer(TransferRequest request, String username) {

        Account fromAccount = accountRepository
                .findByAccountNumberForUpdate(request.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account toAccount = accountRepository
                .findByAccountNumberForUpdate(request.getToAccountNumber())
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

    @Override
    public List<TransactionResponse> getTransactionHistory(String accountNumber, String username) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        List<Transaction> transactions =
                transactionRepository.findByFromAccount_AccountNumberOrToAccount_AccountNumber(
                        accountNumber, accountNumber
                );

        return transactions.stream().map(tx -> TransactionResponse.builder()
                .transactionRef(tx.getTransactionRef())
                .fromAccount(tx.getFromAccount().getAccountNumber())
                .toAccount(tx.getToAccount().getAccountNumber())
                .amount(tx.getAmount())
                .status(tx.getStatus().name())
                .createdAt(tx.getCreatedAt())
                .build()
        ).toList();
    }
}