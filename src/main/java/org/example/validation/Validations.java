package org.example.validation;

import org.example.exception.EmailException;
import org.example.exception.InvalidNameException;
import org.example.exception.InvalidPhoneNumberException;
import org.example.exception.InvalidPinException;

public class Validations {


    public static String validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!email.matches(emailRegex)) throw new EmailException("Wrong email format");
        return email;
    }

    public static String validatePhoneNumber(String phoneNumber) {
        String phoneRegex = "^(\\+234|0)[789][01]\\d{8}$";
        if (!phoneNumber.matches(phoneRegex)) throw new InvalidPhoneNumberException("Invalid phone number format");
        return phoneNumber;
    }

    public static String validateName(String name) {
        if (name == null || !name.matches("^[a-zA-Z][a-zA-Z' -]{1,29}$")) throw new InvalidNameException("Invalid name format");
        return name;
    }

    public static String validateTransferPin(String transferPin) {
        if (transferPin == null || !transferPin.matches("\\d{4}")) throw new InvalidPinException("Transfer pin must be exactly 4 digit pin");
        return transferPin;
    }

}
