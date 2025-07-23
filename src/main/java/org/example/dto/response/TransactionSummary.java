package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.Direction;

@Data
public class TransactionSummary {
    private String type;
    private double amount;
    private Direction direction;

    public TransactionSummary(String type, double amount, Direction direction) {
        this.type = type;
        this.amount = amount;
        this.direction = direction;
    }
}
