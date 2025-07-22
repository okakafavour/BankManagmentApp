package org.example.data.repository;

import org.example.data.model.Transactions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionsRepository extends MongoRepository<Transactions, String> {
    List<Transactions> findBySenderAccountOrRecipientAccountOrReceiverAccount(String sender, String recipient, String receiver);
}
