package com.awantunai.integration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONParser;
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
import com.awantunai.entities.User;
import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;
import com.awantunai.repositories.AccountRepository;
import com.awantunai.repositories.TransactionRepository;
import com.awantunai.repositories.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AtbankApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class AccountControllerIT {

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

	public void init() {
		user = User.builder().username("abc99709213").password("def").build();
		userRepository.save(user);
		account = getAccount();
	}

	@Test
	public void test_create_account_success() throws Exception {
		init();
		Map payload = new HashMap();
		payload.put("userId", user.getId());
		payload.put("amount", "300");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/accounts"),
			HttpMethod.POST, entity, String.class);

		JSONObject json = (JSONObject) JSONParser.parseJSON(response.getBody());
		Account account = Account.builder().id(new Long((Integer)json.get("id"))).build();
		transactionRepository.deleteAccountTransactionByAccountId(account);
		accountRepository.delete(account);
		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void test_update_account_success() {
		init();
		accountRepository.save(account);
		Map payload = new HashMap();
		payload.put("accountId", account.getId());
		payload.put("accountStatus", "CLOSED");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/accounts"),
			HttpMethod.PUT, entity, String.class);

		Assert.assertNotNull(response.getBody());
		accountRepository.delete(account);
	}

	private Account getAccount() {
		return Account.builder().userId(user).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
	}

	@After
	public void tearDown() {
		userRepository.delete(user);
	}
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
