package org.example.service;

import org.example.data.model.Account;
import org.example.data.repository.AccountRepository;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.TransferResponse;
import org.example.enums.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void testTransferSuccess() {

        Account sender = new Account();
        sender.setAccountNumber("0712345678");
        sender.setBalance(5000.0);
        sender.setAccountType(AccountType.SAVINGS);
        sender.setCreatedAt(LocalDateTime.now());

        Account receiver = new Account();
        receiver.setAccountNumber("0800000000");
        receiver.setBalance(2000.0);
        receiver.setAccountType(AccountType.SAVINGS);
        receiver.setCreatedAt(LocalDateTime.now());

        accountRepository.save(sender);
        accountRepository.save(receiver);

        System.out.println("=== ACCOUNTS BEFORE TRANSFER ===");
        List<Account> allBefore = accountRepository.findAll();
        allBefore.forEach(acc -> System.out.println(acc.getAccountNumber() + ": " + acc.getBalance()));


        TransferRequest request = new TransferRequest();
        request.setSenderAccountNumber(sender.getAccountNumber());
        request.setReceiverAccountNumber(receiver.getAccountNumber());
        request.setAmount(1000.0);

        TransferResponse response = accountService.transfer(request);

        assertEquals("Transfer successful", response.getMessage());
        assertEquals(sender.getAccountNumber(), response.getSenderAccountNumber());
        assertEquals(receiver.getAccountNumber(), response.getReceiverAccountNumber());
        assertEquals(1000.0, response.getAmountTransferred());

        System.out.println("=== ACCOUNTS AFTER TRANSFER ===");
        List<Account> allAfter = accountRepository.findAll();
        allAfter.forEach(acc -> System.out.println(acc.getAccountNumber() + ": " + acc.getBalance()));

        Account updatedSender = accountRepository.findByAccountNumber(sender.getAccountNumber()).orElseThrow();
        Account updatedReceiver = accountRepository.findByAccountNumber(receiver.getAccountNumber()).orElseThrow();

        assertEquals(4000.0, updatedSender.getBalance());
        assertEquals(3000.0, updatedReceiver.getBalance());
    }
}
