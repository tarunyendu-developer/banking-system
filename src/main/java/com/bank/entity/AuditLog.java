package com.bank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  who performed action
    private String username;

    //  action name
    private String action; // REGISTER, LOGIN, TRANSFER, CREATE_ACCOUNT

    //  details
    private String details;

    //  SUCCESS / FAILED
    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();
}