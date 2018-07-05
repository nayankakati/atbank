package com.awantunai.responses;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;


@Getter
@Setter
public class AccountSuccessResponse extends Response {

	private Long id;
	private UserSuccessResponse user;
	private AccountType accountType;
	private AccountStatus accountStatus;
	private Date createdOn;
	private BigDecimal balance;
}
