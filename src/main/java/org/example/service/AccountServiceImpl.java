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
    public TransferResponse transfer(TransferRequest transferRequest) {
       try{
           TransferResult result = accountMapper.mapToTransfer(transferRequest);
           return accountMapper.mapToTransferResponse(result.getSender(), result.getRecipient());
       } catch (Exception e) {
           TransferResponse transferResponse = new TransferResponse();
           transferResponse.setStatus("FAILED");
           transferResponse.setMessage("Transfer Failed");
           transferResponse.setTimestamp(LocalDateTime.now());
           return transferResponse;
       }
    }

    @Override
    public double balance(double amount) {
        this.balance += amount;
        return balance;

    }
}
