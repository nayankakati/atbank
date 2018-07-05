package com.awantunai.services;

import java.math.BigDecimal;
import java.util.List;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.exceptions.TransactionException;


public interface TransactionService {
	Account creditAmountToAccount(Account account, BigDecimal amount) throws TransactionException;
	Account debitAmountToAccount(Account account, BigDecimal amount) throws TransactionException;
	Account transferAmountToAccount(Account fromAccount, Account toAccount, BigDecimal amount) throws TransactionException;
	List<AccountTransactions> listTransactions(Account account, Long from, Long to, Integer size) throws TransactionException;
	AccountTransactions saveAccountTransaction(AccountTransactions accountTransactions) throws TransactionException;
}
