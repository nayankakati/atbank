package com.awantunai.services;

import java.math.BigDecimal;

import com.awantunai.entities.Account;
import com.awantunai.entities.User;
import com.awantunai.exceptions.AccountException;
import com.awantunai.requests.AccountUpdateRequest;


public interface AccountService {
	Account createAccount(User user, BigDecimal amount) throws AccountException;
	Account updateAccount(AccountUpdateRequest accountRequest) throws AccountException;
	Account findAccount(Long accountId) throws AccountException;
	Account saveAccount(Account account) throws AccountException;
	Account getAccount(Long accountId) throws AccountException;
}
