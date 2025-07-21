package org.example.data.model;

import lombok.Data;
import org.example.enums.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("Transaction")
public class Transactions {
    @Id
    private String id;
    private LocalDateTime timeStamp;
    private double amount;
    private TransactionType transactionType;
    private String senderAccount;
    private String recipientAccount;
    private String description;
    private String receiverAccount;

}
