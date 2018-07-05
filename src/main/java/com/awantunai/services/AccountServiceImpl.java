package com.awantunai.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.entities.User;
import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;
import com.awantunai.enums.TransactionType;
import com.awantunai.exceptions.AccountException;
import com.awantunai.repositories.AccountRepository;
import com.awantunai.requests.AccountUpdateRequest;


@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private TransactionService transactionService;

	public Account createAccount(User user, BigDecimal amount) throws AccountException {
		Account existingAccount  = accountRepository.findAccountByUserId(user);
		if(existingAccount != null)
			throw new AccountException("The Account with  " + user.getId() + " is already exits.");
		existingAccount = createNewAccount(user, amount);
		accountRepository.save(existingAccount);
		transactionService.saveAccountTransaction(createAccountTransactions(existingAccount));
		return existingAccount;
	}

	public Account updateAccount(AccountUpdateRequest accountRequest) throws AccountException {
		Optional<Account> accountToUpdate  = accountRepository.findById(accountRequest.getAccountId());

		if(accountToUpdate.orElse(null) == null)
			throw new AccountException("The Account with id " + accountRequest.getAccountId() + " does not exists.");

		accountToUpdate.get().setAccountStatus((accountRequest.getAccountStatus() != null) ? accountRequest.getAccountStatus(): accountToUpdate.get().getAccountStatus());
		accountRepository.save(accountToUpdate.get());
		return accountToUpdate.get();
	}

	public Account findAccount(Long accountId) throws  AccountException {
		Optional<Account> account =  accountRepository.findById(accountId);

		if(account.orElse(null) == null || !(account.get().getAccountStatus().equals(AccountStatus.ACTIVE)))
			throw new AccountException("The Account with id " + accountId + " does not exists.");

		return account.get();
	}

	public Account saveAccount(Account account) throws AccountException {
		return accountRepository.saveAndFlush(account);
	}

	public Account getAccount(Long accountId) throws AccountException {
		Optional<Account> existingAccount  = accountRepository.findById(accountId);
		if(existingAccount.orElse(null) == null)
			throw new AccountException("The Account with  " + accountId + " does not exits.");

		return existingAccount.get();
	}


	private Account createNewAccount(User user, BigDecimal amount) {
		return Account.builder().userId(user).balance(amount).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(System.currentTimeMillis())).build();
	}

	private AccountTransactions createAccountTransactions(Account account) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("A new Account has been created with id ").append(account.getId()).append(" and amount with ").append(account.getBalance());

		return AccountTransactions.builder().accountId(account).body(buffer.toString()).transactionOn(new Date(System.currentTimeMillis())).transactionType(TransactionType.CREDIT).build();
	}
}
