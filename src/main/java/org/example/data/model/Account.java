package org.example.data.model;

import lombok.Data;
import org.example.enums.AccountType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document("Account")
public class Account {
    @Id
    private String id;

    @Indexed(unique = true)
    private String accountNumber;
    private AccountType accountType;
    private double balance;
    private List<Transactions> transactionsList;
    private String pin;
    private LocalDateTime createdAt;

}
