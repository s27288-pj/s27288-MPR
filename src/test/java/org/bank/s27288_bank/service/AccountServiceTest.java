package org.bank.s27288_bank.service;

import org.bank.s27288_bank.exception.ValidationException;
import org.bank.s27288_bank.model.account.Account;
import static org.bank.s27288_bank.model.account.CurrencyType.*;

import org.bank.s27288_bank.model.client.Client;
import org.bank.s27288_bank.repository.AccountRepository;

import org.bank.s27288_bank.repository.ClientRepository;
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
        accountService = new AccountService(accountRepository, new ClientService(new ClientRepository()));
        Client client = new Client(null, "Jan", "Kowalski", "12345678901");
        ClientRepository clientRepository = new ClientRepository();
        clientRepository.create(client);
    }

    @AfterEach
    void cleanUp() {
        accountRepository.removeAll();
    }

    @Test
    void shouldCorrectlyCreateNewAccount(){
        Account account = new Account(null, 0, PLN, 100.0, ClientRepository.getName(0), ClientRepository.getSurname(0), ClientRepository.getPesel(0));

        Account result = assertDoesNotThrow(() -> accountService.create(account));

        assertEquals(account.getBalance(), result.getBalance());
    }

    @Test
    void shouldNotReturnAnyAccount(){
        List<Account> result = accountService.getAllAccounts();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotCreateNewAccount() {
        Account account = new Account(null, 0, PLN, -100.0, ClientRepository.getName(0), ClientRepository.getSurname(0), ClientRepository.getPesel(0));

        ValidationException exception = assertThrows(ValidationException.class, () -> accountService.create(account));

        assertEquals("Balance cannot be negative, Balance", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBalance")
    void shouldNotCreateNewAccountWithInvalidBalance(Double balance) {
        Account account = new Account(null, 0, PLN, balance, ClientRepository.getName(0), ClientRepository.getSurname(0), ClientRepository.getPesel(0));

        ValidationException exception = assertThrows(ValidationException.class, () -> accountService.create(account));

        assertEquals("Balance cannot be negative, Balance", exception.getMessage());
    }

    @Test
    void shouldCorrectlyReturnAccountById() {
        Account account = new Account(null, 0, PLN, 100.0, ClientRepository.getName(0), ClientRepository.getSurname(0), ClientRepository.getPesel(0));
        Account createdAccount = accountService.create(account);

        Account result = accountService.getById(createdAccount.getId());

        assertEquals(createdAccount.getId(), result.getId());
    }

    private static Stream<Arguments> provideInvalidBalance() {
        return Stream.of(
                Arguments.of(-100.0),
                Arguments.of(-0.01)
        );
    }
}
