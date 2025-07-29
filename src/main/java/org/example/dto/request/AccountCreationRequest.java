package org.example.dto.request;

import lombok.Data;
import org.example.enums.AccountType;
@Data
public class AccountCreationRequest {
    private String userId;
    private AccountType accountType;
    private String pin;
}
