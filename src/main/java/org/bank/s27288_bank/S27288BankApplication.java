package org.bank.s27288_bank;

import org.bank.s27288_bank.model.account.Account;
import org.bank.s27288_bank.repository.AccountRepository;
import org.bank.s27288_bank.service.AccountService;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.bank.s27288_bank.model.account.CurrencyType.*;


@SpringBootApplication
public class S27288BankApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(S27288BankApplication.class);

		AccountService accountService = context.getBean(AccountService.class);

		accountService.create("Jan", "Kowalski", "12345678901", PLN, 100.0);
		accountService.create("Piotr", "Nowak", "12345678902", EUR, 200.0);
		accountService.create("Anna", "Kowalska", "12345678903", USD, 300.0);

		List<Account> allAccounts = accountService.getAllAccounts();
		System.out.println(allAccounts);

		AccountRepository accountRepository = context.getBean("accountRepository", AccountRepository.class);
		Optional<Account> accountById = accountRepository.getById(0);
		System.out.println(accountById);

		List<Account> accountByBalance = accountRepository.getByBalance(150.0);

		System.out.println(accountByBalance);
	}
}
