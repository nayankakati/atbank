package com.awantunai.controllers;

import static com.awantunai.helpers.ResponseBuilder.createAccountSuccessResponse;
import static com.awantunai.helpers.ResponseBuilder.createErrorResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.awantunai.entities.Account;
import com.awantunai.exceptions.UserException;
import com.awantunai.requests.UserRequest;
import com.awantunai.responses.Response;
import com.awantunai.services.UserService;

@RestController
@ComponentScan
public class LoginController {
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public Response login(@RequestBody UserRequest user) {
		try {
			Account account = userService.loginUser(user);
			return createAccountSuccessResponse(account);
		} catch (UserException exception) {
			return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}

}
