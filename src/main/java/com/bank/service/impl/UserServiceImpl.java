package com.bank.service.impl;

import com.bank.dto.RegisterRequest;
import com.bank.entity.User;
import com.bank.exception.InvalidCredentialsException;
import com.bank.exception.UserNotFoundException;
import com.bank.repository.UserRepository;
import com.bank.service.AuditService;
import com.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bank.exception.UserAlreadyExistsException;
import com.bank.dto.LoginRequest;
import com.bank.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;

    @Override
    public void registerUser(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            auditService.log(request.getUsername(), "REGISTER", "Username already exists", "FAILED");
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            auditService.log(request.getUsername(), "REGISTER", "Email already exists", "FAILED");
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setIsActive(true);

        userRepository.save(user);

        //  SUCCESS LOG
        auditService.log(user.getUsername(), "REGISTER", "User registered successfully", "SUCCESS");
    }

    @Override
    public String login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        //  Generate token
        return jwtUtil.generateToken(user.getUsername());
    }
}