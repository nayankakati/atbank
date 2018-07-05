package com.awantunai.responses;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends Response {

	String message;
	String code;

	public ErrorResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
