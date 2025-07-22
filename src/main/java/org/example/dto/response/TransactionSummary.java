package org.example.dto.response;

import lombok.Data;

@Data
public class TransactionSummary {
    private String type;
    private double amount;
    private Direction direction;
}
