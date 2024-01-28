package org.bank.s27288_bank.controller;

import org.bank.s27288_bank.model.client.Client;
import org.bank.s27288_bank.service.ClientService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllCars() {
        List<Client> allCars = clientService.getAllClients();
        return ResponseEntity.ok(allCars);
    }

    @GetMapping
    public ResponseEntity<Client> getCarByParam(@RequestParam(required = false) Integer id) {
        Client client = clientService.getById(id);

        return ResponseEntity.ok(client);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getCarByPathVariable(@PathVariable Integer id) {
        Client client = clientService.getById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/create")
    public ResponseEntity<Client> createCar(@RequestBody Client car) {
        Client createdCar = clientService.create(car);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(createdCar);
    }
}
