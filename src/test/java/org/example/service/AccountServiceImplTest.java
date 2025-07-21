package org.example.service;

import org.example.data.model.Account;
import org.example.data.model.User;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.UserRepository;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegisterRequest;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.LoginResponse;
import org.example.dto.response.TransferResponse;
import org.example.enums.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.example.enums.AccountType.SAVINGS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceImplTest {

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserServiceImpl userService;

    @Test
    public void testToTransfer() {
        // Register a user
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("firstName");
        registerRequest.setLastName("LastName");
        registerRequest.setMiddleName("MiddleName");
        registerRequest.setEmail("okakafavour81@gmail.com");
        registerRequest.setPhoneNumber("07015705372");
        registerRequest.setPassword("123456");
        registerRequest.setAccountType(AccountType.SAVINGS);
        userService.register(registerRequest);

        // Log in the user
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("okakafavour81@gmail.com");
        loginRequest.setPassword("123456");
        userService.login(loginRequest);

        // Manually credit the sender's account with enough balance for the test
        User sender = userRepository.findByEmail("okakafavour81@gmail.com").orElseThrow();
        Account senderAccount = sender.getAccounts().get(0);
        senderAccount.setBalance(1000); // Give sender enough money
        accountRepository.save(senderAccount);

        // Create a recipient user
        RegisterRequest recipientRequest = new RegisterRequest();
        recipientRequest.setFirstName("Recipient");
        recipientRequest.setLastName("User");
        recipientRequest.setMiddleName("Test");
        recipientRequest.setEmail("recipient@gmail.com");
        recipientRequest.setPhoneNumber("07010000000");
        recipientRequest.setPassword("654321");
        recipientRequest.setAccountType(AccountType.SAVINGS);
        userService.register(recipientRequest);

        User recipient = userRepository.findByEmail("recipient@gmail.com").orElseThrow();
        Account recipientAccount = recipient.getAccounts().get(0);

        // Create transfer request
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSenderAccountNumber(senderAccount.getAccountNumber());
        transferRequest.setRecipientAccountNumber(recipientAccount.getAccountNumber());
        transferRequest.setAmount(500);
        transferRequest.setTransferPin("transferPin"); // Optional: if you use transfer PINs
        transferRequest.setAccountType(AccountType.SAVINGS);

        // Perform the transfer
        TransferResponse response = accountService.transfer(transferRequest);

        // Assert the new balance of the sender
        assertEquals(500, response.getBalance());
    }


}