package org.example.service;

import org.example.dto.request.TransferRequest;
import org.example.dto.response.TransferResponse;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    String generateAccountNumber();
    TransferResponse transfer(TransferRequest transferRequest);
    double balance(double amount);
}
