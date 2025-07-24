package org.example.data.repository;

import org.example.data.model.Bank;
import org.example.data.model.Transactions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BankRepository extends MongoRepository<Bank, String> {
    List<Bank> findBySenderAccountNumberOrRecipientAccountNumber(String senderAccountNumber, String recipientAccountNumber);
}
