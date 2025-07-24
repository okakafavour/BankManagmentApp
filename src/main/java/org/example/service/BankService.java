package org.example.service;

import org.example.data.model.Account;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.TransferResponse;
import org.example.enums.AccountType;

import java.util.List;

public interface BankService {

    Account createAccount(String userId, AccountType accountType, String pin);

    List<Account> getAllAccounts();

    Account findAccountByNumber(String accountNumber);

    TransferResponse transfer(TransferRequest request);
}
