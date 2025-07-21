package org.example.data.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferResult {
    private Account sender;
    private Account recipient;
    private double amount;
    private LocalDateTime timestamp;

}
