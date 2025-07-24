package org.example.data.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("Bank")
public class Bank {
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private List<Account> accountsList;
}
