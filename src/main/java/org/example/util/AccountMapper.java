package org.example.util;

import org.example.data.model.Account;
import org.example.data.model.TransferResult;
import org.example.data.repository.AccountRepository;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.TransferResponse;
import org.example.exception.AccountBalanceException;
import org.example.exception.WrongAccountTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountMapper {

    @Autowired
    AccountRepository accountRepository;

    public TransferResult mapToTransfer(TransferRequest transferRequest) {

        Account sender = accountRepository.findByAccountNumber(transferRequest.getSenderAccountNumber())
                .orElseThrow(()-> new RuntimeException("sender not found"));

        Account recipient = accountRepository.findByAccountNumber(transferRequest.getRecipientAccountNumber())
                .orElseThrow(()-> new RuntimeException("receiver not found"));

        if (sender.getAccountType() != transferRequest.getAccountType()) throw new WrongAccountTypeException("wrong account type");

        double amount = transferRequest.getAmount();
        if (sender.getBalance() < amount) throw new AccountBalanceException("Insufficient balance");

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        accountRepository.save(sender);
        accountRepository.save(recipient);

        TransferResult result = new TransferResult();
        result.setSender(sender);
        result.setRecipient(recipient);
        result.setAmount(amount);
        result.setTimestamp(LocalDateTime.now());
        return result;
    }

    public TransferResponse mapToTransferResponse(Account sender, Account recipient) {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setBalance(sender.getBalance());
        transferResponse.setMessage("Transfer Successfully");
        transferResponse.setSenderAccountNumber(sender.getAccountNumber());
        transferResponse.setRecipientAccountNumber(recipient.getAccountNumber());
        transferResponse.setTimestamp(LocalDateTime.now());
        transferResponse.setStatus("SUCCESS");
        return transferResponse;
    }
}
