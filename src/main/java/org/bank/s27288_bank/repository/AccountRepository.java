package org.bank.s27288_bank.repository;


import org.bank.s27288_bank.model.account.Account;
import org.bank.s27288_bank.model.client.Client;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {
    private List<Account> accountList = new ArrayList<>();

    public Account create(Account account) {
        account.setId(accountList.size());
        accountList.add(account);
        return account;
    }
    public Optional<Account> getById(Integer id){
        return accountList
                .stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }
    //create code for getting all acount with balance bigger than 100
    public List<Account> getByBalance(double balance){
        return accountList
                .stream()
                .filter(it -> it.getBalance() > balance)
                .toList();
    }
    public List<Account> getAll(){
        return accountList;
    }
    public void removeByID(Integer id){
        Optional<Account> account = getById(id);
        account.ifPresent(it -> accountList.remove(it));
    }
    public void removeAll(){
        accountList = new ArrayList<>();
    }

    public Optional<Account> modify(Account account) {
        Optional<Account> accountToModify = getById(account.getId());

        return accountToModify.map(it -> {
            it.setClientId(account.getClientId());
            it.setCurrency(account.getCurrency());
            it.setBalance(account.getBalance());
            return it;
        });
    }
}
