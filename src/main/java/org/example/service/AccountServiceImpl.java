package org.example.service;

import org.example.data.model.Account;
import org.example.data.model.Transactions;
import org.example.data.model.TransferResult;
import org.example.data.model.User;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.TransactionsRepository;
import org.example.data.repository.UserRepository;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.TransferResponse;
import org.example.exception.*;
import org.example.util.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService{

    private Random random;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Override
    public String generateAccountNumber() {
        String accountNumber;
        do {
            int randomNum = (int) (Math.random() * 100000000);
            accountNumber = String.format("07%08d", randomNum);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    @Override
    public TransferResponse transfer(TransferRequest request) {
        Account sender = accountRepository.findByAccountNumber(request.getSenderAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found with account number: " + request.getSenderAccountNumber()));

        Account receiver = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found with account number: " + request.getReceiverAccountNumber()));

        if (sender.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance. Sender has: " + sender.getBalance() + ", required: " + request.getAmount());
        }

        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        accountRepository.save(sender);
        accountRepository.save(receiver);

        return accountMapper.mapToTransferResponse(sender, receiver, request.getAmount());
    }

    @Override
    public void withdraw(String userId, double amount, String pin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPin().equals(pin)) {
            throw new InvalidPinException("Pin is not correct");
        }

        String accountId = user.getAccountIds().get(0);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    public void deposit(String userId, double amount, String pin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!user.getPin().equals(pin)) throw new RuntimeException("pin is not correct");

        String accountId = user.getAccountIds().get(0);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public List<TransactionsSummary> viewTransactionHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String accountId = user.getAccountIds().get(0);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        String accountNumber = account.getAccountNumber();

        List<Transactions> transactions = transactionsRepository.findBySenderAccountOrRecipientAccountOrReceiverAccount(accountNumber, accountNumber, accountNumber);
        return transactions.stream()
                .sort
    }

    public List<Transactions> viewTransactions(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String accountId = user.getAccountIds().get(0);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        String accountNumber = account.getAccountNumber();
        return transactionsRepository.findBySenderAccountOrRecipientAccountOrReceiverAccount(accountNumber, accountNumber, accountNumber);
    }


}
