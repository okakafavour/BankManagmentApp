package org.example.service;

import org.example.data.model.Account;
import org.example.data.model.Bank;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.BankRepository;
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
class BankServiceImplTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BankServiceImpl bankService;

    @Autowired
    BankRepository bankRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    public void testTransferSuccess() {

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

        TransferResponse response =  bankService.transfer(request);

        assertEquals("Transfer Successful", response.getMessage());
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

    @Test
    public void testToGetAllAccounts(){
        Account sender = new Account();
        sender.setAccountNumber("0712345678");
        sender.setBalance(5000.0);
        sender.setAccountType(AccountType.SAVINGS);
        sender.setCreatedAt(LocalDateTime.now());
        accountRepository.save(sender);

        Account receiver = new Account();
        receiver.setAccountNumber("0800000000");
        receiver.setBalance(2000.0);
        receiver.setAccountType(AccountType.SAVINGS);
        receiver.setCreatedAt(LocalDateTime.now());
        accountRepository.save(receiver);

        Account thirdUser = new Account();
        thirdUser.setAccountNumber("071212341219");
        thirdUser.setBalance(4000);
        thirdUser.setAccountType(AccountType.SAVINGS);
        thirdUser.setCreatedAt(LocalDateTime.now());
        accountRepository.save(thirdUser);

        assertEquals(3,  bankService.getAllAccounts().size());

    }
}