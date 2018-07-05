package com.awantunai.controllers;

import static com.awantunai.helpers.ResponseBuilder.createAccountListSuccessResponse;
import static com.awantunai.helpers.ResponseBuilder.createAccountSuccessResponse;
import static com.awantunai.helpers.ResponseBuilder.createErrorListResponse;
import static com.awantunai.helpers.ResponseBuilder.createErrorResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.awantunai.entities.Account;
import com.awantunai.entities.User;
import com.awantunai.exceptions.AccountException;
import com.awantunai.exceptions.UserException;
import com.awantunai.requests.AccountRequest;
import com.awantunai.requests.AccountUpdateRequest;
import com.awantunai.responses.Response;
import com.awantunai.services.AccountService;
import com.awantunai.services.UserService;

@RestController
@RequestMapping("/api")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@PostMapping("/accounts")
	public Response createAccount(@RequestBody AccountRequest accountRequest) {
		try {
			User user = userService.findUser(accountRequest.getUserId());
			Account account = accountService.createAccount(user, accountRequest.getAmount());
			return createAccountSuccessResponse(account);
		} catch (AccountException | UserException exception) {
			return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}

	@PutMapping("/accounts")
	public Response updateAccount(@RequestBody AccountUpdateRequest accountRequest) {
		try {
			Account account = accountService.updateAccount(accountRequest);
			return createAccountSuccessResponse(account);
		} catch (AccountException exception) {
			return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}

	@GetMapping("/accounts/{accountId}")
	public List<?> getAccount(@PathVariable Long accountId) {
		try {
			Account account = accountService.getAccount(accountId);
			return createAccountListSuccessResponse(account);
		} catch (AccountException exception) {
			return createErrorListResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}

}
