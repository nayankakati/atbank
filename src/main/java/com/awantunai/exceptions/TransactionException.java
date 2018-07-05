package com.awantunai.exceptions;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransactionException extends RuntimeException {
	private String message;

	public TransactionException(String message) {
		super(message);
		this.message = message;
	}
}
