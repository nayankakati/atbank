package com.awantunai.repositories;

import java.util.List;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.exceptions.TransactionException;


public interface TransactionRepositoryCustom {
	List<AccountTransactions> listTransactions(Account accountId, Long from, Long to, Integer size) throws TransactionException;

}
