package com.bank.dto;

import lombok.Data;

@Data
public class CreateAccountRequest {

    private String accountType; // SAVINGS / CURRENT
}