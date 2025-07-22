package org.example.util;

import org.example.data.model.Account;
import org.example.dto.response.TransferResponse;
import org.springframework.stereotype.Service;

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
