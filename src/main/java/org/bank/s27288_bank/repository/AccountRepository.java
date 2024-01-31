package org.bank.s27288_bank.repository;

import org.bank.s27288_bank.model.account.Account;

import org.springframework.stereotype.Repository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {
    private List<Account> accountList = new ArrayList<>();

    @Setter
    @Getter
    private Integer id = 0;

    public Account create(Account account) {
        accountList.add(account);
        id++;
        return account;
    }
    public Optional<Account> getById(Integer id){
        return accountList
                .stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }
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
            it.setCurrencyType(account.getCurrencyType());
            it.setBalance(account.getBalance());
            return it;
        });
    }
}
