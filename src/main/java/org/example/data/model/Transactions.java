package org.example.data.model;

import org.example.enums.TransactionType;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Transactions {
    @Id
    private String id;
    private LocalDateTime timeStamp;
    private double amount;
    private TransactionType transactionType;
    private String senderAccount;
    private String description;
    private String receiverAccount;

    public Transactions(String id, LocalDateTime timeStamp, double amount, TransactionType transactionType, String senderAccount, String description, String receiverAccount) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.transactionType = transactionType;
        this.senderAccount = senderAccount;
        this.description = description;
        this.receiverAccount = receiverAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }
}
