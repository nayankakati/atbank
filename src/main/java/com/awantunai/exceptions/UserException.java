package com.awantunai.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserException extends Exception {
	private String message;

	public UserException(String message) {
		super(message);
		this.message = message;
	}
}
