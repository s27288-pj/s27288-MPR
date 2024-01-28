package org.bank.s27288_bank.service;

import lombok.RequiredArgsConstructor;
import org.bank.s27288_bank.exception.ValidationException;
import org.bank.s27288_bank.model.account.Account;
import org.bank.s27288_bank.model.client.Client;
import org.bank.s27288_bank.repository.AccountRepository;
import org.bank.s27288_bank.repository.ClientRepository;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;

    public Account create(Account account) {
        if (account.getClientId() == null) {
            throw new ValidationException("Client ID", "Client ID cannot be empty");
        }
        if (account.getCurrency() == null) {
            throw new ValidationException("Currency", "Currency cannot be empty");
        }
        if (account.getBalance() == null) {
            throw new ValidationException("Balance", "Balance cannot be empty");
        }
        if (account.getBalance() < 0) {
            throw new ValidationException("Balance", "Balance cannot be negative");
        }
        Client client = clientService.getById(account.getClientId());
        return accountRepository.create(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.getAll();
    }

    public Account getById(Integer id) {
        if (id == null) {
            throw new ValidationException("id", "ID cannot be null");
        }
        return accountRepository.getById(id).orElseThrow(() -> new ValidationException("id", "Account with ID " + id + " not found"));
    }
}
