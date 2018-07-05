package com.awantunai.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.entities.User;
import com.awantunai.responses.AccountSuccessResponse;
import com.awantunai.responses.AccountTransactionSuccessResponse;
import com.awantunai.responses.ErrorResponse;
import com.awantunai.responses.UserSuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseBuilder {

	public static UserSuccessResponse createUserSuccessResponse(User user) {
		UserSuccessResponse successResponse = new UserSuccessResponse();
		successResponse.setId(user.getId());
		successResponse.setUsername(user.getUsername());
		return successResponse;
	}

	public static ErrorResponse createErrorResponse(String code, Exception exception) {
		return new ErrorResponse(code, exception.getMessage());
	}

	public static List<ErrorResponse> createErrorListResponse(String code, Exception exception) {
		return Arrays.asList(new ErrorResponse(code, exception.getMessage()));
	}

	public static AccountSuccessResponse createAccountSuccessResponse(Account account) {
		AccountSuccessResponse accountSuccessResponse = new AccountSuccessResponse();
		accountSuccessResponse.setUser(createUserSuccessResponse(account.getUserId()));
		accountSuccessResponse.setId(account.getId());
		accountSuccessResponse.setAccountStatus(account.getAccountStatus());
		accountSuccessResponse.setAccountType(account.getAccountType());
		accountSuccessResponse.setCreatedOn(account.getCreatedOn());
		accountSuccessResponse.setBalance(account.getBalance());
		return accountSuccessResponse;
	}

	public static List<AccountSuccessResponse> createAccountListSuccessResponse(Account account) {
		AccountSuccessResponse accountSuccessResponse = new AccountSuccessResponse();
		accountSuccessResponse.setUser(createUserSuccessResponse(account.getUserId()));
		accountSuccessResponse.setId(account.getId());
		accountSuccessResponse.setAccountStatus(account.getAccountStatus());
		accountSuccessResponse.setAccountType(account.getAccountType());
		accountSuccessResponse.setCreatedOn(account.getCreatedOn());
		accountSuccessResponse.setBalance(account.getBalance());

		return Arrays.asList(accountSuccessResponse);
	}

	public static List<AccountTransactionSuccessResponse> createAccountTransactionSucessResponse(List<AccountTransactions> accountTransactions) {
		List<AccountTransactionSuccessResponse> transactionSuccessResponses = new ArrayList<>();
		accountTransactions.forEach(accountTransaction -> {
			AccountTransactionSuccessResponse accountTransactionSuccessResponse = new AccountTransactionSuccessResponse();
			accountTransactionSuccessResponse.setBody(accountTransaction.getBody());
			accountTransactionSuccessResponse.setTransactionType(accountTransaction.getTransactionType());
			accountTransactionSuccessResponse.setAccountId(accountTransaction.getAccountId().getId());
			accountTransactionSuccessResponse.setId(accountTransaction.getId());
			accountTransactionSuccessResponse.setTransactionOn(accountTransaction.getTransactionOn());
			transactionSuccessResponses.add(accountTransactionSuccessResponse);
		});
		return transactionSuccessResponses;
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
