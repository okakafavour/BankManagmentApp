package org.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferResponse {
    private String transactionId;
    private String status;
    private String message;
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private double amount;
    private double balance;
    private LocalDateTime timestamp;
}

