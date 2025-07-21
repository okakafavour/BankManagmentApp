package org.example.dto.request;

import lombok.Data;
import org.example.enums.AccountType;

@Data
public class TransferRequest {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private double amount;
    private String transferPin;
    private AccountType accountType;


}
