package com.awantunai.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.lang.NonNull;

import com.awantunai.enums.AccountStatus;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {
	@NonNull
	private AccountStatus accountStatus;
	@NonNull
	private Long accountId;
}
