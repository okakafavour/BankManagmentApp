package org.example.util;

import org.example.data.model.Account;
import org.example.data.model.User;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.UserRepository;
import org.example.dto.request.RegisterRequest;
import org.example.dto.response.RegisterResponse;
import org.example.enums.AccountType;
import org.example.exception.UserAlreadyExist;
import org.example.service.AccountServiceImpl;
import org.example.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    EmailServiceImpl  emailService;

    @Autowired
    AccountRepository accountRepository;

    public User mapToRegisterRequest(RegisterRequest registerRequest) {
        User user = createUser(registerRequest);
        Account account = createAccount(registerRequest);

        Account savedAccount = accountRepository.save(account);
        user.setAccountIds(List.of(savedAccount.getId()));

        generateVerificationToken(user);
        User savedUser = userRepository.save(user);
        sendVerificationEmail(savedUser);
        return savedUser;
    }



    public RegisterResponse mapToRegisterResponse(User user) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        registerResponse.setMessage("Registration successful. check your email to confirm");
        return registerResponse;
    }


    public User createUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setMiddleName(registerRequest.getMiddleName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(PasswordHashingMapper.hashPassword(registerRequest.getPassword()));
        user.setVerified(false);
        return user;
    }

    public Account createAccount(RegisterRequest registerRequest) {
        AccountType accountType;
        try {
            String type = registerRequest.getAccountType().name();
            accountType = AccountType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account type. Valid types: SAVINGS, CURRENT.");
        }

        Account account = new Account();
        String accountNumber = accountService.generateAccountNumber();
        if (accountNumber == null) {
            throw new IllegalStateException("Generated account number is null!");
        }

        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setCreatedAt(LocalDateTime.now());
        account.setBalance(0.0);
        return account;
    }



    public void generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusMinutes(30));
    }

    public void sendVerificationEmail(User user) {
        String link = "http://localhost:8080/api/auth/verify?token=" + user.getVerificationToken();
        emailService.sendVerificationEmail(user.getEmail(), link);
    }

}
