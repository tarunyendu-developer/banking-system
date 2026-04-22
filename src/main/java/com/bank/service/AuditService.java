package com.bank.service;

public interface AuditService {

    void log(String username, String action, String details, String status);
}