package org.bank.s27288_bank.service;

import org.bank.s27288_bank.exception.ValidationException;
import org.bank.s27288_bank.model.account.Account;
import org.bank.s27288_bank.model.account.CurrencyType;

import org.bank.s27288_bank.repository.AccountRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {
    private static AccountService accountService;
    private static AccountRepository accountRepository;

    @BeforeAll
    static void setUp() {
        accountRepository = new AccountRepository();
        accountService = new AccountService(accountRepository);
    }

    @AfterEach
    void cleanUp() {
        accountRepository.removeAll();
    }

    @Test
    void shouldNotReturnAnyAccount(){
        List<Account> result = accountService.getAllAccounts();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCorrectlyCreateNewAccount(){
        Account result = assertDoesNotThrow(() -> accountService.create("Jan", "Kowalski", "12345678901", CurrencyType.PLN, 100.0));

        assertEquals(100.0, result.getBalance());
    }

    @Test
    void shouldThrowInvalidPeselLenghtException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> accountService.create("Jan", "Kowalski", "1234567890", CurrencyType.PLN, 100.0));

        assertEquals("Pesel needs to have 11 digits - Pesel", exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPeselException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> accountService.create("Jan", "Kowalski", "1234567890a", CurrencyType.PLN, 100.0));

        assertEquals("Pesel can only contain digits - Pesel", exception.getMessage());
    }

    @Test
    void shouldCreateAccountsAndGetSize() {
        accountService.create("Jan", "Kowalski", "12345678901", CurrencyType.PLN, 100.0);
        accountService.create("Piotr", "Nowak", "12345678902", CurrencyType.EUR, 200.0);
        accountService.create("Anna", "Kowalska", "12345678903", CurrencyType.USD, 300.0);

        List<Account> result = accountService.getAllAccounts();

        assertEquals(3, result.size());
    }

    @Test
    void shouldCorrectlyReturnAccountById() {
        Account createdAccount = accountService.create("Jan", "Kowalski", "12345678901", CurrencyType.PLN, 100.0);

        Account result = accountService.getById(createdAccount.getId());

        assertEquals(createdAccount.getId(), result.getId());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBalance")
    void shouldNotCreateNewAccountWithInvalidBalance(Double balance) {
        ValidationException exception = assertThrows(ValidationException.class, () -> accountService.create("Jan", "Kowalski", "12345678901", CurrencyType.PLN, balance));

        assertEquals("Balance cannot be negative - Balance", exception.getMessage());
    }

    private static Stream<Arguments> provideInvalidBalance() {
        return Stream.of(
                Arguments.of(-100.0),
                Arguments.of(-0.01)
        );
    }
}
