package org.example.dto.request;

import lombok.Data;

@Data
public class DepositRequest {
    private double amount;
    private String pin;
}
