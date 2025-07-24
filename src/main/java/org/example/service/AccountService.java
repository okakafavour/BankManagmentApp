package org.example.service;

import org.example.dto.request.DepositRequest;
import org.example.dto.request.WithdrawalRequest;
import org.example.dto.response.DepositResponse;
import org.example.dto.response.WithdrawalResponse;
import org.example.dto.response.AccountBalanceResponse;

import java.util.List;

public interface AccountService {

    void deposit(String userId, double amount, String pin);

    void withdraw(String userId, double amount, String pin);

    AccountBalanceResponse getAccountBalance(String userId);

    List<TransactionsSummary> viewTransactionHistory(String userId);
}
