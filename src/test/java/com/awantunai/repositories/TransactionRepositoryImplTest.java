package com.awantunai.repositories;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.entities.User;
import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;
import com.awantunai.enums.TransactionType;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryImplTest {
	@Autowired
	private TransactionRepositoryImpl transactionRepositoryImpl;
	@Autowired
	private TestEntityManager testEntityManager;

	private User user;
	private Account account;
	private AccountTransactions accountTransactions;

	@Test
	public void test_transaction_list() {
		user = getUser();
		account = getAccount();
		accountTransactions = getAccountTransactions(TransactionType.CREDIT);

		testEntityManager.persist(user);
		testEntityManager.persist(account);
		testEntityManager.persist(accountTransactions);

		List<AccountTransactions> accountTransactionss = transactionRepositoryImpl.listTransactions(account, 1530391143000l, 1530391143000l, 10 );
		assertNotNull(accountTransactionss);

		testEntityManager.remove(user);
		testEntityManager.remove(account);
		testEntityManager.remove(accountTransactions);
	}

	private Account getAccount() {
		return Account.builder().userId(user).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
	}
	private User getUser() {
		return User.builder().username("abc").password("qwerty").build();
	}

	private AccountTransactions getAccountTransactions(TransactionType transactionType) {
		return AccountTransactions.builder().accountId(account).body("body").transactionOn(new Date(1530391143000l)).transactionType(transactionType).build();
	}
}
