package org.example.controller;

import org.example.dto.response.AccountBalanceResponse;
import org.example.service.AccountService;
import org.example.service.TransactionsSummary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    public ResponseEntity<AccountBalanceResponse> balance() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountBalanceResponse response = accountService.getAccountBalance(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam("amount") double amount, @RequestParam("pin") String pin) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.deposit(email, amount, pin);
        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam("amount") double amount, @RequestParam("pin") String pin) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.withdraw(email, amount, pin);
        return ResponseEntity.ok("Withdraw successful");
    }

    @GetMapping("/view-transactions")
    public List<TransactionsSummary> viewTransactions() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountService.viewTransactionHistory(email);
    }
}
