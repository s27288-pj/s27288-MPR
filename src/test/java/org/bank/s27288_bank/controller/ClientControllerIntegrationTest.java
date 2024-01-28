package org.bank.s27288_bank.controller;

import org.bank.s27288_bank.model.client.Client;
import org.bank.s27288_bank.repository.ClientRepository;
import org.bank.s27288_bank.model.request.ErrorResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ClientControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void cleanUp() {
        clientRepository.removeAll();
    }

    @Test
    void shouldCreateNewClient() throws JsonProcessingException {
        Client client = new Client(null, "Jan", "Kowalski", "12345678901");
        String clientRequestJson = objectMapper.writeValueAsString(client);

        Client result = webTestClient.post().uri("/client/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientRequestJson)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Client.class)
                .returnResult()
                .getResponseBody();
        assertNotEquals(result.getId(), client.getId());
        assertEquals(result.getPesel(), client.getPesel());

        Optional<Client> clientFromRepository = clientRepository.getById(result.getId());
        assertTrue(clientFromRepository.isPresent());

        assertEquals(1, clientRepository.getAll().size());
    }

    @Test
    void shouldReturnBadRequestForValidationException() throws JsonProcessingException {
        Client client = new Client(null, "Jan", "Kowalski", "1234567890");
        String clientRequestJson = objectMapper.writeValueAsString(client);

        ErrorResponse result = webTestClient.post().uri("/client/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientRequestJson)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals("Client pesel must be 11 characters long, Pesel", result.getMessage());
    }

    @Test
    void shouldReturnAllClients() {
        Client client1 = new Client(null, "Jan", "Kowalski", "12345678901");
        Client client2 = new Client(null, "Adam", "Nowak", "12345678902");
        Client client3 = new Client(null, "Pawel", "Nowak", "12345678903");

        clientRepository.create(client1);
        clientRepository.create(client2);
        clientRepository.create(client3);

        List<Client> result = webTestClient.get().uri("/client/all")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Client.class)
                .returnResult()
                .getResponseBody();

        assertEquals(3, result.size());
    }

    @Test
    void shouldReturnSpecifiedClientByRequestParam() {
        Client client1 = new Client(null, "Jan", "Kowalski", "12345678901");
        Client client2 = new Client(null, "Adam", "Nowak", "12345678902");
        Client client3 = new Client(null, "Pawel", "Nowak", "12345678903");

        clientRepository.create(client1);
        clientRepository.create(client2);
        clientRepository.create(client3);

        Client result = webTestClient.get().uri("/client?id=1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Client.class)
                .returnResult()
                .getResponseBody();

        assertEquals(client2, result);
    }
}
