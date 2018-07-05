package com.awantunai.controllers;

import static com.awantunai.helpers.ResponseBuilder.createErrorResponse;
import static com.awantunai.helpers.ResponseBuilder.createUserSuccessResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.awantunai.entities.User;
import com.awantunai.exceptions.UserException;
import com.awantunai.requests.UserRequest;
import com.awantunai.responses.Response;
import com.awantunai.services.UserService;


@RestController
@ComponentScan
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/users")
	public Response createUser(@RequestBody UserRequest user) {
		try {
			User savedUser = userService.createUser(user);
			return createUserSuccessResponse(savedUser);
		} catch (UserException exception) {
			return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}

	@PutMapping("/users")
	public Response updateUser(@RequestBody UserRequest user) {
		try {
			if(user.getPassword() == null)
				return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), new UserException("Password is mandatory."));

			User updatedUser = userService.updateUser(user);
			return createUserSuccessResponse(updatedUser);
		} catch (UserException exception) {
			return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}

	@DeleteMapping("/users")
	public Response deleteUser(@RequestBody UserRequest user) {
		try {
			User updatedUser = userService.deleteUser(user);
			return createUserSuccessResponse(updatedUser);
		} catch (UserException exception) {
			return createErrorResponse(HttpStatus.PRECONDITION_FAILED.name(), exception);
		}
	}
}
