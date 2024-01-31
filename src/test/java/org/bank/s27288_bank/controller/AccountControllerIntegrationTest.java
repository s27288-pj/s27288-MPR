package org.bank.s27288_bank.controller;

import org.bank.s27288_bank.model.account.Account;
import org.bank.s27288_bank.model.account.CurrencyType;
import org.bank.s27288_bank.repository.AccountRepository;
import org.bank.s27288_bank.model.request.ErrorResponse;

import org.bank.s27288_bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AccountControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    public void cleanUp() {
        accountRepository.removeAll();
        accountRepository.setId(0);
    }

    @Test
    void shouldCreateNewAccount() {
        var account = accountService.create("Jan", "Kowalski", "12345678901", CurrencyType.PLN, 100.0);

        webTestClient.get().uri("/account?id=0")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Account.class)
                .isEqualTo(account);
    }

    @Test
    void shouldReturnBadRequestForValidationException() {
        Account account = new Account(null, "Jan", "Kowalski", "12345678901", CurrencyType.PLN, -100.0);

        ErrorResponse result = webTestClient.post().uri("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals("Balance cannot be negative - Balance", result.getMessage());
    }

    @Test
    void shouldReturnAllAccounts() {
        var account1 = accountService.create("Jan", "Kowalski", "12345678901", CurrencyType.PLN, 100.0);
        var account2 = accountService.create("Pawel", "Nowak", "12345678902", CurrencyType.EUR, 200.0);
        var account3 = accountService.create("Piotr", "Kowalski", "12345678903", CurrencyType.PLN, 300.0);

        List<Account> result = webTestClient.get().uri("/account/all")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Account.class)
                .returnResult()
                .getResponseBody();

        assertEquals(3, result.size());
        assertEquals(account1, result.get(0));
        assertEquals(account2, result.get(1));
        assertEquals(account3, result.get(2));
    }

    @Test
    void shouldReturnSpecifiedAccountByRequestParam() {
        var account1 = accountService.create("Jan", "Kowalski", "12345678901", CurrencyType.PLN, 100.0);
        var account2 = accountService.create("Pawel", "Nowak", "12345678902", CurrencyType.EUR, 200.0);
        var account3 = accountService.create("Piotr", "Kowalski", "12345678903", CurrencyType.PLN, 300.0);

        Account result = webTestClient.get().uri("/account?id=1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Account.class)
                .returnResult()
                .getResponseBody();

        assertEquals(account2, result);
    }
}
