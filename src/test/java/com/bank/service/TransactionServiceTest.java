package com.bank.service;

import com.bank.dto.TransferRequest;
import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.entity.User;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account sender;
    private Account receiver;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setUsername("tarun");

        sender = new Account();
        sender.setAccountNumber("ACC1001");
        sender.setBalance(new BigDecimal("10000"));
        sender.setUser(user);

        receiver = new Account();
        receiver.setAccountNumber("ACC2001");
        receiver.setBalance(new BigDecimal("5000"));
        receiver.setUser(user);
    }

    //  SUCCESS TEST
    @Test
    @DisplayName("Transfer should succeed when balance is sufficient")
    void transfer_Success() {

        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("ACC1001");
        request.setToAccountNumber("ACC2001");
        request.setAmount(new BigDecimal("2000"));

        when(accountRepository.findByAccountNumberForUpdate("ACC1001"))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByAccountNumberForUpdate("ACC2001"))
                .thenReturn(Optional.of(receiver));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(i -> i.getArgument(0));

        transactionService.transfer(request, "tarun");

        // 🔥 BUSINESS LOGIC CHECK
        assertEquals(new BigDecimal("8000"), sender.getBalance());
        assertEquals(new BigDecimal("7000"), receiver.getBalance());

        // 🔥 VERIFY DB CALLS
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    //  INSUFFICIENT BALANCE
    @Test
    @DisplayName("Transfer should fail when balance is insufficient")
    void transfer_InsufficientBalance() {

        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("ACC1001");
        request.setToAccountNumber("ACC2001");
        request.setAmount(new BigDecimal("999999"));

        when(accountRepository.findByAccountNumberForUpdate("ACC1001"))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByAccountNumberForUpdate("ACC2001"))
                .thenReturn(Optional.of(receiver));

        assertThrows(InsufficientBalanceException.class,
                () -> transactionService.transfer(request, "tarun"));
    }

    //  ACCOUNT NOT FOUND
    @Test
    @DisplayName("Transfer should fail when sender account not found")
    void transfer_AccountNotFound() {

        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("INVALID");
        request.setToAccountNumber("ACC2001");
        request.setAmount(new BigDecimal("100"));

        when(accountRepository.findByAccountNumberForUpdate("INVALID"))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> transactionService.transfer(request, "tarun"));
    }
}