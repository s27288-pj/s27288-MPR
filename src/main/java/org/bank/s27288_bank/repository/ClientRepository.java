package org.bank.s27288_bank.repository;

import org.bank.s27288_bank.model.client.Client;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepository {
private static List<Client> clientList = new ArrayList<>();

    public Client create(Client client) {
        client.setId(clientList.size());
        clientList.add(client);
        return client;
    }
    //create code for getting name, surname and pesel by ClientID
    public static String getName(Integer id){
        return clientList
                .stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .get()
                .getName();
    }
    public static String getSurname(Integer id){
        return clientList
                .stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .get()
                .getSurname();
    }
    public static String getPesel(Integer id){
        return clientList
                .stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .get()
                .getPesel();
    }
    public Optional<Client> getById(Integer id){
        return clientList
                .stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    public List<Client> getAll(){
        return clientList;
    }

    public void removeById(Integer id){
        Optional<Client> client = getById(id);

        client.ifPresent(it -> clientList.remove(it));
    }
    public void removeAll(){
        clientList = new ArrayList<>();
    }

    public Optional<Client> modify(Client client) {
        Optional<Client> clientToModify = getById(client.getId());

        return clientToModify.map(it -> {
            it.setName(client.getName());
            it.setSurname(client.getSurname());
            it.setPesel(client.getPesel());
            return it;
        });
    }
}