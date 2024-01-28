package org.bank.s27288_bank.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Client {
    private Integer Id;
    private String name;
    private String surname;
    private String pesel;
}
