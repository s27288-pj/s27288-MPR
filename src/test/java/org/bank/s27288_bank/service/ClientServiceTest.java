package org.bank.s27288_bank.service;

import org.bank.s27288_bank.exception.ValidationException;
import org.bank.s27288_bank.model.client.Client;
import org.bank.s27288_bank.repository.ClientRepository;
import org.bank.s27288_bank.repository.AccountRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
public class ClientServiceTest {
    private static ClientService clientService;
    private static ClientRepository clientRepository;

    @BeforeAll
    static void setUp() {
        clientRepository = new ClientRepository();
        clientService = new ClientService(clientRepository);
    }

    @AfterEach
    void cleanUp() {
        clientRepository.removeAll();
    }

    @Test
    void shouldCorrectlyCreateNewClient(){
        Client client = new Client(null, "Jan", "Kowalski", "12345678901");

        Client result = assertDoesNotThrow(() -> clientService.create(client));

        assertEquals(client.getName(), result.getName());
    }

    @Test
    void shouldNotReturnAnyClient(){
        List<Client> result = clientService.getAllClients();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotCreateNewClient() {
        Client client = new Client(null, "Jan", "Kowalski", "1234567890178");

        ValidationException exception = assertThrows(ValidationException.class, () -> clientService.create(client));

        assertEquals("Client pesel must be 11 characters long, Pesel", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPesel")
    void shouldNotCreateNewClientWithInvalidPesel(String pesel) {
        Client client = new Client(null, "Jan", "Kowalski", pesel);

        ValidationException exception = assertThrows(ValidationException.class, () -> clientService.create(client));

        assertEquals("Client pesel must be 11 characters long, Pesel", exception.getMessage());
    }

    private static Stream<Arguments> provideInvalidPesel() {
        return Stream.of(
                Arguments.of("1234567890"),
                Arguments.of("123456789012"),
                Arguments.of("12345678912132123")
        );
    }
}
