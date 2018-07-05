package com.awantunai.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.enums.TransactionType;
import com.awantunai.exceptions.TransactionException;
import com.awantunai.repositories.TransactionRepository;


@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private AccountService accountService;

	private BigDecimal zero = new BigDecimal(0);

	public Account creditAmountToAccount(Account account, BigDecimal amount) throws TransactionException {
			if (amount.compareTo(zero) != 1) {
				throw new TransactionException("Negative amount cannot be added.");
			}
			StringBuffer buffer = new StringBuffer();
			BigDecimal balance = account.getBalance();
			balance = balance.add(amount);
			account.setBalance(balance);
			account = accountService.saveAccount(account);
			// Create a transaction report for this credit line
			buffer.append("The amount ").append(amount).append(" was added to the account id ").append(account.getId());
			AccountTransactions transactions = getAccountTransaction(buffer.toString(), account, TransactionType.CREDIT);
			transactionRepository.save(transactions);
			return account;
	}

	public Account debitAmountToAccount(Account account, BigDecimal amount) throws TransactionException {
		if ((zero.compareTo(amount) == 1) || (account.getBalance().compareTo(amount) == -1)) {
			throw new TransactionException("Either your account has Insufficient Balance or you entered Negative Amount.");
		}
		StringBuffer buffer = new StringBuffer();
		BigDecimal balance = account.getBalance();
		balance = balance.subtract(amount);
		account.setBalance(balance);
		accountService.saveAccount(account);
		// Create a transaction report for this credit line
		buffer.append("The amount ").append(amount).append(" was debited  from the account id ").append(account.getId());
		AccountTransactions transactions = getAccountTransaction(buffer.toString(), account, TransactionType.DEBIT);
		transactionRepository.save(transactions);
		return account;
	}

	public Account transferAmountToAccount(Account fromAccount, Account toAccount, BigDecimal amount) throws TransactionException {
		debitAmountToAccount(fromAccount, amount);
		creditAmountToAccount(toAccount, amount);
		return fromAccount;
	}

	public List<AccountTransactions> listTransactions(Account account, Long from, Long to, Integer size) throws TransactionException {
		return transactionRepository.listTransactions(account, from, to, size);
	}

	public AccountTransactions saveAccountTransaction(AccountTransactions accountTransactions) throws TransactionException {
		return transactionRepository.save(accountTransactions);
	}

	private AccountTransactions getAccountTransaction(String body, Account account, TransactionType transactionType) {
		return AccountTransactions.builder().accountId(account).body(body).transactionOn(new Date(System.currentTimeMillis())).transactionType(transactionType).build();
	}
}
