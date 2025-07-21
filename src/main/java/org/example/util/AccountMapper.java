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

    public TransferResponse mapToTransferResponse(Account sender, Account receiver, double amount) {
        TransferResponse response = new TransferResponse();
        response.setMessage("Transfer successful");
        response.setSenderAccountNumber(sender.getAccountNumber());
        response.setReceiverAccountNumber(receiver.getAccountNumber());
        response.setAmountTransferred(amount);
        return response;
    }

}
