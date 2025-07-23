package org.example.service;

import lombok.Data;
import org.example.enums.Direction;
import org.example.enums.TransactionType;

import java.time.LocalDateTime;

@Data
public class TransactionsSummary {
    private TransactionType transactionType;
    private double amount;
    private Direction direction;
    private LocalDateTime date;
    private String narration;


    public TransactionsSummary(TransactionType transactionType, double amount, Direction direction, LocalDateTime date, String narration) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.direction = direction;
        this.date = date;
        this.narration = narration;
    }



}
