package org.example.controller;

import org.example.dto.response.AccountBalanceResponse;
import org.example.service.AccountService;
import org.example.service.TransactionsSummary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam("userId") String userId, @RequestParam("amount") double amount, @RequestParam("pin") String pin) {
         accountService.deposit(userId, amount, pin);
         return ResponseEntity.ok("deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam("userId") String userId, @RequestParam("amount") double amount,@RequestParam("pin") String pin) {
        accountService.withdraw(userId, amount, pin);
        return ResponseEntity.ok("withdraw successful");
    }

    @GetMapping("/balance")
    public ResponseEntity<AccountBalanceResponse> balance(@RequestParam("userId") String userId) {
       AccountBalanceResponse response = accountService.getAccountBalance(userId);
       return ResponseEntity.ok(response);
    }

    @GetMapping("/view-transactions")
    public List<TransactionsSummary> viewTransactions(@RequestParam("userId") String userId) {
        return accountService.viewTransactionHistory(userId);
    }
}
