package org.example.service;

import org.example.data.model.Account;
import org.example.data.model.User;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.TransactionsRepository;
import org.example.data.repository.UserRepository;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.TransferResponse;
import org.example.enums.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
        transactionsRepository.deleteAll();
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

    @Test
    public void testToWithdraw() {
        Account account = new Account();
        account.setAccountNumber("0712345688");
        account.setBalance(5000.00);
        account.setAccountType(AccountType.SAVINGS);
        account.setCreatedAt(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);

        User user = new User();
        user.setFirstName("ijidola");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPin("1234");
        user.setAccountIds(List.of(savedAccount.getId()));
        User savedUser = userRepository.save(user);

        accountService.withdraw(savedUser.getUserId(), 2000, "1234");

        Account updated = accountRepository.findById(savedAccount.getId()).orElseThrow();
        assertEquals(3000.00, updated.getBalance());
    }

    @Test
    public void testToViewTheListOfTransactions() {
        System.out.println("👉 Creating first account (SAVINGS)");
        Account account = new Account();
        account.setAccountNumber("0712345688");
        account.setBalance(5000.00);
        account.setAccountType(AccountType.SAVINGS);
        account.setCreatedAt(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);
        System.out.println("✅ First account saved with ID: " + savedAccount.getId());

        System.out.println("👉 Creating user linked to first account");
        User user = new User();
        user.setFirstName("ijidola");
        user.setLastName("Micheal");
        user.setMiddleName("sam");
        user.setPassword("123456");
        user.setPin("1234");
        user.setEmail("okakaFavour81@gmail.com");
        user.setAccountIds(List.of(savedAccount.getId()));
        User savedUser = userRepository.save(user);
        System.out.println("✅ First user saved with ID: " + savedUser.getUserId());

        System.out.println("👉 Creating second account (CURRENT)");
        Account account2 = new Account();
        account2.setAccountNumber("0711100045");
        account2.setBalance(3000.00);
        account2.setAccountType(AccountType.CURRENT);
        account2.setCreatedAt(LocalDateTime.now());
        Account savedAccount2 = accountRepository.save(account2);
        System.out.println("✅ Second account saved with ID: " + savedAccount2.getId());

        System.out.println("👉 Creating user linked to second account");
        User user2 = new User();
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setMiddleName("sam");
        user2.setPassword("12345");
        user2.setPin("2255");
        user2.setEmail("johndoe12@gmail.com");
        user2.setAccountIds(List.of(savedAccount2.getId()));
        User secondUser = userRepository.save(user2);
        System.out.println("✅ Second user saved with ID: " + secondUser.getUserId());

        System.out.println("⚠️ Transfer and transaction logic is currently commented out. No transactions will show.");

    TransferRequest request = new TransferRequest();
    request.setSenderAccountNumber(account.getAccountNumber());
    request.setReceiverAccountNumber(account2.getAccountNumber());
    request.setAmount(1000.0);

    accountService.transfer(request);
    accountService.deposit(savedUser.getUserId(), 2000, "1234");
    accountService.withdraw(savedUser.getUserId(), 2000, "1234");

    List<TransactionsSummary> history = accountService.viewTransactionHistory(savedUser.getUserId());

    assertNotNull(history);
    assertEquals(3, history.size());
    history.forEach(tx -> {
        System.out.println("Type: " + tx.getTransactionType());
        System.out.println("Amount: " + tx.getAmount());
        System.out.println("Direction: " + tx.getDirection());
        System.out.println("Date: " + tx.getDate());
        System.out.println("Narration: " + tx.getNarration());
        System.out.println("------------");
    });

        System.out.println("✅ Test completed without running any transaction logic.");
    }

}