package com.awantunai.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.entities.User;
import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;
import com.awantunai.enums.TransactionType;
import com.awantunai.exceptions.AccountException;
import com.awantunai.exceptions.UserException;
import com.awantunai.repositories.AccountRepository;
import com.awantunai.requests.AccountUpdateRequest;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceImplTest {
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private TransactionService transactionService;
	@InjectMocks
	private AccountServiceImpl accountServiceImpl;
	private Account account;
	private User user;

	@Before
	public void init() {
		user = getUser();
		account = getAccount();
	}

	@Test
	public void test_create_account_success() {
		when(accountRepository.findAccountByUserId(user)).thenReturn(null);
		when(accountRepository.save(any())).thenReturn(account);
		when(transactionService.saveAccountTransaction(any())).thenReturn(getAccountTransactions(TransactionType.CREDIT));
		Account actualAccount = accountServiceImpl.createAccount(user, new BigDecimal(50));
		assertEquals(actualAccount.getId(), actualAccount.getId());
		assertEquals(actualAccount.getUserId(), actualAccount.getUserId());
		assertEquals(actualAccount.getCreatedOn(), actualAccount.getCreatedOn());
		assertEquals(actualAccount.getBalance(), actualAccount.getBalance());
		assertEquals(actualAccount.getAccountType(), actualAccount.getAccountType());
		assertEquals(actualAccount.getAccountStatus(), actualAccount.getAccountStatus());
	}

	@Test
	public void test_create_account_failure() throws UserException {
		when(accountRepository.findAccountByUserId(user)).thenReturn(account);
		assertThatExceptionOfType(AccountException.class)
			.isThrownBy(() -> accountServiceImpl.createAccount(user, new BigDecimal(0)))
			.is(new Condition<>(e -> Objects.nonNull(e), "The Account with user id " + user.getId() + " is already exits."));
	}

	@Test
	public void test_update_account_success() {
		AccountUpdateRequest accountUpdateRequest = AccountUpdateRequest.builder().accountId(account.getId()).accountStatus(AccountStatus.CLOSED).build();

		when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
		when(accountRepository.save(any())).thenReturn(account);
		Account actualAccount = accountServiceImpl.updateAccount(accountUpdateRequest);
		assertEquals(actualAccount.getId(), actualAccount.getId());
		assertEquals(actualAccount.getUserId(), actualAccount.getUserId());
		assertEquals(actualAccount.getCreatedOn(), actualAccount.getCreatedOn());
		assertEquals(actualAccount.getBalance(), actualAccount.getBalance());
		assertEquals(actualAccount.getAccountType(), actualAccount.getAccountType());
		assertEquals(actualAccount.getAccountStatus(), actualAccount.getAccountStatus());
	}

	@Test
	public void test_update_account_failure() throws UserException {
		AccountUpdateRequest accountUpdateRequest = AccountUpdateRequest.builder().accountId(account.getId()).accountStatus(AccountStatus.CLOSED).build();
		when(accountRepository.findById(user.getId())).thenReturn(Optional.empty());
		assertThatExceptionOfType(AccountException.class)
			.isThrownBy(() -> accountServiceImpl.updateAccount(accountUpdateRequest))
			.is(new Condition<>(e -> Objects.nonNull(e), "The Account with id " + account.getId() + " does not exits."));
	}

	@Test
	public void test_find_account_success() {
		when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
		when(accountRepository.save(any())).thenReturn(account);
		Account actualAccount = accountServiceImpl.findAccount(account.getId());
		assertEquals(actualAccount.getId(), actualAccount.getId());
		assertEquals(actualAccount.getUserId(), actualAccount.getUserId());
		assertEquals(actualAccount.getCreatedOn(), actualAccount.getCreatedOn());
		assertEquals(actualAccount.getBalance(), actualAccount.getBalance());
		assertEquals(actualAccount.getAccountType(), actualAccount.getAccountType());
		assertEquals(actualAccount.getAccountStatus(), actualAccount.getAccountStatus());
	}

	@Test
	public void test_find_account_failure() throws UserException {
		when(accountRepository.findById(user.getId())).thenReturn(Optional.empty());
		assertThatExceptionOfType(AccountException.class)
			.isThrownBy(() -> accountServiceImpl.findAccount(account.getId()))
			.is(new Condition<>(e -> Objects.nonNull(e), "The Account with id " + account.getId() + " does not exits."));
	}

	@Test
	public void test_save_account_success() throws UserException {
		when(accountRepository.saveAndFlush(account)).thenReturn(account);
		Account actualAccount = accountServiceImpl.saveAccount(account);
		assertEquals(actualAccount.getId(), actualAccount.getId());
		assertEquals(actualAccount.getUserId(), actualAccount.getUserId());
		assertEquals(actualAccount.getCreatedOn(), actualAccount.getCreatedOn());
		assertEquals(actualAccount.getBalance(), actualAccount.getBalance());
		assertEquals(actualAccount.getAccountType(), actualAccount.getAccountType());
		assertEquals(actualAccount.getAccountStatus(), actualAccount.getAccountStatus());
	}

	@Test
	public void test_get_account_success() {
		when(accountRepository.findById(any())).thenReturn(Optional.of(account));
		Account actualAccount = accountServiceImpl.getAccount(account.getId());
		assertEquals(actualAccount.getId(), actualAccount.getId());
		assertEquals(actualAccount.getUserId(), actualAccount.getUserId());
		assertEquals(actualAccount.getCreatedOn(), actualAccount.getCreatedOn());
		assertEquals(actualAccount.getBalance(), actualAccount.getBalance());
		assertEquals(actualAccount.getAccountType(), actualAccount.getAccountType());
		assertEquals(actualAccount.getAccountStatus(), actualAccount.getAccountStatus());
	}

	@Test
	public void test_get_account_failure() throws UserException {
		when(accountRepository.findById(any())).thenReturn(Optional.empty());
		assertThatExceptionOfType(AccountException.class)
			.isThrownBy(() -> accountServiceImpl.getAccount(account.getId()))
			.is(new Condition<>(e -> Objects.nonNull(e), "The Account with " + account.getId() + " does not exits."));
	}

	private Account getAccount() {
		return Account.builder().id(1l).userId(getUser()).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
	}
	private User getUser() {
		return User.builder().id(2l).username("abc").password("qwerty").build();
	}

	private AccountTransactions getAccountTransactions(TransactionType transactionType) {
		return AccountTransactions.builder().accountId(account).body("body").transactionOn(new Date(1530391143000l)).transactionType(transactionType).build();
	}
}
