package org.example.dto.request;

import lombok.Data;
import org.example.data.model.User;
import org.example.enums.AccountType;

import java.time.LocalDateTime;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String password;
    private String phoneNumber;
    private boolean verified;
    private AccountType accountType;
    private boolean isVerified = false;
    private String verificationToken;
    private LocalDateTime tokenExpiryDate;
}
