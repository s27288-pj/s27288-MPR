package org.bank.s27288_bank.controller;

import org.bank.s27288_bank.model.account.Account;
import org.bank.s27288_bank.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> allAccounts = accountService.getAllAccounts();
        return ResponseEntity.ok(allAccounts);
    }

    @GetMapping
    public ResponseEntity<Account> getAccountByParam(@RequestParam(required = false) Integer id) {
        Account account = accountService.getById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountByPathVariable(@PathVariable Integer id) {
        Account account = accountService.getById(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.create(account.getName(), account.getSurname(), account.getPesel(), account.getCurrencyType(), account.getBalance());
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(createdAccount);
    }
}
