package com.bank.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponse {

    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private Boolean isActive;
}