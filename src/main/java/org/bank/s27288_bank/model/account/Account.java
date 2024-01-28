package org.bank.s27288_bank.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {
    private Integer id;
    private Integer clientId;
    private CurrencyType currency;
    private Double balance;
    private String name;
    private String surname;
    private String pesel;
}
