package org.example.data.model;

import lombok.Data;

import java.util.List;


public class Customer extends User{
    private List<Account> account;

    public Customer(String firstName, String middleName, String lastName, String email, String password, String phoneNumber) {
        super(firstName, middleName, lastName, email, password, phoneNumber);
    }
}
