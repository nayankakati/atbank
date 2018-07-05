package com.awantunai.responses;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.awantunai.enums.TransactionType;


@Getter
@Setter
public class AccountTransactionSuccessResponse extends Response {
	private Long id;
	private Long accountId;
	private String body;
	private TransactionType transactionType;
	private Date transactionOn;
}
