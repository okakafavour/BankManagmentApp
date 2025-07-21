package org.example.service;

import org.example.data.model.Account;
import org.example.data.model.TransferResult;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.UserRepository;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.TransferResponse;
import org.example.exception.AccountBalanceException;
import org.example.exception.WrongAccountTypeException;
import org.example.util.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService{

    private Random random;
    private double balance = 0.0;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;

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
    public double balance(double amount) {
        this.balance += amount;
        return balance;

    }
}
