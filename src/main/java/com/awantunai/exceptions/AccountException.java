package com.awantunai.exceptions;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountException extends RuntimeException {
	private String message;

	public AccountException(String message) {
		super(message);
		this.message = message;
	}
}
