package com.bank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  unique reference
    @Column(unique = true, nullable = false)
    private String transactionRef;

    // sender
    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    // receiver
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    //  amount
    @Column(nullable = false)
    private BigDecimal amount;

    // status
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum TransactionStatus {
        SUCCESS,
        FAILED
    }
}