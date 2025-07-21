package org.example.data.repository;

import org.example.data.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
    boolean existsByAccountNumber(String accountNumber);

    Optional <Account> findByAccountNumber(String senderAccountNumber);
}
