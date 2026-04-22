package com.bank.service.impl;

import com.bank.entity.AuditLog;
import com.bank.repository.AuditLogRepository;
import com.bank.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(String username, String action, String details, String status) {

        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setAction(action);
        log.setDetails(details);
        log.setStatus(status);

        auditLogRepository.save(log);
    }
}