package org.example.data.model;

import org.example.enums.AccountType;

import java.util.List;

public class Account {
    private String accountNumber;
    private AccountType accountType;
    private double balance;
    private List<Transactions> transactionsList;
}
