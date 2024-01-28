package org.bank.s27288_bank;

import org.bank.s27288_bank.model.account.Account;
import org.bank.s27288_bank.model.account.CurrencyType;
import static org.bank.s27288_bank.model.account.CurrencyType.*;
import org.bank.s27288_bank.model.client.Client;
import org.bank.s27288_bank.repository.AccountRepository;
import org.bank.s27288_bank.repository.ClientRepository;
import org.bank.s27288_bank.service.AccountService;
import org.bank.s27288_bank.service.ClientService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;


@SpringBootApplication
public class S27288BankApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(S27288BankApplication.class);

		ClientService clientService = context.getBean(ClientService.class);
		AccountService accountService = context.getBean(AccountService.class);

		Client client1 = new Client(null, "Jan", "Kowalski", "12345678901");
		Client client2 = new Client(null, "Adam", "Nowak", "12345678902");
		Client client3 = new Client(null, "Pawel", "Nowak", "12345678903");

		clientService.create(client1);
		clientService.create(client2);
		clientService.create(client3);

		Account account1 = new Account(null, 0, PLN, 100.0, ClientRepository.getName(0), ClientRepository.getSurname(0), ClientRepository.getPesel(0));
		Account account2 = new Account(null, 1, EUR, 200.0, ClientRepository.getName(1), ClientRepository.getSurname(1), ClientRepository.getPesel(1));
		Account account3 = new Account(null, 2, PLN, 300.0, ClientRepository.getName(2), ClientRepository.getSurname(2), ClientRepository.getPesel(2));
		Account account4 = new Account(null, 0, PLN, 400.0, ClientRepository.getName(0), ClientRepository.getSurname(0), ClientRepository.getPesel(0));

		accountService.create(account1);
		accountService.create(account2);
		accountService.create(account3);
		accountService.create(account4);

		List<Client> allClients = clientService.getAllClients();
		System.out.println(allClients);

		List<Account> allAccounts = accountService.getAllAccounts();
		System.out.println(allAccounts);

		ClientRepository clientRepository = context.getBean("clientRepository", ClientRepository.class);
		Optional<Client> clientById = clientRepository.getById(0);

		System.out.println(clientById);

		AccountRepository accountRepository = context.getBean("accountRepository", AccountRepository.class);
		Optional<Account> accountById = accountRepository.getById(0);
		System.out.println(accountById);

		List<Account> accountByBalance = accountRepository.getByBalance(100.0);

		System.out.println(accountByBalance);
	}
}
