package org.bank.s27288_bank.service;

import org.bank.s27288_bank.model.account.Account;
import org.bank.s27288_bank.model.account.CurrencyType;
import org.bank.s27288_bank.repository.AccountRepository;
import org.bank.s27288_bank.exception.ValidationException;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account create(String name, String surname, String pesel, CurrencyType currencyType, Double balance) {
        // Validate if Null
        if (currencyType == null) {
            throw new ValidationException("CurrencyType", "CurrencyType cannot be empty");
        }
        if (balance == null) {
            throw new ValidationException("Balance", "Balance cannot be empty");
        }
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Name", "Name cannot be empty");
        }
        if (surname == null || surname.isEmpty()) {
            throw new ValidationException("Surname", "Surname cannot be empty");
        }
        if (pesel == null || pesel.isEmpty()) {
            throw new ValidationException("Pesel", "Pesel cannot be empty");
        }

        // Others Validations
        if (balance < 0) {
            throw new ValidationException("Balance", "Balance cannot be negative");
        }
        if (pesel.length() != 11) {
            throw new ValidationException("Pesel", "Pesel needs to have 11 digits");
        }
        if (pesel.chars().anyMatch(it -> !Character.isDigit(it))) {
            throw new ValidationException("Pesel", "Pesel can only contain digits");
        }

        var id = accountRepository.getId();
        var account = new Account(id, name, surname, pesel, currencyType, balance);
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
