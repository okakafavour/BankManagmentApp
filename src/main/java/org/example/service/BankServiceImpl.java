package org.example.service;

import org.example.data.model.Account;
import org.example.data.model.Transactions;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.TransactionsRepository;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.TransferResponse;
import org.example.enums.AccountType;
import org.example.enums.TransactionType;
import org.example.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class BankServiceImpl implements BankService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionsRepository transactionsRepository;


    private String generateAccountNumber() {
        String accountNumber;
        do {
            int randomNum = (int) (Math.random() * 100000000);
            accountNumber = String.format("07%08d", randomNum);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    @Override
    public Account createAccount(String userId, AccountType accountType, String pin) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(accountType);
        account.setId(userId);
        account.setPin(pin);
        return accountRepository.save(account);
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
