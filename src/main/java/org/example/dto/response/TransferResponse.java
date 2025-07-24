package org.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferResponse {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private double amountTransferred;
    private String message;
    private LocalDateTime timestamp;
    private double balance;
    private String status;

}
