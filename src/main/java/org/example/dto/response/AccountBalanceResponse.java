package org.example.dto.response;

import lombok.Data;
import org.example.enums.AccountType;

@Data
public class AccountBalanceResponse {
    private String accountNumber;
    private AccountType accountType;
    private double balance;
}
