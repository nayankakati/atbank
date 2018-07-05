package com.awantunai.controllers;

import static com.awantunai.helpers.ResponseBuilder.createAccountSuccessResponse;
import static com.awantunai.helpers.ResponseBuilder.createAccountTransactionSucessResponse;
import static com.awantunai.helpers.ResponseBuilder.createErrorListResponse;
import static com.awantunai.helpers.ResponseBuilder.createErrorResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.exceptions.AccountException;
import com.awantunai.exceptions.TransactionException;
import com.awantunai.requests.TransactionRequest;
import com.awantunai.responses.Response;
import com.awantunai.services.AccountService;
import com.awantunai.services.TransactionService;


@RestController
@RequestMapping("/api")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private AccountService accountService;

	@PostMapping("/transactions/credit")
	public Response creditAmountToAccount(@RequestBody TransactionRequest transactionRequest) {
		try {
			Account account = accountService.findAccount(transactionRequest.getAccountId());
			Account updatedAccount = transactionService.creditAmountToAccount(account, transactionRequest.getAmount());
			return createAccountSuccessResponse(updatedAccount);
		} catch (AccountException | TransactionException exception) {
			return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}

	@PostMapping("/transactions/debit")
	public Response debitAmountToAccount(@RequestBody TransactionRequest transactionRequest) {
		try {
			Account account = accountService.findAccount(transactionRequest.getAccountId());
			Account updatedAccount = transactionService.debitAmountToAccount(account, transactionRequest.getAmount());
			return createAccountSuccessResponse(updatedAccount);
		} catch (AccountException | TransactionException exception) {
			return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}

	@PostMapping("/transactions/transfer")
	public Response transferAmountToAccount(@RequestBody TransactionRequest transactionRequest) {
		try {
			if(transactionRequest.getToAccountId() == null)
				return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), new AccountException("To account number is mandatory."));

			Account fromAccount = accountService.findAccount(transactionRequest.getAccountId());
			Account toAccount = accountService.findAccount(transactionRequest.getToAccountId());
			Account updatedAccount = transactionService.transferAmountToAccount(fromAccount, toAccount, transactionRequest.getAmount());
			return createAccountSuccessResponse(updatedAccount);
		} catch (AccountException | TransactionException exception) {
			return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}

	@GetMapping("/transactions/accounts/{accountId}")
	public List<?> listAllTheTransactionForAnAccountId(@PathVariable Long accountId, @RequestParam(required = false) @NonNull Long from, @RequestParam(required = false) @NonNull Long to, @RequestParam(required = false) @NonNull Integer size) {
		try {
			Account account = accountService.findAccount(accountId);
			List<AccountTransactions> accountTransactionss = transactionService.listTransactions(account, from, to, size);
			return createAccountTransactionSucessResponse(accountTransactionss);
		} catch (AccountException | TransactionException exception) {
			return createErrorListResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}
}
