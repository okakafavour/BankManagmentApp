package org.example.service;

import org.example.data.model.Account;
import org.example.data.model.Transactions;
import org.example.data.model.User;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.TransactionsRepository;
import org.example.data.repository.UserRepository;
import org.example.dto.response.AccountBalanceResponse;
import org.example.enums.Direction;
import org.example.enums.TransactionType;
import org.example.exception.*;
import org.example.util.AccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService{

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private Random random;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void withdraw(String userId, double amount, String pin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (user.getAccountIds().isEmpty()) throw new RuntimeException("User does not have any accounts");

        String accountId = user.getAccountIds().get(0);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        boolean isPinCorrect = passwordEncoder.matches(pin, account.getPin());
        if (!isPinCorrect) throw new InvalidPinException("Incorrect PIN");

        if (account.getBalance() < amount) throw new RuntimeException("Insufficient funds");

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transactions transactions = new Transactions();
        transactions.setTransactionType(TransactionType.WITHDRAW);
        transactions.setAmount(amount);
        transactions.setSenderAccount(account.getAccountNumber());
        transactions.setRecipientAccount(null);
        transactions.setDate(LocalDateTime.now());
        transactionsRepository.save(transactions);

        log.info("User {} withdrew {} from account {}", userId, amount, account.getAccountNumber());
    }


    @Override
    public AccountBalanceResponse getAccountBalance(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if(user.getAccountIds().isEmpty()) throw new RuntimeException("User does not have any accounts");
        String accountId = user.getAccountIds().get(0);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found for ID: " + accountId));

        AccountBalanceResponse response = new AccountBalanceResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());

        log.info("User {} checked balance for account {}", userId, account.getAccountNumber());
        return response;
    }

    @Override
    public void deposit(String userId, double amount, String pin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID:" + userId));

        if (user.getAccountIds().isEmpty()) throw new RuntimeException("User not found with this acount");
        String accountId = user.getAccountIds().get(0);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        boolean isPinCorrect = passwordEncoder.matches(pin, account.getPin());
        if(!isPinCorrect) throw new InvalidPinException("Incorrect PIN");

        double newbalance = account.getBalance() + amount;
        account.setBalance(newbalance);
        accountRepository.save(account);

        Transactions transactions = new Transactions();
        transactions.setTransactionType(TransactionType.DEPOSIT);
        transactions.setAmount(amount);
        transactions.setSenderAccount(null);
        transactions.setReceiverAccount(account.getAccountNumber());
        transactions.setDate(LocalDateTime.now());
        transactionsRepository.save(transactions);
        log.info("User {} deposited {} into account {}", userId, amount, accountId);
    }

    @Override
    public List<TransactionsSummary> viewTransactionHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String accountId = user.getAccountIds().get(0);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        String accountNumber = account.getAccountNumber();

        List<Transactions> transactions = transactionsRepository
                .findBySenderAccountOrRecipientAccountOrReceiverAccount(accountNumber, accountNumber, accountNumber);

        return transactions.stream()
                .sorted(Comparator.comparing(Transactions::getDate))
                .map(transaction -> {
                    String direction;
                    String narration = "";

                    if (transaction.getSenderAccount() != null && transaction.getSenderAccount().equals(accountNumber)) {
                        direction = "SENT";

                        if (transaction.getRecipientAccount() != null) {
                            String recipientName = findUserNameByAccountNumber(transaction.getRecipientAccount());
                            narration = "Transfer to " + recipientName;
                        } else {
                            narration = "Withdrawal";
                        }

                    } else if (transaction.getRecipientAccount() != null && transaction.getRecipientAccount().equals(accountNumber)) {
                        direction = "RECEIVED";

                        if (transaction.getSenderAccount() != null) {
                            String senderName = findUserNameByAccountNumber(transaction.getSenderAccount());
                            narration = "Transfer from " + senderName;
                        } else {
                            narration = "Deposit";
                        }

                    } else {
                        direction = "SELF";
                        narration = "Self transaction";
                    }


                    Direction directionEnum = Direction.fromString(direction);
                    return new TransactionsSummary(
                            transaction.getTransactionType(),
                            transaction.getAmount(),
                            directionEnum,
                            transaction.getDate(),
                            narration
                    );
                })
                .collect(Collectors.toList());
    }


    private String findUserNameByAccountNumber(String accountNumber) {
        Optional<Account> OptAccount = accountRepository.findByAccountNumber(accountNumber);
        if(OptAccount.isEmpty()) return "Unknown";

        String accountId = OptAccount.get().getId();

        Optional<User> OptUser = userRepository.findAll().stream()
                .filter(user -> user.getAccountIds().contains(accountId))
                .findFirst();

        if(OptUser.isPresent()) {
            User user = OptUser.get();
            return user.getFirstName() + " " + user.getLastName();
        } else {
            return "Unknown";
        }
    }



}
