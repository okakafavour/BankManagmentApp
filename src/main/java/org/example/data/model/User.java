package org.example.data.model;

import lombok.Data;
import org.example.enums.AccountType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document("Bank User")
public class User {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String password;
    private String phoneNumber;
    private List<Account> accounts;
    private AccountType accountType;
    private boolean isVerified = false;
    private String verificationToken;
    private LocalDateTime tokenExpiryDate;
    private String transferPin;

}
