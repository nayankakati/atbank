package com.awantunai.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.entities.User;
import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;
import com.awantunai.enums.TransactionType;
import com.awantunai.exceptions.TransactionException;
import com.awantunai.repositories.TransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceImplTest {
	@Mock
	private TransactionRepository transactionRepository;
	@Mock
	private AccountService accountService;
	@InjectMocks
	private TransactionServiceImpl transactionServiceImpl;

	private BigDecimal amount;
	private Account account;
	private User user;
	private AccountTransactions accountTransactions;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		user = getUser();
		account = getAccount();
	}

	@Test
	public void test_credit_amount_to_account_success() {
		accountTransactions = getAccountTransactions(TransactionType.CREDIT);
		when(accountService.saveAccount(account)).thenReturn(account);
		when(transactionRepository.save(any())).thenReturn(accountTransactions);
		Account actualAccount = transactionServiceImpl.creditAmountToAccount(account, new BigDecimal(100));
		assertEquals(actualAccount.getId(), account.getId());
		assertEquals(actualAccount.getAccountStatus(), account.getAccountStatus());
		assertEquals(actualAccount.getAccountType(), account.getAccountType());
		assertEquals(actualAccount.getBalance(), account.getBalance());
		assertEquals(actualAccount.getUserId(), account.getUserId());
		assertEquals(actualAccount.getCreatedOn(), account.getCreatedOn());
	}

	@Test
	public void test_credit_amount_to_account_failure() {
		accountTransactions = getAccountTransactions(TransactionType.CREDIT);
		when(accountService.saveAccount(account)).thenReturn(account);
		when(transactionRepository.save(any())).thenReturn(accountTransactions);
		assertThatExceptionOfType(TransactionException.class)
			.isThrownBy(() -> transactionServiceImpl.creditAmountToAccount(account, new BigDecimal(-33)))
			.is(new Condition<>(e -> Objects.nonNull(e), "Negative amount cannot be added."));
	}

	@Test
	public void test_debit_amount_to_account_success() {
		accountTransactions = getAccountTransactions(TransactionType.DEBIT);
		when(accountService.saveAccount(account)).thenReturn(account);
		when(transactionRepository.save(any())).thenReturn(accountTransactions);
		Account actualAccount = transactionServiceImpl.debitAmountToAccount(account, new BigDecimal(00));
		assertEquals(actualAccount.getId(), account.getId());
		assertEquals(actualAccount.getAccountStatus(), account.getAccountStatus());
		assertEquals(actualAccount.getAccountType(), account.getAccountType());
		assertEquals(actualAccount.getBalance(), account.getBalance());
		assertEquals(actualAccount.getUserId(), account.getUserId());
		assertEquals(actualAccount.getCreatedOn(), account.getCreatedOn());
	}

	@Test
	public void test_debit_amount_to_account_failure() {
		accountTransactions = getAccountTransactions(TransactionType.DEBIT);
		when(accountService.saveAccount(account)).thenReturn(account);
		when(transactionRepository.save(any())).thenReturn(accountTransactions);
		assertThatExceptionOfType(TransactionException.class)
			.isThrownBy(() -> transactionServiceImpl.debitAmountToAccount(account, new BigDecimal(-55)))
			.is(new Condition<>(e -> Objects.nonNull(e), "Either your account has Insufficient Balance or you entered Negative Amount."));
	}
	@Test
	public void test_transfer_amount_to_account_success() {
		accountTransactions = getAccountTransactions(TransactionType.DEBIT);
		when(accountService.saveAccount(account)).thenReturn(account);
		when(transactionRepository.save(any())).thenReturn(accountTransactions);
		Account actualAccount = transactionServiceImpl.transferAmountToAccount(account, account, new BigDecimal(30));
		assertEquals(actualAccount.getId(), account.getId());
		assertEquals(actualAccount.getAccountStatus(), account.getAccountStatus());
		assertEquals(actualAccount.getAccountType(), account.getAccountType());
		assertEquals(actualAccount.getBalance(), account.getBalance());
		assertEquals(actualAccount.getUserId(), account.getUserId());
		assertEquals(actualAccount.getCreatedOn(), account.getCreatedOn());
	}

	@Test
	public void test_transfer_amount_to_account_failure() {
		accountTransactions = getAccountTransactions(TransactionType.DEBIT);
		when(accountService.saveAccount(account)).thenReturn(account);
		when(transactionRepository.save(any())).thenReturn(accountTransactions);
		assertThatExceptionOfType(TransactionException.class)
			.isThrownBy(() -> transactionServiceImpl.transferAmountToAccount(account, account, new BigDecimal(-55)))
			.is(new Condition<>(e -> Objects.nonNull(e), "Either your account has Insufficient Balance or you entered Negative Amount."));
	}

	@Test
	public void test_transaction_list_success() {
		List<AccountTransactions> accountTransactionss = Arrays.asList(getAccountTransactions(TransactionType.CREDIT));
		when(transactionRepository.listTransactions(any(), anyLong(), anyLong(), anyInt())).thenReturn(accountTransactionss);
		List<AccountTransactions> actualAccountTransaction = transactionServiceImpl.listTransactions(account, 123l, 123l, 10);
		assertEquals(accountTransactionss.size(), actualAccountTransaction.size());
		assertEquals(accountTransactionss.get(0).getBody(), actualAccountTransaction.get(0).getBody());
		assertEquals(accountTransactionss.get(0).getAccountId(), actualAccountTransaction.get(0).getAccountId());
		assertEquals(accountTransactionss.get(0).getTransactionOn(), actualAccountTransaction.get(0).getTransactionOn());
		assertEquals(accountTransactionss.get(0).getTransactionType(), actualAccountTransaction.get(0).getTransactionType());
	}

	@Test
	public void test_save_account_transactions_success() {
		accountTransactions = getAccountTransactions(TransactionType.CREDIT);
		when(transactionRepository.save(accountTransactions)).thenReturn(accountTransactions);
		AccountTransactions actualAccountTransactions = transactionServiceImpl.saveAccountTransaction(accountTransactions);
		assertEquals(accountTransactions.getBody(), actualAccountTransactions.getBody());
		assertEquals(accountTransactions.getAccountId(), actualAccountTransactions.getAccountId());
		assertEquals(accountTransactions.getTransactionOn(), actualAccountTransactions.getTransactionOn());
		assertEquals(accountTransactions.getTransactionType(), actualAccountTransactions.getTransactionType());
	}

	private User getUser() {
		return User.builder().id(1l).username("abc").password("qwerty").build();
	}

	private Account getAccount() {
		return Account.builder().id(1l).userId(getUser()).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
	}

	private AccountTransactions getAccountTransactions(TransactionType transactionType) {
		return AccountTransactions.builder().accountId(account).body("body").transactionOn(new Date(1530391143000l)).transactionType(transactionType).build();
	}
}
