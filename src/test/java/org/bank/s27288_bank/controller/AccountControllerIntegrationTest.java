package org.bank.s27288_bank.controller;

import org.bank.s27288_bank.model.account.Account;
import static org.bank.s27288_bank.model.account.CurrencyType.*;

import org.bank.s27288_bank.model.client.Client;
import org.bank.s27288_bank.repository.AccountRepository;
import org.bank.s27288_bank.model.request.ErrorResponse;
import org.bank.s27288_bank.repository.ClientRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

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
    private ObjectMapper objectMapper;

    @BeforeEach
    public void cleanUp() {
        accountRepository.removeAll();
        Client client = new Client(null, "Jan", "Kowalski", "12345678901");
        ClientRepository clientRepository = new ClientRepository();
        clientRepository.create(client);
    }

    @Test
    void shouldCreateNewAccount() throws JsonProcessingException {
        Account account = new Account(null, 0, PLN, 100.0, ClientRepository.getName(0), ClientRepository.getSurname(0), ClientRepository.getPesel(0));
        String accountRequestJson = objectMapper.writeValueAsString(account);

        Account result = webTestClient.post().uri("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountRequestJson)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Account.class)
                .returnResult()
                .getResponseBody();
        assertNotEquals(result.getId(), account.getId());
        assertEquals(result.getBalance(), account.getBalance());

        Optional<Account> accountFromRepository = accountRepository.getById(result.getId());
        assertTrue(accountFromRepository.isPresent());
    }

//    @Test
//    void shouldReturnBadRequestForValidationException() throws JsonProcessingException {
//        Account account = new Account(null, 0, PLN, -100.0, "Jan", "Kowalski", "12345678901");
//        String accountRequestJson = objectMapper.writeValueAsString(account);
//
//        account.setBalance(-100.0);
//        accountRequestJson = objectMapper.writeValueAsString(account);
//
//        ErrorResponse result = webTestClient.post().uri("/account/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(accountRequestJson)
//                .exchange()
//                .expectStatus()
//                .isBadRequest()
//                .expectBody(ErrorResponse.class)
//                .returnResult()
//                .getResponseBody();
//
//        assert result != null;
//        assertEquals("Balance must be greater than 0, balance", result.getMessage());
//    }

    @Test
    void shouldReturnAllAccounts() {
        Account account1 = new Account(null, 0, PLN, 100.0, "Jan", "Kowalski", "12345678901");
        Account account2 = new Account(null, 1, EUR, 200.0, "Pawel", "Nowak", "12345678902");
        Account account3 = new Account(null, 2, PLN, 300.0, "Piotr", "Kowalski", "12345678903");

        accountRepository.create(account1);
        accountRepository.create(account2);
        accountRepository.create(account3);

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
        Account account1 = new Account(null, 0, PLN, 100.0, "Jan", "Kowalski", "12345678901");
        Account account2 = new Account(null, 1, EUR, 200.0, "Pawel", "Nowak", "12345678902");
        Account account3 = new Account(null, 2, PLN, 300.0, "Piotr", "Kowalski", "12345678903");

        accountRepository.create(account1);
        accountRepository.create(account2);
        accountRepository.create(account3);

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
