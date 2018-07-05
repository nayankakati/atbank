package com.awantunai.responses;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserSuccessResponse extends Response {

	private Long id;
	private String username;
}
