package com.bank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique account number
    @Column(unique = true, nullable = false)
    private String accountNumber;

    // relation with user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // account type
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    // money
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum AccountType {
        SAVINGS,
        CURRENT
    }
}