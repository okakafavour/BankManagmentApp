package org.example.service;

import org.example.data.model.Account;
import org.example.data.model.Transactions;
import org.example.data.model.User;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.TransactionsRepository;
import org.example.data.repository.UserRepository;
import org.example.dto.request.AccountCreationRequest;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.AccountCreationResponse;
import org.example.dto.response.TransferResponse;
import org.example.enums.AccountType;
import org.example.enums.TransactionType;
import org.example.exception.AccountNotFoundException;
import org.example.exception.InvalidPinException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class BankServiceImpl implements BankService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private String generateAccountNumber() {
        String accountNumber;
        do {
            int randomNum = (int) (Math.random() * 100000000);
            accountNumber = String.format("07%08d", randomNum);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    @Override
    public AccountCreationResponse createAccount(AccountCreationRequest request) {
        if (request.getPin() == null ||request.getPin().length() < 4) throw new InvalidPinException("Pin must be atleast 4 characters");

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setUserId(user.getUserId());
        account.setPin(passwordEncoder.encode(request.getPin())); // ✅ You missed this
        account.setBalance(0.0);
        account.setCreatedAt(LocalDateTime.now());
        account.setTransactionsList(new ArrayList<>()); // ✅ Optional

        accountRepository.save(account);
         AccountCreationResponse response = new AccountCreationResponse();
         response.setAccountId(account.getId());
         response.setMessage("Account created");
         return response;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account findAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    @Override
    public TransferResponse transfer(TransferRequest request) {
        Account sender = accountRepository.findByAccountNumber(request.getSenderAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found with account number: " + request.getSenderAccountNumber()));

        Account receiver = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found with account number: " + request.getReceiverAccountNumber()));

        if (!passwordEncoder.matches(request.getTransferPin(), sender.getPin())) {
            throw new InvalidPinException("Incorrect PIN");
        }

        if (sender.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance. Sender has: " + sender.getBalance() + ", required: " + request.getAmount());
        }

        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transactions transaction = new Transactions();
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(request.getAmount());
        transaction.setSenderAccount(request.getSenderAccountNumber());
        transaction.setRecipientAccount(request.getReceiverAccountNumber());
        transaction.setDate(LocalDateTime.now());
        transactionsRepository.save(transaction);

        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setAmountTransferred(request.getAmount());
        transferResponse.setSenderAccountNumber(sender.getAccountNumber());
        transferResponse.setReceiverAccountNumber(receiver.getAccountNumber());
        transferResponse.setTimestamp(LocalDateTime.now());
        transferResponse.setBalance(sender.getBalance());
        transferResponse.setMessage("Transfer Successful");

        return transferResponse;
    }

}
