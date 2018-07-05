package com.awantunai.integration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.awantunai.AtbankApplication;
import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.entities.User;
import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;
import com.awantunai.enums.TransactionType;
import com.awantunai.repositories.AccountRepository;
import com.awantunai.repositories.TransactionRepository;
import com.awantunai.repositories.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AtbankApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class TransactionControllerIT {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@LocalServerPort
	private int port;
	private TestRestTemplate restTemplate = new TestRestTemplate();
	private HttpHeaders headers = new HttpHeaders();
	private Account account;
	private User user;
	private AccountTransactions accountTransactions;

	public void init() {
		user = getUser();
		userRepository.save(user);
		account = getAccount();
		accountRepository.save(account);
		accountTransactions = getAccountTransactions(TransactionType.CREDIT);
	}

	@Test
	public void test_credit_to_account_success() {
		init();
		Map payload = new HashMap();
		payload.put("accountId", account.getId());
		payload.put("amount", "300");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/transactions/credit"),
			HttpMethod.POST, entity, String.class);

		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void test_debit_to_account_success() {
		init();
		Map payload = new HashMap();
		payload.put("accountId", account.getId());
		payload.put("amount", "100");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/transactions/debit"),
			HttpMethod.POST, entity, String.class);

		Assert.assertNotNull(response.getBody());
	}


	@Test
	public void test_transfer_to_account_success() {
		init();
		User toUser = User.builder().username("abc99709213").password("qwerty").build();
		userRepository.save(toUser);
		Account toAccount = Account.builder().userId(toUser).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
		accountRepository.save(toAccount);

		Map payload = new HashMap();
		payload.put("accountId", account.getId());
		payload.put("toAccountId", toAccount.getId());
		payload.put("amount", "50");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/transactions/transfer"),
			HttpMethod.POST, entity, String.class);

		Assert.assertNotNull(response.getBody());
		transactionRepository.deleteAccountTransactionByAccountId(toAccount);
		accountRepository.delete(toAccount);
		userRepository.delete(toUser);
	}

	@Test
	public void test_list_transactions_for_account_success() {
		init();
		transactionRepository.save(accountTransactions);
		Map payload = new HashMap();

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/transactions/accounts/"+ account.getId()),
			HttpMethod.GET, entity, String.class);

		Assert.assertNotNull(response.getBody());
	}


	@After
	public void tearDown() {
		transactionRepository.deleteAccountTransactionByAccountId(account);
		accountRepository.delete(account);
		userRepository.delete(user);
	}

	private Account getAccount() {
		return Account.builder().userId(user).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
	}
	private User getUser() {
		return User.builder().username("abc9112390").password("qwerty").build();
	}

	private AccountTransactions getAccountTransactions(TransactionType transactionType) {
		return AccountTransactions.builder().accountId(account).body("body").transactionOn(new Date(1530391143000l)).transactionType(transactionType).build();
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
