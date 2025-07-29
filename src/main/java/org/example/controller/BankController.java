package org.example.controller;

import org.example.data.model.Account;
import org.example.dto.request.AccountCreationRequest;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.AccountCreationResponse;
import org.example.dto.response.TransferResponse;
import org.example.service.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<AccountCreationResponse> createAccount(@RequestBody AccountCreationRequest request) {
         AccountCreationResponse account = bankService.createAccount(request);
         return ResponseEntity.ok(account);
    }

    @GetMapping("/all-accounts")
    public ResponseEntity<Account> getAccounts(@RequestParam String userId) {
        List<Account> allAccounts = bankService.getAllAccounts();
        return ResponseEntity.ok(allAccounts.get(0));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest) {
        TransferResponse response = bankService.transfer(transferRequest);
        return ResponseEntity.ok(response);
    }

}
