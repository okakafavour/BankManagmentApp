package org.example.data.model;

import org.springframework.data.annotation.Id;

public abstract class User {
    @Id
    private String id;
    private String firstName;
    private String middleName;
    private String email;
    private String password;
    private String phoneNumber;

    public User(String firstName, String middleName, String lastName, String email, String password, String phoneNumber) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
