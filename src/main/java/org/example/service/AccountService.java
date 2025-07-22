package org.example.service;

import org.example.dto.request.TransferRequest;
import org.example.dto.response.TransferResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {
    String generateAccountNumber();
    TransferResponse transfer(TransferRequest transferRequest);
    void withdraw(String userId, double amount, String pin);
    void deposit(String userId, double amount, String pin);
    List<TransactionsSummary> viewTransactionHistory(String userId);
}