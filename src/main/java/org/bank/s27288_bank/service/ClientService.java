package org.bank.s27288_bank.service;

import lombok.RequiredArgsConstructor;
import org.bank.s27288_bank.exception.ValidationException;
import org.bank.s27288_bank.model.client.Client;
import org.bank.s27288_bank.repository.ClientRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client create(Client client) {
        if (client.getName() == null || client.getName().isEmpty()) {
            throw new ValidationException("Name", "Client name cannot be empty");
        }
        if (client.getSurname() == null || client.getSurname().isEmpty()) {
            throw new ValidationException("Surname", "Client surname cannot be empty");
        }
        if (client.getPesel() == null || client.getPesel().toString().isEmpty()) {
            throw new ValidationException("Pesel", "Client pesel cannot be empty");
        }
        if (client.getPesel().toString().length() != 11) {
            throw new ValidationException("Pesel", "Client pesel must be 11 characters long");
        }
        return clientRepository.create(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.getAll();
    }

    public Client getById(Integer id) {
        if (id == null) {
            throw new ValidationException("id", "ID cannot be null");
        }
        return clientRepository.getById(id).orElseThrow(() -> new ValidationException("id", "Client with ID " + id + " not found"));
    }
}
