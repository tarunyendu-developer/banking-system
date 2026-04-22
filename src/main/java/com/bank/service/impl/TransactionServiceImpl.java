package com.bank.service.impl;

import com.bank.dto.TransactionResponse;
import com.bank.dto.TransferRequest;
import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.UnauthorizedAccessException;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.service.AuditService;
import com.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuditService auditService;

    @Override
    @Transactional
    public void transfer(TransferRequest request, String username) {

        Account fromAccount = accountRepository
                .findByAccountNumberForUpdate(request.getFromAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        Account toAccount = accountRepository
                .findByAccountNumberForUpdate(request.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        if (!fromAccount.getUser().getUsername().equals(username)) {
            auditService.log(username, "TRANSFER", "Unauthorized access attempt", "FAILED");
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            auditService.log(username, "TRANSFER", "Insufficient balance", "FAILED");
            throw new InsufficientBalanceException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction tx = new Transaction();
        tx.setTransactionRef(java.util.UUID.randomUUID().toString());
        tx.setFromAccount(fromAccount);
        tx.setToAccount(toAccount);
        tx.setAmount(request.getAmount());
        tx.setStatus(Transaction.TransactionStatus.SUCCESS);

        transactionRepository.save(tx);

        //  SUCCESS LOG
        auditService.log(username, "TRANSFER",
                "Transferred " + request.getAmount() +
                        " from " + fromAccount.getAccountNumber() +
                        " to " + toAccount.getAccountNumber(),
                "SUCCESS");
    }

    @Override
    public List<TransactionResponse> getTransactionHistory(String accountNumber, String username) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Unauthorized");
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